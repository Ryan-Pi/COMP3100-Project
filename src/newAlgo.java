import java.util.ArrayList;

public class newAlgo extends Scheduler{
	//first fit
	// but uses worst fit scheduling if servers have running jobs
	ArrayList<Server> activeServers = new ArrayList<Server>();
	ArrayList<Server> capableServers = new ArrayList<Server>();
	boolean first = true;
	
	public newAlgo(Messenger messenger) {
		super(messenger);
	}
	
	public void findServer(int serverNo) {
		int fit = 999;
		for(int i = 0; i < serverNo; i++) {
			read();
			Server server = new Server(str);
			capableServers.add(server);
		}
		for(int i = 0; i < serverNo; i++) {
			for(int j = 0; j < activeServers.size(); j++) {
				if(capableServers.get(i).getServerType().equals(activeServers.get(j).getServerType())){
					if(capableServers.get(i).getServerID().equals(activeServers.get(j).getServerType())) {
						if(capableServers.get(i).getServerWaitingJobsInt()==0) {
							Server server = capableServers.get(i);
							int cores = server.getServerCoresInt();
							int memory = server.getServerMemoryInt();
							int disk = server.getServerDiskInt();
							if(job.getCoresInt()<=cores && job.getDiskInt()<=disk && job.getMemoryInt()<=memory) {
								targetServer = server;
								break;
							}
						}
					}
				}
			}
		}
		if(first) {
			targetServer = capableServers.get(capableServers.size()-1);
			first = false;
		}
		//targetServer = XXX;
		updateActiveServers();
	}
	
	public ArrayList<Server> updateActiveServers(){
		//returns a list of servers in use
		activeServers.add(targetServer);
		return activeServers;
	}
}
