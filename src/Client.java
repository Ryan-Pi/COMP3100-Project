import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;

public class Client {
	Socket client;
	BufferedReader in;
	DataOutputStream dout;
	String str = new String();
	String serverType = new String();
	boolean first = true;
	int lrr = 0;
	int max = 0;
	
	public Client(int port) {
		try {
			client = new Socket("localhost",port);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			dout = new DataOutputStream(client.getOutputStream());
			write("HELO\n");
			waitFor("OK");
			write("AUTH vm\n");
			waitFor("OK");
			write("REDY\n");
			message();
			quit();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void schedule() {
		try {
			while(str.contains("JOBN")){
				String[] jobStr = str.split(" ",7);
				//jobStr[2] = jobid, str[4],[5],[6] = cores, disk, memory
				write("GETS " + "Capable " + jobStr[4] + " " + jobStr[5] + " " + jobStr[6] + "\n");
				while(!str.contains("DATA")){
					str = in.readLine();
				}
				write("OK\n");
				if(first) {
					findLargest();
					first = false;
				}
				write("OK\n");
				waitFor(".");
				write("SCHD " + jobStr[2] + " " + serverType + " " + lrr + "\n");
				//str2[2] == job id
				lrr++;
				if(lrr>=max) {
					lrr = 0;
				}
				waitFor("OK");
				write("REDY\n");
				str = in.readLine();
			}
		} catch(Exception e) {
			System.out.println(e);
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
	
	public void message() {
		try {
			while(!str.equals("NONE")) {
				if(str.contains("JOBN")) {
					schedule();
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
	
	public void quit() {
		try {
			write("QUIT\n");
			in.close();
			dout.close();
			client.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void write(String n) {
		try {
			dout.write((n).getBytes());
			dout.flush();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void waitFor(String n) {
		//clears string, as otherwise the client may send another message too quickly due to 
		//having OK in str while waiting for another OK message
		//mainly created to ensure that the initial handshake between client and server occurs properly
		try {
			while(!str.equals(n)) {
				str = in.readLine();
			}
			str = "";
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		Client client = new Client(50000);
	}

}
