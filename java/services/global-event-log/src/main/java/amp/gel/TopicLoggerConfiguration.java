package amp.gel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.berico.fallwizard.SpringConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicLoggerConfiguration extends SpringConfiguration {

	@Valid
	@NotBlank
	@JsonProperty
	private String tableName;

	@Valid
	@NotNull
	@JsonProperty
	private ConnectorConfiguration connector = new ConnectorConfiguration();

	@Valid
	@NotNull
	@JsonProperty
	private TopologyServiceConfiguration topologyService = new TopologyServiceConfiguration();

	public String getTableName() {
		return tableName;
	}

	public ConnectorConfiguration getConnector() {
		return connector;
	}

	public TopologyServiceConfiguration getTopologyService() {
		return topologyService;
	}
}
