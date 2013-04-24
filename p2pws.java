/*Alexio Mota*/
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Object;

public class p2pws implements Runnable{

	Hashtable<String, String> files;
	Socket conn;
    int current_port;

	p2pws(Socket sock, int socket){
		this.conn = sock;
		this.files = new Hashtable<String, String>();
	    this.current_port = socket;
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
				new Thread(new p2pws(conn, port)).start();
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
				
				String tokens[] = line.split(" ");
				//Check to see if the line contains a valid request
				if((tokens[0].equals("PUT") || tokens[0].equals("DELETE")) && tokens.length == 3){
					HTTP_Request(tokens, fromClient);
				} else if (tokens[0].equals("GET")) {
                    try {
                        functions.HTTP_Get(tokens[1], toClient, current_port, files);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

				toClient.writeBytes("Ok\n");
			}
			System.out.println("Closing the connection.");
			conn.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	//Evaluates received request from client/peer
	public void HTTP_Request(String[] input, BufferedReader fromClient){

		switch(input[0]){
			case "PUT":
				try{
					wsPUT(input, fromClient);
				}
				catch(IOException e){
					System.out.println(e);
					return;
				}
				break;
			case "DELETE":
				wsDELETE(input, fromClient);
				break;
			default:
				System.out.println("Incorrect Input");
				break;
		}
	}

	public String response(String cmd){

		switch(cmd){
			case "1": //if Content not found
				return "HTTP/1.1 404 Not Found\nContent-Length: 0";
			default: //request was a success
				return "HTTP/1.1 200 OK\nContent-Length: 0";
		}
	}

	public void wsPUT(String[] input, BufferedReader fromClient) throws IOException{
		//removes content-length line
		String line = fromClient.readLine();
		String size[] = line.split(" ");
		int limit = Integer.parseInt(size[1]); //Get the Content-Length: ?

		String content = "";
		while (limit > 0) { //Loop will run until the Content-length is 0 or less

			line = fromClient.readLine();
			limit-=line.length();
			limit--;
			content+=line;
		}
		content+=line;
		files.put(input[2], content);
	}	

	public void wsDELETE(String[] input, BufferedReader fromClient){
		String dlt_file = input[1];
		//Remove file content from hash map
	}
}
