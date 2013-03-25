/*
 * TAKTUS
 *
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 9.1.2008
 * 
 * Beschreibung:
 * 
 * Konfigurations-Dialog
 * 
 * 
 */
package AChord.Hilfe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import AChord.AClasses.ADialog;
import AChord.AClasses.ALabel;
import AChord.AClasses.APanel;
import AChord.AClasses.AUnsignedField;
import AChord.MainFrame.AChordFrame;
import AChord.Schema.Song;
import AChord.Schema.Takt;
import AChord.Schema.TaktSchemaTeil;

public class DialogKonfiguration extends ADialog
{
    private final static long serialVersionUID = 1;

    AChordFrame parent;
	private APanel jPanelRahmen;
	private APanel jPanelH;
	private APanel jPanelNeu;
	private APanel jPanelAbstand;
	private JRadioButton jRadioButtonNurB;
	private JRadioButton jRadioButtonNurH;
	private JRadioButton jRadioButtonBNurWennHalbton;
	private JCheckBox jCheckBoxRahmenDruckEin;
	private JCheckBox jCheckBoxRahmenSongEin;
	private JCheckBox jCheckBoxAkkordNeuKlang;
	private JCheckBox jCheckBoxAkkordNeuArt;
	private AUnsignedField unsignedAbstandVor;
	private AUnsignedField unsignedAbstandZwischen;
	private AUnsignedField unsignedAbstandVorText;
	private ALabel label1;
	private ALabel label2;
	private ALabel label3;
	
	private JButton buttonUebernehmen;
	private JButton buttonAbbrechen;
	private JButton buttonDefault;
	
	Song song;

	boolean istAenderungSong = false;
	
	int artBH; 
	int abstandVor;
	int abstandZwischen;
	int abstandVorText;


