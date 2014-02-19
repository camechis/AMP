package amp.spring.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import amp.rabbit.transport.SimpleRoutingInfoCache;
import amp.spring.configuration.AmpereConfig;
import amp.spring.configuration.BasicAmpereConnectionConfig;
import amp.spring.configuration.CommandableRoutingInfoCache;
import amp.spring.configuration.SimpleRoutingInfoConfig;
import amp.spring.configuration.SslAmpereConnectionConfig;
import amp.spring.configuration.TokenAmpereConnectionConfig;

public class AmpereConfigurationImportSelector implements ImportSelector {

	public static final String DEFAULT_MODE_ATTRIBUTE_NAME = "mode";
	
	public static final String DEFAULT_TOPOLOGY_ATTRIBUTE_NAME = "topology";
	
	public static final String DEFAULT_CACHE_ATTRIBUTE_NAME = "cache";


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
		
		AmpereConnectionMode mode = (AmpereConnectionMode) attributes.get(getModeAttributeName());
		Topology topology = (Topology)attributes.get(DEFAULT_TOPOLOGY_ATTRIBUTE_NAME);
		RoutingInfoCache cache = (RoutingInfoCache)attributes.get(DEFAULT_CACHE_ATTRIBUTE_NAME);
		
		List<String> imports = new ArrayList<String>();
		//everyone gets this now lets make a choice on the rest
		imports.add(AmpereConfig.class.getName());
		setMode(imports, mode);
		setTopology(imports,topology);
		setRoutingCache(imports,cache);
		return (String[])imports.toArray();
	}
	
	private void setRoutingCache(List<String> imports, RoutingInfoCache cache) {
		switch(cache) {
		case SIMPLE:
			imports.add(SimpleRoutingInfoConfig.class.getName());
		case COMMANDABLE:
			imports.add(CommandableRoutingInfoCache.class.getName());
		}
	}

	private void setTopology(List<String> imports, Topology topology) {
		switch(topology) {
		case GTS:
			break;
		case SIMPLE:
			break;
		}
	}

	private void setMode( List<String>  imports, AmpereConnectionMode mode) {
		switch (mode) {
		case BASIC:
			imports.add(BasicAmpereConnectionConfig.class.getName());
			break;
		case SSL:
			imports.add(SslAmpereConnectionConfig.class.getName());
			break;
		case TOKEN:
			imports.add(TokenAmpereConnectionConfig.class.getName());
			break;
		}
	}
	
}
