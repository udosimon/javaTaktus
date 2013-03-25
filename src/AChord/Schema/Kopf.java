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
 * 
 * Stellt ein Kopfzeichen dar
 * 
 * 
 */
package AChord.Schema;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;


public class Kopf extends APanel
{
    private final static long serialVersionUID = 1;

    /**
	 * Konstruktor
	 */
	public Kopf()
	{
		super();
	}
	
	/**
	 * @param width
	 * @param height
	 */
	public void setKopfSize(int width, int height)
	{
		this.setSize(width, height);
		this.setOpaque(false);
	}

	/* 
	 * malen des Kopfzeichens
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g)    
	{
		super.paint(g);
		Graphics2D gra = (Graphics2D)g;

		int fontsize = this.getHeight();
		Font f = new Font(Konfiguration.getInstance().getSchriftArtNarrow(),Font.BOLD, fontsize);
		gra.setFont(f);
		gra.drawString("O", 0, (int)(this.getHeight()*0.88));
		FontMetrics fm = gra.getFontMetrics(f);
		int breite = fm.stringWidth("O");

		if (fontsize >= 12)
		{
			gra.drawLine((int)(breite *0.48), 0,
						 (int)(breite *0.48), this.getHeight());
			gra.drawLine(0, (int)(this.getHeight() * 0.5),
						 breite, (int)(this.getHeight() * 0.5));
		}
		else
		{
			gra.drawLine((int)(breite *0.4), 0,
					 (int)(breite *0.4), this.getHeight());
			gra.drawLine(0, (int)(this.getHeight() * 0.45),
					 breite, (int)(this.getHeight() * 0.45));
		}

		// Test
		//gra.drawRect(1, 1, (int)this.getWidth()-2, (int)this.getHeight()-2);
	}

}
