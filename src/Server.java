//This class contains information about a server,
//most importantly its type, ID, status, cores, memory and disk
//It has getters and setters for the above information as well
//getters will default to returning String values unless they have Int in the name e.g. getServerCoresInt()

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
	
	//constructor used for all responses from
	//ds-server regarding server information
	public Server(String server) {
		String temp[] = server.split(" ",9);
		type = temp[0];
		id = temp[1];
		status = temp[2];
		currentStartTime = temp[3];
		cores = temp[4];
		memory = temp[5];
		disk = temp[6];
		//number of jobs waiting on server
		waiting = temp[7];
		//number of jobs running on server
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
	
	public int getServerCoresInt() {
		return Integer.valueOf(cores);
	}

	public int getServerMemoryInt() {
		return Integer.valueOf(memory);
	}
	
	public int getServerDiskInt() {
		return Integer.valueOf(disk);
	}
	
	public int getServerWaitingJobsInt() {
		return Integer.valueOf(waiting);
	}
	
	public int getServerRunningJobsInt() {
		return Integer.valueOf(running);
	}
	
	public void setServerType(String s) {
		type = s;
	}
	
	public void setServerID(String s) {
		id = s;
	}
	
	public void setServerStatus(String s) {
		status = s;
	}

	public void setServerCurrentStartTime(String s) {
		currentStartTime = s;
	}

	public void setServerCores(String s) {
		cores = s;
	}

	public void setServerMemory(String s) {
		memory = s;
	}
	
	public void setServerDisk(String s) {
		disk = s;
	}
	
	public void setServerWaitingJobs(String s) {
		waiting = s;
	}
	
	public void setServerRunningJobs(String s) {
		running = s;
	}

}
