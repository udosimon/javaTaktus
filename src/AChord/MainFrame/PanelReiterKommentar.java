/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 27.11.2007 
 * 
 * Beschreibung:
 * 
 * Der Panel, der den Kommentar darstellt und bearbeitet
 */
package AChord.MainFrame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import AChord.AClasses.APanel;
import AChord.Schema.Kommentar;
import AChord.Schema.TextZeile;

public class PanelReiterKommentar extends APanel
{
    private final static long serialVersionUID = 1;

    private static final int MAX_KOMM_ZEICHEN = 60;
	Kommentar kommentar = new Kommentar();;
	boolean istLadePhase;
	APanel parent;

	private JLabel jLabelName;
	private JCheckBox jCheckBoxAnzeige;
	private JTextField jTextFieldKomm;
	
	boolean istNachAkkorden = true;

	/**
	 * Konstruktor
	 * @param parent
	 */
	public PanelReiterKommentar(APanel parent)
	{
		super();
		this.parent = parent;
		if (parent instanceof PanelReiter2Schema)
			istNachAkkorden = true;
		else
			istNachAkkorden = false;
		construct();
	}

	/**
	 * Konstruktor
	 * @param parent
	 * @param kommentar
	 */
	public PanelReiterKommentar(APanel parent, Kommentar kommentar)
	{
		super();
		this.kommentar = kommentar;
		this.parent = parent;
		if (parent instanceof PanelReiter2Schema)
			istNachAkkorden = true;
		else
			istNachAkkorden = false;
		construct();
		myInit();
	}

	/**
	 * Initialisierungen des Frames
	 */
	private void construct()
	{
		try
		{
			istLadePhase = true;
			initForm();
			istLadePhase = false;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			istLadePhase = false;
		}
	}

	/**
	 * Aufbau der Oberfl‰che
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		this.setLayout(null);
		this.setSize(470, 90);
		this.setLocation(10, parent.getHeight() - this.getHeight() - 5);
		this.setBorder(new LineBorder(Color.BLACK));

		this.add(getJLabelName());
		this.add(getJCheckBoxAnzeige());
		this.add(getJTextFieldKomm());
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelName()
	{
		if (jLabelName == null)
		{
			jLabelName = new JLabel();
			if (istNachAkkorden)
				jLabelName.setText("Kommentar unterhalb des Akkordschemas:");
			else
				jLabelName.setText("Kommentar unterhalb des Textes:");

			jLabelName.setFont(getFontA());
			jLabelName.setBounds(6, 6, 250, 14);
		}
		return jLabelName;
	}
	
	/**
	 * Soll Kommentar im Song angezeigt werden
	 * @return
	 */
	private JCheckBox getJCheckBoxAnzeige()
	{
		if (jCheckBoxAnzeige == null)
		{
			jCheckBoxAnzeige = new JCheckBox();
			jCheckBoxAnzeige.setBounds(17, 62, 180, 15);
			jCheckBoxAnzeige.setText("Anzeige des Kommentares");
			jCheckBoxAnzeige.setFont(getFontA());
			jCheckBoxAnzeige.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onCheckBoxAnzeige();}});
		}
		return jCheckBoxAnzeige;
	}

	/**
	 * @return
	 */
	private JTextField getJTextFieldKomm()
	{
		if (jTextFieldKomm == null)
		{
			jTextFieldKomm = new JTextField();
			jTextFieldKomm.setBounds(20, 30, 400, 25);
			jTextFieldKomm.setFont(getFontA());
			PlainDocument docTitel = new PlainDocument();
			jTextFieldKomm.setDocument(docTitel);
			docTitel.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { handleTextChange(); }
				public void removeUpdate(DocumentEvent e) { handleTextChange(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});
		}
		return jTextFieldKomm;
	}

	/**
	 *  weitere Initialisierungen aus Konstruktor
	 */
	private void myInit()
	{
		getJCheckBoxAnzeige().setSelected(kommentar.istTextAnzeige());
		if (kommentar.getTextZeilen().size() > 0)
		{
			TextZeile zeile = (TextZeile)kommentar.getTextZeilen().elementAt(0);
			getJTextFieldKomm().setText(zeile.getText());
		}
	}
	
	/**
	 * Anpassung im Song, wenn der Text angezeigt bzw. nicht angezeigt werden soll.
	 */
	private void onCheckBoxAnzeige()
	{
		setzeKommDaten();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				if (istNachAkkorden)
					((PanelReiter2Schema)parent).resetSong();
				else
				{
					((PanelReiter4Text)parent).resetSong();
				}
			}
		});
	}
	
	/**
	 * Anpassungen im Song, wenn der Text ver‰ndert wird
	 */
	public void handleTextChange()
	{
		if (istLadePhase)
			return;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				istLadePhase = true;
				if (getJTextFieldKomm().getText().length() > MAX_KOMM_ZEICHEN)
				{
					String st = getJTextFieldKomm().getText().substring(0, MAX_KOMM_ZEICHEN);
					getJTextFieldKomm().setText(st);
				}
				setzeKommDaten();
				
				if (istNachAkkorden)
					((PanelReiter2Schema)parent).resetSong();
				else
				{
					((PanelReiter4Text)parent).resetSong();
				}
				istLadePhase = false;
			}
		});

	}
	
	/**
	 * 
	 */
	private void setzeKommDaten()
	{
		kommentar.removeAndClearZeilen();
		TextZeile zeile = new TextZeile();
		zeile.setText(getJTextFieldKomm().getText());
		kommentar.addZeile(zeile);

		if (getJCheckBoxAnzeige().isSelected())
			kommentar.setTextAnzeige(Kommentar.KOMM_ANZEIGE_MIT);
		else
			kommentar.setTextAnzeige(Kommentar.KOMM_ANZEIGE_OHNE);
	}
	
	/**
	 * Aufruf von auﬂen, wenn ein Song neu geladen wird, damit
	 * die entsprechenden Attribute hier im Panel korrekt 
	 * dargestellt werden.
	 * @param komm
	 */
	public void setDaten(Kommentar komm)
	{
		kommentar.removeAndClearZeilen();
		kommentar.setTextZeilen(komm.getTextZeilen());
		kommentar.setTextAnzeige(komm.getTextAnzeige());
		getJTextFieldKomm().setText("");
		getJCheckBoxAnzeige().setSelected(false);
		
		myInit();
	}
}
