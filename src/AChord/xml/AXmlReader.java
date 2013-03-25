/*
 * TAKTUS
 * 
 * Copyright
 * Udo Simon
 * Dorfstr.36
 * 24220 Boksee
 * 
 * 30.08.2007
 *
 * Udo Simon
 * 
 * Beschreibung:
 * allgemeine Methoden zum Lesen einer XML-Datei
 */
package AChord.xml;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class AXmlReader
{
	private File xmlFile;
    private Document document = null;
    private Element root = null;
    
	/**
	 * Konstruktor
	 * @param xmlFile
	 */
	public AXmlReader(File xmlFile)
	{
		this.xmlFile = xmlFile;
	}
	
    /**
     * einlesen der Daten aus Xml-Datei
     * @return
     */
    public Document parse() 
    {
        SAXReader reader = new SAXReader();
        try 
        {
            document = reader.read(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
			document = null;
		}
        return document;
    }
    
    /**
     * holen eines Root-Elements
     * @return
     */
    public Element getRoot()
    {
    	if (document != null)
    		root = document.getRootElement();
    	else
    		root = null;
    	return root;
    }
    
    /**
     * holen der Elemente, die zu einem bestimmte Root gehören
     * @param root
     * @param filter
     * @return
     */
    public Vector getElementList(Element root, String filter) 
    {
    	Vector elemente = new Vector();
    	if (document != null)
    	{
		    Iterator i; 
		    if (filter == null || filter.length() == 0)
		    	i = root.elementIterator();
		    else
		    	i = root.elementIterator(filter);
		    	
		    // iterate through child elements of root
		    while (i.hasNext()) 
		    {
		    	Element e = (Element) i.next();
		    	elemente.add(e);	
		   	}
    	}
    	return elemente;
    }
    
	/**
	 * @return
	 */
	public Document getDocument() 
	{
		return document;
	}

}
