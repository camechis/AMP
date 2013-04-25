package amp.gel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.berico.fallwizard.SpringConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleConfiguration extends SpringConfiguration {

	@Valid
	@NotNull
	@JsonProperty
	private String exampleProperty;
	
	public void setExampleProperty(String value){
		
		this.exampleProperty = value;
	}
	
	public String getExampleProperty(){
		
		return this.exampleProperty;
	}
}
