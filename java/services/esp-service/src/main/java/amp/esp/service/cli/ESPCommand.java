package amp.esp.service.cli;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import com.yammer.dropwizard.cli.Command;
import com.yammer.dropwizard.config.Bootstrap;


public class ESPCommand extends Command {

	public ESPCommand() {
		
		super("ESP", "Prints 'Hello Command' to the console.");
	}

	@Override
	public void configure(Subparser subparser) {}

	@Override
	public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
		
		System.out.println("Hello Command");
	}

}
