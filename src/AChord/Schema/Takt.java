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
 * Beschreibung:
 * 		Darstellung eines Taktes; ein Takt kann bis zu 4 Akkorden enthalten
 * 		(Thema Teilung).
 * 		Zu einem Takt gehören auch die Breaks auf die vier Zählzeiten (wenn
 * 		vorhanden) und ein möglicher Hinweistext zu einem der Breaks.
 * 
 * Beispiel:
 * 
 * 		B
 * 		 /
 * 		  C /
 * 		   /
 * 		  / A
 * 			 /
 * 			  D
 * 
 *  B == Akkord1
 *  C == Akkord2
 *  A == Akkord3
 *  D == Akkord4
 * 
 *
 */
package AChord.Schema;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.SwingUtilities;

import AChord.AClasses.APanel;


public class Takt extends APanel
{
    private final static long serialVersionUID = 1;

    // welche Arten von Teilung
	public final static int TAKT_TEILUNG_LEERER_TAKT = 0;
	public final static int TAKT_TEILUNG_OHNE = 1;
	public final static int TAKT_TEILUNG_ZWEI = 2;
	public final static int TAKT_TEILUNG_DREIOBENLINKS = 3;
	public final static int TAKT_TEILUNG_DREIUNTENRECHTS = 4;
	public final static int TAKT_TEILUNG_VIER = 5;
	
	Akkord akkord1;
	Akkord akkord2;
	Akkord akkord3;
	Akkord akkord4;
	
	boolean seperatorA = false;
	boolean seperatorB = false;
	boolean seperatorC = false;

	int teilung = 0;
	
	int breakQuot = 0;
	String breakText = "";
	TaktBreak taktBreak1 = null;
	TaktBreak taktBreak2 = null;
	TaktBreak taktBreak3 = null;
	TaktBreak taktBreak4 = null;
	
	boolean istMarkiertAlsGewaehlt = false;
	boolean istTaktSelektiert = false;
	boolean istTemporaerMarkiert = false;

	Song song;
	TaktSchemaTeil teil;
	
	int tempTaktNr = -1;
	
	/**
	 * Konstruktor
	 */
	public Takt(int teilung, Song song, TaktSchemaTeil teil)
	{
		super();
		this.teilung = teilung;
		this.song = song;
		this.teil = teil;
		myInit();
	}
	
	/**
	 * Konstruktor
	 */
	public Takt(Song song, TaktSchemaTeil teil)
	{
		super();
		this.song = song;
		this.teil = teil;
		myInit();
	}

	
	/**
	 * Teil zu dem dieser Takt gehört
	 * @return
	 */
	public TaktSchemaTeil getTeil()
	{
		return teil;
	}
	
	/**
	 * setzen bzw. rücksetzen eines Breaks
	 * @param breakQuot
	 */
	public void setBreakQuotIntern(int zaehlzeit, boolean istGewaehlt)
	{
		this.setAenderungIstErfolgt();

		int bitsetzer;
		switch (zaehlzeit)
		{
			case 1:
				if (istGewaehlt)
					bitsetzer= 0x1;
				else
					bitsetzer= ~0x1;
				break;
			case 2:
				if (istGewaehlt)
					bitsetzer= 0x2;
				else
					bitsetzer= ~0x2;
				break;
			case 3:
				if (istGewaehlt)
					bitsetzer= 0x4;
				else
					bitsetzer= ~0x4;
				break;
			case 4:
				if (istGewaehlt)
					bitsetzer= 0x8;
				else
					bitsetzer= ~0x8;
				break;
	
			default:
				if (istGewaehlt)
					bitsetzer= 0x1;
				else
					bitsetzer= ~0x1;
				break;
		}
		if (istGewaehlt)
			this.breakQuot = this.breakQuot | bitsetzer;
		else
			this.breakQuot = this.breakQuot & bitsetzer;
	}

	/**
	 * @param breakQuot
	 */
	public void setBreakQuot(int breakQuot)
	{
		this.setAenderungIstErfolgt();
		this.breakQuot = breakQuot;
	}
	
	/**
	 * @return
	 */
	public int getBreakQuot()
	{
		return this.breakQuot;
	}
	
	/**
	 * @return
	 */
	public boolean istGenauEinBreakGesetzt()
	{
		if ((this.breakQuot == 1) || (this.breakQuot == 2) ||
			(this.breakQuot == 4) || (this.breakQuot == 8))
			return true;
		else
			return false;
	}
	
