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
 *  - eine Zeile Text
 *  
 *  TODO:
 *  Text-Zeilen markierbar machen (wie Takt); ist bis jetzt nur vorbereitet.
 * 
 */
package AChord.Schema;

import java.awt.event.MouseEvent;

import AChord.AClasses.ALabel;

public class TextZeile extends ALabel
{
    private final static long serialVersionUID = 1;

    boolean istMarkiertAlsGewaehlt = false;

	/**
	 * Konstruktor
	 */
	public TextZeile()
	{
		super();
		myInit();
	}
	
	/* (non-Javadoc)
	 * @see APanel#resetRahmen()
	 */
	public void resetRahmen()
	{
		istMarkiertAlsGewaehlt = false;
		super.resetRahmen();
	}
	
	/**
	 * 
	 */
	public void setMausRahmen()
	{
		this.setMausMarkierungsrahmen();
		this.setRahmen();
	}

	/**
	 * 
	 */
	public void resetMausRahmen()
	{
		if (this.istMarkiertAlsGewaehlt)
		{
			this.setMarkierungsrahmen();
			this.setRahmen();
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
	}

	private void taktMouseClicked(MouseEvent e)
	{
//		System.out.println("######  taktMouseClicked");
	}
	private void taktMouseEntered(MouseEvent e)
	{
		this.setMausRahmen();
	}
	private void taktMouseExited(MouseEvent e)
	{
		this.resetMausRahmen();
	}
	private void taktMousePressed(MouseEvent e)
	{
		if ((this.getParent() != null) &&
			(this.getParent().getParent() != null) &&
			(this.getParent().getParent() instanceof Song))
			{
				((Song)this.getParent().getParent()).resetAlleTextMarkierungen();
				((Song)this.getParent().getParent()).setMarkierungAktuellerText(this);
				this.setMarkiertenRahmen();
			}
		else if ((this.getParent() != null) &&
				(this.getParent().getParent() != null) &&
				(this.getParent().getParent().getParent() != null) &&
				(this.getParent().getParent().getParent() instanceof Song))
		{
			((Song)this.getParent().getParent().getParent()).resetAlleTextMarkierungen();
			((Song)this.getParent().getParent().getParent()).setMarkierungAktuellerText(this);
			this.setMarkiertenRahmen();
		}

	}
	private void taktMouseReleased(MouseEvent e)
	{
//		System.out.println("######  taktMouseReleased");
	}

	
	private void myInit()
	{
		this.setOpaque(false);
		
		this.setMarkierungsrahmen();
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString()
	{
		return this.getText();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isValid()
	 */
	public boolean isValid()
	{
		if (this.getText() == null)
			return false;
		return true;
	}

	/**
	 * @return
	 */
	public TextZeile transientClone()
	{
		TextZeile zeile = new TextZeile();
		zeile.setText(this.getText());
		return zeile;
	}
}
