import java.io.DataOutputStream;
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
	
	public Client(int port, String algorithm) {
		try {
			client = new Socket("localhost",port);
			messenger = new Messenger(client);
			algo = algorithm;
			if(algorithm.equals("stage2")){
				scheduler = new Stage2(messenger);
			}else if(algorithm.equals("fc")) {
				scheduler = new FirstCapable(messenger);
			}else if(algorithm.equals("ff")) {
				scheduler = new FirstFit(messenger);
			}else if(algorithm.equals("lrr")){
				scheduler = new LargestRoundRobin(messenger);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

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
		algoList.add("stage2");
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