	/**
	 * @return
	 */
	public boolean istBreakAufEins()
	{
		if ((this.breakQuot & 0x1) > 0)
		{
			return true;
		}
		return false;
	}
	/**
	 * @return
	 */
	public boolean istBreakAufZwei()
	{
		if ((this.breakQuot & 0x2) > 0)
		{
			return true;
		}
		return false;
	}
	/**
	 * @return
	 */
	public boolean istBreakAufDrei()
	{
		if ((this.breakQuot & 0x4) > 0)
		{
			return true;
		}
		return false;
	}
	/**
	 * @return
	 */
	public boolean istBreakAufVier()
	{
		if ((this.breakQuot & 0x8) > 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @param zaehlzeit
	 * @return
	 */
	public boolean istBreakAuf(int zaehlzeit)
	{
		if ((zaehlzeit == 1) && (istBreakAufEins()))
			return true;
		if ((zaehlzeit == 2) && (istBreakAufZwei()))
			return true;
		if ((zaehlzeit == 3) && (istBreakAufDrei()))
			return true;
		if ((zaehlzeit == 4) && (istBreakAufVier()))
			return true;
		return false;
	}

	/**
	 * @return
	 */
	public String getBreakText()
	{
		return this.breakText;
	}
	
	
	/**
	 * @param st
	 */
	public void setBreakText(String st)
	{
		this.setAenderungIstErfolgt();
		this.breakText = st;
	}
	
	
	/* 
	 * überschreiben der paint-Methode, um den Takt darzustellen
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g)    
	{
		super.paint(g);
		
		if (seperatorA)
		{
			// großer Trennstrich für geteilten Takt
			Graphics2D gSepA = (Graphics2D)g;
			gSepA.drawLine(this.getWidth() / 2 + this.getWidth() / 4, 
						  this.getHeight() / 4, 
						  this.getWidth() / 4,
						  this.getHeight() / 2 + this.getHeight() / 4);
		}
		if (seperatorB)
		{
			// großer Trennstrich für geteilten Takt
			Graphics2D gSepB = (Graphics2D)g;
			gSepB.drawLine((int)(this.getWidth() / 2.7), 
						  this.getHeight() / 6, 
						  this.getWidth() / 6,
						  (int)(this.getHeight() / 2.7));
		}

		if (seperatorC)
		{
			// großer Trennstrich für geteilten Takt
			Graphics2D gSepC = (Graphics2D)g;
			int halb = this.getHeight() / 2;
			gSepC.drawLine((int)(this.getWidth() / 2.9) + halb, 
						   (int)(this.getHeight() / 6.2) + halb, 
						   (int)(this.getWidth() / 6.2) + halb,
						   (int)(this.getHeight() / 2.9) + halb);
		}
		
	}
	
	/**
	 * @return
	 */
	public boolean istLeererTakt()
	{
		if (this.teilung == TAKT_TEILUNG_LEERER_TAKT)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 */
	public void setTaktAufLeer()
	{
		seperatorA = false;
		seperatorB = false;
		seperatorC = false;

		this.teilung = TAKT_TEILUNG_LEERER_TAKT;
		this.removeAll();
		
		this.akkord1 = null;
		this.akkord2 = null;
		this.akkord3 = null;
		this.akkord4 = null;
		
		this.setNormalenRahmen();
		if (! song.istWirdGeDruckt())
			this.setRahmen();
		else
			super.resetRahmen();
	}
	
	/**
	 * @param a1
	 * 
	 * Takt wird komplett von einem Akkord gefüllt
	 */
	public void setTaktEinTakt(Akkord a1)
	{
		seperatorA = false;
		seperatorB = false;
		seperatorC = false;
		
		this.akkord1 = a1;
		this.akkord2 = null;
		this.akkord3 = null;
		this.akkord4 = null;
	}
	/**
	 * @param a1
	 * @param a2
	 * 
	 * Der Takt wird von 2 Akkorden zu jeweils gleichen Teilen gebildet
	 */
	public void setTaktGeteilt(Akkord a1, Akkord a2)
	{
		seperatorA = true;
		seperatorB = false;
		seperatorC = false;

		this.akkord1 = a1;
		this.akkord2 = a2;
		this.akkord3 = null;
		this.akkord4 = null;
	}

	/**
	 * @param a1
	 * @param a2
	 * @param a3
	 * 
	 * Takt wird 2-geteilt und der obere Teil nochmal 2-geteilt
	 */
	public void setTaktGeteiltObenGeteilt(Akkord a1, Akkord a2, Akkord a3)
	{
		seperatorA = true;
		seperatorB = true;
		seperatorC = false;

		this.akkord1 = a1;
		this.akkord2 = a2;
		this.akkord3 = a3;
		this.akkord4 = null;
	}

	/**
	 * @param a1
	 * @param a2
	 * @param a3
	 * 
	 * Takt wird 2-geteilt und der untere Teil nochmal 2-geteilt
	 */
	public void setTaktGeteiltUntenGeteilt(Akkord a1, Akkord a2, Akkord a3)
	{
		seperatorA = true;
		seperatorB = false;
		seperatorC = true;

		this.akkord1 = a1;
		this.akkord2 = null;
		this.akkord3 = a2;
		this.akkord4 = a3;
	}

	/**
	 * @param a1
	 * @param a2
	 * @param a3
	 * @param a4
	 * 
	 * Takt wird 2-geteilt und der obere und der untere Teil werden 
	 * nochmal 2-geteilt
	 */
	public void setTaktGeteiltObenUntenGeteilt(Akkord a1, Akkord a2, 
											   Akkord a3, Akkord a4)
	{
		seperatorA = true;
		seperatorB = true;
		seperatorC = true;

		this.akkord1 = a1;
		this.akkord2 = a2;
		this.akkord3 = a3;
		this.akkord4 = a4;
	}
	
	public void setAkkord(Akkord akk)
	{
		switch (akk.getAkkordNr())
		{
			case 1:
				akkord1 = akk;
				break;
			case 2:
				akkord2 = akk;
				break;
			case 3:
				akkord3 = akk;
				break;
			case 4:
				akkord4 = akk;
				break;
			default:
				akkord1 = akk;
		}
	}
	
	/**
	 * @param a1
	 * @param a2
	 * @param a3
	 * @param a4
	 * 
	 * in dieser Methode wird bestimmt, wie der Takt zu teilen ist
	 */
	public void setAkkord(Akkord a1, Akkord a2, Akkord a3, Akkord a4)
	{
		if (this.teilung == TAKT_TEILUNG_LEERER_TAKT)
		{
			this.setTaktAufLeer();
		}
		if ((this.teilung == TAKT_TEILUNG_OHNE) && 
			(a1 != null))
		{
			this.setTaktEinTakt(a1);
		}
		else if ((this.teilung == TAKT_TEILUNG_ZWEI) && 
				 (a1 != null) && (a2 != null))
		{
			this.setTaktGeteilt(a1, a2);
		}
		else if ((this.teilung == TAKT_TEILUNG_DREIOBENLINKS) && 
				 (a1 != null) && (a2 != null) && (a3 != null))
		{
			this.setTaktGeteiltObenGeteilt(a1, a2, a3);
		}
		else if ((this.teilung == TAKT_TEILUNG_DREIUNTENRECHTS) &&
				 (a1 != null) && (a3 != null) && (a4 != null))
		{
			this.setTaktGeteiltUntenGeteilt(a1, a3, a4);
		}
		else if ((this.teilung == TAKT_TEILUNG_VIER) && 
				 (a1 != null) && (a2 != null) && (a3 != null) && (a4 != null))
		{
			this.setTaktGeteiltObenUntenGeteilt(a1, a2, a3, a4);
		}
	}
	
	/**
	 * Übergabe aller Akkorde diesen Taktes im Vector
	 * @param v
	 */
	public void setAkkord(Vector v)
	{
		if ((this.teilung == TAKT_TEILUNG_OHNE) && (v.size() == 1))
		{
			Akkord a1 = (Akkord)v.elementAt(0);
			this.setTaktEinTakt(a1);
		}
		else if ((this.teilung == TAKT_TEILUNG_ZWEI) && (v.size() == 2))
		{
			Akkord a1 = (Akkord)v.elementAt(0);
			Akkord a2 = (Akkord)v.elementAt(1);
			this.setTaktGeteilt(a1, a2);
		}
		else if ((this.teilung == TAKT_TEILUNG_DREIOBENLINKS) && (v.size() == 3))
		{
			Akkord a1 = (Akkord)v.elementAt(0);
			Akkord a2 = (Akkord)v.elementAt(1);
			Akkord a3 = (Akkord)v.elementAt(2);
			this.setTaktGeteiltObenGeteilt(a1, a2, a3);
		}
		else if ((this.teilung == TAKT_TEILUNG_DREIUNTENRECHTS) && (v.size() == 3))
		{
			Akkord a1 = (Akkord)v.elementAt(0);
			Akkord a2 = (Akkord)v.elementAt(1);
			Akkord a3 = (Akkord)v.elementAt(2);
			this.setTaktGeteiltUntenGeteilt(a1, a2, a3);
		}
		else if ((this.teilung == TAKT_TEILUNG_VIER) && (v.size() == 4))
		{
			Akkord a1 = (Akkord)v.elementAt(0);
			Akkord a2 = (Akkord)v.elementAt(1);
			Akkord a3 = (Akkord)v.elementAt(2);
			Akkord a4 = (Akkord)v.elementAt(3);
			this.setTaktGeteiltObenUntenGeteilt(a1, a2, a3, a4);
		}
	}

	/**
	 * @param kantenlaenge
	 */
	public void setTaktSize(int kantenlaenge)
	{
//		this.setSize(kantenlaenge), kantenlaenge);
		this.setSize(kantenlaenge+(kantenlaenge/8), kantenlaenge);
//		this.setSize((int)(kantenlaenge*1.5), kantenlaenge);
		this.setOpaque(false);
	}

	/**
	 * Test, um Taktgröße zu erkennen
	 */
	private void handleTakt()
	{
//		this.setRahmen();
	}

	/**
	 * bei Konstruktion eines Taktes setzen Akkord und Anpassung der
	 * Größe des Akkords;
	 * hier: genau ein Akkord
	 * @param akk
	 */
	private void setzeEinAkkWert(Akkord akk)
	{
//		int kante = (this.getWidth() / 4) * 3;
//		int xpos = kante/8;
//		int ypos = (this.getHeight() / 2) - (kante / 2);

		int kante = (int)(this.getWidth() * 0.85);
		akk.setAkkordSize(kante);
		int xpos = 3;
		int ypos = 4;
		akk.setLocation(xpos, ypos);
		this.add(akk);
	}

	/**
	 * bei Konstruktion eines Taktes setzen Akkord und Anpassung der
	 * Größe des Akkords;
	 * hier: Akkord in linken oberen Hälfte, ungeteilt
	 * @param akk
	 */
	private void setzeEinAkkWertObenLinks(Akkord akk)
	{
		int kante = (int)(this.getWidth() * 0.6);
		akk.setAkkordSize(kante);
		int xpos = (int)(this.getWidth() * 0.01);
		int ypos = (this.getHeight() / 2) - (kante*7/10);
		akk.setLocation(xpos, ypos);
		this.add(akk);
	}
	/**
	 * bei Konstruktion eines Taktes setzen Akkord und Anpassung der
	 * Größe des Akkords;
	 * hier: Akkord in rechter unteren Hälfte, ungeteilt
	 * @param akk
	 */
	private void setzeEinAkkWertUntenRechts(Akkord akk)
	{
		int kante = (int)(this.getWidth() * 0.6);
		akk.setAkkordSize(kante);
		int xpos = (int)(this.getWidth() * 0.42);
		int ypos = (int)(this.getHeight() * 0.46);
		akk.setLocation(xpos, ypos);
		this.add(akk);
	}
	/**
	 * bei Konstruktion eines Taktes setzen Akkord und Anpassung der
	 * Größe des Akkords;
	 * hier: zwei Akkorde in der oberen linken Hälfte
	 * @param a1
	 * @param a2
	 */
	private void setzeZweiAkkWerteObenLinks(Akkord a1, Akkord a2)
	{
		int kanteKlein = (int)((this.getWidth() / 5) * 1.9);
		
		a1.setAkkordSize(kanteKlein);
		int xpos1 = 0;
		int ypos1 = 0;
		a1.setLocation(xpos1, ypos1);
		
		a2.setAkkordSize(kanteKlein);
		int xpos2 = (int)(kanteKlein - (0.5 * kanteKlein));
		int ypos2 = (int)(kanteKlein - (0.31 * kanteKlein));
		a2.setLocation(xpos2, ypos2);
		this.add(a1);
		this.add(a2);
	}
	/**
	 * bei Konstruktion eines Taktes setzen Akkord und Anpassung der
	 * Größe des Akkords;
	 * hier: zwei Akkorde in der unteren rechten Hälfte
	 * @param a1
	 * @param a2
	 */
	private void setzeZweiAkkWerteUntenRechts(Akkord a1, Akkord a2)
	{
		int kanteKlein = (int)((this.getWidth() / 5) * 1.9);
		a1.setAkkordSize(kanteKlein);
		int xpos2 = this.getWidth() / 2 - (int)(kanteKlein * 0.1);
		int ypos2 = (this.getHeight() / 2) - (int)(kanteKlein * 0.2);
		a1.setLocation(xpos2, ypos2);

		a2.setAkkordSize(kanteKlein);
		int xpos3 = this.getWidth() / 2 + (int)(kanteKlein - (0.4 * kanteKlein));
		int ypos3 = (this.getHeight() / 2) + (int)(kanteKlein - (0.63 * kanteKlein));
		a2.setLocation(xpos3, ypos3);
		this.add(a1);
		this.add(a2);
	}
	
	/**
	 * kontruieren eines Takts inkl. der zugehörigen Akkorde
	 */
	public void konstuiereTakt()
	{
		if (this.teilung == TAKT_TEILUNG_OHNE)
		{
			this.setzeEinAkkWert(akkord1);
		}
		else if (this.teilung == TAKT_TEILUNG_ZWEI)
		{
			this.setzeEinAkkWertObenLinks(akkord1);
			this.setzeEinAkkWertUntenRechts(akkord2);
		}
		else if (this.teilung == TAKT_TEILUNG_DREIOBENLINKS)
		{
			this.setzeZweiAkkWerteObenLinks(akkord1, akkord2);
			this.setzeEinAkkWertUntenRechts(akkord3);
		}
		else if (this.teilung == TAKT_TEILUNG_DREIUNTENRECHTS)
		{
			this.setzeEinAkkWertObenLinks(akkord1);
			this.setzeZweiAkkWerteUntenRechts(akkord3, akkord4);
		}
		else if (this.teilung == TAKT_TEILUNG_VIER)
		{
			this.setzeZweiAkkWerteObenLinks(akkord1, akkord2);
			this.setzeZweiAkkWerteUntenRechts(akkord3, akkord4);
		}
		else if (this.teilung == TAKT_TEILUNG_LEERER_TAKT)
		{
			this.setTaktAufLeer();
		}
		handleTakt();
	}

	/**
	 * @param teilung
	 */
	public void setTeilung(int teilung)
	{
		this.setAenderungIstErfolgt();
		this.teilung = teilung;
	}

	/**
	 * @return the teilung
	 */
	public int getTeilung()
	{
		return teilung;
	}

	/**
	 * wenn vom Bediener die Teilung eines Taktes geändert wurde, dann
	 * wird der Takt mit seinen Akkorden neu konstuiert
	 * @param teilung
	 */
	public void checkeNachAenderungTeilung(int teilung)
	{
		this.teilung = teilung;
		
		switch (teilung)
		{
			case Takt.TAKT_TEILUNG_OHNE:
				akkord4 = null;
				akkord3 = null;
				akkord2 = null;
				if (akkord1 == null)
					System.out.println("!!!FEHLER!!! handleTeilung Ohne");
				akkord1.setInBearbeitung(true);
				seperatorA = false;
				seperatorB = false;
				seperatorC = false;
				break;
				
			case Takt.TAKT_TEILUNG_ZWEI:
				akkord4 = null;
				if (akkord3 != null)
				{
					akkord2 = akkord3.teilungsClone();
					akkord2.setAkkordNr(2);
					akkord2.setInBearbeitung(true);
				}
				akkord3 = null;
				if (akkord2 == null)
				{
					akkord2 = akkord1.teilungsClone();
					akkord2.setAkkordNr(2);
					akkord2.setInBearbeitung(true);
				}
				seperatorA = true;
				seperatorB = false;
				seperatorC = false;
				break;

			case Takt.TAKT_TEILUNG_DREIOBENLINKS:
				akkord4 = null;
				if (akkord3 == null)
				{
					akkord3 = akkord2.teilungsClone();
					akkord3.setAkkordNr(3);
				}
				akkord2 = akkord1.teilungsClone();
				akkord2.setAkkordNr(2);
				akkord2.setInBearbeitung(true);
				seperatorA = true;
				seperatorB = true;
				seperatorC = false;
				break;

			case Takt.TAKT_TEILUNG_DREIUNTENRECHTS:
				if (akkord3 == null)
				{
					akkord3 = akkord2.teilungsClone();
					akkord3.setAkkordNr(3);
				}
				if (akkord4 == null)
				{
					akkord4 = akkord3.teilungsClone();
					akkord4.setAkkordNr(4);
					akkord4.setInBearbeitung(true);
				}
				akkord2 = null;
				seperatorA = true;
				seperatorB = false;
				seperatorC = true;
				break;

			case Takt.TAKT_TEILUNG_VIER:
				if (akkord4 == null)
				{
					akkord4 = akkord3.teilungsClone();
					akkord4.setAkkordNr(4);
					akkord4.setInBearbeitung(true);
				}
				if (akkord2 == null)
				{
					akkord2 = akkord1.teilungsClone();
					akkord2.setAkkordNr(2);
					akkord2.setInBearbeitung(true);
				}
				seperatorA = true;
				seperatorB = true;
				seperatorC = true;
				break;

			default:
				akkord1 = null;
				akkord2 = null;
				akkord3 = null;
				akkord4 = null;
				seperatorA = false;
				seperatorB = false;
				seperatorC = false;
				break;
		}
	}
	
	/**
	 * Wenn von außen der Rahmen für den zu bearbeitenden Takt auf den nächsten
	 * folgenden bewegt werden soll, dann wird zunächst überprüft, ob innerhalb
	 * des Takes noch ein Akkord folgt (innerhalb eines Taktes werden, wenn mehr
	 * als 1 Akkord vorhanden ist, die Akkorde einzeln markiert);
	 * Wenn ein innerhalb des Taktes zu markierender Akkord vorhanden ist, dann
	 * wird dieser markiert (Rückgabewert == true);
	 * wenn kein weiterer Akkord innerhalb des Taktes vorhanden ist, dann wird
	 * nix getan und false zurückgegeben.
	 * @param aktAkk
	 * @return
	 */
	public boolean setRahmenNaechsterAkkord(Akkord aktAkk)
	{
		boolean retWert = true;
		switch (aktAkk.getAkkordNr()) {
			case 4:
				retWert = false;
				break;
			case 3:
				if (akkord4 == null)
					retWert = false;
				else
					setAktivenAkkord(akkord4);
				break;
			case 2:
				if (akkord3 == null)
					retWert = false;
				else
					setAktivenAkkord(akkord3);
				break;
			case 1:
				if (akkord2 == null)
					if (akkord3 == null)
						retWert = false;
					else
						setAktivenAkkord(akkord3);
				else
					setAktivenAkkord(akkord2);
				break;
	
			default:
				retWert = false;
				break;
		}
		return retWert;
	}

	/**
	 * Wenn von außen der Rahmen für den zu bearbeitenden Takt auf den vorherigen
	 * Takt bewegt werden soll, dann wird zunächst überprüft, ob innerhalb
	 * des Takes noch ein Akkord vorhergeht (innerhalb eines Taktes werden, wenn mehr
	 * als 1 Akkord vorhanden ist, die Akkorde einzeln markiert);
	 * Wenn ein innerhalb des Taktes zu markierender Akkord vorhanden ist, dann
	 * wird dieser markiert (Rückgabewert == true);
	 * wenn kein weiterer Akkord innerhalb des Taktes vorhanden ist, dann wird
	 * nix getan und false zurückgegeben.
	 * @param aktAkk
	 * @return
	 */
	public boolean setRahmenVorherigerAkkord(Akkord aktAkk)
	{
		boolean retWert = true;
		switch (aktAkk.getAkkordNr()) {
			case 1:
				retWert = false;
				break;
			case 2:
				if (akkord1 == null)
					retWert = false;
				else
					setAktivenAkkord(akkord1);
				break;
			case 3:
				if (akkord2 == null)
					if (akkord1 == null)
						retWert = false;
					else
						setAktivenAkkord(akkord1);
				else
					setAktivenAkkord(akkord2);
				break;
			case 4:
				if (akkord3 == null)
					retWert = false;
				else
					setAktivenAkkord(akkord3);
				break;
	
			default:
				retWert = false;
				break;
		}
		return retWert;
	}
	
	/**
	 * amrkieren des aktuell im Takt markierten Akkord
	 * @param akk
	 */
	private void setAktivenAkkord(final Akkord akk)
	{
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				akk.setAktivenAkkord();
			}
		});
	}
	
