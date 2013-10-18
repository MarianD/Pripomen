package sk.solidsoft.pripomen;

import java.util.prefs.Preferences;

public class Pripomienka {
	private String 	titulok		  = "";
	private String 	sprava		  = "";
	private boolean menitFarbu    = true;					// Nastavuje sa okam�ite obsluhou udalosti za�iarkovacieho pol��ka
	private int		casOpakovania = Const.STD_CAS;
	private int		casJednotka   = Const.STD_JEDNOTKA;		// �asov� jednotka (0-hodiny, 1-min�ty, 2-sekundy) 
	private boolean jednorazovo   = false;					// Nastavuje sa okam�ite obsluhou udalosti za�iarkavacieho pol��ka
	private int 	milisekund;								// Vypo��tava sa z predch�dzaj�cich 2 pol�
	private boolean zmenaCasu     = false;					// Nastavuje spracovanie udalost� tla�idla cmdMinimalizuj, ak sa
															//  zmenil po�et milisek�nd; ��ta a resetuje sracovanie udalosti
															//  �asova�a

	String getTitulok() {
		return titulok;
	}

	String getSprava() {
		return sprava;
	}

	boolean isMenitFarbu() {
		return menitFarbu;
	}

	void setMenitFarbu(boolean menitFarbu) {
		this.menitFarbu = menitFarbu;
	}

	int getCasOpakovania() {
		return casOpakovania;
	}

	int getCasJednotka() {
		return casJednotka;
	}

	boolean isJednorazovo() {
		return jednorazovo;
	}

	void setJednorazovo(boolean jednorazovo) {
		this.jednorazovo = jednorazovo;
	}

	int getMilisekund() {
		return milisekund;
	}

	boolean isZmenaCasu() {
		return zmenaCasu;
	}

	void setZmenaCasu(boolean zmenaCasu) {
		this.zmenaCasu = zmenaCasu;
	}

	void nacitajZadaneUdaje() {
		titulok		  = Const.txtTitulok    .getText();
		sprava		  = Const.txtSprava     .getText();
		casOpakovania = (Integer) Const     .sprCasOpak.getModel().getValue();
		casJednotka   = Const.cboCasJednotka.getSelectedIndex();
		
		vypocitajMilisekundy();
	}

	
	void ulozMa() {
		nacitajZadaneUdaje();
		
		Preferences prefs = Preferences.userNodeForPackage(PripomenUI.class);
		prefs.put		(Const.TITULOK_OKNA,     titulok);
		prefs.put		(Const.TEXT_PRIPOMIENKY, sprava);
		prefs.putBoolean(Const.MENIT_FARBU,      menitFarbu);
		prefs.putInt	(Const.CAS_OPAKOVANIA, 	 casOpakovania);
		prefs.putInt	(Const.CAS_JEDNOTKA,     casJednotka);
		prefs.putBoolean(Const.JEDNORAZOVO, 	 jednorazovo);
	}
	
	void obnovMaZdisku() {
		Preferences prefs = Preferences.userNodeForPackage(PripomenUI.class);
		titulok 		  = prefs.get		(Const.TITULOK_OKNA,     titulok);
		sprava  		  = prefs.get		(Const.TEXT_PRIPOMIENKY, sprava);
		menitFarbu 		  = prefs.getBoolean(Const.MENIT_FARBU,      Const.STD_MENIT_FARBU);
		casOpakovania 	  = prefs.getInt	(Const.CAS_OPAKOVANIA, 	 Const.STD_CAS);
		casJednotka 	  = prefs.getInt	(Const.CAS_JEDNOTKA,     Const.STD_JEDNOTKA);
		jednorazovo 	  = prefs.getBoolean(Const.JEDNORAZOVO, 	 Const.STD_JEDNORAZOVO);
		
		vypocitajMilisekundy();
	}
	
	private void vypocitajMilisekundy() {
		milisekund = 1000 * casOpakovania;		// Najprv prekpoklad�m, �e i�lo o sekundy
		
		if (casJednotka == Const.CAS_JEDNOT_MINUTY)
			milisekund *= 60;
		
		if (casJednotka == Const.CAS_JEDNOT_HODINY)
			milisekund *= 60 * 60;
	}
	
}
