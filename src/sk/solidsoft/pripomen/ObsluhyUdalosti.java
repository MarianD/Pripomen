package sk.solidsoft.pripomen;

import java.io.*;
import java.awt.Component;
import java.awt.TrayIcon;
import java.awt.event.*;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * Spracovanie udalosti stlaËenia prÌkazovÈho tlaËidla Spustiù/SkonËiù, PokraËovaù
 * (= Minimalizovaù), Uloûiù a PoËÌtaË v hlavnom okne programu.
 * (PÙvodne m· tlaËidlo Spustiù/SkonËiù funkciu "Spustiù", po prvom pouûitÌ sa menÌ 
 *  na "SkonËiù.")
 * V prÌpade funkcie "Nastaviù" sa spustÌ ËasovaË a vykonaj˙ sa Ôalöie pomocnÈ Ëinnosti.
 */
final class CmdButtonListener implements ActionListener {
	static final 		 String	 	 EOL	     = Const.EOL;
	static final 		 String  	 TAB         = Const.TAB;
	private static final Pripomienka pripomienka = Const.pripomienka;
	private static final Timer  	 casovac     = Const.casovac;

	@Override
	public void actionPerformed(ActionEvent e) {
		Object	   zdroj      = e.getSource();
		PripomenUI pripomenUI = Staticke.getHlavneOkno(zdroj);
		JButton    tlacidlo   = (JButton) zdroj;

		if (tlacidlo == Const.cmdSpustit) {
			if (tlacidlo.getActionCommand() == Const.ACTION_SKONCIT) {
				Staticke.skonciOkamzite(false);
			} else if (tlacidlo.getActionCommand() == Const.ACTION_SPUSTIT) {
				pripomienka.nacitajZadaneUdaje();
				spustiCasovac(pripomenUI);
				Const.cmdSpustit    .setText(Lokalizacia.skoncit);
				Const.cmdSpustit    .setActionCommand(Const.ACTION_SKONCIT);
				
				Const.cmdMinimalizuj.setVisible(true);
				pripomenUI			.getRootPane().setDefaultButton(Const.cmdMinimalizuj);
				pripomenUI.dajIkonuNaPodnos();
			}
		} else if (tlacidlo == Const.cmdMinimalizuj) {
			int oldMilisekund = pripomienka.getMilisekund();
			pripomienka.nacitajZadaneUdaje();
			if (oldMilisekund != pripomienka.getMilisekund()) {
				Const.pripomienka.setZmenaCasu(true);	// Vyuûije a vypne to CasovacListener
				spustiCasovac(pripomenUI);
			}
			pripomenUI.setExtendedState(JFrame.ICONIFIED);
		} else if (tlacidlo == Const.cmdPocitac) {
			String textPocitacInfo = 
						Lokalizacia.infoVerzia + " JRE (Java Runtime Environment):" + EOL + TAB + 
							"%s (%s)" + EOL + EOL +
						Lokalizacia.InfoVerziaSpecif + " JRE (Java Runtime Environment): " + EOL + TAB + 
							"%s (%s, %s)" + EOL + EOL +
						Lokalizacia.infoVerzia + " JVM (Java Virtual Machine): " + EOL + TAB + 
							"%s (%s, %s)" + EOL + EOL +
						Lokalizacia.InfoVerziaSpecif + " JVM (Java Virtual Machine): " + EOL + TAB + 
							"%s (%s, %s)" + EOL + EOL +
						Lokalizacia.InfoPredvKodovanie + EOL + TAB + 
							"%s" + EOL + EOL +
						Lokalizacia.InfoOperacnySystem + EOL + TAB + 
							Lokalizacia.InfoVerziaArchitekt + EOL + EOL +
						Lokalizacia.InfoPredvolJazyKraj + EOL + TAB +
							"%s, %s" + EOL + EOL +
						Lokalizacia.InfoNastavJazykKraj + EOL + TAB +
							"%s, %s" + EOL + EOL +
						Lokalizacia.InfoPrihlasPouziv + EOL + TAB + 
							"%s" + EOL;

			String INFO_O_SYSTEME = String.format(textPocitacInfo,
													System.getProperty("java.version"),
													System.getProperty("java.vendor"),
													System.getProperty("java.specification.version"),
													System.getProperty("java.specification.name"),
													System.getProperty("java.specification.vendor"),
													System.getProperty("java.vm.version"),
													System.getProperty("java.vm.name"),
													System.getProperty("java.vm.vendor"),
													System.getProperty("java.vm.specification.version"),
													System.getProperty("java.vm.specification.name"),
													System.getProperty("java.vm.specification.vendor"),
													new OutputStreamWriter(new ByteArrayOutputStream()).getEncoding(),
													System.getProperty("os.name" ),
													System.getProperty("os.version"),
													System.getProperty("os.arch"),
													PripomenUI.getOrigLokalita().getDisplayLanguage(),
													PripomenUI.getOrigLokalita().getDisplayCountry(),
													Lokalizacia.getLocale()		.getDisplayLanguage(),
													Lokalizacia.getLocale()		.getDisplayCountry(),
													System.getProperty("user.name"));
			JOptionPane.showMessageDialog(null, INFO_O_SYSTEME, Lokalizacia.infoOPocitaci, JOptionPane.INFORMATION_MESSAGE);
		} else if (tlacidlo == Const.cmdUlozit) {
			pripomienka.ulozMa();
			Const.cmdUlozit.setEnabled(false);
		}
	}
	
