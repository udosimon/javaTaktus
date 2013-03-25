/*
 * TAKTUS
 *
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 15.11.2007 
 *
 * Beschreibung:
 * 
 * Info-Dialog
 * 
 * enthält copyright und Versionsnummer
 * 
 * 
 */
package AChord.Hilfe;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import AChord.AClasses.ADialog;
import AChord.AClasses.AIconManager;
import AChord.AClasses.ALabel;
import AChord.AClasses.APanel;



public class DialogInfo extends ADialog
{
    private final static long serialVersionUID = 1;

    //**************************************************
    public final static String VERSIONS_NUMMER = "1.8";
    //**************************************************
	
	public final static String VERSIONS_TEXT = 
					"<html><center><b>T</b>itel-Erstellung<p>"+
					"<b>Ak</b>korde und <b>T</b>exte<p>"+
					"Autor: <b>U</b>do <b>S</b>imon<p><p>"+
					"Version: " + VERSIONS_NUMMER +""+
					"<HR><font size=-1>Wenn ihr Fragen und Anregungen habt, dann<br>" +
					"sendet diese bitte an taktus@boksee.de<p><p>"+
					"copyrihgt © 2008 by Udo Simon<p><p>" + 
					"<font size=-2>Taktus ist Freeware, und kann ohne Einschränkungen benutzt und weiter verteilt werden.<br>" +
					"Verwendung auf eigene Gefahr. Für entstehende Schäden und/oder Datenverlust ist der Autor dieser Software nicht haftbar zu machen.<p>";

	Frame parent;
	private APanel jPanelLogo;
	private ALabel labelVersionsText;
	private JButton buttonEnde;
	
	/**
	 * Konstruktor
	 * @param parent
	 * @param title
	 * @param modal
	 */
	public DialogInfo(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		this.parent = parent;
		construct();
	}
	
	/**
	 * Initialisierungen des Frames
	 */
	private void construct()
	{
		try
		{
			initForm();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Aufbau der Oberfläche
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		this.setSize(270,500);
		this.setLocation(parent.getX()+400, parent.getY()+150);
		this.getContentPane().setLayout(null);
		setFont(getFontA());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.getContentPane().add(getJPanelLogo());
		this.getContentPane().add(getJLabelVersionsText());
		this.getContentPane().add(getJButtonEnde());
	}
	/**
	 * @return
	 */
	private APanel getJPanelLogo()
	{
		if (jPanelLogo == null)
		{
			jPanelLogo = new APanel();
			jPanelLogo.setBounds(58, 20, 150, 130);
			jPanelLogo.setLayout(null);
			jPanelLogo.setBorder(new LineBorder(Color.black));
			jPanelLogo.setImage(AIconManager.ICON_TAKT_LOGO);
		}
		return jPanelLogo;
	}
	
	private ALabel getJLabelVersionsText() {
		if (labelVersionsText == null) {
			labelVersionsText = new ALabel(VERSIONS_TEXT);
			labelVersionsText.setFont(getFontC());
			labelVersionsText.setBounds(0, 160, 265, 260);
			labelVersionsText.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return labelVersionsText;
	}
	
	/**
	 * @return
	 */
	public JButton getJButtonEnde()
	{
		if (buttonEnde == null)
		{
			buttonEnde = new JButton();
			buttonEnde.setText("Ok");
			buttonEnde.setFont(getFontA());
			buttonEnde.setBounds(92, 435, 80, 30);
			buttonEnde.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonEnde();}});
		}
		return buttonEnde;
	}
	
	/**
	 * 
	 */
	private void onButtonEnde()
	{
		dispose();
		setVisible(false);
	}

}
