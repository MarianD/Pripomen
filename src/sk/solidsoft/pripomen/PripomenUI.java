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
 * 29.3.2009:  Znemo�nenie kontextovej ponuky ikony na podnose (aj d�sledku zaklopania na �u) po�as zobrazenie
 *             pripomienkovacieho okna.
 *            
 *  1.4.2009:  Lokaliz�cia (zatia� vyu�it� len na zadanie mena programu, predvolen�ho titulku
 *             pripomienkovacieho okna a predvolen�ho textu v �om - v s�bore Loc.properties)
 *            
 *  8.4.2009:  Pre syst�mov� oblas� doroben� mal� ikona (16 x 16)
 *  
 *  2.8.2009:  Po��ta po�et spusten� a pri �tarte sa p�ta, �i m� program znovu spusti�.
 *             Pripom�nacie okienko sa ned� zavrie� klopnut�m na zatv�racie tla�idlo (to sp�sobovalo chybu,
 *             lebo okienko sa len skrylo, a neodohmlila sa kontextov� ponuka na syst�movom podnose).
 *  
 *  3.8.2009:  Cyklick� striedanie farieb p�sma v pripomienkovacom okne (pri ka�dom novom pripomenut� in� farba p�sma).
 *  		   Mierny refaktoring.
 *  
 *  4.8.2009:  V hlavnom okne (�vodnom nastavovacom okienku) som tle�idlo Nastavi� nastavil ako �tandardn�, tak�e som
 *             mohol zru�i� obsluhu kl�vesa Enter - zisk je ten, �e Look and Feels m��u to tla�idlo zv�razni�.
 *  
 * 30.8.2009:  Uklad� si permanentne de� v roku posledn�ho spustenia, a ke� pri spusten� nes�hlas� s aktu�lnym d�om,
 *             resetuje ulo�en� po�et spusten� - to pre pr�pad, �e sa predt�m na konci d�a program neukon�il korektne.
 *             V ToolTipe zobrazuje aj text spr�vy - ak je pridlh�, zobraz� len za�iatok (cca 2/3 povolenej d�ky),
 *             bodky a koniec (cca 1/3 povolenej d�ky).
 *             V pripomienkovacom okne sa po�n�c od 1. pripomenutia (teda NIE ihne� po nastaven�) zobrazuje poradov�
 *             ��slo pripomenutia.
 *  
 * 31.8.2009:  Do lokaliz�cie (s�bor Loc.properties) pridan� zadanie textu na potvrdzuj�com tla�idle
 *             okna pripomienky.
 *  
 *  2.9.2009:  Do hlavn�ho okna programu pridan� tla�idlo "Minimalizova�", ktor� umo�n� minimalizova� okno (na podnos)
 *             rovnako ako minimaliza�n� tla�idlo v z�hlav� okna. Toto tla�idlo sa v�ak zjav� a� po spusten� pripo-
 *             mienkovania, t.j. po obnove okna z ikony na podnose, a po spusten� pripomienkovania sa st�va �tandardn�m
 *             tla�idlom okna.
 *  
 *  3.9.2009:  Drobn� refaktoring
 *  
 *  6.9.2009:  V��� refaktoring.
 *             Pridlh� text spr�vy je na podnose skr�ten� tak, �e kon�� cel�m posledn�m slovom.
 *  
 *  8.9.2009:  �al�� refaktoring.
 *             Kone�ne namiesto java.util.Timer, java.util.TimerTask a java.util.Timer.schedule pou�it� javax.swing.Timer,
 *             java.awt.event.ActionListener a javax.swing.Timer.start().
 *             Ako pr�jemn� ved�aj�� produkt sa po uzamknut� po��ta�a na dlh�� �as zjav� len jedno zme�kan�pripomienkovacie
 *             okno, a nie hne� 2 za sebou.
 *             Vysk��an� aj mo�nos� po�as behu �asova�a zmeni� nastavenia (titulok okna, text spr�vy aj interval opakovania)
 *             - ale vr�til som to sp�, tak�e zatia� je to e�te neimplementovan�.
 *  
 * 13.9.2009:  Pridan� za�krt�vacie pol��ko pre vo�bu, �i sa m� meni� farba textu v pripomienkovacom okne.
 * 			   Mo�nos� zvoli� �as pripomienkovania v hodin�ch, min�tach alebo v sekund�ch.
 * 			   Predvolen� nastavenie pripomienkovania zmenen� zo "60 min�t" na "1 hodinu".
 * 			   V tooltipe ikony na syst�movom podnose opraven� sklo�ovanie pri 1 min�te (hodine, sekunde).
 * 			   Mo�nos� dynamicky (po spusten� �asova�a) meni� titulok pripomienkovacieho okna, text spr�vy v �om, ako aj to,
 * 			   �i sa maj� meni� farby pripomienkovac�ch spr�v.
 * 			   Vylep�en� dizajn �vodn�ho (nastavovacieho) okna.           		
 *  
 * 14.9.2009:  E�te viac vylep�en� a skr�snen� dizajn �vodn�ho (nastavovacieho) okna.
 * 			   Pri pokuse o skon�enie programu vyp�e v dial�govom okne aj (pr�padne skr�ten�) text pripomienky.
 * 			   Mo�nos� vynulova� po��tadlo spusten�ch programov tajnou kombin�ciou Ctrl+Home v textovom poli hlavn�ho okna
 * 			   programu.
 * 			   �al�� refaktoring.
 *  
 * 29.9.2009:  Mo�nos� jednorazov�ho pripomenutia za�krt�vac�m pol��kom v hlavnom okne.
 * 			   Pri jeho za�krtnut� / od�krtnut� sa dynamicky men�
 * 			     - ikona okna, aby tomu zodpovedala,
 * 			  	 - menovka �asov�ho nastavenia, aby tomu zodpovedala.
 * 			   Pri jednorazovom pripomenut�:
 * 				 - in� dodatkov� text v pripomienkovacom okne
 * 				 - in� text v jeho ozname o ��sle pripomienky
 * 				 - in� text na jeho potvrdzovacom tla�idle
 * 				 - in� ikona na podnose
 * 				 - in� ikona hlavn�ho okna,
 * 				 - in� text pri �asovom ozna�en� v tooltipe ikony na podnose.
 * 			   Zmenil som p�smo v hlavnom aj v pripomienkovacom okne na Arial (namiesto p�vodn�ho - logick�ho - mena Dialog),
 * 			   preto�e v nemeck�ch Windows boli niektor� znaky s diakritikou nespr�vne.
 * 			   Opraven� chyba pri nulovan� po��tadla spusten�ch programov.
 * 
 * 30.9.2009:  Mierny refaktoring.
 * 			   Jednorazovos�/opakovanos� pripomienkovania dynamicky signalizovan� aj farbou pozadia textov�ho po�a pre text
 *			   spr�vy v hlavnom okne.
 *			   Menenie farieb je dynamicky signalizovan� farebn�m pr��kom v hlavnom okne.
 *			   Menenie fariem je signalizovan� aj farebn�m pr��kom v pripomienkovacom okne.
 *			   Zmenil som p�smo v kontextovej ponuke ikony na podnose na Arial.
 * 
 * 01.10.2009: Vypnut� menenie farieb je signalizovan� jednofarebn�m pr��kom v hlavnom aj pripomienkovacom okne.
 * 
 * 24.11.2009: Zru�il som zmeny p�sma z 29. a 30.9.2009 a nastavil som �tandardn� lokaliz�ciu na sloven�inu a Slovensko,
 * 			   aby aj v nemeck�ch Windows sa spr�vne na��tali zdroje a aj zobrazovali znaky s diakritikou. Funguje to!
 * 
 * 08.12.2009: V titulkovom pruhu hlavn�ho okna sa zobrazuje tie� verzia JRE (Java Runtime Environment)
 * 
 * 23.12.2009: Namiesto zlo�it�ho zis�ovania rozmerov obrazovky a okna a vypo��tavania poz�cie pre umiestnenie hlavn�ho
 * 			   okna a pripomienkovacieho okna do stredu obrazovky pou�it� met�da setLocationRelativeTo(null).
 * 			   Pridan� obr�zkov� tla�idlo s po��ta�om, ktor� zobraz� verziu JRE (Java Runtime Environment) a s��asne
 * 			   zru�enie tohto oznamu v titulkovom pruhu (vi� predch�dzaj�ca verzia). Okrem toho zobraz� aj mnoh� �al�ie
 * 			   syst�mov� inform�cie.
 * 
 * 04.01.2010: Len refaktoring - v�aka tomu, �e kon�tantn� re�azce a re�azcov� liter�ly s� v�dy internovan� (vi�
 * 			   String.intern()), som nahradil v�etky vhodn� re�azcov� ".equals" efekt�vnej�ou oper�ciou "=="
 * 			   (i�lo len o triedu ObsluhyUdalosti).
 * 
 * 07.01.2010: Za�iatok refaktoringu - zn�enie �rovne pr�stupu niektor�ch pol� (na "private"). Treba to dokon�i�.
 * 
 * 09.01.2010: Len refaktoring:
 * 			   Z modulu "ObsluhyUdalosti.java" odstr�nen� pr�zdna trieda ObsluhyUdalosti. (Modul nemus� ma� ani jednu
 *             verejn� triedu.) V�etky triedy okrem PripomenUI zbaven� pr�vlastku "public". V triede Staticke zmaren�
 *             jej dedenie atrub�tom "final" a zmaren� vytv�ranie exempl�rov z nej definovan�m (nikdy nepou�it�ho)
 * 
 * 10.01.2010: Len refaktoring:
 *             V triede MenuItemMoje uroben� v�etky �leny ako "final", aby trieda poskytovala len nezmenite�n� exempl�re.
 * 
 * 12.01.2010: Len refaktoring:
 * 			   V�etky svoje triedy som dal ako "final"
 * 
 * 13.01.2010: Len refaktoring:
 * 			   V�etky kon�tanty v triede PripomenUI, ktor� neboli s�kromn� (lebo sa pou��vali vo viacer�ch triedach, alebo
 * 			   preto�e sa s�ce pou�ili len v jedinej triede, ale inej ne� PripomenUI - z d�vodu ich udr�ania pohromade boli
 * 			   v hlavnej triede PripomenUI), som dal do novej triedy Const, z ktorej sa nedaj� robi� exempl�re.
 *			   Triedy, ktor� ich pou��vaj�, som upravil.
 *			   Dokon�en� zmena pr�stupu pol� na "private" (za�at� 7.1.2001)
 * 
 * 17.01.2010: Len refaktoring:
 * 			   V tiede PripomenUI v�etky premenn� pre obsluhy udalost� (listenery) zmenen� zo statick�ch pol� triedy na
 *             lok�lne premenn� met�dy pridajObsluhy().
 * 
 * 20.01.2010: Len refaktoring:
 * 			   Z triedy Const som v�etky polia, ktor� neboli skuto�n�mi kon�tantami, presunul do novovytvorenej triedy
 * 			   Spolocne
 * 
 * 27.02.2010: Do zobrazovania syst�mov�ch inform�ci� som pridal aj predvolen� k�dovanie s�borov Java Virtual Machine.
 * 
 * 28.02.2010: Preto�e pri na��tan� zo s�boru sa pou��va predvolen� k�dovanie JVM, ned� sa spolieha� na to, �e p�smen� ako
 * 			   "�" a "�" sa na��taj� spr�vne - preto som v s�bore vlastnost� Loc.properties nap�sal tieto znaky ako
 * 				\u010d a \u0165. Pre istotu som aj v s�bore Const.java texty na tla�idl�ch zap�sal podobne.
 * 
 * 02.03.2010: Zmena v skuto�nosti vykonan� v triede sk.solidsoft.internationalization.MyControl - pri vytv�ran�
 * 			   InputStreamReadera som pridal �al�� parameter - Charset - ako "cp1250", aby virtu�lny stroj s in�m
 *             predvolen�m k�dovan�m pracoval spr�vne s re�azcami v Loc.properties, pripraven�mi na mojom po��ta�i,
 *             bez potreby k�dova� znaky s diakritikou - vi� predch�dzaj�cu verziu.
 * 
 * 09.03.2010: Vzh�adom k predch�dzaj�cim zmen�m je zbyto�n� nastavova� Locale na sk_SK. Odstr�nil som to.
 * 
 * 14.03.2010: V module "Staticke.java" upraven� funkcia pripadneSkrat(), ktor� skracuje text spr�vy pre dial�gov�
 *             okno s ot�zkou na skon�enie programu - p�vodn�mu re�azcu sa najprv osekaj� �vodn� a z�vere�n� medzery
 * 			   (hlavne z�vere�n� sp�sobovali probl�m, lebo posledn� slovo sa h�adalo od poslednej medzery).
 *            
 * 22.06.2011: Pr�prava na lokaliz�cie do r�znych jazykov - v s�bore Loc.properties zmenen� n�zvy premenn�ch na anglick�
 * 
 * 02.07.2011: Do hlavn�ho okna pridan� text licencie ("Program m��ete pou��va� aj ��ri� zadarmo.")
 * 
 * 03.07.2011: Za�at� lokaliz�cia - v s�bore Const.java
 * 
 * 13.07.2011: Pokra�ovanie lokaliz�cie v s�bore Const.java
 * 
 * 04.08.2011: Pokra�ovanie lokaliz�cie v s�bore Const.java a ve�k� refaktoring:
 * 			   V s�bore Const.java ponechan� len skuto�n�, nikdy sa nemeniace kon�tanty - ostatn�, ur�en� pre lokaliz�ciu,
 * 			   prenesen� do novovytvoren�ho s�boru Lokalizacia.java (s jedninou triedou Lokalizacia v nej).
 * 			   Aj zo s�boru PripomenUI.java prenesen� v�etko, t�kaj�ce sa lokaliz�cie, do triedy Localizacia. V triede
 * 			   Lokalizacia vytvoren� met�dy setLocale() a pomocn� - s�kromn� - met�dy gatString() a nastavRetazce().
 *             Do inform�ci� o po��ta�i som pridal (viac-menej pre seba pre testovanie) aj nastaven� jazyk a krajinu
 * 
 * 05.08.2011: Pridan� ikona pre ulo�enie nastavenej pripomienky - uklad� text titulku a spr�vy do preferenci�
 *             (vo Windows je to HKEY_CURRENT_USER\\Software\JavaSoft\Prefs).
 * 
 * 06.08.2011: Pokra�ovanie v lokaliz�cii, prenesen� skuto�n� kon�tanty z triedy PripomenUI do triedy Const, mierny
 * 			   refaktoring
 * 
 * 07.08.2011: Pokra�ovanie v lokaliz�cii a prenesen� �al�ie skuto�n� kon�tanty z triedy PripomenUI do triedy Const,
 * 			   mierny refaktring
 * 
 * 08.08.2011: Mierny refaktoring
 * 
 * 09.08.2011: Pokra�ovanie v lokaliz�cii
 * 
 * 13.08.2011: Nov� trieda Pripomienka - �dajov� model pre okno pripomienky, v�etky �daje na jednom mieste
 * 
 * 14.08.2011: Ukladanie zadan�ch �dajov met�dou ulozMa() triedy Pripomienka
 * 
 * 15.08.2011: Obnovenie zadan�ch �dajov met�dou obnovMaZdisku() triedy Pripomienka
 * 			   Program obnovuje u� aj ulo�en� menenie farieb, jednorazovos�, nastaven� �as a �asov� jednotku, teda u� V�ETKO
 * 			   Mo�nos� meni� jednorazovos�, �as a �asov� jednotku aj po spusten� pripomienkovania, teda u� V�ETKO
 * 			   �asova� prenesen� do triedy Spolocne
 * 			   �iaden ovl�dac� prvok v triede Spolocne u� nie je inicializovan� (okrem �asova�a, ktor� mus� by�) 
 * 			   - v�etky sa inicializuj� v triede PripomenUI, v met�de nastavVlastnostiPrvkov().
 * 			   �prava k�du, aby si na��taval �daje v�lu�ne z exepl�ra triedy Pripomienka.
 * 			   Pri dodato�nej zmene �asu alebo �asovej jednotky zobraz� o tom okam�ite spr�vu, pri�om preru�� ��slovanie
 * 			   aj zmenu farby.
 * 
 * 16.08.2011: Niektor� na tvrdo p�san� texty prenesen� do triedy Const (potom p�jdu do triedy Lokalizacia)
 * 
 * 17.08.2011: Pridan� funguj�ce tla�idlo na zobrazenie / nastavenie jazyka
 * 			   Refaktoring - odstr�nen� zbyto�n� ActionCommand tla�idiel, ostatn� nastaven� nez�visle od ich n�zvu (lokality)
 * 
 * 18.08.2011: Refaktoring - zmena nevhodn�ch mien na vhodnej�ie, zavedeh� kon�tanty pre �asov� jednotku a �al�ie kon�tanty
 * 			   Opraven� chyba - pri dodato�nej zmene jednorazovosti sa text v ToolTipe ikony pr�slu�ne nezmenil.
 * 			   Obrovsk� lokaliz�cia programu.
 * 
 * 19.08.2011: Pokra�ovanie v lokaliz�cii
 * 
 * 20.08.2011: Pokra�ovanie v lokaliz�cii
 * 
 * 21.08.2011: Pokra�ovanie v lokaliz�cii - okrem "tajnej" kl�vesovej kombin�cie Ctrl + Home dokon�en�.
 * 			   Odstr�nen� (mo�no do�asne) upozor�ovanie na to, �e program je u� spusten� - aj kv�li probl�mom s lokaliz�ciou.
 * 
 * 22.08.2011: Opraven� chyba - pri zmene jazyka sa neuchov�vala nastaven� �asov� jednotka
 * 			   Opraven� chyba - nelokalizoval sa ToolTip ikony po��ta�a
 * 			   Lokaliz�cia pre ToolTip ikony ulo�enia
 * 			   Ve�k� refaktoring - skoro v�etko z triedy Spolocne prenesen� do triedy Const
 * 
 * 23.08.2011: Opraven� chyba - pri zmene jazyka sa neuchov�vali pou�vate�om zmenen� texty. Presunul som ich lokalizovan�
 * 			   nastavovanie (v triede PripomenUI) z met�dy nastavLokalizovan�Texty() (ktor� sa vol� opakovane pri
 * 			   ka�dej zmene jazyka) do met�dy nastavVlastnostiPrvkov (ktor� sa vol� len raz). T�m som 
 * 
 * 25.08.2011: V tejto triede v met�de main() pripraven� (opozn�mkovan�) pou�itie in�ho Look and Feel. V triede DlgPripomen()
 * 			   v s�vislosti s t�m v met�de NastavVlastnosti nastaven� natvrdo p�smo a biele pozadie.
 * 
 * 19.10.2013: Lokaliza�n� s�bory: Namiesto uvedenia koncovej medzery ("\ ") v k���ov�ch slov�ch TimeToAlarm 
 *			   a PeriodOfReminder t�to pridan� programovo (v triede Lokalizacia)
 * 			   Prechod z k�dovania cp1250 na utf8 pre lokaliza�n� s�bory (�prava uroben� v triede
 * 			   sk.solidsoft.internationalization.MyControl)
 * 
 * 11.11.2013: Do hlavn�ho okna pridan� meno prekladate�a do pr�slu�n�ho jazyka 
 * 14.11.2013: Do hlavn�ho okna pridan� tooltip tla�idlu pre v�ber jazyka 
 * 26.02.2014: Pridan� n�rska lokaliz�cia (autor: Michael Sagnes, kvikk92@gmail.com) 
 * 30.03.2014: Tla�idlo pre zmenu jazyka nahraden� po�om so zoznamom 
 */

