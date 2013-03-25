/*
 * 19.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Erweiterung für Taktus-JTextArea
 * 
 */

package AChord.AClasses;

import java.awt.Font;

import javax.swing.JTextArea;

import AChord.Hilfe.Konfiguration;

public class ATextArea extends JTextArea
{
	String stArr[] = new String[100];

	public ATextArea()
	{
		super();
	}

	public ATextArea(int rows, int columns)
	{
		super(rows, columns);
	}

	/**
	 * @return
	 */
	public Font getFontA()
	{
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD,12);
	}
	
	
	/**
	 * @return
	 */
	public Font getFontB()
	{
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.PLAIN,12);
	}
	
	/**
	 * @return
	 */
	public Font getFontC()
	{
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.PLAIN,16);
	}

	/**
	 * 
	 */
	public void initTextChange()
	{
		String st = this.getText();
		
		int i = 0;
		while (st.indexOf(10) > -1)
		{
			stArr[i] = st.substring(0, st.indexOf(10));
			st = st.substring(st.indexOf(10)+1, st.length());
			i++;
		}
		stArr[i] = st;
	}

	/**
	 * @return
	 */
	public String[] getStringArray()
	{
		return stArr;
	}
	
	/**
	 * @return
	 * 
	 * gibt die Zeile aus dem TextArea zurück, in der geändert wurde
	 */
	public String getLineLastChanged()
	{
		String retString = new String("");
		boolean istEnde = false;
		int j = -1;
		for (int i=0; i < this.getLineCount(); i++)
		{
			j += stArr[i].length()+1;
			if ((this.getCaretPosition() <= j) && (!istEnde))
			{
				retString = stArr[i];
				istEnde = true;
			}
		}
		
		return retString;
	}
	
	/**
	 * @return

	 * gibt die ZeileNr aus dem TextArea zurück, in der geändert wurde
	 * erste Zeile ist 0
	 * wenn neue Zeile eingefügt, dann -1
	 */
	public int getLineNrLastChanged()
	{
		int retNr = -1;
		
		int j = -1;
		for (int i=0; i < this.getLineCount(); i++)
		{
			j += stArr[i].length()+1;
//			System.out.println("=="+this.getLineCount()+"="+this.getCaretPosition()+"="+
//					stArr[i].length()+"/"+j);
			if ((this.getCaretPosition() <= j) && (retNr == -1))
				retNr = i;
		}
		
		
		return retNr;
	}
}
