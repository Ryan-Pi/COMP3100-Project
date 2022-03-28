import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.DataInputStream;

public class Client {
	Socket client;
	BufferedReader in;
	DataOutputStream dout;
	String str = new String();
	String serverType = new String();
	String[][] largestServers;
	boolean first = true;
	int lrr = 0;
	
	public Client(int port) {
		try {
			client = new Socket("localhost",port);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			dout = new DataOutputStream(client.getOutputStream());
			dout.write(("HELO\n").getBytes());
			dout.flush();
			while(!str.equals("OK")) {
			str = in.readLine();
			}
			dout.write(("AUTH vm\n").getBytes());
			dout.flush();
			while(!str.equals("OK")) {
				str = in.readLine();
			}
			dout.write(("REDY\n").getBytes());
			dout.flush();
			message();
			quit();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void largestServers(String[][] servers, int count) {
		//creates an array consisting of servers with the largest serverType and their ids
		largestServers = new String[count][2];
		int num = 0;
		for(int i = 0; i < servers.length; i++) {
			if(servers[i][0].equals(serverType)) {
			// str[0] = serv type, str[1] = serv id, serv[4,5,6] = core, disk, memory
			largestServers[num][0] = servers[i][0];
			largestServers[num][1] = servers[i][1];
			num++;
			}
		}
	}
	
	public void schedule() {
		try {
		while(str.contains("JOBN")){
			String[] str2 = str.split(" ",7);
			//str2[2] = jobid, str[4],[5],[6] = cores, disk, memory
			String gets = "GETS " + "Capable " + str2[4] + " " + str2[5] + " " + str2[6] + "\n";
			dout.write(gets.getBytes());
			dout.flush();
			int cores = 0;
			while(!str.contains("DATA")){
				str = in.readLine();
			}
			String[] str3 = str.split(" ", 3); //str3[1] = no. servers
			int serverNo = Integer.valueOf(str3[1]);
			dout.write(("OK\n").getBytes());
			dout.flush();
			if(first) {
				String[][] servers;
				int count = 0;
				servers = new String[serverNo][9];
				for(int i = 0; i < serverNo; i++){
					str = in.readLine();
					servers[i] = str.split(" ",7);
						if(Integer.valueOf(servers[i][4]) > cores){
							serverType = servers[i][0];
							count = 0;
							cores = Integer.valueOf(servers[i][4]);
						}
					count++;
				}
				largestServers(servers, count);
				first = false;
			}
			dout.write(("OK\n").getBytes());
			dout.flush();
			while(!str.equals(".")){
				str = in.readLine();
			}
			//str2[2] == job id
			String schd = "SCHD " + str2[2] + " " + largestServers[lrr][0] + " " + largestServers[lrr][1] + "\n";
			lrr++;
			if(lrr>=largestServers.length) {
				lrr = 0;
			}
			dout.write(schd.getBytes());
			dout.flush();
			while(!str.equals("OK")){
				str = in.readLine();
			}
			dout.write(("REDY\n").getBytes());
			dout.flush();
			str = in.readLine();
		}
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
				dout.write(("REDY\n").getBytes());
				dout.flush();
			}
			str = in.readLine();
			}	
		} catch(Exception e) {
			System.out.println(e);
		}	
	}
	
	public void quit() {
		try {
			dout.write(("QUIT\n").getBytes());
			dout.flush();
			in.close();
			dout.close();
			System.out.println("Closing client");
			client.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	

	public static void main(String[] args) {
		Client client = new Client(50000);
	}

}
