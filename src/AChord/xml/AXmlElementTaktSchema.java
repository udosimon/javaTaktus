/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 13.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * XmlElement mit TaktSchema-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.Song;

public class AXmlElementTaktSchema extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_TAKTSCHEMA;

	public AXmlElementTaktSchema(Song song)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_ANZAHLTEILE, ""+song.getSchema().getAnzahlTeile());
		this.addAttribute(AXml.ATTR_TAKTEPROZEILE, ""+song.getSchema().getTakteProZeile());
		this.addAttribute(AXml.ATTR_TON, ""+song.getSchema().getTonArt());
		this.addAttribute(AXml.ATTR_HALBTON, ""+song.getSchema().getTonHalbTon());
		this.addAttribute(AXml.ATTR_ART, song.getSchema().getTonGeschlecht());
		this.addAttribute(AXml.ATTR_ANZEIGETONART, song.getSchema().istAnzeigeTonArt());
		this.addAttribute(AXml.ATTR_HEIGHTANTEIL, ""+song.getSchema().getHeightAnteil());
	}
}

