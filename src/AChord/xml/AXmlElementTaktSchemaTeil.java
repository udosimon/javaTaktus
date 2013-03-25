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
 * XmlElement mit TaktSchemaTeil-Daten 
 */
package AChord.xml;

import org.dom4j.tree.DefaultElement;

import AChord.Schema.TaktSchemaTeil;

public class AXmlElementTaktSchemaTeil extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_TAKTSCHEMATEIL;

	public AXmlElementTaktSchemaTeil(TaktSchemaTeil teil)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_TEILART, ""+teil.getTeilArt());
		this.addAttribute(AXml.ATTR_ANZAHLTAKTE, ""+teil.getTaktAnzahl());
		this.addAttribute(AXml.ATTR_NAME, teil.getTeilName());
		this.addAttribute(AXml.ATTR_WIEDERHOLUNG, ""+teil.istWiederholung());
		this.addAttribute(AXml.ATTR_ANZAHLTAKTEKASTEN, ""+teil.getAnzahlTakteKasten());
		this.addAttribute(AXml.ATTR_HEIGHTANTEIL, ""+teil.getHeightAnteil());
		this.addAttribute(AXml.ATTR_KOPFNR, ""+teil.getKopfTakt());
	}
}


