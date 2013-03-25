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
 * 
 * Stellt ein Breakzeichen (Pfeil nach unten über Takt) dar
 * 
 * 
 */
package AChord.Schema;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;

public class TaktBreak extends APanel
{
    private final static long serialVersionUID = 1;

    int taktQuotient = 0;
	String breakText = "";
	
	int hoehe = 0;

	/**
	 * Konstruktor
	 * @param quotient
	 */
	public TaktBreak(int quotient)
	{
		super();
		this.taktQuotient = quotient;
	}
	
	/**
	 * @param width
	 * @param height
	 */
	public void setBreakSize(int width, int height)
	{
		this.setSize(width, height);
		this.setOpaque(false);
	}
	
	/**
	 * @param hoehe
	 */
	public void setBreakHoehe(int hoehe)
	{
		this.hoehe = hoehe;
		this.setSize(hoehe, hoehe);
		this.setOpaque(false);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param taktBreite
	 */
	public void setBreakLocation(int x, int y, int taktBreite)
	{
		int xDelta = 0;
		if (this.taktQuotient == 1)
			xDelta = (int)(taktBreite * 0.0);
		else if (this.taktQuotient == 2)
			xDelta = (int)(taktBreite * 0.30);
		else if (this.taktQuotient == 3)
			xDelta = (int)(taktBreite * 0.60);
		else if (this.taktQuotient == 4)
			xDelta = (int)(taktBreite * 0.90);
			
		this.setLocation(x + xDelta, y);
	}
	
	/**
	 * @param str
	 */
	public void setBreakText(String str)
	{
		this.breakText = " " + str;
	}

	/* 
	 * malen eines Break-Zeichens
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g)    
	{
		super.paint(g);
		
		Graphics2D gra = (Graphics2D)g;

		int fontsize = hoehe;
		Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.BOLD, fontsize);
		gra.setFont(f);
		FontMetrics fm = gra.getFontMetrics(f);
		int breite = fm.stringWidth(breakText);
		
		this.setSize(hoehe+breite, hoehe);

		gra.drawString(breakText, 10, this.getHeight()-2);

		gra.drawLine(4, 0,
					 4, this.getHeight());
		gra.drawLine(5, 0,
					 5, this.getHeight());
		gra.drawLine(1, this.getHeight() - 5, 
					 5, this.getHeight());
		gra.drawLine(8, this.getHeight() - 5, 
				 	 4, this.getHeight());

		// Test
		//gra.drawRect(1, 1, (int)this.getWidth()-2, (int)this.getHeight()-2);
	}

}
