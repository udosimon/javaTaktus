/*
 * TAKTUS
 *
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 05.10.2007 
 *
 * Beschreibung:
 * 
 * Der im Hauptframe auf Reiter 1 (Titel) dargestellte Panel
 */
package AChord.MainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import AChord.AClasses.APanel;
import AChord.AClasses.APanelTonArt;
import AChord.AClasses.APanelTonArtInterface;
import AChord.Hilfe.Konfiguration;
import AChord.Schema.Song;
import AChord.Schema.TextSchema;


public class PanelReiter1Titel extends APanel implements APanelTonArtInterface
{
    private final static long serialVersionUID = 1;

    private static final int yA = 28;  //y-Abstand zwischen zwei RadioButtons (min 16)
	private static final int yD = 23;   //y-Abstand erster RadioButtons von oben

	private JTextField jTextFieldAutor;
	private JLabel jLabelAutor;
	private APanel jPanelSchema;
	private APanel jPanelTonart;
	private APanelTonArt panelTonArt;

	private JRadioButton jRadioButtonAnzTakte8;
	private JLabel jLabelTextMit1SeiteZeile2;
	private JTextField jTextFieldTitelName;
	private JLabel jLabelTitelHinweis;
	private JLabel jLabelTitelName;
	private JRadioButton jRadioButtonSchemaUnsichtbar;
	private JRadioButton jRadioButtonSchema7;
	private JRadioButton jRadioButtonSchema6;
	private JRadioButton jRadioButtonSchema5;
	private JRadioButton jRadioButtonSchema4;
	private JRadioButton jRadioButtonSchema3;
	private JRadioButton jRadioButtonSchema2;
	private JRadioButton jRadioButtonSchema1;
	private JRadioButton jRadioButtonAnzTakte6;
	private JRadioButton jRadioButtonAnzTakte4;
	private APanel jPanelAnzahlTakte;
	
	Song song;
	private JRadioButton jRadioButtonTextMit2Seiten;
	private JRadioButton jRadioButtonTextMit1Seite;
	private JRadioButton jRadioButtonTextOhne;
	private APanel aPanelTextAnzeige;
	private JCheckBox jCheckBoxTonartAnzeige;

	boolean istLadePhase = false;
	boolean istDirektNachNeuLaden = true;

	AChordFrame parent;
	
	private JButton jRadioButtonHinweisNeuerTitel;
	
	/**
	 * Konstruktor
	 * @param parent
	 */
	public PanelReiter1Titel(AChordFrame parent)
	{
		super();
		this.parent = parent;
		construct();
	}
	
	/**
	 * Initialisierungen des Frames
	 */
	private void construct()
	{
		try
		{
			istLadePhase = true;

			initForm();
			myInit();

			istLadePhase = false;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			istLadePhase = false;
		}
	}
	
	/**
	 * @throws Exception
	 * 
	 * Aufbau der Oberfläche
	 */
	private void initForm() throws Exception
	{
		this.setPreferredSize(new java.awt.Dimension(452, 682));
		this.setLayout(null);
		setFont(getFontA());
		add(getJLabelTitelName());
		add(getJTextFieldTitelName());
		add(getJLabelTitelHinweis());
		add(getJPanelTonart());
		add(getJPanelSchema());
		add(getJLabelAutor());
		add(getJTextFieldAutor());
		add(getJPanelAnzahlTakte());
		add(getJButtonHinweisNeuerTitel());
		this.add(getJPanelTextAnzeige());
	}
	
