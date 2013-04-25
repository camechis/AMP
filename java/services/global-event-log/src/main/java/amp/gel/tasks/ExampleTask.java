package amp.gel.tasks;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;


public class ExampleTask extends Task {

	public ExampleTask() {
		
		super("example");
	}

	@Override
	public void execute(ImmutableMultimap<String, String> params, PrintWriter writer)
			throws Exception {
		
		writer.println("Hello Task!");
	}
}