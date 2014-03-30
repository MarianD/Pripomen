package sk.solidsoft.pripomen;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.prefs.*;

import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JOptionPane;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/*
 * 29.3.2009:  Znemonenie kontextovej ponuky ikony na podnose (aj dôsledku zaklopania na òu) poèas zobrazenie
 *             pripomienkovacieho okna.
 *            
 *  1.4.2009:  Lokalizácia (zatia¾ vyuitá len na zadanie mena programu, predvoleného titulku
 *             pripomienkovacieho okna a predvoleného textu v òom - v súbore Loc.properties)
 *            
 *  8.4.2009:  Pre systémovú oblas dorobená malá ikona (16 x 16)
 *  
 *  2.8.2009:  Poèíta poèet spustení a pri štarte sa pıta, èi má program znovu spusti.
 *             Pripomínacie okienko sa nedá zavrie klopnutím na zatváracie tlaèidlo (to spôsobovalo chybu,
 *             lebo okienko sa len skrylo, a neodohmlila sa kontextová ponuka na systémovom podnose).
 *  
 *  3.8.2009:  Cyklické striedanie farieb písma v pripomienkovacom okne (pri kadom novom pripomenutí iná farba písma).
 *  		   Mierny refaktoring.
 *  
 *  4.8.2009:  V hlavnom okne (úvodnom nastavovacom okienku) som tleèidlo Nastavi nastavil ako štandardné, take som
 *             mohol zruši obsluhu klávesa Enter - zisk je ten, e Look and Feels môu to tlaèidlo zvırazni.
 *  
 * 30.8.2009:  Ukladá si permanentne deò v roku posledného spustenia, a keï pri spustení nesúhlasí s aktuálnym dòom,
 *             resetuje uloenı poèet spustení - to pre prípad, e sa predtım na konci dòa program neukonèil korektne.
 *             V ToolTipe zobrazuje aj text správy - ak je pridlhı, zobrazí len zaèiatok (cca 2/3 povolenej dåky),
 *             bodky a koniec (cca 1/3 povolenej dåky).
 *             V pripomienkovacom okne sa poènúc od 1. pripomenutia (teda NIE ihneï po nastavení) zobrazuje poradové
 *             èíslo pripomenutia.
 *  
 * 31.8.2009:  Do lokalizácie (súbor Loc.properties) pridané zadanie textu na potvrdzujúcom tlaèidle
 *             okna pripomienky.
 *  
 *  2.9.2009:  Do hlavného okna programu pridané tlaèidlo "Minimalizova", ktoré umoní minimalizova okno (na podnos)
 *             rovnako ako minimalizaèné tlaèidlo v záhlaví okna. Toto tlaèidlo sa však zjaví a po spustení pripo-
 *             mienkovania, t.j. po obnove okna z ikony na podnose, a po spustení pripomienkovania sa stáva štandardnım
 *             tlaèidlom okna.
 *  
 *  3.9.2009:  Drobnı refaktoring
 *  
 *  6.9.2009:  Väèší refaktoring.
 *             Pridlhı text správy je na podnose skrátenı tak, e konèí celım poslednım slovom.
 *  
 *  8.9.2009:  Ïalší refaktoring.
 *             Koneène namiesto java.util.Timer, java.util.TimerTask a java.util.Timer.schedule pouité javax.swing.Timer,
 *             java.awt.event.ActionListener a javax.swing.Timer.start().
 *             Ako príjemnı ved¾ajší produkt sa po uzamknutí poèítaèa na dlhší èas zjaví len jedno zmeškanépripomienkovacie
 *             okno, a nie hneï 2 za sebou.
 *             Vyskúšaná aj monos poèas behu èasovaèa zmeni nastavenia (titulok okna, text správy aj interval opakovania)
 *             - ale vrátil som to spä, take zatia¾ je to ešte neimplementované.
 *  
 * 13.9.2009:  Pridané zaškrtávacie políèko pre vo¾bu, èi sa má meni farba textu v pripomienkovacom okne.
 * 			   Monos zvoli èas pripomienkovania v hodinách, minútach alebo v sekundách.
 * 			   Predvolené nastavenie pripomienkovania zmenené zo "60 minút" na "1 hodinu".
 * 			   V tooltipe ikony na systémovom podnose opravené skloòovanie pri 1 minúte (hodine, sekunde).
 * 			   Monos dynamicky (po spustení èasovaèa) meni titulok pripomienkovacieho okna, text správy v òom, ako aj to,
 * 			   èi sa majú meni farby pripomienkovacích správ.
 * 			   Vylepšenı dizajn úvodného (nastavovacieho) okna.           		
 *  
 * 14.9.2009:  Ešte viac vylepšenı a skrásnenı dizajn úvodného (nastavovacieho) okna.
 * 			   Pri pokuse o skonèenie programu vypíše v dialógovom okne aj (prípadne skrátenı) text pripomienky.
 * 			   Monos vynulova poèítadlo spustenıch programov tajnou kombináciou Ctrl+Home v textovom poli hlavného okna
 * 			   programu.
 * 			   Ïalší refaktoring.
 *  
 * 29.9.2009:  Monos jednorazového pripomenutia zaškrtávacím políèkom v hlavnom okne.
 * 			   Pri jeho zaškrtnutí / odškrtnutí sa dynamicky mení
 * 			     - ikona okna, aby tomu zodpovedala,
 * 			  	 - menovka èasového nastavenia, aby tomu zodpovedala.
 * 			   Pri jednorazovom pripomenutí:
 * 				 - inı dodatkovı text v pripomienkovacom okne
 * 				 - inı text v jeho ozname o èísle pripomienky
 * 				 - inı text na jeho potvrdzovacom tlaèidle
 * 				 - iná ikona na podnose
 * 				 - iná ikona hlavného okna,
 * 				 - inı text pri èasovom oznaèení v tooltipe ikony na podnose.
 * 			   Zmenil som písmo v hlavnom aj v pripomienkovacom okne na Arial (namiesto pôvodného - logického - mena Dialog),
 * 			   pretoe v nemeckıch Windows boli niektoré znaky s diakritikou nesprávne.
 * 			   Opravená chyba pri nulovaní poèítadla spustenıch programov.
 * 
 * 30.9.2009:  Mierny refaktoring.
 * 			   Jednorazovos/opakovanos pripomienkovania dynamicky signalizovaná aj farbou pozadia textového po¾a pre text
 *			   správy v hlavnom okne.
 *			   Menenie farieb je dynamicky signalizované farebnım prúkom v hlavnom okne.
 *			   Menenie fariem je signalizované aj farebnım prúkom v pripomienkovacom okne.
 *			   Zmenil som písmo v kontextovej ponuke ikony na podnose na Arial.
 * 
 * 01.10.2009: Vypnuté menenie farieb je signalizované jednofarebnım prúkom v hlavnom aj pripomienkovacom okne.
 * 
 * 24.11.2009: Zrušil som zmeny písma z 29. a 30.9.2009 a nastavil som štandardnú lokalizáciu na slovenèinu a Slovensko,
 * 			   aby aj v nemeckıch Windows sa správne naèítali zdroje a aj zobrazovali znaky s diakritikou. Funguje to!
 * 
 * 08.12.2009: V titulkovom pruhu hlavného okna sa zobrazuje tie verzia JRE (Java Runtime Environment)
 * 
 * 23.12.2009: Namiesto zloitého zisovania rozmerov obrazovky a okna a vypoèítavania pozície pre umiestnenie hlavného
 * 			   okna a pripomienkovacieho okna do stredu obrazovky pouitá metóda setLocationRelativeTo(null).
 * 			   Pridané obrázkové tlaèidlo s poèítaèom, ktoré zobrazí verziu JRE (Java Runtime Environment) a súèasne
 * 			   zrušenie tohto oznamu v titulkovom pruhu (viï predchádzajúca verzia). Okrem toho zobrazí aj mnohé ïalšie
 * 			   systémové informácie.
 * 
 * 04.01.2010: Len refaktoring - vïaka tomu, e konštantné reazce a reazcové literály sú vdy internované (viï
 * 			   String.intern()), som nahradil všetky vhodné reazcové ".equals" efektívnejšou operáciou "=="
 * 			   (išlo len o triedu ObsluhyUdalosti).
 * 
 * 07.01.2010: Zaèiatok refaktoringu - zníenie úrovne prístupu niektorıch polí (na "private"). Treba to dokonèi.
 * 
 * 09.01.2010: Len refaktoring:
 * 			   Z modulu "ObsluhyUdalosti.java" odstránená prázdna trieda ObsluhyUdalosti. (Modul nemusí ma ani jednu
 *             verejnú triedu.) Všetky triedy okrem PripomenUI zbavené prívlastku "public". V triede Staticke zmarené
 *             jej dedenie atrubútom "final" a zmarené vytváranie exemplárov z nej definovaním (nikdy nepouitého)
 * 
 * 10.01.2010: Len refaktoring:
 *             V triede MenuItemMoje urobené všetky èleny ako "final", aby trieda poskytovala len nezmenite¾né exempláre.
 * 
 * 12.01.2010: Len refaktoring:
 * 			   Všetky svoje triedy som dal ako "final"
 * 
 * 13.01.2010: Len refaktoring:
 * 			   Všetky konštanty v triede PripomenUI, ktoré neboli súkromné (lebo sa pouívali vo viacerıch triedach, alebo
 * 			   pretoe sa síce pouili len v jedinej triede, ale inej ne PripomenUI - z dôvodu ich udrania pohromade boli
 * 			   v hlavnej triede PripomenUI), som dal do novej triedy Const, z ktorej sa nedajú robi exempláre.
 *			   Triedy, ktoré ich pouívajú, som upravil.
 *			   Dokonèená zmena prístupu polí na "private" (zaèatá 7.1.2001)
 * 
 * 17.01.2010: Len refaktoring:
 * 			   V tiede PripomenUI všetky premenné pre obsluhy udalostí (listenery) zmenené zo statickıch polí triedy na
 *             lokálne premenné metódy pridajObsluhy().
 * 
 * 20.01.2010: Len refaktoring:
 * 			   Z triedy Const som všetky polia, ktoré neboli skutoènımi konštantami, presunul do novovytvorenej triedy
 * 			   Spolocne
 * 
 * 27.02.2010: Do zobrazovania systémovıch informácií som pridal aj predvolené kódovanie súborov Java Virtual Machine.
 * 
 * 28.02.2010: Pretoe pri naèítaní zo súboru sa pouíva predvolené kódovanie JVM, nedá sa spolieha na to, e písmená ako
 * 			   "è" a "" sa naèítajú správne - preto som v súbore vlastností Loc.properties napísal tieto znaky ako
 * 				\u010d a \u0165. Pre istotu som aj v súbore Const.java texty na tlaèidlách zapísal podobne.
 * 
 * 02.03.2010: Zmena v skutoènosti vykonaná v triede sk.solidsoft.internationalization.MyControl - pri vytváraní
 * 			   InputStreamReadera som pridal ïalší parameter - Charset - ako "cp1250", aby virtuálny stroj s inım
 *             predvolenım kódovaním pracoval správne s reazcami v Loc.properties, pripravenımi na mojom poèítaèi,
 *             bez potreby kódova znaky s diakritikou - viï predchádzajúcu verziu.
 * 
 * 09.03.2010: Vzh¾adom k predchádzajúcim zmenám je zbytoèné nastavova Locale na sk_SK. Odstránil som to.
 * 
 * 14.03.2010: V module "Staticke.java" upravená funkcia pripadneSkrat(), ktorá skracuje text správy pre dialógové
 *             okno s otázkou na skonèenie programu - pôvodnému reazcu sa najprv osekajú úvodné a závereèné medzery
 * 			   (hlavne závereèné spôsobovali problém, lebo posledné slovo sa h¾adalo od poslednej medzery).
 *            
 * 22.06.2011: Príprava na lokalizácie do rôznych jazykov - v súbore Loc.properties zmenené názvy premennıch na anglické
 * 
 * 02.07.2011: Do hlavného okna pridanı text licencie ("Program môete pouíva aj šíri zadarmo.")
 * 
 * 03.07.2011: Zaèatá lokalizácia - v súbore Const.java
 * 
 * 13.07.2011: Pokraèovanie lokalizácie v súbore Const.java
 * 
 * 04.08.2011: Pokraèovanie lokalizácie v súbore Const.java a ve¾kı refaktoring:
 * 			   V súbore Const.java ponechané len skutoèné, nikdy sa nemeniace konštanty - ostatné, urèené pre lokalizáciu,
 * 			   prenesené do novovytvoreného súboru Lokalizacia.java (s jedninou triedou Lokalizacia v nej).
 * 			   Aj zo súboru PripomenUI.java prenesené všetko, tıkajúce sa lokalizácie, do triedy Localizacia. V triede
 * 			   Lokalizacia vytvorené metódy setLocale() a pomocné - súkromné - metódy gatString() a nastavRetazce().
 *             Do informácií o poèítaèi som pridal (viac-menej pre seba pre testovanie) aj nastavenı jazyk a krajinu
 * 
 * 05.08.2011: Pridaná ikona pre uloenie nastavenej pripomienky - ukladá text titulku a správy do preferencií
 *             (vo Windows je to HKEY_CURRENT_USER\\Software\JavaSoft\Prefs).
 * 
 * 06.08.2011: Pokraèovanie v lokalizácii, prenesené skutoèné konštanty z triedy PripomenUI do triedy Const, mierny
 * 			   refaktoring
 * 
 * 07.08.2011: Pokraèovanie v lokalizácii a prenesené ïalšie skutoèné konštanty z triedy PripomenUI do triedy Const,
 * 			   mierny refaktring
 * 
 * 08.08.2011: Mierny refaktoring
 * 
 * 09.08.2011: Pokraèovanie v lokalizácii
 * 
 * 13.08.2011: Nová trieda Pripomienka - údajovı model pre okno pripomienky, všetky údaje na jednom mieste
 * 
 * 14.08.2011: Ukladanie zadanıch údajov metódou ulozMa() triedy Pripomienka
 * 
 * 15.08.2011: Obnovenie zadanıch údajov metódou obnovMaZdisku() triedy Pripomienka
 * 			   Program obnovuje u aj uloené menenie farieb, jednorazovos, nastavenı èas a èasovú jednotku, teda u VŠETKO
 * 			   Monos meni jednorazovos, èas a èasovú jednotku aj po spustení pripomienkovania, teda u VŠETKO
 * 			   Èasovaè prenesenı do triedy Spolocne
 * 			   iaden ovládací prvok v triede Spolocne u nie je inicializovanı (okrem èasovaèa, ktorı musí by) 
 * 			   - všetky sa inicializujú v triede PripomenUI, v metóde nastavVlastnostiPrvkov().
 * 			   Úprava kódu, aby si naèítaval údaje vıluène z exeplára triedy Pripomienka.
 * 			   Pri dodatoènej zmene èasu alebo èasovej jednotky zobrazí o tom okamite správu, prièom preruší èíslovanie
 * 			   aj zmenu farby.
 * 
 * 16.08.2011: Niektoré na tvrdo písané texty prenesené do triedy Const (potom pôjdu do triedy Lokalizacia)
 * 
 * 17.08.2011: Pridané fungujúce tlaèidlo na zobrazenie / nastavenie jazyka
 * 			   Refaktoring - odstránené zbytoèné ActionCommand tlaèidiel, ostatné nastavené nezávisle od ich názvu (lokality)
 * 
 * 18.08.2011: Refaktoring - zmena nevhodnıch mien na vhodnejšie, zavedehé konštanty pre èasovú jednotku a ïalšie konštanty
 * 			   Opravená chyba - pri dodatoènej zmene jednorazovosti sa text v ToolTipe ikony príslušne nezmenil.
 * 			   Obrovská lokalizácia programu.
 * 
 * 19.08.2011: Pokraèovanie v lokalizácii
 * 
 * 20.08.2011: Pokraèovanie v lokalizácii
 * 
 * 21.08.2011: Pokraèovanie v lokalizácii - okrem "tajnej" klávesovej kombinácie Ctrl + Home dokonèené.
 * 			   Odstránené (mono doèasne) upozoròovanie na to, e program je u spustenı - aj kvôli problémom s lokalizáciou.
 * 
 * 22.08.2011: Opravená chyba - pri zmene jazyka sa neuchovávala nastavená èasová jednotka
 * 			   Opravená chyba - nelokalizoval sa ToolTip ikony poèítaèa
 * 			   Lokalizácia pre ToolTip ikony uloenia
 * 			   Ve¾kı refaktoring - skoro všetko z triedy Spolocne prenesené do triedy Const
 * 
 * 23.08.2011: Opravená chyba - pri zmene jazyka sa neuchovávali pouvate¾om zmenené texty. Presunul som ich lokalizované
 * 			   nastavovanie (v triede PripomenUI) z metódy nastavLokalizovanéTexty() (ktorá sa volá opakovane pri
 * 			   kadej zmene jazyka) do metódy nastavVlastnostiPrvkov (ktorá sa volá len raz). Tım som 
 * 
 * 25.08.2011: V tejto triede v metóde main() pripravené (opoznámkované) pouitie iného Look and Feel. V triede DlgPripomen()
 * 			   v súvislosti s tım v metóde NastavVlastnosti nastavené natvrdo písmo a biele pozadie.
 * 
 * 19.10.2013: Lokalizaèné súbory: Namiesto uvedenia koncovej medzery ("\ ") v k¾úèovıch slovách TimeToAlarm 
 *			   a PeriodOfReminder táto pridaná programovo (v triede Lokalizacia)
 * 			   Prechod z kódovania cp1250 na utf8 pre lokalizaèné súbory (úprava urobená v triede
 * 			   sk.solidsoft.internationalization.MyControl)
 * 
 * 11.11.2013: Do hlavného okna pridané meno prekladate¾a do príslušného jazyka 
 * 14.11.2013: Do hlavného okna pridanı tooltip tlaèidlu pre vıber jazyka 
 * 26.02.2014: Pridaná nórska lokalizácia (autor: Michael Sagnes, kvikk92@gmail.com) 
 * 30.03.2014: Tlaèidlo pre zmenu jazyka nahradené po¾om so zoznamom 
 */

