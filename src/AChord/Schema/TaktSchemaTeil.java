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
 * Ein TaktSchmmaTeil enthält die Takte.
 * Ein TaktSchmmaTeil weiß, wieviel Takte dazugehören, den Namen eines Teils,
 * ob Wiederholungszeichen und Kästen vorhanden sind
 * 
 */
package AChord.Schema;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;


public class TaktSchemaTeil extends APanel
{
    private final static long serialVersionUID = 1;

    static final int TEIL_ART_UNDEFINED = -1;
	static final int TEIL_ART_INTRO = 0;
	static final int TEIL_ART_TEIL_1 = 1;
	static final int TEIL_ART_TEIL_2 = 2;
	static final int TEIL_ART_TEIL_3 = 3;
	static final int TEIL_ART_TEIL_4 = 4;
	static final int TEIL_ART_TEIL_5 = 5;
	static final int TEIL_ART_TEIL_6 = 6;
	static final int TEIL_ART_OUTRO = 9;
	static final int TEIL_ART_MITTELTRO = 10;
	static final int TEIL_ART_KOPFANHANG = 11;
	
	static final int[] AKK_TEILART_MENGE = new int[] {TEIL_ART_INTRO,
													TEIL_ART_TEIL_1,
													TEIL_ART_TEIL_2,
													TEIL_ART_TEIL_3,
													TEIL_ART_TEIL_4,
													TEIL_ART_TEIL_5,
													TEIL_ART_TEIL_6,
													TEIL_ART_OUTRO,
													TEIL_ART_MITTELTRO,
													TEIL_ART_KOPFANHANG};
	
	static final int NORMALE_TAKTE_PRO_ZEILE = 4;
	
	static final int TEIL_YPOS = 1;
	static final int TEIL_ABSTAND_TAKT_OBEN_UNTEN = 4;
	static final int TAKT_ABSTAND = 11;
	
	static public final int TEIL_MIN_ANZAHLTAKTE = 1;
	static public final int TEIL_MAX_ANZAHLTAKTE = 32;
	static public final int TEIL_MIN_ANZAHLKASTENTAKTE = 0;
	static public final int TEIL_MAX_ANZAHLKASTENTAKTE = 4;

	
	int teilArt;
	int taktAnzahl;
	int taktAnzahlVorher = 0;
	int taktAnzahlProZeile;
	
	String teilName = "";
	
	Vector takte = new Vector();
	Vector takteKasten2 = new Vector();
	
	boolean istWiederholung = false;
	int anzahlTakteKasten = 0;
	int anzahlTakteKastenVorher = 0;
	int kopfTakt = 0;

	int heightAnteil = 0;
	
	JLabel labelName = new JLabel();
	
	Kasten kasten1 = null;
	Kasten kasten2 = null;
	Kopf kopf = null;
	Kopf kopfName = null;
	WiederholungsZeichen wiederhZLinks = null;
	WiederholungsZeichen wiederhZRechts = null;
	Vector vBreaks = new Vector();
	
	Song song;
	
	/**
	 * Konstruktor
	 */
	public TaktSchemaTeil(int art, int anzahl, Song song)
	{
		super();
		teilArt = art;
		taktAnzahl = anzahl;
		taktAnzahlVorher = anzahl;
		this.song = song;
		taktAnzahlProZeile = NORMALE_TAKTE_PRO_ZEILE;
		teilName = "";
	    this.addMouseMotionListener(new MyMouseMotionListener());
	    this.addMouseListener(new MyMouseListener());
	}
	
	/**
	 * @return
	 */
	public WiederholungsZeichen getWiederhZLinksGra()
	{
		return wiederhZLinks;
	}
	/**
	 * @return
	 */
	public WiederholungsZeichen getWiederhZRechtsGra()
	{
		return wiederhZRechts;
	}
	/**
	 * @return
	 */
	public Kopf getKopfGra()
	{
		return kopf;
	}
	
	/**
	 * @return
	 */
	public Kasten getKasten1Gra()
	{
		return kasten1;
	}

	/**
	 * @return
	 */
	public Kasten getKasten2Gra()
	{
		return kasten2;
	}
	
