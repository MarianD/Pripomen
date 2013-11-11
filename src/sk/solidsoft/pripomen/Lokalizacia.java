/**
 * 
 */
package sk.solidsoft.pripomen;

import java.util.Locale;
import java.util.ResourceBundle;

import sk.solidsoft.internationalization.MyControl;

/**
 * @author Mari�n D�ne�
 *
 */
final class Lokalizacia {
	private static  Locale	lokalita;
	static  ResourceBundle 	retazce;
	
	static  String	  menovkaTitulku;		// Text: Titulok okna pripomienky:
	static  String	  menovkaTextu;			// Text: Text v okne pripomienky:
	static  String	  menoProgramu;
	static  String	  titulokPripomienky;	// Predvolen� titulok okna pripomienky
	static  String	  textPripomienky;		// Predvolen� text pripomienky
	static  String	  textPotvrdenia;
	
	static  String	  hodin;			    // "hod�n";
	static  String	  minut;			    // "min�t";
	static  String	  sekund;				// "sek�nd";
	static  String	  opakovane;			// Text: Pripom�na� ka�d�ch (menovka)
	static  String	  jednorazovo;			// Text: Pripomen�� o (menovka)
	static  String	  spustit;				// Text na tla�idle a akcia k nemu priraden�
	static  String	  ukazatOkno;     		// Text: Zobrazi� okno
	static  String	  minimalizovat;	    // Text: Pokra�ova�
	static  String	  skoncit;			    // Text: Skon�i�
	
	static  String	  menitFarbu;			// Text: Meni� farbu textu v pripomienkach
	static  String	  pripomenutLenRaz;		// Text: Pripomen�� len raz

	static  String	  verzia;         		// Text: verzia nn  (dd. mm. rrrr)
	static  String 	  licence; 	   			// Text: "            Program m��ete pou��va� aj ��ri� zadarmo.";
	static  String 	  translator; 	   		// Text: "            Preklad do sloven�iny: Mari�n D�ne�.";
	
	static  String    hodina;				// Text: hodina
	static  String    hodiny;				// Text: hodiny
	static  String    hodinu;				// Text: hodinu

	static  String    minuta;				// Text: min�ta
	static  String    minuty;				// Text: min�ty
	static  String    minutu;				// Text: min�tu
	
	static  String    sekunda;				// Text: sekunda
	static  String    sekundy;				// Text: sekundy
	static  String    sekundu;				// Text: sekundu
	
	static  String    jednorazovePrip;		// Text: JEDNORAZOV� pripomenutie bude o %d %s.
	static  String    ziadneDalsiePrip;		// Text: �al�ie pripomenutie u� nebude a program sa skon��.
	static  String    dalsiePripomenutie;	// Text: �al�ie pripomenutie bude o %d %s.
	static  String    zmenaCasuNastav;		// Text: Zmenili ste nastavenie �asu pripom�nania.

	static  String    nastavenyCas;			// Text: Nastaven� �as:
	static  String    intervalOpak;			// Text: Interval opakovania:

	static  String    skonci�Program;		// Text: Skon�i� program
	static  String    naozajSoncit;			// Text: Naozaj chcete skon�i� pripom�nanie
	static  String    ntePripomenutie;  	// Text: (Toto je %d. pripomenutie)
	static  String    poslPripomenutie;		// Text: (Toto je %d. a posledn� pripomenutie)";
	
	static  String	  toolTipUlozit;        // Text: Ulo�� texty a ostatn� nastavenia pripomienky ako predvolen�

	static  String    infoOPocitaci;		// Text: Inform�cie o Va�om po��ta�i;
	static  String    infoVerzia;			// Text: Verzia
	static  String    InfoVerziaSpecif;		// Text: Verzia �pecifik�cie
	static  String    InfoPredvKodovanie;	// Text: Predvolen� k�dovanie s�borov:
	static  String    InfoOperacnySystem;	// Text: Opera�n� syst�m:
	static  String    InfoVerziaArchitekt;	// Text: %s verzia %s (architekt�ra %s)
	static  String    InfoPredvolJazyKraj;	// Text: Predvolen� jazyk a krajina:
	static  String    InfoNastavJazykKraj;	// Text: Nastaven� jazyk a krajina (v tomto programe):
	static  String    InfoPrihlasPouziv;	// Text: Prihl�sen� pou��vate�:
	
	
	static int getLocNumber() {
		int i;
		for (i = 0; i < Const.POCET_LOKALIT - 1; ++i)
			if (lokalita.equals(Const.LOKALITA[i])) {
				break;
			}
		return i;	// Ak nen�jde, vr�ti posledn�, �o je en-EN
	}
	
	static Locale getLocale() {
		return lokalita;
	}

	static void setLocale(Locale loc) {
		lokalita = loc;
		int cisloLokality = getLocNumber();						// Pre istotu - ak nie je v mojom zozname, tak ju zmen�m
		lokalita = Const.LOKALITA[cisloLokality];				//  na posledn�, �o je en-US
		Locale.setDefault(lokalita);							// Preo�e ResourceBundle.getBundle() vracia �tandardn� 
																//  lokalitu, ak ju n�jde a pritomnen�jde zadan�
		nastavRetazce();
	}
	
	/**
	 * Potla�enie �tandardn�ho kon�truktora, aby sa nedali robi� exempl�re triedy
	 */
	private Lokalizacia() {
		// Tento kon�truktor sa nikdy nezavol�
	}

