import java.util.ArrayList;

public class newAlgo extends Scheduler{
	//improved first fit algorithm,
	//with added migration which attempts to do best fit?
	//where if all available servers are on (active or idle)
	//it will attempt to use worst fit
	ArrayList<Server> activeServers = new ArrayList<Server>();
	boolean first = true;
	
	public newAlgo(Messenger messenger) {
		super(messenger);
	}
	
	@Override
	public void getServerInfo() {				
		write("GETS Available " + job.getCores() + " " + job.getDisk() + " " + job.getMemory());
		read();
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);			
		write("OK");
	}
	
//	public void findServer(int numberOfServers) {
//		boolean allOn = true;
//		ArrayList<Server> availableServers = new ArrayList<Server>();
//		//request all servers with resources currently available to execute the job
//		for(int i = 0; i < numberOfServers; i++) {
//			read();
//			Server server = new Server(str);
//			availableServers.add(server);
//			if(server.getServerStatus().equals("inactive")) {
//				allOn = false;
//			}
//		}
//		//if there are available servers, schedule to the first such server
//		if(availableServers.size()>0) {
//			if(!allOn) {
//				targetServer = availableServers.get(0);
//			} else {
//				int fit = -1;
//				for(int i = 0; i < availableServers.size();i++) {
//					Server server = availableServers.get(i);
//					int serverFit = server.getServerCoresInt()-job.getCoresInt();
//					if(serverFit>fit) {
//						targetServer = server;
//					}
//				}
//			}
//		}else {
//			//find a server capable of running the job
//			write("GETS Capable " + job.getCores() + " " + job.getDisk() + " " + job.getMemory());
//			while(!str.contains("DATA")) {
//				read();
//			}
//			String[] serverStr = str.split(" ",3);
//			int serverNo = Integer.valueOf(serverStr[1]);
//			write("OK");
//			ArrayList<Server> capableServers = new ArrayList<Server>();
//			for(int i = 0; i < serverNo; i++) {
//				read();
//				Server server = new Server(str);
//				capableServers.add(server);
//			}
//			read();
//			write("OK");
//			//look for server that has the lowest estimated wait time
//			int waitTime = 99999999;
//			for(int i = 0; i < capableServers.size();i++) {
//				Server server = capableServers.get(i);
//				write("EJWT " + server.getServerType() + " " + server.getServerID());
//				read();
//				int time = Integer.valueOf(str);
//				if(time<waitTime) {
//					targetServer = server;
//				}
//				write("OK");
//				waitFor(".");
//			}
//		}
//	}
	
	public void findServer() {
		
	}
	
	@Override
	public void migrate() {
		
	}
}
