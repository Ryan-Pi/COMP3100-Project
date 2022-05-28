
public class fc extends Scheduler{
	
	public fc(Messenger messenger) {
		super(messenger);
	}
	
	public void findServer(int numberOfServers) {
		read();
		Server server = new Server(str);
		targetServer = server;
		for(int i = 0; i < numberOfServers-1; i++) {
			//clearing the input stream, as we only need the first server
			read();
		}
	}
	
}
