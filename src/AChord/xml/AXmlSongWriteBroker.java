/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 12.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Schreiben der Taktus-Xml-Datei
 */
package AChord.xml;

import java.awt.Component;
import java.io.File;

import org.dom4j.Element;

import AChord.Hilfe.DialogInfo;
import AChord.Schema.Song;
import AChord.Schema.Takt;
import AChord.Schema.TaktSchemaTeil;
import AChord.Schema.TextZeile;

public class AXmlSongWriteBroker extends AXml
{
	private static AXmlSongWriteBroker INSTANCE = new AXmlSongWriteBroker();
	
	Component parent;
	
	/**
	 * Singleton
	 * @return
	 */
	public static AXmlSongWriteBroker getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Konstruktor
	 */
	public AXmlSongWriteBroker()
	{
		super();
	}

	/**
	 * Aufruf von aussen: schreiben einer Taktus-Song-Datei
	 * @param xmlFile
	 * @param song
	 * @param parent
	 */
	public void writeSongFromXml(File xmlFile, Song song, Component parent)
	{
		this.parent = parent;
		
		AXmlWriter xmlw = new AXmlWriter(xmlFile);
		
		// Schreiben des XML-Headers
		xmlw.addComment("=======================================================================");
		xmlw.addComment("    Taktus - XmlDatei eines Titels - ");
		xmlw.addComment("    Taktus - Version: " + DialogInfo.VERSIONS_NUMMER);
		xmlw.addComment("=======================================================================");
		
		Element root = xmlw.addRoot(this.TAG_ROOT);

		// Attribute des Songs
		AXmlElementSong eleSong = new AXmlElementSong(song);
		xmlw.addElement2Element(root, eleSong);
		
		// Attribute des Akkord-Schemas
		AXmlElementTaktSchema eleTaktSchema = new AXmlElementTaktSchema(song);
		xmlw.addElement2Element(root, eleTaktSchema);
		
		// Kommentar unter Akkord-Schema
		AXmlElementKommentar eleKomm = new AXmlElementKommentar(song.getSchema().getKommentar());
		xmlw.addElement2Element(eleTaktSchema, eleKomm);
		for (int m=0;m<song.getSchema().getKommentar().getTextZeilen().size();m++)
		{
			TextZeile zeile = (TextZeile)song.getSchema().getKommentar().getTextZeilen().elementAt(m);
			AXmlElementTextZeile eleZeile = new AXmlElementTextZeile(zeile);
			xmlw.addElement2Element(eleKomm, eleZeile);
		}
		
		for (int i=0; i<song.getSchema().getTaktSchemaTeile().size(); i++)
		{
			// Akkord-Schema-Teil
			TaktSchemaTeil taktTeil = (TaktSchemaTeil)song.getSchema().getTaktSchemaTeile().elementAt(i);
			AXmlElementTaktSchemaTeil eleSchemaTeil = new AXmlElementTaktSchemaTeil(taktTeil);
			xmlw.addElement2Element(eleTaktSchema, eleSchemaTeil);
			
			for (int j=0; j<taktTeil.getAlleTakte().size(); j++)
			{
				// Takt
				Takt takt = (Takt)taktTeil.getAlleTakte().elementAt(j);
				AXmlElementTakt eleTakt = new AXmlElementTakt(takt);
				xmlw.addElement2Element(eleSchemaTeil, eleTakt);
				
				// bis zu 4 Akkorde pro Takt
				AXmlElementAkkord eleAkk;
				if (takt.getAkkord1() != null)
				{
					eleAkk = new AXmlElementAkkord(takt.getAkkord1());
					xmlw.addElement2Element(eleTakt, eleAkk);
				}
				if (takt.getAkkord2() != null)
				{
					eleAkk = new AXmlElementAkkord(takt.getAkkord2());
					xmlw.addElement2Element(eleTakt, eleAkk);
				}
				if (takt.getAkkord3() != null)
				{
					eleAkk = new AXmlElementAkkord(takt.getAkkord3());
					xmlw.addElement2Element(eleTakt, eleAkk);
				}
				if (takt.getAkkord4() != null)
				{
					eleAkk = new AXmlElementAkkord(takt.getAkkord4());
					xmlw.addElement2Element(eleTakt, eleAkk);
				}
			}
		}
		
		// Attribute des Text-Schemas
		AXmlElementTextSchema eleText = new AXmlElementTextSchema(song);
		xmlw.addElement2Element(root, eleText);

		// Kommentar unterhalb des Text-Schemas
		AXmlElementKommentar eleKommText = new AXmlElementKommentar(song.getText().getKommentar());
		xmlw.addElement2Element(eleText, eleKommText);
		for (int m=0;m<song.getText().getKommentar().getTextZeilen().size();m++)
		{
			TextZeile zeile = (TextZeile)song.getText().getKommentar().getTextZeilen().elementAt(m);
			AXmlElementTextZeile eleZeile = new AXmlElementTextZeile(zeile);
			xmlw.addElement2Element(eleKommText, eleZeile);
		}
		
		for (int k=0; k<song.getText().getZeilen().size(); k++)
		{
			// Textzeile
			TextZeile zeile = (TextZeile)song.getText().getZeilen().elementAt(k);
			AXmlElementTextZeile eleZeile = new AXmlElementTextZeile(zeile);
			xmlw.addElement2Element(eleText, eleZeile);
		}
		
		// schreiben der Taktus-Xml-Datei
		xmlw.write();
		
	}
}
