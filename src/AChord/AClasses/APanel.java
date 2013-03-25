/*
 * 25.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * 
 * Methoden für alle Tatktus-Panel
 * 
 * 
 */
package AChord.AClasses;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import AChord.Hilfe.Konfiguration;


public class APanel extends JPanel  
{
	Border border;
	Border altBorder;
	ImageIcon	 m_image = null;
	
	boolean istAenderungErfolgt = false;
	
	static boolean istMarkierungErlaubt = true;

	/**
	 * 
	 */
	public APanel()
	{
		super();
		border = new LineBorder(Color.BLACK, 1);
		altBorder = new LineBorder(Color.BLACK, 1);
	}
	
	/**
	 * markieren, wenn mit Maus der Rahmen über diesen Takt gezogen wird
	 */
	public void setMausRahmenSelektierung()
	{
		this.setBorder(new LineBorder(Color.red,3));
	}
	
	/**
	 * resetten der selektierten Takte
	 */
	public void resetMausRahmenSelektierung()
	{
		this.setBorder(altBorder);
	}
	
	/**
	 * dieser Takt wird bearbeitet
	 */
	public void setMarkierungsrahmen()
	{
		this.border = new LineBorder(Color.BLUE,3);
		altBorder = new LineBorder(Color.BLUE,3);
	}

	/**
	 * auf diesem Takt befindet sich die Maus
	 */
	public void setMausMarkierungsrahmen()
	{
		this.border = new LineBorder(Color.CYAN,3);
		altBorder = new LineBorder(Color.CYAN,3);
	}
	
	/**
	 * 
	 */
	public void setNormalenRahmen()
	{
		border = new LineBorder(Color.BLACK, 1);
		altBorder = new LineBorder(Color.BLACK, 1);
	}
	
	/**
	 * 
	 */
	public void setRahmen()
	{
		this.setBorder(border);
	}
	
	/**
	 * 
	 */
	public void resetRahmen()
	{
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		altBorder = new EmptyBorder(0, 0, 0, 0);
	}
	
	ButtonGroup  m_ButtonGroup = null;

	public Component add(Component comp)
	{
		if (comp instanceof JRadioButton)
		{
			if (m_ButtonGroup == null)
				m_ButtonGroup = new ButtonGroup();
			m_ButtonGroup.add((AbstractButton) comp);
		}
		Component co = super.add(comp);
		return co;
	}
	public void add(Component comp, Object constraints)
	{
		if (comp instanceof JRadioButton)
		{
			if (m_ButtonGroup == null)
				m_ButtonGroup = new ButtonGroup();
			m_ButtonGroup.add((AbstractButton) comp);
		}
		super.add(comp, constraints);
	}
	public void remove(Component comp)
	{
		if (comp instanceof JRadioButton)
		{
			if (m_ButtonGroup != null)
				m_ButtonGroup.remove((AbstractButton) comp);
		}
		super.remove(comp);
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
		return new java.awt.Font(Konfiguration.getInstance().getSchriftArt(),Font.PLAIN,22);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (m_image != null)
			g.drawImage(m_image.getImage(), 0, 0, null);
	}

	public void setImage(ImageIcon image)
	{
		m_image = image;
		if (m_image != null)
			m_image.setImage(m_image.getImage().getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT));
		repaint();
	}

//	public void onRadioButtonTon(final String ton, ItemEvent evt)
//	{
//		
//	}
//	public void onKeyEvent(KeyEvent e)
//	{
//		
//	}
//	public void onRadioButtonHalbTon(final String halbTon, ItemEvent evt)
//	{
//		
//	}
//	public void onRadioButtonTonGeschlecht(final String geschlecht, ItemEvent evt)
//	{
//		
//	}
//	public void onRadioButtonTonKlang(final String klang, ItemEvent evt) 
//	{
//		
//	}

	/**
	 * @return the istAenderungErfolgt
	 */
	public boolean istAenderungErfolgt()
	{
		return istAenderungErfolgt;
	}

	/**
	 * @param istAenderungErfolgt the istAenderungErfolgt to set
	 */
	public void setIstAenderungErfolgt(boolean istAenderungErfolgt)
	{
		this.istAenderungErfolgt = istAenderungErfolgt;
	}
	
	/**
	 * 
	 */
	public void setAenderungIstErfolgt()
	{
		this.istAenderungErfolgt = true;
	}
	
	/**
	 * 
	 */
	public void resetAenderungIstErfolgt()
	{
		this.istAenderungErfolgt = false;
	}


	
	
	public void aPanelMouseDragged(MouseEvent e)
	{
	}
	public void aPanelMouseClicked(MouseEvent e)
	{
	}
	public void aPanelMouseExited(MouseEvent e)
	{
	}
	public void aPanelMouseEntered(MouseEvent e)
	{
	}
	public void aPanelMousePressed(MouseEvent e)
	{
	}
	public void aPanelMouseReleased(MouseEvent e)
	{
	}
	
	public class MyMouseMotionListener extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e)
	    {
			if (istMarkierungErlaubt)
				aPanelMouseDragged(e);
	    }
	}
	public class MyMouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e) 
		{
			if (istMarkierungErlaubt)
				aPanelMousePressed(e);
		}
		public void mouseReleased(MouseEvent e) 
		{
			if (istMarkierungErlaubt)
				aPanelMouseReleased(e);
		}
		public void mouseEntered(MouseEvent e) 
		{
			if (istMarkierungErlaubt)
				aPanelMouseEntered(e);
		}
		public void mouseExited(MouseEvent e) 
		{
			if (istMarkierungErlaubt)
				aPanelMouseExited(e);
		}
		public void mouseClicked(MouseEvent e) 
		{
			if (istMarkierungErlaubt)
				aPanelMouseClicked(e);
		}
	}
	/**
	 * @return the altBorder
	 */
	public Border getAltBorder()
	{
		return altBorder;
	}

	/**
	 * @param altBorder the altBorder to set
	 */
	public void setAltBorder(Border altBorder)
	{
		this.altBorder = altBorder;
	}

	/**
	 * @return
	 */
	public boolean isIstMarkierungErlaubt() 
	{
		return istMarkierungErlaubt;
	}

	/**
	 * @param istMarkierungErlaubt
	 */
	public static void setIstMarkierungErlaubt(boolean ist) 
	{
		istMarkierungErlaubt = ist;
	}

}