	/**
	 * Button, der bei neuen Songs hinter dem Hinweistext unterhalb des
	 * Titels angezeigt wird;
	 * Anzeige eines Textes zu den verbotenen Zeichen im Dateinamen
	 * @return
	 */
	public JButton getJButtonHinweisNeuerTitel()
	{
		if (jRadioButtonHinweisNeuerTitel == null)
		{
			jRadioButtonHinweisNeuerTitel = new JButton();
			jRadioButtonHinweisNeuerTitel.setText("!");
			jRadioButtonHinweisNeuerTitel.setFont(new Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD,14));
			jRadioButtonHinweisNeuerTitel.setForeground(Color.BLUE.darker());
			jRadioButtonHinweisNeuerTitel.setMargin(new Insets(2,2,2,2));
			jRadioButtonHinweisNeuerTitel.setBounds(440, 60, 20, 20);
			jRadioButtonHinweisNeuerTitel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonHinweisText();}});
		}
		return jRadioButtonHinweisNeuerTitel;
	}

	/**
	 * @return
	 */
	private JLabel getJLabelTitelName() {
		if (jLabelTitelName == null) {
			jLabelTitelName = new JLabel();
			jLabelTitelName.setText("Titel-Name");
			jLabelTitelName.setBounds(22, 40, 69, 15);
			jLabelTitelName.setFont(getFontA());
		}
		return jLabelTitelName;
	}

	/**
	 * @return
	 */
	private JTextField getJTextFieldTitelName() {
		if (jTextFieldTitelName == null) {
			jTextFieldTitelName = new JTextField();
			jTextFieldTitelName.setBounds(114, 37, 295, 21);
			jTextFieldTitelName.setFont(getFontA());
			PlainDocument docTitel = new PlainDocument();
			jTextFieldTitelName.setDocument(docTitel);
			docTitel.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleDocTitelName(); }
				public void removeUpdate(DocumentEvent e) { handleDocTitelName(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		return jTextFieldTitelName;
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelTitelHinweis()
	{
		if (jLabelTitelHinweis == null)
		{
			jLabelTitelHinweis = new JLabel();
			jLabelTitelHinweis.setBounds(110, 58, 350, 25);
			jLabelTitelHinweis.setFont(getFontA());
			jLabelTitelHinweis.setText("Der eingegebene Titel wird als Dateiname vorgeschlagen.");
			jLabelTitelHinweis.setVisible(true);
		}
		return jLabelTitelHinweis;
	}

	/**
	 * @return
	 */
	private JLabel getJLabelAutor() {
		if (jLabelAutor == null) {
			jLabelAutor = new JLabel();
			jLabelAutor.setText("Autor:");
			jLabelAutor.setFont(getFontA());
			jLabelAutor.setBounds(23, 88, 58, 15);
		}
		return jLabelAutor;
	}
	
	private JTextField getJTextFieldAutor() {
		if (jTextFieldAutor == null) {
			jTextFieldAutor = new JTextField();
			jTextFieldAutor.setFont(getFontA());
			jTextFieldAutor.setBounds(114, 85, 192, 21);
			PlainDocument docAutor = new PlainDocument();
			jTextFieldAutor.setDocument(docAutor);
			docAutor.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleDocAutor(); }
				public void removeUpdate(DocumentEvent e) { handleDocAutor(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		return jTextFieldAutor;
	}

	/**
	 * allgemeiner Rahmen für die Tonart
	 * @return
	 */
	private APanel getJPanelTonart() {
		if (jPanelTonart == null) {
			jPanelTonart = new APanel();
			jPanelTonart.setBounds(23, 390, 170, 200);
			jPanelTonart.setLayout(null);
			jPanelTonart.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Tonart"));

			jPanelTonart.add(getPanelTonArt());
			jPanelTonart.add(getJCheckTonartAnzeige());
		}
		return jPanelTonart;
	}
	
	/**
	 * Checkbox unterhalb der Tonartauswahl, welches angibt, ob die
	 * Tonart im Titel des Song mit angezeigt werden soll
	 * @return
	 */
	private JCheckBox getJCheckTonartAnzeige()
	{
		if (jCheckBoxTonartAnzeige == null)
		{
			jCheckBoxTonartAnzeige = new JCheckBox();
			jCheckBoxTonartAnzeige.setText("Anzeige im Titel");
			jCheckBoxTonartAnzeige.setFont(getFontA());
			jCheckBoxTonartAnzeige.setBounds(20, 160, 120, 20);
			jCheckBoxTonartAnzeige.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxTonartAnzeige();}});
		}
		return jCheckBoxTonartAnzeige;
	}

	/**
	 * Taktus-implementiertes Panel, welches alle möglichen Tonarten enthält
	 * (APanelTonArt)
	 * @return
	 */
	private APanelTonArt getPanelTonArt()
	{
		if (panelTonArt == null)
		{
			panelTonArt = new APanelTonArt(this);
			panelTonArt.setBounds(10, 20, 150, 140);
			panelTonArt.setLayout(null);
			panelTonArt.setInVisisbleKlang();
			panelTonArt.setButtonsEnabled(false);
		}
		return panelTonArt;
	}


	/**
	 * @return
	 */
	private APanel getJPanelSchema() {
		if (jPanelSchema == null) {
			jPanelSchema = new APanel();
			jPanelSchema.setBounds(221, 171, 240, 371);
			jPanelSchema.setLayout(null);
			jPanelSchema.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Schema"));
			jPanelSchema.add(getJRadioButtonSchema1());
			jPanelSchema.add(getJRadioButtonSchema2());
			jPanelSchema.add(getJRadioButtonSchema3());
			jPanelSchema.add(getJRadioButtonSchema4());
			jPanelSchema.add(getJRadioButtonSchema5());
			jPanelSchema.add(getJRadioButtonSchema6());
			jPanelSchema.add(getJRadioButtonSchema7());
			jPanelSchema.add(getJRadioButtonSchemaUnsichtbar());
		}
		return jPanelSchema;
	}
	
	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema1()
	{
		if (jRadioButtonSchema1 == null)
		{
			jRadioButtonSchema1 = new JRadioButton();
			jRadioButtonSchema1.setText("  A   A   B   C  (je 8 Takte)");
			jRadioButtonSchema1.setBounds(21, yD+(0*yA), 200, 21);
			jRadioButtonSchema1.setFont(getFontA());
			jRadioButtonSchema1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(1, evt);} });
		}
		return jRadioButtonSchema1;
	}

	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema2()
	{
		if (jRadioButtonSchema2 == null)
		{
			jRadioButtonSchema2 = new JRadioButton();
			jRadioButtonSchema2.setText(" ||: A :||   B   C  (je 8 Takte)");
			jRadioButtonSchema2.setFont(getFontA());
			jRadioButtonSchema2.setBounds(21, yD+(1*yA), 200, 24);
			jRadioButtonSchema2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(2, evt);}});
		}
		return jRadioButtonSchema2;
	}

	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema3()
	{
		if (jRadioButtonSchema3 == null)
		{
			jRadioButtonSchema3 = new JRadioButton();
			jRadioButtonSchema3.setText(" 2 Teile (je 8 Takte)");
			jRadioButtonSchema3.setFont(getFontA());
			jRadioButtonSchema3.setBounds(21, yD+(5*yA), 200, 24);
			jRadioButtonSchema3.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(3, evt);}});
		}
		return jRadioButtonSchema3;
	}
	
	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema4()
	{
		if (jRadioButtonSchema4 == null)
		{
			jRadioButtonSchema4 = new JRadioButton();
			jRadioButtonSchema4.setText(" 1 Teil (12 Takte)");
			jRadioButtonSchema4.setFont(getFontA());
			jRadioButtonSchema4.setBounds(21, yD+(2*yA), 200, 24);
			jRadioButtonSchema4.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(4, evt);}});
		}
		return jRadioButtonSchema4;
	}

	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema5()
	{
		if (jRadioButtonSchema5 == null)
		{
			jRadioButtonSchema5 = new JRadioButton();
			jRadioButtonSchema5.setText(" 1 Teil (20 Takte)");
			jRadioButtonSchema5.setFont(getFontA());
			jRadioButtonSchema5.setBounds(21, yD+(4*yA), 200, 24);
			jRadioButtonSchema5.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(5, evt);}});
		}
		return jRadioButtonSchema5;
	}

	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema6()
	{
		if (jRadioButtonSchema6 == null)
		{
			jRadioButtonSchema6 = new JRadioButton();
			jRadioButtonSchema6.setText(" 2 Teile (je 16 Takte)");
			jRadioButtonSchema6.setFont(getFontA());
			jRadioButtonSchema6.setBounds(21, yD+(6*yA), 200, 24);
			jRadioButtonSchema6.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(6, evt);}});
		}
		return jRadioButtonSchema6;
	}

	/**
	 * eins der vorgegebenen Schemata
	 * @return
	 */
	private JRadioButton getJRadioButtonSchema7()
	{
		if (jRadioButtonSchema7 == null)
		{
			jRadioButtonSchema7 = new JRadioButton();
			jRadioButtonSchema7.setText(" 1 Teil (16 Takte)");
			jRadioButtonSchema7.setFont(getFontA());
			jRadioButtonSchema7.setBounds(21, yD+(3*yA), 200, 24);
			jRadioButtonSchema7.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(7, evt);}});
		}
		return jRadioButtonSchema7;
	}

	/**
	 * Dieser unischtbare Dummy-RadioButton wird dann gewählt, wenn
	 * keine der anderen Schemata gewählt werden kann, weil im Reiter
	 * "Teile" von den Standard-Schemata abgewichen wird 
	 * @return
	 */
	public JRadioButton getJRadioButtonSchemaUnsichtbar()
	{
		if (jRadioButtonSchemaUnsichtbar == null)
		{
			jRadioButtonSchemaUnsichtbar = new JRadioButton();
			jRadioButtonSchemaUnsichtbar.setText("  Unishctbar");
			jRadioButtonSchemaUnsichtbar.setVisible(false);
			jRadioButtonSchemaUnsichtbar.setFont(getFontA());
			jRadioButtonSchemaUnsichtbar.setBounds(21, yD+(5*yA), 200, 24);
			jRadioButtonSchemaUnsichtbar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonSchema(-1, evt);}});
		}
		return jRadioButtonSchemaUnsichtbar;
	}

	
	/**
	 * Panel, in dem die Anzahl der Takte pro dargestellter Zeile
	 * gewählt werden kann; aktuell sind nur 4, 6 oder 8 möglich
	 * @return
	 */
	private APanel getJPanelAnzahlTakte()
	{
		if (jPanelAnzahlTakte == null)
		{
			jPanelAnzahlTakte = new APanel();
			jPanelAnzahlTakte.setBounds(23, 171, 124, 51);
			jPanelAnzahlTakte.setLayout(null);
			jPanelAnzahlTakte.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Takte pro Zeile"));
			jPanelAnzahlTakte.add(getJRadioButtonAnzTakte4());
			jPanelAnzahlTakte.add(getJRadioButtonAnzTakte6());
			jPanelAnzahlTakte.add(getJRadioButtonAnzTakte8());
		}	return jPanelAnzahlTakte;
	}
	
	/**
	 * @return
	 */
	private JRadioButton getJRadioButtonAnzTakte4()
	{
		if (jRadioButtonAnzTakte4 == null)
		{
			jRadioButtonAnzTakte4 = new JRadioButton();
			jRadioButtonAnzTakte4.setText("4");
			jRadioButtonAnzTakte4.setFont(getFontA());
			jRadioButtonAnzTakte4.setBounds(11, 20, 33, 22);
			jRadioButtonAnzTakte4.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					onRadioButtonAnzTakte(evt);
				}
			});
		}
		return jRadioButtonAnzTakte4;
	}
	

	/**
	 * @return
	 */
	private JRadioButton getJRadioButtonAnzTakte6()
	{
		if (jRadioButtonAnzTakte6 == null)
		{
			jRadioButtonAnzTakte6 = new JRadioButton();
			jRadioButtonAnzTakte6.setText("6");
			jRadioButtonAnzTakte6.setFont(getFontA());
			jRadioButtonAnzTakte6.setBounds(47, 20, 33, 22);
			jRadioButtonAnzTakte6.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					onRadioButtonAnzTakte(evt);
				}
			});
		}
		return jRadioButtonAnzTakte6;
	}

	/**
	 * @return
	 */
	private JRadioButton getJRadioButtonAnzTakte8()
	{
		if (jRadioButtonAnzTakte8 == null)
		{
			jRadioButtonAnzTakte8 = new JRadioButton();
			jRadioButtonAnzTakte8.setText("8");
			jRadioButtonAnzTakte8.setFont(getFontA());
			jRadioButtonAnzTakte8.setBounds(83, 20, 33, 22);
			jRadioButtonAnzTakte8.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					onRadioButtonAnzTakte(evt);
				}
			});
		}
		return jRadioButtonAnzTakte8;
	}
	
	
	/**
	 * Panel, in dem gewählt wird ob der Song einen Text enthält, und
	 * wenn ja, ob der Text auf der gleichen Seite wie die Akkorde oder
	 * auf einer zweiten Seite angezeigt werden soll.
	 * @return
	 */
	private APanel getJPanelTextAnzeige()
	{
		if (aPanelTextAnzeige == null)
		{
			aPanelTextAnzeige = new APanel();
			aPanelTextAnzeige.setBorder(new TitledBorder(new LineBorder(Color.BLACK),"Textanzeige" +	""));
			aPanelTextAnzeige.setBounds(23, 252, 160, 114);
			aPanelTextAnzeige.setLayout(null);
			aPanelTextAnzeige.add(getJRadioButtonTextOhne());
			aPanelTextAnzeige.add(getJRadioButtonTextMit1Seite());
			aPanelTextAnzeige.add(getJLabelTextMit1SeiteZeile2());
			aPanelTextAnzeige.add(getJRadioButtonTextMit2Seiten());
		}
		return aPanelTextAnzeige;
	}
	

	/**
	 * @return
	 */
	private JRadioButton getJRadioButtonTextOhne()
	{
		if (jRadioButtonTextOhne == null)
		{
			jRadioButtonTextOhne = new JRadioButton();
			jRadioButtonTextOhne.setText("ohne Text");
			jRadioButtonTextOhne.setFont(getFontA());
			jRadioButtonTextOhne.setBounds(11, 19, 131, 22);
			jRadioButtonTextOhne.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonText(0, evt);} });
}
		return jRadioButtonTextOhne;
	}
	

	/**
	 * @return
	 */
	private JRadioButton getJRadioButtonTextMit1Seite()
	{
		if (jRadioButtonTextMit1Seite == null)
		{
			jRadioButtonTextMit1Seite = new JRadioButton();
			jRadioButtonTextMit1Seite.setText("Akkorde und Text");
			jRadioButtonTextMit1Seite.setFont(getFontA());
			jRadioButtonTextMit1Seite.setBounds(11, 49, 145, 17);
			jRadioButtonTextMit1Seite.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonText(1, evt);} });
		}
		return jRadioButtonTextMit1Seite;
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelTextMit1SeiteZeile2() {
		if (jLabelTextMit1SeiteZeile2 == null) {
			jLabelTextMit1SeiteZeile2 = new JLabel();
			jLabelTextMit1SeiteZeile2.setText("auf einer Seite");
			jLabelTextMit1SeiteZeile2.setFont(getFontA());
			jLabelTextMit1SeiteZeile2.setBounds(30, 59, 145, 22);
		}
		return jLabelTextMit1SeiteZeile2;
	}

	/**
	 * @return
	 */
	private JRadioButton getJRadioButtonTextMit2Seiten()
	{
		if (jRadioButtonTextMit2Seiten == null)
		{
			jRadioButtonTextMit2Seiten = new JRadioButton();
			jRadioButtonTextMit2Seiten.setText("Text auf 2. Seite");
			jRadioButtonTextMit2Seiten.setFont(getFontA());
			jRadioButtonTextMit2Seiten.setBounds(11, 79, 131, 22);
			jRadioButtonTextMit2Seiten.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onRadioButtonText(2, evt);} });
		}
		return jRadioButtonTextMit2Seiten;
	}
	
	/**
	 * wird beim Click auf einer der Radiobuttons im Panel "Textanzeige" ausgelöst
	 * @param textAnzeige
	 * @param evt
	 */
	private void onRadioButtonText(final int textAnzeige, ActionEvent evt)
	{
		song.getText().setTextAnzeige(textAnzeige);
		song.reloadTextSchema();
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				// setzen der Textanzeige
				switch (textAnzeige)
				{
					case TextSchema.TEXT_ANZEIGE_OHNE:
						parent.getJTabbedPaneAnzeige().setEnabledAt(1, false);
						parent.getJTabbedPaneAnzeige().setSelectedIndex(0);
						break;
					case TextSchema.TEXT_ANZEIGE_EINESEITE:
						parent.getJTabbedPaneAnzeige().setEnabledAt(1, false);
						parent.getJTabbedPaneAnzeige().setSelectedIndex(0);
						break;
					case TextSchema.TEXT_ANZEIGE_ZWEISEITEN:
						parent.getJTabbedPaneAnzeige().setEnabledAt(1, true);
						break;
	
					default:
						break;
				}
			}
		});

	}


	/**
	 * wird beim Click auf einer der Radiobuttons im Panel "Schema" ausgelöst
	 * @param schemaNr
	 * @param evt
	 */
	private void onRadioButtonSchema(int schemaNr, ActionEvent evt)
	{
		if (istLadePhase)
			song.resetAlleAenderungMarker();
		
		if (schemaNr == -1)
		{
			song.resetAlleAenderungMarker();
		    return;
		}
		
//		if ((! istDirektNachNeuLaden) || (song.istEineAenderungErfolgt()))
		if (! istDirektNachNeuLaden)
		{
			int res = JOptionPane.showConfirmDialog(parent, 
					"Es werden alle Takte zurückgesetzt.\n"+
					"Soll der Schemawechsel trotzdem erfolgen ?", 
					"Warnung", 
					JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.NO_OPTION)
			{
				getJRadioButtonSchemaUnsichtbar().setSelected(true);
				return;
			}

		}
		song.getSchema().initSchema(schemaNr, song);
		resetSong();
		song.reloadTaktSchema();

		song.resetAlleAenderungMarker();
		istDirektNachNeuLaden = true;
	}
	
	
	/**
	 * wird beim Click auf einer der Radiobuttons im Panel "Takte pro Zeile" ausgelöst
	 * @param evt
	 */
	private void onRadioButtonAnzTakte(ActionEvent evt)
	{
		if (istLadePhase) 
			return;
		int anzProZeile = 4;
		if (jRadioButtonAnzTakte4.isSelected())
			anzProZeile = 4;
		else if (jRadioButtonAnzTakte6.isSelected())
			anzProZeile = 6;
		else if (jRadioButtonAnzTakte8.isSelected())
			anzProZeile = 8;
		
		song.getSchema().setTakteProZeile(anzProZeile);
		
		resetSong();
		song.reloadTaktSchema();
		song.getSchema().berechneAnteiligeHoehen();
		song.aktualisiereBlatt();
	}
	
	/**
	 * 
	 */
	public void onCheckBoxTonartAnzeige()
	{
		if (getJCheckTonartAnzeige().isSelected())
		{
			song.getSchema().getAkkTonArtSong().setInBearbeitung(true);
			song.getSongText().getSchema().getAkkTonArtSong().setInBearbeitung(true);
		}
		else
		{
			song.getSchema().getAkkTonArtSong().setInBearbeitung(false);
			song.getSongText().getSchema().getAkkTonArtSong().setInBearbeitung(false);
		}
		this.aktualisiereBlatt();
	}

	/**
	 * 
	 */
	public void onButtonHinweisText()
	{
		JOptionPane.showMessageDialog(this, 
					"Der eingegebene Titel wird als Dateiname des neuen Songs\n" +
					"vorgeschlagen. Sind in dem Titel Zeichen enthalten, die inner-\n" +
					"halb eines Dateinamen nicht vorhanden sein dürfen, dann werden\n" +
					"diese Zeichen im <Speichern unter>-Dialog aus dem Dateinamen\n" +
					"entfernt.\n"+
					"Der vom Taktus-Programm vorgeschlagene Dateiname kann im \n"+
					"<Speichern unter>-Dialog vom Bediener nachbearbeitet werden.",
					"Hinweis",
					JOptionPane.INFORMATION_MESSAGE);
	}

	
	
	/**
	 *  weitere Initialisierungen aus Konstruktor
	 */
	private void myInit()
	{
		this.getJRadioButtonSchema1().setSelected(true);
		this.getJRadioButtonAnzTakte4().setSelected(true);
	}


	/**
	 * wird aufgerufen, wenn sich auf Grund eines Click-Ereignisses eines
	 * RadioButtons das Layout des dargestellten Songs ändert
	 */
	private void resetSong()
	{
		song.konstruierBlatt(true);
		song.setRahmenAlleSchemas();
		this.parent.getJPanelTeile().setDaten(song);
		this.parent.getJPanelTakte().setDaten(song);
	}
	
	
	/**
	 * Aufruf von außen, wenn ein Song neu geladen wird, damit
	 * die entsprechenden Attribute hier im Panel korrekt 
	 * dargestellt werden.
	 * @param song
	 */
	public void setDaten(Song song)
	{
		istLadePhase = true;

		this.song = song;
		this.getJTextFieldAutor().setText(song.getAutor());
		this.getJTextFieldTitelName().setText(song.getTitel());
		
		switch (song.getSchema().getTakteProZeile())
		{
			case 4:
				getJRadioButtonAnzTakte4().setSelected(true);
				break;
			case 6:
				getJRadioButtonAnzTakte6().setSelected(true);
				break;
			case 8:
				getJRadioButtonAnzTakte8().setSelected(true);
				break;
			default:
				getJRadioButtonAnzTakte4().setSelected(true);
		}

		istLadePhase = false;
	}
	
	
	/**
	 * Initialisierungen nach dem Programmstart
	 * @param istStart
	 */
	public void setStartPhase(boolean istStart)
	{
		getJTextFieldTitelName().setEnabled(!istStart);
		getJTextFieldAutor().setEnabled(!istStart);
		for (int i=0;i<getJPanelSchema().getComponentCount();i++)
		{
			Component com = getJPanelSchema().getComponent(i);
			com.setEnabled(!istStart);
		}
		for (int i=0;i<getJPanelAnzahlTakte().getComponentCount();i++)
		{
			Component com = getJPanelAnzahlTakte().getComponent(i);
			com.setEnabled(!istStart);
		}
		for (int i=0;i<getJPanelTextAnzeige().getComponentCount();i++)
		{
			Component com = getJPanelTextAnzeige().getComponent(i);
			com.setEnabled(!istStart);
		}
		getPanelTonArt().setButtonsEnabled(!istStart);
		getJCheckTonartAnzeige().setEnabled(!istStart);
	}
	
	/**
	 * setzen des internen Attibutes von außen
	 */
	public void resetDirektNachNeuLaden()
	{
		istDirektNachNeuLaden = false;
	}
	
	/**
	 * 
	 */
	public void setNeuSong()
	{
		istLadePhase = true;
		istDirektNachNeuLaden = true;
		getPanelTonArt().getJRadioButtonF().doClick();
		getPanelTonArt().getJRadioButtonDur().doClick();
		getPanelTonArt().getJRadioButtonOhneVz().doClick();
		getJCheckTonartAnzeige().setSelected(false);
		onCheckBoxTonartAnzeige();
		getJRadioButtonTextMit1Seite().doClick();
		getJRadioButtonAnzTakte4().doClick();
		getJRadioButtonSchema1().doClick();
		getJTextFieldTitelName().requestFocus();
		getJTextFieldTitelName().selectAll();

		// der Hinweistext für einen neuen Titel wird eingeblendet
		getJLabelTitelHinweis().setVisible(true);
		getJButtonHinweisNeuerTitel().setVisible(true);
		
		istLadePhase = false;
	}
	
	/**
	 * 
	 */
	public void setAltSong()
	{
		istLadePhase = true;

		istDirektNachNeuLaden = false;

		// der Hinweistext für einen neuen Titel wird ausgeblendet
		getJLabelTitelHinweis().setVisible(false);
		getJButtonHinweisNeuerTitel().setVisible(false);

		getJRadioButtonSchemaUnsichtbar().doClick();
		
		if (song.getSchema().getAkkTonArtSong() != null)
		{
			panelTonArt.getJRadioButtonKreuz().setEnabled(true);
			panelTonArt.getJRadioButtonb().setEnabled(true);
			switch (song.getSchema().getAkkTonArtSong().getAkkTonOhneHalb())
			{
				case 1:
					panelTonArt.getJRadioButtonC().setSelected(true);
					panelTonArt.getJRadioButtonb().setEnabled(false);
					break;
				case 3:
					panelTonArt.getJRadioButtonD().setSelected(true);
					break;
				case 5:
					panelTonArt.getJRadioButtonE().setSelected(true);
					panelTonArt.getJRadioButtonKreuz().setEnabled(false);
					break;
				case 6:
					panelTonArt.getJRadioButtonF().setSelected(true);
					panelTonArt.getJRadioButtonb().setEnabled(false);
					break;
				case 8:
					panelTonArt.getJRadioButtonG().setSelected(true);
					break;
				case 10:
					panelTonArt.getJRadioButtonA().setSelected(true);
					break;
				case 12:
					panelTonArt.getJRadioButtonH().setSelected(true);
					panelTonArt.getJRadioButtonKreuz().setEnabled(false);
					break;
				default:
					panelTonArt.getJRadioButtonb().setEnabled(false);
					panelTonArt.getJRadioButtonC().setSelected(true);
			}
			
			switch (song.getSchema().getAkkTonArtSong().getAkkHalbton())
			{
				case 0:
					panelTonArt.getJRadioButtonOhneVz().setSelected(true);
					break;
				case 1:
					panelTonArt.getJRadioButtonb().setSelected(true);
					break;
				case 2:
					panelTonArt.getJRadioButtonKreuz().setSelected(true);
					break;
				default:
					panelTonArt.getJRadioButtonOhneVz().setSelected(true);
			}
			
			if ((song.getSchema().getAkkTonArtSong().getAkkArt().equals("")) ||
				(song.getSchema().getAkkTonArtSong().getAkkArt().equals("d")))
			{
				panelTonArt.getJRadioButtonDur().setSelected(true);
			}
			else
			{
				panelTonArt.getJRadioButtonMoll().setSelected(true);
			}
			
			if (song.getSchema().getAkkTonArtSong().isInBearbeitung())
			{
				getJCheckTonartAnzeige().setSelected(true);
			}
			else
			{
				getJCheckTonartAnzeige().setSelected(false);
			}
			
			switch (song.getText().getTextAnzeige())
			{
				case TextSchema.TEXT_ANZEIGE_OHNE:
					getJRadioButtonTextOhne().doClick();
					break;
				case TextSchema.TEXT_ANZEIGE_EINESEITE:
					getJRadioButtonTextMit1Seite().doClick();
					break;
				case TextSchema.TEXT_ANZEIGE_ZWEISEITEN:
					getJRadioButtonTextMit2Seiten().doClick();
					break;
	
				default:
					getJRadioButtonTextOhne().doClick();
					break;
			}
			
			switch (song.getSchema().getTakteProZeile())
			{
				case 4:
					getJRadioButtonAnzTakte4().doClick();
					break;
				case 6:
					getJRadioButtonAnzTakte6().doClick();
					break;
				case 8:
					getJRadioButtonAnzTakte8().doClick();
					break;
				default:
					getJRadioButtonAnzTakte4().doClick();
					break;
			}
			
		}
		istLadePhase = false;
	}
	

	/**
	 * Aktualisieren des Inhaltes des Songs auf der Oberfläche
	 */
	private void aktualisiereBlatt()
	{
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				song.aktualisiereBlatt();
				song.repaint();
				song.getSongText().aktualisiereBlatt();
				song.getSongText().repaint();
			}
		});
	}

	/**
	 * Änderungen im Titel werden hier direkt im Song auf der
	 * Oberfläche dargestellt
	 */
	private void handleDocTitelName()
	{
		if (istLadePhase)
			return;
		song.setTitel(getJTextFieldTitelName().getText());
		song.aktualisiereBlatt();
		song.getSongText().setTitel(getJTextFieldTitelName().getText());
		song.getSongText().aktualisiereBlatt();
	}
	
	/**
	 * Änderungen beim Autor werden hier direkt im Song auf der
	 * Oberfläche dargestellt
	 */
	private void handleDocAutor()
	{
		if (istLadePhase)
			return;
		song.setAutor(getJTextFieldAutor().getText());
		song.aktualisiereBlatt();
		song.getSongText().setAutor(getJTextFieldAutor().getText());
		song.getSongText().aktualisiereBlatt();
	}
	

	
	/* 
	 * Wenn in "APanelTonArt" ein Ton geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Song dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonTon(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonTon(final String ton, ItemEvent evt)
	{
		if ((evt.getStateChange() == ItemEvent.DESELECTED) || (istLadePhase))
			return;
//		System.out.println("===PanelReiter1 onButtonTon "+ton);
		
		song.getSchema().getAkkTonArtSong().setAkkTon(ton);
		song.getSchema().getAkkTonArtSong().vorbereiteAkkord();
		song.getSongText().getSchema().getAkkTonArtSong().setAkkTon(ton);
		song.getSongText().getSchema().getAkkTonArtSong().vorbereiteAkkord();
		
		if (song.getSchema().getAkkTonArtSong().istHalbTonNachObenVerboten())
		{
			if (panelTonArt.getJRadioButtonKreuz().isSelected())
				panelTonArt.getJRadioButtonOhneVz().setSelected(true);
			panelTonArt.getJRadioButtonKreuz().setEnabled(false);
		}
		else
			panelTonArt.getJRadioButtonKreuz().setEnabled(true);
		
		if (song.getSchema().getAkkTonArtSong().istHalbTonNachUntenVerboten())
		{
			if (panelTonArt.getJRadioButtonb().isSelected())
				panelTonArt.getJRadioButtonOhneVz().setSelected(true);
			panelTonArt.getJRadioButtonb().setEnabled(false);
		}
		else
			panelTonArt.getJRadioButtonb().setEnabled(true);

		this.aktualisiereBlatt();
	}
	
	/* 
	 * Wenn in "APanelTonArt" ein Halb-Ton geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Song dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonHalbTon(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonHalbTon(final String halbTon, ItemEvent evt)
	{
		if ((evt.getStateChange() == ItemEvent.DESELECTED) || (istLadePhase))
			return;
//		System.out.println("===PanelReiter1 onButtonHalbTon "+halbTon);
		song.getSchema().getAkkTonArtSong().setAkkHalbTon(halbTon);
		song.getSchema().getAkkTonArtSong().vorbereiteAkkord();
		song.getSongText().getSchema().getAkkTonArtSong().setAkkHalbTon(halbTon);
		song.getSongText().getSchema().getAkkTonArtSong().vorbereiteAkkord();
		this.aktualisiereBlatt();
	}
	
	/* 
	 * Wenn in "APanelTonArt" ein Ton-Geschlecht geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Song dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonTonGeschlecht(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonTonGeschlecht(final String geschlecht, ItemEvent evt)
	{
		if ((evt.getStateChange() == ItemEvent.DESELECTED) || (istLadePhase))
			return;
//		System.out.println("===PanelReiter1 onButtonTonGeschlecht "+geschlecht);
		song.getSchema().getAkkTonArtSong().setAkkArt(geschlecht);
		song.getSchema().getAkkTonArtSong().vorbereiteAkkord();
		song.getSongText().getSchema().getAkkTonArtSong().setAkkArt(geschlecht);
		song.getSongText().getSchema().getAkkTonArtSong().vorbereiteAkkord();
		this.aktualisiereBlatt();
	}

	/* 
	 * Key-Events spielen hier keine Rolle
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onKeyEvent(java.awt.event.KeyEvent)
	 */
	public void onKeyEvent(KeyEvent e)
	{
		
	}
	
	/* 
	 * TonKlang wird hier nicht dargestellt
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonTonKlang(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonTonKlang(final String klang, ItemEvent evt) 
	{
		
	}

}
