package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import client.Client;



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
		String request=null;
		String answer;
		String nickname;
		String password;
		String token;
		String nomeRazza;
		String tipoRazza;
		int scelta=0;
		Scanner input = new Scanner(System.in);
		while (true){
			while(scelta!=9) {
				System.out.println("1 - @creaUtente");
				System.out.println("2 - @login");
				System.out.println("3 - @creaRazza");
				System.out.println("9 - termina client");
				scelta = input.nextInt();
				switch(scelta) {
				case 1:
					bufferedWriter.flush();
					System.out.println("nome utente: ");
					nickname = keyboardReader.readLine();
					System.out.println("password: ");
					password = keyboardReader.readLine();
					request="@creaUtente,user="+nickname+",pass="+password;
					break;
				case 2:
					bufferedWriter.flush();
					System.out.println("nome utente: ");
					nickname = keyboardReader.readLine();
					System.out.println("password: ");
					password = keyboardReader.readLine();
					request="@login,user="+nickname+",pass="+password;
					break;
				case 3:
					bufferedWriter.flush();
					System.out.println("token: ");
					token = keyboardReader.readLine();
					System.out.println("nome razza: ");
					nomeRazza = keyboardReader.readLine();
					System.out.println("tipo [e/c]: ");
					tipoRazza = keyboardReader.readLine();
					request="@creaRazza,token="+token+",nome="+nomeRazza+",tipo="+tipoRazza;
					break;
				case 9:
					bufferedWriter.flush();
					break;
				default:
					bufferedWriter.flush();
					System.out.println("scelta non consentita\n");
					break;
				}
				if(scelta==1 || scelta==2 || scelta ==3) {
					System.out.println("Sending request to server: " + request);
					bufferedWriter.write(request);
					bufferedWriter.newLine();
					bufferedWriter.flush();
					answer = bufferedReader.readLine();
					if (answer != null) {
						System.out.println("Server response: " + answer);
					}
				}
			}
			socket.close();
			System.out.println("Terminating client.");
			break;
		}

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
