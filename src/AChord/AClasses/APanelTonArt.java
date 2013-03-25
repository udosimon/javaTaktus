/*
 * 11.11.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Panel, in dem Ton, Tonart, Klang und Halbton gewählt wird
 */
package AChord.AClasses;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import AChord.Hilfe.Konfiguration;
import AChord.MainFrame.PanelReiter3Takt;




public class APanelTonArt extends APanel
{
    private final static long serialVersionUID = 1;

	private static final int yA = 16;  //y-Abstand zwischen zwei RadioButtons (min 16)
	private static final int yD = 0;   //y-Abstand erster RadioButtons von oben

	private JRadioButton jRadioButtonC;
	private JRadioButton jRadioButtonD;
	private JRadioButton jRadioButtonE;
	private JRadioButton jRadioButtonF;
	private JRadioButton jRadioButtonG;
	private JRadioButton jRadioButtonA;
	private JRadioButton jRadioButtonH;
	private JRadioButton jRadioButtonKreuz;
	private JRadioButton jRadioButtonb;
	private JRadioButton jRadioButtonOhneVz;

	private JRadioButton jRadioButtonDur;
	private JRadioButton jRadioButtonMoll;
	private JRadioButton jRadioButton6;
	private JRadioButton jRadioButton7;
	private JRadioButton jRadioButton9;
	private JRadioButton jRadioButton0;
	private JRadioButton jRadioButtonMaj;
	private JRadioButton jRadioButton75;
	private JRadioButton jRadioButtonPlus;
	private JRadioButton jRadioButton79;
	private JRadioButton jRadioButtonArtOhne;
	private JRadioButton jRadioButtonArtUnsichtbar;
	private JRadioButton jRadioButtonVzUnsichtbar;
	private JRadioButton jRadioButtonTonUnsichtbar;
	private JRadioButton jRadioButtonGeschlechtUnsichtbar;
	
	private JTextField jTextFieldArtEgal;

	private APanel jPanelTonArtVz;
	private APanel jPanelTonArtGeschlecht;
	private APanel jPanelTonArtKlang;

	private JRadioButton jRadioButtonFrei;
	
	private APanelTonArtInterface parent;
	
	boolean onRadioButtonClick = false;
	
	/**
	 * 
	 */
	public APanelTonArt(APanelTonArtInterface parent)
	{
		super();
		this.parent = parent;
		construct();
	}

