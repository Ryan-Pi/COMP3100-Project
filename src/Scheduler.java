import java.util.ArrayList;

public abstract class Scheduler {
	
	ArrayList<String> serverTypes;
	ArrayList<Server> activeServers = new ArrayList<Server>();
	Server targetServer;
	Messenger messenger;
	String str;
	
	
	public Scheduler(Messenger messengerClient) {
		this.messenger = messengerClient;
	}
	
	public void updateServerTypes(ArrayList<String> serverTypeList) {
		//this should only be called at the start of the client to get list of server types
		serverTypes = serverTypeList;
	}
	
	public ArrayList<Server> updateActiveServers(){
		//returns a list of servers in use
		activeServers.add(targetServer);
		return activeServers;
	}
	
	public void write(String n) {
		messenger.message(n);
	}
	
	public void read() {
		str = messenger.read();
	}
	
	public void waitFor(String n) {
		messenger.waitFor(n);
	}
	
	public abstract void schedule(String j);
}
