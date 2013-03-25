/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 21.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * Die Klasse TextSchema enthält:
 *  - den Text
 * 
 */
package AChord.Schema;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Vector;

import javax.swing.JLabel;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;


public class TextSchema extends APanel
{
    private final static long serialVersionUID = 1;

    final static int ZEILE_GROESSTE_HOEHE = 30;
	final static int ZEILE_MINDEST_ABSTAND = -3;
	final static int ZEILE_MAXIMAL_ABSTAND = 20;
	final static int ZEILE_LABEL_ANPASSUNG = 0;
	
	public final static int TEXT_ANZEIGE_OHNE = 0;
	public final static int TEXT_ANZEIGE_EINESEITE = 1;
	public final static int TEXT_ANZEIGE_ZWEISEITEN = 2;

	final static int HOEHE_KOMMENTAR = 24;
	
	int heightAnteil = 0;
	int heightAnteilZweiteSeite = 0;
	int anzahlZeilen = 0;
	int textAnzeige = TEXT_ANZEIGE_OHNE;
	
	Vector textZeilen = new Vector();
	
	int fontGroesse;
	int minZeilenAbstand;

	Kommentar kommentar = new Kommentar();

	/**
	 * Konstruktor
	 * @param anzahlZeilen
	 */
	public TextSchema(int anzahlZeilen)
	{
		super();
		this.anzahlZeilen = anzahlZeilen;
		kommentar = new Kommentar();
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void setTextSchemaSize(int x, int y)
	{
		this.setSize(x, y);
		this.setOpaque(false);
	}

	/**
	 * @return
	 */
	public int getTextAnzeige()
	{
		return textAnzeige;
	}

	/**
	 * @param textAnzeige
	 */
	public void setTextAnzeige(int textAnzeige)
	{
		this.setAenderungIstErfolgt();
		this.textAnzeige = textAnzeige;
	}
	
	/**
	 * @return
	 */
	public boolean istTextAufZweiterSeite()
	{
		if (textAnzeige == TEXT_ANZEIGE_ZWEISEITEN)
			return true;
		else
			return false;
	}

	/**
	 * @return
	 */
	public boolean istTextAufEsterSeite()
	{
		if (textAnzeige == TEXT_ANZEIGE_EINESEITE)
			return true;
		else
			return false;
	}
	
	/**
	 * @param heightAnteil
	 */
	public void setHeightAnteil(int heightAnteil)
	{
		this.heightAnteil = heightAnteil;
	}
	
	/**
	 * @return
	 */
	public int getHeightAnteil()
	{
		return this.heightAnteil;
	}
	
	/**
	 * @return
	 */
	public int getAnzahlZeilen()
	{
		return this.anzahlZeilen;
	}
	
	/**
	 * @param anzahl
	 */
	public void setAnzahlZeilen(int anzahl)
	{
		this.setAenderungIstErfolgt();
		this.anzahlZeilen = anzahl;
	}
	
	/**
	 * @param zeilen
	 */
	public void setZeilen(Vector zeilen)
	{
		textZeilen = zeilen;
		this.anzahlZeilen = textZeilen.size();
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
	 * @return
	 */
	public Vector getZeilen()
	{
		return this.textZeilen;
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
		this.remove(kommentar);
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
	 * konstruieren (Größe berechnen und hinzufügen) des Textes und des
	 * Kommentares
	 */
	public void konstruiereTextSchema()
	{
		bestimmeFontGroesse();
		
		TextZeile zeile;
		
		for (int i = 0; i < this.textZeilen.size(); i++)
		{
			zeile = (TextZeile)this.textZeilen.elementAt(i);
			Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.PLAIN, fontGroesse);
			zeile.setFont(f);
			zeile.setSize(this.getWidth(), fontGroesse + 3);
			zeile.setLocation(20, (i * (fontGroesse + minZeilenAbstand) + Konfiguration.getInstance().getAbstandVorText()));
			zeile.setHorizontalAlignment(JLabel.LEFT);

			this.add(zeile);
		}
		
		// Testausgabe
		String st = "-T-";
		if (kommentar.getTextZeilen().size() > 0)
		{
			TextZeile zei = (TextZeile)kommentar.getTextZeilen().elementAt(0);
			st += zei.getText();
		}
		
		if (kommentar.istTextAnzeige())
		{
			kommentar.setLayout(null);
			int kommHeight = HOEHE_KOMMENTAR;
			kommentar.setKommentarSize(this.getWidth(), kommHeight);
			kommentar.setLocation(50, this.getHeight()-kommHeight);
			kommentar.konstruiereKommentar();
			this.add(kommentar);
		}
		else
		{
			this.remove(kommentar);
		}
	}
	
	/**
	 * initialisieren des Textes, wenn neuer Song
	 */
	public void initZeile()
	{
		TextZeile zeile = new TextZeile();
		zeile.setText("");
		this.addZeile(zeile);
		
		kommentar.removeAndClearZeilen();
		kommentar.setTextAnzeige(Kommentar.KOMM_ANZEIGE_OHNE);
	}
	
	/**
	 * @param fontGroesse
	 * @return
	 */
	private int bestimmeZeilenAbstand(int fontGroesse)
	{
		int abstand = ZEILE_MINDEST_ABSTAND;
		
		int alleZeilenHoehe = 0;
		
		int height = this.getHeight() - Konfiguration.getInstance().getAbstandVorText() - 5;
		if (kommentar.istTextAnzeige())
			height -= HOEHE_KOMMENTAR;
		while ((alleZeilenHoehe < height) && (this.textZeilen.size() > 1))
		{
			alleZeilenHoehe = 0;
			abstand++;
			for (int i = 0; i < this.textZeilen.size(); i++)
			{
				alleZeilenHoehe += fontGroesse;
				if ((i+1) < this.getZeilen().size())
					alleZeilenHoehe += abstand;
			}
		}
		if (abstand > ZEILE_MAXIMAL_ABSTAND)
			abstand = ZEILE_MAXIMAL_ABSTAND;
		return abstand;
	}
	
	/**
	 * @return
	 */
	private int bestimmeFontGroesse()
	{
		int zeilenbreite = this.getWidth();
		fontGroesse = ZEILE_GROESSTE_HOEHE;
		while (zeilenbreite > this.getWidth()-75)
		{
			fontGroesse--;
			zeilenbreite = 0;
			for (int i = 0; i < this.textZeilen.size(); i++)
			{
				TextZeile st = (TextZeile)this.textZeilen.elementAt(i);
				Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.PLAIN, fontGroesse);
				JLabel l = new JLabel();
				l.setFont(f);
				FontMetrics fm = l.getFontMetrics(f);
				int widthZeile = fm.stringWidth(st.getText());
				zeilenbreite = Math.max(zeilenbreite, widthZeile);
			}
		}
		
		this.minZeilenAbstand = bestimmeZeilenAbstand(fontGroesse);
		
		int absPanelhoehe = this.getHeight() - Konfiguration.getInstance().getAbstandVorText() - 8;
		if (kommentar.istTextAnzeige())
			absPanelhoehe -= HOEHE_KOMMENTAR;
		int aktPanelhoehe = (this.textZeilen.size()) * (fontGroesse + minZeilenAbstand);
		while (aktPanelhoehe > absPanelhoehe)
		{
			fontGroesse--;
			aktPanelhoehe = (this.textZeilen.size()) * (fontGroesse + minZeilenAbstand);
		}
		return fontGroesse;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if (this.textZeilen != null)
			if (this.anzahlZeilen != this.textZeilen.size())
				return false;
		if ((this.textAnzeige != TEXT_ANZEIGE_OHNE) &&
			(this.textAnzeige != TEXT_ANZEIGE_EINESEITE) &&
			(this.textAnzeige != TEXT_ANZEIGE_ZWEISEITEN))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return "TextSchema " + heightAnteil +"/"+ anzahlZeilen;
	}
	
	/**
	 * @return
	 */
	public TextSchema transientClone()
	{
		TextSchema tschema = new TextSchema(this.anzahlZeilen);
		tschema.setTextAnzeige(this.textAnzeige);
		tschema.setHeightAnteil(this.heightAnteil);
		tschema.setHeightAnteilZweiteSeite(this.heightAnteilZweiteSeite);
		for (int i=0; i<this.textZeilen.size(); i++)
		{
			TextZeile zeile = (TextZeile)this.textZeilen.elementAt(i);
			tschema.addZeile(zeile.transientClone());
		}
		tschema.setKommentar(this.kommentar.transientClone());
		return tschema;
	}

	/**
	 * @return
	 */
	public int getHeightAnteilZweiteSeite()
	{
		return heightAnteilZweiteSeite;
	}

	/**
	 * @param heightAnteilZweiteSeite
	 */
	public void setHeightAnteilZweiteSeite(int heightAnteilZweiteSeite)
	{
		this.heightAnteilZweiteSeite = heightAnteilZweiteSeite;
	}

	/**
	 * @return
	 */
	public Kommentar getKommentar()
	{
		return kommentar;
	}

	/**
	 * @param kommentar
	 */
	public void setKommentar(Kommentar kommentar)
	{
		this.kommentar = kommentar;
	}

}
