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

		if(args.length > 1){
			System.out.println("Invalid number of arguments\n");
		}
	}
}