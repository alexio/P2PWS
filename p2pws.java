/*Alexio Mota*/
import java.io.*;
import java.net.*;
import java.util.*;

public class p2pws implements Runnable{

	Socket conn;

	p2pServer(Socket sock){
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

	}
}