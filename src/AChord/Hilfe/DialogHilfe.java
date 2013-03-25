/*
 * TAKTUS
 *
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 5.1.2007 
 *
 * Beschreibung:
 * 
 * Hilfe-Dialog
 * 
 * 
 */
package AChord.Hilfe;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import AChord.AClasses.ADialog;
import AChord.AClasses.ALabel;



public class DialogHilfe extends ADialog
{
    private final static long serialVersionUID = 1;

	Frame parent;
	private JScrollPane jScrollPane1;
	private ALabel jLabelHilfeText;
	private JButton buttonEnde;

    /**
     * Dieser Dialog ist nicht modal, darum singleton
     */
    private static DialogHilfe singleton = null;
	
	/**
	 * Konstruktor
	 * @param parent
	 * @param title
	 * @param modal
	 */
	private DialogHilfe(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		this.parent = parent;
		construct();
	}
	/**
	 * Konstruktor
	 * @param parent
	 * @param title
	 * @param modal
	 * @return
	 */
	public static DialogHilfe Instance(Frame parent, String title, boolean modal)
	{
		if(singleton == null) {
			singleton = new DialogHilfe(parent, title, modal);
		}
		return singleton;
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
		this.setSize(675,560);
		this.setLocation(parent.getX()+250, parent.getY()+200);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().add(getJSrollPane1());
		getContentPane().add(getJButtonEnde());
	}
	
	/**
	 * @return
	 */
	private JScrollPane getJSrollPane1()
	{
		if (jScrollPane1 == null)
		{
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(35, 19, 601, 466);
			jScrollPane1.setBorder(new LineBorder(Color.BLACK, 1));
			jScrollPane1.setViewportView(getJLabelHilfeText());
			jScrollPane1.setViewportBorder(new LineBorder(getJLabelHilfeText().getBackground(), 5));
		}
		return jScrollPane1;
	}
	
