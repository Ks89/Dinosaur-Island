package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import client.ClientGui;



public class ClientGui {

	private String host;
	private int port;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String request;
	private String answer;
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	private String token;

	public ClientGui(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getRispostaServer() throws IOException, InterruptedException{
		answer = bufferedReader.readLine();
		if (answer != null) {
			System.out.println("Server response: " + answer);
		}
		return answer;
	}


	public void creaUtente(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@creaUtente,user="+nomeUtente+",pass="+password;
		this.inviaAlServer();
	}

	public void eseguiLogin(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@login,user="+nomeUtente+",pass="+password;
		this.inviaAlServer();
	}

	public void creaRazza(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		this.token = nomeUtente + "-" + password;
		request="@creaRazza,token="+this.token+",nome="+nomeUtente+",tipo="+password;
		this.inviaAlServer();
	}

	public void logout() throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@logout,token="+this.token;
		this.inviaAlServer();
	}

	public void uscitaPartita() throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@uscitaPartita,token="+this.token;
		this.inviaAlServer();
	}

	public void mappaGenerale() throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@mappaGenerale,token="+this.token;
		this.inviaAlServer();
	}

	public void statoDinosauro(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@statoDinosauro,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}	
	
	public void accessoPartita(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@accessoPartita,token="+token;
		this.inviaAlServer();
	}
	
	public void vistaLocale(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@vistaLocale,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}
	
	public void crescitaDinosauro(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		request="@cresciDinosauro,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}
	
	public void inviaAlServer() throws IOException, InterruptedException{
		System.out.println("Sending request to server: " + request);
		bufferedWriter.write(request);
		bufferedWriter.newLine();
		bufferedWriter.flush();
		this.getRispostaServer();	
	}


	public void runClient() throws IOException, InterruptedException {
		System.out.println("Connecting to server. Host: " + host + " - port: " + port);
		this.socket = new Socket(host, port);
		System.out.println("Connected.");

		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		String token;
		String idDino;
		int scelta=0;
		Scanner input = new Scanner(System.in);
		while (true){
			while(scelta!=99) {
				System.out.println("6 - @listaGiocatori");
				System.out.println("9 - @listaDinosauri");
				System.out.println("10 - @vistaLocale");
				System.out.println("12 - @movimentoDinosauro");
				System.out.println("13 - @crescitaDinosauro");
				System.out.println("14 - @deponiUovo");
				System.out.println("99 - termina client");
				scelta = input.nextInt();
				switch(scelta) {
				case 6:
					bufferedWriter.flush();
					System.out.println("token: ");
					token = keyboardReader.readLine();
					request="@listaGiocatori,token="+token;
					break;
				case 9:
					bufferedWriter.flush();
					System.out.println("token: ");
					token = keyboardReader.readLine();
					request="@listaDinosauri,token="+token;
					break;
				case 12: //movimento dinosauro
					bufferedWriter.flush();
					System.out.println("token: ");
					token = keyboardReader.readLine();
					System.out.println("idDino: ");
					idDino = keyboardReader.readLine();
					System.out.println("destinazione: ");
					String destinazione = keyboardReader.readLine();
					request="@muoviDinosauro,token=" + token + ",idDino=" + idDino + ",dest={" + destinazione + "}";
					break;
				case 14:
					bufferedWriter.flush();
					System.out.println("token: ");
					token = keyboardReader.readLine();
					System.out.println("idDino: ");
					idDino = keyboardReader.readLine();
					request="@deponiUovo,token="+token+",idDino="+idDino;
					break;
				default:
					bufferedWriter.flush();
					System.out.println("scelta non consentita\n");
					break;
				}
			}
			socket.close();
			System.out.println("Terminating client.");
			break;
		}

	}
	public static void main(String[] args) {
		ClientGui client = new ClientGui("localhost", 1234);
		Gui gui = new Gui(client);
		LoginGui loginGui = new LoginGui(gui);
		loginGui.aggiuntaGiocatore();

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
