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
 * Beschreibung:
 * 		Darstellung eines Akkordes
 * 
 * 		Aufbau:  	 b7/9 		 #5
 * 					B			F
 * 				     m			 m
 * 
 * Diese Klasse enthält alle Daten und Methoden, um auf einen
 * Akkord zuzugreifen und ihn darzustellen
 *
 */
package AChord.Schema;


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;
import AChord.MainFrame.PanelReiter3Takt;

public class Akkord extends APanel
{
    private final static long serialVersionUID = 1;

	public final static String AKK_TON_C = "C";
	public final static String AKK_TON_D = "D";
	public final static String AKK_TON_E = "E";
	public final static String AKK_TON_F = "F";
	public final static String AKK_TON_G = "G";
	public final static String AKK_TON_A = "A";
	private final static String AKK_TON_H = "H";
	public final static String AKK_HALBTON = "#";
	public final static String AKK_TON_FEHLER = "?";
	private final static String[] AKK_TONMENGE = new String[] 
	              {AKK_TON_H, 		//--Dummy--
				   AKK_TON_C,   	//--1--
				   AKK_HALBTON,		//--2--								 
				   AKK_TON_D,  	 	//--3--
				   AKK_HALBTON,		//--4--
				   AKK_TON_E,   	//--5--
				   AKK_TON_F,   	//--6--
				   AKK_HALBTON,		//--7--
				   AKK_TON_G,   	//--8--
				   AKK_HALBTON,		//--9--
				   AKK_TON_A,   	//--10--
				   AKK_HALBTON,		//--11--
				   AKK_TON_H,		//--12--
				   AKK_TON_C};		//--Dummy--
	

	public final static String AKK_TON_HALBERHOEHT = "#";
	public final static String AKK_TON_HALBVERMINDERT = "b";
	public final static String AKK_TON_HALBOHNE = "";
	public final static String[] AKK_HALBMENGE = new String[] {AKK_TON_HALBOHNE,		//--0--
										AKK_TON_HALBERHOEHT,		//--1--
										AKK_TON_HALBVERMINDERT};	//--2--

	final static String AKK_TON_ART_DUR = "";
	final static String AKK_TON_ART_MOLL = "m";
	final static String AKK_TON_ART_FEHLER = "";
	final static String[] AKK_TONARTMENGE = new String[] {AKK_TON_ART_DUR, 
													      AKK_TON_ART_MOLL};
	
	final static String AKK_KL_MAJOR = "maj";
	final static String AKK_KL_MAJOR7 = "maj7";
	final static String AKK_KL_SEXTE = "6";
	final static String AKK_KL_SEPTIME = "7";
	final static String AKK_KL_NONE = "9";
	final static String AKK_KL_SEXTNONE = "6/9";
	final static String AKK_KL_SEPTQUINT = "7/5";
	final static String AKK_KL_VERMINDERT = "o";
	final static String AKK_KL_UEBERMAESSIG = "+";
	final static String AKK_KL_SUS = "sus4";
	final static String AKK_KL_SEPTNONE = "7/9";
	final static String AKK_KL_OHNE = "ohne";
	final static String AKK_KL_FEHLER = "";
	final static String[] AKK_KLMENGE = new String[] {AKK_KL_MAJOR, 
													  AKK_KL_MAJOR7,
													  AKK_KL_SEXTE,
													  AKK_KL_SEPTIME,
													  AKK_KL_NONE,
													  AKK_KL_SEXTNONE,
													  AKK_KL_SEPTQUINT,
													  AKK_KL_VERMINDERT,
													  AKK_KL_UEBERMAESSIG,
													  AKK_KL_OHNE,
													  AKK_KL_SEPTNONE};
	

	private int akkTon = 0;
	private int akkHalbton = 0;
	private String akkArt = "";
	private String akkKlang = "";
	
	private String akkMain = "";
	private String akkMainHalbton = "";
	private String akkOben = "";
	private String akkUnten = "";

