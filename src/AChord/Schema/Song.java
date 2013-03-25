/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 10.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * Die Klasse Song enthält:
 *  - Details (Titel, Autor, ..)
 *  - das Schema mit den Takten/Akkorde (TaktSchema)
 *  - das TextSchema
 *  
 *  weitere Themen:
 *  - Behandlung des SplitPanes
 *  - Makrierung von Takten (inkl. PopupMenü und Rechteck malen)
 *  - Konstruieren des Songs (inkl. Schemateile, Takte, Akkorde)
 * 
 */
package AChord.Schema;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import AChord.AClasses.AIconManager;
import AChord.AClasses.ALabel;
import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;
import AChord.MainFrame.AChordFrame;
import AChord.Tools.DialogTansponieren;


public class Song extends APanel
{
    private final static long serialVersionUID = 1;

    private static final String NEWSONG_TITEL = "Titel";
	private static final String NEWSONG_AUTOR = "";
	private static final int NEWSONG_SCHEMANR = 1;
	private static final int NEWSONG_HEIGTHANTEIL_TEXT2 = 70;
	private static final int NEWSONG_HEIGTHANTEIL_TITEL = 6;
	private static final int NEWSONG_TONART = 6;
	private static final int NEWSONG_ANZAHL_TEILE = 4;
	private static final int NEWSONG_ANZAHL_TAKTE = 4;
	static final int FONTSIZE_TITEL = 20;
	static final int FONTSIZE_AUTOR = 14;
	static final double a4Verhaeltnis = 1.4143; // Verhältnis DinA4-Seite Höhe zu Breite
	static final double TITEL_OHNE_AUTOR = 0.65;
	static final double TITEL_MIT_AUTOR = 0.5;
	
	private static final int MIN_RECT_SIZE = 6;
	
	String titel = NEWSONG_TITEL;
	String autor = NEWSONG_TITEL;
	TaktSchema schema;
	TextSchema text;
	
	int heightVerhaeltnis;
	int widthVerhaeltnis;
	
	
	int titelHeightAnteil = 0;	// 0..100
	int titelGesamtHeight = 0;
	
	ALabel labelTitel = new ALabel();
	ALabel labelAutor = new ALabel();
	
	JSplitPane splitPane = new JSplitPane();
	
	boolean first = true;
	boolean inEventHandling = false;
	boolean istFuerDruck = false;
	boolean istSplit = true;
	int heightSchema;

	SongText songText;

	public Rectangle markRect;

	int merkeMarkiertenTakt = 0;
	Vector markierteTakte = new Vector();
	JPopupMenu popup;
	JMenuItem menuItemCopy;
	JMenuItem menuItemTransp;
	JMenuItem menuItemPaste;
	JMenuItem menuItemLeeren;
	
	/**
	 * Konstruktor
	 */
	public Song()
	{
		super();

		construct();
	}
	/**
	 * Konstruktor
	 * @param breitenV
	 * @param hoehenV
	 */
	public Song(int breitenV, int hoehenV)
	{
		super();
		widthVerhaeltnis = breitenV;
		heightVerhaeltnis = hoehenV;
		this.setSize(widthVerhaeltnis, heightVerhaeltnis);

		construct();
	}
	
