/*
 * TAKTUS
 *
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 05.10.2007 
 *
 * Beschreibung:
 * 
 * Der im Hauptframe auf Reiter 2 (Schema-Teile) dargestellte Panel
 * 
 * Hier werden einzelne Song-Teile modifiziert, hinzugefügt und gelöscht
 */
package AChord.MainFrame;

import javax.swing.SwingUtilities;

import AChord.AClasses.APanel;
import AChord.Schema.Kommentar;
import AChord.Schema.Song;
import AChord.Schema.TaktSchemaTeil;

public class PanelReiter2Schema extends APanel
{
    private final static long serialVersionUID = 1;

    Song song;

	/**
	 * Konstruktor
	 */
	public PanelReiter2Schema()
	{
		super();
		construct();
	}

	/**
	 * Initialisierungen des Frames
	 */
	private void construct()
	{
		try
		{
			initForm();
			myInit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Aufbau der Oberfläche
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		this.setPreferredSize(new java.awt.Dimension(452, 682));
		this.setLayout(null);
		setFont(getFontA());
	}

	/**
	 *  weitere Initialisierungen aus Konstruktor
	 */
	private void myInit()
	{
		
	}

	/**
	 * Aufruf von außen, wenn ein Song neu geladen wird, damit
	 * die entsprechenden Attribute hier im Panel korrekt 
	 * dargestellt werden.
	 * @param song
	 */
	public void setDaten(Song song)
	{
		this.removeAll();
		this.song = song;
		resetTeile();
	}

	/**
	 * Die einzelnen Teile im Reiter "Teile" aus Song laden und darstellen
	 * @param song
	 */
	private void resetTeile()
	{
		for (int i=0; i<song.getTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)song.getTeile().elementAt(i);
			PanelReiter2SchemaTeil pteil = new PanelReiter2SchemaTeil(i, teil);
			
			this.add(pteil);
		}
		Kommentar komm = song.getSchema().getKommentar();
		PanelReiterKommentar pKomm = new PanelReiterKommentar(this, komm);
		this.add(pKomm);
		
		this.repaint();
	}
	
	/**
	 * wird aufgerufen, wenn sich auf Grund eines Click-Ereignisses eines
	 * RadioButtons das Layout des dargestellten Songs ändert
	 */
	public void resetSong()
	{
		song.getSchema().berechneAnteiligeHoehen();
		song.aktualisiereBlatt();
		song.setRahmenAlleSchemas();
		song.reloadTaktSchema();
	}
	
	/**
	 * hinzufügen eines neuen Teils
	 * @param neuerTeil
	 * @param istObenUnten
	 * @param teilAltIndex
	 */
	public void neuerTeil(TaktSchemaTeil neuerTeil, 
								int istObenUnten, 
								int teilAltIndex)
	{
		if (song.getTeile().size() >= 6)
			return;
		int index = teilAltIndex;
		if (istObenUnten == 2)
			index++;
		this.removeAll();
		song.getTeile().insertElementAt(neuerTeil, index);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				resetTeile();
				resetSong();
			}
		});
	}
	
	/**
	 * löschen eines Teils; min. ein Teil bleibt erhalten
	 * @param loeschTeil
	 * @param teilAltIndex
	 */
	public void loescheTeil(TaktSchemaTeil loeschTeil, int teilAltIndex)
	{
		if (song.getTeile().size() <= 1) // min. ein Teil muss erhalten bleiben
			return;
		
		this.removeAll();
		song.getTeile().remove(teilAltIndex);
		song.getSchema().removeTaktSchemaTeil(loeschTeil);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				resetTeile();
				resetSong();
				((AChordFrame)SwingUtilities.windowForComponent(PanelReiter2Schema.this)).setAkkordVonAussen();
			}
		});
	}
}
