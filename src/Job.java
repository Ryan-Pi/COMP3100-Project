//This class contains information about a job,
//namely its ID and its requirements for cores, disk and memory
//It has getters and setters for the above information as well

public class Job {
	int id;
	int cores;
	int disk;
	int memory;
	String serverType;
	int serverID;
	
	public Job(int jobId, int jobCores, int jobDisk, int jobMemory) {
		id = jobId;
		cores = jobCores;
		disk = jobDisk;
		memory = jobMemory;
	}
	
	public Job(String n) {
		String[] jobStr = n.split(" ",7);
		//jobStr[2] = jobid, str[4],[5],[6] = cores, disk, memory
		id = Integer.valueOf(jobStr[2]);
		cores = Integer.valueOf(jobStr[4]);
		disk = Integer.valueOf(jobStr[5]);
		memory = Integer.valueOf(jobStr[6]);
	}
	
	public String getID() {
		return Integer.toString(id);
	}
	
	public String getCores() {
		return Integer.toString(cores);
	}

	public String getDisk() {
		return Integer.toString(disk);
	}

	public String getMemory() {
		return Integer.toString(memory);
	}
	
	public int getCoresInt() {
		return cores;
	}

	public int getDiskInt() {
		return disk;
	}

	public int getMemoryInt() {
		return memory;
	}
	
	public void setId(int newId) {
		id = newId;
	}
	
	public void setCores(int newCores) {
		cores = newCores;
	}

	public void setDisk(int newDisk) {
		disk = newDisk;
	}

	public void setMemory(int newMemory) {
		memory = newMemory;
	}
	
	public String getMigrationInfo() {
		return Integer.toString(id) + " " + serverType + " " + serverID;
	}
	
}
