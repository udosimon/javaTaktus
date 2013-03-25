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
 * XmlElement mit Akkord-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.Akkord;

public class AXmlElementAkkord extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_AKKORD;

	public AXmlElementAkkord(Akkord akk)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_TON, ""+akk.getAkkTon());
		this.addAttribute(AXml.ATTR_HALBTON, ""+akk.getAkkHalbton());
		this.addAttribute(AXml.ATTR_ART, akk.getAkkArt());
		this.addAttribute(AXml.ATTR_KLANG, akk.getAkkKlang());
	}
}

