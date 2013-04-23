// Alexio Mota
import java.io.*;
import java.net.*;

public class CalcClient implements Runnable{

	Socket conn;

	CalcClient(Socket sock){
		this.conn = sock;
	}

	public static void main(String args[]) throws Exception{

		String server_name = "localhost";
		int port = 12345;
		if(args.length == 1){
			server_name = args[0]; //if there is 1 arg, server is set to it according to specs.
		}
		else if(args.length == 2){
			//if there are 2 args, both server and port are changed (as long as arg[1] is a valid input)
			server_name = args[0];
			try{
				port = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException nfe){
				System.out.println(nfe);
				System.exit(1);
			}

			if(port < 1024 || port > 65535){
				System.out.println("Port number given does not fall within 1024 and 65535");
				System.exit(2);
			}
			System.out.println("Server: " + server_name + " port " + port + "\n" );
		}
		else if(args.length > 0){
			System.err.println("Incorrect number of inputs\n");
			System.exit(3);
		}


		try{

			Socket sock = null;
			try{
				sock = new Socket("localhost", 12345);
			}
			catch(UnknownHostException uhe){
				System.out.println(uhe);
				System.exit(4);
			}

			
			if(sock == null){
				System.out.println("Connection not made, exiting! \n");
				System.exit(6);
			}

			//if connection is made, thread for user input is created. Thread also sends input to server
			new Thread(new CalcClient(sock)).start();
			
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String msg;
			while((msg = fromServer.readLine()) != null){ //while there's data from server
				System.out.println("=> " + msg);
			}
			sock.close(); //close connection
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(7);
		}
	}


	/*reads user input and send it to server*/
	public void run(){
		 
		 try{

			BufferedReader user_input = new BufferedReader(new InputStreamReader(System.in));
		    DataOutputStream toServer = new DataOutputStream(conn.getOutputStream());

		    String line;
			while((line = user_input.readLine()) != null){
				line.trim();
				toServer.writeBytes(line + '\n');
			}
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(5);
		}
		
	}
}