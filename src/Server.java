
public class Server {
	private String type;
	private String id;
	private String status;
	private String currentStartTime;
	private String cores;
	private String memory;
	private String disk;
	private String waiting;
	private String running;
	
	public Server(String server) {
		String temp[] = server.split(" ",9);
		type = temp[0];
		id = temp[1];
		status = temp[2];
		currentStartTime = temp[3];
		cores = temp[4];
		memory = temp[5];
		disk = temp[6];
		waiting = temp[7];
		running = temp[8];
	}
	
	public String getServerType() {
		return type;
	}
	
	public String getServerID() {
		return id;
	}
	
	public String getServerStatus() {
		return status;
	}

	public String getServerCurrentStartTime() {
		return currentStartTime;
	}

	public String getServerCores() {
		return cores;
	}

	public String getServerMemory() {
		return memory;
	}
	
	public String getServerDisk() {
		return disk;
	}
	
	public String getServerWaitingJobs() {
		return waiting;
	}
	
	public String getServerRunningJobs() {
		return running;
	}

}