	private void spustiCasovac(PripomenUI pripomenUI) {
		casovac.setDelay(pripomienka.getMilisekund());
		if (casovac.isRunning())
			casovac.restart();		// Ak sa zadaali zmeny v nastavenÌ ËasovaËa
		else
			casovac.start();
		Staticke.nastavToolTipIkony(pripomienka.getCasOpakovania());
	}
}


/**
 * Spracovanie potvrdenia pripomienkovacieho okna
 * kl·vesom Enter
 */
final class CmdOkListener implements ActionListener {
	
	private static boolean skoncitProgram = false;

	/**
	 * @param skoncitProgram Signaliz·cia, Ëi sa m· program uû skonËiù
	 */
	public static void setSkoncitProgram(boolean skoncitProgram) {
		CmdOkListener.skoncitProgram = skoncitProgram;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Component   zdroj       = (Component) e.getSource();
		DlgPripomen dlgPripomen = (DlgPripomen) zdroj.getParent().getParent().getParent().getParent();
		boolean     jednorazovo = Const.pripomienka.isJednorazovo();

		dlgPripomen.setVisible(false);
		Spolocne.trayIcon.getPopupMenu().setEnabled(true);
		
		if (skoncitProgram && jednorazovo)
			Staticke.skonciOkamzite(true);
	}
}


/**
 * Spracovanie stlaËenia kl·vesov v hlavnom okne -
 * - skonËenie pomocou kl·vesa Esc
 * - spustenie ËasovaËa kl·vesom Enter
 * - minimalizovanie okna (po spustenÌ ËasovaËa) kl·vesom Enter
 * - vynulovanie poËtu spusten˝ch exepl·rov programu kombin·ciou Ctrl+Home
 */
final class KeyPressListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent e) {
		int    klaves      = e.getKeyCode();
		int    modifikator = e.getModifiersEx();
		int    ctrlDown    = InputEvent.CTRL_DOWN_MASK;

		switch (klaves) {
		case KeyEvent.VK_ESCAPE:
			Staticke.skonciOkamzite(false);
			break;
		case KeyEvent.VK_HOME:
			if ((modifikator & ctrlDown) == ctrlDown)
				if (showConfirmDialog(null,	Const.OTAZKA_NULOVANIA,	Const.TITULOK_NULOVANIA, YES_NO_OPTION) == YES_OPTION) {
					Preferences prefs = Preferences.userNodeForPackage(PripomenUI.class);
					prefs.putInt(Const.POCET_SPUSTENI, 1);
				}
			break;
		/* Netreba, lebo som nastavil ako ötandardnÈ tlaËidlo	
		case KeyEvent.VK_ENTER:
			Spolocne.cmdNastavit.doClick();
			break;
		*/	
		}
	}
}



/**
 * Spracovanie udalostÌ zatvorenia a minimalizovania hlavnÈho okna.
 *   Zatvorenie okna      - skonËenie programu, ale aû po overenÌ pouûÌvateæovho z·meru
 *   Minimalizovanie okna - umiestnenie ikony na podnos
 */
final class WindowLstnr extends WindowAdapter {

	@Override
	public void windowClosing(WindowEvent e) {
		PripomenUI pripomenUI = (PripomenUI) e.getSource();

		/*
		 * ZobrazÌ dialÛgovÈ okno na overenie ˙myslu skonËiù program
		 * V prÌpade nes˙hlasu pouûÌvateæa hlavnÈ okno nezavrie,
		 * v prÌpade s˙hlasu cel˝ program skonËÌ.
		 */
		if (Staticke.skonciOkamzite(false) == JOptionPane.NO_OPTION)
			pripomenUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		PripomenUI pripomenUI = (PripomenUI) e.getSource();

		/* 
		 * ZneviditeænÌ okno, nastavÌ ho na norm·lne a vûdy navrchu kvÙli 
		 * neskoröej obnove a umiestni ikonu do systÈmovej oblasti.
		 */
		pripomenUI.dajIkonuNaPodnos();
	}
}