public final class PripomenUI extends JFrame {
	private static final long 	 serialVersionUID  = 1L;
	
	private static  	 Locale		origLokalita   = Locale.getDefault();		// Kv�li testovaniu bude e�te raz priraden�

	private static final JLabel 	lblTitulok     = new JLabel();
	private static final JLabel 	lblSprava      = new JLabel();
	private static final JLabel 	lblMenitFarby  = new JLabel();
	private static final JLabel 	lblCopyright   = new JLabel(Const.COPYRIGHT);
	private static final JLabel 	lblLicence     = new JLabel();
	private static final JLabel 	lblTranslator  = new JLabel();
	private static final SystemTray podnos    	   = SystemTray.getSystemTray();
	private static final PopupMenu 	popup          = new PopupMenu();
	
	private static final Toolkit 	defaultToolkit = Toolkit.getDefaultToolkit();	// Pre pr�stup k zdrojom v s�bore .jar

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
		
		origLokalita = Locale.getDefault();					// E�te predt�m, ne� ju pou��vate� zmen�
		Lokalizacia.setLocale(origLokalita);				// Origin�lne nastavenie jazyka
		
		nastavVlastnostiPrvkov();							// Nelokalizovan� vlastnosti ovl�dac�ch prvkov
		nastavLoklizovaneTexty();							// Lokalizovan� vlastnosti ovl�dac�ch prvkov
		rozmiestniPrvky();									// Rozmiestnenie ovl�dac�ch prvkov
		pridajObsluhy();									// Obsluhy udalost� ovl�dac�ch prvkov
		pack();												// Nastavenie na minim�lnu ve�kos� okna
		
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
		setExtendedState(JFrame.NORMAL); // Kv�li neskor�ej obnove
		setAlwaysOnTop(true); 			 // Aby sa po zaklopan� na ikonu na podnose dostalo navrch; ihne� potom sa toto vypne

