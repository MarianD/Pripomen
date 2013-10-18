package sk.solidsoft.pripomen;

import java.awt.Image;
import java.awt.TrayIcon;

/*
 * Trieda premenných, spoločných pre viacero tried
 */
final class Spolocne {
	
	/**
	 * Potlačenie štandardného konštruktora, aby sa nedali robiť exempláre triedy
	 */
	private Spolocne() {
		// Tento konštruktor sa nikdy nezavolá
	}

	static 		 Image 				  ikonaDlg;
    static 		 TrayIcon       	  trayIcon;
}