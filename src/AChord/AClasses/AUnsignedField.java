/*
 * 19.10.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Erweiterung für ein Usigned-Field
 * 
 */
package AChord.AClasses;

import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AUnsignedField extends JTextField implements DocumentListener
{
	HashSet changeListeners = new HashSet();
	public AUnsignedField()
	{
		super();
		AUnsignedValidator doc = new AUnsignedValidator(9);
		super.setDocument(doc);
		setHorizontalAlignment(doc.getHorizontalAlignment());
	}
	public AUnsignedField(int anzahlZiffern)
	{
		super();
		AUnsignedValidator doc = new AUnsignedValidator(anzahlZiffern);
		super.setDocument(doc);
		setHorizontalAlignment(doc.getHorizontalAlignment());
		getDocument().addDocumentListener(this);

	}
	public AUnsignedField(AUnsignedValidator doc)
	{
		super();
		super.setDocument(doc);
		setHorizontalAlignment(doc.getHorizontalAlignment());
	}
	public boolean isDefined()
	{
		return getText().length() > 0;
	}
	public Long getLong()
	{
		if (isDefined())
			return new Long(getText());
		else
			return new Long(0);
	}
	public void setValue(Long value)
	{
		setText(value.toString());
	}
	public Short getShort()
	{
		if (isDefined())
			return new Short(getText());
		else
			return new Short("0");
	}
	public void setValue(Short value)
	{
		setText(value.toString());
	}
	public Byte getByte()
	{
		if (isDefined())
			return new Byte(getText());
		else
			return new Byte("0");
	}
	public void setValue(Byte value)
	{
		setText(value.toString());
	}
	public void changedUpdate(DocumentEvent e)
	{
		fireChangeEvent(new ChangeEvent(this));
	}
	public void insertUpdate(DocumentEvent e)
	{
		fireChangeEvent(new ChangeEvent(this));
	}
	public void removeUpdate(DocumentEvent e)
	{
		fireChangeEvent(new ChangeEvent(this));
	}

	public void addChangeListener(ChangeListener listener)
	{
		changeListeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener)
	{
		changeListeners.remove(listener);
	}
	
	protected void fireChangeEvent(ChangeEvent event)
	{
		for (Iterator i = changeListeners.iterator(); i.hasNext();)
		{
			ChangeListener listener = (ChangeListener) i.next();
			listener.stateChanged(event);
		}
	}	

}

