package amp.tests.integration;

public class TestRequest extends TestEvent
{
	public int responseCount;
	
	public TestRequest(int responseCount){
		this.responseCount = responseCount;
	}
}