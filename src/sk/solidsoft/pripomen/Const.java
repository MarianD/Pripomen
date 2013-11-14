package sk.solidsoft.pripomen;

import java.awt.Color;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

/**
 * Trieda konštánt, spoloènıch pre viacero tried
 */
final class Const {
	
	static final String   VERZIA_END        = " 37  (14. 11. 2013)";
	static final String   COPYRIGHT	   	    = "Copyright © RNDr. Marián Déneš, SolidSoft, 2009-2013";
	static final String   LICENCE_BEGIN     = "            ";
	static final String   TRANSLATOR_BEGIN  = "                    ";
	static final String	  EOL	            = System.getProperty("line.separator");
	static final String   TAB               = "          ";

	static final String   IKONA 		    = "/res/Ikona.gif";
	static final String   IKONA_1 	        = "/res/Ikona1.gif";
	static final String   IKONA_DLG 	    = "/res/IkonaDlg.gif";
	static final String   IKONA_TRAY 	    = "/res/IkonaTray.gif";
	static final String   IKONA_TRAY_1      = "/res/IkonaTray1.gif";
	static final String   IKONA_POCITAC     = "/res/Host24.gif";
	static final String   IKONA_ULOZIT      = "/res/Save24.gif";
	static final String   FAREBNY_PRUH      = "/res/MenitFarby.gif";
	static final String   JEDNOFAR_PRUH     = "/res/StalaFarba.gif";
	static final String	  BUNDLE_NAME       = "res/Loc";				// Reazce pre lokalizáciu a predvolené hodnoty

	/*
	 * K¾úèe pre vıber/úschovu permanentného uloenia nastavenıch vlastností pripomienky 
	 */
	static final String	  DEN_POSL_SPUST    = "denPoslednehoSpustenia";	// K¾úè pre vıber/úschovu permanentného uloenia
	static final String   POCET_SPUSTENI    = "pocetSpusteni";			// K¾úè pre vıber/úschovu permanentného uloenia
	static final String   TITULOK_OKNA      = "titulokSpravy";			// K¾úè pre vıber/úschovu permanentného uloenia
	static final String   TEXT_PRIPOMIENKY  = "textPripomienky";		// K¾úè pre vıber/úschovu permanentného uloenia
	static final String	  MENIT_FARBU       = "menitFarbu";				// K¾úè pre vıber/úschovu permanentného uloenia
	static final String   CAS_OPAKOVANIA    = "casOpakovania";			// K¾úè pre vıber/úschovu permanentného uloenia
	static final String   CAS_JEDNOTKA      = "casJednotka";			// K¾úè pre vıber/úschovu permanentného uloenia
	static final String   JEDNORAZOVO	    = "jednorazovo";			// K¾úè pre vıber/úschovu permanentného uloenia
	
	static final String	  POCITAC           = "Pocitac";				// Len názov pre ActionCommand tlaèidla

	/*
	 * Predvolené hodnoty pripomienky
	 */
	static final int 	  STD_CAS 		    =  60; 						// Predvolenı èas pre toèidlo
	static final int 	  CAS_JEDNOT_HODINY =   0;						// Èasová jednotka "Hodiny" pre pole so zoznamom
	static final int 	  CAS_JEDNOT_MINUTY =   1;						// Èasová jednotka "Minúty" pre pole so zoznamom
	static final int 	  CAS_JEDNOT_SEKUND =   2;						// Èasová jednotka "Sekundy" pre pole so zoznamom
	static final int 	  STD_JEDNOTKA 	    = CAS_JEDNOT_MINUTY; 		// Predvolená èasová jednotka
	static final int 	  MIN_CAS 		    =   1; 						// Minimálny èas pre toèidlo
	static final int 	  MAX_CAS 	  	    = 720; 						// Maximálny èas pre toèidlo
	static final int 	  KROK 			    =   1; 						// Krok pre toèidlo
	static final boolean  STD_MENIT_FARBU   = true; 					// Predvolená je cyklická zmena farby textu pripomienky
	static final boolean  STD_JEDNORAZOVO   = false; 					// Predvolené je opakované pripomínanie
	
