/* @arthur: Hua Yang */
import java.io.*;
import java.net.*;
import java.lang.Object.*;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;

public class pClient {
	public static void main(String args[]) throws Exception {
		// default server name
		String line, server = "localhost";
		// default server port
		int port;
		Socket sock = null;
		BufferedReader userdata = new BufferedReader(new InputStreamReader(System.in));

		if (args.length != 2) { // incorrect input
			System.err.println("Incorrent input");
			return;
		}  
		else { // sets server name and port to the ones given by the user
			
			server = args[0];
			try {
				
				port = Integer.parseInt(args[1]);
				if (port < 1024 || port > 65535) { // checks if input is out of bounds
					
					System.out.println("Error: Port number out of bounds!");
					return;
				}
			} catch (NumberFormatException e) { // checks if given parameter is a number

				System.out.println("Error: Port must be a number!");
				return;
			}
		}

		// attempts to connect to the socket
		try {
			
			sock = new Socket(server, port);
			System.out.println("Using port: " + port);
		} 
		catch (UnknownHostException e) { // error if the hostname is incorrect
			
			System.out.println("Error: Hostname does not exist!");
			System.exit(1);
		} 
		catch (ConnectException e) { // error if the server is full
			
			System.out.println("Error: Server is currently full!");
			System.exit(1);
		} 
		catch (NoRouteToHostException e) { // error if the port number is incorrect
			
			System.out.println("Error: Port does not exist!");
			System.exit(1);
		} 
		catch (IOException e) { // other socket errors
			
			System.out.println("Error: " + e);
			System.exit(1);
		}

		System.out.println("Put Command format: \"PUT(or DELETE) /path/to/file/name\" \nAll other input will be invalid (Case Sensitive) \nType \"end\" to Quit\n");
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		// continues to read line from client till 'end' to close socket
		while ((line = userdata.readLine()) != null) {
            if (line.equals("end")) { break; }

			clientRequest(toServer, line); //Call the Put Method
			String result = fromServer.readLine();
			System.out.println("Server says: " + result);
		}
        toServer.close();
        fromServer.close();
		sock.close();
	}

	//If valid put command, put request will be sent server
	public static void clientRequest(DataOutputStream toServer, String input){

		//Split string into array of Strings. 0 index is command, 1 index is path
		String result[] = input.split(" "); 
		if(result.length > 2){ //Incorrect input from user
			System.out.println("Incorrect input");
			return;
		}
 	
	 	if(result[0].equals("PUT")){ //PUT Command
	 		String file; //Will contain contents of read file
			try{
				/*Attempt to read file*/
				file = fileScan(result[1]);
			}
			catch(IOException e){
				System.out.println(e);
				return;
			}

			try{
				/*Send Put request to Server*/
				toServer.writeBytes(result[0] + " " + result[1]+" HTTP/1.1\n");
				toServer.writeBytes("Content-Length: "+file.length()+"\n");
				toServer.writeBytes(file + "\n");
                System.out.println("Successfully PUT: " + result[1]);
			}
			catch(IOException e){
				System.out.println(e);
				return;
			}
		}
		else if(result[0].equals("DELETE")){ //Command DELETE was inputed

			try{
				/*Send DELETE request to Server*/
				toServer.writeBytes(result[0] + " " + result[1]+" HTTP/1.1\n");
			}
			catch(IOException e){
				System.out.println(e);
				return;
			}
		}
		else{
			System.out.println("Incorrect input");
			return;
		}

	}

	/*Used to print contents of a String array*/
	public static void printString(String result[]){
		for(int i = 0; i < result.length; i++){
			System.out.println("i: " + result[i]);
		}
	}

	/*Reads from file and stores contents in a String*/
	public static String fileScan(String filepath) throws IOException{
		FileInputStream stream = new FileInputStream(new File(filepath));
		try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    return Charset.defaultCharset().decode(bb).toString();
		}
	    finally {
		    stream.close();
		}
	}
}
