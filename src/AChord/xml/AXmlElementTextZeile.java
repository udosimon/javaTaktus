/*
 * TAKTUS
 * 
 * 13.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * XmlElement mit TextZeilen-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.TextZeile;

public class AXmlElementTextZeile extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_ZEILE;

	public AXmlElementTextZeile(TextZeile zeile)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_WORT, zeile.getText());
	}
}
