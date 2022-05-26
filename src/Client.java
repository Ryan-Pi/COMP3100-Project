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
	String str = new String();
	String serverType = new String();
	boolean first = true;
	int serverId = 0;
	int max = 0;
	
	public Client(int port, String algorithm) {
		try {
			client = new Socket("localhost",port);
			messenger = new Messenger(client);
			if(algorithm.equals("new")){
				scheduler = new newAlgo(messenger);
			}else if(algorithm.equals("fc")) {
				scheduler = new fc(messenger);
			} else {
				scheduler = new lrr(messenger);
			}
			
			//in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			//dout = new DataOutputStream(client.getOutputStream());
			
//			write("HELO\n");
//			waitFor("OK");
//			write("AUTH vm\n");
//			waitFor("OK");
//			write("REDY\n");
//			message(algo);
//			quit();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void schedule(String algorithm) {
		try {
			while(str.contains("JOBN")){
				String[] jobStr = str.split(" ",7);
				//jobStr[2] = jobid, str[4],[5],[6] = cores, disk, memory
				write("GETS " + "Capable " + jobStr[4] + " " + jobStr[5] + " " + jobStr[6] + "\n");
				while(!str.contains("DATA")){
					str = in.readLine();
				}
				write("OK\n");
				if(algorithm.equals("lrr")) {
					if(first) {
						findLargest();
						first = false;
					}
				}
				if(algorithm.equals("fc")) {
					findFirst();
				}
				if(algorithm.equals("new")) {
					findNew(Integer.valueOf(jobStr[4]));
				}
				write("OK\n");
				waitFor(".");
				write("SCHD " + jobStr[2] + " " + serverType + " " + serverId + "\n");
				//str2[2] == job id
				if(algorithm.equals("lrr")) {
					serverId++;
					if(serverId>=max) {
						serverId = 0;
					}
				}
				waitFor("OK");
				write("REDY\n");
				str = in.readLine();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void findNew(int jobCores) {
		String[] serverStr = str.split(" ", 3);
		int serverNo = Integer.valueOf(serverStr[1]);
		//serverStr[1] = number of servers
		String[][] servers;
		servers = new String[serverNo][9];
		boolean found = false;
			for(int i = 0; i < serverNo; i++){
				try {
					str = in.readLine();
				} catch (Exception e) {
					System.out.println(e);
				}
				servers[i] = str.split(" ",7);
					if((Integer.valueOf(servers[i][4])-jobCores) >= 1){
						serverType = servers[i][0];
						serverId = Integer.valueOf(servers[i][1]);
						found = true;
					}
					if(i == (serverNo-1) && found == false) {
						serverType = servers[i][0];
						serverId = Integer.valueOf(servers[i][1]);
					}
			}
		}
	
	public void findLargest() {
		try {
			String[] serverStr = str.split(" ", 3);
			int serverNo = Integer.valueOf(serverStr[1]);
			//serverStr[1] = number of servers
			String[][] servers;
			int count = 0;
			int cores = 0;
			servers = new String[serverNo][9];
			for(int i = 0; i < serverNo; i++){
				str = in.readLine();
				servers[i] = str.split(" ",7);
					if(Integer.valueOf(servers[i][4]) > cores){
						serverType = servers[i][0];
						count = 0;
						cores = Integer.valueOf(servers[i][4]);
					}
					if(servers[i][0].equals(serverType)) {
						count++;
					}
			}
			max = count;
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
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
	
	public void message(String algorithm) {
		try {
			while(!str.equals("NONE")) {
				if(str.contains("JOBN")) {
					schedule(algorithm);
				}
				if(str.contains("JCPL")){
					write("REDY\n");
				}
				str = in.readLine();
			}	
		} catch(Exception e) {
			System.out.println(e);
		}	
	}
	
	public void run() {
		while(!str.equals("NONE")) {
			if(str.contains("JOBN")) {
				//schedule a job
				scheduler.schedule(str);
				
			}
			if(str.contains("JCPL")) {
				write("REDY");
			}
			str = read();
		}
		quit();
	}
	
	public void quit() {
		try {
			write("QUIT\n");
			in.close();
			dout.close();
			client.close();
			System.out.println("Scheduling finished successfully.");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/*
	public void newQuit() {
		messenger.message("QUIT\n");
		try {
		in.close();
		dout.close();
		client.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	*/
	
//	public void write(String n) {
//		try {
//			dout.write((n).getBytes());
//			dout.flush();
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
	public void write(String n) {
		n = n + "\n";
		messenger.message(n);
	}
	
	public String read() {
		return messenger.read();
	}
	
	public void waitFor(String n) {
		messenger.waitFor(n);
	}
	
//	public void waitFor(String n) {
//		//clears string, as otherwise the client may send another message too quickly due to 
//		//having OK in str while waiting for another OK message
//		//mainly created to ensure that the initial handshake between client and server occurs properly
//		try {
//			while(!str.equals(n)) {
//				str = in.readLine();
//			}
//			str = "";
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
	
	public void handshake() {
		//initial handshake between server and client
		message("HELO");
		messenger.waitFor("OK");
		message("AUTH vm");
		messenger.waitFor("OK");
		message("REDY");
	}

	public static void main(String[] args) {
		String algorithm = "lrr";
		ArrayList<String> algoList = new ArrayList<String>();
		algoList.add("fc");
		algoList.add("new");
		//change this to be within Client and to accept all strings but
		//default to lrr, and print a line to notify
		if(args[0].equals("-a")) {
			algorithm = args[1];
			if(algoList.contains(algorithm)==false) {
				System.out.println("Algorithm " + "\"" + algorithm + "\" not found, defaulting to LRR scheduling!");
			} else {
				System.out.println("Running using " + algorithm + "scheduling!");
			}
		}
		Client client = new Client(50000, algorithm);
		client.handshake();
		client.run();
	}

}
