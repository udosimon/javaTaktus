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
 * Beschreibung:
 * 
 * Datenklasse, die alle Konfigurationsdaten enthält;
 * Programm kann immer auf die Daten zugreifen (als Singleton)
 * Methoden zum Einlesen und Schreiben der Konfigurationsdatei
 * 
 * 
 */
package AChord.Hilfe;

import java.io.File;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;

import AChord.xml.AXml;
import AChord.xml.AXmlElementKonfLayout;
import AChord.xml.AXmlElementKonfUmgebung;
import AChord.xml.AXmlReader;
import AChord.xml.AXmlWriter;


public class Konfiguration
{
	private static final String INIDATEINAME = "Taktus.ini";
	
	public static final int ANZEIGE_NEUERTAKT_KLANG = 1;
	public static final int ANZEIGE_NEUERTAKT_ART = 2;
	public static final int ANZEIGE_NEUERTAKT_ARTKLANG = 3;
	public static final int ANZEIGE_NEUERTAKT_OHNE = 0;
	
	public static final int ANZEIGE_B_AKKORD_IMMER_B = 0;
	public static final int ANZEIGE_B_AKKORD_IMMER_H = 1;
	public static final int ANZEIGE_B_AKKORD_B_NUR_WENN_HALBTON = 2;
	
	//Default-Werte
	public final int DEF_ABSTAND_ZWISCHEN_TEILEN = 14;
	public final int DEF_ABSTAND_VOR_ERSTEM_TEIL = 12;
	public final int DEF_ABSTAND_VOR_TEXT = 12;
	public final int DEF_ANZEIGE_B_AKKORD = ANZEIGE_B_AKKORD_IMMER_B;
	public final int DEF_ANEZIGE_NEUER_TAKT = ANZEIGE_NEUERTAKT_OHNE;
	public final boolean DEF_RAHMEN_DRUCK = false;
	public final boolean DEF_RAHMEN_SONG = false;

	private static Konfiguration INSTANCE = new Konfiguration();
	
	// Konfiguration Layout
	private int anzeigeNeuerTakt = DEF_ANEZIGE_NEUER_TAKT;
	
	private int anzeigeBimAkkord = DEF_ANZEIGE_B_AKKORD;
	
	private boolean showRahmenImDruck = DEF_RAHMEN_DRUCK;
	private boolean showRahmenImSong = DEF_RAHMEN_SONG;
	
	private int abstandZwischenTeilen = DEF_ABSTAND_ZWISCHEN_TEILEN;
	private int abstandVorErstemTeil = DEF_ABSTAND_VOR_ERSTEM_TEIL;
	private int abstandVorText = DEF_ABSTAND_VOR_TEXT;

	// dieser Wert wird aktuell nicht im Konfigurationsdialog gesetzt;
	// hier wird die Anzeige H/B im Tonart-Panel geregelt;
	// aktueller Stand: hier wird immer ein "H" angezeigt
	private boolean istAnzeigeBimPanel = false;

	// Umgebungsdaten
	private String taktusDirectory = null;

	private String schriftArt;
	private String schriftArtNarrow;

	/**
	 * Konstruktor
	 */
	private Konfiguration()
	{
		super();
		leseIniDatei();
	}
	
	/**
	 * Singleton
	 * @return
	 */
	public static Konfiguration getInstance()
	{
		return INSTANCE;
	}
	
	
	
	// ***************************************
	// getter und setter
	// ***************************************

	public String getSchriftArt() {
		return schriftArt;
	}

	public void setSchriftArt(String schriftArt) {
		this.schriftArt = schriftArt;
	}

	public String getSchriftArtNarrow() {
		return schriftArtNarrow;
	}

	public void setSchriftArtNarrow(String schriftArtNarrow) {
		this.schriftArtNarrow = schriftArtNarrow;
	}

	/**
	 * @return
	 */
	public int getKonfAnzeigeNeuerTakt()
	{
		return anzeigeNeuerTakt;
	}
	
