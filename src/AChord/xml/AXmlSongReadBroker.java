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
 * Udo Simon/
 * 
 * Beschreibung:
 * 
 * Liest einen Titel aus XML-Datei und weisst die Details
 * den entsprechenden Schematas zu
 * 
 */
package AChord.xml;

import java.awt.Component;
import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;

import AChord.Schema.Akkord;
import AChord.Schema.Kommentar;
import AChord.Schema.Song;
import AChord.Schema.Takt;
import AChord.Schema.TaktSchema;
import AChord.Schema.TaktSchemaTeil;
import AChord.Schema.TextSchema;
import AChord.Schema.TextZeile;

public class AXmlSongReadBroker extends AXml
{
	private static AXmlSongReadBroker INSTANCE = new AXmlSongReadBroker();
	
	Component parent;
	
	/**
	 * Sigleton
	 * @return
	 */
	public static AXmlSongReadBroker getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Konstruktor
	 */
	public AXmlSongReadBroker()
	{
		super();
	}
	
	/**
	 * Aufruf von außen zum lesen eines Taktus-Songs aus der XML-Datei
	 * @param xmlFile
	 * @param parent
	 * @return
	 */
	public Song readSongFromXml(File xmlFile, Component parent)
	{
		this.parent = parent;
		
		Song song = null;
		
		// lesen der Datei
		AXmlReader xmlr = new AXmlReader(xmlFile);
		Document doc = xmlr.parse();
		if (doc!=null)
		{
			Element root = xmlr.getRoot();
//			System.out.println("-----"+root.getName());

			song = new Song();
			boolean ok = bestimmeSongDetails(xmlr, root, song);
			if ((! ok) || (! song.isValid()))
			{
				JOptionPane.showMessageDialog(parent, 
										"Falsche Song-Parameter (Titel,Text)", 
										"Fehler in der Title-Datei", 
										JOptionPane.ERROR_MESSAGE);
				return null;
			}

//			System.out.println("------"+song.toString());
			
			ok = bestimmeSchemaDetails(xmlr, root, song);
			if (! ok)
				return null;
			
			ok = bestimmeText(xmlr, root, song);
			if (! ok)
				return null;
			
			String message = song.checkeSong();
			if (message != null)
			{
				JOptionPane.showMessageDialog(parent, 
						message, 
						"Fehler in der Title-Datei", 
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		return song;
	}

	/**
	 * Xml: bestimmen der Song-Attribute, wie Titel, Autor
	 * @param xmlr
	 * @param element
	 * @param song
	 */
	private boolean bestimmeSongDetails(AXmlReader xmlr, Element element, Song song)
	{
		boolean retValue = true;
		
		Vector v = null;
		Element el;
		
		v = xmlr.getElementList(element, TAG_TITEL);
		if ((v != null) && (v.size() > 0))
		{
			try
			{
				el = (Element)v.elementAt(0);
				song.setTitel((String)el.attributeValue(ATTR_NAME));
				song.setAutor((String)el.attributeValue(ATTR_AUTOR));
				song.setTitelHeightAnteil(new Integer(el.attributeValue(ATTR_HEIGHTANTEIL)).intValue());
				int x = new Integer(el.attributeValue(ATTR_WIDTH)).intValue();
				int y = new Integer(el.attributeValue(ATTR_HEIGHT)).intValue();
				song.setSongSize(x, y);
			} 
			catch (Exception e)
			{
				retValue = false;
			}
		}
		
//		v = xmlr.getElementList(element, TAG_TEXT);
//		if ((v != null) && (v.size() > 0))
//		{
//			el = (Element)v.elementAt(0);
//		}
		return retValue;
	}
	
	/**
	 * Xml: bestimmen der Akkord-Schema-Attribute, wie Anzahl der Teile, Anzahl 
	 * Takte pro Zeile, Anzeige der Song-Tonart, Kommentar
	 * @param xmlr
	 * @param element
	 * @param song
	 */
	private boolean bestimmeSchemaDetails(AXmlReader xmlr, Element element, Song song)
	{
		Vector v = null;
		Element el = null;

		v = xmlr.getElementList(element, TAG_TAKTSCHEMA);
		TaktSchema schema = null;
		if ((v != null) && (v.size() > 0))
		{
			schema = new TaktSchema(song);
			el = (Element)v.elementAt(0);
			try
			{
				schema.setAnzahlTeile(new Integer(el.attributeValue(ATTR_ANZAHLTEILE)).intValue());
				schema.setTakteProZeile(new Integer(el.attributeValue(ATTR_TAKTEPROZEILE)).intValue());
				schema.setTonArt(new Integer(el.attributeValue(ATTR_TON)).intValue());
				schema.setTonHalbTon(new Integer(el.attributeValue(ATTR_HALBTON)).intValue());
				schema.setTonGeschlecht((String)el.attributeValue(ATTR_ART));
				schema.setAnzeigeTonArt((String)el.attributeValue(ATTR_ANZEIGETONART));
				schema.setHeightAnteil(new Integer(el.attributeValue(ATTR_HEIGHTANTEIL)).intValue());
				schema.setKommentar(getKommentare(xmlr, el	));
				
				song.setSchema(schema);
			} 
			catch (Exception e)
			{
				schema.setAnzahlTeile(0);
				schema.setTakteProZeile(0);
				schema.setTonArt(0);
			}
		}

		if ((schema == null) || (! schema.isValid()))
		{
			JOptionPane.showMessageDialog(parent, 
									"Falsche Song-Parameter (TaktSchema)", 
									"Fehler in der Title-Datei", 
									JOptionPane.ERROR_MESSAGE);
			return false;
		}

//		System.out.println("-------"+schema.toString());

		return bestimmeSchemaTeileDetails(xmlr, el, schema, song);
	}


	/**
	 * Xml: bestimmen der Attribute der Akkord-Schema-Teile, wie Name des Teils,
	 * Anzeige Wiederholungszeichen, Anzeige Kästen, Anzeige Kopf
	 * @param xmlr
	 * @param element
	 * @param schema
	 * @param song
	 */
	private boolean bestimmeSchemaTeileDetails(AXmlReader xmlr, 
											Element element, 
											TaktSchema schema, 
											Song song)
	{
		boolean retValue = true;
		Vector elemSchemaTeile = xmlr.getElementList(element, TAG_TAKTSCHEMATEIL);
		for (int j=0;j<elemSchemaTeile.size();j++)
		{
			TaktSchemaTeil teil = null;
			Element eleSchemaTeil = (Element)elemSchemaTeile.elementAt(j);
			try
			{
				int teilart = new Integer(eleSchemaTeil.attributeValue(ATTR_TEILART)).intValue();
				int anzahl = new Integer(eleSchemaTeil.attributeValue(ATTR_ANZAHLTAKTE)).intValue(); 
				teil = new TaktSchemaTeil(teilart, anzahl, song);
				teil.setTeilName((String)eleSchemaTeil.attributeValue(ATTR_NAME));
				teil.setIstWiederholung(new Boolean(eleSchemaTeil.attributeValue(ATTR_WIEDERHOLUNG)).booleanValue());
				teil.setAnzahlTakteKasten(new Integer(eleSchemaTeil.attributeValue(ATTR_ANZAHLTAKTEKASTEN)).intValue());
				teil.setHeightAnteil(new Integer(eleSchemaTeil.attributeValue(ATTR_HEIGHTANTEIL)).intValue());
				teil.setKopfTakt(new Integer(eleSchemaTeil.attributeValue(ATTR_KOPFNR)).intValue());
			} 
			catch (Exception e)
			{
				teil = null;
			} 
			if ((teil == null) || (! teil.isValid())) 
			{
				JOptionPane.showMessageDialog(parent, 
										"Falsche Song-Parameter (TaktSchemaTeil)", 
										"Fehler in der Title-Datei", 
										JOptionPane.ERROR_MESSAGE);
				return false;
			}

//			System.out.println("--------"+teil.toString());
			
			schema.addSchemaTeil(teil);
			retValue = bestimmeTakteDetails(xmlr, eleSchemaTeil, teil, song);
			if (! retValue)
				return false;
			teil.trenneAbKastenTakte();
		}
		return retValue;
	}
	
	/**
	 * Xml: bestimmen der Attribute der Takte, wie Teilung und Breaks
	 * @param xmlr
	 * @param element
	 * @param teil
	 * @param song
	 */
	private boolean bestimmeTakteDetails(AXmlReader xmlr, 
										Element element, 
										TaktSchemaTeil teil, 
										Song song)
	{
		boolean retValue = true;
		Vector elemTakte = xmlr.getElementList(element, TAG_TAKT);
		for (int j=0;j<elemTakte.size();j++)
		{
			Takt takt = null;
			Element eleTakt = (Element)elemTakte.elementAt(j);
			try
			{
				int teilung = new Integer(eleTakt.attributeValue(ATTR_TEILUNG)).intValue();
				takt = new Takt(teilung, song, teil);
				takt.setBreakQuot(new Integer(eleTakt.attributeValue(ATTR_BREAK)).intValue());
				takt.setBreakText(eleTakt.attributeValue(ATTR_BREAKTEXT));
			} 
			catch (Exception e)
			{
				takt = null;
			} 
			if ((takt == null) || (! takt.isValid())) 
			{
				JOptionPane.showMessageDialog(parent, 
										"Falsche Song-Parameter (Takt)", 
										"Fehler in der Title-Datei", 
										JOptionPane.ERROR_MESSAGE);
				return false;
			}

//			System.out.println("---------"+takt.toString());
			
			teil.addTakt(takt);
			retValue = bestimmeAkkordDetails(xmlr, eleTakt, takt);
			if (! retValue)
				return false;
		}
		return retValue;
	}
	
	/**
	 * Xml: bestimmen der Attribute der Akkorde
	 * @param xmlr
	 * @param element
	 * @param takt
	 */
	private boolean bestimmeAkkordDetails(AXmlReader xmlr, Element element, Takt takt)
	{
		Vector elemAkkorde = xmlr.getElementList(element, TAG_AKKORD);
		Vector vAkk = new Vector();
		
		for (int j=0;j<elemAkkorde.size();j++)
		{
			Akkord akk = null;
			Element eleAkkord = (Element)elemAkkorde.elementAt(j);
			try
			{
				akk = new Akkord(j+1);
				int ton = new Integer(eleAkkord.attributeValue(ATTR_TON)).intValue();
				int halbton = new Integer(eleAkkord.attributeValue(ATTR_HALBTON)).intValue();
				String art = eleAkkord.attributeValue(ATTR_ART);
				String klang = eleAkkord.attributeValue(ATTR_KLANG);
				akk.setAkkord(ton, halbton, art, klang);
			} 
			catch (Exception e)
			{
				akk = null;
			} 
			if ((akk == null) || (! akk.isValid())) 
			{
				JOptionPane.showMessageDialog(parent, 
										"Falsche Song-Parameter (Akkord)", 
										"Fehler in der Title-Datei", 
										JOptionPane.ERROR_MESSAGE);
				return false;
			}

			vAkk.add(akk);
//			System.out.println("----------"+akk.toString());
		}
		takt.setAkkord(vAkk);
		return true;
	}
	
	/**
	 * Xml: bestimmen der Attribute des Text-Schemas, wie Kommentar
	 * @param xmlr
	 * @param element
	 * @param song
	 * @return
	 */
	private boolean bestimmeText(AXmlReader xmlr, Element element, Song song)
	{
		boolean retValue = true;
		Vector elemSchemaText = xmlr.getElementList(element, TAG_TEXT);
		for (int j=0;j<elemSchemaText.size();j++)
		{
			TextSchema textSchema = null;
			Element eleSchemaTeil = (Element)elemSchemaText.elementAt(j);
			try
			{
				int anzahl = new Integer(eleSchemaTeil.attributeValue(ATTR_ANZAHLZEILEN)).intValue();
				textSchema = new TextSchema(anzahl);
				textSchema.setHeightAnteil(new Integer(eleSchemaTeil.attributeValue(ATTR_HEIGHTANTEIL)).intValue());
				textSchema.setHeightAnteilZweiteSeite(new Integer(eleSchemaTeil.attributeValue(ATTR_TEXTHEIGHTANTEIL)).intValue());
				textSchema.setTextAnzeige(new Integer(eleSchemaTeil.attributeValue(ATTR_TEXTANZEIGE)).intValue());
				textSchema.setKommentar(getKommentare(xmlr, eleSchemaTeil));
			} 
			catch (Exception e)
			{
				textSchema = null;
			} 
			if (textSchema == null) 
			{
				JOptionPane.showMessageDialog(parent, 
										"Falsche Song-Parameter (Text)", 
										"Fehler in der Title-Datei", 
										JOptionPane.ERROR_MESSAGE);
				return false;
			}

//			System.out.println("--------"+textSchema.toString());
			
			song.setText(textSchema);
			retValue = bestimmeTextZeilen(xmlr, eleSchemaTeil, textSchema);
			if (! retValue)
				return false;
		}
		return retValue;
	}
	
	/**
	 * Xml: bestimmen der Attribute des Textes
	 * @param xmlr
	 * @param element
	 * @param txt
	 * @return
	 */
	private boolean bestimmeTextZeilen(AXmlReader xmlr, Element element, TextSchema txt)
	{
		Vector elemZeilen = xmlr.getElementList(element, TAG_ZEILE);
		
		for (int j=0;j<elemZeilen.size();j++)
		{
			TextZeile zeile = null;
			Element eleZeile = (Element)elemZeilen.elementAt(j);
			try
			{
				zeile = new TextZeile();
				zeile.setText(eleZeile.attributeValue(ATTR_WORT));
			} 
			catch (Exception e)
			{
				zeile = null;
			} 
			if ((zeile == null) || (! zeile.isValid())) 
			{
				JOptionPane.showMessageDialog(parent, 
										"Falsche Song-Parameter (TextZeile)", 
										"Fehler in der Title-Datei", 
										JOptionPane.ERROR_MESSAGE);
				return false;
			}

//			System.out.println("----------"+zeile.toString());
			
			txt.addZeile(zeile);
		}
		
		if (! txt.isValid())
		{
			JOptionPane.showMessageDialog(parent, 
					"Falsche Song-Parameter (TextZeile)", 
					"Fehler in der Title-Datei", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}

	/**
	 * Xml: bestimmen der Attribute des Kommentares
	 * @param xmlr
	 * @param element
	 * @return
	 */
	private Kommentar getKommentare(AXmlReader xmlr, Element element)
	{
		Kommentar retKomm = new Kommentar();

		Vector v = xmlr.getElementList(element, TAG_KOMMENTAR);
		if ((v != null) && (v.size() > 0))
		{
			Element eleSchemaKomm = (Element)v.elementAt(0);
			retKomm.setAnzahlZeilen(new Integer(eleSchemaKomm.attributeValue(ATTR_ANZAHLZEILEN)).intValue());
			retKomm.setHeightAnteil(new Integer(eleSchemaKomm.attributeValue(ATTR_HEIGHTANTEIL)).intValue());
			retKomm.setTextAnzeige(new Integer(eleSchemaKomm.attributeValue(ATTR_TEXTANZEIGE)).intValue());
			Vector vZeilen = xmlr.getElementList(eleSchemaKomm, TAG_ZEILE);
			for (int j=0;j<vZeilen.size();j++)
			{
				TextZeile zeile = null;
				Element eleZeile = (Element)vZeilen.elementAt(j);
				zeile = new TextZeile();
				zeile.setText(eleZeile.attributeValue(ATTR_WORT));
				retKomm.addZeile(zeile);
			}
		}

		return retKomm;
	}
}
