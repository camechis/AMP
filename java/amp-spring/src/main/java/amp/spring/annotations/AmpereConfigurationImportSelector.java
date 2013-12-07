package amp.spring.annotations;

import java.util.Map;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public class AmpereConfigurationImportSelector implements ImportSelector {

	public static final String DEFAULT_MODE_ATTRIBUTE_NAME = "mode";

	protected String getModeAttributeName() {
		return DEFAULT_MODE_ATTRIBUTE_NAME;
	}

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(
				EnableAmpere.class.getName(), false);
		Assert.notNull(attributes, String.format(
				"@%s is not present on importing class '%s' as expected",
				EnableAmpere.class.getSimpleName(), metadata.getClassName()));
		AmpereMode mode = (AmpereMode) attributes.get(getModeAttributeName());
		String [] imports = null;
		switch (mode) {
		case BASIC:
			System.out.println("I AM BASIC");
			break;
		case SSL:
			System.out.print("I AM SSL");
			break;
		default:
			System.out.println("I AM DEFAULT");
			break;

		}
		Assert.notNull(imports, String.format("Unknown mode: '%s'", mode));
		return null;
	}
}