		if (jednorazovo)
			ikonaTray = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_TRAY_1));
		else
			ikonaTray = defaultToolkit.getImage(
					PripomenUI.class.getResource(Const.IKONA_TRAY));

		Spolocne.trayIcon.setImage(ikonaTray);

		if (podnos.getTrayIcons().length == 0) { // Ikona na podnose e�te nie je
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
		 * Takto zlo�ito nastavujem pole so zoznamom pre pr�pad zmeny jazyka, aby sa zachovala vybran� �asov� jednotka
		 */
		int index = Const.cboCasJednotka.getSelectedIndex();
		if (index == -1) {											// �asov� jednotka e�te nenastaven�
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
		grpLay.setHonorsVisibility(false); 			// Aby mali aj nevidite�n� komponenty rezervovan� miesto
		cp    .setLayout(grpLay);

		grpLay.setAutoCreateGaps(true);
		grpLay.setAutoCreateContainerGaps(true);

		// Skupina pre nastavenie textu titulku a po��ta�a
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

		// Skupina pre nastavenie �asu
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

		// Hlavn� st�pce pre rozmiestnenie ovl�dac�ch prvkov (a ich skup�n)
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

		// Hlavn� riadky pre rozmiestnenie ovl�dac�ch prvkov (a ich skup�n)
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
		 * Resetovania po��tadla spusten�ch exempl�rov programu pri prvom spusten� v dni
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