public final class PripomenUI extends JFrame {
	private static final long 	 serialVersionUID  = 1L;
	
	private static  	 Locale		origLokalita   = Locale.getDefault();		// Kvôli testovaniu bude ešte raz priradené

	private static final JLabel 	lblTitulok     = new JLabel();
	private static final JLabel 	lblSprava      = new JLabel();
	private static final JLabel 	lblMenitFarby  = new JLabel();
	private static final JLabel 	lblCopyright   = new JLabel(Const.COPYRIGHT);
	private static final JLabel 	lblLicence     = new JLabel();
	private static final JLabel 	lblTranslator  = new JLabel();
	private static final SystemTray podnos    	   = SystemTray.getSystemTray();
	private static final PopupMenu 	popup          = new PopupMenu();
	
	private static final Toolkit 	defaultToolkit = Toolkit.getDefaultToolkit();	// Pre prístup k zdrojom v súbore .jar

	private static Image ikonaProgramu;
	private static Image ikonaTray;
	private static Image ikonaPocitac;
	private static Image ikonaUlozit;
	private static Image imgMenitFarby;
	private static Image imgStalaFarba;
	
	private static Icon  icoMenitFarby;
	private static Icon  icoStalaFarba;

	private MenuItemMoje miObnovit = new MenuItemMoje(this);
	private MenuItemMoje miSkoncit = new MenuItemMoje(this);

	
	static Locale getOrigLokalita() {
		return origLokalita;
	}

