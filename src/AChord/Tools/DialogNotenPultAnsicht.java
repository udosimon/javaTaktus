/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 10.12.2009
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Dialog, in dem Bildschirmfüllend der Song angezeigt wird;
 * "Notebook als Notenpult"
 * 
 * 
 */
package AChord.Tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import AChord.AClasses.ADialog;
import AChord.AClasses.AFile;
import AChord.AClasses.AIconManager;
import AChord.AClasses.APanel;
import AChord.Hilfe.Konfiguration;
import AChord.MainFrame.AChordFrame;
import AChord.Schema.Song;
import AChord.Schema.SongText;
import AChord.xml.AXmlSongReadBroker;

public class DialogNotenPultAnsicht extends ADialog
{
    private final static long serialVersionUID = 1;
    
    private final static int WIDTH_PANELSTEUERUNG = 100;
    private final static int WIDTH_PANELSTEUERUNG_IN_ARBEIT = 300;
    private final static Color COLOR_BACKGROUND = Color.LIGHT_GRAY;
    private final static Color COLOR_FOREGROUND = Color.BLACK;
    
    APanel panelScreenSong;
    APanel panelScreenText;
    APanel panelSteuerung;
    APanel panelTaktus;
        
    JButton jButtonCancel;
    JButton jButtonResize;
    JButton jButtonAuswahl;
    JButton jButtonNextTitle;
    JButton jButtonPreviousTitle;
    JButton jButtonNachOben;
    JButton jButtonNachUnten;
    JButton jButtonLoeschen;
    JButton jButtonTransponieren;
    
    
	private JScrollPane scrollListBox;
    JList listBox;
    JLabel labelTake;
    
    Song songScreen;

    AChordFrame parent;

    Vector listeTake = new Vector();
    boolean istInitScreen = true;
    boolean istSelectionAuswertung = true;
    int aktWidthPanelSteuerung = WIDTH_PANELSTEUERUNG;
    
    
    /**
     * Konstruktor
     * @param parent
     * @param title
     * @param modal
     */
    public DialogNotenPultAnsicht(AChordFrame parent, String title, boolean modal)
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
		istInitScreen = true;

    	this.setLayout(null);
    	
    	// Listener, der das Bewegen und Ändern der Größe des Dialoges handelt
    	this.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener()
    	{
			public void ancestorMoved(HierarchyEvent e) 
			{
//				System.out.println("MOVING");				
			}
			public void ancestorResized(HierarchyEvent e) 
			{
				handleResizing(e);
			}			
		});
    	
    	panelScreenSong = new APanel();
    	panelScreenSong.setLayout(null);
//    	panelScreenSong.setBorder(new LineBorder(Color.BLACK, 1));
    	panelScreenSong.setBackground(COLOR_BACKGROUND);
    	panelScreenText = new APanel();
    	panelScreenText.setLayout(null);
