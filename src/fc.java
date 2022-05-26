
public class fc extends Scheduler{
	
	public fc(Messenger messenger) {
		super(messenger);
	}

	public void schedule(String j) {
		Job job = new Job(j);
		str = messenger.getCurrentLine();
		String[] serverStr = str.split(" ",3);
		int serverNo = Integer.valueOf(serverStr[1]);
		read();
		Server server = new Server(str);
		targetServer = server;
		updateActiveServers();
		for(int i = 0; i < serverNo-1; i++) {
			//clearing the input stream, as we only need the first server
			read();
		}
		write("OK");
		waitFor(".");
		write("SCHD " + job.getID() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
	}
	
}
