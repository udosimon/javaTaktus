/*
 * TAKTUS
 * 
 * 25.2.2008
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Druck-Dialog
 * 
 * 
 */

package AChord.Print;

import java.awt.Color;
import java.awt.Frame;
import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import AChord.AClasses.ADialog;
import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;
import AChord.Schema.Song;
import AChord.Schema.SongText;
import AChord.xml.AXmlSongReadBroker;

public class DialogDruck extends ADialog
{
	Frame parent;
	APanel druckPanel;

	public DialogDruck(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		this.parent = parent;
		construct();
	}
	
	/**
	 * 
	 */
	private void construct()
	{
		try
		{
			initForm();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		this.getContentPane().setLayout(null);
		this.setSize(350, 20);
		this.getContentPane().add(getJPanelDruck());
	}

	/**
	 * @return
	 */
	private APanel getJPanelDruck()
	{
		if (druckPanel == null)
		{
			druckPanel = new APanel();
			druckPanel.setLayout(null);
			druckPanel.setBorder(new LineBorder(Color.black));
		}
		return druckPanel;
	}

	/**
	 * @param f
	 */
	public void setDaten(Song song)
	{
		this.setVisible(true);
		PrintUtility pu = new PrintUtility();
    	preparePrintFile(song, pu);
		setVisible(false);
		pu.print();
		pu.clearComponentList();
		dispose();
	}
	
	/**
	 * @param retFiles
	 */
	public void setDaten(File[] retFiles)
	{
		this.setVisible(true);
		final PrintUtility pu = new PrintUtility();
        for (int i = 0; i < retFiles.length; i++)
		{
			File f = retFiles[i];
			Song songAuswahlDruck;
			songAuswahlDruck = AXmlSongReadBroker.getInstance().readSongFromXml(f, this);
        	preparePrintFile(songAuswahlDruck, pu);
		}
		setVisible(false);
		pu.print();
		pu.clearComponentList();
		dispose();
	}

	/**
	 * @param Song songAuswahlDruck
	 * @param PrintUtility pu
	 */
	private void preparePrintFile(Song songAuswahlDruck, PrintUtility pu)
	{
		if (songAuswahlDruck != null)
		{
			songAuswahlDruck.setWirdGedruckt();
			songAuswahlDruck.setLocation(0, 0);
			songAuswahlDruck.setVisible(true);
			songAuswahlDruck.setLayout(null);
			songAuswahlDruck.setSizeBreiteA4(750);
			getJPanelDruck().add(songAuswahlDruck);
			
			if (Konfiguration.getInstance().isShowRahmenImDruck())
			{
				songAuswahlDruck.setRahmen();
				if (songAuswahlDruck.getSchema() != null)
					songAuswahlDruck.getSchema().setRahmen();
				if (songAuswahlDruck.getText() != null)
					songAuswahlDruck.getText().setRahmen();
			}

			songAuswahlDruck.konstruierBlatt(false);
			songAuswahlDruck.handleSongText();

			songAuswahlDruck.setBackground(Color.WHITE);
			songAuswahlDruck.setForeground(Color.BLACK);

			pu.addComponentToPrint(songAuswahlDruck);

			songAuswahlDruck.repaint();

			if (songAuswahlDruck.getText().istTextAufZweiterSeite())
			{
				final SongText songSeite2 = songAuswahlDruck.getSongText().transientCloneText();
				songSeite2.setLocation(1, songAuswahlDruck.getHeight());
				songSeite2.setVisible(true);
				songSeite2.setLayout(null);
				songSeite2.setSizeBreiteA4(750);
				songSeite2.konstruierTextBlatt(false);
				songSeite2.setBackground(Color.WHITE);
				songSeite2.setForeground(Color.BLACK);
				getJPanelDruck().add(songSeite2);
				
				pu.addComponentToPrint(songSeite2);
			}
		}
	}
}