	double faktor = 1;
	int deltaY = 0;
	int deltaX = 0;
	
	int akkordNr = 0;
	
	boolean inBearbeitung = false;

	/**
	 * Konstruktor
	 * @param nr
	 */
	public Akkord(int nr)
	{
		super();
		akkordNr = nr;
		
		akkTon = 0;
		akkHalbton = 0;
		akkArt = "";
		akkKlang = "";
		
		akkMain = "";
		akkMainHalbton = "";
		akkOben = "";
		akkUnten = "";
		
		faktor = 1.0;
		deltaY = 0;
		
		this.setOpaque(false);
	}
	
	/**
	 * Konstruktor
	 * @param nr
	 * @param istBearbeitung
	 */
	public Akkord(int nr, boolean istBearbeitung)
	{
		super();
		akkTon = 0;
		akkHalbton = 0;
		akkArt = "";
		akkKlang = "";
		
		akkMain = "";
		akkMainHalbton = "";
		akkOben = "";
		akkUnten = "";
		
		faktor = 1.0;
		deltaY = 0;
		
		this.setOpaque(false);
		
		this.inBearbeitung = istBearbeitung;
	}
	
	/**
	 * Konstruktor
	 * @param nr
	 * @param ton
	 * @param halbton
	 * @param art
	 * @param klang
	 */
	public Akkord(int nr, int ton, int halbton, String art, String klang)
	{
		super();
		akkTon = ton;
		akkHalbton = halbton;
		akkArt = art;
		akkKlang = klang;
		
		vorbereiteAkkord();
	}

	/**
	 * @param ton
	 * @param halbton
	 * @param art
	 * @param klang
	 */
	public void setAkkord(int ton, int halbton, String art, String klang)
	{
		akkTon = ton;
		akkHalbton = halbton;
		akkArt = art;
		akkKlang = klang;
		
//		setRahmen();	// Test, um Größe des Akkords darzustellen
		
		vorbereiteAkkord();
	}
	
	/* 
	 * überschreiben der paint-Methode, um den Akkord darzustellen
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g)    
	{
		super.paint(g);
		
		int fontsizeAkkTon = 0;
		int fontsizeObenUnten = 0;
		int fontX = -1;
		int fontY = 0;

		Graphics2D g2 = (Graphics2D)g;
		fontsizeAkkTon = (int)((this.getHeight() + 4) * faktor);
		fontY = (int)this.getHeight() - (fontsizeAkkTon / 10) - deltaY;

		// Test, um Größe zu erkennen
		//g2.drawRect(1, 1, (int)this.getWidth()-3, (int)this.getHeight()-2);

		Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(), Font.BOLD, fontsizeAkkTon);
		g2.setFont(f);
		g2.drawString(akkMain, fontX+deltaX, fontY);

		FontMetrics fm = g2.getFontMetrics(f);
		int width = fm.stringWidth(akkMain);

		fontsizeObenUnten = (int)((double)fontsizeAkkTon * 0.35);
		Font f2 = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.BOLD,fontsizeObenUnten);
		g2.setFont(f2);

		if (akkOben.length() > 0)
		{
			int obenX = fontX + width - 1;
			int obenY = fontY - (int)(fontsizeAkkTon * 0.65);

			// Sicherheitsabfrage, da sonst das obere Zeichen nach oben rauswandert
			if ((fontsizeObenUnten-obenY)>2) 
				obenY = fontsizeObenUnten - 2;

			if (akkOben.length() > 2)
				obenX = obenX - 2;
			
			g2.drawString(akkOben, obenX, obenY);
		}

		if (akkUnten.length() > 0)
		{
			int obenX = fontX+width-2;
			int obenY = fontY+0*(fontsizeAkkTon / 5);
			g2.drawString(akkUnten, obenX+2, obenY);
		}
	}

	/**
	 * @param kantenlaenge
	 */
	public void setAkkordSize(int kantenlaenge)
	{
//		this.setSize(kantenlaenge, kantenlaenge);
		this.setSize(kantenlaenge, kantenlaenge-kantenlaenge/6);
		this.setOpaque(false);
	}
	

