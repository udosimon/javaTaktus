/*
 * TAKTUS
 * 
 * 11.09.2007
 *
 * Udo Simon
 * 
 * Beschreibung:
 *
 */
package AChord.Print;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

import javax.swing.RepaintManager;

public class PrintUtility implements Printable 
{	
	Component componentToBePrinted;
	private Vector vComponents = new Vector();
	
	/**
	 * @param c
	 */
	public static void printComponent(Component c) 
	{
//		vComponents.add(c);
		new PrintUtility().print();
	}
	
	/**
	 * @param componentToBePrinted
	 */
	public PrintUtility() 
	{
	}
	
	/**
	 * @param componentToBePrinted
	 */
	public void addComponentToPrint(Component componentToBePrinted)
	{
		vComponents.add(componentToBePrinted);
	}
	
	/**
	 * 
	 */
	public void clearComponentList()
	{
		//vComponents.clear();
		vComponents = new Vector();
	}
	
	public Vector getComponentList()
	{
		return vComponents;
	}
	
	/**
	 * 
	 */
	public void print() 
	{
//		PrinterJob printJob = PrinterJob.getPrinterJob();
//		PageFormat pf = printJob.defaultPage();
//		pf.setOrientation(PageFormat.PORTRAIT);
//		Paper pap = pf.getPaper();
//		pap.setImageableArea(0, 0, pap.getWidth(), pap.getHeight());
//		pf.setPaper(pap);
//		printJob.setPrintable(this,pf);
		
		Paper a4Paper = new java.awt.print.Paper(); 
		double paperWidth  =  8.26;
		double paperHeight = 11.69;
		a4Paper.setSize(paperWidth * 72.0, paperHeight * 72.0);
		double leftMargin   = 0.78; /* should be about 2cm */
		double rightMargin  = 0.78;
		double topMargin    = 0.78;
		double bottomMargin = 0.78;
		a4Paper.setImageableArea(leftMargin * 72.0, topMargin * 72.0,
		                         (paperWidth  - leftMargin - rightMargin)*72.0,
		                         (paperHeight - topMargin - bottomMargin)*72.0);

		PrinterJob printJob = PrinterJob.getPrinterJob();
//		PageFormat pf = printJob.defaultPage();
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT);
		pf.setPaper(a4Paper);
		printJob.setPrintable(this,pf);

//		printJob.setPrintable(this);
		if (printJob.printDialog())
		{
			try {

				for (int i=0;i<vComponents.size();i++)
				{
					componentToBePrinted = (Component)vComponents.elementAt(i);
					printJob.print();
				}
				
			} catch(PrinterException pe) {
				System.out.println("Fehler beim Drucken: " + pe);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pf, int pageIndex) 
	{	
//		pf.setOrientation(PageFormat.PORTRAIT);
		int response = NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
		
		// for faster printing, turn off double buffering
		disableDoubleBuffering(componentToBePrinted);
		
		Dimension d = componentToBePrinted.getSize(); //get size of document
		double panelWidth = d.width; //width in pixels
		double panelHeight = d.height; //height in pixels
		double pageHeight = pf.getImageableHeight(); //height of printer page
		double pageWidth = pf.getImageableWidth(); //width of printer page
		
		double scaleX = pageWidth / panelWidth;
		double scaleY = (pageHeight) / panelHeight;
		
		int totalNumPages = (int) Math.ceil(scaleY * panelHeight / pageHeight);
		
		// make sure not print empty pages
		if (pageIndex >= totalNumPages) {
			response = NO_SUCH_PAGE;
		} else {
			// shift Graphic to line up with beginning of print-imageable region
			g2.translate(pf.getImageableX(), pf.getImageableY());
			// shift Graphic to line up with beginning of next page to print
			g2.translate(0f, -pageIndex * pageHeight);
			// scale the page so the width fits...
			g2.scale(scaleX, scaleY);
			componentToBePrinted.paint(g2); //repaint the page for printing			
			response = Printable.PAGE_EXISTS;
		}
		enableDoubleBuffering(componentToBePrinted);
		g2.dispose();
		return response;
	}
	
	/**
	 * @param c
	 */
	public static void disableDoubleBuffering(Component c) 
	{
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}
	
	/**
	 * @param c
	 */
	public static void enableDoubleBuffering(Component c) 
	{
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
	
	
}