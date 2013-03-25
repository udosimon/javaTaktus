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
 * Darstellung einer Kommentarzeile unterhalb des Takt-Schemas bzw.
 * unterhalb des Text-Schemas
 */
package AChord.Schema;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JLabel;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;

public class Kommentar extends APanel
{
    private final static long serialVersionUID = 1;

    final static int ZEILE_MINDEST_ABSTAND = -5;
	final static int ZEILE_MAXIMAL_ABSTAND = 20;
	final static int ZEILE_GROESSTE_HOEHE = 30;

	public final static int KOMM_ANZEIGE_OHNE = 0;
	public final static int KOMM_ANZEIGE_MIT = 1;
	final static int KOMM_FONT_GROESSE = 16;
	
	int heightAnteil = 0;
	int anzahlZeilen = 0;
	
	Vector textZeilen = new Vector();
	int textAnzeige = KOMM_ANZEIGE_OHNE;
	
	int fontGroesse;
	int minZeilenAbstand;

	/**
	 * @param anzahlZeilen
	 */
	public Kommentar(int anzahlZeilen)
	{
		super();
		this.anzahlZeilen = anzahlZeilen;
	}

	/**
	 * @param anzahlZeilen
	 */
	public Kommentar()
	{
		super();
		this.anzahlZeilen = 0;
		textZeilen = new Vector();
		heightAnteil = 0;
		textAnzeige = KOMM_ANZEIGE_OHNE;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void setKommentarSize(int x, int y)
	{
		this.setSize(x, y);
		this.setOpaque(false);
	}

	/**
	 * @return the heightAnteil
	 */
	public int getHeightAnteil()
	{
		return heightAnteil;
	}

	/**
	 * @param heightAnteil the heightAnteil to set
	 */
	public void setHeightAnteil(int heightAnteil)
	{
		this.heightAnteil = heightAnteil;
	}

	/**
	 * @return the anzahlZeilen
	 */
	public int getAnzahlZeilen()
	{
		return anzahlZeilen;
	}

	/**
	 * @param anzahlZeilen the anzahlZeilen to set
	 */
	public void setAnzahlZeilen(int anzahlZeilen)
	{
		this.anzahlZeilen = anzahlZeilen;
	}

	/**
	 * @return the textZeilen
	 */
	public Vector getTextZeilen()
	{
		return textZeilen;
	}

	/**
	 * @param textZeilen the textZeilen to set
	 */
	public void setTextZeilen(Vector textZeilen)
	{
		this.textZeilen = textZeilen;
	}
	
	/**
	 * @param zeile
	 */
	public void addZeile(TextZeile zeile)
	{
		this.setAenderungIstErfolgt();

		this.textZeilen.add(zeile);
		this.anzahlZeilen = textZeilen.size();
	}

	/**
	 * 
	 */
	public void removeZeilen()
	{
		for (int i = 0; i < this.textZeilen.size(); i++)
		{
			TextZeile zeile = (TextZeile)this.textZeilen.elementAt(i);
			this.remove(zeile);
		}
	}

	/**
	 * 
	 */
	public void removeAndClearZeilen()
	{
		removeZeilen();
		this.textZeilen.clear();
	}

	/**
	 * @return the fontGroesse
	 */
	public int getFontGroesse()
	{
		return fontGroesse;
	}

	/**
	 * @param fontGroesse the fontGroesse to set
	 */
	public void setFontGroesse(int fontGroesse)
	{
		this.fontGroesse = fontGroesse;
	}

	/**
	 * @return the minZeilenAbstand
	 */
	public int getMinZeilenAbstand()
	{
		return minZeilenAbstand;
	}

	/**
	 * @param minZeilenAbstand the minZeilenAbstand to set
	 */
	public void setMinZeilenAbstand(int minZeilenAbstand)
	{
		this.minZeilenAbstand = minZeilenAbstand;
	}
	
	/**
	 * 
	 */
	public void konstruiereKommentar()
	{
//		bestimmeFontGroesse();
		fontGroesse = KOMM_FONT_GROESSE;
		minZeilenAbstand = 2;
		
		TextZeile zeile;
		
		for (int i = 0; i < this.textZeilen.size(); i++)
		{
			zeile = (TextZeile)this.textZeilen.elementAt(i);
			Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.BOLD, fontGroesse);
			zeile.setFont(f);
			zeile.setSize(this.getWidth(), KOMM_FONT_GROESSE+2);
			zeile.setLocation(0, (i * (KOMM_FONT_GROESSE) + minZeilenAbstand));
			zeile.setHorizontalAlignment(JLabel.LEFT);

			this.add(zeile);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if (this.textZeilen != null)
			if (this.anzahlZeilen != this.textZeilen.size())
				return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		String st = "**";
		if (this.getTextZeilen().size() > 0)
		{
			TextZeile zei = (TextZeile)this.getTextZeilen().elementAt(0);
			st += zei.getText();
		}

		return "Kommentar " + this.textAnzeige + "/" + st;
	}
	

	/**
	 * @return the textAnzeige
	 */
	public int getTextAnzeige()
	{
		return textAnzeige;
	}

	/**
	 * @param textAnzeige the textAnzeige to set
	 */
	public void setTextAnzeige(int textAnzeige)
	{
		this.setAenderungIstErfolgt();
		this.textAnzeige = textAnzeige;
	}
	
	/**
	 * @return
	 */
	public boolean istTextAnzeige()
	{
		if (this.textAnzeige == KOMM_ANZEIGE_MIT)
			return true;
		else
			return false;
	}

	public Kommentar transientClone()
	{
		Kommentar komm = new Kommentar();
		komm.setTextAnzeige(this.textAnzeige);
		komm.setAnzahlZeilen(this.anzahlZeilen);
		komm.setHeightAnteil(this.heightAnteil);
		for (int i=0; i<this.textZeilen.size(); i++)
		{
			TextZeile zeile = (TextZeile)this.textZeilen.elementAt(i);
			komm.addZeile(zeile.transientClone());
		}
		return komm;
	}
}
