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
 * XmlElement mit Takt-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.Takt;

public class AXmlElementTakt  extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_TAKT;

	public AXmlElementTakt(Takt takt)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_TEILUNG, ""+takt.getTeilung());
		this.addAttribute(AXml.ATTR_BREAK, ""+takt.getBreakQuot());
		this.addAttribute(AXml.ATTR_BREAKTEXT, takt.getBreakText());
	}
}
