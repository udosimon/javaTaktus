/*
 * TAKTUS
 *
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 28.09.2007 
 * 
 * Beschreibung:
 * 
 * HauptFrame
 * 
 * Aufruf von außen (z.B. aus main):
 * 
 * 		AChordFrame f = new AChordFrame();
 *		f.setVisible(true);
 *		f.initSong();
 *
 * Darstellen des kompletten Taktus-Frames
 *
 */
package AChord.MainFrame;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import AChord.AClasses.AFrame;
import AChord.AClasses.AIconManager;
import AChord.AClasses.APanel;
import AChord.Hilfe.DialogHilfe;
import AChord.Hilfe.DialogInfo;
import AChord.Hilfe.DialogKonfiguration;
import AChord.Hilfe.Konfiguration;
import AChord.Print.PrintUtility;
import AChord.Schema.Song;
import AChord.Schema.SongText;
import AChord.Schema.Takt;
import AChord.Tools.DialogNotenPultAnsicht;
import AChord.Tools.DialogTansponieren;
import AChord.xml.AXmlSongReadBroker;
import AChord.xml.AXmlSongWriteBroker;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;



public class AChordFrame extends AFrame 
{
    private final static long serialVersionUID = 1;

    private static final String TAKTUS_TITLE = "Taktus";
    public static final String TAKTUS_EXTENSION = ".Taktus";
    
    private static final int BREITE_SONG = 490;

	private PanelReiter2Schema jPanelTeile;
	private PanelReiter4Text jPanelText;
	private PanelReiter3Takt jPanelTakte;
	private PanelReiter1Titel jPanelTitel;
	private JPanel jPanelSong;
	private JPanel jPanelSongText;
	JPanel tempDruckPanel;

	private JMenu jMenuBearbeiten = null;
	private JMenu jMenuDatei = null;
	private JMenu jMenuHilfe = null;
	private JMenuItem jMItemNeu = null;
	private JMenuItem jMItemLaden = null;
	private JMenuItem jMItemSpeichern = null;
	private JMenuItem jMItemSpeichernUnter = null;
	private JMenuItem jMItemAuswahlDrucken = null;
	private JMenuItem jMItemDrucken = null;
	private JMenuItem jMItemBeenden = null;
	private JMenuItem jMItemKonfiguration = null;
	private JMenuItem jMItemTransponieren = null;
	private JMenuItem jMItemHilfe = null;
	private JMenuItem jMItemInfo = null;
	private JMenuBar jMenuBar1;
	
	private JTabbedPane jTabbedPane1Bearbeitung;
	private JTabbedPane jTabbedPaneAnzeige;
	
	private Song song = null;
	
	private JToolBar jToolBarMain;
	private JButton jButtonNeu;
	private JButton jButtonOpen;
	private JButton jButtonSave;
	private JButton jButtonPrint;
	private JButton jButtonTransponieren = null;
	private JButton jButtonInfo;
	private JButton jButtonHilfe;
	private APanel jPanelLogo;
	
	private JButton jButtonNotenPultAnsicht;
	
	private String currentDateiName = "";

	boolean istLadePhase = false;
	
	boolean istTestNotenPultAnsicht = true;
	
