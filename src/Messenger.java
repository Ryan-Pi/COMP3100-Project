import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//this class contains methods for writing to output stream and reading from input stream,
//this class is responsible for all communication functions between
//the client and ds-server
public class Messenger {
	BufferedReader in;
	DataOutputStream dout;
	String str = new String();
	
	public Messenger(Socket client) throws IOException {
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		dout = new DataOutputStream(client.getOutputStream());
	}
	
	public void message(String n) {
		n = n + "\n";
		try {
			dout.write((n).getBytes());
			dout.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String read() {
		try {
			str = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public String getCurrentLine() {
		return str;
	}
	
	public void waitFor(String n) {
		//clears string, as otherwise the client may send another message too quickly
		//and cause communications to stop between ds-server and the scheduler
		while(!str.equals(n)) {
			str = read();
		}
		str = "";
	}

}
