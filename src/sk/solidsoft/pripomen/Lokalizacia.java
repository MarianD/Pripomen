/**
 * 
 */
package sk.solidsoft.pripomen;

import java.util.Locale;
import java.util.ResourceBundle;

import sk.solidsoft.internationalization.MyControl;

/**
 * @author Marián Déneš
 *
 */
final class Lokalizacia {
	private static  Locale	lokalita;
	static  ResourceBundle 	retazce;
	
	static  String	  menovkaTitulku;		// Text: Titulok okna pripomienky:
	static  String	  menovkaTextu;			// Text: Text v okne pripomienky:
	static  String	  menoProgramu;
	static  String	  titulokPripomienky;	// Predvolenı titulok okna pripomienky
	static  String	  textPripomienky;		// Predvolenı text pripomienky
	static  String	  textPotvrdenia;
	
	static  String	  hodin;			    // "hodín";
	static  String	  minut;			    // "minút";
	static  String	  sekund;				// "sekúnd";
	static  String	  opakovane;			// Text: Pripomína kadıch (menovka)
	static  String	  jednorazovo;			// Text: Pripomenú o (menovka)
	static  String	  spustit;				// Text na tlaèidle a akcia k nemu priradená
	static  String	  ukazatOkno;     		// Text: Zobrazi okno
	static  String	  minimalizovat;	    // Text: Pokraèova
	static  String	  skoncit;			    // Text: Skonèi
	
	static  String	  menitFarbu;			// Text: Meni farbu textu v pripomienkach
	static  String	  pripomenutLenRaz;		// Text: Pripomenú len raz

	static  String	  verzia;         		// Text: verzia nn  (dd. mm. rrrr)
	static  String 	  licence; 	   			// Text: "            Program môete pouíva aj šíri zadarmo.";
	static  String 	  translator; 	   		// Text: "            Preklad do slovenèiny: Marián Déneš.";
	
	static  String    hodina;				// Text: hodina
	static  String    hodiny;				// Text: hodiny
	static  String    hodinu;				// Text: hodinu

	static  String    minuta;				// Text: minúta
	static  String    minuty;				// Text: minúty
	static  String    minutu;				// Text: minútu
	
	static  String    sekunda;				// Text: sekunda
	static  String    sekundy;				// Text: sekundy
	static  String    sekundu;				// Text: sekundu
	
	static  String    jednorazovePrip;		// Text: JEDNORAZOVÉ pripomenutie bude o %d %s.
	static  String    ziadneDalsiePrip;		// Text: Ïalšie pripomenutie u nebude a program sa skonèí.
	static  String    dalsiePripomenutie;	// Text: Ïalšie pripomenutie bude o %d %s.
	static  String    zmenaCasuNastav;		// Text: Zmenili ste nastavenie èasu pripomínania.

	static  String    nastavenyCas;			// Text: Nastavenı èas:
	static  String    intervalOpak;			// Text: Interval opakovania:

	static  String    skonciProgram;		// Text: Skonèi program
	static  String    naozajSoncit;			// Text: Naozaj chcete skonèi pripomínanie
	static  String    ntePripomenutie;  	// Text: (Toto je %d. pripomenutie)
	static  String    poslPripomenutie;		// Text: (Toto je %d. a posledné pripomenutie)";
	
	static  String	  toolTipUlozit;        // Text: Uloí texty a ostatné nastavenia pripomienky ako predvolené

	static  String    infoOPocitaci;		// Text: Informácie o Vašom poèítaèi;
	static  String    infoVerzia;			// Text: Verzia
	static  String    InfoVerziaSpecif;		// Text: Verzia špecifikácie
	static  String    InfoPredvKodovanie;	// Text: Predvolené kódovanie súborov:
	static  String    InfoOperacnySystem;	// Text: Operaènı systém:
	static  String    InfoVerziaArchitekt;	// Text: %s verzia %s (architektúra %s)
	static  String    InfoPredvolJazyKraj;	// Text: Predvolenı jazyk a krajina:
	static  String    InfoNastavJazykKraj;	// Text: Nastavenı jazyk a krajina (v tomto programe):
	static  String    InfoPrihlasPouziv;	// Text: Prihlásenı pouívate¾:
	
	
	static int getLocNumber() {
		int i;
		for (i = 0; i < Const.POCET_LOKALIT - 1; ++i)
			if (lokalita.equals(Const.LOKALITA[i])) {
				break;
			}
		return i;	// Ak nenájde, vráti posledné, èo je en-EN
	}
	
	static Locale getLocale() {
		return lokalita;
	}

	static void setLocale(Locale loc) {
		lokalita = loc;
		int cisloLokality = getLocNumber();						// Pre istotu - ak nie je v mojom zozname, tak ju zmením
		lokalita = Const.LOKALITA[cisloLokality];				//  na poslednú, èo je en-US
		Locale.setDefault(lokalita);							// Preoe ResourceBundle.getBundle() vracia štandardnú 
																//  lokalitu, ak ju nájde a pritomnenájde zadanú
		nastavRetazce();
	}
	
	/**
	 * Potlaèenie štandardného konštruktora, aby sa nedali robi exempláre triedy
	 */
	private Lokalizacia() {
		// Tento konštruktor sa nikdy nezavolá
	}

