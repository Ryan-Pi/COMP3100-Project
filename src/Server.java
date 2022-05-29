
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
	
	public void modifyServerByAddingJob(Job j) {
		cores = Integer.toString(Integer.valueOf(cores) - j.getCoresInt());
		memory = Integer.toString(Integer.valueOf(memory) - j.getMemoryInt());
		disk = Integer.toString(Integer.valueOf(disk) - j.getDiskInt());
		if(Integer.valueOf(cores)<0) {
			waiting = Integer.toString(Integer.valueOf(waiting) + 1);
		} else {
			running = Integer.toString(Integer.valueOf(running) + 1);
		}
	}
	
	public void modifyServerByRemovingJob(Job j) {
		cores = Integer.toString(Integer.valueOf(cores) + j.getCoresInt());
		memory = Integer.toString(Integer.valueOf(memory) + j.getMemoryInt());
		disk = Integer.toString(Integer.valueOf(disk) + j.getDiskInt());
		waiting = Integer.toString(Integer.valueOf(waiting) - 1);
	}

}
