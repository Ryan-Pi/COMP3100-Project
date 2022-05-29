import java.util.ArrayList;

public class Stage2 extends Scheduler{
	//This is the algorithm written for stage 2 of the assignment
	//it is a modified first fit algorithm
	//with migration capability
	
	//this boolean checks what the Messenger
	//needs to wait for as a response from
	//ds-server when it sends the scheduling decision
	boolean available = true;
	
	public Stage2(Messenger messenger) {
		super(messenger);
	}
	
	
	//this is a method from the Scheduler superclass
	//which has been overridden as we want to get Available servers
	//and not Capable Servers
	@Override
	public void getServerInfo() {
		write("GETS Avail " + job.getCores() + " " + job.getMemory() + " " + job.getDisk());
		read();
		String[] serverStr = str.split(" ",3);
		numberOfServers = Integer.valueOf(serverStr[1]);
		write("OK");
	}
	
	public void findServer(int numberOfServers) {
		//finds first server that is readily available to execute job
		//if none, finds capable server with lowest estimated waiting time
		//modified first fit
		ArrayList<Server> availableServers = new ArrayList<Server>();
		for(int i = 0; i < numberOfServers; i++) {
			read();
			Server server = new Server(str);
			availableServers.add(server);
		}
		//if there is at least one
		//available server
		//schedule the job to the first
		//such server
		if(availableServers.size()>0) {
			available = true;
			targetServer = availableServers.get(0);
		} else {
			//if there are no available servers
			//request information on all capable servers
			//then request information on the estimated wait time
			//of each server and schedule the job to the server
			//with the least amount of estimated wait time
			available = false;
			write("OK");
			waitFor(".");
			ArrayList<Server> capableServers = new ArrayList<Server>();
			//get all capable servers
			write("GETS Capable " + job.getCores() + " " + job.getMemory() + " " + job.getDisk());
			while(!str.contains("DATA")) {
				read();
			}
			String[] serverStr = str.split(" ",3);
			//get number of servers sent
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
				//request estimated wait time of server
				write("EJWT " + server.getServerType() + " " + server.getServerID());
				read();
				int serverWait = Integer.valueOf(str);
				if(serverWait < waitTime) {
					//if the estimated wait time of the server
					//is less than the target server,
					//it becomes the new target server
					//and its wait time becomes the new wait time to compare
					//between servers
					targetServer = server;
					waitTime = serverWait;
				}
			}
		}
	}
	
	//method from Scheduler superclass
	//overridden as sometimes there is no need to wait 
	//for ds-server to respond with "."
	//as a result of requesting waiting time using "EJWT"
	//which will make the server respond with an int when 
	//it receives "OK" and if the messenger waits for
	//".", it will wait forever
	@Override
	public void sendSchedule() {
		write("OK");
		if(available) {
			waitFor(".");
		}
		//sends ds-server scheduling decision
		write("SCHD " + job.getID() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
		waitFor("OK");
		write("REDY");
	}

	@Override
	public void migrate() {
		//migrates one waiting job by first fit
		//the first available server that can take a waiting job
		//is given the job unless
		ArrayList<Server> waitServers = new ArrayList<Server>();
		boolean migrated = false;
		//get all servers to check if they have waiting jobs
		write("GETS All");
		read();
		String[] serverStr = str.split(" ",3);
		int serverNo = Integer.valueOf(serverStr[1]);
		write("OK");
		for(int i = 0; i < serverNo; i++) {
			//put all servers with waiting jobs in a list
			read();
			Server server = new Server(str);
			if(server.getServerWaitingJobsInt()>0) {
				waitServers.add(server);
			}
		}
		write("OK");
		waitFor(".");
		if(waitServers.size()>0) {
			//if there are servers with waiting jobs,
			//get the first such job and try to find a 
			//new server to migrate
			//if no such server exists, attempt to do the
			//same for the second job in the list
			//once one job has been migrated, end the migration and wait
			//for ds-server to either send JOBN for scheduling a job or
			//JCPL for migrating a job
			for(int i = 0; i < waitServers.size();i++) {
				Server server = waitServers.get(i);
				ArrayList<Job> waitJobs = new ArrayList<Job>();
				//get list of all jobs on server in wait list
				write("LSTJ " + server.getServerType() + " " + server.getServerID());
				while(!str.contains("DATA")) {
					read();
				}
				String[] jobStr = str.split(" ",3);
				int jobNo = Integer.valueOf(jobStr[1]);
				write("OK");
				//get number of jobs from DATA
				for(int j = 0; j < jobNo; j++) {
					//add waiting jobs to list
					read();
					Job temp = new Job(str, server);
					waitJobs.add(temp);
				}
				write("OK");
				waitFor(".");
				//find if one of the waiting jobs has an 
				//available server that it can use
				for(int j = 0; j < waitJobs.size(); j++) {
					ArrayList<Server> availableServers = new ArrayList<Server>();
					Job temp = waitJobs.get(j);
					//get all immediately available servers for the current job
					write("GETS Avail " + temp.getCores() + " " + temp.getMemory() + " " + temp.getDisk());
					while(!str.contains("DATA")){
						read();
					}
					String[] avServerStr = str.split(" ", 3);
					//get number of available server information
					//that ds-server is sending
					int serverNum = Integer.valueOf(avServerStr[1]);
					write("OK");
					for(int k = 0; k < serverNum; k++) {
						read();
						Server avServer = new Server(str);
						//add servers to available servers list
						//unless the job is already queued on that server
						if(avServer.getServerType().equals(temp.getServerType())) {
							if(avServer.getServerID().equals(temp.getServerID())) {
								//if the server type of current server is the same
								//but the ID is different from that of the server
								//the job is assigned to, add it to the list
								availableServers.add(avServer);
							}
						} else {
							//if server type of current server is different
							//from server type of server that the job is assigned to
							//add it to the list
							availableServers.add(avServer);
						}
					}
					write("OK");
					waitFor(".");
					//
					if(availableServers.size()>0) {
						//if available servers, not including the job's assigned server,
						//are greater than 0, check that the job is not already running
						//on its assigned server, and if it is not, migrate it to the
						//first available server on the list
						boolean isRunning = false;
						targetServer = availableServers.get(0);
						//get list of all jobs and their status
						//on the job's currently assigned server
						write("LSTJ " + temp.getServerType() + " " + temp.getServerID());
						while(!str.contains("DATA")) {
							read();
						}
						String[] jobInfo = str.split(" ",3);
						//get number of jobs send by ds-server
						int jobNum = Integer.valueOf(jobInfo[1]);
						write("OK");
						//get number of jobs from DATA
						for(int k = 0; k < jobNum; k++) {
							//add waiting jobs to list
							read();
							Job work = new Job(str, server);
							if(work.getID().equals(temp.getID())) {
								if(work.getStatus().equals("2")) {
									//checks if the job is already running
									isRunning = true;
								}
								break;
							}
						}
						write("OK");
						waitFor(".");
						//migrate job to target server
						//unless the job is already running
						//on its assigned server
						if(!isRunning) {
							write("MIGJ " + temp.getMigrationInfo() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
							waitFor("OK");
							migrated = true;
						}
					} else {
						ArrayList<Server> capableServers = new ArrayList<Server>();
						//if no available servers to migrate to
						//check if any capable servers are faster
						// and migrate waiting job to that server if it exists
						write("GETS Capable " + temp.getCores() + " " + temp.getMemory() + " " + temp.getDisk());
						while(!str.contains("DATA")) {
							read();
						}
						String[] cpServerStr = str.split(" ", 3);
						int serverN = Integer.valueOf(cpServerStr[1]);
						write("OK");
						for(int k = 0; k < serverN; k++) {
							read();
							Server cpServer = new Server(str);
							//add capable servers to list
							//unless the job we are migrating
							//is already on that server
							if(cpServer.getServerType().equals(temp.getServerType())) {
								if(!cpServer.getServerID().equals(temp.getServerID())) {
									capableServers.add(cpServer);
								}
							} else {
								capableServers.add(cpServer);
							}
						}
						write("OK");
						waitFor(".");
						//check estimated wait time of the
						//job's current server
						write("EJWT " + temp.getServerType() + " " + temp.getServerID());
						read();
						//set wait time as the wait time of the server
						// the job is currently on
						int waitTime = Integer.valueOf(str);
						if(capableServers.size()>0) {
							//if capable servers, not including the job's assigned server,
							//are greater than 0, find the estimated waiting time of the assigned server.
							//Then find if another server has a lower estimated waiting time, if so,
							//migrate the job to the server with the lowest estimated waiting time
							for(int k = 0; k < capableServers.size(); k++) {
								Server cpServer = capableServers.get(k);
								write("EJWT " + cpServer.getServerType() + " " + cpServer.getServerID());
								read();
								int serverWait = Integer.valueOf(str);
								//if wait time of any capable server is shorter than current server
								//set waitTime to that server and set it as target server
								if(serverWait < waitTime) {
									migrated = true;
									targetServer = cpServer;
									waitTime = serverWait;
								}
							}
							if(migrated) {
								//if a capable server has lower estimated waiting time than
								//the job's assigned server, migrate the job to the specified server
								//unless the job is already running on its current server
								boolean isRunning = false;
								//get list of jobs from job's assigned server
								write("LSTJ " + temp.getServerType() + " " + temp.getServerID());
								while(!str.contains("DATA")) {
									read();
								}
								String[] jobInfo = str.split(" ",3);
								int jobNum = Integer.valueOf(jobInfo[1]);
								write("OK");
								//get number of jobs from DATA
								for(int k = 0; k < jobNum; k++) {
									//add waiting jobs to list
									read();
									Job work = new Job(str, server);
									if(work.getID().equals(temp.getID())) {
										if(work.getStatus().equals("2")) {
											isRunning = true;
										}
										break;
									}
								}
								write("OK");
								waitFor(".");
								//migrate job to target server unless
								//it is already running on its assigned server
								if(!isRunning) {
									write("MIGJ " + temp.getMigrationInfo() + " " + targetServer.getServerType() + " " + targetServer.getServerID());
									waitFor("OK");
									migrated = true;
								}
							}
						}
					}
					//if migration has occurred, 
					//break out of waitJobs loop
					if(migrated) {
						break;
					}
				}
				//if migration has occurred
				//break out of waitServers loop
				if(migrated) {
					break;
				}
			}
		} else {
			//if no servers have waiting jobs
			//do nothing
		}
		write("REDY");
	}

}