	/**
	 * 	überprüfen, ob alles korrekt, setzen der interne
	 *  Akkordwerte und setzen der Print-Werte (faktor, deltaX, deltaY)
	 *  in Abhängigkeit der Akkord-Art und des Akkord-Klanges
	 */
	public void vorbereiteAkkord()
	{
		this.setAenderungIstErfolgt();
		
		akkMain = bestimmeTon();
		akkUnten = bestimmeArt();
		akkOben = akkMainHalbton + bestimmeKlang();
		
		if ((akkOben.length() == 0) && (akkUnten.length() == 0))
		{
			faktor = 0.95;
			deltaY = 0;
			deltaX = 0;
		}
		else if ((akkOben.length() == 2) && (akkUnten.length() == 0)) 
		{
			faktor = 0.9;
			deltaY = 0;
			deltaX = 0;
		}
		else if (akkOben.length() == 2)
		{
			faktor = 0.9;
			deltaY = 3;
			deltaX = 0;
		}
		else if (akkOben.length() == 3)
		{
			faktor = 0.9;
			deltaY = 3;
			deltaX = 0;
		}
		else if (akkOben.length() == 4)
		{
			faktor = 0.8;
			deltaY = 3;
			deltaX = 0;
		}
		else if (akkOben.length() > 4)
		{
			faktor = 0.7;
			deltaY = 3;
			deltaX = 0;
		}
		else	
		{
			faktor = 0.9;
			deltaY = -1;
			deltaX = 0;
		}
	}

	/**
	 * bestimmt aus der int-Darstellung die String-Darstellung
	 * @param intTon
	 * @return
	 */
	private String getAkkTonString(int intTon)
	{
		String strTon = AKK_TONMENGE[intTon];
		if (strTon.equals("H"))
			strTon = Konfiguration.getInstance().getBTonAkkord();
		return strTon;
	}
	
	/**
	 * @return
	 */
	public boolean istHalbTon()
	{
		if (getAkkTonString(this.akkTon).equals(AKK_HALBTON))
			return true;
		else
			return false;
	}
	
	/**
	 * @return
	 * Kontrolle, ob korekter Ton übergeben wurde und Rückgabe des Tons
	 */
	private String bestimmeTon()
	{
		if (! this.isValid())
			return AKK_TON_FEHLER;
		
		String st = getAkkTonString(this.akkTon);
		// wenn denn ein Halbton vorliegt...
		if (st.equals(AKK_HALBTON))
		{
			// ...dann wird geschaut, ob akkHalbton (Attribut "HalbTon" in XML)
			// 1 (vermindert) oder 2 (erhöht ist)
			// wenn 0 und Halbton, dann wird von vermindert ausgegangen
			if ((akkHalbton == 1) || (akkHalbton == 0))
			{
				st = getAkkTonString(this.akkTon + 1);
				akkMainHalbton = AKK_TON_HALBVERMINDERT;
			}
			else
			{
				st = getAkkTonString(this.akkTon - 1);
				akkMainHalbton = AKK_TON_HALBERHOEHT;
			}
		}
		else
		{
			akkMainHalbton = AKK_TON_HALBOHNE;
		}
		if ((st.equals("H") && (akkMainHalbton.equals(AKK_TON_HALBVERMINDERT))))
			st = Konfiguration.getInstance().getBTonAkkordWennHalb();
				
		return st;
	}

	/**
	 * @return
	 */
	private String bestimmeArt()
	{
		String st = AKK_TON_ART_FEHLER;
		for (int i =0 ; i < AKK_TONARTMENGE.length ; i++)
		{
			if (akkArt.equals(AKK_TONARTMENGE[i]))
				st = akkArt;
		}
		return st;
	}

