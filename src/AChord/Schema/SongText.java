/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 10.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * - abgeleitet von Song
 * - zeigt den Text an (2. Titelseite)
 * 
 */
package AChord.Schema;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import AChord.AClasses.APanel;

public class SongText extends Song
{
    private final static long serialVersionUID = 1;

    JSplitPane splitPane = new JSplitPane();

	/**
	 * Konstruktor
	 */
	public SongText()
	{
		super();
		schema = new TaktSchema(this);
		text = new TextSchema(0);
	}

	/**
	 * @param song
	 */
	public void setDaten(Song song)
	{
		this.setTitel(song.getTitel());
		this.setAutor(song.getAutor());
		this.setTitelHeightAnteil(song.getTitelHeightAnteil());
		this.setSongSize(song.getWidth(), song.getHeight());
		this.setSchema(song.getSchema().transientClone(null));
		this.setText(song.getText().transientClone());
	}

	/**
	 * 
	 */
	public void aktualisiereBlatt()
	{
		int fonthoehe = (titelGesamtHeight/2) - (titelGesamtHeight/15);
		if (autor.trim().length() > 0)
		{
			fonthoehe = (titelGesamtHeight/2) - (titelGesamtHeight/10);
		}

		// Titel
		berechneUndSetzeTitelGroesse(fonthoehe);

		// Titel-Tonart
		if ((this.getSchema().getAkkTonArtSong() != null) && 
			(this.getSchema().getAkkTonArtSong().isInBearbeitung()))
		{
			this.remove(this.getSchema().getAkkTonArtSong());
			setzeAkkordTitelTonArt();
			this.add(this.getSchema().getAkkTonArtSong());
		}
		else
		{
			this.remove(getSchema().getAkkTonArtSong());
			this.getSchema().getAkkTonArtSong().repaint();
		}

		//Autor
		if (autor.trim().length() > 0)
		{
			berechneUndSetzeAutorGroesse();
			this.add(labelAutor);
		}
		else
		{
			this.remove(labelAutor);
		}

		text.konstruiereTextSchema();
	}

	/**
	 * @param isSplit
	 */
	public void konstruierTextBlatt(boolean isSplit)
	{
		this.istFuerDruck = isSplit;
		// Titel/Autor*****************************************************
		// Höhe des Titelbereiches
		titelGesamtHeight = (int)((double)this.heightVerhaeltnis / 100 * titelHeightAnteil);
		// Font des Titels
		int fonthoehe = (titelGesamtHeight/2) - (titelGesamtHeight/15);
		if (autor.trim().length() == 0)
		{
			fonthoehe = (titelGesamtHeight/2) - (titelGesamtHeight/10);
		}

		// Titel
		berechneUndSetzeTitelGroesse(fonthoehe);
		this.add(labelTitel);
		
		// Titel-Tonart
		this.remove(getSchema().getAkkTonArtSong());
		if (this.getSchema().getAkkTonArtSong() != null)
		{
			setzeAkkordTitelTonArt();
			this.add(getSchema().getAkkTonArtSong());
		}

		//Autor
		if (autor.trim().length() > 0)
		{
			berechneUndSetzeAutorGroesse();
			this.add(labelAutor);
		}

		if (isSplit)
		{
			splitPane.setSize(this.getWidth(),this.getHeight()-titelGesamtHeight);
			splitPane.setLocation(0, titelGesamtHeight);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerSize(4);
			Color col = Color.BLUE;
			this.setSplitPaneDividerColor(splitPane, col);
			splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
			splitPane.setOpaque(false);
//	        splitPane.setOneTouchExpandable(true);
			splitPane.setContinuousLayout(true);
			this.add(splitPane);
		}

		// Text*******************************************************************
		int heightText = (int)((double)this.heightVerhaeltnis / 100 * text.getHeightAnteilZweiteSeite());
		text.setTextSchemaSize(this.widthVerhaeltnis, heightText);
		// Test, um Taktgröße zu erkennen
		text.setLayout(null);
		text.konstruiereTextSchema();

		if (isSplit)
		{
			text.setLocation(0, 0);
			splitPane.setTopComponent(text);
			splitPane.setDividerLocation(heightText);
			APanel pan = new APanel();
			if (text.getBorder() != null)
				pan.setRahmen();
			splitPane.setBottomComponent(pan);
		}
		else
		{
			text.setLocation(0, titelGesamtHeight);
			this.add(text);
		}

//		System.out.println("konstruiereBlatt Text: "+this.getTitelHeightAnteil()+"/"+
//				this.getSchema().getHeightAnteil()+"/"+
//				this.getText().getHeightAnteil()+"///"+
//				titelGesamtHeight + "/"+
//				this.getSchema().getHeight()+"/"+
//				this.getText().getHeight()+"**"+first);
		
		if ((isSplit) && (first))
		{
			first = false;
			initSplitPane();
		}
	}
	
	/**
	 * Initialisiert das SplitPane unterhalb des Textes auf Seite 2
	 * (vom Bediener während der Laufzeit einstellbar)
	 */
	private void initSplitPane()
	{
		text.addComponentListener(new ComponentListener()
		{
			public void componentResized(ComponentEvent e)
			{
				if (! inEventHandling)
				{
					inEventHandling = true;
					
					setAenderungIstErfolgt();

					int heightAnteilTexte = (100*text.getHeight()) / 
												(SongText.this.getHeight());
//					System.out.println("___________________________"+
//							text.getHeight()+"******"+
//							heightAnteilTexte);

					text.setHeightAnteilZweiteSeite(heightAnteilTexte);

					text.konstruiereTextSchema();
					inEventHandling = false;
				}
			}
			public void componentHidden(ComponentEvent e)
			{
			}
			public void componentMoved(ComponentEvent e)
			{
			}
			public void componentShown(ComponentEvent e)
			{
			}
		});

	}

	/**
	 * @return
	 */
	public SongText transientCloneText()
	{
		SongText songText = new SongText();
		songText.setTitel(this.titel);
		songText.setAutor(this.autor);
		songText.setTitelHeightAnteil(this.titelHeightAnteil);
		
		songText.setSchema(this.schema.transientClone(null));
		
		songText.setText(this.text.transientClone());
		
		return songText;
	}

}