	/**
	 * Konstruktor
	 * @param parent
	 * @param title
	 * @param modal
	 */
	public DialogKonfiguration(AChordFrame parent, String title, boolean modal)
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
		this.setSize(490, 350);
		this.setLocation(parent.getX()+250, parent.getY()+200);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		{
			jPanelH = new APanel();
			getContentPane().add(jPanelH);
			jPanelH.setLayout(null);
			jPanelH.setBounds(12, 12, 236, 130);
			jPanelH.setBorder(BorderFactory.createTitledBorder("Darstellung H/B"));
			{
				jRadioButtonNurB = new JRadioButton();
				jPanelH.add(jRadioButtonNurB);
				jRadioButtonNurB.setText("immer B-Anzeige");
				jRadioButtonNurB.setBounds(17, 26, 156, 25);
				jRadioButtonNurB.setFont(getFontA());
				jRadioButtonNurB.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onRadioButtonBAnzeige(Konfiguration.ANZEIGE_B_AKKORD_IMMER_B);
					}
				});
			}
			{
				jRadioButtonNurH = new JRadioButton();
				jPanelH.add(jRadioButtonNurH);
				jRadioButtonNurH.setText("immer H");
				jRadioButtonNurH.setBounds(17, 57, 156, 25);
				jRadioButtonNurH.setFont(getFontA());
				jRadioButtonNurH.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onRadioButtonBAnzeige(Konfiguration.ANZEIGE_B_AKKORD_IMMER_H);
					}
				});
			}
			{
				jRadioButtonBNurWennHalbton = new JRadioButton();
				jPanelH.add(jRadioButtonBNurWennHalbton);
				jRadioButtonBNurWennHalbton.setText("B nur wenn vermindert");
				jRadioButtonBNurWennHalbton.setBounds(17, 88, 156, 25);
				jRadioButtonBNurWennHalbton.setFont(getFontA());
				jRadioButtonBNurWennHalbton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onRadioButtonBAnzeige(Konfiguration.ANZEIGE_B_AKKORD_B_NUR_WENN_HALBTON);
					}
				});
			}
		}
		{
			jPanelRahmen = new APanel();
			jPanelRahmen.setLayout(null);
			getContentPane().add(jPanelRahmen);
			jPanelRahmen.setBounds(255, 12, 216, 97);
			jPanelRahmen.setBorder(BorderFactory.createTitledBorder("Anzeige Rahmen"));
			{
				jCheckBoxRahmenSongEin = new JCheckBox();
				jPanelRahmen.add(jCheckBoxRahmenSongEin);
				jCheckBoxRahmenSongEin.setText("Rahmen im Anzeigebereich ");
				jCheckBoxRahmenSongEin.setBounds(25, 26, 185, 25);
				jCheckBoxRahmenSongEin.setFont(getFontA());
				jCheckBoxRahmenSongEin.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onRahmenSong();
					}
				});
			}
			{
				jCheckBoxRahmenDruckEin = new JCheckBox();
				jPanelRahmen.add(jCheckBoxRahmenDruckEin);
				jCheckBoxRahmenDruckEin.setText("Rahmen im Ausdruck");
				jCheckBoxRahmenDruckEin.setBounds(25, 57, 175, 28);
				jCheckBoxRahmenDruckEin.setFont(getFontA());
				jCheckBoxRahmenDruckEin.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onRahmenDruck();
					}
				});
			}
		}
		{
			jPanelNeu = new APanel();
			jPanelNeu.setLayout(null);
			getContentPane().add(jPanelNeu);
			jPanelNeu.setBounds(255, 110, 216, 100);
			jPanelNeu.setBorder(BorderFactory.createTitledBorder("Anzeige neuer Takt"));
			{
				jCheckBoxAkkordNeuArt = new JCheckBox();
				jPanelNeu.add(jCheckBoxAkkordNeuArt);
				jCheckBoxAkkordNeuArt.setText("neuer Takt mit Tonart");
				jCheckBoxAkkordNeuArt.setBounds(25, 26, 160, 25);
				jCheckBoxAkkordNeuArt.setFont(getFontA());
				jCheckBoxAkkordNeuArt.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onTaktNeu();
					}
				});
			}
			{
				jCheckBoxAkkordNeuKlang = new JCheckBox();
				jPanelNeu.add(jCheckBoxAkkordNeuKlang);
				jCheckBoxAkkordNeuKlang.setText("neuer Takt mit Tonklang");
				jCheckBoxAkkordNeuKlang.setBounds(25, 57, 185, 25);
				jCheckBoxAkkordNeuKlang.setFont(getFontA());
				jCheckBoxAkkordNeuKlang.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						onTaktNeu();
					}
				});
			}
		}
		jPanelAbstand = new APanel();
		getContentPane().add(jPanelAbstand);
		jPanelAbstand.setLayout(null);
		jPanelAbstand.setBounds(12, 145, 236, 123);
		jPanelAbstand.setBorder(BorderFactory.createTitledBorder("Abstände"));
		{
			label1 = new ALabel();
			jPanelAbstand.add(label1);
			label1.setText("Abstand vor erstem Teil:");
			label1.setBounds(17, 26, 170, 25);
			label1.setFont(getFontA());

			label2 = new ALabel();
			jPanelAbstand.add(label2);
			label2.setText("Abstand zwischen den Teilen:");
			label2.setBounds(17, 57, 170, 25);
			label2.setFont(getFontA());
			
			label3 = new ALabel();
			jPanelAbstand.add(label3);
			label3.setText("Abstand vor Text:");
			label3.setBounds(17, 88, 170, 25);
			label3.setFont(getFontA());
			
			unsignedAbstandVor = new AUnsignedField(3);
			jPanelAbstand.add(unsignedAbstandVor);
			unsignedAbstandVor.setBounds(195, 26, 28, 25);
			unsignedAbstandVor.setFont(getFontA());
			unsignedAbstandVor.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e) 
				{
					onAbstandVor(unsignedAbstandVor.getLong());
				}
			});
			
			unsignedAbstandZwischen = new AUnsignedField(3);
			jPanelAbstand.add(unsignedAbstandZwischen);
			unsignedAbstandZwischen.setBounds(195, 57, 28, 25);
			unsignedAbstandZwischen.setFont(getFontA());
			unsignedAbstandZwischen.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e) 
				{
					onAbstandZwischen(unsignedAbstandZwischen.getLong());
				}
			});

			unsignedAbstandVorText = new AUnsignedField(3);
			jPanelAbstand.add(unsignedAbstandVorText);
			unsignedAbstandVorText.setBounds(195, 88, 28, 25);
			unsignedAbstandVorText.setFont(getFontA());
			unsignedAbstandVorText.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e) 
				{
					onAbstandVorText(unsignedAbstandVorText.getLong());
				}
			});
		}
		getContentPane().add(getJButtonAbbrechen());
		getContentPane().add(getJButtonUebernehmen());
		getContentPane().add(getJButtonDefault());
	}
	
	
	
	/**
	 * @return
	 */
	public JButton getJButtonUebernehmen()
	{
		if (buttonUebernehmen == null)
		{
			buttonUebernehmen = new JButton();
			buttonUebernehmen.setText("Übernehmen");
			buttonUebernehmen.setFont(getFontA());
			buttonUebernehmen.setBounds(160, 280, 140, 30);
			buttonUebernehmen.setMnemonic('n');
			buttonUebernehmen.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonUebernehmen();}});
		}
		return buttonUebernehmen;
	}

	/**
	 * @return
	 */
	public JButton getJButtonAbbrechen()
	{
		if (buttonAbbrechen == null)
		{
			buttonAbbrechen = new JButton();
			buttonAbbrechen.setText("Abbrechen");
			buttonAbbrechen.setFont(getFontA());
			buttonAbbrechen.setBounds(310, 280, 140, 30);
			buttonAbbrechen.setMnemonic('A');
			buttonAbbrechen.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonAbbrechen();}});
		}
		return buttonAbbrechen;
	}

	/**
	 * @return
	 */
	public JButton getJButtonDefault()
	{
		if (buttonDefault == null)
		{
			buttonDefault = new JButton();
			buttonDefault.setText("Standard-Werte");
			buttonDefault.setFont(getFontA());
			buttonDefault.setBounds(270, 225, 180, 30);
			buttonDefault.setMnemonic('S');
			buttonDefault.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonDefault();}});
		}
		return buttonDefault;
	}

	/**
	 * schreiben der Konfiguration und beenden des Dialoges
	 */
	private void onButtonUebernehmen()
	{
		Konfiguration.getInstance().setIstAnzeigeBimAkkord(artBH);
		Konfiguration.getInstance().setShowRahmenImSong(jCheckBoxRahmenSongEin.isSelected());
		Konfiguration.getInstance().setShowRahmenImDruck(jCheckBoxRahmenDruckEin.isSelected());
		Konfiguration.getInstance().setAbstandVorErstemTeil(abstandVor);
		Konfiguration.getInstance().setAbstandZwischenTeilen(abstandZwischen);
		Konfiguration.getInstance().setAbstandVorText(abstandVorText);
		Konfiguration.getInstance().setNeuerTakt(jCheckBoxAkkordNeuArt.isSelected(), 
				jCheckBoxAkkordNeuKlang.isSelected());

		Konfiguration.getInstance().schreibeIniDatei();

		if ((istAenderungSong) && (song != null))
		{
			// übernehmen der neuen Einstellung in die aktuelle Anzeige
			song.getSchema().getAkkTonArtSong().vorbereiteAkkord();

	    	for (int i=0; i<song.getSchema().getTaktSchemaTeile().size(); i++)
	    	{
	    		TaktSchemaTeil teil = (TaktSchemaTeil)song.getSchema().getTaktSchemaTeile().elementAt(i);
	    		for (int j=0; j<teil.getAlleTakte().size(); j++)
	    		{
	    			Takt takt = (Takt)teil.getAlleTakte().elementAt(j);
	    			if (takt.getAkkord1() != null)
	    				takt.getAkkord1().vorbereiteAkkord();
	    			if (takt.getAkkord2() != null)
	    				takt.getAkkord2().vorbereiteAkkord();
	    			if (takt.getAkkord3() != null)
	    				takt.getAkkord3().vorbereiteAkkord();
	    			if (takt.getAkkord4() != null)
	    				takt.getAkkord4().vorbereiteAkkord();
	    		}
	    	}
	    	song.resetRahmenAlleSchemas();
	    	song.setRahmenAlleSchemas();
			song.konstruierBlatt(true);
			parent.getJPanelTakte().setTaktDaten(song.setMarkierungErsterTakt(), false);
			parent.repaint();
		}

		onButtonAbbrechen();
	}
	
	/**
	 * beenden des Dialoges
	 */
	private void onButtonAbbrechen()
	{
		dispose();
		setVisible(false);
	}
	
	/**
	 * setzen der Standard-Werte vom Bediener
	 */
	private void onButtonDefault()
	{
		Konfiguration.getInstance().setDefaultWerte();
		setDaten();
	}

	/**
	 * Reaktion auf einen der RadioButton im Panel "Darstellung H/B"
	 * @param art
	 */
	private void onRadioButtonBAnzeige(int art)
	{
		artBH = art;
		istAenderungSong = true;
	}
	
	/**
	 * Reaktion auf Checkbox "Rahmen im Anzeigebereich"
	 */
	private void onRahmenSong()
	{
		istAenderungSong = true;
	}

	/**
	 * Reaktion auf Checkbox "Rahmen im Ausdruck"
	 */
	private void onRahmenDruck()
	{
		istAenderungSong = true;
	}
	
	/**
	 * Reaktion auf Änderung im Textfeld für den "Abstand vor erstem Teil:"
	 */
	private void onAbstandVor(Long vor)
	{
		istAenderungSong = true;
		if (vor == null)
			abstandVor = 0;
		else
			abstandVor = vor.intValue();
	}

	/**
	 * Reaktion auf Änderung im Textfeld für den "Abstand zwischen den Teilen:"
	 */
	private void onAbstandZwischen(Long zwischen)
	{
		istAenderungSong = true;
		if (zwischen == null)
			abstandZwischen = 0;
		else
			abstandZwischen = zwischen.intValue();
	}
	/**
	 * Reaktion auf Änderung im Textfeld für den "Abstand vor Text:"
	 */
	private void onAbstandVorText(Long vor)
	{
		istAenderungSong = true;
		if (vor == null)
			abstandVorText = 0;
		else
			abstandVorText = vor.intValue();
	}
	
	/**
	 * Reaktion auf Checkboxen "neuer Takt mit Tonart" und "neuer Takt mit Tonklang"
	 */
	private void onTaktNeu()
	{
		// keine direkte Anzeige im Song beim Verlassen des Dialoges, darum
		// passiert hier nix
		
	}

	/**
	 * setzen der Daten von aussen
	 */
	public void setDaten(Song song)
	{
		istAenderungSong = false;
		this.song = song;
		setDaten();
	}

	/**
	 * setzen der Daten
	 */
	private void setDaten()
	{
		jRadioButtonNurB.setSelected(Konfiguration.getInstance().istNurBAkkord());
		jRadioButtonNurH.setSelected(Konfiguration.getInstance().istNurHAkkord());
		jRadioButtonBNurWennHalbton.setSelected(Konfiguration.getInstance().istBAkkordNurWennHalb());
		artBH = Konfiguration.getInstance().getAnzeigeBimAkkord();
		
		jCheckBoxRahmenSongEin.setSelected(Konfiguration.getInstance().isShowRahmenImSong());
		jCheckBoxRahmenDruckEin.setSelected(Konfiguration.getInstance().isShowRahmenImDruck());
		
		jCheckBoxAkkordNeuArt.setSelected(Konfiguration.getInstance().istNeuerTaktMitArt());
		jCheckBoxAkkordNeuKlang.setSelected(Konfiguration.getInstance().istNeuerTaktMitKlang());
		
		unsignedAbstandVor.setText(""+Konfiguration.getInstance().getAbstandVorErstemTeil());
		unsignedAbstandZwischen.setText(""+Konfiguration.getInstance().getAbstandZwischenTeilen());
		unsignedAbstandVorText.setText(""+Konfiguration.getInstance().getAbstandVorText());
		abstandVor = Konfiguration.getInstance().getAbstandVorErstemTeil();
		abstandZwischen = Konfiguration.getInstance().getAbstandZwischenTeilen();
		abstandVorText = Konfiguration.getInstance().getAbstandVorText();
	}

}