/**
 * Trieda na spracovanie udalostÌ na ikone programu na podnose
 */
final class PodnosListener implements ActionListener  {
	
	@Override
    public void actionPerformed(ActionEvent e) {
    	Object       zdroj     = e.getSource();
    	MenuItemMoje menuItem;

		// Zaklopanie na ikonu = zvolenie 1. poloûky kontextovej ponuky ikony
    	if (zdroj.getClass().getName() == "java.awt.TrayIcon") {		
			TrayIcon icon = (TrayIcon) zdroj;
			menuItem      = (MenuItemMoje)icon.getPopupMenu().getItem(0);
		}
		// Zvolenie poloûky z kontextovej ponuky ikony
    	else																
			menuItem = (MenuItemMoje) zdroj;

		if (menuItem.getActionCommand() == Const.ACTION_UKAZ_OKNO) {
			if (Spolocne.trayIcon.getPopupMenu().isEnabled()) {
				menuItem.pripomenUI.setVisible(true);
				menuItem.pripomenUI.setAlwaysOnTop(false);
			} else
				;
		} else if (menuItem.getActionCommand() == Const.ACTION_SKONCIT)
			Staticke.skonciOkamzite(false);
	}
}


/**
 * Trieda na spracovanie udalosti ËasovaËa - zapÌpanie a zobrazenie
 * okna s pripomenutÌm
 */
final class CasovacListener implements ActionListener {
	static final DlgPripomen dlgPripomen  	   = new DlgPripomen();
	private static int       cisloPripomenutia = -1;
	static int               cisloFarby    	   = 0;
	static boolean			 prveSpustenie 	   = true;

	@Override
	public void actionPerformed(ActionEvent e) {
		String  titulok     = Const.pripomienka.getTitulok();
		String  sprava	    = Const.pripomienka.getSprava();
		boolean jednorazovo = Const.pripomienka.isJednorazovo();
		
		if (Const.pripomienka.isZmenaCasu()) {
			sprava = Lokalizacia.zmenaCasuNastav;
			--cisloPripomenutia;									// Pozdrûanie ËÌslovania
			if (Const.pripomienka.isMenitFarbu()) {
				cisloFarby = --cisloFarby % Const.POCET_FARIEB;		// Pozdrûanie zmeny farby
			}
			DlgPripomen.lblCisloPripom.setVisible(false);
			if (jednorazovo) {
				prveSpustenie = true;								// Aby tento oznam o zmene Ëasu nebol posledn˝m
			}
			Const.pripomienka.setZmenaCasu(false);
		} else {
			DlgPripomen.lblCisloPripom.setVisible(true);
		}
		
		sprava = nastavSpravu(sprava);

		Staticke.zvukovySignal(Const.ZVUK_SIGNALOV, Const.MEDZI_SIGNALMI);
		dlgPripomen.setIconImage(Spolocne.ikonaDlg);
		dlgPripomen.setTitulok(titulok);
		dlgPripomen.setText(sprava);
		pripadneZmenFarbuTextuPripomienky();
		nastavOpakovatelnostCasovaca();
		
		String  ktorePripom = jednorazovo ? 
									String.format(Lokalizacia.poslPripomenutie, ++cisloPripomenutia) : 
									String.format(Lokalizacia.ntePripomenutie,  ++cisloPripomenutia);
        if (!prveSpustenie)										
        	DlgPripomen.lblCisloPripom.setText(ktorePripom);
		
		if (Const.pripomienka.isJednorazovo() && !prveSpustenie)
			DlgPripomen.cmdOK.setText(Lokalizacia.textPotvrdenia + " - " + Lokalizacia.skonciùProgram);
		else
			DlgPripomen.cmdOK.setText(Lokalizacia.textPotvrdenia);
		
		prveSpustenie = false;
		
		Spolocne.trayIcon.getPopupMenu().setEnabled(false);
		dlgPripomen.setVisible(true);
	}

	private String nastavSpravu(String sprava) {
		String  casJednotka = (String) Const.cboCasJednotka.getItemAt(Const.pripomienka.getCasJednotka());
		Integer cas	        = (Integer) Const.pripomienka.getCasOpakovania();
		boolean jednorazovo = Const.pripomienka.isJednorazovo();
		
		sprava += Const.EOL + Const.EOL;
		
		if (jednorazovo)
			if (prveSpustenie)
				sprava += String.format(Lokalizacia.jednorazovePrip, cas, 
				Staticke.sklonovane(cas, casJednotka, false));
			else {
				sprava += Lokalizacia.ziadneDalsiePrip;
			}
		else
			sprava += String.format(Lokalizacia.dalsiePripomenutie, cas, 
						Staticke.sklonovane(cas, casJednotka, false));
		return sprava;
	}