	/**
	 * @return
	 */
	public int getTeilArt()
	{
		return teilArt;
	}
	/**
	 * @param art
	 */
	public void setTeilArt(int art)
	{
		teilArt = art;
	}
	
	/**
	 * @param anzahl
	 */
	public void setTaktAnzahl(int anzahl)
	{
		this.setAenderungIstErfolgt();

		taktAnzahlVorher = taktAnzahl;
		taktAnzahl = anzahl;
	}
	
	/**
	 * @return
	 */
	public int getTaktAnzahl()
	{
		return this.taktAnzahl;
	}
	
	/**
	 * @return
	 */
	public int getTaktAnzahlVorher() 
	{
		return taktAnzahlVorher;
	}

	/**
	 * @param taktAnzahlVorher
	 */
	public void setTaktAnzahlVorher(int taktAnzahlVorher) 
	{
		this.taktAnzahlVorher = taktAnzahlVorher;
	}

	/**
	 * @param anzahl
	 */
	public void setTaktAnzahlProZeile(int anzahl)
	{
		this.setAenderungIstErfolgt();

		taktAnzahlProZeile = anzahl;
	}
	
	/**
	 * @param name
	 */
	public void setTeilName(String name)
	{
		this.setAenderungIstErfolgt();

		teilName = name;
	}
	
	/**
	 * @return
	 */
	public String getTeilName()
	{
		return this.teilName;
	}
	
	/**
	 * 
	 */
	public void setIstKopfTeil(boolean ist)
	{
		if (ist)
			this.teilArt = TEIL_ART_KOPFANHANG;
		else
			this.teilArt = TEIL_ART_TEIL_1;
	}
	
	/**
	 * @return
	 */
	public boolean istKopfTeil()
	{
		if (this.teilArt == TEIL_ART_KOPFANHANG)
			return true;
		else
			return false;
	}
	
	/**
	 * @param ist
	 */
	public void setIstWiederholung(boolean ist)
	{
		this.setAenderungIstErfolgt();

		istWiederholung = ist;
	}
	
	/**
	 * @return
	 */
	public boolean istWiederholung()
	{
		return this.istWiederholung;
	}
	
	/**
	 * @param nr
	 */
	public void setKopfTakt(int nr)
	{
		this.setAenderungIstErfolgt();

		this.kopfTakt = nr;
		if ((nr == 0) && (kopf != null))
		{
			this.remove(kopf);
			kopf = null;
		}
	}
	
	/**
	 * @return
	 */
	public int getKopfTakt()
	{
		return this.kopfTakt;
	}
	
	/**
	 * @param anzahl
	 */
	public void setAnzahlTakteKasten(int anzahl)
	{
		this.setAenderungIstErfolgt();

		anzahlTakteKastenVorher = anzahlTakteKasten;
		anzahlTakteKasten = anzahl;
	}

	/**
	 * @return
	 */
	public int getAnzahlTakteKasten()
	{
		return this.anzahlTakteKasten;
	}
	
	/**
	 * @return
	 */
	public int getAnzahlTakteKastenVorher() 
	{
		return anzahlTakteKastenVorher;
	}

	/**
	 * @param anzahlTakteKastenVorher
	 */
	public void setAnzahlTakteKastenVorher(int anzahlTakteKastenVorher) 
	{
		this.anzahlTakteKastenVorher = anzahlTakteKastenVorher;
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
		return heightAnteil;
	}
	
	/**
	 * @param takte
	 */
	public void setTakte(Vector takte)
	{
		this.takte = takte;
	}
	
	/**
	 * @param takt
	 */
	public void addTakt(Takt takt)
	{
		this.takte.add(takt);
	}
	
	/**
	 * @param takt
	 * @param index
	 */
	public void insertTakt(Takt takt, int index)
	{
		this.takte.add(index, takt);
	}
	
	/**
	 * @param index
	 */
	public Takt removeTakt(int index)
	{
		return (Takt)this.takte.remove(index);
	}
	
