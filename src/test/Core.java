package test;

import java.io.IOException;

public class Core {

	public static void main(String[] args) {
		CavsBot bot = new CavsBot();
		try {
			bot.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bot.joinChannel("#cavariux");
		bot.start();
	}

}
