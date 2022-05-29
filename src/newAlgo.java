import java.util.ArrayList;

public class newAlgo extends Scheduler{
	//modified first fit algorithm
	//with migration capability
	//migration is worst fit on readily available servers
	ArrayList<Server> activeServers = new ArrayList<Server>();
	boolean first = true;
	
	public newAlgo(Messenger messenger) {
		super(messenger);
	}
	
//	public void getServerInfo() {				
//		write("GETS Available " + job.getCores() + " " + job.getDisk() + " " + job.getMemory());
//		read();
//		String[] serverStr = str.split(" ",3);
//		numberOfServers = Integer.valueOf(serverStr[1]);			
//		write("OK");
//	}
//	
//	public void findServer(int numberOfServers) {
//		ArrayList<Server> availableServers = new ArrayList<Server>();
//		//request all servers with resources currently available to execute the job
//		for(int i = 0; i < numberOfServers; i++) {
//			read();
//			Server server = new Server(str);
//			availableServers.add(server);
//		}
//		//if there are available servers, schedule to the first such server
//		if(availableServers.size()>0) {
//				targetServer = availableServers.get(0);
//			} else {
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
//			write("OK");
//			waitFor(".");
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
//			}
//		}
//	}
	
	@Override
	public void getServerInfo() {
		//str = messenger.getCurrentLine();
		write("GETS Avail " + job.getCores() + " " + job.getMemory() + " " + job.getDisk());
		read();
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);
		write("OK");
	}
	
	public void findServer(int numberOfServers) {
		ArrayList<Server> availableServers = new ArrayList<Server>();
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			availableServers.add(server);
		}
		write("OK");
		waitFor(".");
		if(availableServers.size()>0) {
			targetServer = availableServers.get(0);
		} else {
			write("Gets Capable " + job.getCores() + job.getMemory() + job.getDisk());
			read();
			String[] serverStr = str.split(" ",3);
			numberOfServers = Integer.valueOf(serverStr[1]);
			write("OK");
			
		}
	}
	
//	public void findServer(int numberOfServers) {
//	boolean serverChanged = false;
//	ArrayList<Server> capableServers = new ArrayList<Server>();
//	for(int i = 0; i < numberOfServers; i++) {
//		read();
//		Server server = new Server(str);
//		capableServers.add(server);
//	}
//	for(int i = 0; i < capableServers.size(); i++) {
//		Server server = capableServers.get(i);
//		int cores = server.getServerCoresInt();
//		if(job.getCoresInt()<=cores) {
//			targetServer = server;
//			serverChanged = true;
//			break;
//		}
//	}
//	if(serverChanged == false) {
//		for(int i = 0; i < capableServers.size();i++) {
//			Server server = capableServers.get(i);
//			if(!server.getServerStatus().equals("inactive")) {
//					targetServer = capableServers.get(i);
//					break;
//			}
//		}
//	}
//}
	
//	@Override
//	public void migrate() {
//		//list of servers with waiting jobs
//		ArrayList<Server> waitServers = new ArrayList<Server>();
//		//list of jobs that are waiting
//		ArrayList<Job> waitList = new ArrayList<Job>();
//		write("GETS All");
//		while(!str.contains("DATA")) {
//			read();
//		}
//		String[] serverStr = str.split(" ",3);
//		int serverNo = Integer.valueOf(serverStr[1]);
//		write("OK");
//		for(int i = 0; i < serverNo; i++) {
//			read();
//			Server server = new Server(str);
//			if(server.getServerWaitingJobsInt()>0) {
//				waitServers.add(server);
//			}
//		}
//		write("OK");
//		for(int i = 0; i < waitServers.size(); i++) {
//			Server server = waitServers.get(i);
//			write("LSTJ " + server.getServerType() + " " + server.getServerID());
//			while(!str.contains("DATA")) {
//				read();
//			}
//			String[] jobStr = str.split(" ",3);
//			int jobNo = Integer.valueOf(jobStr[1]);
//			write("OK");
//			for(int j = 0; j < jobNo; j++) {
//				read();
//				String[] jobDesc = str.split(" ",8);
//				//jobDesc[1] is the status of the job
//				//with "1" indicating that it is waiting to execute
// 				if(jobDesc[1].equals("1")) {
// 					int id = Integer.valueOf(jobDesc[0]);
// 					int cores = Integer.valueOf(jobDesc[5]);
// 					int memory = Integer.valueOf(jobDesc[6]);
// 					int disk = Integer.valueOf(jobDesc[7]);
// 					Job wait = new Job(id, cores, memory, disk,server.getServerType(),server.getServerID());
// 					waitList.add(wait);
// 				}
//			}
//			write("OK");
//			waitFor(".");
//		}
//		for(int i = 0; i < waitList.size(); i++) {
//			boolean found = false;
//			ArrayList<Server> availableServers = new ArrayList<Server>();
//			Job temp = waitList.get(i);
//			write("GETS Avail " + temp.getCores() + " " + temp.getMemory() + " " + temp.getDisk());
//			while(!str.contains("DATA")) {
//				read();
//			}
//			String[] serverDetails = str.split(" ",3);
//			int serverNumber = Integer.valueOf(serverDetails[1]);
//			write("OK");
//			if(serverNumber==0) {
//				waitFor(".");
//			} else {
//				for(int k = 0; k < serverNumber; k++) {
//					read();
//					Server server = new Server(str);
//					availableServers.add(server);
//				}
//				int fit = 9999;
//				for(int k = 0; k < availableServers.size(); k++){
//					Server server = availableServers.get(k);
//					int serverFit = server.getServerCoresInt() - temp.getCoresInt();
//					if(serverFit < fit) {
//						found = true;
//						targetServer = server;
//					}
//				}
//				write("OK");
//				waitFor(".");
//				if(found) {
//					System.out.println(temp.getID() + " migrating to " + targetServer.getServerType() + " " + targetServer.getServerID() + "from " + temp.getMigrationInfo());
//					write("MIGJ " + temp.getID() + " " + temp.getMigrationInfo() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
//				}
//			}
//		}
//	}
}
