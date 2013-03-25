/*
 * 25.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Diese Klasse bietet ein paar kleine Komfort-Funktionen,
 * um IconImages zubauen. 
 * (und spart das lästige try-catchen bei den LLogicalUrl's)
 * 
 */
package AChord.AClasses;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;

public class AIconManager 
{
	public static final ImageIcon ICON_NEXT = getIconFromClasspathImages("Next.gif");
	public static final ImageIcon ICON_PREVIOUS = getIconFromClasspathImages("Previous.gif");
	public static final ImageIcon ICON_ARROW_UP = getIconFromClasspathImages("blackuparrow.gif");
	public static final ImageIcon ICON_ARROW_DOWN = getIconFromClasspathImages("blackdownarrow.gif");
	public static final ImageIcon ICON_TAKT_LOGO = getIconFromClasspathImages("takt.jpg");
	public static final ImageIcon ICON_NEU = getIconFromClasspathImages("TBNewIcon.gif");
	public static final ImageIcon ICON_OPEN = getIconFromClasspathImages("TBOpenIcon.gif");
	public static final ImageIcon ICON_QUIT = getIconFromClasspathImages("TBQuitIcon.gif");
	public static final ImageIcon ICON_SAVE = getIconFromClasspathImages("TBSaveIcon.gif");
	public static final ImageIcon ICON_PRINT = getIconFromClasspathImages("TBPrintIcon.gif");
	public static final ImageIcon ICON_INFO = getIconFromClasspathImages("Info.gif");
	public static final ImageIcon ICON_HILFE = getIconFromClasspathImages("TBHelpIcon.gif");
	public static final ImageIcon ICON_TRANSP = getIconFromClasspathImages("TBBearbeitenIcon.gif");
	public static final ImageIcon ICON_COPY = getIconFromClasspathImages("copy.gif");
	public static final ImageIcon ICON_PASTE = getIconFromClasspathImages("paste.gif");
	public static final ImageIcon ICON_TRASH = getIconFromClasspathImages("trash.gif");
	public static final ImageIcon ICON_NOTENPULT = getIconFromClasspathImages("NotenPult.gif");
	public static final Image ICON_TAKT = getImageFromClasspathImages("takticon.jpg");
	
	private final static AIconManager INSTANCE = new AIconManager();
	public static AIconManager getInstance()
	{
		return INSTANCE;
	}

	public AIconManager()
	{
		init();
	}

	private void init()
	{
	}

	/**
	 * Lies ein ImageIcon im %Classpath%/images Verzeichnis
	 * @param iconFileName
	 * @return das ImageIcon bzw. null, wenn es nicht gelesen werden kann
	 */
	public static ImageIcon getIconFromClasspathImages(String iconFileName)
	{
		return getIcon("AChord/images/"+iconFileName);
	}

	public static Image getImageFromClasspathImages(String iconFileName)
	{
		return getImage("AChord/images/"+iconFileName);
	}

	/**
	 * Diese Methode macht die eigentliche Arbeit und holt das Icon...
	 * @param iconFileName
	 * @return
	 */
	private static ImageIcon getIcon(String iconFileName)
	{
		URL iconURL = ClassLoader.getSystemResource(iconFileName);
		if (iconURL != null)
			return new ImageIcon(iconURL);
		else
			System.out.println("-ERR-Icon konnte nicht geladen werden: " + iconFileName + " :IconURL = "+iconURL);
		return null;
	}
	private static Image getImage(String iconFileName)
	{
		URL iconURL = ClassLoader.getSystemResource(iconFileName);
		if (iconURL != null)
		{
			Image img = Toolkit.getDefaultToolkit().getImage(iconURL);
			return img;
		}
		else
			System.out.println("-ERR-Image konnte nicht geladen werden: " + iconFileName + " :IconURL = "+iconURL);
		return null;
	}
}