	/**
	 * 
	 */
	private void construct()
	{
		try
		{
			initForm();
			myInit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	private void initForm() throws Exception
	{
		//this.setBorder(new LineBorder(Color.black));
		this.add(getJRadioButtonC());
		this.add(getJRadioButtonD());
		this.add(getJRadioButtonE());
		this.add(getJRadioButtonF());
		this.add(getJRadioButtonG());
		this.add(getJRadioButtonA());
		this.add(getJRadioButtonH());
		this.add(getJRadioButtonTonUnsichtbar());
		this.add(getJPanelTonVz());
		this.add(getJPanelTonGeschlecht());
		this.add(getJPanelTonKlang());
	}
	
	public void setBounds(int x,
            int y,
            int width,
            int height)
	{
		super.setBounds(x,y,width,height);
		getJPanelTonKlang().setBounds(150, 0, 60, this.getHeight());
	}

	public APanel getJPanelTonKlang() {
		if (jPanelTonArtKlang == null) {
			jPanelTonArtKlang = new APanel();
			jPanelTonArtKlang.setLayout(null);
//			jPanelTonArtKlang.setBorder(new LineBorder(Color.black));
//			jPanelTonArtKlang.setBounds(150, 0, 60, 150);
			jPanelTonArtKlang.add(getJRadioButton6());
			jPanelTonArtKlang.add(getJRadioButton7());
			jPanelTonArtKlang.add(getJRadioButton9());
			jPanelTonArtKlang.add(getJRadioButton0());
			jPanelTonArtKlang.add(getJRadioButtonPlus());
			jPanelTonArtKlang.add(getJRadioButton75());
			jPanelTonArtKlang.add(getJRadioButtonMaj());
//			jPanelTonArtKlang.add(getJRadioButtonSus());
			jPanelTonArtKlang.add(getJRadioButton79());
			jPanelTonArtKlang.add(getJRadioButtonArtOhne());
			jPanelTonArtKlang.add(getJTextFieldEgal());
			jPanelTonArtKlang.add(getRadioButtonFrei());
			jPanelTonArtKlang.add(getJRadioButtonArtUnsichtbar());
		}
		return jPanelTonArtKlang;
	}
	
	public APanel getJPanelTonVz()
	{
		if (jPanelTonArtVz == null)
		{
			jPanelTonArtVz = new APanel();
			jPanelTonArtVz.setBounds(77, 0, 55, 60);
			jPanelTonArtVz.setLayout(null);
//			jPanelTonArtVz.setBorder(new LineBorder(Color.black));
			jPanelTonArtVz.add(getJRadioButtonKreuz());
			jPanelTonArtVz.add(getJRadioButtonb());
			jPanelTonArtVz.add(getJRadioButtonOhneVz());
			jPanelTonArtVz.add(getJRadioButtonVzUnsichtbar());
		}
		return jPanelTonArtVz;
	}
	
	public APanel getJPanelTonGeschlecht()
	{
		if (jPanelTonArtGeschlecht == null)
		{
			jPanelTonArtGeschlecht = new APanel();
			jPanelTonArtGeschlecht.setBounds(77, 82, 55, 45);
			jPanelTonArtGeschlecht.setLayout(null);
//			jPanelTonArtGeschlecht.setBorder(new LineBorder(Color.black));
			jPanelTonArtGeschlecht.add(getJRadioButtonDur());
			jPanelTonArtGeschlecht.add(getJRadioButtonMoll());
			jPanelTonArtGeschlecht.add(getJRadioButtonGeschlechtUnsichtbar());
		}
		return jPanelTonArtGeschlecht;
	}
	public JRadioButton getJRadioButtonC()
	{
		if (jRadioButtonC == null)
		{
			jRadioButtonC = new JRadioButton();
			jRadioButtonC.setText("C");
			jRadioButtonC.setFont(getFontA());
			jRadioButtonC.setBounds(5, yD+(0*yA), 35, 20);
			jRadioButtonC.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(jRadioButtonC.getText(), evt);}});
			jRadioButtonC.addKeyListener(keyListener);
		}
		return jRadioButtonC;
	}
	

	public JRadioButton getJRadioButtonD()
	{
		if (jRadioButtonD == null)
		{
			jRadioButtonD = new JRadioButton();
			jRadioButtonD.setText("D");
			jRadioButtonD.setFont(getFontA());
			jRadioButtonD.setBounds(5, yD+(1*yA), 35, 20);
			jRadioButtonD.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(jRadioButtonD.getText(), evt);}});
			jRadioButtonD.addKeyListener(keyListener);
		}
		return jRadioButtonD;
	}
	

	public JRadioButton getJRadioButtonE()
	{
		if (jRadioButtonE == null)
		{
			jRadioButtonE = new JRadioButton();
			jRadioButtonE.setText("E");
			jRadioButtonE.setFont(getFontA());
			jRadioButtonE.setBounds(5, yD+(2*yA), 35, 20);
			jRadioButtonE.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(jRadioButtonE.getText(), evt);}});
			jRadioButtonE.addKeyListener(keyListener);
		}
		return jRadioButtonE;
	}
	

	public JRadioButton getJRadioButtonF()
	{
		if (jRadioButtonF == null)
		{
			jRadioButtonF = new JRadioButton();
			jRadioButtonF.setText("F");
			jRadioButtonF.setFont(getFontA());
			jRadioButtonF.setBounds(5, yD+(3*yA), 35, 20);
			jRadioButtonF.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(jRadioButtonF.getText(), evt);}});
			jRadioButtonF.addKeyListener(keyListener);
	}
		return jRadioButtonF;
	}
	

	public JRadioButton getJRadioButtonG()
	{
		if (jRadioButtonG == null)
		{
			jRadioButtonG = new JRadioButton();
			jRadioButtonG.setText("G");
			jRadioButtonG.setFont(getFontA());
			jRadioButtonG.setBounds(5, yD+(4*yA), 35, 20);
			jRadioButtonG.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(jRadioButtonG.getText(), evt);}});
			jRadioButtonG.addKeyListener(keyListener);
		}
		return jRadioButtonG;
	}
	

	public JRadioButton getJRadioButtonA()
	{
		if (jRadioButtonA == null)
		{
			jRadioButtonA = new JRadioButton();
			jRadioButtonA.setText("A");
			jRadioButtonA.setFont(getFontA());
			jRadioButtonA.setBounds(5, yD+(5*yA), 35, 20);
			jRadioButtonA.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(jRadioButtonA.getText(), evt);}});
			jRadioButtonA.addKeyListener(keyListener);
		}
		return jRadioButtonA;
	}
	

	public JRadioButton getJRadioButtonH()
	{
		if (jRadioButtonH == null)
		{
			jRadioButtonH = new JRadioButton();
			jRadioButtonH.setText(Konfiguration.getInstance().getBTonPanel());
			jRadioButtonH.setFont(getFontA());
			jRadioButtonH.setBounds(5, yD+(6*yA), 35, 20);
			jRadioButtonH.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTon(Konfiguration.getInstance().getBTonAkkord(), evt);}});
			jRadioButtonH.addKeyListener(keyListener);
		}
		return jRadioButtonH;
	}
	

	public JRadioButton getJRadioButtonKreuz()
	{
		if (jRadioButtonKreuz == null)
		{
			jRadioButtonKreuz = new JRadioButton();
			jRadioButtonKreuz.setText("#");
			jRadioButtonKreuz.setFont(getFontA());
			jRadioButtonKreuz.setBounds(0, yD+(0*yA), 35, 20);
			jRadioButtonKreuz.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonHalbTon(jRadioButtonKreuz.getText(), evt);}});
			jRadioButtonKreuz.addKeyListener(keyListener);
		}
		return jRadioButtonKreuz;
	}
	

	public JRadioButton getJRadioButtonb()
	{
		if (jRadioButtonb == null)
		{
			jRadioButtonb = new JRadioButton();
			jRadioButtonb.setText("b");
			jRadioButtonb.setFont(getFontA());
			jRadioButtonb.setBounds(0, yD+(1*yA), 35, 20);
			jRadioButtonb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonHalbTon(jRadioButtonb.getText(), evt);}});
			jRadioButtonb.addKeyListener(keyListener);
		}
		return jRadioButtonb;
	}
	

	public JRadioButton getJRadioButtonOhneVz()
	{
		if (jRadioButtonOhneVz == null)
		{
			jRadioButtonOhneVz = new JRadioButton();
			jRadioButtonOhneVz.setText("ohne");
			jRadioButtonOhneVz.setFont(getFontA());
			jRadioButtonOhneVz.setBounds(0, yD+(2*yA), 55, 20);
			jRadioButtonOhneVz.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonHalbTon(jRadioButtonOhneVz.getText(), evt);}});
			jRadioButtonOhneVz.addKeyListener(keyListener);
		}
		return jRadioButtonOhneVz;
	}
	
	
	public JRadioButton getJRadioButtonMaj() 
	{
		if (jRadioButtonMaj == null) 
		{
			jRadioButtonMaj = new JRadioButton();
			jRadioButtonMaj.setText("maj");
			jRadioButtonMaj.setFont(getFontA());
			jRadioButtonMaj.setBounds(0, yD+(0*yA), 55, 20);
			jRadioButtonMaj.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButtonMaj.getText(),evt);}});
			jRadioButtonMaj.addKeyListener(keyListener);
		}
		return jRadioButtonMaj;
	}
	public JRadioButton getJRadioButton6() 
	{
		if (jRadioButton6 == null) 
		{
			jRadioButton6 = new JRadioButton();
			jRadioButton6.setText("6");
			jRadioButton6.setFont(getFontA());
			jRadioButton6.setBounds(0, yD+(1*yA), 45, 20);
			jRadioButton6.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButton6.getText(),evt);}});
			jRadioButton6.addKeyListener(keyListener);
		}
		return jRadioButton6;
	}
	public JRadioButton getJRadioButton7() 
	{
		if (jRadioButton7 == null) 
		{
			jRadioButton7 = new JRadioButton();
			jRadioButton7.setText("7");
			jRadioButton7.setFont(getFontA());
			jRadioButton7.setBounds(0, yD+(2*yA), 45, 20);
			jRadioButton7.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButton7.getText(),evt);}});
			jRadioButton7.addKeyListener(keyListener);
		}
		return jRadioButton7;
	}
	public JRadioButton getJRadioButton9() 
	{
		if (jRadioButton9 == null) 
		{
			jRadioButton9 = new JRadioButton();
			jRadioButton9.setText("9");
			jRadioButton9.setFont(getFontA());
			jRadioButton9.setBounds(0, yD+(3*yA), 45, 20);
			jRadioButton9.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButton9.getText(),evt);}});
			jRadioButton9.addKeyListener(keyListener);
		}
		return jRadioButton9;
	}
	public JRadioButton getJRadioButton0() 
	{
		if (jRadioButton0 == null) 
		{
			jRadioButton0 = new JRadioButton();
			jRadioButton0.setText("o");
			jRadioButton0.setFont(getFontA());
			jRadioButton0.setBounds(0, yD+(4*yA), 45, 20);
			jRadioButton0.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButton0.getText(),evt);}});
			jRadioButton0.addKeyListener(keyListener);
		}
		return jRadioButton0;
	}
	public JRadioButton getJRadioButton75() 
	{
		if (jRadioButton75 == null) 
		{
			jRadioButton75 = new JRadioButton();
			jRadioButton75.setText("7/5");
			jRadioButton75.setFont(getFontA());
			jRadioButton75.setBounds(0, yD+(5*yA), 45, 20);
			jRadioButton75.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButton75.getText(),evt);}});
			jRadioButton75.addKeyListener(keyListener);
		}
		return jRadioButton75;
	}
	public JRadioButton getJRadioButton79() 
	{
		if (jRadioButton79 == null) 
		{
			jRadioButton79 = new JRadioButton();
			jRadioButton79.setText("7/9");
			jRadioButton79.setFont(getFontA());
			jRadioButton79.setBounds(0, yD+(6*yA), 55, 20);
			jRadioButton79.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButton79.getText(),evt);}});
			jRadioButton79.addKeyListener(keyListener);
		}
		return jRadioButton79;
	}
	public JRadioButton getJRadioButtonPlus() 
	{
		if (jRadioButtonPlus == null) 
		{
			jRadioButtonPlus = new JRadioButton();
			jRadioButtonPlus.setText("+");
			jRadioButtonPlus.setFont(getFontA());
			jRadioButtonPlus.setBounds(0, yD+(7*yA), 45, 20);
			jRadioButtonPlus.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButtonPlus.getText(),evt);}});
			jRadioButtonPlus.addKeyListener(keyListener);
		}
		return jRadioButtonPlus;
	}
	public JRadioButton getRadioButtonFrei()
	{
		if (jRadioButtonFrei == null)
		{
			jRadioButtonFrei = new JRadioButton();
			jRadioButtonFrei.setText("frei:");
			jRadioButtonFrei.setFont(getFontB());
			jRadioButtonFrei.setBounds(0,
								  getJTextFieldEgal().getY()-20,
								  50,20);
			jRadioButtonFrei.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonFrei(evt);}});
		}
		return jRadioButtonFrei;
	}
	public JTextField getJTextFieldEgal()
	{
		if (jTextFieldArtEgal == null)
		{
			jTextFieldArtEgal = new JTextField();
			jTextFieldArtEgal.setText("");
			jTextFieldArtEgal.setFont(getFontA());
			jTextFieldArtEgal.setBounds(5,yD+(9*yA+33), 45, 20);
			jTextFieldArtEgal.setBackground(getJPanelTonKlang().getBackground());
			PlainDocument docTitel = new PlainDocument();
			jTextFieldArtEgal.setDocument(docTitel);
			docTitel.addDocumentListener( new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { onTextFieldEgal(); }
				public void removeUpdate(DocumentEvent e) { onTextFieldEgal(); }
				public void	changedUpdate(DocumentEvent e) {  }
			});

		}
		return jTextFieldArtEgal;
	}
	public JRadioButton getJRadioButtonArtOhne() 
	{
		if (jRadioButtonArtOhne == null) 
		{
			jRadioButtonArtOhne = new JRadioButton();
			jRadioButtonArtOhne.setText("ohne");
			jRadioButtonArtOhne.setFont(getFontA());
			jRadioButtonArtOhne.setBounds(0, yD+(8*yA+3), 55, 20);
			jRadioButtonArtOhne.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					onRadioButtonArt(jRadioButtonArtOhne.getText(),evt);}});
			jRadioButtonArtOhne.addKeyListener(keyListener);
		}
		return jRadioButtonArtOhne;
	}
	public JRadioButton getJRadioButtonArtUnsichtbar() 
	{
		if (jRadioButtonArtUnsichtbar == null) 
		{
			jRadioButtonArtUnsichtbar = new JRadioButton();
			jRadioButtonArtUnsichtbar.setBounds(0, 0, 20, 20);
			jRadioButtonArtUnsichtbar.setVisible(false);
			jRadioButtonArtUnsichtbar.addKeyListener(keyListener);
		}
		return jRadioButtonArtUnsichtbar;
	}
	
	public JRadioButton getJRadioButtonVzUnsichtbar()
	{
		if (jRadioButtonVzUnsichtbar == null)
		{
			jRadioButtonVzUnsichtbar = new JRadioButton();
			jRadioButtonVzUnsichtbar.setBounds(0, 0, 20, 20);
			jRadioButtonVzUnsichtbar.setVisible(false);
			jRadioButtonVzUnsichtbar.addKeyListener(keyListener);
		}
		return jRadioButtonVzUnsichtbar;
	}
	public JRadioButton getJRadioButtonTonUnsichtbar()
	{
		if (jRadioButtonTonUnsichtbar == null)
		{
			jRadioButtonTonUnsichtbar = new JRadioButton();
			jRadioButtonTonUnsichtbar.setBounds(0, 0, 20, 20);
			jRadioButtonTonUnsichtbar.setVisible(false);
			jRadioButtonTonUnsichtbar.addKeyListener(keyListener);
		}
		return jRadioButtonTonUnsichtbar;
	}
	public JRadioButton getJRadioButtonGeschlechtUnsichtbar()
	{
		if (jRadioButtonGeschlechtUnsichtbar == null)
		{
			jRadioButtonGeschlechtUnsichtbar = new JRadioButton();
			jRadioButtonGeschlechtUnsichtbar.setBounds(0, 0, 20, 20);
			jRadioButtonGeschlechtUnsichtbar.setVisible(false);
			jRadioButtonGeschlechtUnsichtbar.addKeyListener(keyListener);
		}
		return jRadioButtonGeschlechtUnsichtbar;
	}

	public JRadioButton getJRadioButtonDur()
	{
		if (jRadioButtonDur == null)
		{
			jRadioButtonDur = new JRadioButton();
			jRadioButtonDur.setText("Dur");
			jRadioButtonDur.setFont(getFontA());
			jRadioButtonDur.setBounds(0, yD+(0*yA), 65, 20);
			jRadioButtonDur.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTonGeschlecht(jRadioButtonDur.getText(), evt);}});
			jRadioButtonDur.addKeyListener(keyListener);
		}
		return jRadioButtonDur;
	}

	public JRadioButton getJRadioButtonMoll()
	{
		if (jRadioButtonMoll == null)
		{
			jRadioButtonMoll = new JRadioButton();
			jRadioButtonMoll.setText("Moll");
			jRadioButtonMoll.setFont(getFontA());
			jRadioButtonMoll.setBounds(0, yD+(1*yA), 65, 20);
			jRadioButtonMoll.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					parent.onRadioButtonTonGeschlecht(jRadioButtonMoll.getText(), evt);}});
			jRadioButtonMoll.addKeyListener(keyListener);
		}
		return jRadioButtonMoll;
	}

	private void myInit()
	{
		
	}
	
	/**
	 * 
	 */
	public void setInVisisbleKlang()
	{
		for (int i=0;i<getJPanelTonKlang().getComponentCount();i++)
		{
			Component com = getJPanelTonKlang().getComponent(i);
			com.setVisible(false);
		}
	}
	
	/**
	 * @param ist
	 */
	public void setButtonsEnabled(boolean ist)
	{
		getJRadioButtonC().setEnabled(ist);
		getJRadioButtonD().setEnabled(ist);
		getJRadioButtonE().setEnabled(ist);
		getJRadioButtonF().setEnabled(ist);
		getJRadioButtonG().setEnabled(ist);
		getJRadioButtonA().setEnabled(ist);
		getJRadioButtonH().setEnabled(ist);
		for (int i=0;i<getJPanelTonKlang().getComponentCount();i++)
		{
			Component com = getJPanelTonKlang().getComponent(i);
			com.setEnabled(ist);
		}
		for (int i=0;i<getJPanelTonVz().getComponentCount();i++)
		{
			Component com = getJPanelTonVz().getComponent(i);
			com.setEnabled(ist);
		}
		for (int i=0;i<getJPanelTonGeschlecht().getComponentCount();i++)
		{
			Component com = getJPanelTonGeschlecht().getComponent(i);
			com.setEnabled(ist);
		}
	}
	
	public void handleKeyEvent(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_C:
				getJRadioButtonC().doClick();
				break;
			case KeyEvent.VK_D:
				getJRadioButtonD().doClick();
				break;
			case KeyEvent.VK_E:
				getJRadioButtonE().doClick();
				break;
			case KeyEvent.VK_F:
				getJRadioButtonF().doClick();
				break;
			case KeyEvent.VK_G:
				getJRadioButtonG().doClick();
				break;
			case KeyEvent.VK_A:
				getJRadioButtonA().doClick();
				break;
			case KeyEvent.VK_H:
				getJRadioButtonH().doClick();
				break;
			case KeyEvent.VK_B:
				if (getJRadioButtonb().isEnabled())
				{
					if (getJRadioButtonb().isSelected())
						getJRadioButtonOhneVz().doClick();
					else
						getJRadioButtonb().doClick();
				}
				break;
			case KeyEvent.VK_K:
			case KeyEvent.VK_NUMBER_SIGN:
				if (getJRadioButtonKreuz().isEnabled())
				{
					if (getJRadioButtonKreuz().isSelected())
						getJRadioButtonOhneVz().doClick();
					else
						getJRadioButtonKreuz().doClick();
				}
				break;
			case KeyEvent.VK_M:
				if (getJRadioButtonMoll().isSelected())
					getJRadioButtonDur().doClick();
				else
					getJRadioButtonMoll().doClick();
				break;
			case KeyEvent.VK_J:
				if (getJRadioButtonMaj().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButtonMaj().doClick();
				break;
			case KeyEvent.VK_6:
				if (getJRadioButton6().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButton6().doClick();
				break;
			case KeyEvent.VK_7:
				if (getJRadioButton7().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButton7().doClick();
				break;
			case KeyEvent.VK_9:
				if (getJRadioButton9().isSelected())
					getJRadioButton79().doClick();
				else if (getJRadioButton79().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButton9().doClick();
				break;
			case KeyEvent.VK_0:
				if (getJRadioButton0().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButton0().doClick();
				break;
			case KeyEvent.VK_PLUS:
				if (getJRadioButtonPlus().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButtonPlus().doClick();
				break;
			case KeyEvent.VK_5:
				if (getJRadioButton75().isSelected())
					getJRadioButtonArtOhne().doClick();
				else
					getJRadioButton75().doClick();
				break;

			default:
				break;
		}
	}
	
	public void onKeyEvent(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_T:
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
				((PanelReiter3Takt)parent).handleKeyEvent(e);
				break;
				
			default:
				handleKeyEvent(e);
				break;
			}
	}
	
	public void onRadioButtonFrei(ItemEvent evt)
	{
		if (evt.getStateChange() == ItemEvent.SELECTED)
		{
			getJTextFieldEgal().setEnabled(true);
			parent.onRadioButtonTonKlang(getJTextFieldEgal().getText(), null);
			getJTextFieldEgal().setBackground(Color.white);
			getJTextFieldEgal().requestFocus();
		}
		else
		{
			getJTextFieldEgal().setEnabled(false);
			getJTextFieldEgal().setBackground(getJPanelTonKlang().getBackground());
		}
	}

	private void onTextFieldEgal()
	{
		if (onRadioButtonClick)
			return;
		
		if (getJTextFieldEgal().getText().length() > 0)
		{
			parent.onRadioButtonTonKlang(getJTextFieldEgal().getText(), null);
			getJTextFieldEgal().setBackground(Color.white);
		}
		else
		{
			getJRadioButtonArtOhne().doClick();
			getJRadioButtonArtOhne().requestFocus();
			getJTextFieldEgal().setBackground(getJPanelTonKlang().getBackground());
		}
	}
	
	public void setOnVonAussen(boolean ist)
	{
		onRadioButtonClick = ist;
	}
	
	private void onRadioButtonArt(final String klang, ItemEvent evt)
	{
		parent.onRadioButtonTonKlang(klang,evt);
		onRadioButtonClick = true;
		//System.out.println("## "+klang);
		getJTextFieldEgal().setText("");
		onRadioButtonClick = false;
	}


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
