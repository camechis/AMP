package amp.esp.service.managed;

import com.yammer.dropwizard.lifecycle.Managed;

public class ESPManaged implements Managed {

	@Override
	public void start() throws Exception {
		
		System.out.println("Started.");
	}

	@Override
	public void stop() throws Exception {
		
		System.out.println("Stop.");
	}

}
