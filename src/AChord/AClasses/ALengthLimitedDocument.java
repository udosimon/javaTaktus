/*
 * 26.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Längen-beschränkung
 * 
 * 
 */
package AChord.AClasses;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.*;

public class ALengthLimitedDocument extends PlainDocument
{
	int	m_length = 0;
	public ALengthLimitedDocument()
	{
		super();
	}
	public ALengthLimitedDocument(int length)
	{
		super();
		m_length = length;
	}
	public int getMaxLength()
	{
		return m_length;
	}
	public void setMaxLength(int length)
	{
		m_length = length;
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
/*
		char[] upper = str.toCharArray();
		for (int i = 0; i < upper.length; i++)
			upper[i] = Character.toUpperCase(upper[i]);
*/
		if ( (m_length > 0) &&
			 ((str.length() + getLength()) > m_length) )
		{
			String stri = str.substring(0, m_length - getLength());
			super.insertString(offs, stri, a);
			Toolkit.getDefaultToolkit().beep();
		}
		else
			super.insertString(offs, str, a);
	}
}


