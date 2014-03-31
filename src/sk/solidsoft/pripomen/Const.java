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
 * Trieda konöt·nt, spoloËn˝ch pre viacero tried
 */
final class Const {
	
	static final String   VERZIA_END        = " 40  (xx. x. 2014)";
	static final String   COPYRIGHT	   	    = "Copyright © RNDr. Mari·n DÈneö, 2009-2014";
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
	static final String	  BUNDLE_NAME       = "res/Loc";				// Reùazce pre lokaliz·ciu a predvolenÈ hodnoty

	/*
	 * Kæ˙Ëe pre v˝ber/˙schovu permanentnÈho uloûenia nastaven˝ch vlastnostÌ pripomienky 
	 */
	static final String	  DEN_POSL_SPUST    = "denPoslednehoSpustenia";	// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String   POCET_SPUSTENI    = "pocetSpusteni";			// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String   TITULOK_OKNA      = "titulokSpravy";			// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String   TEXT_PRIPOMIENKY  = "textPripomienky";		// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String	  MENIT_FARBU       = "menitFarbu";				// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String   CAS_OPAKOVANIA    = "casOpakovania";			// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String   CAS_JEDNOTKA      = "casJednotka";			// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	static final String   JEDNORAZOVO	    = "jednorazovo";			// Kæ˙Ë pre v˝ber/˙schovu permanentnÈho uloûenia
	
	static final String	  POCITAC           = "Pocitac";				// Len n·zov pre ActionCommand tlaËidla

	/*
	 * PredvolenÈ hodnoty pripomienky
	 */
	static final int 	  STD_CAS 		    =  60; 						// Predvolen˝ Ëas pre toËidlo
	static final int 	  CAS_JEDNOT_HODINY =   0;						// »asov· jednotka "Hodiny" pre pole so zoznamom
	static final int 	  CAS_JEDNOT_MINUTY =   1;						// »asov· jednotka "Min˙ty" pre pole so zoznamom
	static final int 	  CAS_JEDNOT_SEKUND =   2;						// »asov· jednotka "Sekundy" pre pole so zoznamom
	static final int 	  STD_JEDNOTKA 	    = CAS_JEDNOT_MINUTY; 		// Predvolen· Ëasov· jednotka
	static final int 	  MIN_CAS 		    =   1; 						// Minim·lny Ëas pre toËidlo
	static final int 	  MAX_CAS 	  	    = 720; 						// Maxim·lny Ëas pre toËidlo
	static final int 	  KROK 			    =   1; 						// Krok pre toËidlo
	static final boolean  STD_MENIT_FARBU   = true; 					// Predvolen· je cyklick· zmena farby textu pripomienky
	static final boolean  STD_JEDNORAZOVO   = false; 					// PredvolenÈ je opakovanÈ pripomÌnanie
	
	static final int 	  ZVUK_SIGNALOV     =   5;						// PoËet zvukov˝ch sign·lov pri kaûdom pripomenutÌ
	static final int 	  MEDZI_SIGNALMI    = 250;						// PoËet milisek˙nd medzi dvoma zvukov˝mi sign·lmi

	static final int	  MAX_DLZKA_SPR     =  39;						// Maxim·lna dÂûka spr·vy pre ToolTip
	
	static final Color[]  FARBA_PISMA       = {						// Pole farieb pre cyklick˙ zmenu farby textu pripomienky
												Color.BLUE, 				// modr·
		        								Color.RED, 				    // ËervemÌ
		        								new Color(56, 200, 0), 	    // zelen·
		        								Color.MAGENTA, 			    // purpurov·
		        								new Color(240, 138, 0), 	// hned·
		                                                };
	static final Color	  FARBA_JEDNORAZ    = new Color(230, 255, 230);		// zelen·
	static final Color	  FARBA_OPAKOV      = new Color(255, 240, 255);		// ruûov·
	static final int   	  POCET_FARIEB      = FARBA_PISMA.length;
	static final String	  ACTION_SPUSTIT    = "Spustiù";
	static final String	  ACTION_SKONCIT    = "SkonËiù";
	static final String	  ACTION_UKAZ_OKNO  = "Uk·û okno";

	/*
	 * NelokalizovanÈ reùazce pre "tajn˙" kombin·ciu kl·vesov Ctrl + Home
	 */
	static final String   TITULOK_NULOVANIA = "Vynulovaù poËÌtadlo";
	static final String   OTAZKA_NULOVANIA  = "Chyst·te sa VYNULOVAç poËÌtadlo spusten˝ch programov." + EOL +
            									"Ste si ISTÕ, ûe tento program NIE JE spusten˝ viackr·t?";
	
	static final Locale[]  LOKALITA 		=  {new Locale("sk", "SK"), 
												new Locale("cs", "CZ"), 
												new Locale("no", "NO"), 
												new Locale("en", "US")		// AnglickÈ musÌ byù poslednÈ
												};							
	static final int       POCET_LOKALIT    =  LOKALITA.length;
	
	static final String[]  LANGUAGE         =  {new String("Slovak"),		 
												new String("Czech"),
												new String("Norwegian"),
												new String("English"),
											    };

	static final Pripomienka	 	  pripomienka    = new Pripomienka();
	static final Timer  			  casovac        = new Timer(0, new CasovacListener());
	
	/*
	 * Ovl·dacie prvky
	 */
	static final JLabel				  lblCasOpak	 = new JLabel();
	
	static final JTextField			  txtTitulok	 = new JTextField();
	static final JTextField			  txtSprava	     = new JTextField();

	static final JSpinner			  sprCasOpak     = new JSpinner();

	static final JComboBox            cboJazyk       = new JComboBox(LANGUAGE);
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
	 * PotlaËenie ötandardnÈho konötruktora, aby sa nedali robiù exempl·re triedy
	 */
	private Const() {
		// Tento konötruktor sa nikdy nezavol·
	}
}

