package com.sarxos.gadacz.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

import com.sarxos.gadacz.server.GadaczCommands;
import com.sarxos.gadacz.server.GadaczServer;


public class GadaczClientThread extends Thread {

	private String login = null; // Login klienta
	private boolean op = false; // Czy klient jest opem
	private Socket socket = null; // Soket komunikacji z aplikacj¹ klienta
	private BufferedReader input = null; // Czytacz strumienia wejœciowego
	private PrintStream output = null; // Strumien wyjsciowy
	private GadaczServer server = null; // Serwer aplikacji
	private long period = 100; // Czas oczekiwania watku w ms

	// private final String MSG_DELIMITER = ""; // Znak rozdzielaj¹cy polecenie

	public GadaczClientThread(Socket socket, GadaczServer server) {

		this.socket = socket;
		this.server = server;

		try {
			// Tworzenie po³¹czenia wejœciowego i wyjœciowego
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		String received_string = "";
		boolean show_message = true;

		try {

			loop: while (true) {

				received_string = input.readLine();

				int pozycja_rozdz = received_string.indexOf(' ');
				String cmd = received_string.substring(0, pozycja_rozdz).toLowerCase();
				String body = received_string.substring(pozycja_rozdz + 1, received_string.length());

				// Pobieramy numer identyfikacyjny komendy gadacza przeslanej od
				// klienta
				int cmd_id = -1;
				// System.out.println("[" + cmd + "]");
				for (GadaczCommands gc : GadaczCommands.values()) {
					if (cmd.indexOf(gc.getCommandString()) != -1) {
						cmd_id = gc.getId();
						break;
					}
				}

				switch (cmd_id) {

					// Logowanie klienta do gadacza
					// hello [login_string]
					case 1:
						// Sprawdzamy czy login nie jest juz zarezerwowany
						String login = body;
						boolean login_is_free = server.checkLoginFree(login);

						if (login_is_free) {
							setLogin(login);
							server.sendPriv("Pomyslnie zalogowano", GadaczServer.NAME, this.getLogin());
							server.sendMessage("Przyszedl uzytkownik " + this, GadaczServer.NAME);
						} else {
							server.sendPriv("loginbusy", GadaczServer.NAME, this.getLogin());
							period = 0;
							show_message = false;
							break loop;
						}
						break;

					// Wysylanie wiadomosci publicznej
					// msg [message_body_string]
					case 2:
						server.sendMessage(body, this.getLogin());
						break;

					// Wysylanie wiadomosci prywatnej do uzytkownika o danym
					// loginie
					// priv [login_to_whom_string] [priv_message_body_string]
					case 3:
						int pozycja_rozdzielenia = body.indexOf(' ');
						String do_kogo = body.substring(0, pozycja_rozdzielenia);
						String priv_body = body.substring(pozycja_rozdzielenia + 1, body.length());
						server.sendPriv(priv_body, this.getLogin(), do_kogo);
						break;

					// Nadawanie opa uzytkownikowi
					// op [login_string]
					case 4:
						// Jesli uzytkownik nadajacy opa jest opem to mozna
						// dalej
						if (isOp()) {
							String login_to_whom = body;
							server.setOp(login_to_whom, true);
						}
						break;

					// Odbieranie opa uzytkownikowi
					// deop [login_to_whom_string]
					case 5:
						// Jesli uzytkownik zabieraj¹cy opa jest opem to mozna
						// dalej
						if (isOp()) {
							String login_to_whom = body;
							server.setOp(login_to_whom, false);
						}
						break;

					case 6: // kick
						break;
					case 7: // ban
						break;
					default: // komenda nieznana
						break;
				}

				Thread.sleep(period);
			}

		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.logoutClient(this, show_message);
		}
	}

	@Override
	public String toString() {
		return GadaczServer.PREFIX + getLogin();
	}

	public boolean isOp() {
		return op;
	}

	public void setOp(boolean op) {
		this.op = op;
	}

	public BufferedReader getInput() {
		return input;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public PrintStream getOutput() {
		return output;
	}

	public GadaczServer getServer() {
		return server;
	}

	public Socket getSocket() {
		return socket;
	}

}
