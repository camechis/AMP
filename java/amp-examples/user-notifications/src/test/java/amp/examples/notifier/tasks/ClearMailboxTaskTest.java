package amp.examples.notifier.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintWriter;

import org.junit.Test;

import amp.examples.notifier.core.notifiers.NotificationMailbox;

import com.google.common.collect.ImmutableMultimap;

/**
 * Verifies that the Clear Mailbox Task actually
 * clears the mailbox when invoked.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class ClearMailboxTaskTest {
	
	@Test
	public void mailbox_is_cleared_when_execute_is_called() throws Exception {
		
		NotificationMailbox mailbox = mock(NotificationMailbox.class);
		
		ClearMailboxTask clearMailboxTask = new ClearMailboxTask(mailbox);
		
		@SuppressWarnings("unchecked")
		ImmutableMultimap<String, String> params = mock(ImmutableMultimap.class);
		
		PrintWriter writer = mock(PrintWriter.class);
		
		clearMailboxTask.execute(params, writer);
		
		verify(mailbox).clearMailbox();
	}

}
