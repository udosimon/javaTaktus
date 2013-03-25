/*
 * 15.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Methoden für alle Taktus-Dialoge
 * 
 * 
 */
package AChord.AClasses;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import AChord.Hilfe.Konfiguration;

public class ADialog extends JDialog
{
    private final static long serialVersionUID = 1;

    public ADialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		construct();
	}
    public ADialog(Dialog parent, String title, boolean modal)
	{
		super(parent, title, modal);
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
	
	public void activated()
	{
//		System.out.println("-----------activated-------------");
	}
	public void deactivated()
	{
//		System.out.println("-----------deactivated-------------");
	}
	public void iconified()
	{
//		System.out.println("-----------iconified-------------");
	}
	public void deiconified()
	{
//		System.out.println("-----------deiconified-------------");
	}
	public void opened()
	{
//		System.out.println("-----------opended-------------");
	}
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
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.PLAIN,16);
	}

	/**
	 * @return
	 */
	public Font getFontSmall()
	{
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD,10);
	}

}
