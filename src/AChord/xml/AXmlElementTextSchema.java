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
 * XmlElement mit TextSchema-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.Song;

public class AXmlElementTextSchema extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_TEXT;

	public AXmlElementTextSchema(Song song)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_ANZAHLZEILEN, ""+song.getText().getAnzahlZeilen());
		this.addAttribute(AXml.ATTR_HEIGHTANTEIL, ""+song.getText().getHeightAnteil());
		this.addAttribute(AXml.ATTR_TEXTANZEIGE, ""+song.getText().getTextAnzeige());
		this.addAttribute(AXml.ATTR_TEXTHEIGHTANTEIL, ""+song.getSongText().getText().getHeightAnteilZweiteSeite());
	}
}
