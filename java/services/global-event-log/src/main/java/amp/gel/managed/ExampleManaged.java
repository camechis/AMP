package amp.gel.managed;

import com.yammer.dropwizard.lifecycle.Managed;

public class ExampleManaged implements Managed {

	public void start() throws Exception {

		System.out.println("Started.");
	}

	public void stop() throws Exception {

		System.out.println("Stop.");
	}

}
