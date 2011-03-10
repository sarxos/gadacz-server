package com.sarxos.gadacz.server;

public enum GadaczCommands {

	HELLO(	"hello",	1),		// proba zalogowania sie ludka do gadacza
	MSG(	"msg",		2),		// wiadomosc publiczna
	PRIV(	"priv",	3),		// wiadomosc prywatna
	OP(		"op", 		4),		// nadawanie statusu opa
	DEOP(	"deop", 	5),		// usuanie statusu opa
	KICK(	"kick", 	6),		// wylogowanie ludka z gadazca	 
	BAN(	"ban", 		7);		// zablokowanie dostepu do gadacza dla ludka
	
	private String commandString;
	private int id;
	
	private GadaczCommands(String cmd, int id) {
		this.commandString = cmd;
		this.id = id;
	}

	public String getCommandString() {
		return commandString;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return getCommandString();
	}
}
