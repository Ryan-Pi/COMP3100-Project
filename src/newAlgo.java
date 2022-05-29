import java.util.ArrayList;

public class newAlgo extends Scheduler{
	//modified first fit algorithm
	//with migration capability
	//migration is worst fit on readily available servers
	boolean available = true;
	
	public newAlgo(Messenger messenger) {
		super(messenger);
	}
	
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
		//finds first server that is readily available to execute job
		//if none, finds capable server with lowest estimated waiting time
		//This is essentially a modified First-Fit Algorithm
		ArrayList<Server> availableServers = new ArrayList<Server>();
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			availableServers.add(server);
		}
		if(availableServers.size()>0) {
			available = true;
			targetServer = availableServers.get(0);
		} else {
			available = false;
			write("OK");
			waitFor(".");
			ArrayList<Server> capableServers = new ArrayList<Server>();
			write("GETS Capable " + job.getCores() + " " + job.getMemory() + " " + job.getDisk());
			while(!str.contains("DATA")) {
				read();
			}
			String[] serverStr = str.split(" ",3);
			int serverNo = Integer.valueOf(serverStr[1]);
			write("OK");
			for(int i = 0; i < serverNo; i++) {
				read();
				Server server = new Server(str);
				capableServers.add(server);
			}
			write("OK");
			waitFor(".");
			int waitTime = 999999999;
			for(int i = 0; i < capableServers.size(); i++) {
				Server server = capableServers.get(i);
				write("EJWT " + server.getServerType() + " " + server.getServerID());
				read();
				int serverWait = Integer.valueOf(str);
				if(serverWait < waitTime) {
					targetServer = server;
				}
			}
		}
	}
	
	@Override
	public void sendSchedule() {
		write("OK");
		if(available) {
			waitFor(".");
		}
		write("SCHD " + job.getID() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
		waitFor("OK");
		write("REDY");
	}
	
	@Override
	public void migrate() {
	
	}
}