	/**
	 * den Akkord des Taktes markieren, der sich in Bearbeitung befindet
	 */
	public void setMarkierungsRahmenAlleAkkorde()
	{
		if ((akkord1 != null) && (akkord1.isInBearbeitung()))
			akkord1.setMarkiertenRahmen();
		if ((akkord2 != null) && (akkord2.isInBearbeitung()))
			akkord2.setMarkiertenRahmen();
		if ((akkord3 != null) && (akkord3.isInBearbeitung()))
			akkord3.setMarkiertenRahmen();
		if ((akkord4 != null) && (akkord4.isInBearbeitung()))
			akkord4.setMarkiertenRahmen();
	}
	
	/**
	 * @return
	 */
	public Akkord getAkkordInBearbeitung()
	{
		if ((akkord1 != null) && (akkord1.isInBearbeitung()))
			return akkord1;
		if ((akkord2 != null) && (akkord2.isInBearbeitung()))
			return akkord2;
		if ((akkord3 != null) && (akkord3.isInBearbeitung()))
			return akkord3;
		if ((akkord4 != null) && (akkord4.isInBearbeitung()))
			return akkord4;
		return null;
	}
	
	/**
	 * für alle Akkorde des Taktes wird die Maus-Markierung aktiviert (initial)
	 */
	public void aktiviereAkkordMausMarkierung()
	{
		if (akkord1 != null)
			akkord1.aktiviereMausMarkierung();
		if (akkord2 != null)
			akkord2.aktiviereMausMarkierung();
		if (akkord3 != null)
			akkord3.aktiviereMausMarkierung();
		if (akkord4 != null)
			akkord4.aktiviereMausMarkierung();
	}

	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#setMausRahmenSelektierung()
	 */
	public void setMausRahmenSelektierung()
	{
		super.setMausRahmenSelektierung();
		istTaktSelektiert = true;
	}
	/**
	 * 
	 */
	public void resetMausRahmenSelektierungMitMarker()
	{
		resetMausRahmenSelektierung();
		istTaktSelektiert = false;
	}
	/**
	 * 
	 */
	public void resetMausRahmenSelektierungNurMarkierung()
	{
		resetMausRahmenSelektierung();
	}
	/**
	 * dieser Takt ist mit dem Mausrahmen selektiert worden
	 */
	public boolean istTaktSelektiert()
	{
		return istTaktSelektiert;
	}
	/**
	 * @return
	 * Dieser Takt befindet sich in Bearbeitung (blauer Rahmen)
	 */
	public boolean istMarkiertAlsGewaehlt()
	{
		return this.istMarkiertAlsGewaehlt;
	}
	