//    	panelScreenText.setBorder(new LineBorder(Color.BLACK, 1));
    	panelScreenText.setBackground(COLOR_BACKGROUND);
    	panelTaktus = new APanel();
    	panelTaktus.setLayout(null);
    	panelTaktus.setBorder(new LineBorder(Color.BLACK, 1));
    	panelTaktus.setBackground(COLOR_BACKGROUND);
    	panelSteuerung = new APanel();
    	panelSteuerung.setLayout(null);
    	panelSteuerung.setBorder(new LineBorder(Color.BLACK, 2));

    	this.getContentPane().add(panelTaktus);
    	panelTaktus.add(panelScreenSong);
    	panelTaktus.add(panelScreenText);
    	this.getContentPane().add(panelSteuerung);
		panelSteuerung.add(getJButtonCancel());
		panelSteuerung.add(getJButtonResize());
		panelSteuerung.add(getJButtonAuswahl());
		panelSteuerung.add(getJButtonNextTitle());
		panelSteuerung.add(getJButtonPreviousTitle());
		panelSteuerung.add(getJButtonNachOben());
		panelSteuerung.add(getJButtonNachUnten());
		panelSteuerung.add(getJButtonLoeschen());
		panelSteuerung.add(getJButtonTransponieren());
		panelSteuerung.add(getScrollListBox());
		panelSteuerung.add(getJLabelTake());

		setButtonsEnabled(false);

		istInitScreen = false;

		this.addKeyListener(keyListener);
	}

	/**
	 * @return
	 */
	private JButton getJButtonTransponieren()
	{
		if (jButtonTransponieren == null)
		{
			jButtonTransponieren = new JButton();
			jButtonTransponieren.setText("");
			jButtonTransponieren.setIcon(AIconManager.ICON_TRANSP);
			jButtonTransponieren.setFont(getFontA());
			jButtonTransponieren.setBounds(5, 310, WIDTH_PANELSTEUERUNG-10, 30);
			jButtonTransponieren.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onTransponieren();}});
			jButtonTransponieren.addKeyListener(keyListener);
		}
		return jButtonTransponieren;
	}

	/**
	 * @return
	 */
	private JButton getJButtonCancel()
	{
		if (jButtonCancel == null)
		{
			jButtonCancel = new JButton();
			jButtonCancel.setText("Beenden");
			jButtonCancel.setFont(getFontA());
			jButtonCancel.setBounds(5, 420, WIDTH_PANELSTEUERUNG-10, 30);
			jButtonCancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonCancel();}});
			jButtonCancel.addKeyListener(keyListener);

		}
		return jButtonCancel;
	}

	/**
	 * @return
	 */
	private JButton getJButtonResize()
	{
		if (jButtonResize == null)
		{
			jButtonResize = new JButton();
			jButtonResize.setText("<<");
			jButtonResize.setFont(getFontA());
			jButtonResize.setBounds(5, 380, 50, 30);
			jButtonResize.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonResize();}});
			jButtonResize.addKeyListener(keyListener);
		}
		return jButtonResize;
	}

	/**
	 * @return
	 */
	private JButton getJButtonNextTitle()
	{
		if (jButtonNextTitle == null)
		{
			jButtonNextTitle = new JButton();
			jButtonNextTitle.setText("");
			jButtonNextTitle.setIcon(AIconManager.ICON_NEXT);
			jButtonNextTitle.setFont(getFontA());
			jButtonNextTitle.setBounds(55, 260, 40, 30);
			jButtonNextTitle.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonNext();}});
			jButtonNextTitle.addKeyListener(keyListener);
		}
		return jButtonNextTitle;
	}

	/**
	 * @return
	 */
	private JButton getJButtonPreviousTitle()
	{
		if (jButtonPreviousTitle == null)
		{
			jButtonPreviousTitle = new JButton();
			jButtonPreviousTitle.setText("");
			jButtonPreviousTitle.setIcon(AIconManager.ICON_PREVIOUS);
			jButtonPreviousTitle.setFont(getFontA());
			jButtonPreviousTitle.setBounds(5, 260, 40, 30);
			jButtonPreviousTitle.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonPrevious();}});
			jButtonPreviousTitle.addKeyListener(keyListener);
		}
		return jButtonPreviousTitle;
	}

	/**
	 * @return
	 */
	private JButton getJButtonNachOben()
	{
		if (jButtonNachOben == null)
		{
			jButtonNachOben = new JButton();
			jButtonNachOben.setText("");
			jButtonNachOben.setIcon(AIconManager.ICON_ARROW_UP);
			jButtonNachOben.setFont(getFontA());
			jButtonNachOben.setBounds(WIDTH_PANELSTEUERUNG_IN_ARBEIT-45, 45, 40, 30);
			jButtonNachOben.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonBewegen(true);}});
			jButtonNachOben.addKeyListener(keyListener);
		}
		return jButtonNachOben;
	}

	/**
	 * @return
	 */
	private JButton getJButtonNachUnten()
	{
		if (jButtonNachUnten == null)
		{
			jButtonNachUnten = new JButton();
			jButtonNachUnten.setText("");
			jButtonNachUnten.setIcon(AIconManager.ICON_ARROW_DOWN);
			jButtonNachUnten.setFont(getFontA());
			jButtonNachUnten.setBounds(WIDTH_PANELSTEUERUNG_IN_ARBEIT-45, 85, 40, 30);
			jButtonNachUnten.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonBewegen(false);}});
			jButtonNachUnten.addKeyListener(keyListener);
		}
		return jButtonNachUnten;
	}

	/**
	 * @return
	 */
	private JButton getJButtonAuswahl()
	{
		if (jButtonAuswahl == null)
		{
			jButtonAuswahl = new JButton();
			jButtonAuswahl.setText("+");
			jButtonAuswahl.setMargin(new Insets(2,2,2,2));
			jButtonAuswahl.setFont(new Font(Konfiguration.getInstance().getSchriftArt(),Font.BOLD,16));
			jButtonAuswahl.setBounds(WIDTH_PANELSTEUERUNG_IN_ARBEIT-45, 145, 40, 30);
			jButtonAuswahl.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonAuswahl();}});
			jButtonAuswahl.addKeyListener(keyListener);
		}
		return jButtonAuswahl;
	}

	/**
	 * @return
	 */
	private JButton getJButtonLoeschen()
	{
		if (jButtonLoeschen == null)
		{
			jButtonLoeschen = new JButton();
			jButtonLoeschen.setText("");
			jButtonLoeschen.setIcon(AIconManager.ICON_TRASH);
			jButtonLoeschen.setFont(getFontA());
			jButtonLoeschen.setBounds(WIDTH_PANELSTEUERUNG_IN_ARBEIT-45, 185, 40, 30);
			jButtonLoeschen.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					onButtonLoeschen();}});
			jButtonLoeschen.addKeyListener(keyListener);
		}
		return jButtonLoeschen;
	}

	private JLabel getJLabelTake()
	{
		if (labelTake == null)
		{
			labelTake = new JLabel();
			labelTake.setText("Take 1");
			labelTake.setFont(getFontA());
			labelTake.setBounds(5, 5, WIDTH_PANELSTEUERUNG-10, 30);
		}
		return labelTake;
	}

	private JList getListBox()
	{
		if (listBox == null)
		{
//			String[] data = {"eins","zwei","Titel 35","asdfads sdf dsfsd fdsfds"};
			listBox = new JList();
			listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listBox.setFont(getFontA());
			listBox.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e) {
					listValueChanged(e);		
				}
			});
			listBox.addKeyListener(keyListener);

		}
		return listBox;
	}
	
	private JScrollPane getScrollListBox()
	{
		if (scrollListBox == null)
		{
			scrollListBox = new JScrollPane();
			scrollListBox.setBounds(5, 45, aktWidthPanelSteuerung-10, 200);
			scrollListBox.setViewportView(getListBox());
		}
		return scrollListBox;
	}

	
	
	/**
	 * @param e
	 */
	private void listValueChanged(ListSelectionEvent e)
	{
		if ((! istSelectionAuswertung) || (istInitScreen))
			return;
		if (getListBox().isSelectionEmpty())
		{
			System.out.println("  NIX selectiert");
			// buttons disablen
			setButtonsEnabled(false);
		}
		else
		{
			System.out.println("  is was selectiert");
			anzeigeTaktusSong();
			// buttons enablen
			setButtonsEnabled(true);
		}
	}
	
	/**
	 * 
	 */
	private void onButtonNext()
	{
		int index = listBox.getSelectedIndex();
		if (index < listeTake.size()-1)
			listBox.setSelectedIndex(index+1);
	}
	/**
	 * 
	 */
	private void onButtonPrevious()
	{
		int index = listBox.getSelectedIndex();
		if (index > 0)
			listBox.setSelectedIndex(index-1);
	}

		/**
	 * umsortieren der Titel; d.h. der markierte Titel wird
	 * entweder nach oben (istNachOben==true) oder nach unten geschoben
	 * @param istNachOben
	 */
	private void onButtonBewegen(boolean istNachOben)
	{
		istSelectionAuswertung = false;
		int index = listBox.getSelectedIndex();
		if (istNachOben)
		{
			if (index == 0)
				System.out.println("    bewegen nach oben: index == 0");
			else
			{
				System.out.println("    bewegen nach oben: index == " + index);
				
				Object o = listBox.getSelectedValue();
				listeTake.remove(o);
				listeTake.insertElementAt(o, index-1);
				listBox.setListData(listeTake);
				listBox.setSelectedIndex(index-1);
			}
		}
		else
		{
			if (index == listeTake.size()-1)
				System.out.println("    bewegen nach unten: index == Ende");
			else
			{
				System.out.println("    bewegen nach unten: index == " + index);
				
				Object o = listBox.getSelectedValue();
				listeTake.remove(o);
				listeTake.insertElementAt(o, index+1);
				listBox.setListData(listeTake);
				listBox.setSelectedIndex(index+1);
			}
		}
		istSelectionAuswertung = true;
	}
	
	/**
	 * 
	 */
	private void onButtonLoeschen()
	{
		int index = listBox.getSelectedIndex();
		System.out.println("    Löschen: index == " + index);
		Object o = listBox.getSelectedValue();
		listeTake.remove(o);
		listBox.setListData(listeTake);
		if (listeTake.size() > 0)
		{
			listBox.setSelectedIndex(0);
		}
		else
		{
			leereSong();
		}
	}

    /**
     * Aufruf des Transponieren-Dialoges vom Bediener
     */
    private void onTransponieren()
    {
    	DialogTansponieren info = new DialogTansponieren(this,
    			"Transponieren des ganzen Titels",true);
    	info.setDaten(songScreen);
    	info.setVisible(true);
    }

	/**
	 * 
	 */
	private void onButtonCancel()
	{
		close();
	}

	
	

	/**
	 * 
	 */
	private void onButtonAuswahl()
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
				super.approveSelection();
            } 
        };
        
        fc.setFileFilter( new FileFilter()
        {
			public boolean accept( File f ) 
			{
				return f.isDirectory() ||
						f.getName().toLowerCase().endsWith( AChordFrame.TAKTUS_EXTENSION.toLowerCase() );
			}
			public String getDescription() 
			{
				return "Taktus-Titel";
			}
        });

        fc.setDialogTitle("Auswahl Taktus-Dateien");
        fc.setApproveButtonText("Übernehmen");
        fc.setApproveButtonToolTipText("Auswahl der Taktus-Titel für Take");
        fc.setApproveButtonMnemonic('A');
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	final int returnVal = fc.showOpenDialog(null);
    	fc.setVisible(false);
    	this.repaint();
    	
        // schreiben des aktuel gewählten Pfades in die Konfigruationsdatei
    	Konfiguration.getInstance().setTaktusDirectory(fc.getCurrentDirectory().getPath());

        final File[] retFiles;
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
        	retFiles = fc.getSelectedFiles();
        }
        else
        {
        	retFiles = null;
        }
        
        if (retFiles == null)
        	return;

        for (int i = 0; i < retFiles.length; i++)
		{
			File f = retFiles[i];
			if (f.exists())
			{
				AFile af = new AFile(f);
				listeTake.add(af);
			}
		}
        getListBox().setListData(listeTake);
        if ((getListBox().isSelectionEmpty()) && (listeTake.size() > 0))
        	getListBox().setSelectedIndex(0);
    }

	/**
	 * 
	 */
	private void onButtonResize()
	{
		if (aktWidthPanelSteuerung == WIDTH_PANELSTEUERUNG)
		{
			setBreiteSteuerung();
		}
		else
		{
			setSchmaleSteuerung();
		}
		handleInternResizing();
	}

	/**
	 * Aufruf von Außen; kein Song wird übergeben, d.h. leere Anzeige
	 */
	public void setDaten()
	{
		setBreiteSteuerung();
		
    	Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    	final int screenX = screensize.width;
    	final int screenY = screensize.height;
    	
    	this.setSize(screensize);

    	vorbereitePanels(screenX, screenY);
	}
	
    /**
     * Aufruf von innen
     * @param songScreen
     */
    public void resetDaten(Song s)
    {
    	istInitScreen = true;
    	
    	APanel.setIstMarkierungErlaubt(false);

    	panelScreenSong.removeAll();
    	panelScreenText.removeAll();
    	
  		this.songScreen = s;
    	
    	int screenX;
    	int screenY;

    	screenX = this.getWidth();
    	screenY = this.getHeight();
    	this.setSize(screenX, screenY);
    	System.out.println("##ZWEIT##"+screenX+"##"+screenY);

		istInitScreen = true;

    	if (! songScreen.getText().istTextAufZweiterSeite())
    	{
    		setEineSeite(screenX, screenY);
    	}
    	else
    	{
    		setZweiSeiten(screenX, screenY);
    	}
		
		if (Konfiguration.getInstance().isShowRahmenImDruck())
		{
			songScreen.setRahmen();
			if (songScreen.getSchema() != null)
				songScreen.getSchema().setRahmen();
			if (songScreen.getText() != null)
				songScreen.getText().setRahmen();
		}

		songScreen.repaint();
		
		istInitScreen = false;
    }

    /**
     * @param screenX
     * @param screenY
     * @return
     */
    private int vorbereitePanels(int screenX, int screenY)
	{
		screenX = screenX-aktWidthPanelSteuerung;
		panelTaktus.setSize(screenX,screenY);

		panelSteuerung.setSize(aktWidthPanelSteuerung, screenY);
    	panelSteuerung.setLocation(screenX, 0);
    	getJButtonCancel().setLocation(5, screenY-40);
    	getJButtonResize().setLocation(5, screenY-80);

		return screenX;
	}
	
	/**
	 * Größe des Songs ermitteln und konstruieren
	 * (für Song und Text auf 2 Seiten) 
	 * @param screenX
	 * @param screenY
	 */
	private void setZweiSeiten(int screenX, int screenY) 
	{
    	screenX = vorbereitePanels(screenX, screenY);

    	songScreen.setLayout(null);
		songScreen.setLocation(0, 0);

		songScreen.setBackground(COLOR_BACKGROUND);
		songScreen.setForeground(COLOR_FOREGROUND);
		// Gpössenermittlung des Songs (einer Seite) in Abhängigkeit der
		// Breite und Höhe der vorgegebenen Werte
		songScreen.setSizeHoeheA4(screenY-20);
		if ((2 * songScreen.getWidth()) > screenX)
			songScreen.setSizeBreiteA4((screenX/2) - 20);

		songScreen.konstruierBlatt(false);
		songScreen.handleSongText();
	
		SongText songScreenSeite2 = songScreen.getSongText().transientCloneText();
		songScreenSeite2.setVisible(true);
		songScreenSeite2.setLayout(null);
		songScreenSeite2.setLocation(0, 0);
		songScreenSeite2.setSongSize(songScreen.getWidth(), songScreen.getHeight());
		songScreenSeite2.setBackground(COLOR_BACKGROUND);
		songScreenSeite2.setForeground(COLOR_FOREGROUND);
		songScreenSeite2.konstruierTextBlatt(false);
		
		panelScreenSong.setSize((songScreen.getWidth()), songScreen.getHeight());
		panelScreenSong.setLocation((screenX/2)-(songScreen.getWidth()) -10, 10);
		panelScreenSong.add(songScreen);
		
		panelScreenText.setVisible(true);
		panelScreenText.setSize((songScreen.getWidth()), songScreen.getHeight());
		panelScreenText.setLocation((screenX/2), 10);
		panelScreenText.add(songScreenSeite2);

		songScreenSeite2.repaint();
	}

	/**
	 * Größe des Songs ermitteln und konstruieren
	 * (für alles auf einer Seite) 
	 * @param screenX
	 * @param screenY
	 */
	private void setEineSeite(int screenX, int screenY) 
	{
    	screenX = vorbereitePanels(screenX, screenY);

    	songScreen.setLayout(null);
		songScreen.setLocation(0, 0);

		songScreen.setBackground(COLOR_BACKGROUND);
		songScreen.setForeground(COLOR_FOREGROUND);

		// Gpössenermittlung des Songs in Abhängigkeit der
		// Breite und Höhe der vorgegebenen Werte
		songScreen.setSizeHoeheA4(screenY-20);
		if ((songScreen.getWidth()) > screenX)
		{
			songScreen.setSizeBreiteA4(screenX-20);
		}
		
		panelScreenSong.setSize(songScreen.getWidth(), songScreen.getHeight());
		panelScreenSong.setLocation((screenX/2)-(songScreen.getWidth()/2), 10);
		panelScreenSong.add(songScreen);
		
		songScreen.konstruierBlatt(false);
		songScreen.handleSongText();

		panelScreenText.setVisible(false);

	}
    
	/* (non-Javadoc)
	 * @see AChord.AClasses.ADialog#close()
	 */
	public void close() 
	{
    	APanel.setIstMarkierungErlaubt(true);
    	super.close();
	}
	
	/**
	 * Behandlung der Größenänderung durch den Benutzer des Dialoges
	 */
	private void handleResizing(HierarchyEvent e)
	{
		if ((! istInitScreen) && (e.getChangedParent() instanceof AChordFrame))
		{
			handleInternResizing();
		}
	}

	/**
	 * 
	 */
	private void handleInternResizing() 
	{
		if (songScreen == null)
		{
	    	vorbereitePanels(this.getContentPane().getWidth(), this.getContentPane().getHeight());
		}
		else if (! songScreen.getText().istTextAufZweiterSeite())
		{
			setEineSeite(this.getContentPane().getWidth(), this.getContentPane().getHeight());
		}
		else
		{
			setZweiSeiten(this.getContentPane().getWidth(), this.getContentPane().getHeight());
		}
	}
	
	/**
	 * @param isEnabled
	 */
	private void setButtonsEnabled(boolean isEnabled)
	{
		getJButtonNachOben().setEnabled(isEnabled);
		getJButtonNachUnten().setEnabled(isEnabled);
		getJButtonLoeschen().setEnabled(isEnabled);
		getJButtonNextTitle().setEnabled(isEnabled);
		getJButtonPreviousTitle().setEnabled(isEnabled);
		getJButtonTransponieren().setEnabled(isEnabled);
	}

	/**
	 * 
	 */
	private void leereSong() {
		panelScreenSong.removeAll();
		panelScreenText.removeAll();
		this.repaint();
	}
	
	/**
	 * holen des Taktus-Songs aus der Datei und Anzeige
	 */
	private void anzeigeTaktusSong()
	{
		File f = ((AFile)listeTake.get(listBox.getSelectedIndex())).getFile();
		final Song song = AXmlSongReadBroker.getInstance().readSongFromXml(f, this);

		resetDaten(song);
		handleInternResizing();
	}

	/**
	 * 
	 */
	private void setSchmaleSteuerung() 
	{
		aktWidthPanelSteuerung = WIDTH_PANELSTEUERUNG;
		getJButtonResize().setText("<<");
		getScrollListBox().setSize(aktWidthPanelSteuerung-10, 200);
	}

	/**
	 * 
	 */
	private void setBreiteSteuerung() 
	{
		aktWidthPanelSteuerung = WIDTH_PANELSTEUERUNG_IN_ARBEIT;
		getJButtonResize().setText(">>");
		getScrollListBox().setSize(aktWidthPanelSteuerung-50, 200);
	}


	/**
	 * Tasten für schnelle Bearbeitung
	 * @param e
	 */
	public void onKeyEvent(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_DOWN:
				if (!getJButtonNextTitle().hasFocus())
					getJButtonNextTitle().requestFocus();
				getJButtonNextTitle().doClick();
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_UP:
				if (!getJButtonPreviousTitle().hasFocus())
					getJButtonPreviousTitle().requestFocus();
				getJButtonPreviousTitle().doClick();
				break;
				
			default:
				break;
		}
	}
	/**
	 * 
	 */
	public final KeyListener keyListener = new KeyListener() 
	{
		public void keyPressed(KeyEvent e) 
		{
			onKeyEvent(e);
		}
		public void keyTyped(KeyEvent e) 
		{
		}
		public void keyReleased(KeyEvent e) 
		{
		}
	};
}
