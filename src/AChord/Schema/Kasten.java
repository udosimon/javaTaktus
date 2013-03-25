/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 22.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * grafische Darstellung eines Kastens (1 und 2), wenn gefordert
 *
 */
package AChord.Schema;

import java.awt.Font;
import java.awt.Graphics2D;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;



public class Kasten extends APanel
{
    private final static long serialVersionUID = 1;

    String kastenNr = "0";
	
	/**
	 * Konstruktor
	 */
	public Kasten(String kastenNr)
	{
		super();
		this.kastenNr = kastenNr;
	}
	
	/**
	 * @param kastenNr
	 */
	public void setKastenNr(String kastenNr)
	{
		this.kastenNr = kastenNr;
	}
	
	/**
	 * @param width
	 * @param height
	 */
	public void setKastenSize(int width, int height)
	{
		this.setSize(width, height);
		this.setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g)    
	{
		super.paint(g);
		Graphics2D gra = (Graphics2D)g;

		int fontsize = Math.min(12, (int)(this.getHeight() / 3 * 2));

		gra.drawLine(1, 3, this.getWidth() - fontsize/2, 3);
		gra.drawLine(1, 3, 1, this.getHeight());
		
		int fontY = (int)(fontsize * 0.8);
		int fontX = this.getWidth() - fontsize/2;
		Font f = new Font(Konfiguration.getInstance().getSchriftArt(),Font.PLAIN, fontsize);
		gra.setFont(f);
		gra.drawString(kastenNr, fontX, fontY);

		// Test
		//System.out.println("=="+fontsize+"=="+fontY);
		//gra.drawRect(1, 1, (int)this.getWidth()-2, (int)this.getHeight()-2);

	}

}
