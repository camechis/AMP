package amp.extensions.spring;

public class MessageService {

	
	public void printMessageToConsole(ExampleMessage message){
		
		System.out.println(message);
	}
	
	public Reply deliverMessage(Message message){
		
		System.out.println(message.getBody());
		
		return new Reply("Thanks for the message.");
	}
	
	public void printReply(Reply reply){
		
		System.out.println(reply.getBody());
	}
}
