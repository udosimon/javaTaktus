/*
 * 25.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Methoden für alle Taktus-Frames
 * 
 * 
 */
package AChord.AClasses;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import AChord.Hilfe.Konfiguration;

public class AFrame extends JFrame
{
	public AFrame()
	{
		super();
		construct();
	}
	
	private void construct()
	{
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
			{
				close();
			}
            public void windowActivated(WindowEvent e)
			{
            	activated();
			}
            public void windowDeactivated(WindowEvent e)
			{
				deactivated();
			}
            public void windowIconified(WindowEvent e)
			{
				iconified();
			}
            public void windowDeiconified(WindowEvent e)
			{
				deiconified();
			}
            public void windowOpened(WindowEvent e)
			{
				opened();
			}
		});
 
	}
	
	public void activated(){}
	public void deactivated(){}
	public void iconified(){}
	public void deiconified(){}
	public void opened(){}
	public void close() 
	{
		this.setVisible(false);
		this.dispose();
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
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD,14);
	}


}