	/**
	 * Initialisierungen des Frames
	 */
	private void construct()
	{
		schema = new TaktSchema(this);
		text = new TextSchema(0);
		markRect = new Rectangle(0,0,0,0);
	    this.addMouseMotionListener(new MyMouseMotionListener());
	    this.addMouseListener(new MyMouseListener());
	    
	    popup = new JPopupMenu("Markierte Takte");
	    popup.addPopupMenuListener(new PopupMenuListener(){
	    	public void popupMenuCanceled(PopupMenuEvent e){
//	    		System.out.println("++Canceled++");
	    		rectResetMarkierteComponenten(true);
	    		popup.setVisible(false);
	    	}
	    	public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
//	    		System.out.println("++Unsichtbar++");
	    		rectResetMarkierteComponenten(true);
	    	}
	    	public void popupMenuWillBecomeVisible(PopupMenuEvent e){
//	    		System.out.println("++Sichtbar++");
	    		merkeMarkiertenTakt = getMarkierteTaktNr();
	    	}
	    });
	    menuItemCopy = new JMenuItem("Kopieren der markierten Takte in Zwischenablage", AIconManager.ICON_COPY);
	    menuItemCopy.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent ae) { 
            	onKopiereTakte();}});
	    popup.add(menuItemCopy);
	    menuItemPaste = new JMenuItem("Einfügen der Takte aus Zwischenablage", AIconManager.ICON_PASTE);
	    menuItemPaste.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent ae) { 
            	onFuegeTakteEin();}});
	    popup.add(menuItemPaste);
	    popup.addSeparator();
	    menuItemLeeren = new JMenuItem("Leeren der markierten Takte", AIconManager.ICON_TRASH);
	    menuItemLeeren.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent ae) { 
            	onLeereTakte();}});
	    popup.add(menuItemLeeren);
	    popup.addSeparator();
	    menuItemTransp = new JMenuItem("Transponieren der markierten Takte",AIconManager.ICON_TRANSP);
	    menuItemTransp.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent ae) { 
            	onTransponiereTakte();}});
	    popup.add(menuItemTransp);
	}

	/**
	 * Initialisierung der Klasse beim Start des Programms
	 */
	public void initNewSong()
	{
		this.setTitel(NEWSONG_TITEL);
		this.setAutor(NEWSONG_AUTOR);
		this.setTitelHeightAnteil(NEWSONG_HEIGTHANTEIL_TITEL);
		
		schema.setAnzahlTeile(NEWSONG_ANZAHL_TEILE);
		schema.setTakteProZeile(NEWSONG_ANZAHL_TAKTE);
		schema.setTonArt(NEWSONG_TONART);
//		schema.setHeightAnteil(NEWSONG_HEIGTHANTEIL_SCHEMA);
		
		schema.initSchema(NEWSONG_SCHEMANR, this);
		
//		text.setHeightAnteil(100 - NEWSONG_HEIGTHANTEIL_TITEL - NEWSONG_HEIGTHANTEIL_SCHEMA);
		text.initZeile();
		text.setTextAnzeige(TextSchema.TEXT_ANZEIGE_EINESEITE);
		text.setHeightAnteilZweiteSeite(NEWSONG_HEIGTHANTEIL_TEXT2);

	}
	
	/**
	 * @param breitenV
	 * @param hoehenV
	 */
	public void setSongSize(int breitenV, int hoehenV)
	{
		widthVerhaeltnis = breitenV;
		heightVerhaeltnis = hoehenV;
		this.setSize(widthVerhaeltnis, heightVerhaeltnis);
//		System.out.println("========GROESSE SONG:"+this.getWidth()+"/"+this.getHeight());
	}
	
	/**
	 * @param hoehe
	 */
	public void setSizeHoeheA4(int hoehe)
	{
		int breite = (int)(hoehe / a4Verhaeltnis);
		widthVerhaeltnis = breite;
		heightVerhaeltnis = hoehe;
		this.setSize(breite, hoehe);
	}
	
	/**
	 * @param breite
	 */
	public void setSizeBreiteA4(int breite)
	{
		int hoehe = (int)(breite * a4Verhaeltnis);
		widthVerhaeltnis = breite;
		heightVerhaeltnis = hoehe;
//		System.out.println("Song Masse:"+breite+"/"+hoehe+"/"+a4Verhaeltnis);
		this.setSize(breite, hoehe);
	}
	
	/**
	 * @param schema
	 */
	public void setSchema(TaktSchema schema)
	{
		this.schema = schema;
	}
	
	/**
	 * @return
	 */
	public TaktSchema getSchema()
	{
		return this.schema;
	}
	
	/**
	 * @param st
	 */
	public void setTitel(String st)
	{
		this.titel = st;
	}
	
	/**
	 * @return
	 */
	public String getTitel()
	{
		return this.titel;
	}

	/**
	 * @param st
	 */
	public void setAutor(String st)
	{
		this.autor = st;
	}
	
	/**
	 * @return
	 */
	public String getAutor()
	{
		return this.autor;
	}

	/**
	 * @param st
	 */
	public void setText(TextSchema txt)
	{
		this.text = txt;
	}
	
	/**
	 * @return
	 */
	public TextSchema getText()
	{
		return this.text;
	}
	
	/**
	 * @param anteil
	 */
	public void setTitelHeightAnteil(int anteil)
	{
		titelHeightAnteil = anteil;
	}
	
	/**
	 * @return
	 */
	public int getTitelHeightAnteil()
	{
		return this.titelHeightAnteil;
	}
	
	/**
	 * @return
	 */
	public Vector getTeile()
	{
		Vector retVec = new Vector();
		if (schema != null)
		{
			retVec = schema.getTaktSchemaTeile();
		}
		return retVec;
	}
	
	/**
	 * neues Darstellen des Songs
	 */
	public void aktualisiereBlatt()
	{
		int fonthoehe = (int)(titelGesamtHeight * TITEL_MIT_AUTOR);
		if (autor.trim().length() == 0)
		{
			fonthoehe = (int)(titelGesamtHeight * TITEL_OHNE_AUTOR);
		}

		// Titel
		berechneUndSetzeTitelGroesse(fonthoehe);

		// Titel-Tonart
		if ((this.getSchema().getAkkTonArtSong() != null) && 
			(this.getSchema().getAkkTonArtSong().isInBearbeitung()))
		{
			this.remove(this.getSchema().getAkkTonArtSong());
//			System.out.println("===Song Anzeige Akkord "+this.getSchema().getAkkTonArtSong());
			setzeAkkordTitelTonArt();
			this.add(this.getSchema().getAkkTonArtSong());
		}
		else
		{
//			System.out.println("===Song NICHT Anzeige Akkord "+this.getSchema().getAkkTonArtSong());
			this.remove(getSchema().getAkkTonArtSong());
			this.getSchema().getAkkTonArtSong().repaint();
		}

		//Autor
		if (autor.trim().length() > 0)
		{
			berechneUndSetzeAutorGroesse();
			this.add(labelAutor);
		}
		else
		{
			this.remove(labelAutor);
		}

		schema.konstruiereTaktSchema();

		if (text.istTextAufEsterSeite())
			text.konstruiereTextSchema();
	}

	/**
	 * 
	 */
	public void reloadTaktSchema()
	{
		if (istSplit)
		{
			splitPane.remove(schema);
			splitPane.setTopComponent(schema);
			splitPane.setDividerLocation(heightSchema);
		}
		else
		{
			this.remove(schema);
			this.add(schema);
		}
	}

	/**
	 * 
	 */
	public void reloadTextSchema()
	{
		if (getText().getTextAnzeige() == TextSchema.TEXT_ANZEIGE_EINESEITE)
		{
			getText().konstruiereTextSchema();
		}
		else
		{
			getText().removeZeilen();
		}
		getText().repaint();
	}
	
	
	/**
	 * 
	 */
	public void removeSchema()
	{
		this.remove(schema);
	}
	
	/**
	 * @return
	 */
	public boolean istWirdGeDruckt()
	{
		return (this.istFuerDruck);
	}
	
	/**
	 * 
	 */
	public void setWirdGedruckt()
	{
		this.istFuerDruck = true;
	}
	
	/**
	 * berechnen und darstellen der Song-Komponenten 
	 * @param isSplit
	 */
	public void konstruierBlatt(boolean isSplit)
	{
		this.istSplit =  isSplit;
		// Titel/Autor*****************************************************
		// Höhe des Titelbereiches
		titelGesamtHeight = (int)((double)this.heightVerhaeltnis / 100 * titelHeightAnteil);
		// Font des Titels
		int fonthoehe = (int)(titelGesamtHeight * TITEL_MIT_AUTOR);
		if (autor.trim().length() == 0)
		{
			fonthoehe = (int)(titelGesamtHeight * TITEL_OHNE_AUTOR);
		}

		// Titel
		berechneUndSetzeTitelGroesse(fonthoehe);
		this.add(labelTitel);
		
		// Titel-Tonart
		this.remove(getSchema().getAkkTonArtSong());
		if ((this.getSchema().getAkkTonArtSong() != null) &&
			(this.getSchema().getAkkTonArtSong().isInBearbeitung()))
		{
			setzeAkkordTitelTonArt();
			this.add(getSchema().getAkkTonArtSong());
		}

		//Autor
		if (autor.trim().length() > 0)
		{
			berechneUndSetzeAutorGroesse();
			this.add(labelAutor);
		}

		if (this.istSplit)
		{
			splitPane.setSize(this.getWidth(),this.getHeight()-titelGesamtHeight);
			splitPane.setLocation(0, titelGesamtHeight);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerSize(4);
			Color col = Color.BLUE;
			this.setSplitPaneDividerColor(splitPane, col);
			splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
			splitPane.setOpaque(false);
//	        splitPane.setOneTouchExpandable(true);
			splitPane.setContinuousLayout(true);
			this.add(splitPane);
		}

		// TaktSchema****************************************************************
		heightSchema = (int)((double)this.heightVerhaeltnis / 100 * schema.getHeightAnteil());
		schema.setTaktSchemaSize(this.widthVerhaeltnis, heightSchema);
		schema.setLocation(0, titelGesamtHeight);
		schema.setLayout(null);
		schema.konstruiereTaktSchema();

		if (this.istSplit)
		{
			splitPane.setTopComponent(schema);
			splitPane.setDividerLocation(heightSchema);
		}
		else
			this.add(schema);
		
		// Text*******************************************************************
		int heightText = (int)((double)this.heightVerhaeltnis / 100 * text.getHeightAnteil());
		text.setTextSchemaSize(this.widthVerhaeltnis, heightText);
		// Test, um Taktgröße zu erkennen
		text.setLayout(null);
		if (text.istTextAufEsterSeite())
			text.konstruiereTextSchema();

		if (this.istSplit)
		{
			text.setLocation(0, 0);
			splitPane.setBottomComponent(text);
			splitPane.setDividerLocation(heightSchema);
		}
		else
		{
			text.setLocation(0, heightSchema+titelGesamtHeight);
			this.add(text);
		}

//		System.out.println("konstruiereBlatt: "+this.getTitelHeightAnteil()+"/"+
//				this.getSchema().getHeightAnteil()+"/"+
//				this.getText().getHeightAnteil()+"///"+
//				titelGesamtHeight + "/"+
//				this.getSchema().getHeight()+"/"+
//				this.getText().getHeight());

		// Änderung nach 1.8 19.9.11
		// in Unix konnte der Split-Devider ausserhalb des sichtbaren
		// Bereiches rutschen; das wird hiermit unterbunden
		schema.setMinimumSize(new Dimension(10, 10));
		text.setMinimumSize(new Dimension(10, 10));

		if ((this.istSplit) && (first))
		{
			first = false;
			initSplitPane();
		}
	}
	
	/**
	 * darstellen der Tonart rechts neben dem Titel
	 */
	public void setzeAkkordTitelTonArt()
	{
		int akkSeite = titelGesamtHeight / 5 * 3; 
		getSchema().getAkkTonArtSong().setAkkordSize(akkSeite);
		getSchema().getAkkTonArtSong().setLocation(this.widthVerhaeltnis - akkSeite*2 - (this.widthVerhaeltnis / 70), 10);
//		getSchema().getAkkTonArtSong().setRahmen();
	}
	
	/**
	 * @param fonthoehe
	 */
	public void berechneUndSetzeTitelGroesse(int fonthoehe)
	{
		Font f = new Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD, fonthoehe);
		labelTitel.setFont(f);
		labelTitel.setText(titel);
		// Breite des Titels bestimmen
		FontMetrics fm = labelTitel.getFontMetrics(f);
		int widthTitel = fm.stringWidth(titel);
		int heightTitel = fm.getHeight();
		// wenn Titel zu breit, dann kleiner
		while ((widthTitel) > (int)(this.widthVerhaeltnis * 0.7))
		{
			fonthoehe = fonthoehe - 1;
			f = new Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD, fonthoehe);
			labelTitel.setFont(f);
			// Breite des Titels bestimmen
			fm = labelTitel.getFontMetrics(f);
			widthTitel = fm.stringWidth(titel);
			heightTitel = fm.getHeight();
		}
		
		labelTitel.setSize(widthTitel+15, heightTitel);
		labelTitel.setLocation((this.widthVerhaeltnis / 2) - (widthTitel / 2),(titelGesamtHeight/15));
	}
	
	
	/**
	 * 
	 */
	public void berechneUndSetzeAutorGroesse()
	{
		int fonthoehe;
		Font f;
		FontMetrics fm;
		int widthTitel;
		int heightTitel;
		fonthoehe = (titelGesamtHeight/2) - (titelGesamtHeight/5);
		f = new Font(Konfiguration.getInstance().getSchriftArt(),Font.PLAIN, fonthoehe);
		labelAutor.setFont(f);
		labelAutor.setText(this.autor);
		// Breite des Autors bestimmen
		fm = labelAutor.getFontMetrics(f);
		widthTitel = fm.stringWidth(autor);
		heightTitel = fm.getHeight();
		// wenn Titel zu breit, dann kleiner
		while ((widthTitel) > (int)this.widthVerhaeltnis*0.6)
		{
			fonthoehe = fonthoehe - 1;
			f = new Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD, fonthoehe);
			labelAutor.setFont(f);
			// Breite des Titels bestimmen
			fm = labelAutor.getFontMetrics(f);
			widthTitel = fm.stringWidth(autor);
			heightTitel = fm.getHeight();
		}
		labelAutor.setSize(widthTitel+4, heightTitel);
		labelAutor.setLocation((this.widthVerhaeltnis / 2) - (widthTitel / 2),
				((titelGesamtHeight / 2)+ (titelGesamtHeight/15)));
	}

	/**
	 * bestimmen der Teilnummer, die den markierten Takt enthält
	 * @return
	 */
	public int getTeilNr()
	{
		int teilNr = 0;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				if (takt.istMarkiertAlsGewaehlt())
//				if (takt.istMarkiertAlsGewaehlt)
				{
					teilNr = i+1;
				}
			}
		}
		return teilNr;
	}
	
	/**
	 * bestimmt den ersten Takt des Songs
	 * @return
	 */
	public Takt getFirstTakt()
	{
		TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(0);
		Takt takt = (Takt)teil.getAlleTakte().elementAt(0);
		return takt;
	}
	
	/**
	 * bestimmern der Nummer des markierten Taktes im Teil 
	 * (istKomplett == false) bzw. der Takt-Nummer des kompletten 
	 * Songs (istKomplett == true)
	 * @param istKomplett
	 * @return
	 */
	public int getTaktNr(boolean istKomplett)
	{
		int taktNr = 0;
		int taktCount = 0;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				if (takt.istMarkiertAlsGewaehlt())
//				if (takt.istMarkiertAlsGewaehlt)
				{
					if (istKomplett)
						taktNr = taktCount+1;
					else
						taktNr = j+1;
				}
				taktCount++;
			}
		}
		return taktNr;
	}
	
	/**
	 * bestimmt die Nummer des des temporär markierten Taktes (gellblauer Cursor)
	 * im gesamten Song
	 * @return
	 */
	public int getMarkierteTaktNr()
	{
		int taktNr = -1;
		int taktCount = -1;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				taktCount++;
				if (takt.istTemporaerMarkiert())
				{
					taktNr = taktCount;
				}
			}
		}