	/**
	 * @return
	 */
	private String bestimmeKlang()
	{
		String st = AKK_KL_FEHLER;
		for (int i =0 ; i < AKK_KLMENGE.length ; i++)
		{
			if (akkKlang.equals(AKK_KLMENGE[i]))
				st = akkKlang;
		}
		if (st.equals(AKK_KL_OHNE))
		{
			st = "";
		}
		else if (st.equals(AKK_KL_FEHLER))
		{
			st = akkKlang;
		}
		return st;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return "Akkord--"+this.akkTon+"/"+this.akkHalbton+"/"+
						this.akkArt+"/"+this.akkKlang+"------"+
						this.akkMain+"/"+this.akkMainHalbton;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if ((this.akkTon < 1) || (this.akkTon > 12))
			return false;
		if ((this.akkHalbton < 0) || (this.akkHalbton > 2))
			return false;
		return true;
	}
	
	/**
	 * @return
	 */
	public Akkord transientClone()
	{
		Akkord akk = new Akkord(this.getAkkordNr());
		
		akk.setAkkord(this.akkTon, this.akkHalbton, this.akkArt, this.akkKlang);
		akk.setInBearbeitung(this.inBearbeitung);
	
		return akk;
	}
	
	public Akkord teilungsClone()
	{
		Akkord akk = new Akkord(this.getAkkordNr());
		akk.setAkkTon(this.akkTon);
		akk.setAkkHalbton(this.akkHalbton);
		// Konfiguration
		int anzeige = Konfiguration.getInstance().getKonfAnzeigeNeuerTakt();
		switch (anzeige)
		{
			case Konfiguration.ANZEIGE_NEUERTAKT_KLANG:
				akk.setAkkKlang(this.getAkkKlang());
				break;
			case Konfiguration.ANZEIGE_NEUERTAKT_ART:
				akk.setAkkArt(this.getAkkArt());
				break;
			case Konfiguration.ANZEIGE_NEUERTAKT_ARTKLANG:
				akk.setAkkKlang(this.getAkkKlang());
				akk.setAkkArt(this.getAkkArt());
				break;

			default:
				break;
		}

		akk.vorbereiteAkkord();
		return akk;
	}