	/**
	 * Konstruktor
	 */
	public AChordFrame()
	{
		super();
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
			myInit();
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	/**
	 * @throws Exception
	 * 
	 * Aufbau der Oberfläche
	 */
	private void initForm() throws Exception
	{
		this.setLocation(20,20);
		this.setSize(1024,768);
		this.getContentPane().setLayout(null);
		this.setResizable(false);

		Konfiguration.getInstance().setzeBetriebssystemValues();

		this.getContentPane().add(this.getJPanelLogo());
		this.setTitle(TAKTUS_TITLE + "    Initialisierung...");
		this.setVisible(true);
		
		{
			jMenuBar1 = new JMenuBar();
			setJMenuBar(jMenuBar1);
			jMenuBar1.add(getJMenuDatei());
			jMenuBar1.add(getJMenuBearbeiten());
			jMenuBar1.add(getJMenuHilfe());
		}

		this.getContentPane().add(this.getJToolBarMain());
		this.getContentPane().add(this.getJTabbedPaneBearbeitung());
		this.getContentPane().add(this.getJTabbedPaneAnzeige());
	}
	
	/**
	 * setzen initialer Werte der Oberfläche
	 */
	private void myInit()
	{
		jMItemBeenden.setEnabled(true);
		jMItemDrucken.setEnabled(false);
		jMItemSpeichern.setEnabled(false);
		jMItemSpeichernUnter.setEnabled(false);
		jMItemTransponieren.setEnabled(false);
		jButtonSave.setEnabled(false);
		jButtonPrint.setEnabled(false);
		jButtonTransponieren.setEnabled(false);

		this.getJTabbedPaneBearbeitung().setEnabled(false);
		this.getJPanelTitel().setStartPhase(true);

		getJTabbedPaneAnzeige().setEnabledAt(1, false);
		getJTabbedPaneAnzeige().setTitleAt(0, "Seite 1");

		this.setTitle(TAKTUS_TITLE);
		this.setIconImage(AIconManager.ICON_TAKT);
		
	}
	

	
	/**
	 *  initialer Aufruf beim Start des Programs (nach Konstruktor)
	 */
	public void initSong()
	{
    	istLadePhase = true;
    	setNachStart();
    	setSongNeu();
	}

	/**
	 * @return
	 * Bearbeitung-Pane, welches
	 * die 4 Tabs Titel, Teile, Takte und Text enthält
	 * 
	 */
	private JTabbedPane getJTabbedPaneBearbeitung() 
	{
		if (jTabbedPane1Bearbeitung == null) {
			jTabbedPane1Bearbeitung = new JTabbedPane();
			jTabbedPane1Bearbeitung.setBounds(5, 30, 492, 682);
			jTabbedPane1Bearbeitung.setFont(getFontC());
			jTabbedPane1Bearbeitung.addTab("Titel", null, getJPanelTitel(), null);
			jTabbedPane1Bearbeitung.addTab("Teile", null, getJPanelTeile(), null);
			jTabbedPane1Bearbeitung.addTab("Takte", null, getJPanelTakte(), null);
			jTabbedPane1Bearbeitung.addTab("Text", null, getJPanelText(), null);
			jTabbedPane1Bearbeitung.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					onJTabbedPaneBearbeitungChange();}});
		}
		return jTabbedPane1Bearbeitung;
	}

	/**
	 * Event, welches ausgelöst wird, wenn einer der 
	 * 4 Bearbeitungs-Tabs gedrückt wird
	 */
	private void onJTabbedPaneBearbeitungChange()
	{
		if (istLadePhase)
			return;
		
		if (getJTabbedPaneBearbeitung().getSelectedIndex() == 2)
		{
			getJPanelTakte().setFocus();
			getJTabbedPaneAnzeige().setSelectedIndex(0);
		}
		else if (getJTabbedPaneBearbeitung().getSelectedIndex() == 0)
		{
			getJPanelTitel().getJRadioButtonSchemaUnsichtbar().setSelected(true);
			getJPanelTitel().resetDirektNachNeuLaden();
			getJTabbedPaneAnzeige().setSelectedIndex(0);
		}
		else if (getJTabbedPaneBearbeitung().getSelectedIndex() == 3)
		{
			if (getJTabbedPaneAnzeige().isEnabledAt(1))
				getJTabbedPaneAnzeige().setSelectedIndex(1);
		}
		else if (getJTabbedPaneBearbeitung().getSelectedIndex() == 1)
		{
			getJTabbedPaneAnzeige().setSelectedIndex(0);
		}
	}

	/**
	 * @return
	 * Anzeige-Pane, welches die beiden Tabs "Seite 1" und "Seite 2" enthält
	 */
	public JTabbedPane getJTabbedPaneAnzeige() 
	{
		if (jTabbedPaneAnzeige == null) {
			jTabbedPaneAnzeige = new JTabbedPane();
			jTabbedPaneAnzeige.setBounds(510, 0, 510, 725);
			jTabbedPaneAnzeige.setFont(getFontB());
			jTabbedPaneAnzeige.addTab("Seite 1", null, getJPanelSong(), null);
			jTabbedPaneAnzeige.addTab("Seite 2", null, getJPanelSongText(), null);
			jTabbedPaneAnzeige.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					onJTabbedPaneAnzeigeChange();}});
		}
		return jTabbedPaneAnzeige;
	}

	/**
	 * Event, welches ausgelöst wird, wenn einer der 
	 * beiden Anzeige-Tabs gedrückt wird 
	 */
	private void onJTabbedPaneAnzeigeChange()
	{
		if (getJTabbedPaneAnzeige().getSelectedIndex() == 1)
		{
			getJPanelText().handleTextChange();
		}
	}
	
	/**
	 * @return
	 * hier wird das Akkord-Schema (und ev. Text) angezeigt
	 */
	public JPanel getJPanelSong()
	{
		if (jPanelSong == null)
		{
			jPanelSong = new JPanel();
			jPanelSong.setBounds(1, 1, 508, 706);
//			jPanelSong.setBounds(510, 7, 508, 706);
			jPanelSong.setLayout(null);
		}
		return jPanelSong;
	}
	/**
	 * @return
	 * hier wird, wenn so eingestellt, der Song-Text angezeigt
	 */
	public JPanel getJPanelSongText()
	{
		if (jPanelSongText == null)
		{
			jPanelSongText = new JPanel();
			jPanelSongText.setBounds(1, 1, 508, 706);
//			jPanelSong.setBounds(510, 7, 508, 706);
			jPanelSongText.setLayout(null);
		}
		return jPanelSongText;
	}

	/**
	 * @return
	 * hier werden die Titel-Parameter eingestellt
	 */
	public PanelReiter1Titel getJPanelTitel() {
		if (jPanelTitel == null) {
			jPanelTitel = new PanelReiter1Titel(this);
		}
		return jPanelTitel;
	}
	
	/**
	 * @return
	 * hier werden die Teil-Parameter eingestellt
	 */
	public PanelReiter2Schema getJPanelTeile() {
		if (jPanelTeile == null) {
			jPanelTeile = new PanelReiter2Schema();
		}
		return jPanelTeile;
	}

	/**
	 * @return
	 * hier wird der Takt eingegeben
	 */
	public PanelReiter3Takt getJPanelTakte() {
		if (jPanelTakte == null) {
			jPanelTakte = new PanelReiter3Takt();
			jPanelTakte.setLayout(null);
		}
		return jPanelTakte;
	}
	

	/**
	 * @return
	 * hier wird der Text eingegeben
	 */
	private PanelReiter4Text getJPanelText() {
		if (jPanelText == null) {
			jPanelText = new PanelReiter4Text();
			jPanelText.setLayout(null);
		}
		return jPanelText;
	}

	
	/**
	 * @return
	 * Hauptmenü "Datei"
	 */
	private JMenu getJMenuDatei() {
		if (jMenuDatei == null) {
			jMenuDatei = new JMenu();
			jMenuDatei.setText("Datei");
			jMenuDatei.setMnemonic('D');

			jMItemNeu = new JMenuItem("Neu");
			jMItemNeu.setMnemonic('n');
			jMItemLaden = new JMenuItem("Titel laden");
			jMItemLaden.setMnemonic('l');
			jMItemSpeichern = new JMenuItem("Titel speichern");
			jMItemSpeichern.setMnemonic('s');
			jMItemSpeichernUnter = new JMenuItem("Titel speichern unter");
			jMItemSpeichernUnter.setMnemonic('u');
			jMItemDrucken = new JMenuItem("Titel drucken");
			jMItemDrucken.setMnemonic('d');
			jMItemAuswahlDrucken = new JMenuItem("mehrere Titel drucken");
			jMItemAuswahlDrucken.setMnemonic('m');
			jMItemBeenden = new JMenuItem("Beenden");
			jMItemBeenden.setMnemonic('B');
			jMenuDatei.add(jMItemNeu);
			jMenuDatei.add(jMItemLaden);
			jMenuDatei.add(jMItemSpeichern);
			jMenuDatei.add(jMItemSpeichernUnter);
			jMenuDatei.addSeparator();
			jMenuDatei.add(jMItemDrucken);
			jMenuDatei.add(jMItemAuswahlDrucken);
			jMenuDatei.addSeparator();
			jMenuDatei.add(jMItemBeenden);
			
			jMItemNeu.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onNeu();}	});
			jMItemLaden.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onLaden();}	});
			jMItemSpeichern.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onSpeichern(false);}	});
			jMItemSpeichernUnter.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onSpeichern(true);}	});
			jMItemDrucken.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onDrucken();}	});
			jMItemAuswahlDrucken.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onAuswahlDrucken();}	});
			jMItemBeenden.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onBeenden();}	});
		}
		return jMenuDatei;
	}
	
	/**
	 * @return
	 * Hauptmenü "Bearbeiten"
	 */
	private JMenu getJMenuBearbeiten() {
		if (jMenuBearbeiten == null) {
			jMenuBearbeiten = new JMenu();
			jMenuBearbeiten.setText("Bearbeiten");
			jMenuBearbeiten.setMnemonic('B');
			
			jMItemKonfiguration = new JMenuItem("Konfiguration");
			jMItemKonfiguration.setMnemonic('K');
			jMItemKonfiguration.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onKonfigurieren();}	});
			jMItemTransponieren = new JMenuItem("Transponieren");
			jMItemTransponieren.setMnemonic('T');
			jMenuBearbeiten.add(jMItemTransponieren);
			jMenuBearbeiten.addSeparator();
			jMenuBearbeiten.add(jMItemKonfiguration);
			jMItemTransponieren.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onTransponieren();}	});
		}
		return jMenuBearbeiten;
	}

	/**
	 * @return
	 * Hauptmenü "Hilfe"
	 */
	private JMenu getJMenuHilfe() {
		if (jMenuHilfe == null) {
			jMenuHilfe = new JMenu();
			jMenuHilfe.setText("Hilfe");
			jMenuHilfe.setMnemonic('H');
			
			jMItemHilfe = new JMenuItem("Hilfe");
			jMItemHilfe.setMnemonic('H');
			jMItemHilfe.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onHilfe();}	});