	/**
	 * @param anzeige
	 */
	public void setKonfAnzeigeNeuerTakt(int anzeige)
	{
		anzeigeNeuerTakt = anzeige;
	}
	
	/**
	 * @param istArt
	 * @param istKlang
	 */
	public void setNeuerTakt(boolean istArt, boolean istKlang)
	{
		if ((istArt) && (istKlang))
			anzeigeNeuerTakt = ANZEIGE_NEUERTAKT_ARTKLANG;
		else if (istArt)
			anzeigeNeuerTakt = ANZEIGE_NEUERTAKT_ART;
		else if (istKlang)
			anzeigeNeuerTakt = ANZEIGE_NEUERTAKT_KLANG;
		else
			anzeigeNeuerTakt = ANZEIGE_NEUERTAKT_OHNE;
	}
	
	/**
	 * @param istAnzeigeBimPanel the istAnzeigeBimPanel to set
	 */
	public void setIstAnzeigeBimPanel(boolean istAnzeigeBimPanel)
	{
		this.istAnzeigeBimPanel = istAnzeigeBimPanel;
	}

	/**
	 * @return the istAnzeigeBimAkkord
	 */
	public int getAnzeigeBimAkkord()
	{
		return anzeigeBimAkkord;
	}

	/**
	 * @param istAnzeigeBimAkkord the istAnzeigeBimAkkord to set
	 */
	public void setIstAnzeigeBimAkkord(int anzeigeBimAkkord)
	{
		this.anzeigeBimAkkord = anzeigeBimAkkord;
	}
	
	/**
	 * @return
	 */
	public String getBTonAkkord()
	{
		if (anzeigeBimAkkord == ANZEIGE_B_AKKORD_IMMER_B)
			return "B";
		else
			return "H";
	}
	
	/**
	 * @return
	 */
	public String getBTonAkkordWennHalb()
	{
		if (anzeigeBimAkkord == ANZEIGE_B_AKKORD_IMMER_H)
			return "H";
		else
			return "B";
	}
	
	/**
	 * @return
	 */
	public String getBTonPanel()
	{
		if (istAnzeigeBimPanel)
			return "B";
		else
			return "H";
	}

	/**
	 * @param showRahmenImDruck the showRahmenImDruck to set
	 */
	public void setShowRahmenImDruck(boolean showRahmenImDruck)
	{
		this.showRahmenImDruck = showRahmenImDruck;
	}

	/**
	 * @param showRahmenImSong the showRahmenImSong to set
	 */
	public void setShowRahmenImSong(boolean showRahmenImSong)
	{
		this.showRahmenImSong = showRahmenImSong;
	}

	/**
	 * @return the abstandZwischenTeilen
	 */
	public int getAbstandZwischenTeilen()
	{
		return abstandZwischenTeilen;
	}

	/**
	 * @param abstandZwischenTeilen the abstandZwischenTeilen to set
	 */
	public void setAbstandZwischenTeilen(int abstandZwischenTeilen)
	{
		this.abstandZwischenTeilen = abstandZwischenTeilen;
	}

	/**
	 * @return the abstandVorErstemTeil
	 */
	public int getAbstandVorErstemTeil()
	{
		return abstandVorErstemTeil;
	}

	/**
	 * @param abstandVorErstemTeil the abstandVorErstemTeil to set
	 */
	public void setAbstandVorErstemTeil(int abstandVorErstemTeil)
	{
		this.abstandVorErstemTeil = abstandVorErstemTeil;
	}

	/**
	 * @return the abstandVorText
	 */
	public int getAbstandVorText()
	{
		return abstandVorText;
	}

	/**
	 * @param abstandVorText the abstandVorText to set
	 */
	public void setAbstandVorText(int abstandVorText)
	{
		this.abstandVorText = abstandVorText;
	}
	
	/**
	 * @return the taktusDirectory
	 */
	public String getTaktusDirectory()
	{
		return taktusDirectory;
	}

