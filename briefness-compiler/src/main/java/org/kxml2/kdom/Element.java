// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.kdom;

import java.io.IOException;
import java.util.Vector;
import org.xmlpull.v1.*;

// Referenced classes of package org.kxml2.kdom:
//			Node

public class Element extends Node
{

	protected String namespace;
	protected String name;
	protected Vector attributes;
	protected Node parent;
	protected Vector prefixes;

	public Element()
	{
	}

	public void init()
	{
	}

	public void clear()
	{
		attributes = null;
		children = null;
	}

	public Element createElement(String s, String s1)
	{
		return parent != null ? parent.createElement(s, s1) : super.createElement(s, s1);
	}

	public int getAttributeCount()
	{
		return attributes != null ? attributes.size() : 0;
	}

	public String getAttributeNamespace(int i)
	{
		return ((String[])(String[])attributes.elementAt(i))[0];
	}

	public String getAttributeName(int i)
	{
		return ((String[])(String[])attributes.elementAt(i))[1];
	}

	public String getAttributeValue(int i)
	{
		return ((String[])(String[])attributes.elementAt(i))[2];
	}

	public String getAttributeValue(String s, String s1)
	{
		for (int i = 0; i < getAttributeCount(); i++)
			if (s1.equals(getAttributeName(i)) && (s == null || s.equals(getAttributeNamespace(i))))
				return getAttributeValue(i);

		return null;
	}

	public Node getRoot()
	{
		Element element;
		for (element = this; element.parent != null; element = (Element)element.parent)
			if (!(element.parent instanceof Element))
				return element.parent;

		return element;
	}

	public String getName()
	{
		return name;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public String getNamespaceUri(String s)
	{
		int i = getNamespaceCount();
		for (int j = 0; j < i; j++)
			if (s == getNamespacePrefix(j) || s != null && s.equals(getNamespacePrefix(j)))
				return getNamespaceUri(j);

		return (parent instanceof Element) ? ((Element)parent).getNamespaceUri(s) : null;
	}

	public int getNamespaceCount()
	{
		return prefixes != null ? prefixes.size() : 0;
	}

	public String getNamespacePrefix(int i)
	{
		return ((String[])(String[])prefixes.elementAt(i))[0];
	}

	public String getNamespaceUri(int i)
	{
		return ((String[])(String[])prefixes.elementAt(i))[1];
	}

	public Node getParent()
	{
		return parent;
	}

	public void parse(XmlPullParser xmlpullparser)
		throws IOException, XmlPullParserException
	{
		for (int i = xmlpullparser.getNamespaceCount(xmlpullparser.getDepth() - 1); i < xmlpullparser.getNamespaceCount(xmlpullparser.getDepth()); i++)
			setPrefix(xmlpullparser.getNamespacePrefix(i), xmlpullparser.getNamespaceUri(i));

		for (int j = 0; j < xmlpullparser.getAttributeCount(); j++)
			setAttribute(xmlpullparser.getAttributeNamespace(j), xmlpullparser.getAttributeName(j), xmlpullparser.getAttributeValue(j));

		init();
		if (xmlpullparser.isEmptyElementTag())
		{
			xmlpullparser.nextToken();
		} else
		{
			xmlpullparser.nextToken();
			super.parse(xmlpullparser);
			if (getChildCount() == 0)
				addChild(7, "");
		}
		xmlpullparser.require(3, getNamespace(), getName());
		xmlpullparser.nextToken();
	}

	public void setAttribute(String s, String s1, String s2)
	{
		if (attributes == null)
			attributes = new Vector();
		if (s == null)
			s = "";
		for (int i = attributes.size() - 1; i >= 0; i--)
		{
			String as[] = (String[])(String[])attributes.elementAt(i);
			if (as[0].equals(s) && as[1].equals(s1))
			{
				if (s2 == null)
					attributes.removeElementAt(i);
				else
					as[2] = s2;
				return;
			}
		}

		attributes.addElement(new String[] {
			s, s1, s2
		});
	}

	public void setPrefix(String s, String s1)
	{
		if (prefixes == null)
			prefixes = new Vector();
		prefixes.addElement(new String[] {
			s, s1
		});
	}

	public void setName(String s)
	{
		name = s;
	}

	public void setNamespace(String s)
	{
		if (s == null)
		{
			throw new NullPointerException("Use \"\" for empty namespace");
		} else
		{
			namespace = s;
			return;
		}
	}

	protected void setParent(Node node)
	{
		parent = node;
	}

	public void write(XmlSerializer xmlserializer)
		throws IOException
	{
		if (prefixes != null)
		{
			for (int i = 0; i < prefixes.size(); i++)
				xmlserializer.setPrefix(getNamespacePrefix(i), getNamespaceUri(i));

		}
		xmlserializer.startTag(getNamespace(), getName());
		int j = getAttributeCount();
		for (int k = 0; k < j; k++)
			xmlserializer.attribute(getAttributeNamespace(k), getAttributeName(k), getAttributeValue(k));

		writeChildren(xmlserializer);
		xmlserializer.endTag(getNamespace(), getName());
	}
}
