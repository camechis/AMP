package amp.examples.adaptor.tasks;

import java.io.PrintWriter;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.support.channel.BeanFactoryChannelResolver;
import org.springframework.integration.support.channel.ChannelResolver;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;


public class SendZipToLatLonRequestTask extends Task implements ApplicationContextAware {
	
	ApplicationContext context;
	
	public SendZipToLatLonRequestTask() {
		
		super("zip-to-ll");
	}
	
	@Override
	public void execute(ImmutableMultimap<String, String> params, PrintWriter writer)
			throws Exception {
		
		String zipcode = "20110";
		
		if (params.containsKey("zipcode")){
			
			zipcode = join(params.get("zipcode"));
		}
		
		ChannelResolver channelResolver = new BeanFactoryChannelResolver(this.context);

		// Compose the XML message according to the server's schema
		String requestXml2 = "<LatLonListZipCodeRequest>";
		requestXml2 += String.format("<zipCodeList>%s</zipCodeList>", zipcode);
		requestXml2 += "</LatLonListZipCodeRequest>";

		// Create the Message object
		Message<String> message = MessageBuilder.withPayload(requestXml2).build();

		// Send the Message to the handler's input channel
		MessageChannel channel = channelResolver.resolveChannelName("inboundRequestChannel");
		
		System.out.println("Sending message");
		
		channel.send(message);
	}

	/**
	 * Need a handle to the Spring Context because we don't necessarily know the name
	 * of the BeanFactoryChannelResolver and I don't want to mess with @Autowire at the
	 * moment.
	 * @param context The Spring Application Context.
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		
		this.context = context;
	}
	
	/**
	 * Joins a List of String into a single String separated by spaces.
	 * @param message Message as a list of strings.
	 * @return A single concatenated string.
	 */
	private static String join(Collection<String> message){
		
		StringBuilder sb = new StringBuilder();
		
		for(String word : message){
		
			sb.append(word).append(",");
		}
		
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
}