	/**
	 * @param taktusDirectory the taktusDirectory to set
	 */
	public void setTaktusDirectory(String taktusDirectory)
	{
		this.taktusDirectory = taktusDirectory;
		this.schreibeIniDatei();
	}


	/**
	 * @return
	 */
	public boolean istNeuerTaktMitArt()
	{
		if ((anzeigeNeuerTakt == ANZEIGE_NEUERTAKT_ART) ||
			(anzeigeNeuerTakt == ANZEIGE_NEUERTAKT_ARTKLANG))
			return true;
		else
			return false;
	}
	
	/**
	 * @return
	 */
	public boolean istNeuerTaktMitKlang()
	{
		if ((anzeigeNeuerTakt == ANZEIGE_NEUERTAKT_KLANG) ||
			(anzeigeNeuerTakt == ANZEIGE_NEUERTAKT_ARTKLANG))
			return true;
		else
			return false;
	}
	
	/**
	 * @return the istAnzeigeBimPanel
	 */
	public boolean isIstAnzeigeBimPanel()
	{
		return istAnzeigeBimPanel;
	}

	/**
	 * @return
	 */
	public boolean istNurBAkkord()
	{
		if (anzeigeBimAkkord == ANZEIGE_B_AKKORD_IMMER_B)
			return true;
		else
			return false;
	}

	/**
	 * @return
	 */
	public boolean istNurHAkkord()
	{
		if (anzeigeBimAkkord == ANZEIGE_B_AKKORD_IMMER_H)
			return true;
		else
			return false;
	}

	/**
	 * @return
	 */
	public boolean istBAkkordNurWennHalb()
	{
		if (anzeigeBimAkkord == ANZEIGE_B_AKKORD_B_NUR_WENN_HALBTON)
			return true;
		else
			return false;
	}

	/**
	 * @return the showRahmenImDruck
	 */
	public boolean isShowRahmenImDruck()
	{
		return showRahmenImDruck;
	}

	/**
	 * @return the showRahmenImSong
	 */
	public boolean isShowRahmenImSong()
	{
		return showRahmenImSong;
	}


	
	