	private static void nastavRetazce() {
		retazce = ResourceBundle.getBundle(Const.BUNDLE_NAME, lokalita, new MyControl());
		
		menovkaTitulku		= getString("LabelOfTitle");			// Text: Titulok okna pripomienky:
		menovkaTextu		= getString("LabelOfText");				// Text: Text v okne pripomienky:
		menoProgramu   		= getString("ApplicationName");			// Text: Pripomienkovaè
		titulokPripomienky  = Const.pripomienka.getTitulok();		// Získanie titulku, ak bol uloenı
		textPripomienky     = Const.pripomienka.getSprava();		// Získanie textu správy, ak bol uloenı
		textPotvrdenia   	= getString("TextOfConfirmation");
		hodin		   		= getString("Hours");					// "hodín";
		minut		   		= getString("Minutes");					// "minút";
		sekund		   		= getString("Seconds");					// "sekúnd";
		opakovane  	  		= getString("RemindEvery");				// Text: Pripomína kadıch (menovka)
		jednorazovo	   		= getString("RemindAfter");				// Text: Pripomenú o (menovka)
		spustit        		= getString("Launch");					// Text na tlaèidle a akcia k nemu priradená
		ukazatOkno    		= getString("ShowWindow");     			// Text: Zobrazi okno
		minimalizovat  		= getString("Continue");				// Text: Pokraèova
		skoncit        		= getString("Exit");					// Text: Skonèi
		menitFarbu        	= getString("CycleColor");				// Text: Meni farbu textu v pripomienkach
		pripomenutLenRaz	= getString("RemindOnlyOnce");			// Text: Pripomenú len raz
		verzia			    = getString("Version");					// Text: Verzia
		verzia			   += Const.VERZIA_END;
		licence			    = getString("License");					// Text: Program môete pouíva a šíri zadarmo.
		licence 		    = Const.LICENCE_BEGIN + licence;
		translator		    = getString("Translator");				// Text: Preklad do slovenèiny: Marián Déneš
		translator 		    = Const.TRANSLATOR_BEGIN + translator;
		
		hodina			    = getString("Hour1");					// Text: hodina
		hodiny			    = getString("Hours24");					// Text: hodiny
		hodinu			    = getString("Hour1L");					// Text: hodinu

		minuta			    = getString("Minute1");					// Text: minúta
		minuty			    = getString("Minutes24");				// Text: minúty
		minutu			    = getString("Minute1L");				// Text: minútu
		
		sekunda			    = getString("Second1");					// Text: sekunda
		sekundy			    = getString("Seconds24");				// Text: sekundy
		sekundu			    = getString("Second1L");				// Text: sekundu

		jednorazovePrip		= getString("OneTimeReminder");			// Text: JEDNORAZOVÉ pripomenutie bude o %d %s.
		ziadneDalsiePrip	= getString("NoNextReminder");			// Text: Ïalšie pripomenutie u nebude a program sa skonèí.
		dalsiePripomenutie	= getString("NextReminder");			// Text: Ïalšie pripomenutie bude o %d %s..
		zmenaCasuNastav		= getString("YouChangedTime");			// Text: Zmenili ste nastavenie èasu pripomínania.;

		nastavenyCas		= getString("TimeToAlarm") + " ";		// Text: Nastavenı èas:
		intervalOpak		= getString("PeriodOfReminder") + " ";	// Text: Interval opakovania:
		
		skonciProgram		= getString("ExitProgram");				// Text: Skonèi program
		naozajSoncit		= getString("AreYouSureToExit");		// Text: Naozaj chcete skonèi pripomínanie
		ntePripomenutie		= getString("NthReminder");				// Text: (Toto je %d. pripomenutie)
		poslPripomenutie	= getString("LastReminder");			// Text: (Toto je %d. a posledné pripomenutie)";

		toolTipUlozit		= getString("ToolTipSave");				// Text: Uloí texty a ostatné nastavenia pripomienky ako predvolené

		infoOPocitaci		= getString("InfoAboutComputer");		// Text: Informácie o Vašom poèítaèi;
		infoVerzia        	= getString("InfoVersion");				// Text: Verzia
		InfoVerziaSpecif  	= getString("InfoVerionOfSpecif");		// Text: Verzia špecifikácie
		InfoPredvKodovanie  = getString("InfoDefaultCoding");		// Text: Predvolené kódovanie súborov:
		InfoOperacnySystem  = getString("InfoOperatingSyst");		// Text: Operaènı systém:
		InfoVerziaArchitekt = getString("InfoVersionArchit");		// Text: %s verzia %s (architektúra %s)
		InfoPredvolJazyKraj = getString("InfoDefLangCountry");		// Text: Predvolenı jazyk a krajina:
		InfoNastavJazykKraj = getString("InfoSelLangCountry");		// Text: Nastavenı jazyk a krajina (v tomto programe):
		InfoPrihlasPouziv   = getString("InfoLoggedUser");			// Text: Prihlásenı pouívate¾:
		
		// Naèítanie predvolenıch údajov z lokalizaèného súboru, ak neboli uloené
		if (titulokPripomienky.equals(""))
			titulokPripomienky = getString("TitleOfReminder");		// Predvolenı titulok okna pripomienky
		if (textPripomienky.equals(""))
			textPripomienky   	= getString("TextOfReminder");		// Predvolenı text pripomienky
	}

	private static final String getString(String kluc) {
		return retazce.getString(kluc);
	}
}