	private static void nastavRetazce() {
		retazce = ResourceBundle.getBundle(Const.BUNDLE_NAME, lokalita, new MyControl());
		
		menovkaTitulku		= getString("LabelOfTitle");			// Text: Titulok okna pripomienky:
		menovkaTextu		= getString("LabelOfText");				// Text: Text v okne pripomienky:
		menoProgramu   		= getString("ApplicationName");			// Text: Pripomienkova�
		titulokPripomienky  = Const.pripomienka.getTitulok();		// Z�skanie titulku, ak bol ulo�en�
		textPripomienky     = Const.pripomienka.getSprava();		// Z�skanie textu spr�vy, ak bol ulo�en�
		textPotvrdenia   	= getString("TextOfConfirmation");
		hodin		   		= getString("Hours");					// "hod�n";
		minut		   		= getString("Minutes");					// "min�t";
		sekund		   		= getString("Seconds");					// "sek�nd";
		opakovane  	  		= getString("RemindEvery");				// Text: Pripom�na� ka�d�ch (menovka)
		jednorazovo	   		= getString("RemindAfter");				// Text: Pripomen�� o (menovka)
		spustit        		= getString("Launch");					// Text na tla�idle a akcia k nemu priraden�
		ukazatOkno    		= getString("ShowWindow");     			// Text: Zobrazi� okno
		minimalizovat  		= getString("Continue");				// Text: Pokra�ova�
		skoncit        		= getString("Exit");					// Text: Skon�i�
		menitFarbu        	= getString("CycleColor");				// Text: Meni� farbu textu v pripomienkach
		pripomenutLenRaz	= getString("RemindOnlyOnce");			// Text: Pripomen�� len raz
		verzia			    = getString("Version");					// Text: Verzia
		verzia			   += Const.VERZIA_END;
		licence			    = getString("License");					// Text: Program m��ete pou��va� a ��ri� zadarmo.
		licence 		    = Const.LICENCE_BEGIN + licence;
		translator		    = getString("Translator");				// Text: Preklad do sloven�iny: Mari�n D�ne�
		translator 		    = Const.TRANSLATOR_BEGIN + translator;
		
		hodina			    = getString("Hour1");					// Text: hodina
		hodiny			    = getString("Hours24");					// Text: hodiny
		hodinu			    = getString("Hour1L");					// Text: hodinu

		minuta			    = getString("Minute1");					// Text: min�ta
		minuty			    = getString("Minutes24");				// Text: min�ty
		minutu			    = getString("Minute1L");				// Text: min�tu
		
		sekunda			    = getString("Second1");					// Text: sekunda
		sekundy			    = getString("Seconds24");				// Text: sekundy
		sekundu			    = getString("Second1L");				// Text: sekundu

		jednorazovePrip		= getString("OneTimeReminder");			// Text: JEDNORAZOV� pripomenutie bude o %d %s.
		ziadneDalsiePrip	= getString("NoNextReminder");			// Text: �al�ie pripomenutie u� nebude a program sa skon��.
		dalsiePripomenutie	= getString("NextReminder");			// Text: �al�ie pripomenutie bude o %d %s..
		zmenaCasuNastav		= getString("YouChangedTime");			// Text: Zmenili ste nastavenie �asu pripom�nania.;

		nastavenyCas		= getString("TimeToAlarm") + " ";		// Text: Nastaven� �as:
		intervalOpak		= getString("PeriodOfReminder") + " ";	// Text: Interval opakovania:
		
		skonci�Program		= getString("ExitProgram");				// Text: Skon�i� program
		naozajSoncit		= getString("AreYouSureToExit");		// Text: Naozaj chcete skon�i� pripom�nanie
		ntePripomenutie		= getString("NthReminder");				// Text: (Toto je %d. pripomenutie)
		poslPripomenutie	= getString("LastReminder");			// Text: (Toto je %d. a posledn� pripomenutie)";

		toolTipUlozit		= getString("ToolTipSave");				// Text: Ulo�� texty a ostatn� nastavenia pripomienky ako predvolen�

		infoOPocitaci		= getString("InfoAboutComputer");		// Text: Inform�cie o Va�om po��ta�i;
		infoVerzia        	= getString("InfoVersion");				// Text: Verzia
		InfoVerziaSpecif  	= getString("InfoVerionOfSpecif");		// Text: Verzia �pecifik�cie
		InfoPredvKodovanie  = getString("InfoDefaultCoding");		// Text: Predvolen� k�dovanie s�borov:
		InfoOperacnySystem  = getString("InfoOperatingSyst");		// Text: Opera�n� syst�m:
		InfoVerziaArchitekt = getString("InfoVersionArchit");		// Text: %s verzia %s (architekt�ra %s)
		InfoPredvolJazyKraj = getString("InfoDefLangCountry");		// Text: Predvolen� jazyk a krajina:
		InfoNastavJazykKraj = getString("InfoSelLangCountry");		// Text: Nastaven� jazyk a krajina (v tomto programe):
		InfoPrihlasPouziv   = getString("InfoLoggedUser");			// Text: Prihl�sen� pou��vate�:
		
		// Na��tanie predvolen�ch �dajov z lokaliza�n�ho s�boru, ak neboli ulo�en�
		if (titulokPripomienky.equals(""))
			titulokPripomienky = getString("TitleOfReminder");		// Predvolen� titulok okna pripomienky
		if (textPripomienky.equals(""))
			textPripomienky   	= getString("TextOfReminder");		// Predvolen� text pripomienky
	}

	private static final String getString(String kluc) {
		return retazce.getString(kluc);
	}
}
