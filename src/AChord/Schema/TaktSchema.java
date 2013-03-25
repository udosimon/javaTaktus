/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 11.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * Ein Taktschema enthält die TaktSchemaTeile (oberhalb der SplitPane)
 * 
 */
package AChord.Schema;

import java.util.Vector;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;

public class TaktSchema extends APanel
{
    private final static long serialVersionUID = 1;

    final static int HOEHE_KOMMENTAR = 24;
	
	Vector taktSchemaTeile = new Vector();
	int anzahlTeile = 0;
	int takteProZeile = 0;

	Akkord akkTonArtSong = new Akkord(0);
	
	int heightAnteil = 0;
	
	int schemaNr;
	
	Kommentar kommentar = new Kommentar();
	
	Song song;

	/**
	 * Konstruktor 
	 */
	public TaktSchema(Song song)
	{
		super();
		anzahlTeile = 0;
		takteProZeile = 0;
		akkTonArtSong = new Akkord(0);
		kommentar = new Kommentar();
		this.song = song;
	}

	/**
	 * @param v
	 */
	public void setTaktSchemaTeile(Vector v)
	{
		taktSchemaTeile = v;
	}
	
	/**
	 * @param teil
	 */
	public void addSchemaTeil(TaktSchemaTeil teil)
	{
		taktSchemaTeile.add(teil);
	}
	
	/**
	 * @return
	 */
	public Vector getTaktSchemaTeile()
	{
		return this.taktSchemaTeile;
	}
	
