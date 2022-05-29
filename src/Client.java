import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedReader;

public class Client {
	Socket client;
	BufferedReader in;
	DataOutputStream dout;
	Messenger messenger;
	Scheduler scheduler;
	String algo;
	String str = new String();
//	String serverType = new String();
//	boolean first = true;
//	int serverId = 0;
//	int max = 0;
	
	public Client(int port, String algorithm) {
		try {
			client = new Socket("localhost",port);
			messenger = new Messenger(client);
			algo = algorithm;
			if(algorithm.equals("new")){
				scheduler = new newAlgo(messenger);
			}else if(algorithm.equals("fc")) {
				scheduler = new fc(messenger);
			}else if(algorithm.equals("ff")) {
				scheduler = new ff(messenger);
			}else if(algorithm.equals("ffNew")) {
				scheduler = new ffNew(messenger);
			} else {
				scheduler = new lrr(messenger);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void schedule(String algorithm) {
//		try {
//			while(str.contains("JOBN")){
//				String[] jobStr = str.split(" ",7);
//				//jobStr[2] = jobid, str[4],[5],[6] = cores, disk, memory
//				write("GETS " + "Capable " + jobStr[4] + " " + jobStr[5] + " " + jobStr[6] + "\n");
//				while(!str.contains("DATA")){
//					str = in.readLine();
//				}
//				write("OK\n");
//				if(algorithm.equals("lrr")) {
//					if(first) {
//						findLargest();
//						first = false;
//					}
//				}
//				if(algorithm.equals("fc")) {
//					findFirst();
//				}
//				if(algorithm.equals("new")) {
//					findNew(Integer.valueOf(jobStr[4]));
//				}
//				write("OK\n");
//				waitFor(".");
//				write("SCHD " + jobStr[2] + " " + serverType + " " + serverId + "\n");
//				//str2[2] == job id
//				if(algorithm.equals("lrr")) {
//					serverId++;
//					if(serverId>=max) {
//						serverId = 0;
//					}
//				}
//				waitFor("OK");
//				write("REDY\n");
//				str = in.readLine();
//			}
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
//	public void findLargest() {
//		try {
//			String[] serverStr = str.split(" ", 3);
//			int serverNo = Integer.valueOf(serverStr[1]);
//			//serverStr[1] = number of servers
//			String[][] servers;
//			int count = 0;
//			int cores = 0;
//			servers = new String[serverNo][9];
//			for(int i = 0; i < serverNo; i++){
//				str = in.readLine();
//				servers[i] = str.split(" ",7);
//					if(Integer.valueOf(servers[i][4]) > cores){
//						serverType = servers[i][0];
//						count = 0;
//						cores = Integer.valueOf(servers[i][4]);
//					}
//					if(servers[i][0].equals(serverType)) {
//						count++;
//					}
//			}
//			max = count;
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
//	public void findFirst() {
//		try {
//			String[] serverStr = str.split(" ", 3);
//			int serverNo = Integer.valueOf(serverStr[1]);
//			//serverStr[1] = number of servers
//			str = in.readLine();
//			String server[] = str.split(" ",7);
//			serverType = server[0];
//			serverId = Integer.valueOf(server[1]);
//			for(int i = 0; i < serverNo-1; i++){ //serverNo -1 as we just want to get the first server,and this is to clear the input stream
//				str = in.readLine();
//			}
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
//	public void message(String algorithm) {
//		try {
//			while(!str.equals("NONE")) {
//				if(str.contains("JOBN")) {
//					schedule(str);
//				}
//				if(str.contains("JCPL")){
//					write("REDY");
//				}
//				str = in.readLine();
//			}	
//		} catch(Exception e) {
//			e.printStackTrace();
//		}	
//	}
	
	public void schedule(String j) {
		scheduler.schedule(j);
	}
	
	public void run() {
		while(!str.equals("NONE")) {
			if(str.contains("JOBN")) {
				//schedule a job
				scheduler.schedule(str);
			}
			if(str.contains("JCPL")) {
				scheduler.migrate();
				write("REDY");
			}
			str = read();
		}
		quit();
	}
	
	public void quit() {
		write("QUIT");
		try {
			System.exit(0);
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Scheduling finished successfully.");
	}
	
	public void write(String n) {
		messenger.message(n);
	}
	
	public String read() {
		return messenger.read();
	}
	
	public void waitFor(String n) {
		messenger.waitFor(n);
	}
	
	public void handshake() {
		//initial handshake between server and client
		write("HELO");
		waitFor("OK");
		write("AUTH client");
		waitFor("OK");
		write("REDY");
	}

	public static void main(String[] args) {
		String algorithm = "lrr";
		ArrayList<String> algoList = new ArrayList<String>();
		algoList.add("lrr");
		algoList.add("fc");
		algoList.add("ff");
		algoList.add("new");
		algoList.add("ffNew");
		//checks if specified algorithm to run is implemented
		//if not, then exits program
		//notifies user if program exits or if it is running using specified algorithm
		if(args[0].equals("-a")) {
			algorithm = args[1];
			if(algoList.contains(algorithm)==false) {
				System.out.println("Algorithm " + "\"" + algorithm + "\" not found, exiting program");
				System.exit(0);
			} else {
				System.out.println("Running using " + algorithm + " scheduling!");
			}
		}
		Client client = new Client(50000, algorithm);
		client.handshake();
		client.run();
	}

}