//		System.out.println("-------****"+taktNr);
		return taktNr;
	}

	/**
	 * wenn von aussen der erste Takt des Titels markiert werden soll
	 * @return
	 */
	public Takt setMarkierungErsterTakt()
	{
		this.resetAlleTaktMarkierungen();
		TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(0);
		Takt takt = (Takt)teil.getAlleTakte().elementAt(0);
		takt.setMarkiertenRahmen();
		takt.resetMausRahmen();
		return takt;
	}
	
	/**
	 * gibt den Takt zurück, der markiert ist (dunkelblau)
	 * @return
	 */
	public Takt getAktuellMarkiertenTakt()
	{
		Takt takt = null;
		Takt returnTakt = null;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				takt = (Takt)teil.getAlleTakte().elementAt(j);
				if (takt.istMarkiertAlsGewaehlt())
//				if (takt.istMarkiertAlsGewaehlt)
				{
					returnTakt = takt;
				}
			}
		}
		return returnTakt;
	}
	
	/**
	 * von aussen wird die Markierung auf den nächsten Takt gesetzt
	 * (null zurück, wenn kein Takt markiert)
	 * @return
	 */
	public Takt setMarkierungNaechsterTakt()
	{
		Takt takt = null;
		Takt returnTakt = null;
		boolean gefunden = false;
		boolean markiert = false;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				takt = (Takt)teil.getAlleTakte().elementAt(j);
				if ((gefunden) && (! markiert))
				{
					markiert = true;
					takt.setMarkiertenRahmen();
					returnTakt = takt;
				}
				else if (takt.istMarkiertAlsGewaehlt())
//				else if (takt.istMarkiertAlsGewaehlt)
				{
					gefunden = true;
					takt.resetRahmen();
				}
			}
		}
		if ((gefunden) && (! markiert))
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(0);
			takt = (Takt)teil.getAlleTakte().elementAt(0);
			takt.setMarkiertenRahmen();
			returnTakt = takt;
		}
		return returnTakt;
	}
	
	/**
	 * von aussen wird die Markierung auf den vorherigen Takt gesetzt
	 * (null zurück, wenn kein Takt markiert)
	 * @param istMitMarkierungSetzen == false => nur Takt ermitteln ohne markieren
	 * @return
	 */
	public Takt setMarkierungVorherigerTakt(boolean istMitMarkierungSetzen)
	{
		Takt takt = null;
		Takt returnTakt = null;
		boolean gefunden = false;
		boolean markiert = false;

		for (int i=this.schema.getTaktSchemaTeile().size()-1; i>=0; i--)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			
			for (int j=teil.getAlleTakte().size()-1; j>=0; j--)
			{
				takt = (Takt)teil.getAlleTakte().elementAt(j);
				if ((gefunden) && (! markiert))
				{
					markiert = true;
					if (istMitMarkierungSetzen)
						takt.setMarkiertenRahmen();
					returnTakt = takt;
				}
				else if (takt.istMarkiertAlsGewaehlt())
//				else if (takt.istMarkiertAlsGewaehlt)
				{
					gefunden = true;
					if (istMitMarkierungSetzen)
						takt.resetRahmen();
				}
			}
		}
		if ((gefunden) && (! markiert))
		{
			int anzTeile = this.schema.getTaktSchemaTeile().size();
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(anzTeile-1);
			takt = (Takt)teil.getAlleTakte().elementAt(teil.getTaktAnzahl()-1);
			if (istMitMarkierungSetzen)
				takt.setMarkiertenRahmen();
			returnTakt = takt;
		}
		return returnTakt;
	}

	/**
	 * Diese Methode wird von Takt aufgerufen, wenn die Maus auf 
	 * einen markierten Takt klickt; 
	 * hier Code in aufrufenden Window implementieren
	 * Achtung: in der implementierten Methode SwingUtilities.invokeLater benutzen !
	 * @param takt
	 */
	public void setMarkierungAktuellerTakt(final Takt takt)
	{
//		System.out.println(SwingUtilities.windowForComponent(this));
//		((AChordFrame)SwingUtilities.windowForComponent(Song.this)).setAkkordVonAussen(takt.transientClone());
		((AChordFrame)SwingUtilities.windowForComponent(this)).setAkkordVonAussen(takt);
	}
	
	/**
	 * @param zeile
	 */
	public void setMarkierungAktuellerText(final ALabel zeile)
	{
//		((AChordFrame)SwingUtilities.windowForComponent(Song.this)).setTextZeileVonAussen(zeile);
	}
	
	/**
	 *  Alle Markierungen der Takte werden resettet 
	 */
	public void resetAlleTaktMarkierungen()
	{
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				takt.resetRahmen();
			}
		}
	}

	/**
	 *  Alle Markierungen der Texte werden resettet 
	 */
	public void resetAlleTextMarkierungen()
	{
		for (int i = 0;i < this.text.getZeilen().size(); i++)
		{
			TextZeile zeile = (TextZeile)this.text.getZeilen().elementAt(i);
			zeile.resetRahmen();
		}
	}
	/**
	 *  es können die Rahmen gesetzt werden
	 *  (Schema, Teilschema und Textschema)
	 */
	public void setRahmenAlleSchemas()
	{
		if (Konfiguration.getInstance().isShowRahmenImSong())
		{
			this.setRahmen();
			if (schema != null)
				schema.setRahmen();
			if (text != null)
				text.setRahmen();
		}
	}
	
	/**
	 *  resetten der Rahmen
	 */
	public void resetRahmenAlleSchemas()
	{
		this.resetRahmen();
		schema.resetRahmen();
		text.resetRahmen();
	}

    /**
     * Änderungsmarkierung wird zurückgesetzt, d.h. ab jetzt wird 
     * jede Änderung registriert
     */
    public void resetAlleAenderungMarker()
    {
    	this.resetAenderungIstErfolgt();
    	this.getSchema().resetAenderungIstErfolgt();
    	this.getSchema().getAkkTonArtSong().resetAenderungIstErfolgt();
    	for (int i=0; i<this.getSchema().getTaktSchemaTeile().size(); i++)
    	{
    		TaktSchemaTeil teil = (TaktSchemaTeil)this.getSchema().getTaktSchemaTeile().elementAt(i);
    		teil.resetAenderungIstErfolgt();
    		for (int j=0; j<teil.getAlleTakte().size(); j++)
    		{
    			Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
    			takt.resetAenderungIstErfolgt();
    			if (takt.getAkkord1() != null)
    				takt.getAkkord1().resetAenderungIstErfolgt();
    			if (takt.getAkkord2() != null)
    				takt.getAkkord2().resetAenderungIstErfolgt();
    			if (takt.getAkkord3() != null)
    				takt.getAkkord3().resetAenderungIstErfolgt();
    			if (takt.getAkkord4() != null)
    				takt.getAkkord4().resetAenderungIstErfolgt();
    		}
    	}
    	this.getText().resetAenderungIstErfolgt();
    	this.getSchema().getKommentar().resetAenderungIstErfolgt();
    }
    
    /**
     * Abfrage z.B. vor dem öffnen einer anderen Taktus-Datei: wenn eine
     * Änderung am aktuellen Song erfolgte, dann Abfrage
     * @return
     */
    public boolean istEineAenderungErfolgt()
    {
    	boolean ret = false;

    	if (this.istAenderungErfolgt())
    		ret = true;
    	if (this.getSchema().istAenderungErfolgt())
    		ret = true;
    	if (this.getSchema().getAkkTonArtSong().istAenderungErfolgt())
    		ret = true;
    	for (int i=0; i<this.getSchema().getTaktSchemaTeile().size(); i++)
    	{
    		TaktSchemaTeil teil = (TaktSchemaTeil)this.getSchema().getTaktSchemaTeile().elementAt(i);
    		if (teil.istAenderungErfolgt())
        		ret = true;
    		for (int j=0; j<teil.getAlleTakte().size(); j++)
    		{
    			Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
    			if (takt.istAenderungErfolgt())
    	    		ret = true;
    			if ((takt.getAkkord1() != null) && (takt.getAkkord1().istAenderungErfolgt()))
    				ret = true;
    			if ((takt.getAkkord2() != null) && (takt.getAkkord2().istAenderungErfolgt()))
    				ret = true;
    			if ((takt.getAkkord3() != null) && (takt.getAkkord3().istAenderungErfolgt()))
    				ret = true;
    			if ((takt.getAkkord4() != null) && (takt.getAkkord4().istAenderungErfolgt()))
    				ret = true;
    		}
    	}
    	if (this.getText().istAenderungErfolgt())
    		return true;
    	if (this.getSchema().getKommentar().istAenderungErfolgt())
    		return true;
    	
    	return ret;
    }

	/**
	 * wird beim Einlesen aus Xml-Datei aufgerufen: liegt ein Fehler vor ?
	 * wenn ja, dann Fehlertext-Rückgabe
	 * @return
	 */
	public String checkeSong()
	{
		String returnString = null;

		if ((titelHeightAnteil + schema.getHeightAnteil() + text.getHeightAnteil()) != 100)
		{
			returnString = "Falsche Anteilsangaben Titel/Taktschema/Text";
		}

		return returnString;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if (this.titel == null)
			return false;
		if (this.titel.trim().length() < 1)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return "Song--"+titel+"/"+this.getSchema().getTaktSchemaTeile().size();
	}
	
	/**
	 * @return
	 */
	public Song transientClone()
	{
//		Song song = new Song(this.widthVerhaeltnis, this.heightVerhaeltnis);
		Song song = new Song();
		song.setTitel(this.titel);
		song.setAutor(this.autor);
		song.setTitelHeightAnteil(this.titelHeightAnteil);
		
		song.setSchema(this.schema.transientClone(song));
		
		song.setText(this.text.transientClone());
		
		return song;
	}
	
	/**
	 * Dies ist der Anteil, den das Akkord-Schema im Verhältnis zur gesamten
	 * A4-Seite füllt.
	 * @param neuerHeightAnteilTaktSchema
	 */
	public void setNeueHeightAnteile(int neuerHeightAnteilTaktSchema)
	{
		schema.setHeightAnteil(neuerHeightAnteilTaktSchema);
		text.setHeightAnteil(100 - neuerHeightAnteilTaktSchema - this.getTitelHeightAnteil());
	}

	/**
	 * Initialisiert das SplitPane zwischen Akkord-Schema und Text-Anteil
	 * (vom Bediener während der Laufzeit einstellbar)
	 */
	private void initSplitPane()
	{
		schema.addComponentListener(new ComponentListener()
		{
			public void componentResized(ComponentEvent e)
			{
				if (! inEventHandling)
				{
					inEventHandling = true;
					
					Song.this.setAenderungIstErfolgt();

					int heightAnteilTakte = (100*schema.getHeight()) / 
											(text.getHeight() + schema.getHeight() + titelGesamtHeight);
					int heightAnteilTexte = 100 - heightAnteilTakte - Song.this.getTitelHeightAnteil();

					heightSchema = schema.getHeight();
					
//					System.out.println(" SPLIT Anteile: "+
//							heightAnteilTakte + "/" + 
//							heightAnteilTexte + "/" +
//							Song.this.getTitelHeightAnteil()+
//							"<<>>"+
//							splitPane.getSize()+"/"+
//							splitPane.getLocation());
					
					schema.setHeightAnteil(heightAnteilTakte);
					text.setHeightAnteil(heightAnteilTexte);
					
					schema.konstruiereTaktSchema();

					if (text.istTextAufEsterSeite())
						text.konstruiereTextSchema();
					inEventHandling = false;
				}
			}
			public void componentHidden(ComponentEvent e)
			{
			}
			public void componentMoved(ComponentEvent e)
			{
			}
			public void componentShown(ComponentEvent e)
			{
			}
		});

	}
	  
	/**
	 * setzt die Farbe für den Anfasser des Splitpanes zwischen Song und Text
	 * @param splitPane
	 * @param newDividerColor
	 */
	public void setSplitPaneDividerColor(JSplitPane splitPane, Color newDividerColor) 
	{
	      SplitPaneUI splitUI = splitPane.getUI();
	      if (splitUI instanceof BasicSplitPaneUI) 
	      {
	         int divSize = splitPane.getDividerSize();
	         BasicSplitPaneDivider div = ((BasicSplitPaneUI) splitUI).getDivider();
	         Border divBorder = div.getBorder();
	         Border newBorder = null;
	         Border colorBorder = null;
	         
	         int insetsh = 0;
	         int insetsv = 0;
	         
	         if (divBorder != null) {
	            Insets i = divBorder.getBorderInsets(div);
	            insetsh = i.left + i.right;
	            insetsv = i.top + i.bottom;
	         }
	         
//	         System.out.println("  Splitsize:"+divSize+"/"+insetsv+"/"+insetsh);
	         
	         // this border uses a fillRect
	         colorBorder = BorderFactory.createMatteBorder(divSize - insetsv, divSize - insetsh, 0, 0, newDividerColor);
	 
	         if (divBorder == null) {
	            newBorder = colorBorder;
	         } else {
	            newBorder = BorderFactory.createCompoundBorder(divBorder, colorBorder);
	         }
	         
	         newBorder = BorderFactory.createLineBorder(newDividerColor, 1);
	         div.setBorder(newBorder);
	      }
	   }
	/**
	 * @return the songText
	 * enthält den Inhalt der 2. Seite: Titel und Text
	 */
	public SongText getSongText()
	{
		return songText;
	}
	/**
	 * @param songText the songText to set
	 */
	public void setSongText(SongText songText)
	{
		this.songText = songText;
	}
	
	/**
	 * 
	 */
	public void handleSongText()
	{
		songText = new SongText();
		songText.setDaten(this);
		songText.setLocation(6, 2);
		songText.setVisible(true);
		songText.setLayout(null);
		songText.setSizeBreiteA4(this.getWidth());
		songText.setRahmenAlleSchemas();
		songText.konstruierTextBlatt(true);
	}
	

	/**
	 * aus Popup-Menü: 
	 * Transponieren der markierten Takte
	 */
	private void onTransponiereTakte()
	{
		Vector v = bestimmeMarkierteTakte(false);
		setMarkierungFuerMarkierteTakte(v);
    	DialogTansponieren info = new DialogTansponieren(((AChordFrame)SwingUtilities.windowForComponent(this)),"Transponieren der markierten Takte",true);
    	info.setDaten(this, v);
    	info.setVisible(true);
	}

	/**
	 * aus Popup-Menü: 
	 * Kopieren der Takte, die vorher vom Mausrahmen markiert wurden
	 */
	private void onKopiereTakte()
	{
		markierteTakte = bestimmeMarkierteTakte(true);
	}
	
	/**
	 * aus Popup-Menü: 
	 * leeren der markierten Takte
	 */
	private void onLeereTakte()
	{
		Vector v = bestimmeMarkierteTakte(false);
		for (int i = 0; i < v.size(); i++)
		{
			Takt takt = (Takt)v.elementAt(i);
			takt.setTaktAufLeer();
		}
	}
	
	/**
	 * aus Popup-Menü: 
	 * einfügen der Inhalte der markierten Takte
	 */
	private void onFuegeTakteEin()
	{
		int countTaktNr = -1;
		int countNeuerTakt = -1;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				countTaktNr++;
//				System.out.println("++fuegeTakeEin++"+
//						countTaktNr+"/"+merkeMarkiertenTakt+"///"+
//						(countNeuerTakt+1)+"/"+markierteTakte.size());
				if ((merkeMarkiertenTakt > -1) &&
					(countTaktNr >= merkeMarkiertenTakt) && 
					((countNeuerTakt+1) < markierteTakte.size()))
				{
					countNeuerTakt++;
//					System.out.println("++++fuegeTakeEin++++"+
//											countNeuerTakt);
					Takt taktCopy = (Takt)markierteTakte.elementAt(countNeuerTakt);
					takt.setTaktAufLeer();
					takt.copyWerte(taktCopy);
				}
			}
		}
		this.aktualisiereBlatt();
		this.setRahmenAlleSchemas();
		this.reloadTaktSchema();

		Takt takt = this.getAktuellMarkiertenTakt();
		((AChordFrame)SwingUtilities.windowForComponent(this)).setAkkordVonAussen(takt);
		rectResetMarkierteComponenten(true);
	}
	
	
	

	// **********************************************************************
	// Behandlung des Markierens von Takten und des dazugehörigen Popup-Menüs	
	// **********************************************************************
	
	/* 
	 * überschreiben der paint-Methode;
	 * zeichnen des Markierungs-Rechteckes beim Markieren mehrerer Takte
	 * durch den Bediener
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g)
	{
		super.paint(g);
		
		if (markRect.x > MIN_RECT_SIZE || markRect.y > MIN_RECT_SIZE) 
		{
			g.drawRect(markRect.x,markRect.y,markRect.width,markRect.height);
		}
	}

	/**
	 * wird von aussen aufgerufen, um modifiziert Takte wieder als
	 * selektiert zu markieren
	 * @param v
	 */
	public void setMarkierungFuerMarkierteTakte(Vector v)
	{
		if (v == null)
			return;
		for (int i=0;i<v.size();i++)
		{
			Takt takt = (Takt)v.elementAt(i);
			takt.setMausRahmenSelektierung();
		}
	}

	/**
	 * diese Methode wird vom Dialog "DialogTransponieren" aufgerufen
	 * (beim Verlassen des Dialoges)
	 */
	public void resetIstTransponiereDialogGeladen()
	{
		rectResetMarkierteComponenten(true);
	}
	
	/**
	 * @return
	 */
	public boolean istPopupVisible()
	{
		if (popup.isVisible())
			return true;
		else
			return false;
	}
	
	/**
	 * Es werden die Takte bestimmt und zurückgegeben, die vom
	 * Bediener markiert wurden
	 * @return
	 */
	private Vector bestimmeMarkierteTakte(boolean istClone)
	{
		Vector v  = new Vector();
		int countTaktNr = -1;
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				countTaktNr++;
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				if (takt.istTaktSelektiert())
				{
					if (istClone)
					{
						Takt taktclone = takt.transientClone(null);
						takt.setTempTaktNr(countTaktNr);
						v.add(taktclone);
					}
					else
					{
						v.add(takt);
					}
				}
			}
		}
		return v;
	}

	/**
	 * Die Markierungen für die selektierten Takte werden wieder aufgehoben.
	 * @param mitResetMarkierung
	 */
	public void rectResetMarkierteComponenten(boolean mitResetMarkierung)
	{
	    markRect = new Rectangle(0,0,0,0);
		for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
			for (int j = 0; j < teil.getAlleTakte().size(); j++)
			{
				Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
				takt.resetMausRahmenSelektierungNurMarkierung();
				if (mitResetMarkierung)
					takt.resetMausRahmen();
			}
		}
	    repaint();
	}
	
	/**
	 * Wenn die Maus "gedraggt" wird, dann wird hier überprüft, ob ein Takt
	 * berührt oder wieder unberührt wird, um ihn als selektiert oder als
	 * deselektiert zu markieren.
	 * @param e
	 * @param parent
	 */
	public void rectCheckeAufZuMarkierndeComponenten(MouseEvent e, APanel parent)
	{
		int x = e.getX();
		int y = e.getY();
		if (e.getComponent() instanceof TaktSchemaTeil)
		{
			x = e.getX()+parent.getX();
			y = e.getY()+parent.getY()+titelGesamtHeight;
		}
		else if (e.getComponent() instanceof Takt)
		{
			Takt takt = (Takt)parent;
			x = e.getX()+takt.getX();
			y = e.getY()+takt.getY()+takt.getTeil().getY()+titelGesamtHeight;
		}

		if (x > markRect.x && y > markRect.y) 
		{
			markRect.width = x - markRect.x;
			markRect.height = y - markRect.y;
		}
		if ((markRect.getWidth() > MIN_RECT_SIZE) || (markRect.getHeight() > MIN_RECT_SIZE))
		{
			double xObenLinks = markRect.getX();
			double yObenLinks = markRect.getY();
			double xUntenRechts = xObenLinks + markRect.getWidth();
			double yUntenRechts = yObenLinks + markRect.getHeight();
			
			// Componenten
			for (int i = 0;i < this.schema.getTaktSchemaTeile().size(); i++)
			{
				TaktSchemaTeil teil = (TaktSchemaTeil)this.schema.getTaktSchemaTeile().elementAt(i);
				for (int j = 0; j < teil.getAlleTakte().size(); j++)
				{
					Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
					if ((xUntenRechts >= takt.getX()+teil.getX()) &&
						(yUntenRechts >= takt.getY()+teil.getY()+titelGesamtHeight) &&
						(xObenLinks <= takt.getX()+teil.getX()+takt.getWidth()) &&
						(yObenLinks <= takt.getY()+teil.getY()+takt.getHeight()+titelGesamtHeight))
					{
						takt.setMausRahmenSelektierung();
					}
					else
					{
						takt.resetMausRahmenSelektierungMitMarker();
					}
				}
			}
			repaint();
		}
		else
		{
		}
	}
	
	/**
	 * enablen/disablen der Popupmenüs, wenn keine Markierung 
	 * erfolgt ist
	 */
	private void setPopupMenuesTrigger() 
	{
		if  ((markierteTakte.size() > 0) && (getMarkierteTaktNr() > -1))
		{
			menuItemPaste.setEnabled(true);
		}
		else
		{
			menuItemPaste.setEnabled(false);
		}
		menuItemTransp.setEnabled(false);
		menuItemLeeren.setEnabled(false);
		menuItemCopy.setEnabled(false);
	}

	/**
	 * beim Markieren von Takten wird hier das erste "Festhalten" der linken
	 * Maustaste betrachtet.
	 * @param e
	 * @param parent
	 */
	public void rectHandleMousePressed(final MouseEvent e, final APanel parent)
	{
		if (e.isPopupTrigger())
		{
			// Änderung nach 1.8 19.9.09
			// PopupTrigger kann platformabhängig beim MousePressed oder
			// beim MouseReleased erfolgen
//			System.out.println("++++++pressed++++Popup+++++");
		    SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					setPopupMenuesTrigger();
		
					popup.show(e.getComponent(), e.getX(), e.getY());
					popup.setVisible(true);
				}
			});
		}
		else
		{
			rectResetMarkierteComponenten(false);
		    SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					int x = e.getX();
					int y = e.getY();
					if (e.getComponent() instanceof TaktSchemaTeil)
					{
						x = e.getX()+parent.getX();
						y = e.getY()+parent.getY()+titelGesamtHeight;
					}
					else if (e.getComponent() instanceof Takt)
					{
						Takt takt = (Takt)parent;
						x = e.getX()+takt.getX();
						y = e.getY()+takt.getY()+takt.getTeil().getY()+titelGesamtHeight;
					}
			        markRect = new Rectangle(x, y, 0, 0);
				}
			});
		}
	}
	
	
	/**
	 * beim Markieren von Takten wird hier das erste "Loslassen" der linken
	 * Maustaste betrachtet; darstellen des PopupMenüs
	 * @param e
	 * @param parent
	 */
	public void rectHandleMouseReleased(final MouseEvent e, APanel parent)
	{
//		if (e.isPopupTrigger())
//			System.out.println("++++++released++++Popup+++++"+merkeMarkiertenTakt+"/"+
//					getMarkierteTaktNr());

		repaint();
		if ((markRect.getWidth() > MIN_RECT_SIZE) || (markRect.getHeight() > MIN_RECT_SIZE) ||
			(e.isPopupTrigger()))
		{
		    SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					// Änderung nach 1.8 19.9.09
					// PopupTrigger kann platformabhängig beim MousePressed oder
					// beim MouseReleased erfolgen
					if (! e.isPopupTrigger())
					{
						menuItemPaste.setEnabled(false);
						menuItemTransp.setEnabled(true);
						menuItemLeeren.setEnabled(true);
						menuItemCopy.setEnabled(true);
					}
					else
					{
						setPopupMenuesTrigger();
					}

					popup.show(e.getComponent(), e.getX(), e.getY());
					popup.setVisible(true);
				}
			});
		}
		else
		{
			rectResetMarkierteComponenten(true);
		}
	}

	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseClicked(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseClicked(MouseEvent e)
	{
//		System.out.println("######  MARKIEREN EINES TAKTES ##########################");
//		rectResetMarkierteComponenten();
	}

	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMousePressed(java.awt.event.MouseEvent)
	 */
	public void aPanelMousePressed(MouseEvent e)
	{
		rectHandleMousePressed(e, this);
	}
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseReleased(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseReleased(MouseEvent e)
	{
		rectHandleMouseReleased(e, this);
	}

	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseDragged(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseDragged(MouseEvent e)
	{
		rectCheckeAufZuMarkierndeComponenten(e, this);
	}

}
