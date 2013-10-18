package sk.solidsoft.pripomen;

import java.util.prefs.Preferences;

public class Pripomienka {
	private String 	titulok		  = "";
	private String 	sprava		  = "";
	private boolean menitFarbu    = true;					// Nastavuje sa okamžite obsluhou udalosti zaèiarkovacieho políèka
	private int		casOpakovania = Const.STD_CAS;
	private int		casJednotka   = Const.STD_JEDNOTKA;		// Èasová jednotka (0-hodiny, 1-minúty, 2-sekundy) 
	private boolean jednorazovo   = false;					// Nastavuje sa okamžite obsluhou udalosti zaèiarkavacieho políèka
	private int 	milisekund;								// Vypoèítava sa z predchádzajúcich 2 polí
	private boolean zmenaCasu     = false;					// Nastavuje spracovanie udalostí tlaèidla cmdMinimalizuj, ak sa
															//  zmenil poèet milisekúnd; èíta a resetuje sracovanie udalosti
															//  èasovaèa

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
		milisekund = 1000 * casOpakovania;		// Najprv prekpokladám, že išlo o sekundy
		
		if (casJednotka == Const.CAS_JEDNOT_MINUTY)
			milisekund *= 60;
		
		if (casJednotka == Const.CAS_JEDNOT_HODINY)
			milisekund *= 60 * 60;
	}
	
}