	/**
	 * 
	 */
	public void removeTaktSchemaTeile()
	{
		for (int i = 0; i < taktSchemaTeile.size(); i++) 
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)taktSchemaTeile.elementAt(i);
			this.remove(teil);
		}
		this.taktSchemaTeile.clear();
	}
	
	/**
	 * @param teil
	 */
	public void removeTaktSchemaTeil(TaktSchemaTeil teil)
	{
		this.remove(teil);
	}
	
	/**
	 * @param anzahl
	 */
	public void setAnzahlTeile(int anzahl)
	{
		anzahlTeile = anzahl;
	}
	
	/**
	 * @return
	 */
	public int getAnzahlTeile()
	{
		return this.anzahlTeile;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void setTaktSchemaSize(int x, int y)
	{
		this.setSize(x, y);
		this.setOpaque(false);
	}
	
	/**
	 * @param anzahl
	 */
	public void setTakteProZeile(int anzahl)
	{
		takteProZeile = anzahl;
	}
	
	/**
	 * @return
	 */
	public int getTakteProZeile()
	{
		return this.takteProZeile;
	}
	

	/**
	 * @param anteil
	 */
	public void setHeightAnteil(int anteil)
	{
		this.heightAnteil = anteil;
	}
	/**
	 * @return
	 */
	public int getHeightAnteil()
	{
		return this.heightAnteil;
	}
	
	/**
	 * konstruieren (Größe berechnen und hinzufügen) der TaktSchemaTeile
	 * und des Kommentars
	 */
	public void konstruiereTaktSchema()
	{
		int deltaHeight = Konfiguration.getInstance().getAbstandVorErstemTeil();
		int teilGesamtHeight = this.getHeight() - 
					Konfiguration.getInstance().getAbstandVorErstemTeil() - 
					Konfiguration.getInstance().getAbstandZwischenTeilen() * (this.taktSchemaTeile.size()-1); 
		if (kommentar.istTextAnzeige())
		{
			teilGesamtHeight = teilGesamtHeight -  HOEHE_KOMMENTAR;
		}
		
		for (int i = 0;i < this.taktSchemaTeile.size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.taktSchemaTeile.elementAt(i);
			teil.setTaktAnzahlProZeile(this.takteProZeile);
			teil.kontrolliereAnzahlKastenTeile();
			
			int teilHeight = (int)((double)teilGesamtHeight / 100 * teil.getHeightAnteil());
			
			teil.setTaktSchemaTeilSize(this.getWidth(), teilHeight);
			teil.setLocation(0,deltaHeight);
			teil.setLayout(null);
			deltaHeight += teilHeight + Konfiguration.getInstance().getAbstandZwischenTeilen();
			
			teil.konstruiereTeil();
			
			this.add(teil);
		}
		
		String st = "-A-";
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
			kommentar.setLocation(50, deltaHeight- + Konfiguration.getInstance().getAbstandZwischenTeilen());
			kommentar.konstruiereKommentar();
			this.add(kommentar);
		}
		else
		{
			this.remove(kommentar);
		}
	}
	
	/* (non-Javadoc)
	 * @see APanel#setRahmen()
	 */
	public void setRahmen()
	{
		super.setRahmen();
		for (int i = 0;i < this.taktSchemaTeile.size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.taktSchemaTeile.elementAt(i);
			teil.setRahmen();
		}
	}
	
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#resetRahmen()
	 */
	public void resetRahmen()
	{
		super.resetRahmen();
		for (int i = 0;i < this.taktSchemaTeile.size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.taktSchemaTeile.elementAt(i);
			teil.resetRahmen();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if (this.anzahlTeile < 1) 
			return false;
		if ((this.takteProZeile != 4) && (this.takteProZeile != 6) && (this.takteProZeile != 8)) 
			return false;
		if ((this.getTonArt() < 1) || (this.getTonArt() > 12))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return "Schema--" + anzahlTeile +"/"+takteProZeile+"/"+
						heightAnteil+"##"+akkTonArtSong;
	}

	/**
	 * @return
	 */
	public TaktSchema transientClone(Song song)
	{
		TaktSchema schema;
		if (song == null)
			schema = new TaktSchema(this.song);
		else
			schema = new TaktSchema(song);
		schema.setAnzahlTeile(this.anzahlTeile);
		schema.setTakteProZeile(this.takteProZeile);
		
		schema.setAkkTonArtSong(akkTonArtSong.transientClone());
		schema.setHeightAnteil(this.heightAnteil);
		
		for (int i = 0; i < this.taktSchemaTeile.size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.taktSchemaTeile.elementAt(i);
			if (song == null)
				schema.addSchemaTeil(teil.transientClone(this.song));
			else
				schema.addSchemaTeil(teil.transientClone(song));
		}
		
		schema.setKommentar(this.kommentar.transientClone());
		
		return schema;
	}
	
	/**
	 * @return
	 */
	public int getSchemaNr()
	{
		return this.schemaNr;
	}
	
	/**
	 * setzen eines der vorgegebenen Schematas
	 * @param nr
	 */
	public void initSchema(int nr, Song song)
	{
		this.removeTaktSchemaTeile();
		this.schemaNr = nr;
		switch (nr) {
		case 1:
			setAnzahlTeile(4);
			this.setSchema1();
			song.setNeueHeightAnteile(80);
			break;
		case 2:
			setAnzahlTeile(3);
			this.setSchema2();
			song.setNeueHeightAnteile(60);
			break;
		case 3:
			setAnzahlTeile(2);
			this.setSchema3();
			song.setNeueHeightAnteile(50);
			break;
		case 4:
			setAnzahlTeile(1);
			this.setSchema4();
			song.setNeueHeightAnteile(40);
			break;
		case 5:
			setAnzahlTeile(1);
			this.setSchema5();
			song.setNeueHeightAnteile(50);
			break;
		case 6:
			setAnzahlTeile(2);
			this.setSchema6();
			song.setNeueHeightAnteile(85);
			break;
		case 7:
			setAnzahlTeile(1);
			this.setSchema7();
			song.setNeueHeightAnteile(45);
			break;

		default:
			break;
		}
		
		berechneAnteiligeHoehen();
	}

	/**
	 * 
	 */
	public void berechneAnteiligeHoehen()
	{
		int taktAnteil = 100;
		
		int anzahlAlleZeilen = 0;
		for (int i = 0; i < this.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.getTaktSchemaTeile().elementAt(i);
			anzahlAlleZeilen += teil.bestimmeAnzahlZeilen();
		}
//		System.out.println("-AnzTeile:-"+anzahlAlleZeilen+
//						"---SchemaTeile:-"+this.getTaktSchemaTeile().size());
		int kontrolleAnzeil = 0;
		for (int i = 0; i < this.getTaktSchemaTeile().size(); i++)
		{
			TaktSchemaTeil teil = (TaktSchemaTeil)this.getTaktSchemaTeile().elementAt(i);
			long anteil = Math.round(((double)(taktAnteil * teil.bestimmeAnzahlZeilen()) / anzahlAlleZeilen));
			kontrolleAnzeil += anteil;
			while (kontrolleAnzeil > (taktAnteil+1))
			{
				anteil -= 1;
				kontrolleAnzeil -= 1;
			}
//			System.out.println("---HeightAnteil:-"+anteil);
			teil.setHeightAnteil((int)anteil);
		}
	}
	
	/**
	 * 
	 */
	private void setSchema1()
	{
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_1, 8, "A"));
		
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_2, 8, "A"));
		
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_3, 8, "B"));
		
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_4, 8, "C"));
	}

	/**
	 * 
	 */
	private void setSchema2()
	{
		this.addSchemaTeil(machWiederholungTeil(TaktSchemaTeil.TEIL_ART_TEIL_1, 8, 2, "A"));

		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_3, 8, "B"));
		
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_4, 8, "C"));
	}

	/**
	 * 
	 */
	private void setSchema3()
	{
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_1, 8, ""));
		
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_2, 8, ""));
	}

	/**
	 * 
	 */
	private void setSchema4()
	{
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_1, 12, ""));
	}

	/**
	 * 
	 */
	private void setSchema5()
	{
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_1, 20, ""));
	}

	/**
	 * 
	 */
	private void setSchema6()
	{
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_1, 16, ""));
		
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_2, 16, ""));
	}
	
	/**
	 * 
	 */
	private void setSchema7()
	{
		this.addSchemaTeil(machTeilNormal(TaktSchemaTeil.TEIL_ART_TEIL_1, 16, ""));
	}

	
	/**
	 * @param art
	 * @param anzahl
	 * @param name
	 * @return
	 */
	private TaktSchemaTeil machTeilNormal(int art, int anzahl, String name) 
	{
		TaktSchemaTeil teil;
		teil = new TaktSchemaTeil(art, anzahl, song);
		teil.setTaktAnzahlProZeile(this.takteProZeile);
		teil.setTeilName(name);
		teil.setIstWiederholung(false);
		teil.setAnzahlTakteKasten(0);
		teil.setKopfTakt(0);

		teil.initTeil();
		teil.trenneAbKastenTakte();
		
		return teil;
	}
	/**
	 * @param art
	 * @param anzahl
	 * @param kastenAnzahl
	 * @param name
	 * @return
	 */
	private TaktSchemaTeil machWiederholungTeil(int art, int anzahl, 
								int kastenAnzahl, String name) 
	{
		TaktSchemaTeil teil;
		teil = new TaktSchemaTeil(art, anzahl, song);
		teil.setTaktAnzahlProZeile(this.takteProZeile);
		teil.setTeilName(name);
		teil.setIstWiederholung(true);
		teil.setAnzahlTakteKasten(kastenAnzahl);
		teil.setKopfTakt(0);

		teil.initTeil();
		teil.trenneAbKastenTakte();
		
		return teil;
	}

	/**
	 * @param istJa
	 */
	public void setAnzeigeTonArt(String istJa)
	{
		if (istJa.equals("ja"))
			this.akkTonArtSong.setInBearbeitung(true);
		else
			this.akkTonArtSong.setInBearbeitung(false);
	}
	
	/**
	 * @return
	 */
	public String istAnzeigeTonArt()
	{
		if (this.akkTonArtSong.isInBearbeitung())
			return "ja";
		else
			return "";
	}
	
	/**
	 * @return
	 */
	public int getTonHalbTon()
	{
		return this.akkTonArtSong.getAkkHalbton();
	}
	/**
	 * @param halb
	 */
	public void setTonHalbTon(int halb)
	{
		this.akkTonArtSong.setAkkHalbton(halb);
		akkTonArtSong.vorbereiteAkkord();
	}
	
	/**
	 * @return
	 */
	public String getTonGeschlecht()
	{
		return this.akkTonArtSong.getAkkArt();
	}
	/**
	 * @param st
	 */
	public void setTonGeschlecht(String st)
	{
		this.akkTonArtSong.setAkkArt(st);
		akkTonArtSong.vorbereiteAkkord();
	}
	
	/**
	 * @return
	 */
	public int getTonArt() 
	{
		return akkTonArtSong.getAkkTon();
	}

	/**
	 * @param tonart
	 */
	public void setTonArt(int tonart) 
	{
		this.akkTonArtSong.setAkkTon(tonart);
		akkTonArtSong.vorbereiteAkkord();
	}

	/**
	 * @return
	 */
	public Akkord getAkkTonArtSong() 
	{
		return akkTonArtSong;
	}

	/**
	 * @param akkTonArtSong
	 */
	public void setAkkTonArtSong(Akkord akkTonArtSong) 
	{
		this.akkTonArtSong = akkTonArtSong;
	}

	/**
	 * @return the kommentar
	 */
	public Kommentar getKommentar()
	{
		return kommentar;
	}

	/**
	 * @param kommentar the kommentar to set
	 */
	public void setKommentar(Kommentar kommentar)
	{
		this.kommentar = kommentar;
	}
}