	/**
	 * @param takt
	 */
	public void addKasten2Takt(Takt takt)
	{
		this.takteKasten2.add(takt);
	}
	
	/**
	 * fügt einen Kasten-Takt am Anfang der Kasten-Takt-Liste ein
	 * @param takt
	 */
	public void insertKasten2Takt(Takt takt)
	{
		this.takteKasten2.insertElementAt(takt, 0);
	}
	
	/**
	 * @param index
	 * @return
	 */
	public Takt removeKasten2Takt(int index)
	{
		return (Takt)this.takteKasten2.remove(index);
	}
	
	/**
	 * @return
	 */
	public Vector getAlleTakte()
	{
		Vector alle = new Vector();
		alle.addAll(takte);
		alle.addAll(takteKasten2);
		return alle;
	}
	
	/**
	 * 
	 */
	public void removeAlleTakte()
	{
		for (int i = 0; i < takte.size(); i++)
		{
			Takt takt = (Takt)takte.elementAt(i);
			this.remove(takt);
		}
		takte.clear();
		for (int i = 0; i < takteKasten2.size(); i++)
		{
			Takt takt = (Takt)takteKasten2.elementAt(i);
			this.remove(takt);
		}
		takteKasten2.clear();
	}
	

	/**
	 * 
	 */
	public void leereAlleTakte()
	{
		for (int i = 0; i < takte.size(); i++)
		{
			Takt takt = (Takt)takte.elementAt(i);
			takt.setTaktAufLeer();
		}
		for (int i = 0; i < takteKasten2.size(); i++)
		{
			Takt takt = (Takt)takteKasten2.elementAt(i);
			takt.setTaktAufLeer();
		}
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void setTaktSchemaTeilSize(int x, int y)
	{
		this.setSize(x, y);
		this.setOpaque(false);

//		this.setLayout(new BorderLayout());
	}

	/**
	 *  nach Einlesen aus XML-Datei trennen der normalen und der 
	 *  Kasten-Takte
	 */
	public void trenneAbKastenTakte()
	{
		// trennen der Kasten2Takte von den anderen Takten (wenn vorhanden)
		if ((this.anzahlTakteKasten > 0) && (takteKasten2.size() == 0))
		{
			int alteAnzahlTakte = this.takte.size();
			for (int i = alteAnzahlTakte-1;
				 	 i >= (alteAnzahlTakte-this.anzahlTakteKasten);
				 	 i--)
			{
				this.takteKasten2.add(this.takte.remove(i));
			}
		}
	}
	/**
	 * konstruieren (Größe berechnen und hinzufügen) der Takte, Wiederholungszeichen,
	 * Kästen, Teilnamen
	 */
	public void konstruiereTeil()
	{
		// Anzahl der Zeilen innerhalb eines Teiles
		int anzahlZeilen = bestimmeAnzahlZeilen();

		// Höhe einer Zeile
		int zeilenHoehe = this.getHeight() / anzahlZeilen;

		// Das was an der linken Kante des Teiles frei bleiben soll
		// für den Namen)
		int offsetSpalte = setTeilname(zeilenHoehe);
		offsetSpalte = offsetSpalte - offsetSpalte / 2;

		// Höhe des Taktes
		int taktHoehe = zeilenHoehe - (2 * TEIL_ABSTAND_TAKT_OBEN_UNTEN);

		// das was links und rechts in der Zeile frei bleiben
		// soll (für Wiederholungszeichen
		int offsetSpalteLineRechts = WiederholungsZeichen.getWiederholungszeichenBreite(taktHoehe);
		
		// Breite eines Taktes
		int taktBreite = (this.getWidth() - offsetSpalte - (2*offsetSpalteLineRechts)) / 
						 (this.taktAnzahlProZeile) - TAKT_ABSTAND;
		// Berechnen, ob die Breite oder die Höhe kleiner ist;
		// der kleinste Wert ist das Maß für die Takt-Kantenlängen
		int taktKante = Math.min(taktHoehe, taktBreite);
		
		// wenn die Kante dann kleiner ist als die Breite, dann
		// wollen wir alles etwas nach rechts in die Mitte schieben
		if (taktKante < taktBreite)
		{
			offsetSpalte = offsetSpalte + (taktBreite - taktKante ) / 2 ;
		}

		offsetSpalteLineRechts = WiederholungsZeichen.getWiederholungszeichenBreite(taktKante);
		
		// linkes Wiederholungszeichen, wenn vorhanden
		insertLinkesWiederholungszeichen(offsetSpalte, taktKante);
		
		// Berechnung, wo die Takte hinkommen und initialisieren der Takte
		int aktuelleSpalte = 0;
		aktuelleSpalte = handleTakte(zeilenHoehe, offsetSpalte,
				offsetSpalteLineRechts, taktBreite, 
				taktKante, aktuelleSpalte, anzahlZeilen);

		// rechtes Wiederholungszeichen, wenn vorhanden
		insertRechtesWiederholungszeichen(anzahlZeilen, zeilenHoehe,
				offsetSpalte, offsetSpalteLineRechts, taktBreite, taktKante,
				aktuelleSpalte);
		
	}

	/**
	 * @return
	 */
	public int bestimmeAnzahlZeilen()
	{
		int anzahlZeilen = bestimmeAnzahlZeilenOhneKasten();

		// wenn Kastentakte vorhanden, dann Extra-Behandlung
		if (this.anzahlTakteKasten > 0)
		{
			anzahlZeilen += 1;
		}
		return anzahlZeilen;
	}
	
	/**
	 * @return
	 */
	public int bestimmeAnzahlZeilenOhneKasten()
	{
		int anzahlZeilen = this.taktAnzahl / this.taktAnzahlProZeile;
		if ((this.taktAnzahl % this.taktAnzahlProZeile) > 0)
			anzahlZeilen += 1;
		return anzahlZeilen;
	}	
	
	/**
	 * @return
	 */
	public int bestimmeAnzahlTakteLetzteZeile()
	{
		int anzTakteLZeile = taktAnzahl - ((bestimmeAnzahlZeilenOhneKasten() - 1) * taktAnzahlProZeile);
//		System.out.println("Teil: " + getTeilName() + "**********************************************");
//		System.out.println("Anz:"+getTaktAnzahl()+" AnzProZeile:"+taktAnzahlProZeile+
//				" AnzZeilen:"+bestimmeAnzahlZeilenOhneKasten()+
//				" AnzTakteLZeile:"+anzTakteLZeile);
		return anzTakteLZeile;
	}

	/**
	 * Kontrolle, ob die Anzahl der Kastenteile verringert werden muss
	 */
	public void kontrolliereAnzahlKastenTeile()
	{
		if (this.anzahlTakteKasten > bestimmeAnzahlTakteLetzteZeile())
		{
			this.anzahlTakteKasten = bestimmeAnzahlTakteLetzteZeile();
			resetInitTeilKasten();
		}
	}

	/**
	 * @param zeilenHoehe
	 * @param offsetSpalte
	 * @param offsetSpalteLineRechts
	 * @param taktBreite
	 * @param taktKante
	 * @param aktuelleSpalte
	 * @param anzahlZeilen TODO
	 * @return
	 */
	private int handleTakte(int zeilenHoehe, int offsetSpalte,
			int offsetSpalteLineRechts, int taktBreite, int taktKante,
			int aktuelleSpalte, int anzahlZeilen)
	{
		int aktuelleZeile = 0;
		int alteZeile = 0;

		for (int i = 0;i < this.takte.size(); i++)
		{
			Takt takt = (Takt)this.takte.elementAt(i);

			// gucken, ob die nächste Zeile drann ist
			aktuelleZeile = i / this.taktAnzahlProZeile;

			// es geht weiter in der nächsten Zeile
			if (aktuelleZeile != alteZeile)
			{
				aktuelleSpalte = 0;
			}

			int taktXPos = offsetSpalte + offsetSpalteLineRechts + 
							(aktuelleSpalte * (taktBreite+ TAKT_ABSTAND)); 
			int taktYPos = (zeilenHoehe * aktuelleZeile) + TEIL_ABSTAND_TAKT_OBEN_UNTEN;

			aktuelleSpalte = aktuelleSpalte + 1;
			alteZeile = aktuelleZeile;
			
			takt.setTaktSize(taktKante);
			takt.setLocation(taktXPos, taktYPos);
			takt.konstuiereTakt();

			this.add(takt);
			
			if (this.kopfTakt == (i+1))
			{
				Kopf ko = null;
				if (kopf != null)
					ko = kopf;
				else
				{
					ko = new Kopf();
					kopf = ko;
				}
				ko.setKopfSize(taktKante / 3, taktKante / 3);
				ko.setLocation(taktXPos - (int)(taktKante * 0.25), taktYPos);
				this.add(ko);
			}
			
			if (takt.getTaktBreak1() != null)
				this.remove(takt.getTaktBreak1());
			if (takt.getTaktBreak2() != null)
				this.remove(takt.getTaktBreak2());
			if (takt.getTaktBreak3() != null)
				this.remove(takt.getTaktBreak3());
			if (takt.getTaktBreak4() != null)
				this.remove(takt.getTaktBreak4());
			
			for (int ii=1; ii<=4; ii++)
			{
				if (takt.istBreakAuf(ii))
				{
					TaktBreak tBreak = new TaktBreak(ii);
					tBreak.setBreakText(takt.getBreakText());
					tBreak.setBreakHoehe(taktKante / 6);
					tBreak.setBreakLocation(taktXPos, 
								taktYPos-TEIL_ABSTAND_TAKT_OBEN_UNTEN, 
								taktKante);
					this.add(tBreak);
					takt.setTaktBreak(tBreak, ii);
				}
				else
				{
					takt.setTaktBreak(null, ii);
				}
			}
		}
		if (this.anzahlTakteKasten > 0)
		{
			insertKasten(zeilenHoehe, offsetSpalte, offsetSpalteLineRechts,
					taktBreite, taktKante, aktuelleSpalte, aktuelleZeile,
					"1");
			
			// rechtes Wiederholungszeichen, wenn vorhanden
			insertRechtesWiederholungszeichen(anzahlZeilen-1, zeilenHoehe,
					offsetSpalte, offsetSpalteLineRechts, taktBreite, taktKante,
					aktuelleSpalte);
			
			aktuelleZeile += 1;
			aktuelleSpalte = aktuelleSpalte - this.anzahlTakteKasten;
			for (int i = 0;i < this.takteKasten2.size(); i++)
			{
				Takt takt = (Takt)this.takteKasten2.elementAt(i);

				int taktXPos = offsetSpalte + offsetSpalteLineRechts + 
								(aktuelleSpalte * (taktBreite+ TAKT_ABSTAND)); 
				int taktYPos = (zeilenHoehe * aktuelleZeile) + TEIL_ABSTAND_TAKT_OBEN_UNTEN;

				aktuelleSpalte = aktuelleSpalte + 1;
				
				takt.setTaktSize(taktKante);
				takt.setLocation(taktXPos, taktYPos);
				takt.konstuiereTakt();

				this.add(takt);

				if (this.kopfTakt == (i+this.taktAnzahl+1))
				{
					Kopf ko = null;
					if (kopf != null)
						ko = kopf;
					else
					{
						ko = new Kopf();
						kopf = ko;
					}
					ko.setKopfSize(taktKante / 3, taktKante / 3);
					ko.setLocation(taktXPos - (taktKante / 6), taktYPos);
					this.add(ko);
				}
			}
			insertKasten(zeilenHoehe, offsetSpalte, offsetSpalteLineRechts,
					taktBreite, taktKante, aktuelleSpalte, aktuelleZeile,
					"2");
	
		}
		return aktuelleSpalte;
	}

	/**
	 * malen eines "Kastens" (bei Wiederholungen) über den entsprechenden Takten
	 * @param zeilenHoehe
	 * @param offsetSpalte
	 * @param offsetSpalteLineRechts
	 * @param taktBreite
	 * @param taktKante
	 * @param aktuelleSpalte
	 * @param aktuelleZeile
	 * @param kastenBezeichnung
	 */
	private void insertKasten(int zeilenHoehe, int offsetSpalte,
			int offsetSpalteLineRechts, int taktBreite, int taktKante,
			int aktuelleSpalte, int aktuelleZeile,
			String kastenBezeichnung)
	{
		int xPosKasten = offsetSpalte + offsetSpalteLineRechts + 
					((aktuelleSpalte-this.anzahlTakteKasten) * (taktBreite+ TAKT_ABSTAND)) -
					TAKT_ABSTAND;
		int yPosKasten = aktuelleZeile * zeilenHoehe;
		int heightKasten = taktKante / 3;
		int widthKasten = this.anzahlTakteKasten * (taktBreite+ TAKT_ABSTAND) - 
						  taktBreite + taktKante;
		Kasten kasten;
		if (kastenBezeichnung.equals("1"))
		{
			if (kasten1 != null)
			{
				kasten = kasten1;
			}
			else
			{
				kasten = new Kasten(kastenBezeichnung);
				kasten1 = kasten;
			}
		}
		else
		{
			if (kasten2 != null)
			{
				kasten = kasten2;
			}
			else
			{
				kasten = new Kasten(kastenBezeichnung);
				kasten2 = kasten;
			}
		}
			
		kasten.setKastenSize(widthKasten, heightKasten);
		kasten.setLocation(xPosKasten, yPosKasten);
		this.add(kasten);
	}

	/**
	 * @param anzahlZeilen
	 * @param zeilenHoehe
	 * @param offsetSpalte
	 * @param offsetSpalteLineRechts
	 * @param taktBreite
	 * @param taktKante
	 * @param aktuelleSpalte
	 */
	private void insertRechtesWiederholungszeichen(int anzahlZeilen,
			int zeilenHoehe, int offsetSpalte, int offsetSpalteLineRechts,
			int taktBreite, int taktKante, int aktuelleSpalte)
	{
		if (this.istWiederholung)
		{
			// das rechte Wiederholungszeichen kommt hinter den letzten Takt
			if (wiederhZRechts == null)
			{
				wiederhZRechts = new WiederholungsZeichen(false);
			}
			wiederhZRechts.setWiederholungsZeichenSize(taktKante);
			int xPosWZ = offsetSpalte + offsetSpalteLineRechts + 
						((aktuelleSpalte-1) * (taktBreite+ TAKT_ABSTAND))
						+taktKante;
			wiederhZRechts.setLocation(xPosWZ, 
					((anzahlZeilen - 1) * zeilenHoehe) + TEIL_ABSTAND_TAKT_OBEN_UNTEN);
			this.add(wiederhZRechts);
		}
	}

	/**
	 * @param offsetSpalte
	 * @param taktKante
	 */
	private void insertLinkesWiederholungszeichen(int offsetSpalte,
			int taktKante)
	{
		if (this.istWiederholung)
		{
			if (wiederhZLinks == null)
			{
				wiederhZLinks = new WiederholungsZeichen(true);
			}
			wiederhZLinks.setWiederholungsZeichenSize(taktKante);
			wiederhZLinks.setLocation(offsetSpalte, 
								TEIL_ABSTAND_TAKT_OBEN_UNTEN);
			this.add(wiederhZLinks);
		}
	}
	
	/**
	 * @param zeilenhoehe TODO
	 * @return
	 * 
	 * wenn ein Teilname vorhanden ist (z.B. "A"), dann weiter rechts anfangen
	 * mit den Takten 
	 */
	public int setTeilname(int zeilenhoehe)
	{
		int y = TEIL_YPOS;
		
		if (this.istKopfTeil())
		{
			int hoehe = zeilenhoehe;
			if (hoehe > 100)
				hoehe = 100;
			if (kopfName == null)
				kopfName = new Kopf();
			kopfName.setKopfSize(hoehe / 3, hoehe / 3);
			y = TEIL_YPOS + Math.min(30, zeilenhoehe/3);
			kopfName.setLocation(20,0);
//			this.labelName.setSize(Math.min(y, 50) + hoehe/3, hoehe / 3);
			this.labelName.setSize(hoehe, hoehe);
			this.labelName.setText("");
			labelName.setBorder(null);
			this.labelName.add(kopfName);
			this.add(labelName);
		}
		else if (this.teilName.length() <= 1)
		{
			int hoehe = zeilenhoehe / 3;
			if (hoehe > 30)
				hoehe = 30;
			y = TEIL_YPOS + hoehe;
			Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.PLAIN, hoehe);
			FontMetrics fm = labelName.getFontMetrics(f);
			int x = fm.stringWidth(teilName);

			if (kopfName != null)
			{
				labelName.remove(kopfName);
				kopfName = null;
			}
			labelName.setFont(f);
			labelName.setText(teilName);
			labelName.setLocation(0, 0);
			labelName.setSize(x+4, hoehe);
			if (this.teilName.length() > 0)
				labelName.setBorder(new LineBorder(Color.BLACK));
			else
				labelName.setBorder(null);
			this.add(labelName);
		}
		else if (this.teilName.length() > 1)
		{
			int hoehe = zeilenhoehe / 4;
			if (hoehe > 18)
				hoehe = 18;
			y = TEIL_YPOS + hoehe;
			Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.PLAIN, hoehe);
			FontMetrics fm = labelName.getFontMetrics(f);
			int x = fm.stringWidth(teilName);

			if (kopfName != null)
			{
				labelName.remove(kopfName);
				kopfName = null;
			}
			labelName.setFont(f);
			labelName.setText(teilName);
			labelName.setLocation(0, 0);
			labelName.setSize(x+4, hoehe);
			labelName.setBorder(new LineBorder(Color.BLACK));
			this.add(labelName);
		}
		
