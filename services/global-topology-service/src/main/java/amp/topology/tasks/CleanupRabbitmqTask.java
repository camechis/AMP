package amp.topology.tasks;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.RabbitMgmtService;
import rabbitmq.mgmt.model.Queue;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;

public class CleanupRabbitmqTask extends Task {
	
	private static final Logger logger = LoggerFactory.getLogger(CleanupRabbitmqTask.class);
	
	SimpleDateFormat rmqDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static int MAX_IDLE_TIME_MINUTES = 30;
	
	public void setMaxIdleTimeInMinutes(int minutes){ MAX_IDLE_TIME_MINUTES = minutes; }
	
	RabbitMgmtService mgmtService;
	
	public CleanupRabbitmqTask(RabbitMgmtService mgmtService) {
		super("clean-rabbit");
		
		this.mgmtService = mgmtService;
	}

	@Override
	public void execute(
			ImmutableMultimap<String, String> context, 
			PrintWriter writer)
			throws Exception {
		
		pruneUnusedQueues(writer);
	}
	
	protected void pruneUnusedQueues(PrintWriter writer){
		
		Collection<Queue> queues = mgmtService.queues().all();
		
		for (Queue q : queues){
			
			if (shouldBePruned(q)){
				
				mgmtService.queues().delete(q.getVhost(), q.getName());
				
				writer.write(String.format("\nPruned Queue: %s", q.getName()));
			}
		}
	}
	
	protected boolean shouldBePruned(Queue q){
		
		long idleSince = System.currentTimeMillis();
		
		try {
			
			idleSince = rmqDateFormat.parse(q.getIdleSince()).getTime();
			
		} catch (ParseException e) {
			
			logger.error("Error parsing date from Rabbit.", e);
		}
		
		long totalIdleTime = System.currentTimeMillis() - idleSince;
		
		return q.getActiveConsumers() == 0 
			&& q.getConsumers() == 0 
			&& !q.isDurable()
			&& totalIdleTime > (MAX_IDLE_TIME_MINUTES * 60000);
	}
	
}
