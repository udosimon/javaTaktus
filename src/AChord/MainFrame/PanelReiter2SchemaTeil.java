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
 * Ein einzelner Teil er im Hauptframe auf Reiter 2 (Schema-Teile) dargestellten
 * SOngteile
 */
package AChord.MainFrame;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;


import AChord.AClasses.AIconManager;
import AChord.AClasses.ALengthLimitedDocument;
import AChord.AClasses.APanel;
import AChord.Schema.TaktSchemaTeil;


public class PanelReiter2SchemaTeil extends APanel
{
    private final static long serialVersionUID = 1;

	TaktSchemaTeil teil;
	private JLabel jLabelName;
	private JTextField jTextFieldName;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JCheckBox jCheckBoxKopfTeil;
	private JLabel jLabel3;
	private JCheckBox jCheckBoxWiederZ;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JFormattedTextField ftfAnzTakte;
	private JFormattedTextField ftfAnzKastenTakte;
	private JFormattedTextField ftfAnzKopfTakt;
	private JSpinner jSpinnerAnzKTakte;
	private JSpinner jSpinnerKopfTakt;
	private JButton jButtonNeuOben;
	private JButton jButtonNeuUnten;
	private JButton jButtonDel;
	private JButton jButtonLeer;
	
	int teilIndex;
	boolean istLadePhase = false;

	int oldTakteAnz = 8;
	int oldKTakteAnz = 2;

	/**
	 * Konstruktor
	 */
	public PanelReiter2SchemaTeil()
	{
		super();
		construct();
	}

