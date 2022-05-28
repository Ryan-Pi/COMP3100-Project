import java.util.ArrayList;

public class lrr extends Scheduler{
	
	ArrayList<Server> capableServers = new ArrayList<Server>();
	ArrayList<Server> largestServers = new ArrayList<Server>();
	String serverType;
	boolean foundLargest = false;
	int idx = 0;
	
	public lrr(Messenger messenger) {
		super(messenger);
	}
	
	public void findServer(int numberOfServers) {
		if(foundLargest==false) {
			int cores = 0;
			for(int i = 0; i < numberOfServers; i++) {
				read();
				Server server = new Server(str);
				capableServers.add(server);
				if(server.getServerCoresInt()>cores) {
					serverType = server.getServerType();
					cores = server.getServerCoresInt();
				}
			}
			for(int i = 0; i < capableServers.size() ; i++) {
				if(capableServers.get(i).getServerType().equals(serverType)) {
					largestServers.add(capableServers.get(i));
				}
			}
			foundLargest = true;
		} else {
			for(int i = 0; i < numberOfServers; i++){
				read();
			}
		}
		targetServer = largestServers.get(idx);
		idx++;
		if(idx>largestServers.size()-1) {
			idx = 0;
		}
	}

}
