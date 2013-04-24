/*
 * @arthur: Hua Yang
 * @arthur: Alexio Mota
 */
import java.io.*;
import java.net.*;
import java.util.*;


public class CalcServer implements Runnable {
	Socket conn;
    static int port;

	/*
	 * Constructor
	 */
	CalcServer(Socket sock) {
		this.conn = sock;
	}

	/*
	 * Main method
	 */
	public static void main(String args[]) throws Exception {
		ServerSocket svc = null;
		port = 0;
		
        if (args.length == 0) { // if no parameter, use default port "12345"
			port = 12345;
		} else if (args.length == 1) { // if one parameter, set port = input
			try {
				port = Integer.parseInt(args[0]);
				if (port < 1024 || port > 65535) { // checks if input is out of bounds
					System.out.println("Error: Port number out of bounds!");
					System.exit(1);
				}
			} catch (NumberFormatException e) { // checks if given parameter is a number
				System.out.println("Error: Parameter must be a number!");
				System.exit(1);
			}
		} else { // incorrect input
			System.out.println("Usage: java CalcServer [port - optional]");
			System.exit(1);
		}

		try {
			svc = new ServerSocket(port, 5);
		} catch (BindException e) { // error if the desired port is in use/unavailable
			System.out.println("Error: Port number is in use!");
			System.exit(1);
		} catch (SocketException e) { // other socket errors
			System.out.println("Error: " + e);
			System.exit(1); 
		}

		// checks for new connections
		for (;;) {
			Socket conn = svc.accept();
			System.out.println("Received a new connection");
			new Thread(new CalcServer(conn)).start();
		}
	}
	
	/*
	 * Run method for each thread
	 */
	public void run() {
		try {
			// input and output to client
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());
			// stack to store the client inputs
            String[] tokens;
            String line, message, method, url;
			
            message = fromClient.readLine();
            if (message != null) {
                tokens = message.split(" ");
			    method = tokens[0];
                url = tokens[1];
            
                if (method.equals("GET")) {
                    try {
                        functions.Get(url, toClient, port);
                    } catch (Exception e) {
                        System.out.println("Error (GET): " + e);
                    }
                }
            }
            System.out.println("Closing the connection.");
			fromClient.close();
            toClient.close();
            conn.close();
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}
}
