package sk.solidsoft.pripomen;

import java.awt.Container;
import java.awt.Toolkit;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

/**
 * Trieda pomocn�ch statick�ch funkci�
 */
final class Staticke {
	private static final String TRIEDA_OKNA = "sk.solidsoft.pripomen.PripomenUI";
	private static final String EOL         = Const.EOL;
	
	/**
	 * Potla�enie �tandardn�ho kon�truktora, aby sa nedali robi� exempl�re triedy
	 */
	private Staticke() {
		// Tento kon�truktor sa nikdy nezavol�
	}

	/**
	 * Vr�ti exempl�r triedy PripomenUI (odvodenej od JFrame) na z�klade
	 * zadan�ho ovl�dacieho prvku.
	 * Ur�enie: Pre obsluhy udalost� ovl�dac�ch prvkov
	 * 
	 * @param  zdroj Ovl�dac� prvok (zad�vame ten, ktor� vyslal spr�vu
	 * @return Exepl�r triedy PripomenUI, na ktorom je umiestnen� zdroj
	 */
	static final PripomenUI getHlavneOkno(Object zdroj) {
		Container cont = (Container) zdroj;

		// H�ad�me vy��� a vy��� rodi�ovsk� kontainer, a� k�m nenatraf�me na typ TRIEDA_OKNA
		do {
			cont = cont.getParent();
		} while (!cont.getClass().getName().equals(TRIEDA_OKNA));

		return (PripomenUI) cont;
	}

	static String sklonovane(int cas, String jednotka, boolean preSysTray) {
		String vysledok = "";
		
		if (jednotka == Lokalizacia.sekund) {
			if (cas == 1)
				vysledok = preSysTray ? Lokalizacia.sekunda : Lokalizacia.sekundu;
			else if (cas >= 2 && cas <= 4)
				vysledok = Lokalizacia.sekundy;
			else
				vysledok = Lokalizacia.sekund;
		}

		else if (jednotka == Lokalizacia.minut) {
			if (cas == 1)
				vysledok = preSysTray ? Lokalizacia.minuta : Lokalizacia.minutu;
			else if (cas >= 2 && cas <= 4)
				vysledok = Lokalizacia.minuty;
			else
				vysledok = Lokalizacia.minut;
		}
		
		else if (jednotka == Lokalizacia.hodin) {
			if (cas == 1)
				vysledok = preSysTray ? Lokalizacia.hodina : Lokalizacia.hodinu;
			else if (cas >= 2 && cas <= 4)
				vysledok = Lokalizacia.hodiny;
			else
				vysledok = Lokalizacia.hodin;
		}
		
		return vysledok;
	}

	/**
	 * Skr�ti pridlh� re�azec na zadan� d�ku tak, �e vezme z neho
	 * za�iatok, koniec (posledn� slovo) a medzi ne d� bodky.
	 *
	 * @param  retazec - re�azec, ktor� m��e by� pridlh�
	 * @param  naDlzku - po�et znakov, na ktor� sa m� skr�ti�
	 * @return Upraven� re�azec, ak bol pridlh�, in�� origin�lny 
	 */
	static String pripadneSkrat(String retazec, int naDlzku) {
		retazec = retazec.trim();
		
		int origDlzka = retazec.length();
		if (origDlzka > naDlzku) {
			int pozicia = retazec.lastIndexOf(" ");
			
			String cast3  = retazec.substring(pozicia + 1);
			int    dlzka3 = cast3.length();
			
			String cast2  = " ..... ";
			int    dlzka2 = cast2.length();
			
			int    dlzka1 = naDlzku - dlzka2 - dlzka3;
			String cast1  = retazec.substring(0, dlzka1 - 1);
			
			retazec  = cast1 + cast2 + cast3;
		}
		return retazec;
	}

	/**
	 * Vyd� zadan� po�et zvukov�ch sign�lom so zadan�m intervalom medzi nimi
	 * @param pocet    Po�et zvukov�ch sign�lov
	 * @param interval Po�et milisek�nd medzi dvoma sign�lmi
	 */
	static void zvukovySignal(int pocet, int interval) {
		for (int i = 0; i < pocet; ++i) {
			Toolkit.getDefaultToolkit().beep();
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Zobraz� dial�gov� okno s ot�zkou, �i sa m� program naozaj skon�i�.
	 * V dial�govom okne je tie� (pr�padne skr�ten�) text pripomienky,
	 * kv�li rozl�eniu pri s��asnom behu viacer�ch exempl�rov tohto programu.
	 * Ak pou��vate� potvrd� skon�enie, program sa skon��, in�� len vr�ti
	 * hodnotu, zodpovedaj�cu tla�idlu "Nie"
	 * 
	 * @return Hodnota zodpovedaj�ca tla�idlu "Nie" (JOptionPane.NO_OPTION)
	 */
	static int skonciOkamzite(boolean okamzite) {
		if (okamzite)
			koniec();
		
		String textPripomienky = Const.txtSprava.getText();
		textPripomienky        = Staticke.pripadneSkrat(textPripomienky, Const.MAX_DLZKA_SPR);
		
		String titulok = Lokalizacia.skonci�Program;
		String sprava  = "    " + Lokalizacia.naozajSoncit + EOL + EOL + "\"" + textPripomienky + "\"?" + EOL;
		int    volba   = JOptionPane.showConfirmDialog(null, sprava, titulok, JOptionPane.YES_NO_OPTION);

		if (volba == JOptionPane.YES_OPTION) {
			koniec();
		}
		return volba;
	}

	private static void koniec() {
		Preferences prefs = Preferences.userNodeForPackage(PripomenUI.class);
		int pocetSpusteni = prefs.getInt(Const.POCET_SPUSTENI, 1);
		prefs.putInt(Const.POCET_SPUSTENI, --pocetSpusteni);
		System.exit(0);
	}
	
	static void nastavToolTipIkony(Integer casOpakovania) {
		boolean jednorazovo = Const.chkJednorazovo.isSelected();
		String  casJednotka = (String) Const.cboCasJednotka.getSelectedItem();
		String  toolTip     = Lokalizacia.menoProgramu + EOL;
		String  toolTipOpak = jednorazovo ? Lokalizacia.nastavenyCas : Lokalizacia.intervalOpak;
		
		toolTipOpak 	   += String.format(" %d %s.", casOpakovania, Staticke.sklonovane(
									casOpakovania, casJednotka, true));
		
		String toolTipSpr   = Const.txtSprava.getText();
		toolTipSpr          = Staticke.pripadneSkrat(toolTipSpr, Const.MAX_DLZKA_SPR);
		toolTipSpr         += EOL;

		Spolocne.trayIcon.setToolTip(toolTip + toolTipSpr + toolTipOpak);
	}


}
