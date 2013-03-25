/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 15.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Dialog, in dem der Song bzw. eine Menge markierter Takte 
 * transponiert werden kann.
 * 
 * 
 */

package AChord.Tools;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import AChord.AClasses.ADialog;
import AChord.AClasses.ALabel;
import AChord.MainFrame.AChordFrame;
import AChord.Schema.Akkord;
import AChord.Schema.Song;
import AChord.Schema.Takt;
import AChord.Schema.TaktSchemaTeil;


public class DialogTansponieren extends ADialog
{
    private final static long serialVersionUID = 1;

    Window parent;
	private JButton jButtonDurchfuehren;
	private JButton jButtonReset;
	private JButton jButtonCancel;
	private ALabel aLabelHinweis;
	private ALabel aLabelMeldung;
	private JSlider jSliderTrans;
	private ALabel aLabelAkkordHinweis;
	
	private Song song;
	private Song oldSong;
	
	// der Akkord, der im Dialog dargestellt wird
	private Akkord anzeigeAkkord;
	// Original-Ton
	private int anzeigeOriginalTon = 0;
	
	Vector markierteTakte = null;

	/**
	 * Konstruktor
	 * @param parent
	 * @param title
	 * @param modal
	 */
	public DialogTansponieren(AChordFrame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		this.parent = parent;
		construct();
	}

	/**
	 * Konstruktor
	 * @param parent
	 * @param title
	 * @param modal
	 */
	public DialogTansponieren(DialogNotenPultAnsicht parent, String title, boolean modal)
	{
		super(parent, title, modal);
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
			initForm();
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
		this.setSize(344, 260);
		this.setLocation(parent.getX()+150, parent.getY()+200);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.getContentPane().add(getSliderTrans());
		getContentPane().add(getALabelHinweis());
		getContentPane().add(getALabelMeldung());
		getContentPane().add(getALabelAkkordHinweis());
		getContentPane().add(getJButtonReset());
		getContentPane().add(getJButtonCancel());
		this.getContentPane().add(getJButtonDurchfuehren());
	}
	
	/**
	 * Slider, mit dem bestimmt werden, wieviele Halbtöne nach oben
	 * oder nach unten verschoben werden soll
	 * @return
	 */
	private JSlider getSliderTrans()
	{
		if (jSliderTrans == null)
		{
			jSliderTrans = new JSlider();
			getContentPane().add(jSliderTrans);
			jSliderTrans.setBounds(31, 50, 274, 60);
			jSliderTrans.setMinimum(-6);
			jSliderTrans.setMaximum(6);
			jSliderTrans.setMinorTickSpacing(1);
			jSliderTrans.setMajorTickSpacing(1);
			jSliderTrans.setValue(0);
			jSliderTrans.setPaintLabels(true);
			jSliderTrans.setPaintTicks(true);
			jSliderTrans.setSnapToTicks(true);
			jSliderTrans.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e) 
				{
					onSliderGeschoben();
				}
			});
			// ändern der Beschriftung des Sliders
