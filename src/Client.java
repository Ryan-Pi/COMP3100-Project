import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.DataInputStream;

public class Client {

	public static void main(String[] args) {
		try {
			Socket client = new Socket("localhost",50000);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			DataOutputStream dout = new DataOutputStream(client.getOutputStream());
			boolean atlYes = false;
			boolean atl = false;
			 if(args[0].equals("-atl")){
			 	atlYes = true;
			 }
			dout.write(("HELO\n").getBytes());
			dout.flush();
			String str = new String();
			while(!str.equals("OK")) {
			str = in.readLine();
			}
			dout.write(("AUTH vm\n").getBytes());
			dout.flush();
			if(atlYes){
			System.out.println("ATL!");
			}
			while(!str.equals("OK")) {
				str = in.readLine();
			}
			dout.write(("REDY\n").getBytes());
			dout.flush();
			while(!str.equals("NONE")) {
					String serverType = new String();
					boolean first = true;
				while(str.contains("JOBN")){
					String[] str2 = str.split(" ",7);
					//str2[2] = jobid
					//str[4],[5],[6] = cores, disk, memory
					System.out.println("GETTING");
					String gets = "GETS " + "Capable " + str2[4] + " " + str2[5] + " " + str2[6] + "\n";
					dout.write(gets.getBytes());
					dout.flush();
					
					String[][] servers;
					int serverChoice = 0;
					int cores = 0;
					System.out.println("WAITING");
					while(!str.contains("DATA")){
						str = in.readLine();
					}
					String[] str3 = str.split(" ", 3); //str3[1] = no. servers
					int serverNo = Integer.valueOf(str3[1]);
					System.out.println("ServerNO = " + serverNo);
					dout.write(("OK\n").getBytes());
					dout.flush();
					System.out.println("SERVERS");
					servers = new String[serverNo][9];
						for(int i = 0; i < serverNo; i++){
							str = in.readLine();
							servers[i] = str.split(" ",7);
							if(Integer.valueOf(servers[i][4]) > cores){
								cores = Integer.valueOf(servers[i][4]);
								System.out.println(servers[i][0] + " " + servers[i][4]);
								serverChoice = i;
							}
							// str[0] = serv type, str[1] = serv id, serv[4,5,6] = core, disk, mem
							System.out.println(Arrays.toString(servers[i]));
						}
					dout.write(("OK\n").getBytes());
					dout.flush();
					System.out.println("LOOPER");
					while(!str.equals(".")){
						str = in.readLine();
					}
					System.out.println("LOOP");
					dout.write(("OK\n").getBytes());
					dout.flush();
					//str2[2] == job id
					String schd = "SCHD " + str2[2] + " " + servers[serverChoice][0] + " " + servers[serverChoice][1] + "\n";
					dout.write(schd.getBytes());
					dout.flush();
					while(!str.equals("OK")){
						str = in.readLine();
					}
					dout.write(("REDY\n").getBytes());
					dout.flush();
					str = in.readLine();
				}
				
				if(str.contains("JCPL")){
					dout.write(("REDY\n").getBytes());
					dout.flush();
				}
				str = in.readLine();
			}
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

}
