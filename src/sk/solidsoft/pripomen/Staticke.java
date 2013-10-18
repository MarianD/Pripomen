package sk.solidsoft.pripomen;

import java.awt.Container;
import java.awt.Toolkit;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

/**
 * Trieda pomocných statických funkcií
 */
final class Staticke {
	private static final String TRIEDA_OKNA = "sk.solidsoft.pripomen.PripomenUI";
	private static final String EOL         = Const.EOL;
	
	/**
	 * Potlaèenie štandardného konštruktora, aby sa nedali robi exempláre triedy
	 */
	private Staticke() {
		// Tento konštruktor sa nikdy nezavolá
	}

	/**
	 * Vráti exemplár triedy PripomenUI (odvodenej od JFrame) na základe
	 * zadaného ovládacieho prvku.
	 * Urèenie: Pre obsluhy udalostí ovládacích prvkov
	 * 
	 * @param  zdroj Ovládací prvok (zadávame ten, ktorý vyslal správu
	 * @return Exeplár triedy PripomenUI, na ktorom je umiestnený zdroj
	 */
	static final PripomenUI getHlavneOkno(Object zdroj) {
		Container cont = (Container) zdroj;

		// H¾adáme vyšší a vyšší rodièovský kontainer, až kým nenatrafíme na typ TRIEDA_OKNA
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
	 * Skráti pridlhý reazec na zadanú dåžku tak, že vezme z neho
	 * zaèiatok, koniec (posledné slovo) a medzi ne dá bodky.
	 *
	 * @param  retazec - reazec, ktorý môže by pridlhý
	 * @param  naDlzku - poèet znakov, na ktorý sa má skráti
	 * @return Upravený reazec, ak bol pridlhý, ináè originálny 
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
	 * Vydá zadaný poèet zvukových signálom so zadaným intervalom medzi nimi
	 * @param pocet    Poèet zvukových signálov
	 * @param interval Poèet milisekúnd medzi dvoma signálmi
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
	 * Zobrazí dialógové okno s otázkou, èi sa má program naozaj skonèi.
	 * V dialógovom okne je tiež (prípadne skrátený) text pripomienky,
	 * kvôli rozlíšeniu pri súèasnom behu viacerých exemplárov tohto programu.
	 * Ak používate¾ potvrdí skonèenie, program sa skonèí, ináè len vráti
	 * hodnotu, zodpovedajúcu tlaèidlu "Nie"
	 * 
	 * @return Hodnota zodpovedajúca tlaèidlu "Nie" (JOptionPane.NO_OPTION)
	 */
	static int skonciOkamzite(boolean okamzite) {
		if (okamzite)
			koniec();
		
		String textPripomienky = Const.txtSprava.getText();
		textPripomienky        = Staticke.pripadneSkrat(textPripomienky, Const.MAX_DLZKA_SPR);
		
		String titulok = Lokalizacia.skonciProgram;
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
