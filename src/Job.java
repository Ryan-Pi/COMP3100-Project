//This class contains information about a job,
//namely its ID and its requirements for cores, disk and memory
//It has getters and setters for the above information as well
//getters will default to returning String values unless they have Int in the name e.g. getCoresInt()

public class Job {
	int id;
	int status;
	int cores;
	int disk;
	int memory;
	String serverType;
	String serverID;
	
	//this constructor is used in response to
	//ds-server responses to LSTJ
	public Job(String s, Server server) {
		String[] info = s.split(" ",8);
		id = Integer.valueOf(info[0]);
		status = Integer.valueOf(info[1]);
		cores = Integer.valueOf(info[5]);
		memory = Integer.valueOf(info[6]);
		disk = Integer.valueOf(info[7]);
		serverType = server.getServerType();
		serverID = server.getServerID();
	}
	
	//this constructor is used for JOBN
	public Job(String n) {
		String[] jobStr = n.split(" ",7);
		//jobStr[2] = jobid, str[4],[5],[6] = cores, disk, memory
		id = Integer.valueOf(jobStr[2]);
		cores = Integer.valueOf(jobStr[4]);
		memory = Integer.valueOf(jobStr[5]);
		disk = Integer.valueOf(jobStr[6]);
	}
	
	public String getID() {
		return Integer.toString(id);
	}
	
	public String getStatus() {
		return Integer.toString(status);
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
		//information needed for migration of a job
		//returns Job ID + the Server Type of the current server it is assigned to
		//+ the server ID of the current server it is assigned to
		return Integer.toString(id) + " " + serverType + " " + serverID;
	}
	
	public String getServerType() {
		return serverType;
	}
	
	public String getServerID() {
		return serverID;
	}
	
}
