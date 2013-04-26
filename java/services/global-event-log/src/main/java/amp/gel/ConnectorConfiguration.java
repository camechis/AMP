package amp.gel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectorConfiguration {

	@Valid
	@NotBlank
	@JsonProperty
	private String instanceName;

	@Valid
	@NotBlank
	@JsonProperty
	private String zooKeepers;

	@Valid
	@NotBlank
	@JsonProperty
	private String user;

	@Valid
	@NotNull
	@JsonProperty
	private String password;

	public String getInstanceName() {
		return instanceName;
	}

	public String getZooKeepers() {
		return zooKeepers;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