	/**
	 * @return
	 */
	public boolean istAkkordNackt()
	{
		if (akkTon == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * @return the akkTon
	 */
	public int getAkkTon()
	{
		return akkTon;
	}

	/**
	 * @param akkTon the akkTon to set
	 */
	public void setAkkTon(int akkTon)
	{
		this.akkTon = akkTon;
	}
	
	/**
	 * @param ton
	 */
	public void setAkkTon(String ton)
	{
		for (int i=1;i<13;i++)
		{
			if (getAkkTonString(i).equals(ton))
			{
				if (this.akkHalbton == 0)
					this.akkTon = i;
				else if (this.akkHalbton == 1)
				{
					if ((i == 1) || (i == 6))
					{
						this.akkTon = i;
						this.akkHalbton = 0;
					}
					else
						this.akkTon = i-1;
				}
				else if (this.akkHalbton == 2)
				{
					if ((i == 5) || (i == 12))
					{
						this.akkTon = i;
						this.akkHalbton = 0;
					}
					else
						this.akkTon = i+1;
				}
				else
					this.akkTon = i;
			}
		}
	}

	/**
	 * @return the akkHalbton
	 */
	public int getAkkHalbton()
	{
		return akkHalbton;
	}

	/**
	 * @param akkHalbton the akkHalbton to set
	 */
	public void setAkkHalbton(int akkHalbton)
	{
		this.akkHalbton = akkHalbton;
	}
	
	/**
	 * @param halbTon
	 */
	public void setAkkHalbTon(String halbTon)
	{
		String st = getAkkTonString(this.akkTon);
		// wenn denn ein Halbton vorliegt...
		if (! st.equals(AKK_HALBTON))
		{
			// ...dann wird geschaut, wohin der Ton verändert werden muss,
			//  nach oben (Kreuz) oder nach untern (b)
			if (halbTon.equals("#"))
			{
				if ((akkTon != 5) && (akkTon != 12))
				{
					akkTon++;
					akkHalbton = 2;
				}
			} 
			else if (halbTon.equals("b"))	//b
			{
				if ((akkTon != 6) && (akkTon != 1))
				{
					akkTon--;
					akkHalbton = 1;
				}
			}
			else
			{
				akkHalbton = 0;
			}
		}
		else
		{
			if (halbTon.equals("#"))
			{
				if ((akkTon != 5) && (akkTon != 12))
				{
					akkTon = akkTon + 2;
					akkHalbton = 2;
				}
			} 
			else if (halbTon.equals("b"))	//b
			{
				if ((akkTon != 6) && (akkTon != 1))
				{
					akkTon = akkTon - 2;
					akkHalbton = 1;
				}
			}
			else
			{
				if (akkHalbton == 1)
					akkTon++;
				else if (akkHalbton == 2)
					akkTon--;
				akkHalbton = 0;
			}
		}
	}

	/**
	 * @return the akkArt
	 */
	public String getAkkArt()
	{
		return akkArt;
	}

	/**
	 * @param akkArt the akkArt to set
	 */
	public void setAkkArt(String akkArt)
	{
		if (akkArt.equals("Dur"))
			this.akkArt = "";
		else if (akkArt.equals("Moll"))
			this.akkArt = "m";
		else
			this.akkArt = akkArt;
	}

	/**
	 * @return the akkKlang
	 */
	public String getAkkKlang()
	{
		return akkKlang;
	}

	/**
	 * @param akkKlang the akkKlang to set
	 */
	public void setAkkKlang(String akkKlang)
	{
		this.akkKlang = akkKlang;
	}
	
	/**
	 * enthält die laufende Nr des Akkordes im Teil
	 * @return the akkordNr
	 */
	public int getAkkordNr()
	{
		return akkordNr;
	}

	/**
	 * enthält die laufende Nr des Akkordes im Teil
	 * @param akkordNr the akkordNr to set
	 */
	public void setAkkordNr(int akkordNr)
	{
		this.akkordNr = akkordNr;
	}

	/**
	 * @return
	 */
	public boolean istHalbTonNachUntenVerboten()
	{
		if ((akkTon == 6) || (akkTon == 1))
			return true;
		else
			return false;
	}

	/**
	 * @return
	 */
	public boolean istHalbTonNachObenVerboten()
	{
		if ((akkTon == 5) || (akkTon == 12))
			return true;
		else
			return false;
	}
	
	/**
	 * @return
	 */
	public String wandleTonIntern2String()
	{
		return bestimmeTon();
	}
	
	/**
	 * @param ton
	 * @return
	 */
	public int wandleTonString2Intern(String ton, String halbTon)
	{
		this.akkHalbton = 0;
		
		for (int i=0;i<2;i++)
		if (AKK_HALBMENGE[i].equals(halbTon))
			this.akkHalbton = i;
		
		for (int i=0;i<13;i++)
		{
			if (getAkkTonString(i).equals(ton))
			{
				if (this.akkHalbton == 0)
					this.akkTon = i;
				else if (this.akkHalbton == 0)
					this.akkTon = i+1;
				else if (this.akkHalbton == 2)
					this.akkTon = i-1;
				else
					this.akkTon = i;
						
			}
		}
		
		return this.akkTon;
	}
	
	/**
	 * @return
	 */
	public int getAkkTonOhneHalb()
	{
		int ret = 0;
		for (int i=0;i<13;i++)
		{
			if (getAkkTonString(i).equals(this.akkMain))
			{
//				if (this.akkHalbton == 0)
					ret = i;
//				else if (this.akkHalbton == 0)
//					ret = i+1;
//				else if (this.akkHalbton == 2)
//					ret = i-1;
//				else
//					ret = i;
			}
		}
//		System.out.println("*******+++++********"+this.akkMain+"*"+this.akkHalbton+"*"+ret);
		return ret;
	}

	/**
	 * dieser Akkord wird akutell bearbeit
	 * @return the inBearbeitung
	 */
	public boolean isInBearbeitung()
	{
		return inBearbeitung;
	}

	/**
	 * dieser Akkord wird akutell bearbeit
	 * @param inBearbeitung the inBearbeitung to set
	 */
	public void setInBearbeitung(boolean inBearbeitung)
	{
		this.inBearbeitung = inBearbeitung;
	}

	/**
	 * Behandlung Rahmen und Mausrahmen
	 */
	public void resetMausRahmen()
	{
		if (! this.isInBearbeitung())
		{
			super.resetRahmen();
		}
	}

	/* (non-Javadoc)
	 * @see APanel#resetRahmen()
	 */
	public void resetRahmen()
	{
		this.inBearbeitung = false;
		super.resetRahmen();
	}

	/**
	 * Behandlung Rahmen und Mausrahmen
	 */
	public void setMarkiertenRahmen()
	{
		this.setMarkierungsrahmen();
		this.setRahmen();
		this.inBearbeitung = true;
	}

	/**
	 * Behandlung Rahmen und Mausrahmen
	 */
	public void setMausRahmen()
	{
		if (!inBearbeitung)
		{
			this.setMausMarkierungsrahmen();
			this.setRahmen();
		}
	}

	/**
	 * @param e
	 */
	private void taktMouseEntered(MouseEvent e)
	{
//		System.out.println("-----");
		this.setMausRahmen();
	}
	/**
	 * @param e
	 */
	private void taktMouseExited(MouseEvent e)
	{
		this.resetMausRahmen();
	}
	/**
	 * @param e
	 */
	private void taktMousePressed(MouseEvent e)
	{
//		System.out.println("######  taktMousePressed");
//		System.out.println("+++"+this.getParent());
//		System.out.println("++++"+this.getParent().getParent());
//		System.out.println("+++++"+this.getParent().getParent().getParent());
//		System.out.println("++++++"+this.getParent().getParent().getParent().getParent());
	}

	public void setAktivenAkkord() 
	{
		if ((this.getParent() != null) &&
			(this.getParent() instanceof Takt) &&
			(this.getParent().getParent() != null) &&
			(this.getParent().getParent().getParent() != null) &&
			(this.getParent().getParent().getParent() instanceof PanelReiter3Takt))
		{
//			System.out.println("######  akkMousePressed "+this.akkTon+"/"+
//					this.akkHalbton+"/"+
//					this.akkordNr+"/"+this.inBearbeitung);
			((Takt)this.getParent()).resetAlleTaktMarkierungen();
			this.setMarkiertenRahmen();
			((PanelReiter3Takt)this.getParent().getParent().getParent()).setMarkierungAktuellerAkkord(this);
//			System.out.println("######  akkMousePressed "+this.akkTon+"/"+
//					this.akkHalbton+"/"+
//					this.akkordNr+"/"+this.inBearbeitung);
		}
	}
	/**
	 * @param e
	 */
	private void taktMouseReleased(MouseEvent e)
	{
//		System.out.println("######  taktMouseReleased");
	}
	/**
	 * @param e
	 */
	private void taktMouseClicked(MouseEvent e)
	{
//		System.out.println("######  taktMouseClicked");
		setAktivenAkkord();
	}

	/**
	 * 
	 */
	public void aktiviereMausMarkierung()
	{
		if (this.getMouseListeners().length > 0)
			return;
		
		this.addMouseListener
	    (
	    	new MouseListener() 
	    	{
	    		public void mousePressed(MouseEvent e) 
	    		{
	    			taktMousePressed(e);
	    		}
				public void mouseReleased(MouseEvent e) 
				{
					taktMouseReleased(e);
				}
				public void mouseClicked(MouseEvent e) 
				{
					taktMouseClicked(e);
				}
				public void mouseEntered(MouseEvent e) 
				{
					taktMouseEntered(e);
				}
				public void mouseExited(MouseEvent e) 
				{
					taktMouseExited(e);
				}
    	      }
	    );
	}

}