		return y;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return "TeilSchema--"+this.teilArt+"/"+this.teilName+"/"+this.taktAnzahl+
				"/"+this.anzahlTakteKasten+"/"+this.istWiederholung;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		boolean teilArtOk = false;
		for (int i =0 ; i < AKK_TEILART_MENGE.length ; i++)
		{
			if (this.teilArt == AKK_TEILART_MENGE[i])
				teilArtOk = true;
		}
		if (! teilArtOk)
			return false;
		
		if (this.taktAnzahl < 1)
			return false;

		return true;
	}

	/**
	 * @param song TODO
	 * @return
	 */
	public TaktSchemaTeil transientClone(Song song)
	{
		TaktSchemaTeil teil;
		if (song == null)
			teil = new TaktSchemaTeil(this.teilArt, this.taktAnzahl, this.song);
		else
			teil = new TaktSchemaTeil(this.teilArt, this.taktAnzahl, song);
		
		teil.setTaktAnzahlProZeile(this.taktAnzahlProZeile);
		teil.setTeilName(this.teilName);
		teil.setIstWiederholung(this.istWiederholung);
		teil.setAnzahlTakteKasten(this.anzahlTakteKasten);
		teil.setHeightAnteil(this.heightAnteil);
		teil.setKopfTakt(this.kopfTakt);
		
		for (int i=0; i<this.takte.size(); i++)
		{
			Takt takt = (Takt)this.takte.elementAt(i);
			if (song == null)
				teil.addTakt(takt.transientClone(this.song));
			else
				teil.addTakt(takt.transientClone(song));
		}
		
		for (int j=0; j<this.takteKasten2.size(); j++)
		{
			Takt taktK = (Takt)this.takteKasten2.elementAt(j);
			if (song == null)
				teil.addKasten2Takt(taktK.transientClone(this.song));
			else
				teil.addKasten2Takt(taktK.transientClone(song));
		}
		
		return teil;
	}

	/**
	 * 
	 */
	public void initTeil()
	{
		for (int i=0;i<this.taktAnzahl+this.anzahlTakteKasten;i++)
		{
			Takt takt = new Takt(song, this);
			takt.setTeilung(Takt.TAKT_TEILUNG_LEERER_TAKT);
			takt.setTaktAufLeer();
			this.addTakt(takt);
			this.add(takt);
		}
	}
	
	/**
	 * 
	 */
	public void resetInitTeilWZeichen()
	{
		if (! istWiederholung)
		{
			if (this.wiederhZLinks != null)
			{
				this.remove(wiederhZLinks);
				wiederhZLinks = null;
			}
			if (this.wiederhZRechts != null)
			{
				this.remove(wiederhZRechts);
				wiederhZRechts = null;
			}
			resetInitTeilKasten();
		}

	}
	
	/**
	 * unteruscht, ob Kastentakte hinzugefügt oder gelöscht werden sollen und
	 * ob die grafischen "Kästen" gelöscht werden müssen, wenn kein Kastentakt mehr 
	 * vorhanden ist.
	 */
	public void resetInitTeilKasten()
	{
//		System.out.println("**Kasten***++++++"+anzahlTakteKasten+"/"+anzahlTakteKastenVorher);
		
		if (anzahlTakteKasten == 0)
		{
			trenneAbKastenTakte();
			for (int i = 0; i < takteKasten2.size(); i++)
			{
				Takt takt = (Takt)takteKasten2.elementAt(i);
				this.remove(takt);
			}
			takteKasten2.clear();
			if (this.kasten1 != null)
			{
				this.remove(kasten1);
				kasten1 = null;
			}
			if (this.kasten2 != null)
			{
				this.remove(kasten2);
				kasten2 = null;
			}
		}
		else if (anzahlTakteKasten > anzahlTakteKastenVorher)
		{
			for (int i=anzahlTakteKastenVorher;i<this.anzahlTakteKasten;i++)
			{
				// einfügen von Takten in den Kasten; es wird der zugehörige
				// Takt aus Kasten 1 kopiert
				int delta = 1;
				if (takteKasten2 != null)
					delta = takteKasten2.size() + 1;
				Takt takt = ((Takt)this.takte.get(takte.size()-delta)).transientClone(song);
				this.insertKasten2Takt(takt);
			}
		}
		else if (anzahlTakteKasten < anzahlTakteKastenVorher)
		{
			for (int i=anzahlTakteKasten;i<this.anzahlTakteKastenVorher;i++)
			{
				// löschen von Takten aus dem Kasten
				Takt takt = this.removeKasten2Takt(0);
				this.remove(takt);
			}
		}
		else
		{
			// nix tun
		}
	}

	/**
	 * 
	 */
	public void resetInitTeilTakte()
	{
//		System.out.println("*****++++++"+taktAnzahl+"/"+taktAnzahlVorher);
		if (taktAnzahl > taktAnzahlVorher)
		{
			for (int i=taktAnzahlVorher;i<this.taktAnzahl;i++)
			{
				Takt takt = new Takt(song, this);
				takt.setTeilung(Takt.TAKT_TEILUNG_LEERER_TAKT);
				takt.setTaktAufLeer();
				this.insertTakt(takt, i);
			}
		}
		else if (taktAnzahl < taktAnzahlVorher)
		{
			for (int i=taktAnzahl;i<this.taktAnzahlVorher;i++)
			{
				Takt takt = this.removeTakt(taktAnzahl);
				this.remove(takt);
			}
		}
		else
		{
			// nix tun
		}
	}
	
	/**
	 * 
	 */
	public void resetInitTeil()
	{
		if (this.wiederhZLinks != null)
		{
			this.remove(wiederhZLinks);
			wiederhZLinks = null;
		}
		if (this.kasten1 != null)
		{
			this.remove(kasten1);
			kasten1 = null;
		}
		if (this.wiederhZRechts != null)
		{
			this.remove(wiederhZRechts);
			wiederhZRechts = null;
		}
		if (this.kasten2 != null)
		{
			this.remove(kasten2);
			kasten2 = null;
		}
		this.removeAlleTakte();
		this.initTeil();
	}

	
	public void aPanelMouseDragged(MouseEvent e)
	{
		song.rectCheckeAufZuMarkierndeComponenten(e, this);
	}
	public void aPanelMousePressed(MouseEvent e)
	{
		song.rectHandleMousePressed(e, this);
	}
	public void aPanelMouseReleased(MouseEvent e)
	{
		song.rectHandleMouseReleased(e, this);
	}
}