	static final int 	  ZVUK_SIGNALOV     =   5;						// Poèet zvukovıch signálov pri kadom pripomenutí
	static final int 	  MEDZI_SIGNALMI    = 250;						// Poèet milisekúnd medzi dvoma zvukovımi signálmi

	static final int	  MAX_DLZKA_SPR     =  39;						// Maximálna dåka správy pre ToolTip
	
	static final Color[]  FARBA_PISMA       = {						// Pole farieb pre cyklickú zmenu farby textu pripomienky
												Color.BLUE, 				// modrá
		        								Color.RED, 				    // èervemí
		        								new Color(56, 200, 0), 	    // zelená
		        								Color.MAGENTA, 			    // purpurová
		        								new Color(240, 138, 0), 	// hnedá
		                                                };
	static final Color	  FARBA_JEDNORAZ    = new Color(230, 255, 230);		// zelená
	static final Color	  FARBA_OPAKOV      = new Color(255, 240, 255);		// ruová
	static final int   	  POCET_FARIEB      = FARBA_PISMA.length;
	static final String	  ACTION_SPUSTIT    = "Spusti";
	static final String	  ACTION_SKONCIT    = "Skonèi";
	static final String	  ACTION_UKAZ_OKNO  = "Uká okno";
/*
	// Súvisí s odstránenım (mono doèasne) upozornením v metóde main() triedy PripomenUI
	static final String   OTAZKA		    = "                " +
											 	"Program Pripomen je u %d-krat spustenı." +
											 	Const.EOL + Const.EOL +
											 	"Chcete popri tom spusti  ÏALŠIE  (iné)  pripomienkovanie?";
	static final String   TITULOK_OTAZKY    = "Program Pripomen je u spustenı";
*/

	/*
	 * Nelokalizované reazce pre "tajnú" kombináciu klávesov Ctrl + Home
	 */
	static final String   TITULOK_NULOVANIA = "Vynulova poèítadlo";
	static final String   OTAZKA_NULOVANIA  = "Chystáte sa VYNULOVA poèítadlo spustenıch programov." + EOL +
            									"Ste si ISTÍ, e tento program NIE JE spustenı viackrát?";
	
	static final Locale[]  LOKALITA 		=  {new Locale("sk", "SK"), 
												new Locale("cs", "CZ"), 
												new Locale("en", "US")		// Anglické musí by posledné
												};							
	static final int       POCET_LOKALIT    =  LOKALITA.length;

	static final Pripomienka	 	  pripomienka    = new Pripomienka();
	static final Timer  			  casovac        = new Timer(0, new CasovacListener());
	
	/*
	 * Ovládacie prvky
	 */
	static final JLabel				  lblCasOpak	 = new JLabel();
	
	static final JTextField			  txtTitulok	 = new JTextField();
	static final JTextField			  txtSprava	     = new JTextField();

	static final JSpinner			  sprCasOpak     = new JSpinner();

	static final JButton			  cmdJazyk       = new JButton();
	static final JButton			  cmdPocitac     = new JButton();
	static final JButton			  cmdUlozit      = new JButton();
	static final JButton			  cmdSpustit     = new JButton();
	static final JButton			  cmdMinimalizuj = new JButton();

	static final SpinnerNumberModel	  sprNumModel    = new SpinnerNumberModel();
	static final DefaultComboBoxModel cboModel	     = new DefaultComboBoxModel(); 
	static final JComboBox			  cboCasJednotka = new JComboBox();

	static final JCheckBox			  chkMenitFarbu  = new JCheckBox();
	static final JCheckBox			  chkJednorazovo = new JCheckBox();

	
	/**
	 * Potlaèenie štandardného konštruktora, aby sa nedali robi exempláre triedy
	 */
	private Const() {
		// Tento konštruktor sa nikdy nezavolá
	}
}

