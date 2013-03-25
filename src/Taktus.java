/*
 * TAKTUS
 * 
 * 1.9.2007
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Main
 * 
 * 
 */
import javax.swing.*;


import AChord.MainFrame.AChordFrame;
public class Taktus
{
	/**
	 * Haupteinsprungpunkt 
	 *
	 */
	public static void main (String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}

		AChordFrame f = new AChordFrame();
		f.setVisible(true);
		f.initSong();
	}
}