	public PripomenUI() {
		Const.pripomienka.obnovMaZdisku();

//		Locale.setDefault(new Locale("sk", "SK"));			// Len pre testovanie
//		Locale.setDefault(new Locale("cs", "CZ"));			// Len pre testovanie
//		Locale.setDefault(new Locale("no", "NO"));			// Len pre testovanie
//		Locale.setDefault(new Locale("fr", "FR"));			// Len pre testovanie
		
		origLokalita = Locale.getDefault();					// Ešte predtım, ne ju pouívate¾ zmení
		Lokalizacia.setLocale(origLokalita);				// Originálne nastavenie jazyka
		
		nastavVlastnostiPrvkov();							// Nelokalizované vlastnosti ovládacích prvkov
		nastavLoklizovaneTexty();							// Lokalizované vlastnosti ovládacích prvkov
		rozmiestniPrvky();									// Rozmiestnenie ovládacích prvkov
		pridajObsluhy();									// Obsluhy udalostí ovládacích prvkov
		pack();												// Nastavenie na minimálnu ve¾kos okna
		
		setLocationRelativeTo(null);						// Okno do stredu obrazovky

		Const.cmdMinimalizuj.setVisible(false);
		setVisible(true);
	}

	void zmenPriznakFarebnosti() {
		boolean menitFarby = Const.pripomienka.isMenitFarbu();
		JLabel lblMenitFarby2 = DlgPripomen.lblMenitFarby;

		if (menitFarby) {
			lblMenitFarby .setIcon(icoMenitFarby);
			lblMenitFarby2.setIcon(icoMenitFarby);
		} else {
			lblMenitFarby .setIcon(icoStalaFarba);
			lblMenitFarby2.setIcon(icoStalaFarba);
		}
	}

