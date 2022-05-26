
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
	
	public void write(String n) {
		n = n + "\n";
		messenger.message(n);
	}
	
	public void read() {
		str = messenger.read();
	}
	
	public void waitFor(String n) {
		messenger.waitFor(n);
	}
	
}
