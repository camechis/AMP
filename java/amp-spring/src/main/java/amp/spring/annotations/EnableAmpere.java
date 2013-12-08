package amp.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(AmpereConfigurationImportSelector.class)
public @interface EnableAmpere {
	AmpereConnectionMode mode() default AmpereConnectionMode.BASIC;
	Topology topology() default Topology.SIMPLE;
	RoutingInfoCache cache() default RoutingInfoCache.SIMPLE;
}
