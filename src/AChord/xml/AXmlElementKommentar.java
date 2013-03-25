/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 26.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * XmlElement mit Kommentar-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.Kommentar;

public class AXmlElementKommentar extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_KOMMENTAR;

	public AXmlElementKommentar(Kommentar komm)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_ANZAHLZEILEN, ""+komm.getAnzahlZeilen());
		this.addAttribute(AXml.ATTR_HEIGHTANTEIL, ""+komm.getHeightAnteil());
		this.addAttribute(AXml.ATTR_TEXTANZEIGE, ""+komm.getTextAnzeige());
	}

}
