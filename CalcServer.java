// Alexio Mota
import java.io.*;
import java.net.*;
import java.util.*;

public class CalcServer implements Runnable {

	Socket conn;
	CalcServer(Socket sock){
		this.conn = sock;
	}

	public static void main(String args[]) throws Exception{

		int port = 12345;

		if(args.length > 1){
			System.out.println("More than one argument present, invalid\n");
			System.exit(1);
		}
		else if(args.length == 1){

			try{
				//catch exception if arg[0] is not an int
				port = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException nfe){
				System.out.println(nfe);
				System.exit(2);
			}
		}

		try{
			
			ServerSocket server_sock = new ServerSocket(12345, 5);
			for(;;){

				Socket conn = server_sock.accept(); //get connection
				System.out.println("connection establish");

				//create thread for each new connection
				new Thread(new CalcServer(conn)).start();
			}
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(3);
		}
	}

	public void run(){
		try{
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());
			
			Stack<Double> stck = new Stack<Double>();
			String line;
			while((line = fromClient.readLine()) != null){ //while there's data coming from client
				
				double output = 0;

				/*perform the specified operations on the top 2 numbers in the stack. If
				the stack doesn't contain at least two numbers error is reported.*/
				if(line.equals("+")){ //addition

					if(stck.size() < 2){
						toClient.writeBytes("not enough numbers on the stack!\n");
					}
					else{
						output = stck.pop();
						output = stck.pop() + output;
						stck.push(output);
						toClient.writeBytes(Double.toString(output) + '\n');
					}
				}
				else if(line.equals("-")){ //substraction

					if(stck.size() < 2){
						toClient.writeBytes("not enough numbers on the stack!\n");
					}
					else{
						output = stck.pop();
						output = stck.pop() - output;
						stck.push(output);
						toClient.writeBytes(Double.toString(output) + '\n');
					}

				}
				else if(line.equals("*")){ //multiplication

					if(stck.size() < 2){
						toClient.writeBytes("not enough numbers on the stack!\n");
					}
					else{
						output = stck.pop();
						output = stck.pop() * output;
						stck.push(output);
						toClient.writeBytes(Double.toString(output) + '\n');
					}
				}
				else if(line.equals("/")){ //divison

					if(stck.size() < 2){
						toClient.writeBytes("not enough numbers on the stack!\n");
					}
					else{
						output = stck.pop();
						output = stck.pop() / output;
						stck.push(output);
						toClient.writeBytes(Double.toString(output) + '\n');
					}
				}
				else if(line.equals("show")){ //send all stack items to client

					Stack<Double> temp = new Stack<Double>();
					while(!stck.empty()){
						temp.push(stck.pop());
						toClient.writeBytes(Double.toString(temp.peek()) +'\n');
					}
 
					while(!temp.empty()){
						stck.push(temp.pop());
					}
				}
				else{ //store elements in stack
					try{
						stck.push(Double.parseDouble(line));
					}
					catch(NumberFormatException nfe){
						toClient.writeBytes("?\n");
					}
				}
			}

			System.out.println("Connection go bye bye\n");
			conn.close(); //close connection and exit the thread
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(5);
		}
	}	
}