//			Hashtable labelTable = new Hashtable();
//			labelTable.put(new Integer(0), new JLabel("F"));
//			labelTable.put(new Integer(1), new JLabel("#"));
//			labelTable.put(new Integer(2), new JLabel("G"));
//			jSliderTrans.setLabelTable(labelTable);
		}
		return jSliderTrans;
	}

	/**
	 * @return
	 */
	private ALabel getALabelHinweis()
	{
		if (aLabelHinweis == null)
		{
			aLabelHinweis = new ALabel();
			aLabelHinweis.setText("Verschieben um Halbtöne:");
			aLabelHinweis.setFont(getFontA());
			aLabelHinweis.setBounds(35, 25, 180, 25);
		}
		return aLabelHinweis;
	}

	/**
	 * Meldung für den Bediener
	 * @return
	 */
	private ALabel getALabelMeldung()
	{
		if (aLabelMeldung == null)
		{
			aLabelMeldung = new ALabel();
			aLabelMeldung.setText("");
			aLabelMeldung.setFont(getFontA());
			aLabelMeldung.setBounds(35, 110, 280, 25);
		}
		return aLabelMeldung;
	}

	/**
	 * @return
	 */
	private ALabel getALabelAkkordHinweis()
	{
		if (aLabelAkkordHinweis == null)
		{
			aLabelAkkordHinweis = new ALabel();
			aLabelAkkordHinweis.setText("1. Song-Akkord");
			aLabelAkkordHinweis.setFont(getFontSmall());
			aLabelAkkordHinweis.setBounds(240, 0, 120, 12);
		}
		return aLabelAkkordHinweis;
	}

	/**
	 * @return
	 */
	public JButton getJButtonDurchfuehren()
	{
		if (jButtonDurchfuehren == null)
		{
			jButtonDurchfuehren = new JButton();
			jButtonDurchfuehren.setText("Durchführen");
			jButtonDurchfuehren.setFont(getFontA());
			jButtonDurchfuehren.setBounds(37, 145, 127, 30);
			jButtonDurchfuehren.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonDurchfuehren();}});
		}
		return jButtonDurchfuehren;
	}
	
	/**
	 * Rücksetzen auf die Werte, die beim Starten des Dialoges vorlagen
	 * @return
	 */
	private JButton getJButtonReset()
	{
		if (jButtonReset == null)
		{
			jButtonReset = new JButton();
			jButtonReset.setText("Zurücksetzen");
			jButtonReset.setFont(getFontA());
			jButtonReset.setBounds(180, 145, 127, 30);
			jButtonReset.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonReset(evt);}});
		}
		return jButtonReset;
	}
	
	/**
	 * @return
	 */
	private JButton getJButtonCancel()
	{
		if (jButtonCancel == null)
		{
			jButtonCancel = new JButton();
			jButtonCancel.setText("Ok");
			jButtonCancel.setFont(getFontA());
			jButtonCancel.setBounds(180, 190, 127, 30);
			jButtonCancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonCancel(evt);}});
		}
		return jButtonCancel;
	}
	
	
	/**
	 * 
	 */
	private void onSliderGeschoben()
	{
		melde("");
		
		if (anzeigeAkkord != null)
			anzeigeAkkord.setAkkTon(anzeigeOriginalTon);
		transponiereAkkord(anzeigeAkkord, getSliderTrans().getValue());
		handleAnzeigeAkkord();
	}
	
	/**
	 * 
	 */
	private void onButtonDurchfuehren()
	{
		if (getSliderTrans().getValue() == 0)
		{
			melde("Bitte einen Wert ungleich 0 wählen.");
			return;
		}
		melde("");
		
		if (anzeigeAkkord != null)
			anzeigeAkkord.setAkkTon(anzeigeOriginalTon);
		transponiereAkkord(anzeigeAkkord, getSliderTrans().getValue());
		handleAnzeigeAkkord();
		anzeigeOriginalTon = getTonVonAnzeigeAkkord();
		
		if (this.markierteTakte == null)
		{
			// berechnen der Akkorde des gesamten Titels und
			// übernehmen der neuen Einstellung in die aktuelle Anzeige
			transponiereAkkord(song.getSchema().getAkkTonArtSong(), getSliderTrans().getValue());
	    	for (int i=0; i<song.getSchema().getTaktSchemaTeile().size(); i++)
	    	{
	    		TaktSchemaTeil teil = (TaktSchemaTeil)song.getSchema().getTaktSchemaTeile().elementAt(i);
	    		for (int j=0; j<teil.getAlleTakte().size(); j++)
	    		{
	    			Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
	    			if (takt.getAkkord1() != null)
	    				transponiereAkkord(takt.getAkkord1(), getSliderTrans().getValue());
	    			if (takt.getAkkord2() != null)
	    				transponiereAkkord(takt.getAkkord2(), getSliderTrans().getValue());
	    			if (takt.getAkkord3() != null)
	    				transponiereAkkord(takt.getAkkord3(), getSliderTrans().getValue());
	    			if (takt.getAkkord4() != null)
	    				transponiereAkkord(takt.getAkkord4(), getSliderTrans().getValue());
	    		}
	    	}
		}
		else
		{
			// berechnen der markierten Akkorde und
			// übernehmen der neuen Einstellung in die aktuelle Anzeige
    		for (int j=0; j<this.markierteTakte.size(); j++)
    		{
    			Takt takt = (Takt)markierteTakte.elementAt(j);
    			if (takt.getAkkord1() != null)
    				transponiereAkkord(takt.getAkkord1(), getSliderTrans().getValue());
    			if (takt.getAkkord2() != null)
    				transponiereAkkord(takt.getAkkord2(), getSliderTrans().getValue());
    			if (takt.getAkkord3() != null)
    				transponiereAkkord(takt.getAkkord3(), getSliderTrans().getValue());
    			if (takt.getAkkord4() != null)
    				transponiereAkkord(takt.getAkkord4(), getSliderTrans().getValue());
    		}
		}
		
		song.setRahmenAlleSchemas();
		if (parent instanceof AChordFrame)
		{
			song.konstruierBlatt(true);
			AChordFrame ac = (AChordFrame)parent;
			ac.getJPanelTakte().setTaktDaten(song.setMarkierungErsterTakt(), false);
			ac.getJPanelTitel().setAltSong();
			ac.repaint();
		}
		else if (parent instanceof DialogNotenPultAnsicht)
		{
			song.konstruierBlatt(false);
			DialogNotenPultAnsicht no = (DialogNotenPultAnsicht)parent;
			no.repaint();
		}
		
		song.setMarkierungFuerMarkierteTakte(markierteTakte);

		getSliderTrans().setValue(0);
	}

	/**
	 * @param evt
	 */
	private void onButtonReset(ActionEvent evt)
	{
		anzeigeOriginalTon = oldSong.getFirstTakt().getAkkord1().getAkkTon();
		if (anzeigeAkkord != null)
			anzeigeAkkord.setAkkTon(anzeigeOriginalTon);
		transponiereAkkord(anzeigeAkkord, 0);
		handleAnzeigeAkkord();
		
		ersetzeAkkord(song.getSchema().getAkkTonArtSong(), oldSong.getSchema().getAkkTonArtSong());

		int countTaktNr = -1;
		for (int i=0; i<song.getSchema().getTaktSchemaTeile().size(); i++)
    	{
    		TaktSchemaTeil teil = (TaktSchemaTeil)song.getSchema().getTaktSchemaTeile().elementAt(i);
    		TaktSchemaTeil oldteil = (TaktSchemaTeil)oldSong.getSchema().getTaktSchemaTeile().elementAt(i);
    		for (int j=0; j<teil.getAlleTakte().size(); j++)
    		{
    			countTaktNr++;
    			Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
    			Takt oldtakt = (Takt)oldteil.getAlleTakte().elementAt(j);
    			if (takt.getAkkord1() != null)
    				ersetzeAkkord(takt.getAkkord1(), oldtakt.getAkkord1());
    			if (takt.getAkkord2() != null)
    				ersetzeAkkord(takt.getAkkord2(), oldtakt.getAkkord2());
    			if (takt.getAkkord3() != null)
    				ersetzeAkkord(takt.getAkkord3(), oldtakt.getAkkord3());
    			if (takt.getAkkord4() != null)
    				ersetzeAkkord(takt.getAkkord4(), oldtakt.getAkkord4());
    		}
    	}
		
		song.setRahmenAlleSchemas();
		if (parent instanceof AChordFrame)
		{
			song.konstruierBlatt(true);
			AChordFrame ac = (AChordFrame)parent;
			ac.getJPanelTakte().setTaktDaten(song.setMarkierungErsterTakt(), false);
			ac.getJPanelTitel().setAltSong();
			ac.repaint();
		}
		else if (parent instanceof DialogNotenPultAnsicht)
		{
			song.konstruierBlatt(false);
			DialogNotenPultAnsicht no = (DialogNotenPultAnsicht)parent;
			no.repaint();
		}
		
		song.setMarkierungFuerMarkierteTakte(markierteTakte);

		getSliderTrans().setValue(0);

		melde("");
	}

	/**
	 * @param evt
	 */
	private void onButtonCancel(ActionEvent evt)
	{
		close();
	}
	
	/* (non-Javadoc)
	 * @see AChord.AClasses.ADialog#close()
	 */
	public void close() 
	{
		song.resetIstTransponiereDialogGeladen();
		super.close();
	}
	
	/**
	 * setzen der Daten von außen;
	 * hier: kompletter Song soll transponiert werden
	 * @param song
	 */
	public void setDaten(Song song)
	{
		this.markierteTakte = null;
		this.song = song;
		this.oldSong = song.transientClone();
		if (song.getFirstTakt().getAkkord1() != null)
			this.anzeigeAkkord = song.getFirstTakt().getAkkord1().transientClone();
		this.anzeigeOriginalTon = getTonVonAnzeigeAkkord();
		getALabelAkkordHinweis().setText("1. Song-Akkord");
		getALabelAkkordHinweis().setLocation(240, 0);
		initAnzeigeAkkord();
		handleAnzeigeAkkord();
	}

	/**
	 * setzen der Daten von außen;
	 * hier: markierte Takte sollen transponiert werden
	 * @param song
	 */
	public void setDaten(Song song, Vector markierteTakte)
	{
		this.markierteTakte = markierteTakte;
		this.song = song;
		this.oldSong = song.transientClone();
		if (((Takt)markierteTakte.elementAt(0)).getAkkord1() != null)
			this.anzeigeAkkord = ((Takt)markierteTakte.elementAt(0)).getAkkord1().transientClone();
		this.anzeigeOriginalTon = getTonVonAnzeigeAkkord();
		getALabelAkkordHinweis().setText("1. markierter Akkord");
		getALabelAkkordHinweis().setLocation(230, 0);
		initAnzeigeAkkord();
		handleAnzeigeAkkord();
	}
	
	/**
	 * erstmaliges Initialisieren des Layouts des Akkordes, der
	 * als Referenz angezeigt wird
	 */
	private void initAnzeigeAkkord() 
	{
		if (anzeigeAkkord == null)
			return;
		anzeigeAkkord.setAkkordSize(45);
		anzeigeAkkord.setLocation(275, 14);
		this.add(anzeigeAkkord);
		this.repaint();
	}

	/**
	 * refresh des Referenz-Anzeige-Akkords
	 */
	private void handleAnzeigeAkkord()
	{
		if (anzeigeAkkord == null)
			return;
		this.repaint();
	}
	
	/**
	 * @param akk
	 * @param oldakk
	 */
	private void ersetzeAkkord(Akkord akk, Akkord oldakk)
	{
		akk.setAkkTon(oldakk.getAkkTon());
		akk.setAkkHalbton(oldakk.getAkkHalbton());
		akk.vorbereiteAkkord();
	}
	
	/**
	 * bestimmt den Ton (int) aus dem Anzeige-Akkord (wenn denn vorhanden...)
	 */
	private int getTonVonAnzeigeAkkord()
	{
		int ret = 0;
		if (anzeigeAkkord != null)
			ret = anzeigeAkkord.getAkkTon();
		return ret;
	}
	
	/**
	 * @param akk
	 * @param halbeToene
	 */
	public static void transponiereAkkord(Akkord akk, int halbeToene)
	{
		if (akk == null)
			return;
		int neuerTon = akk.getAkkTon() + halbeToene;
		if (neuerTon < 1)
			neuerTon = 12 + neuerTon;
		else if (neuerTon > 12)
			neuerTon = neuerTon - 12;
		
		akk.setAkkTon(neuerTon);
		if (akk.istHalbTon())
			akk.setAkkHalbton(1);
		else
			akk.setAkkHalbton(0);
		akk.vorbereiteAkkord();
	}
	
	/**
	 * @param st
	 */
	private void melde(String st)
	{
		getALabelMeldung().setText(st);
	}
}
