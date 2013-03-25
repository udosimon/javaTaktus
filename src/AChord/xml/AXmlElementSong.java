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
 * XmlElement mit Song-Detail-Kopf-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.Song;

public class AXmlElementSong extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_TITEL;

	public AXmlElementSong(Song song)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_NAME, song.getTitel());
		this.addAttribute(AXml.ATTR_AUTOR, song.getAutor());
		this.addAttribute(AXml.ATTR_HEIGHTANTEIL, ""+song.getTitelHeightAnteil());
		this.addAttribute(AXml.ATTR_WIDTH, ""+song.getWidth());
		this.addAttribute(AXml.ATTR_HEIGHT, ""+song.getHeight());
	}
}
