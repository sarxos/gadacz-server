package com.sarxos.gadacz.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sarxos.gadacz.server.GadaczCommands;
import com.sarxos.gadacz.server.GadaczServer;


/**
 * Simulator of the Gadacz client. Will to login several peoples to the Gadacz
 * server and start conversations.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class GadaczClientSimulator {

	protected static String[] names = new String[] {
		"Roman_G",
		"Leppert",
		"Kaczor",
		"Punk84",
		"Ania_94",
		"SexBomba"
	};

	protected static HashMap<String, List<String>> phrases = new HashMap<String, List<String>>();

	protected static String[] words = new String[] {
		"cze all", "Jak masz na imie?", "no tak", "a widziales go??",
		"to nie on mowie ci", "jakby co to bede w szkole", "nie",
		"ja dziekuje!!! ;P", "ty, a on zapytal mnie jak", "tak", "nie no",
		"to moja metoda :D", "ja nie mam metod", "jak masz na imie",
		"czesc Jacob, poklikamy?", "na priv", "wyslij priva", "gdzie jest admin?",
		"admin!", "nie znam takiego slowa ;P", "za trudne jest dla mnie",
		"no ja lubie adidasy", "a ja nike", "no i nie wiem", "a jak :)",
		"kup mi piwo", "albo wino", "CZesc Karol, jestes ???", "jol ludzie :)))))",
		"nie umiem pisac dobrze", "ja tez nie umiem :((", "Nie mam Ci nic do powiedzenia",
		"Spadaj!!!!!!!!!", "A ja was lubie :*", "Czesc", "Kup mi tez :o)"
	};

	/**
	 * People pretender (fake user).
	 * 
	 * @author Bartosz Firyn (SarXos)
	 */
	public static class GadaczPretender extends Thread {

		private Socket socket = null;
		private BufferedReader input = null;
		private BufferedWriter output = null;
		private boolean running = true;
		private String name = null;

		public GadaczPretender(String host, int port, String name) throws IOException {
			socket = new Socket(host, port);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.name = name;
		}

		@Override
		public void run() {

			super.run();

			try {

				output.write(GadaczCommands.HELLO + " " + name + "\n");

				while (running) {
					List<String> words = phrases.get(name);
					String word = words.get((int) (Math.random() * words.size()));
					output.write(GadaczCommands.MSG + " " + word + "\n");
					output.flush();
					Thread.sleep((long) (10000 + Math.random() * 20000));
				}
			} catch (IOException e) {
				running = false;
				System.out.println("Server disconnected");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public BufferedReader getInput() {
			return input;
		}

		public BufferedWriter getOutput() {
			return output;
		}

		public Socket getSocket() {
			return socket;
		}
	}

	private List<GadaczPretender> pretenders = new ArrayList<GadaczPretender>();

	public GadaczClientSimulator() {
		try {
			for (int i = 0; i < names.length; i++) {
				GadaczPretender pretender = new GadaczPretender(
						"localhost",
						GadaczServer.SERVER_PORT,
						names[i]
				);
				pretenders.add(pretender);
				pretender.start();
			}
		} catch (IOException e) {
			System.err.println("Serwer niedostepny");
		}
	}

	public static void main(String[] args) {

		List<String> giertychowe = new ArrayList<String>();
		giertychowe.add("Zrobimy w koncu porzadek w szkole!");
		giertychowe.add("Wszyscy uczniowie maja chodzic w mundurkach!");
		giertychowe.add("LPR to super partia");
		giertychowe.add("Wprowadzimy zakaz propagowania w szkolach");
		giertychowe.add("A ci komunisci z ZNP to precz!");
		giertychowe.add("Damy podwyzki nauczycielom");
		giertychowe.add("Pani minister finansow niech sie nie wtraca");
		giertychowe.add("Koalicja musi przetrwac");
		giertychowe.add("Albo koniec z koalicja z PiS");
		giertychowe.add("Zdeprawowani uczniowie chodza do szkol");
		giertychowe.add("Zdeprawowani przez homoseksualistow");
		giertychowe.add("Zrobimy w szkolach porzadek");
		giertychowe.add("Smok wawelski jest dowodem przeciw ewolucji!");
		giertychowe.add("Wszechswiat ma 4000 lat");
		giertychowe.add("Mlodziez Wszechpolska to elita mlodziezy");
		giertychowe.add("Nie wiem skad w LPR faszystowskie poglady");
		giertychowe.add("Nie");
		giertychowe.add("Tak");
		giertychowe.add("Odzyskamy Radio Maryja!");
		giertychowe.add("Skad pani ma takie informacje");
		giertychowe.add("Ja nic o tym nie wiem...");

		phrases.put("Roman_G", giertychowe);

		List<String> lepperowe = new ArrayList<String>();
		lepperowe.add("Wszyscy rolnicy dostana swoje wyplaty!");
		lepperowe.add("Nigdy rolnicy nie mieli tak dobrze jak dzisiaj");
		lepperowe.add("Damy rolnikom szanse na rozwoj");
		lepperowe.add("A pan Giertych niech zostanie przy edukacji");
		lepperowe.add("Ta koalicja jest martwa!");
		lepperowe.add("A posel Lyzwinski na pewno jest niewinny");
		lepperowe.add("Molestowanie w Samoobronie? Nie slyszalem!");
		lepperowe.add("Pani minister finansow sie nie zna");
		lepperowe.add("Jesli rzad nie da funduszy koniec z koalicja");
		lepperowe.add("To wszystko wina liberalow z PO");
		lepperowe.add("No ja mam doktorat w Rosji");
		lepperowe.add("Tak");
		lepperowe.add("A ja jestem niewinny!");
		lepperowe.add("Nie wiem");
		lepperowe.add("Nie wiem");

		phrases.put("Leppert", lepperowe);

		List<String> kaczor = new ArrayList<String>();
		kaczor.add("Nie oddamy wladzy liberalom!");
		kaczor.add("Trybunal Konstytucyjny sie myli!");
		kaczor.add("Lustracje do konstytucji!");
		kaczor.add("Spieprzaj dziadu!!!");
		kaczor.add("Afery, afery nic tylko afery!");
		kaczor.add("Spieprzaj dziadu? To nie ja...");
		kaczor.add("Tak");
		kaczor.add("Nie");
		kaczor.add("Nie wiem");
		kaczor.add("PiS wygra wybory nawet za 100 lat");
		kaczor.add("Riki tiki polityki");
		kaczor.add("Mamy tragiczna koalicje");
		kaczor.add("Mamy populistow w rzadzie");
		kaczor.add("Kocham rzadzic!!!");
		kaczor.add("Kocham rzadzic!!!");

		phrases.put("Kaczor", kaczor);

		List<String> punk = new ArrayList<String>();
		punk.add("Wrzuc NOP-a do klopa");
		punk.add("Precz z faszystami");
		punk.add("Precz z Girtychem");
		punk.add("Precz z MW");
		punk.add("Rasta rulez!");
		punk.add("Faszysci do piachu");
		punk.add("Pidzama Porno");
		punk.add("Wlochaty");
		punk.add("Moj ulubiony zespol - Farben Lahre");
		punk.add("Moj ulubiony zespol - Farben Lahre");

		phrases.put("Punk84", punk);

		List<String> ania94 = new ArrayList<String>();
		ania94.add("Lobie cie Karol");
		ania94.add("A mash moze nowe Bravo");
		ania94.add("Bo ja bardzo luibiem Bravo");
		ania94.add("I badzo lubie Girl czytac");
		ania94.add("No a moj chlopak ma nowe adidasy");
		ania94.add("Wogule takie sportowe rzeczy nosi");
		ania94.add("No nie chodzi do szkoly - rzucil");
		ania94.add("No moj chlopak nie potzeboje wykztalcenia");
		ania94.add("Taki on jezt mondry");
		ania94.add("No ja tez slucham Chip-Chop");
		ania94.add("I lubiem tesh Ar-N-B");
		ania94.add("Nie bo moja mama mi nie kaze :(");
		ania94.add("Nie bo moja mama mi nie kaze :(");

		phrases.put("Ania_94", ania94);

		List<String> sex_bomba = new ArrayList<String>();
		sex_bomba.add("Kto sie ze mna umowi na randke???");
		sex_bomba.add("Poklikamy razem?");
		sex_bomba.add("Kto ze mna poklika?");
		sex_bomba.add("Duza dziewczynka szuka duzego pana!");
		sex_bomba.add("Jak macie na imie?");
		sex_bomba.add("Ja jestem Malinka");
		sex_bomba.add("A ty???");
		sex_bomba.add("Nie wiem czy to ma jakies znaczenie...");
		sex_bomba.add("Lubie teatr i kino");
		sex_bomba.add("No troche tez czytam");
		sex_bomba.add("Nie wiem");
		sex_bomba.add("Nie rozumiem o co chodzi...");
		sex_bomba.add("Taki myk!");

		phrases.put("SexBomba", sex_bomba);

		new GadaczClientSimulator();
	}
}
