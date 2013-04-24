/* @arthur: Hua Yang */
import java.io.*;
import java.net.*;
import java.util.*;


public class CalcServer implements Runnable {
	Socket conn;

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
		// default port #
		int port = 0;

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
	 * Calculator method.
	 * Does the given calculation (+,-,*, or /) with the previous two inputs, if possible.
	 * Errors if the current stack size is less than one.
	 */
	public String function(Stack<Double> stack, int method) {
		if (stack.size() <= 1) { // checks the size of the stack
			return "=> Not enough numbers on the stack!";
		}
		// pops the 2 values in the stack for calculation
		Double a = stack.pop();
		Double b = stack.pop();
		Double c = 0.0;
		switch(method) {
			case 1: // addition
				c = b+a; break;
			case 2: // subtraction
				c = b-a; break;
			case 3: // multiplication
				c = b*a; break;
			case 4: // division
				c = b/a; break;
			default: // should never happen!
				return "Error!";
		}
		// push the new value back into the stack
		stack.push(c);
		return "=> " + c;
	}

	/*
	 * Print the existing values in the stack.
	 * Retains the original values in the stack by saving it in a temperary stack then back
	 */
	public String print(Stack<Double> stack) {
		if (stack.empty()) { // error message if stack is empty
			return "The stack is empty!";
		}
		// temperary stack to hold the original values
		Stack<Double> copy = new Stack<Double>();
		String output = "";
		Double temp;
		
		while (!stack.empty()) { // loops through the original stack
			temp = stack.pop();
			// stores the value in a string separated by ';'
			output += "=> " + temp + ";";
			// saves the original values in temperary stack
			copy.push(temp);
		}
		while (!copy.empty()) { // loops through the temperary stack
			temp = copy.pop();
			// copy over the original values to the original stack
			stack.push(temp);
		}
		return output;
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
			Stack<Double> stack = new Stack<Double>();
			String line;
			// boolean for closing the connectiong
			boolean quit = false;

			// continues to read client inputs till 'end' to end the connection
			while ((line = fromClient.readLine()) != null) {
				System.out.println("read \"" + line + "\"");
				String output = "";

				switch(line.toLowerCase()) {
					case "end": // for ending the connection
						quit = true; break;
					case "show": // for printing out the existing values in the stack
						output = print(stack); break;
					case "+": // for addition
						output = function(stack, 1); break;
					case "-": // for subtraction
						output = function(stack, 2); break;
					case "*": // for multiplication
						output = function(stack, 3); break;
					case "/": // for division
						output = function(stack, 4); break;
					default: // attempts to store the client input
						try { stack.push(Double.parseDouble(line)); } 
						catch (Exception e) { // if client input is not a number
							output = "=> ?";
						} break;
				}
				output += '\n';
				toClient.writeBytes(output);
				if (quit) { break; }
			}
			System.out.println("Closing the connection.");
			conn.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