	/**
	 * @return
	 */
	public boolean istTemporaerMarkiert()
	{
		return this.istTemporaerMarkiert;
	}

	
	/**
	 * 
	 */
	public void resetMausRahmen()
	{
		this.istTemporaerMarkiert = false;

		if (this.istMarkiertAlsGewaehlt)
		{
			this.setMarkierungsrahmen();
			this.setRahmen();
		}
		else if (this.istLeererTakt())
		{
			this.setNormalenRahmen();
			if (! song.istWirdGeDruckt())
				this.setRahmen();
			else
				super.resetRahmen();
		}
		else
		{
			super.resetRahmen();
		}
	}

	/* (non-Javadoc)
	 * @see APanel#resetRahmen()
	 */
	public void resetRahmen()
	{
		istMarkiertAlsGewaehlt = false;
		if (this.istLeererTakt())
		{
			this.setNormalenRahmen();
			if (! song.istWirdGeDruckt())
				this.setRahmen();
			else
				super.resetRahmen();
		}
		else
		{
			super.resetRahmen();
		}
	}
	
	/**
	 * 
	 */
	public void setMarkiertenRahmen()
	{
		this.setMarkierungsrahmen();
		this.setRahmen();
		this.istMarkiertAlsGewaehlt = true;
		this.istTemporaerMarkiert = true;
	}

