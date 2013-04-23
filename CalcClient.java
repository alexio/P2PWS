/* @arthur: Hua Yang */
import java.io.*;
import java.net.*;

public class CalcClient {
	public static void main(String args[]) throws Exception {
		// default server name
		String line, server = "localhost";
		// default server port
		int port = 12345;
		Socket sock = null;
		BufferedReader userdata = new BufferedReader(new InputStreamReader(System.in));

		if (args.length > 2) { // incorrect input
			System.err.println("Usage: java TCPClient [server_name - optional [port - optional]]");
			System.exit(1);
		} else if (args.length == 1) { // sets server name to the one given by the user
			server = args[0];
		} else if (args.length == 2) { // sets server name and port to the ones given by the user
			server = args[0];
			try {
				port = Integer.parseInt(args[1]);
				if (port < 1024 || port > 65535) { // checks if input is out of bounds
					System.out.println("Error: Port number out of bounds!");
					System.exit(1);
				}
			} catch (NumberFormatException e) { // checks if given parameter is a number
				System.out.println("Error: Port must be a number!");
				System.exit(1);
			}
		}

		// attempts to connect to the socket
		try {
		sock = new Socket(server, port);
		System.out.println("Using port: " + port);
		} catch (UnknownHostException e) { // error if the hostname is incorrect
			System.out.println("Error: Hostname does not exist!");
			System.exit(1);
		} catch (ConnectException e) { // error if the server is full
			System.out.println("Error: Server is currently full!");
			System.exit(1);
		} catch (NoRouteToHostException e) { // error if the port number is incorrect
			System.out.println("Error: Port does not exist!");
			System.exit(1);
		} catch (IOException e) { // other socket errors
			System.out.println("Error: " + e);
			System.exit(1);
		}

		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		// delimiters to parse the output from server
		String delimiters = "[;\\n]";

		// continues to read line from client till 'end' to close socket
		while ((line = userdata.readLine()) != null) {
			toServer.writeBytes(line + '\n');
			// reads and parse output from server
			String result = fromServer.readLine();
			if (!result.equals("")) {
				String[] tokens = result.split(delimiters);
				for (int i = 0; i < tokens.length; i++) {
					System.out.println(tokens[i]); 
				}
			}
			if (line.equals("end")) { break; }
		}
		sock.close();
	}
}
