package amp.plugin;

import java.beans.PropertyDescriptor;

import com.berico.crypto.OrionEncrypt;
import com.berico.crypto.SimpleCrypto;
import com.berico.fallwizard.SpringConfiguration;
import com.google.common.base.Optional;
import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.config.SslConfiguration;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Given DropWizard's configuration objects (HttpConfig & the SSLConfig object)
 * this modifier will go through all the passwords and decrypt the password
 * as necessary using the com.berico.crypto routines (project util-incubator).
 * 
 * @author jmccune
 */
public class PasswordConfigurationModifier implements IConfigurationModifier {

	private static final Logger logger = LoggerFactory.getLogger(PasswordConfigurationModifier.class);
	private static final String ENCRYPTED = "ENCRYPTED:";
	private SimpleCrypto crypto = new OrionEncrypt();

	/** Implement the interface
	 * @param configuration should be a non-null DropWizard configuration object. 
	 */
	public void modifyConfiguration(SpringConfiguration configuration) {
		
		if (configuration==null) {
			logger.warn("Unable to attempt decrypting passwords-- null configuration!");
			return;
		}
		
		HttpConfiguration httpConfig = configuration.getHttpConfiguration();
		if (httpConfig==null) {
			logger.warn("Unable to attempt decrypting passwords-- empty configuration!");
			return;
		}

		// httpConfig doesn't conform to bean protocol-- getting one type & setting another.
		// So can't do this with handlePasswordsForBean as originally planned.
		
		// Manually get the only password in httpConfig and decrypt if necessary.		
		Optional<String> passwordContainer = httpConfig.getAdminPassword();
		
		// If the password exists...
		if (passwordContainer.isPresent()) {				
			String originalPasswordAsString = passwordContainer.get();

			// Convert it to the raw password.
			String passwordAsString = decryptIfNecessary(originalPasswordAsString);
			
			// Reset the now decrypted password
			if (!passwordAsString.equalsIgnoreCase(originalPasswordAsString)) {
				httpConfig.setAdminPassword(passwordAsString);
			}
		}

		// Attempt to do the same with the SSL Configuration
		SslConfiguration sslConfig = httpConfig.getSslConfiguration();
		if (sslConfig==null) {
			logger.warn("Unable to attempt decrypting passwords-- ssl configuration!");
			return;
		}
		
		handlePasswordsForBean(sslConfig);
	}
	
	/**
	 * Searches the bean for any property that has the word password.
	 * Assumes that the bean's password property is using Google's Guava
	 * framework and is stored in an Optional&lt;String&gt; class object.
	 * @param beanObject (non-null) bean to find password properties.
	 */
	private void handlePasswordsForBean(Object beanObject) {
		
		//Search all the method names.
		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(beanObject);
		for (PropertyDescriptor pd : propertyDescriptors) {
			
			//For each method containing a Password in the name...
			// (We assume that all methods of the bean set/get Optional<String> for the password) 
			if (pd.getName().contains("Password")) {
				try {
					Object passwordValueObject = PropertyUtils.getProperty(beanObject,pd.getName());
					@SuppressWarnings("unchecked")
					Optional<String> passwordContainer = (Optional<String>)passwordValueObject;
					if (!passwordContainer.isPresent()) {
						continue;
					}
					//Otherwise get the value.
					String originalPasswordAsString = passwordContainer.get();
					
					// And convert it to the raw password.
					String passwordAsString = decryptIfNecessary(originalPasswordAsString);
					
					// Reset the now decrypted password
					if (!passwordAsString.equalsIgnoreCase(originalPasswordAsString)) {
						Optional<String> newPasswordContainer = Optional.of(passwordAsString);
						PropertyUtils.setProperty(beanObject,pd.getName(),newPasswordContainer);						
					}
					
					// For debugging only...
					//System.out.println("PROPERTY: "+pd.getName()+ " >> VALUE: "+originalPasswordAsString+" >> "+passwordAsString);
					
				} catch (Exception e) {
					logger.warn("Couldn't get property: "+pd.getName()+" from the configuration in attempt to decrypt!",e);
				}
			}
		}
	}
	
	
	private String decryptIfNecessary(String originalValue) {
		try {
			if (originalValue != null && originalValue.startsWith(ENCRYPTED)) {
				String strippedVal = originalValue
						.substring(ENCRYPTED.length());
				return crypto.decrypt(strippedVal);
			}
		} catch (Exception e) {
			logger.error("Failed to convert value: "+originalValue,e);
		}
		return originalValue;
	}

}
