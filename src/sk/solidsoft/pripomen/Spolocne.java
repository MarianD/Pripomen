package sk.solidsoft.pripomen;

import java.awt.Image;
import java.awt.TrayIcon;

/*
 * Trieda premenn�ch, spolo�n�ch pre viacero tried
 */
final class Spolocne {
	
	/**
	 * Potla�enie �tandardn�ho kon�truktora, aby sa nedali robi� exempl�re triedy
	 */
	private Spolocne() {
		// Tento kon�truktor sa nikdy nezavol�
	}

	static 		 Image 				  ikonaDlg;
    static 		 TrayIcon       	  trayIcon;
}