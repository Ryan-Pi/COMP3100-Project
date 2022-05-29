
public abstract class Scheduler {
	//This is an abstract class which all scheduling classes are based on
	//contains methods that are common to all scheduling subclasses
	
	Job job;
	Server targetServer;
	Messenger messenger;
	String str;
	int numberOfServers;
	
	public Scheduler(Messenger messengerClient) {
		this.messenger = messengerClient;
	}
	
	public void write(String n) {
		messenger.message(n);
	}
	
	public void read() {
		str = messenger.read();
	}
	
	//sends schedule decision to ds-server
	public void sendSchedule() {
		write("OK");
		waitFor(".");
		write("SCHD " + job.getID() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
		waitFor("OK");
		write("REDY");
	}
	
	public void waitFor(String n) {
		messenger.waitFor(n);
		str = messenger.getCurrentLine();
	}
	
	//retrieves capable servers from ds-server
	//is sometimes overridden if want available servers in initial
	//request instead
	public void getServerInfo() {
		write("GETS Capable " + job.getCores() + " " + job.getMemory() + " " + job.getDisk());
		read();
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);
		write("OK");
	}
	
	//schedule a job by providing a string as input
	//used for JOBN
	public void schedule(String j) {
		job = new Job(j);
		getServerInfo();
		findServer(numberOfServers);
		sendSchedule();
	}
	
	//schedule a job by proving a job as input
	//used for any case where a job object has already
	//been created
	public void schedule(Job j) {
		job = j;
		getServerInfo();
		findServer(numberOfServers);
		sendSchedule();
	}
	
	//abstract method filled out by subclasses
	public abstract void findServer(int numberOfServers);
	
	public void migrate() {
		//this code is overridden in a subclass if a scheduling algorithm
		//implements migration of jobs
		//otherwise, it simply tells ds-server "REDY"
		write("REDY");
	}
}
