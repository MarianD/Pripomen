/**
 * 
 */
package sk.solidsoft.pripomen;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.Font;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

//import com.sun.awt.AWTUtilities;


/**
 * @author Marian
 *
 */
final class DlgPripomen extends JDialog {
	private static final long serialVersionUID =   1L;
	
	private static final int	SIRKA          = 510;
	private static final int	VYSKA          = 240;

	private static final int	LP_OKRAJ	   =  10;				// Vn˙tornÈ okraje pre text v poli oznamu (æav˝ a prav˝)
	private static final int	HD_OKRAJ	   =   5;				// Vn˙tornÈ okraje pre text v poli oznamu (horn˝ a doln˝)
	
	static final JTextArea 		txtOznam 	   = new JTextArea();
	static final JLabel			lblMenitFarby  = new JLabel();		// Aj keÔ ide prakticky o to istÈ, ako v PripomenUI, musÌ
	static final JLabel			lblCisloPripom = new JLabel("");	//  to byù IN› objekt, pretoûe sa NARAZ vyskytuje dvakr·t

	static final JButton 		cmdOK    	   = new JButton("");
	
	private static final float	VELKOST_PISMA  = 20;

	
	public DlgPripomen() {
		super();
		nastavVlastnosti();
		rozmiestniPrvky();
		pridajObsluhy();
	}

	private void nastavVlastnosti() {
		Font pismo = new Font("Arial", Font.PLAIN, (int) VELKOST_PISMA);		// KvÙli rÙznym druhom Look and Feel
		
		setModal				 (true);
		setSize					 (new Dimension(SIRKA, VYSKA));
		setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		
		// Okno do stredu obrazovky
		setLocationRelativeTo	 (null);
		setResizable		 	 (false);
		setAlwaysOnTop		 	 (true);
		
//		txtOznam.setFont	     (txtOznam.getFont().deriveFont(VELKOST_PISMA));
		txtOznam.setFont	     (pismo);
		txtOznam.setForeground	 (Const.FARBA_PISMA[0]);
		txtOznam.setBackground	 (new Color(255, 255, 255));					// KvÙli rÙznym druhom Look and Feel
		txtOznam.setEditable     (false);
		txtOznam.setMargin	     (new Insets(HD_OKRAJ, LP_OKRAJ, HD_OKRAJ, LP_OKRAJ));
		txtOznam.setLineWrap  	 (true);
		txtOznam.setWrapStyleWord(true);
		
		cmdOK.setText(Lokalizacia.textPotvrdenia);
	}
	
	private void rozmiestniPrvky() {
		Container   cp     = getContentPane();
		GroupLayout grpLay = new GroupLayout(cp);
		cp.setLayout(grpLay);
		
		grpLay.setAutoCreateGaps		 (true);
		grpLay.setAutoCreateContainerGaps(true);
		
		// HlavnÈ stÂpce pre rozmiestnenie ovl·dacÌch prvkov (a ich skupÌn)
		ParallelGroup stlpec1 = grpLay.createParallelGroup(GroupLayout.Alignment.CENTER);
		
		stlpec1.addComponent(txtOznam)
			   .addComponent(lblMenitFarby)
			   .addComponent(lblCisloPripom)
			   .addComponent(cmdOK);
		
		SequentialGroup vodorovnaSkupina = grpLay.createSequentialGroup();
		vodorovnaSkupina.addGroup(stlpec1);
		grpLay.setHorizontalGroup(vodorovnaSkupina);
		

		// HlavnÈ riadky pre rozmiestnenie ovl·dacÌch prvkov (a ich skupÌn)
		SequentialGroup riadok1 = grpLay.createSequentialGroup();
		
		riadok1.addComponent(txtOznam)
			   .addComponent(lblMenitFarby)
			   .addComponent(lblCisloPripom)
			   .addComponent(cmdOK);
		
		SequentialGroup zvislaSkupina = grpLay.createSequentialGroup();

		zvislaSkupina.addGroup(riadok1);
		
		grpLay.setVerticalGroup(zvislaSkupina);
	}

	private void pridajObsluhy() {
		cmdOK.addActionListener(new CmdOkListener());
	}
	
	/**
	 * @param text Text v pripomienkovacom okne
	 */
	public void setText(String text) {
		txtOznam.setText(text);
	}

	/**
	 * @param titulok Titulok pripomienkovacieho okna
	 */
	public void setTitulok(String titulok) {
		setTitle(titulok);
	}
}