	void zmenPriznakyJednorazovosti() {
		boolean jednorazovo = Const.chkJednorazovo.isSelected();
		Color farbaPozadia;

		if (jednorazovo) {
			ikonaProgramu = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_1));
			ikonaTray = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_TRAY_1));
			farbaPozadia = Const.FARBA_JEDNORAZ;
		} else {
			ikonaProgramu = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA));
			ikonaTray = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_TRAY));
			farbaPozadia = Const.FARBA_OPAKOV;
		}

		this			  .setIconImage(ikonaProgramu);
		Spolocne.trayIcon .setImage(ikonaTray);
		Const.txtSprava	  .setBackground(farbaPozadia);
	}

	void dajIkonuNaPodnos() {
		boolean jednorazovo = Const.chkJednorazovo.isSelected();

		setVisible(false);
		setExtendedState(JFrame.NORMAL); // Kvôli neskoršej obnove
		setAlwaysOnTop(true); 			 // Aby sa po zaklopaní na ikonu na podnose dostalo navrch; ihneï potom sa toto vypne

		if (jednorazovo)
			ikonaTray = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_TRAY_1));
		else
			ikonaTray = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_TRAY));

		Spolocne.trayIcon.setImage(ikonaTray);

		if (podnos.getTrayIcons().length == 0) { // Ikona na podnose ešte nie je
			try {
				podnos.add(Spolocne.trayIcon);
			} catch (AWTException e) {
				System.err.println(e);
			}
		}
	}

	void nastavLoklizovaneTexty() {
		setTitle(Lokalizacia.menoProgramu + " " + Lokalizacia.verzia);
		
		lblTitulok			.setText(Lokalizacia.menovkaTitulku);
		lblSprava 			.setText(Lokalizacia.menovkaTextu);
		
		Const.lblCasOpak    .setText(Lokalizacia.opakovane);
		Const.chkMenitFarbu .setText(Lokalizacia.menitFarbu);
		Const.chkJednorazovo.setText(Lokalizacia.pripomenutLenRaz);
		Const.cmdSpustit	.setText(Const.cmdSpustit.getActionCommand() == Const.ACTION_SKONCIT ? 
										Lokalizacia.skoncit : Lokalizacia.spustit);
		Const.cmdMinimalizuj.setText(Lokalizacia.minimalizovat);
		
		/*
		 * Takto zloito nastavujem pole so zoznamom pre prípad zmeny jazyka, aby sa zachovala vybraná èasová jednotka
		 */
		int index = Const.cboCasJednotka.getSelectedIndex();
		if (index == -1) {											// Èasová jednotka ešte nenastavená
			index = Const.pripomienka.getCasJednotka();
		}
		Const.cboModel      .removeAllElements();
		Const.cboModel		.addElement(Lokalizacia.hodin);
		Const.cboModel		.addElement(Lokalizacia.minut);
		Const.cboModel		.addElement(Lokalizacia.sekund);
		Const.cboCasJednotka.setSelectedIndex(index);
		Const.cboJazyk	   	.setToolTipText(Lokalizacia.toolTipJazyk);
		Const.cmdPocitac 	.setToolTipText(Lokalizacia.infoOPocitaci);
		Const.cmdUlozit  	.setToolTipText(Lokalizacia.toolTipUlozit);
	
		lblLicence			.setText(Lokalizacia.licence);
		lblTranslator		.setText(Lokalizacia.translator);
	
		miObnovit.setLabel		(Lokalizacia.ukazatOkno);
		miSkoncit.setLabel		(Lokalizacia.skoncit);
		Staticke.nastavToolTipIkony(Const.pripomienka.getCasOpakovania());
		
		// popup.setFont(PISMO);
		popup.removeAll();
		popup.add(miObnovit);
		popup.add(miSkoncit);
	}

	private void nastavVlastnostiPrvkov() {

		Const.cboJazyk  .setSelectedIndex(Const.POCET_LOKALIT - 1);		// First set the language to English
		
		String language = Lokalizacia.getLocale().getLanguage();
		for (int i = 0; i < Const.POCET_LOKALIT; ++i)					// Trying all supported languages
			if (language == Const.LOKALITA[i].getLanguage()) {
				Const.cboJazyk.setSelectedIndex(i);
				break;
			}

		Const.txtTitulok.setFont(Const.txtTitulok.getFont().deriveFont(Font.ITALIC));
		Const.txtTitulok.setPreferredSize(new Dimension(200,Const.txtTitulok
						.getPreferredSize().height + 5));
		Const.txtTitulok.setMaximumSize(new Dimension(200, Const.txtTitulok
						.getPreferredSize().height + 5));
		Const.txtSprava	.setFont(Const.txtSprava.getFont().deriveFont((float) Const.txtSprava
						.getFont().getSize() + 8));
		Const.txtSprava	.setPreferredSize(new Dimension(Const.txtSprava
						.getPreferredSize().width, Const.txtSprava
						.getPreferredSize().height + 15));
		Const.txtTitulok.setText(Lokalizacia.titulokPripomienky);
		Const.txtSprava	.setText(Lokalizacia.textPripomienky);
		lblCopyright	.setFont(lblCopyright.getFont().deriveFont(Font.PLAIN));
		lblLicence		.setFont(lblLicence.getFont().deriveFont(Font.PLAIN));
		lblTranslator	.setFont(lblLicence.getFont().deriveFont(Font.PLAIN));

		imgMenitFarby 	  = defaultToolkit.getImage(PripomenUI.class.getResource(Const.FAREBNY_PRUH));
		imgStalaFarba 	  = defaultToolkit.getImage(PripomenUI.class.getResource(Const.JEDNOFAR_PRUH));
		ikonaProgramu 	  = defaultToolkit.getImage(PripomenUI.class.getResource(Const.IKONA));
		Spolocne.ikonaDlg = defaultToolkit.getImage(PripomenUI.class.getResource(Const.IKONA_DLG));
		ikonaPocitac 	  = defaultToolkit.getImage(PripomenUI.class.getResource(Const.IKONA_POCITAC));
		ikonaUlozit  	  = defaultToolkit.getImage(PripomenUI.class.getResource(Const.IKONA_ULOZIT));
		ikonaTray 		  = defaultToolkit.getImage(PripomenUI.class.getResource(Const.IKONA_TRAY));
		Spolocne.trayIcon = new TrayIcon(ikonaTray, "", popup);
		Spolocne.trayIcon.setImageAutoSize(true);

		icoMenitFarby 	 = new ImageIcon(imgMenitFarby);
		icoStalaFarba 	 = new ImageIcon(imgStalaFarba);
		
		Const.cmdPocitac .setIcon(new ImageIcon(ikonaPocitac));
		Const.cmdUlozit  .setIcon(new ImageIcon(ikonaUlozit));
		Const.cmdUlozit  .setVisible(false);

		zmenPriznakyJednorazovosti();
		zmenPriznakFarebnosti();

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Const.chkMenitFarbu .setSelected(Const.pripomienka.isMenitFarbu());
		zmenPriznakFarebnosti();

		Const.sprCasOpak	.setMaximumSize(new Dimension(42, 25));
		Const.sprCasOpak	.setPreferredSize(new Dimension(42, 25));
		Const.sprNumModel	.setMinimum(Const.MIN_CAS);
		Const.sprNumModel	.setMaximum(Const.MAX_CAS);
		Const.sprNumModel	.setStepSize(Const.KROK);
		Const.sprNumModel	.setValue(Const.pripomienka.getCasOpakovania());
		Const.sprCasOpak	.setModel(Const.sprNumModel);
		Const.cboCasJednotka.setModel(Const.cboModel);
		Const.cboCasJednotka.setMaximumSize(new Dimension(60, 25));
		
		Const.chkJednorazovo.setSelected(Const.pripomienka.isJednorazovo());
		zmenPriznakyJednorazovosti();

		Const.cmdSpustit	.setActionCommand(/*skoncit ? Const.ACTION_SKONCIT : */Const.ACTION_SPUSTIT);
		miObnovit			.setActionCommand(Const.ACTION_UKAZ_OKNO);
		miSkoncit			.setActionCommand(Const.ACTION_SKONCIT);
		getRootPane()		.setDefaultButton(Const.cmdSpustit);
	}

	private void rozmiestniPrvky() {
		Container   cp     = getContentPane();
		GroupLayout grpLay = new GroupLayout(cp);
		grpLay.setHonorsVisibility(false); 			// Aby mali aj nevidite¾né komponenty rezervované miesto
		cp    .setLayout(grpLay);

		grpLay.setAutoCreateGaps(true);
		grpLay.setAutoCreateContainerGaps(true);

		// Skupina pre nastavenie textu titulku a poèítaèa
		SequentialGroup titulkovaSkupinaVodorovne = grpLay.createSequentialGroup();
		ParallelGroup titulkovaSkupinaZvislo	  = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);

		titulkovaSkupinaVodorovne.addComponent(Const.txtTitulok)
		        .addGap(13)
				.addComponent(Const.cboJazyk)
				.addGap(12)
				.addComponent(Const.cmdUlozit)
				.addGap(12)
				.addComponent(Const.cmdPocitac);

		titulkovaSkupinaZvislo
				.addComponent(Const.txtTitulok)
				.addComponent(Const.cboJazyk)
				.addComponent(Const.cmdUlozit)
				.addComponent(Const.cmdPocitac);

		// Skupina pre nastavenie èasu
		SequentialGroup casovaSkupinaVodorovne = grpLay.createSequentialGroup();
		ParallelGroup casovaSkupinaZvislo = grpLay
				.createParallelGroup(GroupLayout.Alignment.BASELINE);

		casovaSkupinaVodorovne.addComponent(Const.sprCasOpak)
				.addComponent(Const.cboCasJednotka).addGap(88)
				.addComponent(Const.cmdMinimalizuj)
				.addComponent(Const.cmdSpustit);

		casovaSkupinaZvislo.addComponent(Const.sprCasOpak)
				.addComponent(Const.cboCasJednotka)
				.addComponent(Const.cmdMinimalizuj)
				.addComponent(Const.cmdSpustit);

		// Skupina pre nastavenie menenia farieb
		SequentialGroup farebnaSkupinaVodorovne = grpLay.createSequentialGroup();
		ParallelGroup farebnaSkupinaZvislo      = grpLay.createParallelGroup(GroupLayout.Alignment.CENTER);

		farebnaSkupinaVodorovne
				.addComponent(Const.chkMenitFarbu)
				.addComponent(lblMenitFarby);

		farebnaSkupinaZvislo
				.addComponent(Const.chkMenitFarbu)
				.addComponent(lblMenitFarby);

		// Hlavné ståpce pre rozmiestnenie ovládacích prvkov (a ich skupín)
		ParallelGroup stlpec1 = grpLay.createParallelGroup(GroupLayout.Alignment.TRAILING);
		ParallelGroup stlpec2 = grpLay.createParallelGroup();

		stlpec1 .addComponent(lblTitulok).addComponent(lblSprava)
				.addComponent(Const.lblCasOpak);

		stlpec2 .addGroup(titulkovaSkupinaVodorovne)
				.addComponent(Const.txtSprava)
				.addGroup(farebnaSkupinaVodorovne)
				.addGroup(casovaSkupinaVodorovne)
				.addComponent(Const.chkJednorazovo)
				.addComponent(lblCopyright).addComponent(lblLicence)
				.addComponent(lblTranslator);

		SequentialGroup vodorovnaSkupina = grpLay.createSequentialGroup();

		vodorovnaSkupina.addGroup(stlpec1).addGroup(stlpec2);

		grpLay.setHorizontalGroup(vodorovnaSkupina);

		// Hlavné riadky pre rozmiestnenie ovládacích prvkov (a ich skupín)
		ParallelGroup riadok1 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok2 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok3 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok4 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok5 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok6 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok7 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);
		ParallelGroup riadok8 = grpLay.createParallelGroup(GroupLayout.Alignment.BASELINE);

		riadok1.addComponent(lblTitulok).addGroup(titulkovaSkupinaZvislo);
		riadok2.addComponent(lblSprava) .addComponent(Const.txtSprava);
		riadok3.addGroup	(farebnaSkupinaZvislo);
		riadok4.addComponent(Const.lblCasOpak).addGroup(casovaSkupinaZvislo);
		riadok5.addComponent(Const.chkJednorazovo);
		riadok6.addComponent(lblCopyright);
		riadok7.addComponent(lblLicence);
		riadok8.addComponent(lblTranslator);

		SequentialGroup zvislaSkupina = grpLay.createSequentialGroup();

		zvislaSkupina
				.addGroup(riadok1).addGap(10).addGroup(riadok2).addGap(03)
				.addGroup(riadok3).addGap(18).addGroup(riadok4).addGap(03)
				.addGroup(riadok5).addGap(15).addGroup(riadok6).addGap(03)
				.addGroup(riadok7).addGap(03).addGroup(riadok8);

		grpLay.setVerticalGroup(zvislaSkupina);
	}

	private void pridajObsluhy() {
		KeyPressListener  keyPressLisetner   = new KeyPressListener();
		CmdButtonListener cmdButtonListener  = new CmdButtonListener();
		CheckBoxListener  chkBoxnListener    = new CheckBoxListener();
		ActionListener    podnosListener     = new PodnosListener();
		TxtChangeListener txtChangedListener = new TxtChangeListener();
		SpinnerListener   spinnerListener    = new SpinnerListener();
		ComboBoxListener  comboBoxListener   = new ComboBoxListener();

		addWindowListener(new WindowLstnr());

		Const.txtTitulok		.addKeyListener(keyPressLisetner);
		Const.txtTitulok		.getDocument().addDocumentListener(txtChangedListener);
		Const.txtSprava 		.addKeyListener(keyPressLisetner);
		Const.txtSprava 		.getDocument().addDocumentListener(txtChangedListener);
		Const.sprCasOpak		.addKeyListener(keyPressLisetner);
		
		Const.chkJednorazovo	.addActionListener(chkBoxnListener);
		Const.chkMenitFarbu 	.addActionListener(chkBoxnListener);

		Const.cboJazyk    		.addItemListener  (comboBoxListener);
		Const.cmdPocitac    	.addActionListener(cmdButtonListener);
		Const.cmdUlozit     	.addActionListener(cmdButtonListener);
		Const.cmdSpustit   		.addActionListener(cmdButtonListener);
		Const.cmdMinimalizuj	.addActionListener(cmdButtonListener);
		Const.sprCasOpak    	.addChangeListener(spinnerListener);
		Const.cboCasJednotka 	.addItemListener  (comboBoxListener);

		miObnovit				.addActionListener(podnosListener);
		miSkoncit				.addActionListener(podnosListener);
		Spolocne.trayIcon		.addActionListener(podnosListener);
	}

	public static void main(String[] args) {
		Preferences prefs = Preferences.userNodeForPackage(PripomenUI.class);
		int pocetSpusteni = prefs.getInt(Const.POCET_SPUSTENI, 0);

		GregorianCalendar kalendar = new GregorianCalendar();
		kalendar.setTime(new Date());
		int dnesnyDen = kalendar.get(Calendar.DAY_OF_YEAR);

		/*
		 * Resetovania poèítadla spustenıch exemplárov programu pri prvom spustení v dni
		 */
		int denPoslSpust = prefs.getInt(Const.DEN_POSL_SPUST, 0);
		if (denPoslSpust != dnesnyDen) {
			pocetSpusteni = 0;
			prefs.putInt(Const.DEN_POSL_SPUST, dnesnyDen);
		}
/*
		if (pocetSpusteni != 0)
			if (JOptionPane.showConfirmDialog(null, 
							String.format(Const.OTAZKA, pocetSpusteni), 
							Const.TITULOK_OTAZKY,
							JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
*/		
		prefs.putInt(Const.POCET_SPUSTENI, ++pocetSpusteni);
/*
		try {
		    // Set System L&F
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }		
*/	    
		new PripomenUI();
	}
}
