import java.util.ArrayList;

public class ff extends Scheduler{
	//modified first fit
	
	public ff(Messenger messenger) {
		super(messenger);
	}
	
	@Override
	public void getServerInfo() {
		//str = messenger.getCurrentLine();
		write("GETS Avail " + job.getCores() + " " + job.getDisk() + " " + job.getMemory());
		read();
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);
		write("OK");
	}
	
	public void findServer(int numberOfServers) {
		ArrayList<Server> availableServers = new ArrayList<Server>();
		//request all servers with resources currently available to execute the job
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			availableServers.add(server);
		}
		//if there are available servers, schedule to the first such server
		if(availableServers.size()>0) {
			targetServer = availableServers.get(0);
		} else {
			//find a server capable of running the job
			write("GETS Capable " + job.getCores() + " " + job.getDisk() + " " + job.getMemory());
			read();
			String[] serverStr = str.split(" ",3);
			int serverNo = Integer.valueOf(serverStr[1]);
			write("OK");
			ArrayList<Server> capableServers = new ArrayList<Server>();
			for(int i = 0; i < serverNo; i++) {
				read();
				Server server = new Server(str);
				capableServers.add(server);
			}
			write("OK");
			waitFor(".");
			for(int i = 0; i < capableServers.size(); i++) {
				Server server = capableServers.get(i);
				if(server.getServerStatus().equals("active")||server.getServerStatus().equals("booting")) {
					targetServer = capableServers.get(i);
					break;
				}
			}
		}
	}
	
//	public void findServer(int numberOfServers) {
//		boolean serverChanged = false;
//		ArrayList<Server> capableServers = new ArrayList<Server>();
//		for(int i = 0; i < numberOfServers; i++) {
//			read();
//			Server server = new Server(str);
//			capableServers.add(server);
//		}
//		for(int i = 0; i < capableServers.size(); i++) {
//			Server server = capableServers.get(i);
//			int cores = server.getServerCoresInt();
//			if(job.getCoresInt()<=cores) {
//				targetServer = server;
//				serverChanged = true;
//				break;
//			}
//		}
//		if(serverChanged == false) {
//			for(int i = 0; i < capableServers.size();i++) {
//				Server server = capableServers.get(i);
//				if(!server.getServerStatus().equals("inactive")) {
//						targetServer = capableServers.get(i);
//						break;
//				}
//			}
//		}
//	}
	
}