	public void setzeBetriebssystemValues()
	{
		final String SCHRIFT_ART_DEFAULT = "Arial"; 
		final String SCHRIFT_ART_NARROW_DEFAULT = "Arial Narrow"; 
		final String SCHRIFT_ART_WINDOWS = "Arial"; 
		final String SCHRIFT_ART_NARROW_WINDOWS = "Arial Narrow"; 
		final String SCHRIFT_ART_LINUX = "Liberation Sans"; 
		final String SCHRIFT_ART_NARROW_LINUX = "Liberation Sans Narrow Condensed"; 

    	setSchriftArt(SCHRIFT_ART_DEFAULT);
    	setSchriftArtNarrow(SCHRIFT_ART_NARROW_DEFAULT);
		
		String propname = "os.name";
        String osName = System.getProperty(propname);
        System.out.println(propname + "=" + osName);
        if (osName.startsWith("Win")) {
        	System.out.println("WINDOWS");
        	setSchriftArt(SCHRIFT_ART_WINDOWS);
        	setSchriftArtNarrow(SCHRIFT_ART_NARROW_WINDOWS);
        }
        else if (osName.startsWith("Lin")) {
        	System.out.println("Linux");
        	setSchriftArt(SCHRIFT_ART_LINUX);
        	setSchriftArtNarrow(SCHRIFT_ART_NARROW_LINUX);
        }

	}
	
	
	/**
	 * lesen aus Konfigurationsdatei; erstellen der Konfigurationsdatei,
	 * wenn es sie denn nicht gibt 
	 */
	public void leseIniDatei()
	{
		File file = new File(INIDATEINAME);
		if (file.exists())
		{
			AXmlReader xmlr = new AXmlReader(file);
			Document doc = xmlr.parse();
			if (doc!=null)
			{
				Element el;
				Element root = xmlr.getRoot();
				Vector v = xmlr.getElementList(root, AXml.TAG_K_LAYOUT);
				if ((v != null) && (v.size() > 0))
				{
					try
					{
						el = (Element)v.elementAt(0);
						this.setKonfAnzeigeNeuerTakt(new Integer(el.attributeValue(AXml.ATTR_K_TAKTLEER)).intValue());
						this.setIstAnzeigeBimAkkord(new Integer(el.attributeValue(AXml.ATTR_K_BAKKORD)).intValue());
						this.setIstAnzeigeBimPanel(new Boolean(el.attributeValue(AXml.ATTR_K_BPANEL)).booleanValue());
						this.setShowRahmenImSong(new Boolean(el.attributeValue(AXml.ATTR_K_BORDERSONG)).booleanValue());
						this.setShowRahmenImDruck(new Boolean(el.attributeValue(AXml.ATTR_K_BORDERDRUCK)).booleanValue());
						this.setAbstandZwischenTeilen(new Integer(el.attributeValue(AXml.ATTR_K_TEILABSTAND)).intValue());
						this.setAbstandVorErstemTeil(new Integer(el.attributeValue(AXml.ATTR_K_TEILABSTANDVOR)).intValue());
						this.setAbstandVorText(new Integer(el.attributeValue(AXml.ATTR_K_TEILABSTANDVORTEXT)).intValue());
					}
					catch (Exception e)
					{
						System.out.println("Fehler in INI-Datei !");
						schreibeIniDatei();
					} 
				}
				v = xmlr.getElementList(root, AXml.TAG_K_UMGEBUNG);
				if ((v != null) && (v.size() > 0))
				{
					try
					{
						el = (Element)v.elementAt(0);
						taktusDirectory = (String)el.attributeValue(AXml.ATTR_K_DIRTAKTUS);
						if (taktusDirectory.equals("---"))
							taktusDirectory = null;
					}
					catch (Exception e)
					{
						System.out.println("Fehler in INI-Datei !");
					} 
				}
			}
			else
			{
				System.out.println("Fehler in INI-Datei !");
			}
		}
		else
		{
			// wenn keine Konfiguritions-XML-Datei vorhanden ist, dann wird
			// hier eine Konfigurationsdatei erstellt und mit den Default-
			// Werten befüllt
			setDefaultWerte();
			schreibeIniDatei();
		}
	}

	/**
	 * schreiben der Konfigurationsdatei
	 */
	public void schreibeIniDatei()
	{
		File file = new File(INIDATEINAME);
		AXmlWriter xmlw = new AXmlWriter(file);
		
		xmlw.addComment("=======================================================================");
		xmlw.addComment("    Taktus - ParameterDatei - ");
		xmlw.addComment("    Taktus - Version: " + DialogInfo.VERSIONS_NUMMER);
		xmlw.addComment("=======================================================================");
		
		Element root = xmlw.addRoot(AXml.TAG_ROOT);

		AXmlElementKonfLayout eleLayout = new AXmlElementKonfLayout(this);
		xmlw.addElement2Element(root, eleLayout);

		AXmlElementKonfUmgebung eleUmgeb = new AXmlElementKonfUmgebung(this);
		xmlw.addElement2Element(root, eleUmgeb);

		xmlw.write();
	}

	/**
	 * setzen der Default-Werte für den Konfigurationsdialog
	 */
	public void setDefaultWerte()
	{
		anzeigeNeuerTakt = DEF_ANEZIGE_NEUER_TAKT;
		
		anzeigeBimAkkord = DEF_ANZEIGE_B_AKKORD;
		
		showRahmenImDruck = DEF_RAHMEN_DRUCK;
		showRahmenImSong = DEF_RAHMEN_SONG;
		
		abstandZwischenTeilen = DEF_ABSTAND_ZWISCHEN_TEILEN;
		abstandVorErstemTeil = DEF_ABSTAND_VOR_ERSTEM_TEIL;
		abstandVorText = DEF_ABSTAND_VOR_TEXT;

		// istAnzeigeBimPanel = false;
	}

}
