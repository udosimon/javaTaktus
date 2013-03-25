/*
 * 19.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Längen-beschränkung
 * 
 */
package AChord.AClasses;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.*;

public class AUnsignedValidator extends ALengthLimitedDocument
{
	public AUnsignedValidator()
	{
		super();
	}
	public AUnsignedValidator(int length)
	{
		super(length);
	}
	public int getHorizontalAlignment()
	{
		return JTextField.LEFT;
	}
	public void insertString(int offs, String str, AttributeSet a) 
		throws BadLocationException
	{
		if (str == null)
			return;
		
		boolean ok = true;
		for (int i = 0; i < str.length(); i++)
		{
			if ( (str.charAt(i) < '0') || (str.charAt(i) > '9') )
			{
				ok = false;
				break;
			}
		}

		if (ok)
			super.insertString(offs, str, a);
		else
			Toolkit.getDefaultToolkit().beep();
	}
}


