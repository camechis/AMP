package amp.esp.service.tasks;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;

public class ESPTask extends Task {

	public ESPTask() {
		
		super("ESP");
	}

	@Override
	public void execute(ImmutableMultimap<String, String> params, PrintWriter writer)
			throws Exception {
		
		writer.println("Hello Task!");
	}
}