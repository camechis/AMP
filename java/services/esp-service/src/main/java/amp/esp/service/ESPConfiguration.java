package amp.esp.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.berico.fallwizard.SpringConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ESPConfiguration extends SpringConfiguration {

	@Valid
	@NotNull
	@JsonProperty
	private String espProperty;
	
	public void setESPProperty(String value){
		
		this.espProperty = value;
	}
	
	public String getESPProperty(){
		
		return this.espProperty;
	}
}
