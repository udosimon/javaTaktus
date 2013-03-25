/*
 * 25.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Methoden für alle Taktus-Label
 * 
 * 
 */
package AChord.AClasses;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ALabel extends JLabel
{
	Border border;

	/**
	 * 
	 */
	public ALabel()
	{
		super();
		border = new LineBorder(Color.BLACK, 1);
	}
	public ALabel(String text)
	{
		super(text);
		border = new LineBorder(Color.BLACK, 1);
	}
	
	/**
	 * 
	 */
	public void setMarkierungsrahmen()
	{
		this.border = new LineBorder(Color.BLUE,3);
	}
	
	/**
	 * 
	 */
	public void setMausMarkierungsrahmen()
	{
		this.border = new LineBorder(Color.CYAN,3);
	}
	
	/**
	 * 
	 */
	public void setNormalenRahmen()
	{
		border = new LineBorder(Color.BLACK, 1);
	}
	
	/**
	 * 
	 */
	public void setRahmen()
	{
		this.setBorder(border);
	}
	
	/**
	 * 
	 */
	public void resetRahmen()
	{
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

}
