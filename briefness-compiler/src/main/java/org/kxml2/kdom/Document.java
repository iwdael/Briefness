// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.kdom;

import java.io.IOException;
import org.xmlpull.v1.*;

// Referenced classes of package org.kxml2.kdom:
//			Node, Element

public class Document extends Node
{

	protected int rootIndex;
	String encoding;
	Boolean standalone;

	public Document()
	{
		rootIndex = -1;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String s)
	{
		encoding = s;
	}

	public void setStandalone(Boolean boolean1)
	{
		standalone = boolean1;
	}

	public Boolean getStandalone()
	{
		return standalone;
	}

	public String getName()
	{
		return "#document";
	}

	public void addChild(int i, int j, Object obj)
	{
		if (j == 2)
			rootIndex = i;
		else
		if (rootIndex >= i)
			rootIndex++;
		super.addChild(i, j, obj);
	}

	public void parse(XmlPullParser xmlpullparser)
		throws IOException, XmlPullParserException
	{
		xmlpullparser.require(0, null, null);
		xmlpullparser.nextToken();
		encoding = xmlpullparser.getInputEncoding();
		standalone = (Boolean)xmlpullparser.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone");
		super.parse(xmlpullparser);
		if (xmlpullparser.getEventType() != 1)
			throw new RuntimeException("Document end expected!");
		else
			return;
	}

	public void removeChild(int i)
	{
		if (i == rootIndex)
			rootIndex = -1;
		else
		if (i < rootIndex)
			rootIndex--;
		super.removeChild(i);
	}

	public Element getRootElement()
	{
		if (rootIndex == -1)
			throw new RuntimeException("Document has no root element!");
		else
			return (Element)getChild(rootIndex);
	}

	public void write(XmlSerializer xmlserializer)
		throws IOException
	{
		xmlserializer.startDocument(encoding, standalone);
		writeChildren(xmlserializer);
		xmlserializer.endDocument();
	}
}
