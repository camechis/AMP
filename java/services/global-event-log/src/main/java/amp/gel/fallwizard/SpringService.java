package amp.gel.fallwizard;

import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.berico.fallwizard.SpringConfiguration;
import com.berico.fallwizard.auth.SpringSecurityAuthProvider;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;

public abstract class SpringService<T extends SpringConfiguration> extends
		Service<T> {
	private static final Logger logger = LoggerFactory
			.getLogger(SpringService.class);

	protected ApplicationContext applicationContext;

	@Override
	public void initialize(Bootstrap<T> bootstrap) {
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {

		logger.info("Starting up SpringService");
		logger.info("Using configurations: {}",
				join(configuration.getApplicationContext(), ", "));

		ApplicationContext parentCtx = this.initSpringParent();

		Dropwizard dw = (Dropwizard) parentCtx.getBean("dropwizard");
		dw.setConfiguration(configuration);
		dw.setEnvironment(environment);

		// Instantiate the Spring Application Context
		applicationContext = new FileSystemXmlApplicationContext(
				configuration.getApplicationContext(), true, parentCtx);

		// If we should use Spring Security
		if (configuration.shouldUseSpringSecurity()) {

			// Register the Spring Security Auth Provider
			new SpringSecurityAuthProvider(applicationContext)
					.registerProvider(environment);
		}

		registerManaged(environment);
		registerLifecycle(environment);
		registerTasks(environment);
		registerHealthChecks(environment);
		registerProviders(environment);
		registerResources(environment);
	}

	/**
	 * Little utility to concatenate strings with a separator.
	 * 
	 * @param strings
	 *            Strings to join
	 * @param separator
	 *            Separator between strings
	 * @return Joined String
	 */
	String join(String[] strings, String separator) {

		StringBuilder sb = new StringBuilder();

		for (String string : strings) {

			sb.append(string).append(separator);
		}

		return sb.delete(sb.length() - separator.length(), sb.length())
				.toString();
	}

	/**
	 * Registered JAX-RS Endpoints
	 * 
	 * @param environment
	 *            Dropwizard Environment
	 */
	private void registerResources(Environment environment) {

		final Map<String, Object> beansWithAnnotation = applicationContext
				.getBeansWithAnnotation(Path.class);

		for (String beanName : beansWithAnnotation.keySet()) {

			Object resource = beansWithAnnotation.get(beanName);

			environment.addResource(resource);

			logger.info("Registering resource : "
					+ resource.getClass().getName());
		}
	}

	/**
	 * Register Dropwizard Managed-Lifecycle Components
	 * 
	 * @param environment
	 *            Dropwizard Environment
	 */
	private void registerManaged(Environment environment) {

		final Map<String, Managed> beansOfType = applicationContext
				.getBeansOfType(Managed.class);

		for (String beanName : beansOfType.keySet()) {

			Managed managed = beansOfType.get(beanName);

			environment.manage(managed);

			logger.info("Registering managed: " + managed.getClass().getName());
		}
	}

	/**
	 * Register Jetty Lifecycle Components.
	 * 
	 * @param environment
	 *            Dropwizard Environment
	 */
	private void registerLifecycle(Environment environment) {

		Map<String, LifeCycle> beansOfType = applicationContext
				.getBeansOfType(LifeCycle.class);

		for (String beanName : beansOfType.keySet()) {

			LifeCycle lifeCycle = beansOfType.get(beanName);

			environment.manage(lifeCycle);

			logger.info("Registering lifeCycle: "
					+ lifeCycle.getClass().getName());
		}
	}

	/**
	 * Register Dropwizard Tasks.
	 * 
	 * @param environment
	 *            Dropwizard Environment
	 */
	private void registerTasks(Environment environment) {

		final Map<String, Task> beansOfType = applicationContext
				.getBeansOfType(Task.class);

		for (String beanName : beansOfType.keySet()) {

			Task task = beansOfType.get(beanName);

			environment.addTask(task);

			logger.info("Registering task: " + task.getClass().getName());
		}
	}

	/**
	 * Register Dropwizard Healthchecks.
	 * 
	 * @param environment
	 *            Dropwizard Environment
	 */
	private void registerHealthChecks(Environment environment) {

		final Map<String, HealthCheck> beansOfType = applicationContext
				.getBeansOfType(HealthCheck.class);

		for (String beanName : beansOfType.keySet()) {

			HealthCheck healthCheck = beansOfType.get(beanName);

			environment.addHealthCheck(healthCheck);

			logger.info("Registering healthCheck: "
					+ healthCheck.getClass().getName());
		}
	}

	/**
	 * Register JAX-RS Injectable Providers
	 * 
	 * @param environment
	 *            Dropwizard Environment
	 */
	private void registerProviders(Environment environment) {

		final Map<String, Object> beansWithAnnotation = applicationContext
				.getBeansWithAnnotation(Provider.class);

		for (String beanName : beansWithAnnotation.keySet()) {

			Object provider = beansWithAnnotation.get(beanName);

			environment.addProvider(provider);

			logger.info("Registering provider : "
					+ provider.getClass().getName());
		}
	}

	private ApplicationContext initSpringParent() {
		ApplicationContext parent = new FileSystemXmlApplicationContext(
				new String[] { "conf/dropwizardSpringApplicationContext.xml" },
				true);
		return parent;
	}
}
