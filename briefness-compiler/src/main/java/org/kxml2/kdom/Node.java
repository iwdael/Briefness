// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.kdom;

import java.io.IOException;
import java.util.Vector;
import org.xmlpull.v1.*;

// Referenced classes of package org.kxml2.kdom:
//			Element

public class Node
{

	public static final int DOCUMENT = 0;
	public static final int ELEMENT = 2;
	public static final int TEXT = 4;
	public static final int CDSECT = 5;
	public static final int ENTITY_REF = 6;
	public static final int IGNORABLE_WHITESPACE = 7;
	public static final int PROCESSING_INSTRUCTION = 8;
	public static final int COMMENT = 9;
	public static final int DOCDECL = 10;
	protected Vector children;
	protected StringBuffer types;

	public Node()
	{
	}

	public void addChild(int i, int j, Object obj)
	{
		if (obj == null)
			throw new NullPointerException();
		if (children == null)
		{
			children = new Vector();
			types = new StringBuffer();
		}
		if (j == 2)
		{
			if (!(obj instanceof Element))
				throw new RuntimeException("Element obj expected)");
			((Element)obj).setParent(this);
		} else
		if (!(obj instanceof String))
			throw new RuntimeException("String expected");
		children.insertElementAt(obj, i);
		types.insert(i, (char)j);
	}

	public void addChild(int i, Object obj)
	{
		addChild(getChildCount(), i, obj);
	}

	public Element createElement(String s, String s1)
	{
		Element element = new Element();
		element.namespace = s != null ? s : "";
		element.name = s1;
		return element;
	}

	public Object getChild(int i)
	{
		return children.elementAt(i);
	}

	public int getChildCount()
	{
		return children != null ? children.size() : 0;
	}

	public Element getElement(int i)
	{
		Object obj = getChild(i);
		return (obj instanceof Element) ? (Element)obj : null;
	}

	public Element getElement(String s, String s1)
	{
		int i = indexOf(s, s1, 0);
		int j = indexOf(s, s1, i + 1);
		if (i == -1 || j != -1)
			throw new RuntimeException("Element {" + s + "}" + s1 + (i != -1 ? " more than once in " : " not found in ") + this);
		else
			return getElement(i);
	}

	public String getText(int i)
	{
		return isText(i) ? (String)getChild(i) : null;
	}

	public int getType(int i)
	{
		return types.charAt(i);
	}

	public int indexOf(String s, String s1, int i)
	{
		int j = getChildCount();
		for (int k = i; k < j; k++)
		{
			Element element = getElement(k);
			if (element != null && s1.equals(element.getName()) && (s == null || s.equals(element.getNamespace())))
				return k;
		}

		return -1;
	}

	public boolean isText(int i)
	{
		int j = getType(i);
		return j == 4 || j == 7 || j == 5;
	}

	public void parse(XmlPullParser xmlpullparser)
		throws IOException, XmlPullParserException
	{
		boolean flag = false;
		do
		{
			int i = xmlpullparser.getEventType();
			switch (i)
			{
			case 2: // '\002'
				Element element = createElement(xmlpullparser.getNamespace(), xmlpullparser.getName());
				addChild(2, element);
				element.parse(xmlpullparser);
				break;

			case 1: // '\001'
			case 3: // '\003'
				flag = true;
				break;

			default:
				if (xmlpullparser.getText() != null)
					addChild(i != 6 ? i : 4, xmlpullparser.getText());
				else
				if (i == 6 && xmlpullparser.getName() != null)
					addChild(6, xmlpullparser.getName());
				xmlpullparser.nextToken();
				break;
			}
		} while (!flag);
	}

	public void removeChild(int i)
	{
		children.removeElementAt(i);
		int j = types.length() - 1;
		for (int k = i; k < j; k++)
			types.setCharAt(k, types.charAt(k + 1));

		types.setLength(j);
	}

	public void write(XmlSerializer xmlserializer)
		throws IOException
	{
		writeChildren(xmlserializer);
		xmlserializer.flush();
	}

	public void writeChildren(XmlSerializer xmlserializer)
		throws IOException
	{
		if (children == null)
			return;
		int i = children.size();
		for (int j = 0; j < i; j++)
		{
			int k = getType(j);
			Object obj = children.elementAt(j);
			switch (k)
			{
			case 2: // '\002'
				((Element)obj).write(xmlserializer);
				break;

			case 4: // '\004'
				xmlserializer.text((String)obj);
				break;

			case 7: // '\007'
				xmlserializer.ignorableWhitespace((String)obj);
				break;

			case 5: // '\005'
				xmlserializer.cdsect((String)obj);
				break;

			case 9: // '\t'
				xmlserializer.comment((String)obj);
				break;

			case 6: // '\006'
				xmlserializer.entityRef((String)obj);
				break;

			case 8: // '\b'
				xmlserializer.processingInstruction((String)obj);
				break;

			case 10: // '\n'
				xmlserializer.docdecl((String)obj);
				break;

			case 3: // '\003'
			default:
				throw new RuntimeException("Illegal type: " + k);
			}
		}

	}
}
