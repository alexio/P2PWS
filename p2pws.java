/*Alexio Mota*/
import java.io.*;
import java.net.*;
import java.util.*;

public class p2pws implements Runnable{

	Socket conn;

	p2pws(Socket sock){
		this.conn = sock;
	}

	public static void main(String[] args){

		int port;
		if(args.length != 1){
			System.out.println("Invalid number of arguments\n");
			return;
		}

		try{
			port = Integer.parseInt(args[0]);
			System.out.println("Port: " + port);
		}
		catch(NumberFormatException nfe){
			System.out.println(nfe);
			return;
		}

		try{
			//Create server socket on provided port
			ServerSocket server_sock = new ServerSocket(port);
			for(;;){
				Socket conn = server_sock.accept();
				System.out.println("Connection Established");

				//create thread for each new connection
				new Thread(new p2pws(conn)).start();
			}
		}
		catch(IOException e){
			System.out.println(e);
			return;
		}

	}

	public void run(){
		try {
			// input and output to client
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());
			// stack to store the client inputs
			String line;
			// boolean for closing the connectiong
			boolean quit = false;

			// continues to read client inputs till 'end' to end the connection
			while ((line = fromClient.readLine()) != null) {
				System.out.println("read \"" + line + "\"");
				HTTP_Request(line);
				toClient.writeBytes("Ok\n");
			}
			System.out.println("Closing the connection.");
			conn.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	//Evaluates received request from client/peer
	public void HTTP_Request(String input){
		
	}

	public String response(String cmd){

		switch(cmd){
			case "1": //if Content not found
				return "HTTP/1.1 404 Not Found\nContent-Length: 0";
			default: //request was a success
				return "HTTP/1.1 200 OK\nContent-Length: 0";
		}
	}
}