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
 * Stellt ein Wiederholungszeichen dar
 * 
 * Links bedeutet, Punkte nach rechts; Rechts bedeutet,
 * Punkte nach links
 * 
 */
package AChord.Schema;

import java.awt.Graphics2D;

import AChord.AClasses.APanel;


public class WiederholungsZeichen extends APanel
{
    private final static long serialVersionUID = 1;

    static final int WZEICHEN_RELAIVEBREITE = 3;
	
	boolean istLinks = true;
	
	/**
	 * Konstruktor
	 * @param istLinks
	 */
	public WiederholungsZeichen(boolean istLinks)
	{
		super();
		this.istLinks = istLinks;
	}
	
	/**
	 * 
	 */
	public void setLinks()
	{
		this.istLinks = true;
	}
	
	/**
	 * 
	 */
	public void setRechts()
	{
		this.istLinks = false;
	}
	
	/**
	 * @param hoehe
	 */
	public void setWiederholungsZeichenSize(int hoehe)
	{
		this.setSize((hoehe / WZEICHEN_RELAIVEBREITE), hoehe);
		this.setOpaque(false);
	}
	
	/* 
	 * malen des Widerholungszeichen
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g)    
	{
		super.paint(g);
		Graphics2D gra = (Graphics2D)g;
		if (this.istLinks)
		{
			gra.drawLine(this.getWidth() / 4, 0,
						 this.getWidth() / 4, this.getHeight());
			gra.drawLine(this.getWidth() / 2, 0,
						 this.getWidth() / 2, this.getHeight());
			gra.fillOval(this.getWidth() / 6 * 5, 
						 this.getHeight() / 2 - this.getHeight() / 6, 
						 this.getWidth() / 5, 
						 this.getWidth() / 4);
			gra.fillOval(this.getWidth() / 6 * 5, 
						 this.getHeight() / 2 + this.getHeight() / 11, 
						 this.getWidth() / 5, 
						 this.getWidth() / 4);
		}
		else
		{
			gra.drawLine(this.getWidth() / 4 * 3, 0,
						 this.getWidth() / 4 * 3, this.getHeight());
			gra.drawLine(this.getWidth() / 2, 0,
						 this.getWidth() / 2, this.getHeight());
			gra.fillOval(this.getWidth() / 6, 
						 this.getHeight() / 2 - this.getHeight() / 6, 
						 this.getWidth() / 5, 
						 this.getWidth() / 4);
			gra.fillOval(this.getWidth() / 6, 
						 this.getHeight() / 2 + this.getHeight() / 11, 
						 this.getWidth() / 5, 
						 this.getWidth() / 4);
		}
	
		// Test
		//gra.drawRect(1, 1, (int)this.getWidth()-2, (int)this.getHeight()-2);
	}
	
	/**
	 * @param taktbreite
	 * @return
	 */
	static int getWiederholungszeichenBreite(int taktbreite)
	{
		return (taktbreite / WZEICHEN_RELAIVEBREITE);
	}
}
