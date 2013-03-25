/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 11.10.2007 
 * 
 * Beschreibung:
 * 
 * Der im Hauptframe auf Reiter 3 (Akoord) dargestellte Panel
 * 
 * Hier wird ein einzelner Takt bearbeitet
 */
package AChord.MainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import AChord.AClasses.APanel;
import AChord.AClasses.APanelTonArt;
import AChord.AClasses.APanelTonArtInterface;
import AChord.Hilfe.Konfiguration;
import AChord.Schema.Akkord;
import AChord.Schema.Song;
import AChord.Schema.Takt;



public class PanelReiter3Takt extends APanel implements APanelTonArtInterface
{
    private final static long serialVersionUID = 1;

    private APanel jPanelAkkord;

	Song song;
	private JButton jButtonTaktRechts;
	private JButton jButtonTaktLinks;
	private JLabel jLabelTaktNrKomplett;
	private JLabel jLabelTaktNr;
	private JLabel jLabelTeilNr;
	private JLabel jLabelTeilungRechts;
	private JLabel jLabelTeilungMitte;
	private JLabel jLabelTeilungLinks;
	private JLabel jLabelBreakText1;
	private JLabel jLabelBreakText2;
	private JLabel jLabelBreakZaehlzeit1;
	private JLabel jLabelBreakZaehlzeit2;
	private JLabel jLabelBreakZaehlzeit3;
	private JLabel jLabelBreakZaehlzeit4;
	private JLabel jLabelBreakHinweistext;
	private JCheckBox jCheckBoxBreak1;
	private JCheckBox jCheckBoxBreak2;
	private JCheckBox jCheckBoxBreak3;
	private JCheckBox jCheckBoxBreak4;
	private JRadioButton jRadioButtonBOhne;
	private JTextField jTextFieldBreakHinweisText;
	private JPanel jPanelTaktName;
	private JCheckBox jCheckBoxTeilungLinks;
	private JCheckBox jCheckBoxTeilungMitte;
	private JCheckBox jCheckBoxTeilungRechts;
	private APanelTonArt panelTon;
	private APanel jPanelTonArtTeilung;
	
	Takt taktBearbeitung = null;
	Takt taktSong = null;
	Akkord akkordBearbeitung = null;
	Akkord akkordSong = null;
	
	boolean istBeimTaktLaden = false;
	
	boolean isEventVkEnter = false;

	/**
	 * Konstruktor
	 */
	public PanelReiter3Takt()
	{
		super();
		construct();
	}

