import java.util.ArrayList;

public class ff extends Scheduler{
	//First-Fit Algorithm scheduling
	
	public ff(Messenger messenger) {
		super(messenger);
	}
	
	public void findServer(int numberOfServers) {
		boolean serverChanged = false;
		ArrayList<Server> capableServers = new ArrayList<Server>();
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			capableServers.add(server);
		}
		for(int i = 0; i < capableServers.size(); i++) {
			Server server = capableServers.get(i);
			int cores = server.getServerCoresInt();
			if(job.getCoresInt()<=cores) {
				targetServer = server;
				serverChanged = true;
				break;
			}
		}
		if(serverChanged == false) {
			for(int i = 0; i < capableServers.size();i++) {
				Server server = capableServers.get(i);
				if(server.getServerStatus().equals("booting")||server.getServerStatus().equals("active")) {
						targetServer = capableServers.get(i);
						break;
				}
			}
		}
	}
	
}
