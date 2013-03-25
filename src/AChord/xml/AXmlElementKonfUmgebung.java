/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 4.12.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * XmlElement mit Konfiguration-Umgebungs-Daten 
 */
package AChord.xml;
import org.dom4j.tree.DefaultElement;

import AChord.Hilfe.Konfiguration;

public class AXmlElementKonfUmgebung extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_K_UMGEBUNG;

	public AXmlElementKonfUmgebung(Konfiguration konf)
	{
		super(ELEMENT_NAME);
		if (konf.getTaktusDirectory() == null)
			this.addAttribute(AXml.ATTR_K_DIRTAKTUS, "---");
		else
			this.addAttribute(AXml.ATTR_K_DIRTAKTUS, konf.getTaktusDirectory());
	}
}