	/**
	 * Initialisierungen des Frames
	 */
	private void construct()
	{
		try
		{
			initForm();
			myInit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Aufbau der Oberfläche
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		this.setPreferredSize(new java.awt.Dimension(452, 682));
		this.setLayout(null);
		this.addKeyListener(keyListener);
		setFont(getFontA());
		
		this.add(getJPanelAkkord());
		this.add(getPanelTon());
		this.add(getJPanelTonTeilung());
		this.add(getJPanelTaktName());
	}
	
	/**
	 * In diesem Panel wird das Taktus-implementierte Panel "APanelTonArt"
	 * dargestellt.
	 * @return
	 */
	private APanel getJPanelAkkord() {
		if (jPanelAkkord == null) {
			jPanelAkkord = new APanel();
			jPanelAkkord.setBounds(67, 165, 155, 132);
//			jPanelAkkord.setBounds(80, 160, 125, 125);
			jPanelAkkord.setBorder(new LineBorder(Color.BLACK));
			jPanelAkkord.setLayout(null);
		}
		return jPanelAkkord;
	}

	/**
	 * das Taktus-implementierte Panel "APanelTonArt"
	 * @return
	 */
	private APanelTonArt getPanelTon()
	{
		if (panelTon == null)
		{
			panelTon = new APanelTonArt(this);
			panelTon.setBounds(235, 168, 200, 200);
			panelTon.setLayout(null);
		}
		return panelTon;
	}

	/**
	 * In diesem Panel werden die Bearbeitungsmöglichkeiten unterhalb
	 * des Taktes verwaltet; dies sind die Teilung eines Taktues und
	 * der Heinweis auf einer bestimmten Zählzeit des Taktes
	 * @return
	 */
	private APanel getJPanelTonTeilung()
	{
		if (jPanelTonArtTeilung == null)
		{
			jPanelTonArtTeilung = new APanel();
			jPanelTonArtTeilung.setBounds(60, 290, 225, 255);
			jPanelTonArtTeilung.setLayout(null);
//			jPanelTonArtTeilung.setBorder(new LineBorder(Color.black));
			jPanelTonArtTeilung.add(getJCheckBoxTeilungLinks());
			jPanelTonArtTeilung.add(getJCheckBoxTeilungMitte());
			jPanelTonArtTeilung.add(getJCheckBoxTeilungRechts());
			jPanelTonArtTeilung.add(getJLabelTeilungLinks());
			jPanelTonArtTeilung.add(getJLabelTeilungMitte());
			jPanelTonArtTeilung.add(getJLabelTeilungRechts());
			jPanelTonArtTeilung.add(getJLabelBreakText1());
			jPanelTonArtTeilung.add(getJLabelBreakText2());
			jPanelTonArtTeilung.add(getJLabelBreakZaehlzeit1());
			jPanelTonArtTeilung.add(getJLabelBreakZaehlzeit2());
			jPanelTonArtTeilung.add(getJLabelBreakZaehlzeit3());
			jPanelTonArtTeilung.add(getJLabelBreakZaehlzeit4());
			jPanelTonArtTeilung.add(getJLabelBreakHinweistext());
			jPanelTonArtTeilung.add(getJCheckBoxBreak1());
			jPanelTonArtTeilung.add(getJCheckBoxBreak2());
			jPanelTonArtTeilung.add(getJCheckBoxBreak3());
			jPanelTonArtTeilung.add(getJCheckBoxBreak4());
			jPanelTonArtTeilung.add(getJRadioButtonBOhne());
			jPanelTonArtTeilung.add(getJTextFieldBreakHinweisText());
		}
		return jPanelTonArtTeilung;
	}

	/**
	 * Die Anzeige oberhalb des Taktes: Attribute des Taktes und
	 * zwei Buttons, um zum nächsten bzw. vorherigen Takt zu wechseln
	 * @return
	 */
	private JPanel getJPanelTaktName()
	{
		if (jPanelTaktName == null)
		{
			jPanelTaktName = new JPanel();
			jPanelTaktName.setLayout(null);
			jPanelTaktName.setBounds(80, 35, 125, 120);
			JLabel jLabelTeilNr = new JLabel();
			jLabelTeilNr.setText("TeilNr:");
			jLabelTeilNr.setFont(getFontA());
			jLabelTeilNr.setBounds(6, 12, 120, 21);
			jPanelTaktName.add(jLabelTeilNr);
			JLabel jLabelTaktNrTeil = new JLabel();
			jLabelTaktNrTeil.setText("TaktNr im Teil:");
			jLabelTaktNrTeil.setFont(getFontA());
			jLabelTaktNrTeil.setBounds(6, 32, 120, 21);
			jPanelTaktName.add(jLabelTaktNrTeil);
			JLabel jLabelTaktNrGesamt = new JLabel();
			jLabelTaktNrGesamt.setText("TaktNr:");
			jLabelTaktNrGesamt.setFont(getFontA());
			jLabelTaktNrGesamt.setBounds(6, 52, 120, 21);
			jPanelTaktName.add(jLabelTaktNrGesamt);
			jPanelTaktName.add(getJLabelTeilNr());
			jPanelTaktName.add(getJLabelTaktNr());
			jPanelTaktName.add(getJLabelTaktNrKomplett());
			jPanelTaktName.add(getJButtonTaktLinks());
			jPanelTaktName.add(getJButtonTaktRechts());
		}
		return jPanelTaktName;
	}


	/**
	 * Teilt, wenn vorhanden, die erste Hälfte des Taktes in zwei weitere
	 * Teile
	 * @return
	 */
	public JCheckBox getJCheckBoxTeilungLinks()
	{
		if (jCheckBoxTeilungLinks == null)
		{
			jCheckBoxTeilungLinks = new JCheckBox();
			jCheckBoxTeilungLinks.setText("");
			jCheckBoxTeilungLinks.setFont(getFontA());
			jCheckBoxTeilungLinks.setBounds(32, 10, 20, 20);
			jCheckBoxTeilungLinks.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxTeilungLinks();}});
			jCheckBoxTeilungLinks.addKeyListener(keyListener);
		}
		return jCheckBoxTeilungLinks;
	}
	
	/**
	 * Teilt den Takt (bzw. hebt Teilung auf) in zwei Teile
	 * @return
	 */
	public JCheckBox getJCheckBoxTeilungMitte()
	{
		if (jCheckBoxTeilungMitte == null)
		{
			jCheckBoxTeilungMitte = new JCheckBox();
			jCheckBoxTeilungMitte.setText("");
			jCheckBoxTeilungMitte.setFont(getFontA());
			jCheckBoxTeilungMitte.setBounds(72, 10, 20, 20);
			jCheckBoxTeilungMitte.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxTeilungMitte();}});
			jCheckBoxTeilungMitte.addKeyListener(keyListener);		
		}
		return jCheckBoxTeilungMitte;
	}
	
	/**
	 * Teilt, wenn vorhanden, die zweite Hälfte des Taktes in zwei weitere
	 * Teile
	 * @return
	 */
	public JCheckBox getJCheckBoxTeilungRechts()
	{
		if (jCheckBoxTeilungRechts == null)
		{
			jCheckBoxTeilungRechts = new JCheckBox();
			jCheckBoxTeilungRechts.setText("");
			jCheckBoxTeilungRechts.setFont(getFontA());
			jCheckBoxTeilungRechts.setBounds(113, 10, 20, 20);
			jCheckBoxTeilungRechts.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxTeilungRechts();}});
			jCheckBoxTeilungRechts.addKeyListener(keyListener);
		}
		return jCheckBoxTeilungRechts;
	}

	/**
	 * @return
	 */
	private JLabel getJLabelBreakText1()
	{
		if (jLabelBreakText1 == null)
		{
			jLabelBreakText1 = new JLabel();
			jLabelBreakText1.setText("Break bzw. Hinweis");
			jLabelBreakText1.setFont(getFontB());
			jLabelBreakText1.setBounds(20, 93, 115, 21);
		}
		return jLabelBreakText1;
	}
	/**
	 * @return
	 */
	private JLabel getJLabelBreakText2()
	{
		if (jLabelBreakText2 == null)
		{
			jLabelBreakText2 = new JLabel();
			jLabelBreakText2.setText("auf Zählzeit:");
			jLabelBreakText2.setFont(getFontB());
			jLabelBreakText2.setBounds(20, 110, 115, 21);
		}
		return jLabelBreakText2;
	}
	/**
	 * @return
	 */
	private JLabel getJLabelBreakZaehlzeit1()
	{
		if (jLabelBreakZaehlzeit1 == null)
		{
			jLabelBreakZaehlzeit1 = new JLabel();
			jLabelBreakZaehlzeit1.setText("1");
			jLabelBreakZaehlzeit1.setFont(getFontA());
			jLabelBreakZaehlzeit1.setBounds(21, 130, 15, 21);
		}
		return jLabelBreakZaehlzeit1;
	}
	/**
	 * @return
	 */
	private JLabel getJLabelBreakZaehlzeit2()
	{
		if (jLabelBreakZaehlzeit2 == null)
		{
			jLabelBreakZaehlzeit2 = new JLabel();
			jLabelBreakZaehlzeit2.setText("2");
			jLabelBreakZaehlzeit2.setFont(getFontA());
			jLabelBreakZaehlzeit2.setBounds(47, 130, 15, 21);
		}
		return jLabelBreakZaehlzeit2;
	}
	/**
	 * @return
	 */
	private JLabel getJLabelBreakZaehlzeit3()
	{
		if (jLabelBreakZaehlzeit3 == null)
		{
			jLabelBreakZaehlzeit3 = new JLabel();
			jLabelBreakZaehlzeit3.setText("3");
			jLabelBreakZaehlzeit3.setFont(getFontA());
			jLabelBreakZaehlzeit3.setBounds(73, 130, 15, 21);
		}
		return jLabelBreakZaehlzeit3;
	}
	/**
	 * @return
	 */
	private JLabel getJLabelBreakZaehlzeit4()
	{
		if (jLabelBreakZaehlzeit4 == null)
		{
			jLabelBreakZaehlzeit4 = new JLabel();
			jLabelBreakZaehlzeit4.setText("4");
			jLabelBreakZaehlzeit4.setFont(getFontA());
			jLabelBreakZaehlzeit4.setBounds(99, 130, 15, 21);
		}
		return jLabelBreakZaehlzeit4;
	}
	/**
	 * @return
	 */
	private JLabel getJLabelBreakHinweistext()
	{
		if (jLabelBreakHinweistext == null)
		{
			jLabelBreakHinweistext = new JLabel();
			jLabelBreakHinweistext.setText("Hinweistext:");
			jLabelBreakHinweistext.setFont(getFontB());
			jLabelBreakHinweistext.setBounds(20, 170, 150, 21);
		}
		return jLabelBreakHinweistext;
	}

	/**
	 * Checkbox, um das Breakzeichen (und ev. einen Hinweistext)
	 *  über den Takt auf die Zählzeit 1 zu setzen
	 * @return
	 */
	public JCheckBox getJCheckBoxBreak1()
	{
		if (jCheckBoxBreak1 == null)
		{
			jCheckBoxBreak1 = new JCheckBox();
			jCheckBoxBreak1.setText("");
			jCheckBoxBreak1.setFont(getFontA());
			jCheckBoxBreak1.setBounds(15, 145, 20, 20);
			jCheckBoxBreak1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxBreak(1, evt);}});
			jCheckBoxBreak1.addKeyListener(keyListener);
		}
		return jCheckBoxBreak1;
	}
	/**
	 * Checkbox, um das Breakzeichen (und ev. einen Hinweistext)
	 *  über den Takt auf die Zählzeit 2 zu setzen
	 * @return
	 */
	public JCheckBox getJCheckBoxBreak2()
	{
		if (jCheckBoxBreak2 == null)
		{
			jCheckBoxBreak2 = new JCheckBox();
			jCheckBoxBreak2.setText("");
			jCheckBoxBreak2.setFont(getFontA());
			jCheckBoxBreak2.setBounds(41, 145, 20, 20);
			jCheckBoxBreak2.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxBreak(2, evt);}});
			jCheckBoxBreak2.addKeyListener(keyListener);
		}
		return jCheckBoxBreak2;
	}

	/**
	 * Checkbox, um das Breakzeichen (und ev. einen Hinweistext)
	 *  über den Takt auf die Zählzeit 3 zu setzen
	 * @return
	 */
	public JCheckBox getJCheckBoxBreak3()
	{
		if (jCheckBoxBreak3 == null)
		{
			jCheckBoxBreak3 = new JCheckBox();
			jCheckBoxBreak3.setText("");
			jCheckBoxBreak3.setFont(getFontA());
			jCheckBoxBreak3.setBounds(67, 145, 20, 20);
			jCheckBoxBreak3.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxBreak(3, evt);}});
			jCheckBoxBreak3.addKeyListener(keyListener);
		}
		return jCheckBoxBreak3;
	}

	/**
	 * Checkbox, um das Breakzeichen (und ev. einen Hinweistext)
	 *  über den Takt auf die Zählzeit 4 zu setzen
	 * @return
	 */
	public JCheckBox getJCheckBoxBreak4()
	{
		if (jCheckBoxBreak4 == null)
		{
			jCheckBoxBreak4 = new JCheckBox();
			jCheckBoxBreak4.setText("");
			jCheckBoxBreak4.setFont(getFontA());
			jCheckBoxBreak4.setBounds(93, 145, 20, 20);
			jCheckBoxBreak4.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxBreak(4, evt);}});
			jCheckBoxBreak4.addKeyListener(keyListener);
		}
		return jCheckBoxBreak4;
	}
	/**
	 * interne versteckt Checkbox, um zu regeln, dass kein Break gesetzt wird
	 * @return
	 */
	public JRadioButton getJRadioButtonBOhne()
	{
		if (jRadioButtonBOhne == null)
		{
			jRadioButtonBOhne = new JRadioButton();
			jRadioButtonBOhne.setText("ohne");
			jRadioButtonBOhne.setVisible(false);
			jRadioButtonBOhne.setBounds(125, 145, 70, 20);
			jRadioButtonBOhne.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onCheckBoxBreak(0, evt);}});
			jRadioButtonBOhne.addKeyListener(keyListener);
		}
		return jRadioButtonBOhne;
	}
	
	/**
	 * Testfeld, um den Heinweistext einzugeben, der am Breakzeichen
	 * über dem Takt angezeigt wird. Text wird nur angezeigt, wenn genau
	 * ein Breakzeichen angezeig wird.
	 * @return
	 */
	public JTextField getJTextFieldBreakHinweisText()
	{
		if (jTextFieldBreakHinweisText == null)
		{
			jTextFieldBreakHinweisText = new JTextField();
			jTextFieldBreakHinweisText.setText("");
			jTextFieldBreakHinweisText.setFont(getFontA());
			jTextFieldBreakHinweisText.setBounds(20, 190, 150, 20);
			PlainDocument docTitel = new PlainDocument();
			jTextFieldBreakHinweisText.setDocument(docTitel);
			docTitel.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleDocBreakHinweisText(); }
				public void removeUpdate(DocumentEvent e) { handleDocBreakHinweisText(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		return jTextFieldBreakHinweisText;
	}

	/**
	 * @return
	 */
	private JLabel getJLabelTeilungLinks()
	{
		if (jLabelTeilungLinks == null)
		{
			jLabelTeilungLinks = new JLabel();
			jLabelTeilungLinks.setText("/");
			jLabelTeilungLinks.setFont(getFontB());
			jLabelTeilungLinks.setBounds(42, 28, 15, 21);
		}
		return jLabelTeilungLinks;
	}

	/**
	 * @return
	 */
	private JLabel getJLabelTeilungMitte()
	{
		if (jLabelTeilungMitte == null)
		{
			jLabelTeilungMitte = new JLabel();
			jLabelTeilungMitte.setText("/");
			jLabelTeilungMitte.setFont(getFontC());
			jLabelTeilungMitte.setBounds(79, 30, 25, 25);
		}
		return jLabelTeilungMitte;
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelTeilungRechts()
	{
		if (jLabelTeilungRechts == null)
		{
			jLabelTeilungRechts = new JLabel();
			jLabelTeilungRechts.setText("/");
			jLabelTeilungRechts.setFont(getFontB());
			jLabelTeilungRechts.setBounds(122, 28, 15, 21);
		}
		return jLabelTeilungRechts;
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelTeilNr()
	{
		if (jLabelTeilNr == null)
		{
			jLabelTeilNr = new JLabel();
			jLabelTeilNr.setText("1");
			jLabelTeilNr.setFont(getFontA());
			jLabelTeilNr.setBounds(106, 12, 120, 21);
		}
		return jLabelTeilNr;
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelTaktNr()
	{
		if (jLabelTaktNr == null)
		{
			jLabelTaktNr = new JLabel();
			jLabelTaktNr.setText("2");
			jLabelTaktNr.setFont(getFontA());
			jLabelTaktNr.setBounds(106, 32, 120, 21);
		}
		return jLabelTaktNr;
	}

	/**
	 * @return
	 */
	private JLabel getJLabelTaktNrKomplett()
	{
		if (jLabelTaktNrKomplett == null)
		{
			jLabelTaktNrKomplett = new JLabel();
			jLabelTaktNrKomplett.setText("2");
			jLabelTaktNrKomplett.setFont(getFontA());
			jLabelTaktNrKomplett.setBounds(106, 52, 120, 21);
		}
		return jLabelTaktNrKomplett;
	}

	/**
	 * @return
	 */
	public JButton getJButtonTaktLinks()
	{
		if (jButtonTaktLinks == null)
		{
			jButtonTaktLinks = new JButton();
			jButtonTaktLinks.setText("<");
			jButtonTaktLinks.setFont(getFontA());
			jButtonTaktLinks.setMargin(new Insets(2,2,2,2));
			jButtonTaktLinks.setBounds(0, 95, 25, 25);
			jButtonTaktLinks.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonTaktLinksRechts(true);}});
			jButtonTaktLinks.addKeyListener(keyListener);
		}
		return jButtonTaktLinks;
	}
	
	/**
	 * @return
	 */
	public JButton getJButtonTaktRechts()
	{
		if (jButtonTaktRechts == null)
		{
			jButtonTaktRechts = new JButton();
			jButtonTaktRechts.setText(">");
			jButtonTaktRechts.setMargin(new Insets(2,2,2,2));
			jButtonTaktRechts.setFont(getFontA());
			jButtonTaktRechts.setBounds(100, 95, 25, 25);
			jButtonTaktRechts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					onButtonTaktLinksRechts(false);}});
			jButtonTaktRechts.addKeyListener(keyListener);
		}
		return jButtonTaktRechts;
	}

	
	
	//*********************************************************************************
	//*********************************************************************************
	// Actions von RadioButton, Checkboxen
	//*********************************************************************************
	//*********************************************************************************

	/**
	 * Behandlung von Keyevents; Die Takteingabe soll auch mit der
	 * Tastatur funktionieren
	 * @param e
	 */
	public void handleKeyEvent(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
				if (!getJButtonTaktRechts().hasFocus())
					getJButtonTaktRechts().requestFocus();
				getJButtonTaktRechts().doClick();
				isEventVkEnter = true;
				break;
			case KeyEvent.VK_RIGHT:
				if (!getJButtonTaktRechts().hasFocus())
					getJButtonTaktRechts().requestFocus();
				getJButtonTaktRechts().doClick();
				break;
			case KeyEvent.VK_LEFT:
				if (!getJButtonTaktLinks().hasFocus())
					getJButtonTaktLinks().requestFocus();
				getJButtonTaktLinks().doClick();
				break;
			case KeyEvent.VK_T:
				if (! getJCheckBoxTeilungMitte().isSelected())
				{
					getJCheckBoxTeilungMitte().doClick();
				}
				else if ((! getJCheckBoxTeilungLinks().isSelected()) &&
						 (! getJCheckBoxTeilungRechts().isSelected()))
				{
					getJCheckBoxTeilungLinks().doClick();
				}
				else if ((getJCheckBoxTeilungLinks().isSelected()) &&
						 (! getJCheckBoxTeilungRechts().isSelected()))
				{
					getJCheckBoxTeilungLinks().doClick();
					getJCheckBoxTeilungRechts().doClick();
				}
				else if ((! getJCheckBoxTeilungLinks().isSelected()) &&
						 (getJCheckBoxTeilungRechts().isSelected()))
				{
					getJCheckBoxTeilungLinks().doClick();
				}
				else
					getJCheckBoxTeilungMitte().doClick();
				break;
			case KeyEvent.VK_1:
				if (getJCheckBoxBreak1().isSelected())
					getJRadioButtonBOhne().doClick();
				else
					getJCheckBoxBreak1().doClick();
				break;
			case KeyEvent.VK_2:
				if (getJCheckBoxBreak2().isSelected())
					getJRadioButtonBOhne().doClick();
				else
					getJCheckBoxBreak2().doClick();
				break;
			case KeyEvent.VK_3:
				if (getJCheckBoxBreak3().isSelected())
					getJRadioButtonBOhne().doClick();
				else
					getJCheckBoxBreak3().doClick();
				break;
			case KeyEvent.VK_4:
				if (getJCheckBoxBreak4().isSelected())
					getJRadioButtonBOhne().doClick();
				else
					getJCheckBoxBreak4().doClick();
				break;
			default:
				break;
		}
	}

	/* 
	 * Wenn in "APanelTonArt" ein KeyEvent erzeugt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) für die Weiterleitung gesorgt. 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onKeyEvent(java.awt.event.KeyEvent)
	 */
	public void onKeyEvent(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_T:
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
				handleKeyEvent(e);
				break;
				
			default:
				panelTon.handleKeyEvent(e);
				break;
		}
	}
		
	/**
	 * Reaktion auf ein Event der beiden Buttons "links" und "rechts" (vorheriger
	 * bzw. nächster Takt)
	 * @param istLinks
	 */
	private void onButtonTaktLinksRechts(boolean istLinks)
	{
		if ((song == null) || (taktBearbeitung == null))
			return;
		if (isEventVkEnter)
		{
			// dies musste eingefügt werden, weil bei einem Event "VKEnter"
			// immer zusätzlich zum im KeyListener ausgewerteten Event
			// noch eine ClickEvent (wie von Maus) ausgelöst wird
			isEventVkEnter = false;
			return;
		}
		if (istLinks)
		{
			if (! taktBearbeitung.setRahmenVorherigerAkkord(akkordBearbeitung))
				setTaktDaten(song.setMarkierungVorherigerTakt(true), true);
		}
		else
		{
			if (! taktBearbeitung.setRahmenNaechsterAkkord(akkordBearbeitung))
				setTaktDaten(song.setMarkierungNaechsterTakt(), false);
		}
	}
	
	/* 
	 * Wenn in "APanelTonArt" ein Tongeschlecht geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Takt dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonTonGeschlecht(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonTonGeschlecht(final String geschlecht, ItemEvent evt)
	{
		if ((istBeimTaktLaden) || (evt.getStateChange() == ItemEvent.DESELECTED))
			return;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				taktBearbeitung.removeAll();
				taktSong.removeAll();
				
				akkordBearbeitung.setAkkArt(geschlecht);
				akkordBearbeitung.vorbereiteAkkord();
				akkordSong.setAkkArt(geschlecht);
				akkordSong.vorbereiteAkkord();
				
				taktBearbeitung.konstuiereTakt();
				getJPanelAkkord().add(taktBearbeitung);
				PanelReiter3Takt.this.repaint();

				song.aktualisiereBlatt();
				song.setRahmenAlleSchemas();
				song.reloadTaktSchema();
			}
		});
	}
	
	/* 
	 * Wenn in "APanelTonArt" ein Halb-Ton geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Takt dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonHalbTon(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonHalbTon(final String halbTon, ItemEvent evt)
	{
		if ((istBeimTaktLaden) || (evt.getStateChange() == ItemEvent.DESELECTED))
			return;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				taktBearbeitung.removeAll();
				taktSong.removeAll();
				
				akkordBearbeitung.setAkkHalbTon(halbTon);
				akkordBearbeitung.vorbereiteAkkord();
				akkordSong.setAkkHalbTon(halbTon);
				akkordSong.vorbereiteAkkord();
				
				taktBearbeitung.konstuiereTakt();
				getJPanelAkkord().add(taktBearbeitung);
				PanelReiter3Takt.this.repaint();

				song.aktualisiereBlatt();
				song.setRahmenAlleSchemas();
				song.reloadTaktSchema();
			}
		});
	}

	/* 
	 * Wenn in "APanelTonArt" ein TonKlang geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Takt dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonTonKlang(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonTonKlang(final String klang, ItemEvent evt) 
	{
		if ((istBeimTaktLaden) || 
			((evt != null) && (evt.getStateChange() == ItemEvent.DESELECTED)))
			return;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				taktBearbeitung.removeAll();
				taktSong.removeAll();
				
				akkordBearbeitung.setAkkKlang(klang);
				akkordBearbeitung.vorbereiteAkkord();
				akkordSong.setAkkKlang(klang);
				akkordSong.vorbereiteAkkord();
				
				taktBearbeitung.konstuiereTakt();
				getJPanelAkkord().add(taktBearbeitung);
				PanelReiter3Takt.this.repaint();

				song.aktualisiereBlatt();
				song.setRahmenAlleSchemas();
				song.reloadTaktSchema();
			}
		});
	}

	/* 
	 * Wenn in "APanelTonArt" eine Tonart geclickt wird, dann wird in dieser
	 * Interface-Methode (APanelTonArtInterface) dafür gesorgt, dass
	 * der Ton entsprechend der Vorgaben im Takt dargestellt wird 
	 * (non-Javadoc)
	 * @see AChord.AClasses.APanelTonArtInterface#onRadioButtonTon(java.lang.String, java.awt.event.ItemEvent)
	 */
	public void onRadioButtonTon(final String ton, ItemEvent evt)
	{
		if ((istBeimTaktLaden) || (evt.getStateChange() == ItemEvent.DESELECTED))
			return;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				istBeimTaktLaden = true;
				
				taktBearbeitung.removeAll();
				taktSong.removeAll();
				
				akkordBearbeitung.setAkkTon(ton);
				akkordBearbeitung.vorbereiteAkkord();
				akkordBearbeitung.setRahmen();
				akkordSong.setAkkTon(ton);
				akkordSong.vorbereiteAkkord();
				
				taktBearbeitung.konstuiereTakt();
				getJPanelAkkord().add(taktBearbeitung);
				PanelReiter3Takt.this.repaint();

				song.aktualisiereBlatt();
				song.setRahmenAlleSchemas();
				song.reloadTaktSchema();
				
				if (akkordBearbeitung.istHalbTonNachObenVerboten())
				{
					if (panelTon.getJRadioButtonKreuz().isSelected())
						panelTon.getJRadioButtonOhneVz().setSelected(true);
					panelTon.getJRadioButtonKreuz().setEnabled(false);
				}
				else
					panelTon.getJRadioButtonKreuz().setEnabled(true);
				
				if (akkordBearbeitung.istHalbTonNachUntenVerboten())
				{
					if (panelTon.getJRadioButtonb().isSelected())
						panelTon.getJRadioButtonOhneVz().setSelected(true);
					panelTon.getJRadioButtonb().setEnabled(false);
				}
				else
					panelTon.getJRadioButtonb().setEnabled(true);
						
				istBeimTaktLaden = false;
			}
		});
	}

	/**
	 * Hier wird auf eine Änderung des Hinweistextes reagiert um den Text
	 * im SOng darzustellen
	 */
	private void handleDocBreakHinweisText()
	{
		if (istBeimTaktLaden)
			return;
		taktBearbeitung.setBreakText(getJTextFieldBreakHinweisText().getText());
		taktSong.setBreakText(getJTextFieldBreakHinweisText().getText());
		song.aktualisiereBlatt();
		song.setRahmenAlleSchemas();
		song.reloadTaktSchema();
	}

	/**
	 * Setzen des Break-Zeichens in der entsprechenden Zählzeit (int zeit)
	 * im Song
	 * @param zeit
	 * @param evt
	 */
	private void onCheckBoxBreak(final int zeit, final ItemEvent evt)
	{
		if (istBeimTaktLaden)
			return;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				istBeimTaktLaden = true;
		
				taktBearbeitung.setBreakQuotIntern(zeit, (evt.getStateChange() == ItemEvent.SELECTED));
				taktSong.setBreakQuotIntern(zeit, (evt.getStateChange() == ItemEvent.SELECTED));
				
				if (taktBearbeitung.istGenauEinBreakGesetzt())
				{
					getJTextFieldBreakHinweisText().setEnabled(true);
					taktBearbeitung.setBreakText(getJTextFieldBreakHinweisText().getText());
					taktSong.setBreakText(getJTextFieldBreakHinweisText().getText());
				}
				else
				{
					getJTextFieldBreakHinweisText().setEnabled(false);
					taktBearbeitung.setBreakText("");
					taktSong.setBreakText("");
				}

				song.aktualisiereBlatt();
				song.setRahmenAlleSchemas();
				song.reloadTaktSchema();

				istBeimTaktLaden = false;
			}
		});
	}

	/**
	 * Reaktion beim Click der linken Teilungs-Checkbox
	 */
	private void onCheckBoxTeilungLinks()
	{
		if (istBeimTaktLaden)
			return;

		istBeimTaktLaden = true;
		
		handleTeilung(getJCheckBoxTeilungLinks().isSelected(),
						getJCheckBoxTeilungMitte().isSelected(),
						getJCheckBoxTeilungRechts().isSelected());
				
		istBeimTaktLaden = false;
	}
	
	/**
	 * Reaktion beim Click der mittleren Teilungs-Checkbox
	 */
	private void onCheckBoxTeilungMitte()
	{
		if (istBeimTaktLaden)
			return;

		istBeimTaktLaden = true;

		if (getJCheckBoxTeilungMitte().isSelected())
		{
			getJCheckBoxTeilungLinks().setEnabled(true);
			getJCheckBoxTeilungRechts().setEnabled(true);
		}
		else
		{
			getJCheckBoxTeilungLinks().setEnabled(false);
			getJCheckBoxTeilungRechts().setEnabled(false);
			getJCheckBoxTeilungLinks().setSelected(false);
			getJCheckBoxTeilungRechts().setSelected(false);
		}
		handleTeilung(getJCheckBoxTeilungLinks().isSelected(),
						getJCheckBoxTeilungMitte().isSelected(),
						getJCheckBoxTeilungRechts().isSelected());
						
		istBeimTaktLaden = false;
	}
	
	/**
	 * Reaktion beim Click der rechten Teilungs-Checkbox
	 */
	private void onCheckBoxTeilungRechts()
	{
		if (istBeimTaktLaden)
			return;

		istBeimTaktLaden = true;
		
		handleTeilung(getJCheckBoxTeilungLinks().isSelected(),
						getJCheckBoxTeilungMitte().isSelected(),
						getJCheckBoxTeilungRechts().isSelected());
				
		istBeimTaktLaden = false;
	}
	
	/**
	 * @param t1
	 * @param t2
	 * @param t3
	 * 
	 * Bearbeitung, wenn der Bediener einen der Teilungs-Checkboxen betätigt
	 */
	private void handleTeilung(boolean t1, boolean t2, boolean t3)
	{
		int taktTeil;
		if ((!t1) && (!t2) && (!t3))
			taktTeil = Takt.TAKT_TEILUNG_OHNE;
		else if ((!t1) && (t2) && (!t3))
			taktTeil = Takt.TAKT_TEILUNG_ZWEI;
		else if ((t1) && (t2) && (!t3))
			taktTeil = Takt.TAKT_TEILUNG_DREIOBENLINKS;
		else if ((!t1) && (t2) && (t3))
			taktTeil = Takt.TAKT_TEILUNG_DREIUNTENRECHTS;
		else if ((t1) && (t2) && (t3))
			taktTeil = Takt.TAKT_TEILUNG_VIER;
		else
			taktTeil = Takt.TAKT_TEILUNG_OHNE;
		
		taktBearbeitung.removeAll();
		taktSong.removeAll();
		
		taktBearbeitung.resetAlleTaktMarkierungen();
		taktBearbeitung.checkeNachAenderungTeilung(taktTeil);

		taktSong.resetAlleTaktMarkierungen();
		taktSong.checkeNachAenderungTeilung(taktTeil);
		
		akkordBearbeitung = taktBearbeitung.getAkkordInBearbeitung();
		akkordSong = taktSong.getAkkordInBearbeitung();
		
		taktBearbeitung.konstuiereTakt();
		
		taktBearbeitung.aktiviereAkkordMausMarkierung();
		taktBearbeitung.setMarkierungsRahmenAlleAkkorde();
		getJPanelAkkord().add(taktBearbeitung);
		PanelReiter3Takt.this.repaint();

		this.setzeTonRadioButton();
		
		song.aktualisiereBlatt();
		song.setRahmenAlleSchemas();
		song.reloadTaktSchema();
	}

	
	
	//*********************************************************************************
	//*********************************************************************************
	// allgemeine Methoden
	//*********************************************************************************
	//*********************************************************************************

	/**
	 * setzen des Focuseseses von außen
	 */
	public void setFocus()
	{
		getJButtonTaktRechts().requestFocus();
	}
	
	/**
	 * setzen initialer Werte der Oberfläche
	 */
	private void myInit()
	{
	}
	
	/**
	 * Aufruf von außen, wenn ein Song neu geladen wird, damit
	 * die entsprechenden Attribute hier im Panel korrekt 
	 * dargestellt werden.
	 * @param song
	 */
	public void setDaten(final Song song)
	{
		this.song = song;
		
		setTaktDaten(song.setMarkierungErsterTakt(), false);
	}
	
	
	/**
	 * @param akk
	 */
	private void testAusgabe(Akkord akk)
	{
		if (akk != null)
		{
//			System.out.println("*******"+akk.getAkkTon()+"/"+
//						akk.getAkkHalbton()+"/"+
//						akk.getAkkKlang()+"/"+
//						akk.getAkkArt()+"/"+
//						akk.wandleTonIntern2String()+"/"+
//						akk.getAkkordNr()+"/"+
//						akk.isInBearbeitung());
		}
		else
		{
//			System.out.println("**leer**");
		}
	}
	
	/**
	 * 	setzen der RadioButtons in Abhängigkeit vom Inhalt
	 *  des gewählten Akkordes
	 */
	private void setzeTonRadioButton()
	{
		if (akkordBearbeitung != null)
		{
			//getJButtonTaktLinks().requestFocus();
			panelTon.getJRadioButtonKreuz().setEnabled(true);
			panelTon.getJRadioButtonb().setEnabled(true);
			switch (akkordBearbeitung.getAkkTonOhneHalb())
			{
				case 1:
					panelTon.getJRadioButtonC().setSelected(true);
					panelTon.getJRadioButtonb().setEnabled(false);
					break;
				case 3:
					panelTon.getJRadioButtonD().setSelected(true);
					break;
				case 5:
					panelTon.getJRadioButtonE().setSelected(true);
					panelTon.getJRadioButtonKreuz().setEnabled(false);
					break;
				case 6:
					panelTon.getJRadioButtonF().setSelected(true);
					panelTon.getJRadioButtonb().setEnabled(false);
					break;
				case 8:
					panelTon.getJRadioButtonG().setSelected(true);
					break;
				case 10:
					panelTon.getJRadioButtonA().setSelected(true);
					break;
				case 12:
					panelTon.getJRadioButtonH().setSelected(true);
					panelTon.getJRadioButtonKreuz().setEnabled(false);
					break;
				default:
					panelTon.getJRadioButtonb().setEnabled(false);
					panelTon.getJRadioButtonC().setSelected(true);
			}
			
			switch (akkordBearbeitung.getAkkHalbton())
			{
				case 0:
					panelTon.getJRadioButtonOhneVz().setSelected(true);
					break;
				case 1:
					panelTon.getJRadioButtonb().setSelected(true);
					break;
				case 2:
					panelTon.getJRadioButtonKreuz().setSelected(true);
					break;
				default:
					panelTon.getJRadioButtonOhneVz().setSelected(true);

			}
			
			if (akkordBearbeitung.getAkkArt().equals(""))
			{
				panelTon.getJRadioButtonDur().setSelected(true);
			}
			else
			{
				panelTon.getJRadioButtonMoll().setSelected(true);
			}
			
			if (akkordBearbeitung.getAkkKlang().equals("6"))
			{
				panelTon.getJRadioButton6().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("7"))
			{
				panelTon.getJRadioButton7().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("9"))
			{
				panelTon.getJRadioButton9().setSelected(true);
			}
			else if ((akkordBearbeitung.getAkkKlang().equals("o")) ||
					 (akkordBearbeitung.getAkkKlang().equals("0")))
			{
				panelTon.getJRadioButton0().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("maj"))
			{
				panelTon.getJRadioButtonMaj().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("7/9"))
			{
				panelTon.getJRadioButton79().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("7/5"))
			{
				panelTon.getJRadioButton75().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("+"))
			{
				panelTon.getJRadioButtonPlus().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().equals("ohne")) 
			{
				panelTon.getJRadioButtonArtOhne().setSelected(true);
			}
			else if (akkordBearbeitung.getAkkKlang().trim().length() > 0)
			{
				//System.out.println("1  "+akkordBearbeitung.getAkkKlang());
				panelTon.setOnVonAussen(true);
				panelTon.getJRadioButtonArtUnsichtbar().setSelected(true);
				panelTon.getJRadioButtonArtUnsichtbar().repaint();
				panelTon.getJTextFieldEgal().setText(akkordBearbeitung.getAkkKlang());
				panelTon.getJTextFieldEgal().repaint();
				panelTon.getRadioButtonFrei().setSelected(true);
				getJButtonTaktLinks().requestFocus();

				//panelTon.onHandleFocusEgal(false);
				panelTon.setOnVonAussen(false);
			}
			else
			{
				panelTon.getJRadioButtonArtOhne().setSelected(true);
			}
			
			switch (taktBearbeitung.getTeilung())
			{
				case Takt.TAKT_TEILUNG_OHNE:
					getJCheckBoxTeilungLinks().setSelected(false);
					getJCheckBoxTeilungMitte().setSelected(false);
					getJCheckBoxTeilungRechts().setSelected(false);
					getJCheckBoxTeilungLinks().setEnabled(false);
					getJCheckBoxTeilungRechts().setEnabled(false);
					break;
				case Takt.TAKT_TEILUNG_ZWEI:
					getJCheckBoxTeilungLinks().setSelected(false);
					getJCheckBoxTeilungMitte().setSelected(true);
					getJCheckBoxTeilungRechts().setSelected(false);
					getJCheckBoxTeilungLinks().setEnabled(true);
					getJCheckBoxTeilungRechts().setEnabled(true);
					break;
				case Takt.TAKT_TEILUNG_DREIOBENLINKS:
					getJCheckBoxTeilungLinks().setSelected(true);
					getJCheckBoxTeilungMitte().setSelected(true);
					getJCheckBoxTeilungRechts().setSelected(false);
					getJCheckBoxTeilungLinks().setEnabled(true);
					getJCheckBoxTeilungRechts().setEnabled(true);
					break;
				case Takt.TAKT_TEILUNG_DREIUNTENRECHTS:
					getJCheckBoxTeilungLinks().setSelected(false);
					getJCheckBoxTeilungMitte().setSelected(true);
					getJCheckBoxTeilungRechts().setSelected(true);
					getJCheckBoxTeilungLinks().setEnabled(true);
					getJCheckBoxTeilungRechts().setEnabled(true);
					break;
				case Takt.TAKT_TEILUNG_VIER:
					getJCheckBoxTeilungLinks().setSelected(true);
					getJCheckBoxTeilungMitte().setSelected(true);
					getJCheckBoxTeilungRechts().setSelected(true);
					getJCheckBoxTeilungLinks().setEnabled(true);
					getJCheckBoxTeilungRechts().setEnabled(true);
					break;

				default:
					break;
			}
			if (taktBearbeitung.istBreakAufEins())
				getJCheckBoxBreak1().setSelected(true);
			else
				getJCheckBoxBreak1().setSelected(false);
			if (taktBearbeitung.istBreakAufZwei())
				getJCheckBoxBreak2().setSelected(true);
			else
				getJCheckBoxBreak2().setSelected(false);
			if (taktBearbeitung.istBreakAufDrei())
				getJCheckBoxBreak3().setSelected(true);
			else
				getJCheckBoxBreak3().setSelected(false);
			if (taktBearbeitung.istBreakAufVier())
				getJCheckBoxBreak4().setSelected(true);
			else
				getJCheckBoxBreak4().setSelected(false);

			if (taktBearbeitung.istGenauEinBreakGesetzt())
			{
				getJTextFieldBreakHinweisText().setEnabled(true);
				getJTextFieldBreakHinweisText().setText(taktBearbeitung.getBreakText());
			}
			else
			{
				getJTextFieldBreakHinweisText().setEnabled(false);
				getJTextFieldBreakHinweisText().setText("");
			}
		}
	}

	/**
	 * @param istKompletterTakt
	 * 
	 * deselektieren der RadioButtons (wenn denn der Takt leer ist ...)
	 * 
	 */
	private void resetTaktDaten(boolean istKompletterTakt)
	{
		this.panelTon.getJRadioButtonGeschlechtUnsichtbar().setSelected(true);
		this.panelTon.getJRadioButtonVzUnsichtbar().setSelected(true);
		this.panelTon.getJRadioButtonTonUnsichtbar().setSelected(true);
		this.panelTon.getJRadioButtonArtUnsichtbar().setSelected(true);
		if (istKompletterTakt)
		{
			this.getJCheckBoxTeilungLinks().setSelected(false);
			this.getJCheckBoxTeilungMitte().setSelected(false);
			this.getJCheckBoxTeilungRechts().setSelected(false);
		}
		
		setRadioButtonEnabled(false, istKompletterTakt);
	}
	
	/**
	 * @param ist
	 * @param istKompletterTakt
	 * 
	 *   enablen bzw. disablen der RadioButtons
	 */
	private void setRadioButtonEnabled(boolean ist, boolean istKompletterTakt)
	{
		for (int i=0;i<panelTon.getJPanelTonKlang().getComponentCount();i++)
		{
			Component com = panelTon.getJPanelTonKlang().getComponent(i);
			com.setEnabled(ist);
		}
		for (int i=0;i<panelTon.getJPanelTonVz().getComponentCount();i++)
		{
			Component com = panelTon.getJPanelTonVz().getComponent(i);
			com.setEnabled(ist);
		}
		for (int i=0;i<panelTon.getJPanelTonGeschlecht().getComponentCount();i++)
		{
			Component com = panelTon.getJPanelTonGeschlecht().getComponent(i);
			com.setEnabled(ist);
		}
		if (istKompletterTakt)
		{
			for (int i=0;i<getJPanelTonTeilung().getComponentCount();i++)
			{
				Component com = getJPanelTonTeilung().getComponent(i);
				com.setEnabled(ist);
			}
		}
	}
	
	/**
	 * @param akk
	 */
	private void setzeAkkordMitVorherigenAkkord(Akkord akk)
	{
		Takt taktVorher = song.setMarkierungVorherigerTakt(false);
		if ((taktVorher == null) || (taktVorher.istLeererTakt()) || (taktVorher.getAkkord1() == null))
		{
			if ((song.getSchema() == null) || (song.getSchema().getTonArt() == 0))
				akk.setAkkTon(1);
			else
			{
				akk.setAkkTon(song.getSchema().getTonArt());
				akk.setAkkHalbton(song.getSchema().getAkkTonArtSong().getAkkHalbton());
				akk.setAkkArt(song.getSchema().getAkkTonArtSong().getAkkArt());
			}
		}
		else
		{
			Akkord vorher = taktVorher.getAkkord1();
			akk.setAkkTon(vorher.getAkkTon());
			akk.setAkkHalbton(vorher.getAkkHalbton());

			// Konfiguration
			int anzeige = Konfiguration.getInstance().getKonfAnzeigeNeuerTakt();
			switch (anzeige)
			{
				case Konfiguration.ANZEIGE_NEUERTAKT_KLANG:
					akk.setAkkKlang(vorher.getAkkKlang());
					break;
				case Konfiguration.ANZEIGE_NEUERTAKT_ART:
					akk.setAkkArt(vorher.getAkkArt());
					break;
				case Konfiguration.ANZEIGE_NEUERTAKT_ARTKLANG:
					akk.setAkkKlang(vorher.getAkkKlang());
					akk.setAkkArt(vorher.getAkkArt());
					break;
	
				default:
					break;
			}
		}
	}
	
	/**
	 * @param takt
	 * 
	 * Audruf aus Mouseevent aus dem Song heraus
	 * @param istLetzterAkkordMarkiert
	 */
	public void setTaktDaten(Takt takt, boolean istLetzterAkkordMarkiert)
	{
		istBeimTaktLaden = true;
		
		if (taktBearbeitung != null)
    	{
    		getJPanelAkkord().remove(taktBearbeitung);
    	}
		getJPanelAkkord().repaint();
    	
    	if (song != null)
    	{
			if (takt != null)
			{
				getJLabelTeilNr().setText("" + song.getTeilNr());
				getJLabelTaktNr().setText("" + song.getTaktNr(false));
				getJLabelTaktNrKomplett().setText("" + song.getTaktNr(true));

				// taktBearbeitung für die Darstellung hier auf dem Panel und
				// taktSong als Zeiger auf den entsprechenden Takt im Song;
				// entsprechend für den aktuellen Akkord
				taktBearbeitung = takt.transientClone(null);
				taktSong = takt;
				
				if (takt.istLeererTakt())
				{
					// deselektieren der RadioButtons
//					resetTaktDaten(true);

					taktBearbeitung.setTeilung(Takt.TAKT_TEILUNG_OHNE);
					taktBearbeitung.resetRahmen();
					taktSong.setTeilung(Takt.TAKT_TEILUNG_OHNE);
					akkordBearbeitung = new Akkord(1);
					
					setzeAkkordMitVorherigenAkkord(akkordBearbeitung);
					
					akkordSong = akkordBearbeitung.transientClone();

					taktBearbeitung.setAkkord1(akkordBearbeitung);
					taktSong.setAkkord1(akkordSong);
					
					akkordBearbeitung.vorbereiteAkkord();
					akkordSong.vorbereiteAkkord();

					song.aktualisiereBlatt();
					song.setRahmenAlleSchemas();
					song.reloadTaktSchema();
				}
				else
				{
					if (istLetzterAkkordMarkiert)
					{
						akkordBearbeitung = taktBearbeitung.getLetztenAkkord();
						akkordSong = taktSong.getLetztenAkkord();
					}
					else
					{
						akkordBearbeitung = taktBearbeitung.getAkkord1();
						akkordSong = taktSong.getAkkord1();
					}
				}

				taktBearbeitung.resetAlleTaktMarkierungen();
				akkordBearbeitung.setMarkiertenRahmen();
				akkordBearbeitung.setInBearbeitung(true);
				akkordSong.setInBearbeitung(true);

				taktBearbeitung.setTaktSize(getJPanelAkkord().getWidth()-22);
				taktBearbeitung.setLocation(2, 1);
				taktBearbeitung.konstuiereTakt();
				taktBearbeitung.aktiviereAkkordMausMarkierung();

				getJPanelAkkord().add(taktBearbeitung);
				// setzen der RadioButtons
				setRadioButtonEnabled(true, true);
				setzeTonRadioButton();
				
				// Testausgabe
				testAusgabe(taktBearbeitung.getAkkord1());
				testAusgabe(taktBearbeitung.getAkkord2());
				testAusgabe(taktBearbeitung.getAkkord3());
				testAusgabe(taktBearbeitung.getAkkord4());
			}
    	}

    	istBeimTaktLaden = false;
	}
	
	/**
	 * @param akk
	 * Aufruf vom Mouseevent aus Akkord
	 */
	public void setMarkierungAktuellerAkkord(final Akkord akk)
	{
		if (istBeimTaktLaden)
			return;
		
		istBeimTaktLaden = true;
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				akkordBearbeitung = akk;
				akkordSong = akk.transientClone();
				taktSong.setAkkord(akkordSong);

				if (akkordBearbeitung.istAkkordNackt())
					resetTaktDaten(false);
				else
				{
					setRadioButtonEnabled(true,false);
					setzeTonRadioButton();
				}

				taktBearbeitung.removeAll();
				taktSong.removeAll();

				taktBearbeitung.konstuiereTakt();
				getJPanelAkkord().add(taktBearbeitung);
				PanelReiter3Takt.this.repaint();

				song.aktualisiereBlatt();
				song.setRahmenAlleSchemas();
				song.reloadTaktSchema();

				istBeimTaktLaden = false;
			}
		});
		
	}
	
	/**
	 * 
	 */
	public final KeyListener keyListener = new KeyListener() 
	{
		public void keyPressed(KeyEvent e) 
		{
			onKeyEvent(e);
		}
		public void keyTyped(KeyEvent e) 
		{
		}
		public void keyReleased(KeyEvent e) 
		{
		}
	};

}