	/**
	 * @return
	 */
	private JLabel getJLabelHilfeText()
	{
		if (jLabelHilfeText == null)
		{
			String st = "<html><H3><b>Taktus</b></H3><HR><br>" +
						"Mit dem Programm Taktus kann für ein Musik-Titel das Akkordschema und (wenn gewünscht) ein Text<p>"+
						"- schnell und effizient eingegeben,<br>"+
						"- archiviert,<br>"+
						"- gepflegt,<br>"+
						"- gedruckt und<br>"+
						"- mit anderen Benutzern ausgetauscht werden.<br><br>"+
						"Taktus besteht aus zwei Teilen: links den Teil der Bearbeitung und rechts den Teil der Anzeige <br>" +
						"des Titels.<br><br>"+
						"Nach dem Start des Programmes kann ein neuer Titel gewählt (Menü Datei/neu oder<br>" +
						"Toolbar-Button 'neuer Titel') oder ein bereits vorhandener Titel geladen werden (Menü Datei/Titel<br>" +
						"laden oder Toolbar-Button 'Titel laden').<br>" +
						"Ein bearbeiteter Titel kann gespeichert, gedruckt und transponiert werden.<br><br>"+
						"Im Anzeigebereich wird der Titel so wie auf einem späteren Druck angezeigt. <br>" +
						"Mit dem blauen waagerechten Verschiebebalken kann das Verhaltnis des Akkordschemas zur  <br>" +
						"Seitengröße eingestellt werden.<br>" +
						"Weiterhin können im Anzeigebereich Taktbereiche markiert und kopiert (gute Möglichkeit, um einen<br>" +
						"Teil zu kopieren) bzw. transponiert werden.<br><br>"+
						"Im Bearbeitungsbereich auf der linken Seite sind die verschiedenen Bedienschritte in<br>" +
						"vier Karteikarten aufgeteilt: Titel, Teile, Takte und Text."+
						"<H3><b>Reiter 'Titel'</b></H3>"+
						"<b>Titel-Name:</b> sinnlos, dies nicht anzugeben; bei einem neueingegebenen Titel ist der Titelname<br>" +
						"auch der Vorschlag für den Dateinamen.<br>" +
						"<b>Autor:</b> kann angegeben werden, muss aber nicht.<br>"+
						"<b>Takte pro Zeile:</b> Wahl der Anzahl der Takte pro Zeile (Default: 4)<br>"+
						"<b>Textanzeige:</b> es kann gewählt werden, ob überhaupt ein Text eingegeben werden soll und<br>" +
						"wenn ja, wo dieser Text dargestellt wird (zusammen mit dem Akkordschema auf einer Seite<br>" +
						"oder auf einer Extraseite).<br>"+
						"<b>Tonart:</b> kann angegeben und angezeigt werden, muss aber nicht.<br>"+
						"<b>Schema:</b> Unterstützung bei der Neueingabe eines Titels; beim Wechsel des Schemas werden alle<br>" +
						"Takte geleert.<br>"+
						"Die Schemaauswahl ist ein Vorschlag. Im Reiter 'Teile' kann die Schemavorauswahl bearbeitet werden."+
						"<H3><b>Reiter 'Teile'</b></H3>"+
						"Teile können hinzugefügt, gelöscht und geleert werden (min 1 Teil, max 6 Teile).<br>"+
						"Weiterhin können für jeden Teil die Attribute gepflegt werden. Dies sind: Teilname, Anzahl der Takte <br>" +
						"(min 1, max 24), Wiederholungszeichen ein/aus, Anzahl der Kopftakte (wenn Wiederhoungszeiche ein), <br>" +
						"Kopfteil (Name des Taktes wird das Kopfzeichen) und Nummer des Taktes mit Kopf (0 = kein <br>" +
						"Kopfzeichen).<br>"+
						"Weiterhin kann unterhalb des Akkordschemas ein Kommentar eingegeben werden."+
						"<H3><b>Reiter 'Takte'</b></H3>"+
						"In diesem Bereich werden die Takte mit Akkorden gefüllt.<br>"+
						"Hierbei wurde darauf geachtet, dass alle Aktionen sowohl mit der Maus als auch mit Tastatureingaben <br>" +
						"durchgeführt werden können (um z.B. den Akkord F#7 einzugeben können auch die Tasten f, # und 7 <br>" +
						"betätigt werden).<br>"+
						"Um innerhalb des Akkordschemas zu navigieren, können die Cursor-Tasten 'Pfeil links' und 'Pfeil rechts' <br>" +
						"benutzt werden (ein Akkord nach links bzw. rechts). Weitere Möglichkeit: im rechten Teil des <br>" +
						"Programmes direkt den zu bearbeitende Takt mit der Maus selektieren.<br>"+
						"Im Textfeld 'frei:' kann ein Akkordklang eingegeben werden, falls dieser nicht in der vorgegebenen <br>" +
						"Liste vorhanden ist (der ist dann allerdings auch nicht per Tastenkürzel erreichbar). <br>" +
						"Soll ein Takt in mehrere Akkorde aufgeteilt (bis max 4 Akkorde), so kann dies auch mit der Taste 't' <br>" +
						"erledigt werden.<br>"+
						"Weiterhin können zu jedem Takt Markierungen (Breaks) auf die Zählzeiten 1, 2, 3 oder 4 gesetzt werden,<br>" +
						"zu dem auch ein Hinweistext gehört (Hinweistext aber nur dann, wenn genau ein Break gesetzt ist)."+
						"<H3><b>Reiter 'Texte'</b></H3>"+
						"Hier kann der Songtext und ein kurzer Kommentar unterhalb des Textes eingegeben werden.<br>" +
						"Im Reiter 'Titel' wird eingestellt, ob der Text zusammen mit dem Akkordschema auf einer Seite, auf<br>" +
						"einer Extra-Seite (Reiter 'Seite 2' im Anzeigebereich) oder gar nicht angezeigt werden soll.<br>" +
						"Wird der Text nicht angezeigt, gehen eventuell voher eingegebene Texte nicht verloren.<br><br>"+
						"<b>Konfiguration</b> (Menü Bearbeiten / Konfiguration): <br>" +
						"- Anzeige des H-Akkordes,<br>" +
						"- Rahmenanzeige an/aus im Anzeigebereich und/oder im Ausdruck, <br>" +
						"- Art und Weise der Anzeige eines neuen vorher leeren Taktes,<br>" +
						"- der Abstand vor dem ersten Teil, der Abstand zwischen den einzelnen Teilen und der Abstand vor<br>" +
						"dem Text (in Pixeln).<br><br>" +
						"<b>Drucken mehrerer Taktus-Titel</b> (Menü Datei / mehrere Titel drucken)<br>" +
						"In dem hier aufgeschalteten Dialog können eine oder mehrere Titel selektiert (shift oder<br>" +
						"ctrl + linke Maustaste) und dann gedruckt werden (Sammeldruck).";
						
			jLabelHilfeText = new ALabel(st);
			jLabelHilfeText.setFont(getFontB());
		}
		return jLabelHilfeText;
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
			buttonEnde.setBounds(290, 495, 80, 30);
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
