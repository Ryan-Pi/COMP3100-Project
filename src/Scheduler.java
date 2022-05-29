import java.util.ArrayList;

public abstract class Scheduler {
	
	ArrayList<String> serverTypes;
	Job job;
	Server targetServer;
	Messenger messenger;
	String str;
	int numberOfServers;
	
	//maybe have schedule() use abstract functions and have subclassses provide functions
	
	public Scheduler(Messenger messengerClient) {
		this.messenger = messengerClient;
	}
	
	public void updateServerTypes(ArrayList<String> serverTypeList) {
		//this should only be called at the start of the client to get list of server types
		serverTypes = serverTypeList;
	}
	
	public void write(String n) {
		messenger.message(n);
	}
	
	public void read() {
		str = messenger.read();
	}
	
	public void sendSchedule() {
		System.out.println("Sending Schedule!");
		write("OK");
		waitFor(".");
		write("SCHD " + job.getID() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
		waitFor("OK");
		write("REDY");
	}
	
	public void waitFor(String n) {
		messenger.waitFor(n);
	}
	
	public void getServerInfo() {
		//str = messenger.getCurrentLine();
		write("GETS Capable " + job.getCores() + " " + job.getDisk() + " " + job.getMemory());
		read();
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);
		write("OK");
	}
	
	public void schedule(String j) {
		job = new Job(j);
		getServerInfo();
		findServer(numberOfServers);
		sendSchedule();
	}
	
	public abstract void findServer(int numberOfServers);
	
	public void migrate() {
		
	}
}
