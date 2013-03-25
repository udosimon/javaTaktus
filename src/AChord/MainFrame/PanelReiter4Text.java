/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 17.11.2007
 *  
 * Beschreibung:
 * 
 * Der im Hauptframe auf Reiter 4 (Text) dargestellte Panel
 */
package AChord.MainFrame;

import AChord.AClasses.APanel;
import AChord.AClasses.ATextArea;
import AChord.Schema.Kommentar;
import AChord.Schema.Song;
import AChord.Schema.TextZeile;

import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;


public class PanelReiter4Text extends APanel 
{
    private final static long serialVersionUID = 1;

    Song song;
	private JScrollPane jScrollPanetext;
	private ATextArea jTextAreaText;

	boolean istLadePhase = false;
	PanelReiterKommentar pKomm;
	
	/**
	 * Konstruktor
	 */
	public PanelReiter4Text()
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
		this.add(getJScrollPaneText());
	}

	/**
	 * @return
	 */
	private JScrollPane getJScrollPaneText()
	{
		if (jScrollPanetext == null)
		{
			jScrollPanetext = new JScrollPane();
			jScrollPanetext.setBounds(36, 53, 410, 397);
			jScrollPanetext.setViewportView(getJTextAreaText());
		}
		return jScrollPanetext;
	}
	
	/**
	 * @return
	 */
	private ATextArea getJTextAreaText()
	{
		if (jTextAreaText == null)
		{
			jTextAreaText = new ATextArea();
			jTextAreaText.setFont(getFontB());
			PlainDocument docTitel = new PlainDocument();
			jTextAreaText.setDocument(docTitel);
			docTitel.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleTextChange(); }
				public void removeUpdate(DocumentEvent e) { handleTextChange(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		return jTextAreaText;
	}

	/**
	 * der geänderte Text wird im Song dargestellt
	 */
	public void handleTextChange()
	{
		if (istLadePhase)
			return;
		
		getJTextAreaText().initTextChange();

		if (song.getText() != null)
		{
			// in "song" wird der Text immer gespeichert, auch wenn
			// der Text auf der zweiten Seite angezeigt wird
			song.getText().removeAndClearZeilen();
			for (int i=0; i<getJTextAreaText().getLineCount(); i++)
			{
				TextZeile zeile = new TextZeile();
				zeile.setText(getJTextAreaText().getStringArray()[i]);
				song.getText().addZeile(zeile);
			}
			
			// wenn Text auf zweiter Seite dargestellt wird, dann
			// wird dort auch der Inhalt gesetzt und der Text konstruiert
			if (song.getText().istTextAufZweiterSeite())
			{
				song.getSongText().getText().removeAndClearZeilen();
				for (int i=0; i<getJTextAreaText().getLineCount(); i++)
				{
					TextZeile zeile = new TextZeile();
					zeile.setText(getJTextAreaText().getStringArray()[i]);
					song.getSongText().getText().addZeile(zeile);
				}

				song.getSongText().getText().konstruiereTextSchema();
				song.getSongText().getText().repaint();
			}
			else if (song.getText().istTextAufEsterSeite())
			{
				song.getText().konstruiereTextSchema();
				song.getText().repaint();
			}
		}

	}

	/**
	 * Aufruf von außen, wenn ein Song neu geladen wird, damit
	 * die entsprechenden Attribute hier im Panel korrekt 
	 * dargestellt werden.
	 * @param song
	 */
	public void setDaten(Song song)
	{
		istLadePhase = true;
	
		this.song = song;
		
		String st = "";
		if (song.getText() != null)
		{
			for (int i=0; i<song.getText().getZeilen().size(); i++)
			{
				TextZeile zeile = (TextZeile)song.getText().getZeilen().elementAt(i);
				st += zeile.getText();
				if ((i+1) < song.getText().getZeilen().size())
					st += "\n";
			}
		}
		getJTextAreaText().setText(st);
		
		if (pKomm != null)
			this.remove(pKomm);
		Kommentar komm = song.getText().getKommentar();
		pKomm = new PanelReiterKommentar(this, komm);
		this.add(pKomm);
		
		istLadePhase = false;
	}
	
	/**
	 * wird aufgerufen, wenn sich auf Grund eines Click-Ereignisses eines
	 * RadioButtons das Layout des dargestellten Songs ändert
	 */
	public void resetSong()
	{
		if (song.getText().istTextAufEsterSeite())
		{
			song.getText().konstruiereTextSchema();
			song.getText().repaint();
		}
		else if (song.getText().istTextAufZweiterSeite())
		{
			song.getSongText().getText().setKommentar(song.getText().getKommentar());
			song.getSongText().getText().konstruiereTextSchema();
			song.getSongText().getText().repaint();
		}
	}

}