	/**
	 * Konstruktor
	 * @param teilIndex
	 * @param teil
	 */
	public PanelReiter2SchemaTeil(int teilIndex, TaktSchemaTeil teil)
	{
		super();
		this.teil = teil;
		this.teilIndex = teilIndex;
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
	 * Aufbau der Oberfläche
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		this.setLayout(null);
		this.setSize(470, 90);
		this.setLocation(10, 3 + ((this.getHeight() + 2) * this.teilIndex));
		this.setBorder(new LineBorder(Color.BLACK));
		{
			jLabelName = new JLabel();
			this.add(jLabelName);
			jLabelName.setText("Teilname:");
			jLabelName.setFont(getFontA());
			jLabelName.setBounds(6, 6, 66, 14);
		}
		{
			jTextFieldName = new JTextField();
			this.add(jTextFieldName);
			jTextFieldName.setFont(getFontA());
			jTextFieldName.setBounds(97, 3, 68, 21);
			PlainDocument docTeilName = new PlainDocument();
			jTextFieldName.setDocument(docTeilName);
			docTeilName.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleDocTeilName(); }
				public void removeUpdate(DocumentEvent e) { handleDocTeilName(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		{
			jLabel1 = new JLabel();
			this.add(jLabel1);
			jLabel1.setText("Anzahl Takte:");
			jLabel1.setFont(getFontA());
			jLabel1.setBounds(180, 6, 89, 14);
		}
		{
			jLabel2 = new JLabel();
			this.add(jLabel2);
			jLabel2.setText("Wiederholungszeichen:");
			jLabel2.setFont(getFontA());
			jLabel2.setBounds(180, 35, 142, 14);
		}
		{
			jCheckBoxWiederZ = new JCheckBox();
			this.add(jCheckBoxWiederZ);
			jCheckBoxWiederZ.setBounds(317, 35, 16, 15);
			jCheckBoxWiederZ.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					onCheckBoxWiederholungZ();
				}
			});
		}
		{
			jLabel3 = new JLabel();
			this.add(jLabel3);
			jLabel3.setText("Anzahl Kastentakte:");
			jLabel3.setFont(getFontA());
			jLabel3.setBounds(180, 62, 121, 14);
		}
		{
			jLabel4 = new JLabel();
			this.add(jLabel4);
			jLabel4.setText("Kopfteil:");
			jLabel4.setFont(getFontA());
			jLabel4.setBounds(8, 35, 57, 14);
		}
		{
			jCheckBoxKopfTeil = new JCheckBox();
			this.add(jCheckBoxKopfTeil);
			jCheckBoxKopfTeil.setBounds(93, 35, 17, 15);
			jCheckBoxKopfTeil.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					onCheckBoxKopfTeil();
				}
			});
		}
		{
			jLabel5 = new JLabel();
			this.add(jLabel5);
			jLabel5.setText("Takt mit Kopf:");
			jLabel5.setFont(getFontA());
			jLabel5.setBounds(5, 62, 121, 14);
		}
		{
			jButtonNeuOben = new JButton();
			this.add(jButtonNeuOben);
			jButtonNeuOben.setFont(getFontB());
			jButtonNeuOben.setMargin(new Insets(2,2,2,2));
			jButtonNeuOben.setIcon(AIconManager.ICON_ARROW_UP);
			jButtonNeuOben.setText("Neu");
			jButtonNeuOben.setToolTipText("Teil oberhalb hinzufügen und kopieren der Inhalte");
			jButtonNeuOben.setBounds(375, 5, 70, 22);
			addButtonListenerNeuOben(jButtonNeuOben);
		}
		{
			jButtonNeuUnten = new JButton();
			this.add(jButtonNeuUnten);
			jButtonNeuUnten.setFont(getFontB());
			jButtonNeuUnten.setMargin(new Insets(2,2,2,2));
			jButtonNeuUnten.setIcon(AIconManager.ICON_ARROW_DOWN);
			jButtonNeuUnten.setText("Neu");
			jButtonNeuUnten.setToolTipText("Teil unterhalb hinzufügen und kopieren der Inhalte");
			jButtonNeuUnten.setBounds(375, 63, 70, 22);
			addButtonListenerNeuUnten(jButtonNeuUnten);
		}
		{
			jButtonDel = new JButton();
			this.add(jButtonDel);
			jButtonDel.setFont(getFontB());
			jButtonDel.setMargin(new Insets(2,2,2,2));
			jButtonDel.setText("L\u00F6schen");
			jButtonDel.setToolTipText("Löschen des Teils");
			jButtonDel.setBounds(405, 34, 60, 22);
			addButtonListenerDel(jButtonDel);
		}
		{
			jButtonLeer = new JButton();
			this.add(jButtonLeer);
			jButtonLeer.setFont(getFontB());
			jButtonLeer.setMargin(new Insets(2,2,2,2));
			jButtonLeer.setText("Leeren");
			jButtonLeer.setToolTipText("Leeren aller Takte dieses Teils");
			jButtonLeer.setBounds(355, 34, 50, 22);
			addButtonListenerLeer(jButtonLeer);
		}
		{
			SpinnerNumberModel jSpinnerAnzTakteModel = 
								new SpinnerNumberModel(4,
												TaktSchemaTeil.TEIL_MIN_ANZAHLTAKTE,
												TaktSchemaTeil.TEIL_MAX_ANZAHLTAKTE,
												1);
			JSpinner jSpinnerAnzTakte = new JSpinner();
			this.add(jSpinnerAnzTakte);
			jSpinnerAnzTakte.setFont(getFontA());
			jSpinnerAnzTakte.setModel(jSpinnerAnzTakteModel);
			jSpinnerAnzTakte.setBounds(298, 3, 40, 21);
			JComponent defEditor = jSpinnerAnzTakte.getEditor()  ;
			ftfAnzTakte = ((JSpinner.DefaultEditor)defEditor).getTextField() ;
			ALengthLimitedDocument docSpinnerAnzTakte = new ALengthLimitedDocument(2);
			ftfAnzTakte.setDocument(docSpinnerAnzTakte);
			docSpinnerAnzTakte.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {handleDocAnzTakte(); }
				public void removeUpdate(DocumentEvent e) {  }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		{
			SpinnerNumberModel jSpinnerAnzKTakteModel = 
								new SpinnerNumberModel(2,
												TaktSchemaTeil.TEIL_MIN_ANZAHLKASTENTAKTE,
												TaktSchemaTeil.TEIL_MAX_ANZAHLKASTENTAKTE,
												1);
			jSpinnerAnzKTakte = new JSpinner();
			this.add(jSpinnerAnzKTakte);
			jSpinnerAnzKTakte.setFont(getFontA());
			jSpinnerAnzKTakte.setModel(jSpinnerAnzKTakteModel);
			jSpinnerAnzKTakte.setBounds(298, 59, 40, 21);
			JComponent defEditor = jSpinnerAnzKTakte.getEditor()  ;
			ftfAnzKastenTakte = ((JSpinner.DefaultEditor)defEditor).getTextField() ;
			ALengthLimitedDocument docSpinnerAnzKTakte = new ALengthLimitedDocument(2);
			ftfAnzKastenTakte.setDocument(docSpinnerAnzKTakte);
			docSpinnerAnzKTakte.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleDocAnzKastenTakte(); }
				public void removeUpdate(DocumentEvent e) { }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		{
			SpinnerNumberModel jSpinnerKopfTaktModel = 
								new SpinnerNumberModel(0,
												0,
												TaktSchemaTeil.TEIL_MAX_ANZAHLTAKTE,
												1);
			jSpinnerKopfTakt = new JSpinner();
			this.add(jSpinnerKopfTakt);
			jSpinnerKopfTakt.setFont(getFontA());
			jSpinnerKopfTakt.setModel(jSpinnerKopfTaktModel);
			jSpinnerKopfTakt.setBounds(97, 59, 40, 21);
			JComponent defEditor = jSpinnerKopfTakt.getEditor()  ;
			ftfAnzKopfTakt = ((JSpinner.DefaultEditor)defEditor).getTextField() ;
			ALengthLimitedDocument docSpinnerKopfTakt = new ALengthLimitedDocument(2);
			ftfAnzKopfTakt.setDocument(docSpinnerKopfTakt);
			docSpinnerKopfTakt.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleDocKopfTakt(); }
				public void removeUpdate(DocumentEvent e) { }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
	}
	
	/**
	 *  weitere Initialisierungen aus Konstruktor
	 */
	private void myInit()
	{
		this.jTextFieldName.setText(teil.getTeilName());
		this.ftfAnzTakte.setText(""+teil.getTaktAnzahl());
		
		this.ftfAnzKastenTakte.setText(""+teil.getAnzahlTakteKasten());
		this.jCheckBoxWiederZ.setSelected(teil.istWiederholung());
		if (! this.jCheckBoxWiederZ.isSelected())
		{
			this.ftfAnzKastenTakte.setEnabled(false);
			this.jSpinnerAnzKTakte.setEnabled(false);
		}
		
		this.ftfAnzKopfTakt.setText(""+teil.getKopfTakt());
	}

	/**
	 * Die Änderung des Teil-Namens wird im Song dargestellt
	 */
	private void handleDocTeilName()
	{
		if (istLadePhase)
			return;
		teil.setTeilName(jTextFieldName.getText().trim());
		teil.konstruiereTeil();
	}

	/**
	 * Eine Änderung der Gesamtanzahl der Takte im Teil führt zu
	 * einer Änderung der Darstellung des Songes.
	 */
	private void handleDocAnzTakte()
	{
		if (istLadePhase)
			return;

		int anz;
		
		if (ftfAnzTakte.getText().trim().length() == 0)
			anz = TaktSchemaTeil.TEIL_MIN_ANZAHLTAKTE;
		else
		{
			try	{
				anz = new Integer(ftfAnzTakte.getText()).intValue();
			} catch (Exception e) {
				anz = oldTakteAnz;
			}
		}
		if (anz < TaktSchemaTeil.TEIL_MIN_ANZAHLTAKTE)
			anz = TaktSchemaTeil.TEIL_MIN_ANZAHLTAKTE;
		else if (anz > TaktSchemaTeil.TEIL_MAX_ANZAHLTAKTE)
			anz = TaktSchemaTeil.TEIL_MAX_ANZAHLTAKTE;

		final int anzftf = anz;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				istLadePhase = true;
				ftfAnzTakte.setText(""+anzftf);
				istLadePhase = false;
			}
		});
		
		oldTakteAnz = anz;

		teil.setTaktAnzahl(anz);
		teil.resetInitTeilTakte();
		teil.trenneAbKastenTakte();
		teil.konstruiereTeil();
		if (this.getParent() instanceof PanelReiter2Schema)
			((PanelReiter2Schema)this.getParent()).resetSong();
	}

	/**
	 * Eine Änderung der Anzahl der Kastentakte führt zu einer Änderung
	 * der Darstellung des Songes
	 */
	private void handleDocAnzKastenTakte()
	{
		if (istLadePhase)
			return;

		int anz;
		
		if (ftfAnzKastenTakte.getText().trim().length() == 0)
			anz = TaktSchemaTeil.TEIL_MIN_ANZAHLTAKTE;
		else
		{
			try	{
				anz = new Integer(ftfAnzKastenTakte.getText()).intValue();
			} catch (Exception e) {
				anz = oldKTakteAnz;
			}
		}
		
		// hiermit wird gewährleisetet, dass die Anzahl der Kastentakte nicht
		// größer werden darf, als die Anzahl der Takte in der letzten Zeile
		// des Teiles
		int maxAnzahlKastenTakte = Math.min(teil.bestimmeAnzahlTakteLetzteZeile(), 
											TaktSchemaTeil.TEIL_MAX_ANZAHLKASTENTAKTE);
		if (anz < TaktSchemaTeil.TEIL_MIN_ANZAHLKASTENTAKTE)
			anz = TaktSchemaTeil.TEIL_MIN_ANZAHLKASTENTAKTE;
		else if (anz > maxAnzahlKastenTakte)
			anz = maxAnzahlKastenTakte;

		final int anzftf = anz;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				istLadePhase = true;
				ftfAnzKastenTakte.setText(""+anzftf);
				istLadePhase = false;
			}
		});

		oldKTakteAnz = anz;
		
		teil.setAnzahlTakteKasten(anz);
		teil.resetInitTeilKasten();
		teil.konstruiereTeil();
		if (this.getParent() instanceof PanelReiter2Schema)
			((PanelReiter2Schema)this.getParent()).resetSong();
	}
	
	/**
	 * eine Änderung der Nummer des Kopftaktes führt zu einer Umpositionierung
	 * des Kopf-Symbols innerhalb der Songdarstellung 
	 */
	private void handleDocKopfTakt()
	{
		if (istLadePhase)
			return;

		int taktNr;
		
		if (ftfAnzKopfTakt.getText().trim().length() == 0)
			taktNr = 0;
		else
		{
			try	{
				taktNr = new Integer(ftfAnzKopfTakt.getText()).intValue();
			} catch (Exception e) {
				taktNr = 0;
			}
		}
		if (taktNr < 0)
			taktNr = 0;
		else if (taktNr > (teil.getTaktAnzahl() + teil.getAnzahlTakteKasten()))
			taktNr = teil.getTaktAnzahl() + teil.getAnzahlTakteKasten();

		final int anzftf = taktNr;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				istLadePhase = true;
				ftfAnzKopfTakt.setText(""+anzftf);
				istLadePhase = false;
			}
		});
		
		teil.setKopfTakt(taktNr);
		teil.konstruiereTeil();
		if (this.getParent() instanceof PanelReiter2Schema)
			((PanelReiter2Schema)this.getParent()).resetSong();
	}
	
	/**
	 * Reaktion auf Click Checkbox Wiederholungszeichen: am Beginn und Ende des 
	 * Teils wird ein Wiederholungszeichen dargestellt.
	 */
	private void onCheckBoxWiederholungZ()
	{
		if (istLadePhase)
			return;
		teil.setIstWiederholung(jCheckBoxWiederZ.isSelected());
		if (! jCheckBoxWiederZ.isSelected())
		{
			ftfAnzKastenTakte.setText("0");
			ftfAnzKastenTakte.setEnabled(false);
			jSpinnerAnzKTakte.setEnabled(false);
			teil.setAnzahlTakteKasten(0);
		}
		else
		{
			ftfAnzKastenTakte.setEnabled(true);
			jSpinnerAnzKTakte.setEnabled(true);
		}
		teil.resetInitTeilWZeichen();
		teil.konstruiereTeil();
		if (this.getParent() instanceof PanelReiter2Schema)
			((PanelReiter2Schema)this.getParent()).resetSong();
			
	}
	
	/**
	 * Reaktion auf Click Checkbox Kopf: der Name des Teiles wird 
	 * durch das Kopf-Symbol ersetzt
	 */
	private void onCheckBoxKopfTeil()
	{
		if (istLadePhase)
			return;
		
		if (jCheckBoxKopfTeil.isSelected())
		{
			ftfAnzKopfTakt.setText("0");
			jTextFieldName.setEnabled(false);
			jSpinnerKopfTakt.setEnabled(false);
			teil.setIstKopfTeil(true);
		}
		else
		{
			jTextFieldName.setEnabled(true);
			jSpinnerKopfTakt.setEnabled(true);
			teil.setIstKopfTeil(false);
		}
		teil.konstruiereTeil();
		if (this.getParent() instanceof PanelReiter2Schema)
			((PanelReiter2Schema)this.getParent()).resetSong();
	}

	/**
	 * @param b
	 */
	private void addButtonListenerNeuOben(JButton b)
    {
        b.addActionListener(new ActionListener() 
        { 
            public void actionPerformed(ActionEvent ae) 
            { 
            	onButton(1);
            }
        });
    }
	/**
	 * @param b
	 */
	private void addButtonListenerNeuUnten(JButton b)
    {
        b.addActionListener(new ActionListener() 
        { 
            public void actionPerformed(ActionEvent ae) 
            { 
            	onButton(2);
            }
        });
    }
	/**
	 * @param b
	 */
	private void addButtonListenerDel(JButton b)
    {
        b.addActionListener(new ActionListener() 
        { 
            public void actionPerformed(ActionEvent ae) 
            { 
            	onButton(3);
            }
        });
    }
	/**
	 * @param b
	 */
	private void addButtonListenerLeer(JButton b)
    {
        b.addActionListener(new ActionListener() 
        { 
            public void actionPerformed(ActionEvent ae) 
            { 
            	onButton(4);
            }
        });
    }
	
	/**
	 * Hier wird auf einen Click einer der Buttons eines Teils reagiert
	 * @param action
	 */
	private void onButton(int action)
	{
		// 1 == nach oben neu
		// 2 == nach unten neu
		if ((action == 1) || (action == 2))
		{
			TaktSchemaTeil teilNeu = this.teil.transientClone(null);
			if (this.getParent() instanceof PanelReiter2Schema)
				((PanelReiter2Schema)this.getParent()).neuerTeil(teilNeu, action, this.teilIndex);
		}
		// 3 == löschen
		else if (action == 3)
		{
			if (this.getParent() instanceof PanelReiter2Schema)
				((PanelReiter2Schema)this.getParent()).loescheTeil(this.teil, this.teilIndex);
		}
		// 4 == leeren aller Takte
		else if (action == 4)
		{
			teil.leereAlleTakte();
			teil.konstruiereTeil();
			if (this.getParent() instanceof PanelReiter2Schema)
				((PanelReiter2Schema)this.getParent()).resetSong();

			// dies war der erste Ansatz...
//			if (this.getParent().getParent().getParent().getParent().getParent().getParent() instanceof AChordFrame)
//				((AChordFrame)this.getParent().getParent().getParent().getParent().getParent().getParent()).setAkkordVonAussen();

			// dies scheint der vernünftige Ansatz zu sein:
			// getTopLevelAncestor entspricht "getTopLevelWindow"
			if (this.getTopLevelAncestor() instanceof AChordFrame)
				((AChordFrame)this.getTopLevelAncestor()).setAkkordVonAussen();
		}
		else
		{
			//nix
		}
	}

}