	private void nastavOpakovatelnostCasovaca() {
		boolean opakovat = prveSpustenie || !Const.chkJednorazovo.isSelected();

		Const.casovac.setRepeats(opakovat);
		CmdOkListener.setSkoncitProgram(!opakovat);
	}

	private void pripadneZmenFarbuTextuPripomienky() {
		if (!prveSpustenie && Const.chkMenitFarbu.isSelected()) {
			cisloFarby = ++cisloFarby % Const.POCET_FARIEB;
			DlgPripomen.txtOznam.setForeground(Const.FARBA_PISMA[cisloFarby]);
		}
	}
}


/**
 * Trieda na spracovanie udalosti zmeny zaËiarkavacÌch
 * polÌËok - okamûite premietne zmena a zobrazÌ a umoûnÌ
 * pouûiù ikonu uloûenia
 */
final class CheckBoxListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Object	   zdroj       = e.getSource();
		PripomenUI pripomenUI  = Staticke.getHlavneOkno(zdroj);
		boolean    jednorazovo = Const.chkJednorazovo.isSelected();
		boolean    menitFarbu  = Const.chkMenitFarbu.isSelected();
		String     menovka     = jednorazovo ? Lokalizacia.jednorazovo : Lokalizacia.opakovane;
		
		Const.pripomienka.setJednorazovo(jednorazovo);
		Const.pripomienka.setMenitFarbu(menitFarbu);

		Const.lblCasOpak.setText(menovka);
		pripomenUI		.zmenPriznakyJednorazovosti();
		pripomenUI		.zmenPriznakFarebnosti();
		
		Staticke.nastavToolTipIkony(Const.pripomienka.getCasOpakovania());
		
		Const.cmdUlozit.setVisible(true);		
		Const.cmdUlozit.setEnabled(true);		
	}
}


/**
 * Trieda na spracovanie udalosti zmeny textu - zobrazÌ
 * a umoûnÌ pouûiù ikonu uloûenia
 */
final class TxtChangeListener implements DocumentListener {
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		Const.cmdUlozit.setVisible(true);		
		Const.cmdUlozit.setEnabled(true);		
    }

	@Override
    public void removeUpdate(DocumentEvent e) {
		Const.cmdUlozit.setVisible(true);		
		Const.cmdUlozit.setEnabled(true);		
    }

	@Override
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }
}


/**
 * Trieda na spracovanie udalosti zmeny nastavenia
 * Ëasu - zobrazÌ a umoûnÌ pouûiù ikonu uloûenia
 */
final class SpinnerListener implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent e) {
		Const.cmdUlozit.setVisible(true);		
		Const.cmdUlozit.setEnabled(true);		
	}
}
	
	
/**
 * Trieda na spracovanie udalosti zmeny v˝beru z poæa
 * so zoznamom - zobrazÌ a umoûnÌ pouûiù ikonu uloûenia
 */
final class ComboBoxListener implements ItemListener {
		
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object	   zdroj      = e.getSource();
		PripomenUI pripomenUI = Staticke.getHlavneOkno(zdroj);
		JComboBox  comboBox   = (JComboBox) zdroj;

		if (comboBox == Const.cboJazyk) {
			String item = (String) e.getItem();
			for (int i = 0; i < Const.POCET_LOKALIT; ++i) {
				if (item == Const.LANGUAGE[i]) {
//					//TODO: Pri zmene jazyka sa neprispÙsobuje predvolen˝ titulok a text
					boolean isEnabled = Const.cmdUlozit.isEnabled();	// Uloûenie stavov tlaËidla "Uloûiù", pretoûe
					boolean isVisible = Const.cmdUlozit.isVisible();	//  zmena jazyka ho vûdy aktivuje cez TextChangedListener
					Lokalizacia.setLocale(Const.LOKALITA[i]);
					
					pripomenUI .nastavLoklizovaneTexty();
					Const      .cmdUlozit.setVisible(isVisible);		// Obnova stavov tlaËidla "Uloûiù
					Const      .cmdUlozit.setEnabled(isEnabled);
					break;
				}
			}
		}
		else {
			Const.cmdUlozit.setVisible(true);		
			Const.cmdUlozit.setEnabled(true);		
		}
	}
}	
