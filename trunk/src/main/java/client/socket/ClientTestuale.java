package client.socket;
//package client;
//
//import java.io.*;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.Scanner;
//import client.ClientTestuale;
//
//
//
//public class ClientTestuale {
//	
//	private String host;
//	private int port;
//	
//	public ClientTestuale(String host, int port) {
//		this.host = host;
//		this.port = port;
//	}
//	
//	public void runClient() throws IOException, InterruptedException {
//		System.out.println("Connecting to server. Host: " + host + " - port: " + port);
//		Socket socket = new Socket(host, port);
//		System.out.println("Connected.");
//		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//		String request=null;
//		String answer;
//		String nickname;
//		String password;
//		String token;
//		String nomeRazza;
//		String idDino;
//		String tipoRazza;
//		int scelta=0;
//		Scanner input = new Scanner(System.in);
//		while (true){
//			while(scelta!=99) {
//				System.out.println("1 - @creaUtente");
//				System.out.println("2 - @login");
//				System.out.println("3 - @creaRazza");
//				System.out.println("4 - @accessoPartita");
//				System.out.println("5 - @uscitaPartita");
//				System.out.println("6 - @listaGiocatori");
//				System.out.println("7 - @logout");
//				System.out.println("8 - @mappaGenerale");
//				System.out.println("9 - @listaDinosauri");
//				System.out.println("10 - @vistaLocale");
//				System.out.println("11 - @statoDinosauro");
//				System.out.println("12 - @movimentoDinosauro");
//				System.out.println("13 - @crescitaDinosauro");
//				System.out.println("14 - @deponiUovo");
//				System.out.println("99 - termina client");
//				scelta = input.nextInt();
//				switch(scelta) {
//				case 1:
//					bufferedWriter.flush();
//					System.out.println("nome utente: ");
//					nickname = keyboardReader.readLine();
//					System.out.println("password: ");
//					password = keyboardReader.readLine();
//					request="@creaUtente,user="+nickname+",pass="+password;
//					break;
//				case 2:
//					bufferedWriter.flush();
//					System.out.println("nome utente: ");
//					nickname = keyboardReader.readLine();
//					System.out.println("password: ");
//					password = keyboardReader.readLine();
//					request="@login,user="+nickname+",pass="+password;
//					break;
//				case 3:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					System.out.println("nome razza: ");
//					nomeRazza = keyboardReader.readLine();
//					System.out.println("tipo [e/c]: ");
//					tipoRazza = keyboardReader.readLine();
//					request="@creaRazza,token="+token+",nome="+nomeRazza+",tipo="+tipoRazza;
//					break;
//				case 4:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					request="@accessoPartita,token="+token;
//					break;
//				case 5:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					request="@uscitaPartita,token="+token;
//					break;
//				case 6:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					request="@listaGiocatori,token="+token;
//					break;
//				case 7:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					request="@logout,token="+token;
//					break;
//				case 8:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					request="@mappaGenerale,token="+token;
//					break;
//				case 9:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					request="@listaDinosauri,token="+token;
//					break;
//				case 10:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					System.out.println("idDino: ");
//					idDino = keyboardReader.readLine();
//					request="@vistaLocale,token="+token+",idDino="+idDino;
//					break;
//				case 11:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					System.out.println("idDino: ");
//					idDino = keyboardReader.readLine();
//					request="@statoDinosauro,token="+token+",idDino="+idDino;
//					break;
//				case 12: //movimento dinosauro
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					System.out.println("idDino: ");
//					idDino = keyboardReader.readLine();
//					System.out.println("destinazione: ");
//					String destinazione = keyboardReader.readLine();
//					request="@muoviDinosauro,token=" + token + ",idDino=" + idDino + ",dest={" + destinazione + "}";
//					break;
//				case 13:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					System.out.println("idDino: ");
//					idDino = keyboardReader.readLine();
//					request="@cresciDinosauro,token="+token+",idDino="+idDino;
//					break;	
//				case 14:
//					bufferedWriter.flush();
//					System.out.println("token: ");
//					token = keyboardReader.readLine();
//					System.out.println("idDino: ");
//					idDino = keyboardReader.readLine();
//					request="@deponiUovo,token="+token+",idDino="+idDino;
//					break;
//				default:
//					bufferedWriter.flush();
//					System.out.println("scelta non consentita\n");
//					break;
//				}
//				if(scelta!=99) {
//					System.out.println("Sending request to server: " + request);
//					bufferedWriter.write(request);
//					bufferedWriter.newLine();
//					bufferedWriter.flush();
//					answer = bufferedReader.readLine();
//					if (answer != null) {
//						System.out.println("Server response: " + answer);
//					}
//				}
//			}
//			socket.close();
//			System.out.println("Terminating client.");
//			break;
//		}
//
//	}
//		public static void main(String[] args) {
//			ClientTestuale client = new ClientTestuale("localhost", 1234);
//			try {
//				client.runClient();
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//	}
//
//}
