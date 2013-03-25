/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
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
 * XmlElement mit Konfiguration-Layout-Daten 
 */
package AChord.xml;
import org.dom4j.tree.DefaultElement;

import AChord.Hilfe.Konfiguration;

public class AXmlElementKonfLayout extends DefaultElement
{
	private static final long serialVersionUID = 1L;
	/** Element-Name */
	public static final String ELEMENT_NAME = AXml.TAG_K_LAYOUT;

	public AXmlElementKonfLayout(Konfiguration konf)
	{
		super(ELEMENT_NAME);
		this.addAttribute(AXml.ATTR_K_TAKTLEER, ""+konf.getKonfAnzeigeNeuerTakt());
		this.addAttribute(AXml.ATTR_K_BAKKORD, ""+konf.getAnzeigeBimAkkord());
		this.addAttribute(AXml.ATTR_K_BPANEL, ""+konf.isIstAnzeigeBimPanel());
		this.addAttribute(AXml.ATTR_K_BORDERSONG, ""+konf.isShowRahmenImSong());
		this.addAttribute(AXml.ATTR_K_BORDERDRUCK, ""+konf.isShowRahmenImDruck());
		this.addAttribute(AXml.ATTR_K_TEILABSTAND, ""+konf.getAbstandZwischenTeilen());
		this.addAttribute(AXml.ATTR_K_TEILABSTANDVOR, ""+konf.getAbstandVorErstemTeil());
		this.addAttribute(AXml.ATTR_K_TEILABSTANDVORTEXT, ""+konf.getAbstandVorText());
	}
}

