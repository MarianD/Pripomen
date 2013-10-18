package sk.solidsoft.pripomen;

import java.awt.Image;
import java.awt.TrayIcon;

/*
 * Trieda premennıch, spoloènıch pre viacero tried
 */
final class Spolocne {
	
	/**
	 * Potlaèenie štandardného konštruktora, aby sa nedali robi exempláre triedy
	 */
	private Spolocne() {
		// Tento konštruktor sa nikdy nezavolá
	}

	static 		 Image 				  ikonaDlg;
    static 		 TrayIcon       	  trayIcon;
}