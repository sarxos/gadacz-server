package com.sarxos.gadacz.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.sarxos.gadacz.client.GadaczClientThread;


public class GadaczServer extends ServerSocket {

	public static final int SERVER_PORT = 1007;
	public static final String PROMPT = ":";
	public static final String NAME = "Gadacz";
	public static final String PREFIX = "~";

	private List<GadaczClientThread> clients = new LinkedList<GadaczClientThread>();
	private boolean isRunning;
	private PrintStream logger_out;

	public GadaczServer() throws IOException {
		super(SERVER_PORT);
		logger_out = System.out;
	}

	public void runServer() {

		isRunning = true;

		logMessage("Server start running at port " + SERVER_PORT + " on " + (new Date()));

		try {
			while (isRunning) {
				Socket gniazdo_klienta = this.accept();
				GadaczClientThread watek_klienta = new GadaczClientThread(gniazdo_klienta, this);
				clients.add(watek_klienta);
				watek_klienta.setLogin("temp_" + Integer.toString(watek_klienta.hashCode()));
				watek_klienta.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			stopServer();
		}
	}

	public void stopServer() {

		for (GadaczClientThread gk : clients) {
			logoutClient(gk);
		}

		logMessage("Server stop running at port " + SERVER_PORT + " on " + (new Date()));

		isRunning = false;
	}

	public synchronized boolean checkLoginFree(String login) {
		if (login == null) {
			throw new IllegalArgumentException("Login cannot be null");
		}
		if (login.length() > 0) {
			for (GadaczClientThread k : clients) {
				String user_login = k.getLogin();
				if (user_login != null) {
					if (login.toLowerCase().equalsIgnoreCase(user_login.toLowerCase())) {
						return false;
					}
				}
			}
			return true;
		} else {
			System.err.println("Login have to be at last 1 char");
			return false;
		}
	}

	public synchronized void sendMessage(String msg_body, Object from) {
		logMessage(GadaczCommands.MSG + " from [" + from + "] to [all] [" + msg_body + "]");
		for (GadaczClientThread gk : clients) {
			String msg = PREFIX + from + PROMPT + " " + msg_body;
			// ~user: Wiadomosc
			gk.getOutput().println(msg);
		}
	}

	public synchronized void sendPriv(String msg_body, String from, String to) {

		boolean wyslano = false;

		for (GadaczClientThread gk : clients) {
			if (gk.getLogin().equalsIgnoreCase(to)) {
				wyslano = true;
				String msg = PREFIX + from + " (" +
					GadaczCommands.PRIV.getCommandString() +
					")" + PROMPT + " " + msg_body;
				// ~user (priv): Wiadomosc
				gk.getOutput().println(msg);
				break;
			}
		}

		if (wyslano) {
			logMessage(GadaczCommands.PRIV + " from [" + from + "] to [" + to +
					"] [" + msg_body + "]");
		} else {
			logMessage("ERROR! Nie udalo sie wyslac! " + GadaczCommands.PRIV +
					" from [" + from + "] to [" + to + "] [" +
					msg_body + "]");
		}
	}

	public synchronized void setOp(String login, boolean is_op) {
		if (is_op) {
			logMessage(GadaczCommands.OP + " " + login);
		} else {
			logMessage(GadaczCommands.DEOP + " " + login);
		}
		for (GadaczClientThread gk : clients) {
			if (gk.getLogin() == login) {
				gk.setOp(is_op);
			}
		}
	}

	public synchronized void logoutClient(GadaczClientThread klient) {
		logoutClient(klient, true);
	}

	public synchronized void logoutClient(GadaczClientThread klient, boolean showMessage) {

		clients.remove(klient);
		klient.getOutput().println(NAME + PROMPT + " Wylogowano pomyslnie");

		try {

			if (!klient.getSocket().isClosed()) {
				// Najpierw koniecznie pozamykac komunikacje a dopiero potem
				// strumienie
				klient.getSocket().shutdownInput();
				klient.getSocket().shutdownOutput();
				klient.getInput().close();
				klient.getOutput().close();
				// Na koncu dopiero zamknac socketa
				klient.getSocket().close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (showMessage) {
			sendMessage(
					"Odszedl uzytkownik " + klient + ", pozostalo osob " + clients.size(),
					this.toString());
		}

		klient = null;
		System.gc();
	}

	public void logMessage(String message) {
		// logger_out.println(NAME + PROMPT + " " + message);
		logger_out.println(message);
	}

	@Override
	public String toString() {
		return PREFIX + NAME;
	}

	public List<GadaczClientThread> getClients() {
		return clients;
	}
}