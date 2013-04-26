package amp.gel;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopologyServiceConfiguration {

	@Valid
	@NotNull
	@JsonProperty
	private String clientProfile;

	@Valid
	@NotBlank
	@JsonProperty
	private String name;

	@Valid
	@NotBlank
	@JsonProperty
	private String hostname;

	@Valid
	@NotBlank
	@JsonProperty
	private String vhost;

	@Valid
	@Min(1)
	@Max(65535)
	@JsonProperty
	private int port;

	public String getClientProfile() {
		return clientProfile;
	}

	public String getName() {
		return name;
	}

	public String getHostname() {
		return hostname;
	}

	public String getVhost() {
		return vhost;
	}

	public int getPort() {
		return port;
	}
}
