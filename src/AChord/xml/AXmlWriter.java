/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 10.09.2007 
 *
 * Udo Simon
 * 
 * Beschreibung:
 * allgemeine Methoden zum schreiben einer Xml-Datei
 * 
 */
package AChord.xml;

import java.io.File;
import java.io.FileWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class AXmlWriter
{
	private File xmlFile;
    private Document document = null;
    
	/**
	 * Konstrutor
	 * @param xmlFile
	 */
	public AXmlWriter(File xmlFile)
	{
		this.xmlFile = xmlFile;
		document = DocumentHelper.createDocument();
	}
	
	/**
	 * Konstruktor
	 * @param xmlFile
	 * @param doc
	 */
	public AXmlWriter(File xmlFile, Document doc)
	{
		this.xmlFile = xmlFile;
		if (doc != null)
			document = doc;
		else 
			document = DocumentHelper.createDocument();
	}

	/**
	 * setzen des Xml-Roots
	 * @param rootName
	 * @return
	 */
	public Element addRoot(String rootName)
    {
        Element root = document.addElement(rootName);
        return root;
    }
	
    /**
     * hinzuügen eines Elements zu einem Root
     * @param root
     * @param child
     */
    public void addElement2Element(Element root, Element child)
    {
    	root.add(child);
    }
    
    /**
     * Kommentar hinzufügen
     * @param st
     */
    public void addComment(String st)
    {
    	document.addComment(st);
    }
    
    /**
     * eigentliches Schreiben in die Xml-Datei
     */
    public void write()
    {
    	if (document != null)
    	{
			try {
		        // schreiben in Datei
				OutputFormat format = new OutputFormat(" ", true, "ISO-8859-1");
		        XMLWriter writer = new XMLWriter(new FileWriter( xmlFile), format);
		        writer.write( document );
		        writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
        // outputToSystemOut();
    }

    /**
     * Debugging
     */
    private void outputToSystemOut()
	{
    	if (document != null)
    	{
			try {
				OutputFormat format = OutputFormat.createPrettyPrint();
		        
				XMLWriter writer;
		        writer = new XMLWriter( System.out, format );
		        writer.write( document );
			} catch (Exception e) {
			}
    	}
	}
    
	/**
	 * @return
	 */
	public Document getDocument() 
	{
		return document;
	}
	
	/**
	 * @param document
	 */
	public void setDocument(Document document) 
	{
		this.document = document;
	}

}