	/**
	 * 
	 */
	public void setMausRahmen()
	{
		this.setMausMarkierungsrahmen();
		this.setRahmen();
		this.istTemporaerMarkiert = true;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return "Takt--"+teilung;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if ((teilung < 0) || (teilung > 5))
			return false;
		return true;
		
	}
	
	/**
	 *  Alle Markierungen der Akkorde werden resettet 
	 */
	public void resetAlleTaktMarkierungen()
	{
		if (akkord1 != null)
			akkord1.resetRahmen();
		if (akkord2 != null)
			akkord2.resetRahmen();
		if (akkord3 != null)
			akkord3.resetRahmen();
		if (akkord4 != null)
			akkord4.resetRahmen();
	}

	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseDragged(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseDragged(MouseEvent e)
	{
		song.rectCheckeAufZuMarkierndeComponenten(e, this);
	}
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMousePressed(java.awt.event.MouseEvent)
	 */
	public void aPanelMousePressed(MouseEvent e)
	{
		song.rectHandleMousePressed(e, this);
	}
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseReleased(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseReleased(MouseEvent e)
	{
		song.rectHandleMouseReleased(e, this);
	}
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseEntered(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseEntered(MouseEvent e)
	{
		if (! song.istPopupVisible())
		{
			this.setMausRahmen();
//			System.out.println("###### MouseEntered #####");
		}
	}
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseExited(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseExited(MouseEvent e)
	{
		if (! song.istPopupVisible())
			this.resetMausRahmen();
	}
	/* (non-Javadoc)
	 * @see AChord.AClasses.APanel#aPanelMouseClicked(java.awt.event.MouseEvent)
	 */
	public void aPanelMouseClicked(MouseEvent e)
	{
//		System.out.println("######  MARKIEREN EINES TAKTES #########"+
//				e.isPopupTrigger()+"/"+
//				e.getButton());
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			song.resetAlleTaktMarkierungen();
			song.setMarkierungAktuellerTakt(this);
			this.setMarkiertenRahmen();
		}
	}
	
	/**
	 * setzen initialer Werte des Taktes
	 */
	private void myInit()
	{
		this.setLayout(null);

		this.setMarkierungsrahmen();
		
	    this.addMouseMotionListener(new MyMouseMotionListener());
	    this.addMouseListener(new MyMouseListener());
	}
	
	/**
	 * letzten Akkord des Taktes
	 * @return
	 */
	public Akkord getLetztenAkkord()
	{
		Akkord retAkk;
		if (akkord4 != null)
			retAkk = akkord4;
		else if (akkord3 != null)
			retAkk = akkord3;
		else if (akkord2 != null)
			retAkk = akkord2;
		else
			retAkk = akkord1;
		return retAkk;
	}
	
	
	/**
	 * @return the akkord1
	 */
	public Akkord getAkkord1()
	{
		return akkord1;
	}

	/**
	 * @param akkord1 the akkord1 to set
	 */
	public void setAkkord1(Akkord akkord1)
	{
		this.akkord1 = akkord1;
	}

	/**
	 * @return the akkord2
	 */
	public Akkord getAkkord2()
	{
		return akkord2;
	}

	/**
	 * @param akkord2 the akkord2 to set
	 */
	public void setAkkord2(Akkord akkord2)
	{
		this.akkord2 = akkord2;
	}

	/**
	 * @return the akkord3
	 */
	public Akkord getAkkord3()
	{
		return akkord3;
	}

	/**
	 * @param akkord3 the akkord3 to set
	 */
	public void setAkkord3(Akkord akkord3)
	{
		this.akkord3 = akkord3;
	}

	/**
	 * @return the akkord4
	 */
	public Akkord getAkkord4()
	{
		return akkord4;
	}

	/**
	 * @param akkord4 the akkord4 to set
	 */
	public void setAkkord4(Akkord akkord4)
	{
		this.akkord4 = akkord4;
	}

	/**
	 * @param taktCopy
	 */
	public void copyWerte(Takt taktCopy)
	{
		this.setTeilung(taktCopy.getTeilung());
		this.setBreakQuot(taktCopy.getBreakQuot());
		this.setBreakText(taktCopy.getBreakText());
		
		Akkord akk1 = null;
		if (taktCopy.akkord1 != null)
			akk1 = taktCopy.akkord1.transientClone();
		Akkord akk2 = null;
		if (taktCopy.akkord2 != null)
			akk2 = taktCopy.akkord2.transientClone();
		Akkord akk3 = null;
		if (taktCopy.akkord3 != null)
			akk3 = taktCopy.akkord3.transientClone();
		Akkord akk4 = null;
		if (taktCopy.akkord4 != null)
			akk4 = taktCopy.akkord4.transientClone();
		
		this.setAkkord(akk1, akk2, akk3, akk4);
		
		boolean istAktuellerTakt = this.istMarkiertAlsGewaehlt();
		if (! this.istLeererTakt())
			this.resetRahmen();
		if (istAktuellerTakt)
			this.setMarkiertenRahmen();
				
	}
	
	/**
	 * @param song TODO
	 * @return
	 */
	public Takt transientClone(Song song)
	{
		Takt takt;
		if (song == null)
			takt = new Takt(this.teilung, this.song, teil);
		else
			takt = new Takt(this.teilung, song, teil);
		takt.setBreakQuot(this.getBreakQuot());
		takt.setBreakText(this.getBreakText());
		takt.setTaktBreak1(this.getTaktBreak1());
		takt.setAltBorder(this.getAltBorder());
		if (istMarkiertAlsGewaehlt())
		{
			takt.resetRahmen();
		}
		
		Akkord akk1 = null;
		if (this.akkord1 != null)
			akk1 = this.akkord1.transientClone();
		Akkord akk2 = null;
		if (this.akkord2 != null)
			akk2 = this.akkord2.transientClone();
		Akkord akk3 = null;
		if (this.akkord3 != null)
			akk3 = this.akkord3.transientClone();
		Akkord akk4 = null;
		if (this.akkord4 != null)
			akk4 = this.akkord4.transientClone();
		
		takt.setAkkord(akk1, akk2, akk3, akk4);
		
		return takt;
	}

	/**
	 * @param taktBreak
	 * @param zaehlzeit
	 */
	public void setTaktBreak(TaktBreak taktBreak, int zaehlzeit)
	{
		switch (zaehlzeit)
		{
			case 1:
				this.taktBreak1 = taktBreak;
				break;
			case 2:
				this.taktBreak2 = taktBreak;
				break;
			case 3:
				this.taktBreak3 = taktBreak;
				break;
			case 4:
				this.taktBreak4 = taktBreak;
				break;
	
			default:
				this.taktBreak1 = taktBreak;
				break;
			}
	}
	
	/**
	 * @return the taktBreak
	 */
	public TaktBreak getTaktBreak1()
	{
		return taktBreak1;
	}

	/**
	 * @param taktBreak the taktBreak to set
	 */
	public void setTaktBreak1(TaktBreak taktBreak1)
	{
		this.taktBreak1 = taktBreak1;
	}
	
	/**
	 * @return
	 */
	public TaktBreak getTaktBreak2()
	{
		return taktBreak2;
	}

	/**
	 * @param taktBreak2
	 */
	public void setTaktBreak2(TaktBreak taktBreak2)
	{
		this.taktBreak2 = taktBreak2;
	}

	/**
	 * @return
	 */
	public TaktBreak getTaktBreak3()
	{
		return taktBreak3;
	}

	/**
	 * @param taktBreak3
	 */
	public void setTaktBreak3(TaktBreak taktBreak3)
	{
		this.taktBreak3 = taktBreak3;
	}

	/**
	 * @return
	 */
	public TaktBreak getTaktBreak4()
	{
		return taktBreak4;
	}

	/**
	 * @param taktBreak4
	 */
	public void setTaktBreak4(TaktBreak taktBreak4)
	{
		this.taktBreak4 = taktBreak4;
	}

	/**
	 * @return the tempTaktNr
	 */
	public int getTempTaktNr()
	{
		return tempTaktNr;
	}

	/**
	 * @param tempTaktNr the tempTaktNr to set
	 */
	public void setTempTaktNr(int tempTaktNr)
	{
		this.tempTaktNr = tempTaktNr;
	}
}
