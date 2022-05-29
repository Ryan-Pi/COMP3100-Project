import java.util.ArrayList;

public class ffNew extends Scheduler{
	//First-Fit Algorithm scheduling with migration
	//this is not fully functional
	
	ArrayList<Server> activeServers = new ArrayList<Server>();
	int maxServers = 0;
	
	public ffNew(Messenger messenger) {
		super(messenger);
	}
	
	public void findServer(int numberOfServers) {
		boolean serverChanged = false;
		ArrayList<Server> capableServers = new ArrayList<Server>();
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			System.out.println(server.getServerType() + " " + server.getServerID());
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
		for(int i = 0; i < capableServers.size(); i++) {
			Server server = capableServers.get(i);
			int cores = server.getServerCoresInt();
			if(job.getCoresInt()<=cores && (server.getServerStatus().equals("inactive"))) {
				targetServer = server;
				serverChanged = true;
				break;
			}
		}
		if(serverChanged == false) {
			for(int i = 0; i < capableServers.size();i++) {
				Server server = capableServers.get(i);
				int cores = server.getServerCoresInt();
						if(job.getCoresInt()<=cores) {
							serverChanged = true;
							targetServer = server;
							break;
				}
			}
			if(serverChanged == false) {
				targetServer = capableServers.get(0);
			}
		}
		boolean serverInList = false;
		for(int i = 0; i < activeServers.size(); i++) {
			if(targetServer.getServerType().equals(activeServers.get(i).getServerType())) {
				if(targetServer.getServerID().equals(activeServers.get(i).getServerID())) {
					serverInList = true;
					activeServers.set(i, targetServer);
				}
			}
		}
		if(serverInList==false) {
			activeServers.add(targetServer);
		}
		
	}
	
	
	@Override
	public void migrate() {
		ArrayList<Job> migrationJobs = new ArrayList<Job>();
		ArrayList<Server> allServers = new ArrayList<Server>();
//		for(int i = 0; i < activeServers.size(); i++) {
//			Server server = activeServers.get(i);
//			if(server.getServerWaitingJobsInt()>0) {
//				write("LSTJ " + server.getServerType() + " " + server.getServerID());
//				while(!str.contains("DATA")) {
//					read();
//				}
//				String[] data = str.split(" ",3);
//				int numberOfJobs = Integer.valueOf(data[1]);
//				write("OK");
//				for(int j = 0; j < numberOfJobs; j++) {
//					read();
//					String[] jobData = str.split(" ", 8);
//					if(jobData[1].equals("1")) {
//						int id = Integer.valueOf(jobData[0]);
//						int cores = Integer.valueOf(jobData[5]);
//						int memory = Integer.valueOf(jobData[6]);	
//						int disk = Integer.valueOf(jobData[7]);	
//						Job job = new Job(id,cores,memory,disk, server.getServerType(), server.getServerID());
//						migrationJobs.add(job);
//					}
//				}
//				write("OK");
//			}
//		}
		
		//get all servers
		write("GETS All");
		while(!str.contains("DATA")) {
			read();
		}
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);
		write("OK");
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			allServers.add(server);
		}
		maxServers = allServers.size();
		write("OK");
		if(allServers.size()>=activeServers.size()) {
		for(int i = 0; i < allServers.size(); i++) {
			Server server = allServers.get(i);
			if(!server.getServerStatus().equals("inactive") && server.getServerWaitingJobsInt()>0) {
				write("LSTJ " + server.getServerType() + " " + server.getServerID());
				while(!str.contains("DATA")) {
					read();
				}
				String[] jobStr = str.split(" ",3);
				int numberOfJobs = Integer.valueOf(jobStr[1]);
				write("OK");
				for(int j = 0; j < numberOfJobs; j++) {
					read();
					String[] jobData = str.split(" ", 8);
					if(jobData[1].equals("1")) {
						int id = Integer.valueOf(jobData[0]);
						int cores = Integer.valueOf(jobData[5]);
						int memory = Integer.valueOf(jobData[6]);	
						int disk = Integer.valueOf(jobData[7]);	
						Job job = new Job(id,cores,disk,memory, server.getServerType(), server.getServerID());
						migrationJobs.add(job);
					}
				}
				write("OK");
			}
		}
		for(int i = 0; i < migrationJobs.size(); i++) {
			Job migrationJob = migrationJobs.get(i);
			String cores = migrationJob.getCores();
			String memory = migrationJob.getMemory();
			String disk = migrationJob.getDisk();
			ArrayList<Server> availableServers = new ArrayList<Server>();
			//get servers currently available to run the job
			write("GETS Avail " + cores + " " + memory + " " + disk);
			while(!str.contains("DATA")) {
				read();
			}
			String[] serverData = str.split(" ",3);
			int serverNo = Integer.valueOf(serverData[1]);
			write("OK");
			for(int j = 0; j < serverNo; j++) {
				read();
				Server server = new Server(str);
				availableServers.add(server);
			}
			write("OK");
			int fit = 999;
			for(int j = 0; j < availableServers.size(); j++) {
				Server server = availableServers.get(j);
				if((server.getServerCoresInt() - migrationJob.getCoresInt())<fit && (server.getServerCoresInt() - migrationJob.getCoresInt())>=0) {
					targetServer = server;
					if(fit==0) {
						break;
					}
				}
				
			}	
				
//				for(int j = 0; j < activeServers.size(); j++) {
//					Server server = activeServers.get(j);
//					if(targetServer.getServerType().equals(server.getServerType())) {
//						if(targetServer.getServerID().equals(server.getServerID())) {
//							activeServers.set(j, targetServer);
//							}
//						}
//					if(migrationJob.getMigrationInfo().equals(server.getServerType() + " " + server.getServerID())) {
//							activeServers.get(j).modifyServerByRemovingJob(migrationJob);
//						}
//				}
			waitFor(".");
				if(serverNo>0) {
					String serverInfo = targetServer.getServerType() + " " + targetServer.getServerID();
					write("MIGJ " + migrationJob.getMigrationInfo() + " " + serverInfo);
				}
		}
			while(!str.contains("OK") && !str.contains(".")) {
				read();
			}
		}
	}	
}