//			jMItemHilfe.setAccelerator(KeyStroke.getKeyStroke(112, 2));

			jMItemInfo = new JMenuItem("Info");
			jMItemInfo.setMnemonic('I');
			jMItemInfo.addActionListener(new ActionListener()
			{public void actionPerformed(ActionEvent e)	{onInfo();}	});

			jMenuHilfe.add(jMItemHilfe);
			jMenuHilfe.add(jMItemInfo);

		}
		return jMenuHilfe;
	}

	
    /**
     * @param takt
     * Aufruf von einem anderen Modul, zum Setzen eines bestimmten Taktes
     */
    public void setAkkordVonAussen(final Takt takt)
    {
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				getJTabbedPaneBearbeitung().setSelectedIndex(2);
				getJPanelTakte().setTaktDaten(takt, false);
			}
		});
    }
    /**
     * Aufruf von einem anderen Modul, zum Setzen und Markieren
     * des ersten Taktes eines Teiles
     */
    public void setAkkordVonAussen()
    {
    	final Takt takt = song.setMarkierungErsterTakt();
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				getJPanelTakte().setTaktDaten(takt, false);
			}
		});
    }
    
	/* (non-Javadoc)
	 * @see AChord.AClasses.AFrame#close()
	 * überschreiben der close-Methode
	 */
	public void close() 
	{
    	if ((song != null) && (song.istEineAenderungErfolgt()))
		{
			int res = JOptionPane.showConfirmDialog(this, 
					"Es sind Änderungen erfolgt, die verloren gehen.\n"+
					"Soll Taktus trotzdem beendet werden ?", 
					"Hinweis", 
					JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.NO_OPTION)
				return;
		}
		super.close();
		System.exit(0);
	}
    
    /**
     * beenden Taktus
     */
    private void onBeenden()
    {
    	close();
    }
    
	/**
	 * setzen eines leeren Songs
	 */
	private void setSong()
	{
		getJTabbedPaneBearbeitung().setSelectedIndex(0);
    	
    	if (song != null)
    	{
    		this.getJPanelSong().remove(song);
    		this.getJPanelSongText().remove(song.getSongText());
    	}
    	
		song = new Song();
		song.setLocation(6, 2);
		song.setVisible(true);
		song.setLayout(null);
		song.setSizeBreiteA4(BREITE_SONG);

		this.getJPanelSong().add(song);
		song.initNewSong();
		song.setRahmenAlleSchemas();
		song.konstruierBlatt(true);

		song.handleSongText();
		this.getJPanelSongText().add(song.getSongText());
		
		this.repaint();

		this.getJPanelTitel().setDaten(song);
		this.getJPanelTeile().setDaten(song);
		this.getJPanelTakte().setDaten(song);
		this.getJPanelText().setDaten(song);
		this.getJPanelTitel().setNeuSong();
		
		setCurrentDateiName("");

	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				song.resetAlleAenderungMarker();
			}
		});
	}

	/**
     * vom Bediener initiert: neuer Song
     */
    private void onNeu()
    {
    	istLadePhase = true;

    	setNachStart();

    	if ((song != null) && (song.istEineAenderungErfolgt()))
		{
			int res = JOptionPane.showConfirmDialog(this, 
					"Es sind Änderungen erfolgt, die verloren gehen.\n"+
					"Soll trotzdem ein neuer Titel erstellt werden ?", 
					"Hinweis", 
					JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.NO_OPTION)
			{
				istLadePhase = false;
				return;
			}
		}

	    setSongNeu();
    }

	/**
	 * initialiseren neuer Song
	 */
	private void setSongNeu() 
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
		    	setSong();

				istLadePhase = false;
			}

		});
	}
	
	/**
	 * @param name
	 */
	private void setCurrentDateiName(String name)
	{
		currentDateiName = name;
		if (currentDateiName.length() == 0)
			this.setTitle(TAKTUS_TITLE);
		else
			this.setTitle(TAKTUS_TITLE + " - " + currentDateiName);
	}
    
    /**
     * laden eines Songs vom Bediener initiert
     */
    private void onLaden()
    {
    	istLadePhase = true;
    	
		if ((song != null ) && (song.istEineAenderungErfolgt()))
		{
			int res = JOptionPane.showConfirmDialog(this, 
					"Es sind Änderungen erfolgt, die verloren gehen.\n"+
					"Soll trotzdem ein anderer Titel geladen werden ?", 
					"Hinweis", 
					JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.NO_OPTION)
			{
				istLadePhase = false;
				return;
			}
		}

    	// Namen bestimmen
    	File xmlFile = zeigeDateiAuswahlDialog(true);
    	if (xmlFile == null)
    		return;

		Song sMon = new Song();
		sMon = AXmlSongReadBroker.getInstance().readSongFromXml(xmlFile, this);
		if (sMon != null)
		{
	    	setNachStart();

	    	if (song != null)
	    	{
	    		this.getJPanelSong().remove(song);
	    		this.getJPanelSongText().remove(song.getSongText());
	    	}

	    	song = sMon;
	    	
			song.setLocation(6, 2);
			song.setVisible(true);
			song.setLayout(null);
			song.setSizeBreiteA4(BREITE_SONG);

			this.getJPanelSong().add(song);

			song.setRahmenAlleSchemas();
			song.konstruierBlatt(true);

			song.handleSongText();
			this.getJPanelSongText().add(song.getSongText());
			
			this.repaint();

			this.getJPanelTitel().setDaten(song);
			this.getJPanelTitel().setAltSong();
			this.getJPanelTeile().setDaten(song);
			this.getJPanelTakte().setDaten(song);
			this.getJPanelText().setDaten(song);
			
		    SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					song.resetAlleAenderungMarker();
					if (getJTabbedPaneBearbeitung().getSelectedIndex() == 2)
					{
						getJPanelTakte().setFocus();
					}
				}
			});
		}
		
		istLadePhase = false;
    }
    
    /**
     * Testphase
     */
    private void onNotenPultAnsicht()
    {
    	DialogNotenPultAnsicht dialogKomplettScreen = new DialogNotenPultAnsicht(this,"Notenpult-Ansicht",true);
//		dialogKomplettScreen.resetDaten(song.transientClone(), true);
		dialogKomplettScreen.setDaten();
		dialogKomplettScreen.setVisible(true);
    }
    
    /**
     * Aufruf der Hilfe vom Bediener
     */
    private void onHilfe()
    {
    	DialogHilfe hilfe = DialogHilfe.Instance(this,"Hilfe",false);
    	hilfe.setVisible(true);
    }

    /**
     * Aufruf der Konfiguration vom Bediener
     */
    private void onKonfigurieren()
    {
    	DialogKonfiguration konf = new DialogKonfiguration(this,"Konfiguration",true);
    	konf.setDaten(song);
    	konf.setVisible(true);
    }

    /**
     * Aufruf des Transponieren-Dialoges vom Bediener
     */
    private void onTransponieren()
    {
    	DialogTansponieren info = new DialogTansponieren(this,
    			"Transponieren des ganzen Titels",true);
    	info.setDaten(song);
    	info.setVisible(true);
    }

    /**
     * Aufruf des Info-Dialoges vom Bediener
     */
    private void onInfo()
    {
    	DialogInfo info = new DialogInfo(this,"Informationen",true);
    	info.setVisible(true);
    }
    
    /**
     * Aufruf des Speichern eines Songs vom Bediener
     * @param istSpeichernUnter	== true	 SpeichernUnter-Dialog
     * 							== false speichern in gegebener Datei
     */
    private void onSpeichern(boolean istSpeichernUnter)
    {
//    	System.out.println("+1++"+currentDateiName+"+++"+istSpeichernUnter+"++"+
//    			Konfiguration.getInstance().getTaktusDirectory()+"**"+
//    			File.pathSeparator+"**"+File.separator);
    	
    	if (song.getTitel().length() == 0)
    	{
    		String mes = "Ein Taktus-Schema ohne Titel ist nicht erlaubt !\n" +
    					 "Also bitte einen schönen Namen ausdenken !";
    		JOptionPane.showMessageDialog(this, mes, 
    									"Hinweis", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	
    	File xmlFile;
    	
    	if ((istSpeichernUnter) || (currentDateiName.length() == 0))
    	{
	    	// Namen bestimmen
	    	xmlFile = zeigeDateiAuswahlDialog(false);
	    	if (xmlFile == null)
	    		return;
    	}
    	else
    	{
    		// Dateiname bekannt
    		String path = Konfiguration.getInstance().getTaktusDirectory() + 
    				File.separator +
    				currentDateiName;
    		xmlFile = new File(path);
    	}

		AXmlSongWriteBroker.getInstance().writeSongFromXml(xmlFile, song, this);
	    SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				song.resetAlleAenderungMarker();
			}
		});
    }
    
    
	/**
	 * Vorbereitung zum Drucken
	 * d.h. zuweisen der Song-Teile (inkl. Text) zum tempDruckPanel;
	 * setzen Rahmen; setzen Layout
	 * @param songAuswahlDruck
	 * @param pu
	 */
	private void preparePrintFile(Song songAuswahlDruck, PrintUtility pu)
	{
		if (songAuswahlDruck != null)
		{
			songAuswahlDruck.setWirdGedruckt();
			songAuswahlDruck.setLocation(0, 0);
			//songAuswahlDruck.setVisible(true);
			songAuswahlDruck.setLayout(null);
			songAuswahlDruck.setSizeBreiteA4(750);
			tempDruckPanel.add(songAuswahlDruck);
			
			if (Konfiguration.getInstance().isShowRahmenImDruck())
			{
				songAuswahlDruck.setRahmen();
				if (songAuswahlDruck.getSchema() != null)
					songAuswahlDruck.getSchema().setRahmen();
				if (songAuswahlDruck.getText() != null)
					songAuswahlDruck.getText().setRahmen();
			}

			songAuswahlDruck.konstruierBlatt(false);
			songAuswahlDruck.handleSongText();

			songAuswahlDruck.setBackground(Color.WHITE);
			songAuswahlDruck.setForeground(Color.BLACK);

			pu.addComponentToPrint(songAuswahlDruck);

			songAuswahlDruck.repaint();

			if (songAuswahlDruck.getText().istTextAufZweiterSeite())
			{
				final SongText songSeite2 = songAuswahlDruck.getSongText().transientCloneText();
				songSeite2.setLocation(1, songAuswahlDruck.getHeight());
				songSeite2.setVisible(true);
				songSeite2.setLayout(null);
				songSeite2.setSizeBreiteA4(750);
				songSeite2.konstruierTextBlatt(false);
				songSeite2.setBackground(Color.WHITE);
				songSeite2.setForeground(Color.BLACK);
				tempDruckPanel.add(songSeite2);
				
				pu.addComponentToPrint(songSeite2);
			}
		}
	}

	/**
	 * Besonderheit tempDruckPanel: dies Panel muss dem Hauptframe zugeordet
	 * werden, sonst ist der Ausdruck leer (warum das so ist, dass muss noch
	 * geklärt werden...)
	 * @return
	 */
	private PrintUtility initBeginnDruck()
	{
		tempDruckPanel = new JPanel();
		tempDruckPanel.setLayout(null);
		tempDruckPanel.setLocation(5, 5);
		tempDruckPanel.setSize(5, 5);
		this.getContentPane().add(tempDruckPanel);
		PrintUtility pu = new PrintUtility();
		return pu;
	}

	/**
	 * Drucken eines Songs
	 * @param song
	 */
	public void setDruckDaten(Song song)
	{
		PrintUtility pu = initBeginnDruck();

		preparePrintFile(song, pu);

		pu.print();
		
		initEndeDruck(pu);
	}
	
	/**
	 * Drucken mehrerer Songs
	 * @param retFiles
	 */
	public void setDruckDaten(File[] retFiles)
	{
		PrintUtility pu = initBeginnDruck();
		
        for (int i = 0; i < retFiles.length; i++)
		{
			File f = retFiles[i];
			Song songAuswahlDruck;
			songAuswahlDruck = AXmlSongReadBroker.getInstance().readSongFromXml(f, this);
        	preparePrintFile(songAuswahlDruck, pu);
		}
        
		pu.print();

		initEndeDruck(pu);
	}

	/**
	 * Nachbereichtung Drucken; 
	 * @param pu
	 */
	private void initEndeDruck(PrintUtility pu)
	{
		for (int j=0; j<pu.getComponentList().size();j++)
		{
			tempDruckPanel.remove(song);
		}
		pu.getComponentList().removeAllElements();
		pu.clearComponentList();
		AChordFrame.this.getContentPane().remove(tempDruckPanel);
		tempDruckPanel = null;
		System.gc();
	}
	
    /**
     * Aufruf Drucken aus Menü
     */
    private void onDrucken()
    {
		if (this.song != null)
		{
			final Song songDruck = song.transientClone();
			setDruckDaten(songDruck);
		}
    }

    /**
     * Aufruf aus Menü "mehrere Titel drucken"
     */
    private void onAuswahlDrucken()
    {
    	// in der Taktus-Konfigurationsdatei ("Taktus.ini") wird der Pfad
    	// festgehalten, in dem die Taktus-Songs gespeichert sind; ist dieser
    	// Pfad initial leer, dann wird der Pfad "eigene Dateien" angezeigt und
    	// nach der Wahl des Pfades/Dateien der dann gewählte Pfad in die 
    	// Konfigurationsdatei eingetragen
    	String dir = Konfiguration.getInstance().getTaktusDirectory();
        final JFileChooser fc = new JFileChooser(dir)
        { 
			private static final long serialVersionUID = 1L;

			public void approveSelection() 
            { 
            	int anzahl = this.getSelectedFiles().length;
				if (anzahl == 0) 
				{ 
					return; 
				} 
				String message;
				if (anzahl == 1)
					message = "Soll der gewählte Taktus-Titel gedruckt werden ?";
				else
					message= "Sollen die gewählten " + anzahl + " Taktus-Titel gedruckt werden ?";
				int res = JOptionPane.showConfirmDialog (this, message, "Hinweis", 
                   		JOptionPane.OK_CANCEL_OPTION, 
                   		JOptionPane.QUESTION_MESSAGE); 
				if (res == JOptionPane.OK_OPTION) 
				{ 
					super.approveSelection();
				}
            } 
        };

        fc.setFileFilter( new FileFilter()
        {
			public boolean accept( File f ) 
			{
				return f.isDirectory() ||
						f.getName().toLowerCase().endsWith( TAKTUS_EXTENSION.toLowerCase() );
			}
			public String getDescription() 
			{
				return "Taktus-Titel";
			}
        });

        fc.setDialogTitle("Drucken mehrerer Taktus-Dateien");
        fc.setApproveButtonText("Drucken");
        fc.setApproveButtonToolTipText("Drucken der selektierten Taktus-Titel");
        fc.setApproveButtonMnemonic('D');
        fc.setMultiSelectionEnabled(true);
    	final int returnVal = fc.showOpenDialog(null);
    	fc.setVisible(false);
    	this.repaint();
    	song.repaint();
    	
        // schreiben des aktuel gewählten Pfades in die Konfigruationsdatei
    	Konfiguration.getInstance().setTaktusDirectory(fc.getCurrentDirectory().getPath());

        final File[] retFiles;
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
        	retFiles = fc.getSelectedFiles();
//        	setCurrentDateiName(fc.getSelectedFile().getName());
        }
        else
        {
        	retFiles = null;
//        	setCurrentDateiName("");
        }
        
        if (retFiles == null)
        	return;

        setDruckDaten(retFiles);
    }
    
    /**
     * @return
     */
    private boolean istKeineExtensionVorhanden(File f)
    {
    	if (f.getName ().lastIndexOf (".") > 0)
    		return false;
    	else 
    		return true;
    }
    
    /**
     * Löscht die Zeichen, die in einem Dateinamen nicht vorhanden sein
     * sollen. 
     * Wenn der Rückgabestring gleich dem übergebenen String ist, dann wurden
     * kein solchen Zeichen gefunden.
     * 
     * @param st
     * @param istInklExtension - Extension in st vorhanden, d.h. ein '.' 
     * 							 ist im Dateinamen vorhanden
     * @return
     */
    private String loescheSonderZeichen(String st, boolean istInklExtension)
    {
    	String ret;
    	ret = st.replaceAll("ä", "ae");
    	ret = ret.replaceAll("ö", "oe");
    	ret = ret.replaceAll("ü", "ue");
    	ret = ret.replaceAll("ß", "ss");
    	
    	if (! istInklExtension)
    	{
    		ret = ret.replace('.', ' ');
    	}
    	else
    	{
    		// Extension ist erlaubt, d.h. ein Punkt darf vorhanden sein
    		int pind = st.lastIndexOf('.');
    		if ((pind > -1) && (pind < (st.length()-1)))
    		{
	    		// erstmal Extension (inkl. Punkt) extrahieren
	    		String ohneExt = st.substring(0,pind);
	    		if (ohneExt.indexOf('.') > -1)
	    			ret = "Fehler: >= 2 Punkte";
    		}
    		else if (pind == (st.length()-1))
    		{
    			ret = "Fehler: Punkt an letzter Stelle";
    		}
//    		System.out.println("+++++"+ret);
    	}
    	
    	ret = ret.replace('/', ' ');
    	ret = ret.replace('\\', ' ');
    	ret = ret.replace('>', ' ');
    	ret = ret.replace('<', ' ');
    	ret = ret.replace('|', ' ');
    	ret = ret.replace('~', ' ');
    	ret = ret.replace('°', ' ');
    	return ret;
    }
    
    /**
     * @param istOpen - true => 
     * @return
     */
    private File zeigeDateiAuswahlDialog(boolean istOpen)
    {
    	File retFile = null;
    	if (song == null)
    		return retFile;
    	
    	String dir = Konfiguration.getInstance().getTaktusDirectory();
        JFileChooser fc = new JFileChooser(dir)
        { 
			private static final long serialVersionUID = 1L;

			public void approveSelection() 
            { 
            	
				String[ ] message = {"Die Datei existiert bereits!", 
				   				"Wollen Sie sie wirklich überschreiben?"}; 
				String[ ] message2 = {"Es sind weiterhin Zeichen im Dateinamen vorhanden,",
									  "die nicht enthalten sein sollten.", 
				   					  "Wollen Sie trotzdem speichern?"}; 
				File f = getSelectedFile(); 
				if (f == null) 
				{ 
					return; 
				} 
				if (this.getDialogType() == JFileChooser.SAVE_DIALOG)
				{
					// löschen verbotener Zeichen; 
					// Hinweis, wenn welche vorhanden sind;
					// sonst nix tun
					String dateiName = loescheSonderZeichen(f.getName(), true);
					if (! dateiName.equals(f.getName()))
					{
						int res = JOptionPane.showConfirmDialog (this, message2, "Hinweis", 
	                                                          		JOptionPane.OK_CANCEL_OPTION, 
	                                                           		JOptionPane.QUESTION_MESSAGE); 
						if (res == JOptionPane.OK_OPTION) 
							super.approveSelection(); 
						else
							return;
					}
					
					
					// wenn keine Extension vorhanden, dann Extension ranhängen
					if (istKeineExtensionVorhanden(f))
					{
						f = new File(f.getPath() + TAKTUS_EXTENSION);
						setSelectedFile(f);
					}
					
					if (f.exists())
					{
						int res = JOptionPane.showConfirmDialog (this, message, "Hinweis", 
	                                                           		JOptionPane.OK_CANCEL_OPTION, 
	                                                           		JOptionPane.QUESTION_MESSAGE); 
						if (res == JOptionPane.OK_OPTION) 
						{ 
							super.approveSelection(); 
						} 
					} 
					else
					{
						super.approveSelection(); 
					}
				}
				else
				{
					if (f.exists())
						super.approveSelection(); 
				}
            } 

         };

        fc.setFileFilter( new FileFilter()
        {
			public boolean accept( File f ) 
			{
				return f.isDirectory() ||
						f.getName().toLowerCase().endsWith( TAKTUS_EXTENSION.toLowerCase() );
			}
			public String getDescription() 
			{
				return "Taktus-Titel";
			}
        });

        int returnVal = 0;
        if (istOpen)
        {
            fc.setDialogTitle("Öffnen einer Taktus-Datei");
        	returnVal = fc.showOpenDialog(this);
        }
        else
        {
            fc.setDialogTitle("Speichern der aktuellen Taktus-Datei");
        	if (currentDateiName.length() > 0)
        		fc.setSelectedFile(new File(currentDateiName));
        	else
        	{
            	// wenn ein neuer Song gespeichert werden soll, dann werden
            	// verbotene Zeichen aus dem Titel für den Dateinamen gelöscht
            	String dateiName = loescheSonderZeichen(song.getTitel(),false) + 
            						TAKTUS_EXTENSION;
        		fc.setSelectedFile(new File(dateiName));
        	}
        	returnVal = fc.showSaveDialog(this);
        }

        Konfiguration.getInstance().setTaktusDirectory(fc.getCurrentDirectory().getPath());

        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
        	retFile = fc.getSelectedFile();
        	setCurrentDateiName(fc.getSelectedFile().getName());
        }
        else
        {
//        	if (istOpen)
//        		setCurrentDateiName("");
        }

        return retFile;
    }
    
	/**
	 * notwendige Einstellungen, nachdem ein Song (neu oder geladen) vorhanden ist
	 */
	private void setNachStart()
	{
		if (this.getJPanelLogo() != null)
		{
    		this.getContentPane().remove(getJPanelLogo());
    		this.getJTabbedPaneBearbeitung().setEnabled(true);
    		this.getJPanelTitel().setStartPhase(false);
    		jMItemDrucken.setEnabled(true);
    		jMItemSpeichern.setEnabled(true);
    		jMItemSpeichernUnter.setEnabled(true);
    		jMItemTransponieren.setEnabled(true);
    		jButtonSave.setEnabled(true);
    		jButtonPrint.setEnabled(true);
    		jButtonTransponieren.setEnabled(true);

    		jPanelLogo = null;
		}
	}

	/**
	 * enthält das Logo, welches kurzzeitig beim Start des Programs angezeigt wird
	 * @return
	 */
	private APanel getJPanelLogo()
	{
		if (jPanelLogo == null)
		{
			jPanelLogo = new APanel();
			jPanelLogo.setBounds(680, 201, 150, 130);
			jPanelLogo.setLayout(null);
			jPanelLogo.setBorder(new LineBorder(Color.black));
			jPanelLogo.setImage(AIconManager.ICON_TAKT_LOGO);
		}
		return jPanelLogo;
	}
	

	/**
	 * Toolbar mit Buttons unterhalb der Menüleiste
	 * @return
	 */
	private JToolBar getJToolBarMain()
	{
		if (jToolBarMain == null)
		{
			jToolBarMain = new JToolBar();
			jToolBarMain.setBounds(5, 0, 490, 27);
			jToolBarMain.setFloatable(false);
			jToolBarMain.setBorderPainted(true);
//			jToolBarMain.setBackground(Color.lightGray);
//			jToolBarMain.setBorder(new LineBorder(Color.black));
//			jToolBarMain.setBorder(new MenuBarBorder(Color.gray, Color.darkGray));
			jToolBarMain.setLayout(null);
			jButtonNeu = new JButton();
			jButtonNeu.setMargin(new Insets(2,2,2,2));
			jButtonNeu.setBounds(5, 1, 25, 25);
			jButtonNeu.setIcon(AIconManager.ICON_NEU);
			jButtonNeu.setToolTipText("neuer Titel");
			jButtonNeu.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onNeu();}	});
			jToolBarMain.add(jButtonNeu);

			jButtonOpen = new JButton();
			jButtonOpen.setMargin(new Insets(2,2,2,2));
			jButtonOpen.setBounds(32, 1, 25, 25);
			jButtonOpen.setIcon(AIconManager.ICON_OPEN);
			jButtonOpen.setToolTipText("Titel laden");
			jButtonOpen.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onLaden();}	});
			jToolBarMain.add(jButtonOpen);
			
			jButtonSave = new JButton();
			jButtonSave.setMargin(new Insets(4,2,2,2));
			jButtonSave.setBounds(59, 1, 25, 25);
			jButtonSave.setIcon(AIconManager.ICON_SAVE);
			jButtonSave.setToolTipText("Titel speichnern");
			jButtonSave.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onSpeichern(false);}	});
			jToolBarMain.add(jButtonSave);
			
			jButtonPrint = new JButton();
			jButtonPrint.setMargin(new Insets(6,2,2,2));
			jButtonPrint.setBounds(95, 1, 25, 25);
			jButtonPrint.setIcon(AIconManager.ICON_PRINT);
			jButtonPrint.setToolTipText("Titel drucken");
			jButtonPrint.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onDrucken();}	});
			jToolBarMain.add(jButtonPrint);

			jButtonTransponieren = new JButton();
			jButtonTransponieren.setMargin(new Insets(4,2,2,2));
			jButtonTransponieren.setBounds(130, 1, 25, 25);
			jButtonTransponieren.setIcon(AIconManager.ICON_TRANSP);
			jButtonTransponieren.setToolTipText("Titel transponieren");
			jButtonTransponieren.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onTransponieren();}	});
			jToolBarMain.add(jButtonTransponieren);

			jButtonInfo = new JButton();
			jButtonInfo.setMargin(new Insets(4,4,2,2));
			jButtonInfo.setBounds(170, 1, 25, 25);
			jButtonInfo.setIcon(AIconManager.ICON_INFO);
			jButtonInfo.setToolTipText("Produkt und Versionsinformationen");	
			jButtonInfo.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onInfo();}	});
			jToolBarMain.add(jButtonInfo);

			jButtonHilfe = new JButton();
			jButtonHilfe.setMargin(new Insets(4,4,2,2));
			jButtonHilfe.setBounds(210, 1, 25, 25);
			jButtonHilfe.setIcon(AIconManager.ICON_HILFE);
			jButtonHilfe.setToolTipText("Hilfe");	
			jButtonHilfe.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onHilfe();}	});
			jToolBarMain.add(jButtonHilfe);
			
			jButtonNotenPultAnsicht = new JButton();
			jButtonNotenPultAnsicht.setMargin(new Insets(2,2,2,2));
			jButtonNotenPultAnsicht.setBounds(165, 1, 25, 25);
			jButtonNotenPultAnsicht.setIcon(AIconManager.ICON_NOTENPULT);
			jButtonNotenPultAnsicht.setToolTipText("Notenpult-Ansicht");	
			jButtonNotenPultAnsicht.addActionListener(new ActionListener()
				{public void actionPerformed(ActionEvent e)	{onNotenPultAnsicht();}	});
			
			if (istTestNotenPultAnsicht)
			{
				jToolBarMain.add(jButtonNotenPultAnsicht);
				jButtonInfo.setLocation(200, 1);
				jButtonHilfe.setLocation(227, 1);
			}

			
		}
		return jToolBarMain;
	}

 }
