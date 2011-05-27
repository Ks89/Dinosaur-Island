package isoladinosauri;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import isoladinosauri.Client;


public class Client {
	
	private String host;
	private int port;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void runClient() throws UnknownHostException, IOException, InterruptedException {
		System.out.println("Connecting to server. Host: " + host + " - port: " + port);
		Socket socket = new Socket(host, port);
		System.out.println("Connected.");
		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		//		String request = "@getTime";
		String request;
		String answer;
		while (true){
			request = keyboardReader.readLine();
			System.out.println("Sending request to server: " + request);
			bufferedWriter.write(request);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			answer = bufferedReader.readLine();
			if (answer != null) {
				System.out.println("Server response: " + answer);
			}
		}
//		socket.close();
//		System.out.println("Terminating client.");
	}
		public static void main(String[] args) {
			Client client = new Client("localhost", 1234);
			try {
				client.runClient();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
