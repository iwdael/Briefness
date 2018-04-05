// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.wap;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class WbxmlParser
	implements XmlPullParser
{

	static final String HEX_DIGITS = "0123456789abcdef";
	public static final int WAP_EXTENSION = 64;
	private static final String UNEXPECTED_EOF = "Unexpected EOF";
	private static final String ILLEGAL_TYPE = "Wrong event type";
	private InputStream in;
	private int TAG_TABLE;
	private int ATTR_START_TABLE;
	private int ATTR_VALUE_TABLE;
	private String attrStartTable[];
	private String attrValueTable[];
	private String tagTable[];
	private byte stringTable[];
	private Hashtable cacheStringTable;
	private boolean processNsp;
	private int depth;
	private String elementStack[];
	private String nspStack[];
	private int nspCounts[];
	private int attributeCount;
	private String attributes[];
	private int nextId;
	private Vector tables;
	private int version;
	private int publicIdentifierId;
	private String prefix;
	private String namespace;
	private String name;
	private String text;
	private Object wapExtensionData;
	private int wapCode;
	private int type;
	private boolean degenerated;
	private boolean isWhitespace;
	private String encoding;

	public WbxmlParser()
	{
		TAG_TABLE = 0;
		ATTR_START_TABLE = 1;
		ATTR_VALUE_TABLE = 2;
		cacheStringTable = null;
		elementStack = new String[16];
		nspStack = new String[8];
		nspCounts = new int[4];
		attributes = new String[16];
		nextId = -2;
		tables = new Vector();
	}

	public boolean getFeature(String s)
	{
		if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(s))
			return processNsp;
		else
			return false;
	}

	public String getInputEncoding()
	{
		return encoding;
	}

	public void defineEntityReplacementText(String s, String s1)
		throws XmlPullParserException
	{
	}

	public Object getProperty(String s)
	{
		return null;
	}

	public int getNamespaceCount(int i)
	{
		if (i > depth)
			throw new IndexOutOfBoundsException();
		else
			return nspCounts[i];
	}

	public String getNamespacePrefix(int i)
	{
		return nspStack[i << 1];
	}

	public String getNamespaceUri(int i)
	{
		return nspStack[(i << 1) + 1];
	}

	public String getNamespace(String s)
	{
		if ("xml".equals(s))
			return "http://www.w3.org/XML/1998/namespace";
		if ("xmlns".equals(s))
			return "http://www.w3.org/2000/xmlns/";
		for (int i = (getNamespaceCount(depth) << 1) - 2; i >= 0; i -= 2)
		{
			if (s == null)
			{
				if (nspStack[i] == null)
					return nspStack[i + 1];
				continue;
			}
			if (s.equals(nspStack[i]))
				return nspStack[i + 1];
		}

		return null;
	}

	public int getDepth()
	{
		return depth;
	}

	public String getPositionDescription()
	{
		StringBuffer stringbuffer = new StringBuffer(type >= XmlPullParser.TYPES.length ? "unknown" : XmlPullParser.TYPES[type]);
		stringbuffer.append(' ');
		if (type == 2 || type == 3)
		{
			if (degenerated)
				stringbuffer.append("(empty) ");
			stringbuffer.append('<');
			if (type == 3)
				stringbuffer.append('/');
			if (prefix != null)
				stringbuffer.append("{" + namespace + "}" + prefix + ":");
			stringbuffer.append(name);
			int i = attributeCount << 2;
			for (int j = 0; j < i; j += 4)
			{
				stringbuffer.append(' ');
				if (attributes[j + 1] != null)
					stringbuffer.append("{" + attributes[j] + "}" + attributes[j + 1] + ":");
				stringbuffer.append(attributes[j + 2] + "='" + attributes[j + 3] + "'");
			}

			stringbuffer.append('>');
		} else
		if (type != 7)
			if (type != 4)
				stringbuffer.append(getText());
			else
			if (isWhitespace)
			{
				stringbuffer.append("(whitespace)");
			} else
			{
				String s = getText();
				if (s.length() > 16)
					s = s.substring(0, 16) + "...";
				stringbuffer.append(s);
			}
		return stringbuffer.toString();
	}

	public int getLineNumber()
	{
		return -1;
	}

	public int getColumnNumber()
	{
		return -1;
	}

	public boolean isWhitespace()
		throws XmlPullParserException
	{
		if (type != 4 && type != 7 && type != 5)
			exception("Wrong event type");
		return isWhitespace;
	}

	public String getText()
	{
		return text;
	}

	public char[] getTextCharacters(int ai[])
	{
		if (type >= 4)
		{
			ai[0] = 0;
			ai[1] = text.length();
			char ac[] = new char[text.length()];
			text.getChars(0, text.length(), ac, 0);
			return ac;
		} else
		{
			ai[0] = -1;
			ai[1] = -1;
			return null;
		}
	}

	public String getNamespace()
	{
		return namespace;
	}

	public String getName()
	{
		return name;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public boolean isEmptyElementTag()
		throws XmlPullParserException
	{
		if (type != 2)
			exception("Wrong event type");
		return degenerated;
	}

	public int getAttributeCount()
	{
		return attributeCount;
	}

	public String getAttributeType(int i)
	{
		return "CDATA";
	}

	public boolean isAttributeDefault(int i)
	{
		return false;
	}

	public String getAttributeNamespace(int i)
	{
		if (i >= attributeCount)
			throw new IndexOutOfBoundsException();
		else
			return attributes[i << 2];
	}

	public String getAttributeName(int i)
	{
		if (i >= attributeCount)
			throw new IndexOutOfBoundsException();
		else
			return attributes[(i << 2) + 2];
	}

	public String getAttributePrefix(int i)
	{
		if (i >= attributeCount)
			throw new IndexOutOfBoundsException();
		else
			return attributes[(i << 2) + 1];
	}

	public String getAttributeValue(int i)
	{
		if (i >= attributeCount)
			throw new IndexOutOfBoundsException();
		else
			return attributes[(i << 2) + 3];
	}

	public String getAttributeValue(String s, String s1)
	{
		for (int i = (attributeCount << 2) - 4; i >= 0; i -= 4)
			if (attributes[i + 2].equals(s1) && (s == null || attributes[i].equals(s)))
				return attributes[i + 3];

		return null;
	}

	public int getEventType()
		throws XmlPullParserException
	{
		return type;
	}

	public int next()
		throws XmlPullParserException, IOException
	{
		isWhitespace = true;
		int i = 9999;
label0:
		do
		{
			String s;
			do
			{
				s = text;
				nextImpl();
				if (type < i)
					i = type;
			} while (i > 5);
			if (i < 4)
				break;
			if (s != null)
				text = text != null ? s + text : s;
			switch (peekId())
			{
			default:
				break label0;

			case 2: // '\002'
			case 3: // '\003'
			case 4: // '\004'
			case 68: // 'D'
			case 131: 
			case 132: 
			case 196: 
				break;
			}
		} while (true);
		type = i;
		if (type > 4)
			type = 4;
		return type;
	}

	public int nextToken()
		throws XmlPullParserException, IOException
	{
		isWhitespace = true;
		nextImpl();
		return type;
	}

	public int nextTag()
		throws XmlPullParserException, IOException
	{
		next();
		if (type == 4 && isWhitespace)
			next();
		if (type != 3 && type != 2)
			exception("unexpected type");
		return type;
	}

	public String nextText()
		throws XmlPullParserException, IOException
	{
		if (type != 2)
			exception("precondition: START_TAG");
		next();
		String s;
		if (type == 4)
		{
			s = getText();
			next();
		} else
		{
			s = "";
		}
		if (type != 3)
			exception("END_TAG expected");
		return s;
	}

	public void require(int i, String s, String s1)
		throws XmlPullParserException, IOException
	{
		if (i != type || s != null && !s.equals(getNamespace()) || s1 != null && !s1.equals(getName()))
			exception("expected: " + (i != 64 ? XmlPullParser.TYPES[i] + " {" + s + "}" + s1 : "WAP Ext."));
	}

	public void setInput(Reader reader)
		throws XmlPullParserException
	{
		exception("InputStream required");
	}

	public void setInput(InputStream inputstream, String s)
		throws XmlPullParserException
	{
		in = inputstream;
		try
		{
			version = readByte();
			publicIdentifierId = readInt();
			if (publicIdentifierId == 0)
				readInt();
			int i = readInt();
			if (null == s)
				switch (i)
				{
				case 4: // '\004'
					encoding = "ISO-8859-1";
					break;

				case 106: // 'j'
					encoding = "UTF-8";
					break;

				default:
					throw new UnsupportedEncodingException("" + i);
				}
			else
				encoding = s;
			int j = readInt();
			stringTable = new byte[j];
			int k = 0;
			do
			{
				if (k >= j)
					break;
				int l = inputstream.read(stringTable, k, j - k);
				if (l <= 0)
					break;
				k += l;
			} while (true);
			selectPage(0, true);
			selectPage(0, false);
		}
		catch (IOException ioexception)
		{
			exception("Illegal input format");
		}
	}

	public void setFeature(String s, boolean flag)
		throws XmlPullParserException
	{
		if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(s))
			processNsp = flag;
		else
			exception("unsupported feature: " + s);
	}

	public void setProperty(String s, Object obj)
		throws XmlPullParserException
	{
		throw new XmlPullParserException("unsupported property: " + s);
	}

	private final boolean adjustNsp()
		throws XmlPullParserException
	{
		boolean flag = false;
		for (int i = 0; i < attributeCount << 2; i += 4)
		{
			String s = attributes[i + 2];
			int l = s.indexOf(':');
			String s2;
			if (l != -1)
			{
				s2 = s.substring(0, l);
				s = s.substring(l + 1);
			} else
			{
				if (!s.equals("xmlns"))
					continue;
				s2 = s;
				s = null;
			}
			if (!s2.equals("xmlns"))
			{
				flag = true;
				continue;
			}
			int j1 = nspCounts[depth]++ << 1;
			nspStack = ensureCapacity(nspStack, j1 + 2);
			nspStack[j1] = s;
			nspStack[j1 + 1] = attributes[i + 3];
			if (s != null && attributes[i + 3].equals(""))
				exception("illegal empty namespace");
			System.arraycopy(attributes, i + 4, attributes, i, (--attributeCount << 2) - i);
			i -= 4;
		}

		if (flag)
		{
			for (int j = (attributeCount << 2) - 4; j >= 0; j -= 4)
			{
				String s1 = attributes[j + 2];
				int i1 = s1.indexOf(':');
				if (i1 == 0)
					throw new RuntimeException("illegal attribute name: " + s1 + " at " + this);
				if (i1 == -1)
					continue;
				String s3 = s1.substring(0, i1);
				s1 = s1.substring(i1 + 1);
				String s4 = getNamespace(s3);
				if (s4 == null)
					throw new RuntimeException("Undefined Prefix: " + s3 + " in " + this);
				attributes[j] = s4;
				attributes[j + 1] = s3;
				attributes[j + 2] = s1;
				for (int k1 = (attributeCount << 2) - 4; k1 > j; k1 -= 4)
					if (s1.equals(attributes[k1 + 2]) && s4.equals(attributes[k1]))
						exception("Duplicate Attribute: {" + s4 + "}" + s1);

			}

		}
		int k = name.indexOf(':');
		if (k == 0)
			exception("illegal tag name: " + name);
		else
		if (k != -1)
		{
			prefix = name.substring(0, k);
			name = name.substring(k + 1);
		}
		namespace = getNamespace(prefix);
		if (namespace == null)
		{
			if (prefix != null)
				exception("undefined prefix: " + prefix);
			namespace = "";
		}
		return flag;
	}

	private final void setTable(int i, int j, String as[])
	{
		if (stringTable != null)
			throw new RuntimeException("setXxxTable must be called before setInput!");
		for (; tables.size() < 3 * i + 3; tables.addElement(null));
		tables.setElementAt(as, i * 3 + j);
	}

	private final void exception(String s)
		throws XmlPullParserException
	{
		throw new XmlPullParserException(s, this, null);
	}

	private void selectPage(int i, boolean flag)
		throws XmlPullParserException
	{
		if (tables.size() == 0 && i == 0)
			return;
		if (i * 3 > tables.size())
			exception("Code Page " + i + " undefined!");
		if (flag)
		{
			tagTable = (String[])(String[])tables.elementAt(i * 3 + TAG_TABLE);
		} else
		{
			attrStartTable = (String[])(String[])tables.elementAt(i * 3 + ATTR_START_TABLE);
			attrValueTable = (String[])(String[])tables.elementAt(i * 3 + ATTR_VALUE_TABLE);
		}
	}

	private final void nextImpl()
		throws IOException, XmlPullParserException
	{
		if (type == 3)
			depth--;
		if (degenerated)
		{
			type = 3;
			degenerated = false;
			return;
		}
		text = null;
		prefix = null;
		name = null;
		int i;
		for (i = peekId(); i == 0; i = peekId())
		{
			nextId = -2;
			selectPage(readByte(), true);
		}

		nextId = -2;
		switch (i)
		{
		case -1: 
			type = 1;
			break;

		case 1: // '\001'
			int j = depth - 1 << 2;
			type = 3;
			namespace = elementStack[j];
			prefix = elementStack[j + 1];
			name = elementStack[j + 2];
			break;

		case 2: // '\002'
			type = 6;
			char c = (char)readInt();
			text = "" + c;
			name = "#" + (int)c;
			break;

		case 3: // '\003'
			type = 4;
			text = readStrI();
			break;

		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 128: 
		case 129: 
		case 130: 
		case 192: 
		case 193: 
		case 194: 
		case 195: 
			type = 64;
			wapCode = i;
			wapExtensionData = parseWapExtension(i);
			break;

		case 67: // 'C'
			throw new RuntimeException("PI curr. not supp.");

		case 131: 
			type = 4;
			text = readStrT();
			break;

		default:
			parseElement(i);
			break;
		}
	}

	public Object parseWapExtension(int i)
		throws IOException, XmlPullParserException
	{
		switch (i)
		{
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
			return readStrI();

		case 128: 
		case 129: 
		case 130: 
			return new Integer(readInt());

		case 192: 
		case 193: 
		case 194: 
			return null;

		case 195: 
			int j = readInt();
			byte abyte0[] = new byte[j];
			for (; j > 0; j -= in.read(abyte0, abyte0.length - j, j));
			return abyte0;
		}
		exception("illegal id: " + i);
		return null;
	}

	public void readAttr()
		throws IOException, XmlPullParserException
	{
		int i = readByte();
		int j = 0;
		while (i != 1) 
		{
			for (; i == 0; i = readByte())
				selectPage(readByte(), false);

			String s = resolveId(attrStartTable, i);
			int k = s.indexOf('=');
			StringBuffer stringbuffer;
			if (k == -1)
			{
				stringbuffer = new StringBuffer();
			} else
			{
				stringbuffer = new StringBuffer(s.substring(k + 1));
				s = s.substring(0, k);
			}
			for (i = readByte(); i > 128 || i == 0 || i == 2 || i == 3 || i == 131 || i >= 64 && i <= 66 || i >= 128 && i <= 130; i = readByte())
				switch (i)
				{
				case 0: // '\0'
					selectPage(readByte(), false);
					break;

				case 2: // '\002'
					stringbuffer.append((char)readInt());
					break;

				case 3: // '\003'
					stringbuffer.append(readStrI());
					break;

				case 64: // '@'
				case 65: // 'A'
				case 66: // 'B'
				case 128: 
				case 129: 
				case 130: 
				case 192: 
				case 193: 
				case 194: 
				case 195: 
					stringbuffer.append(resolveWapExtension(i, parseWapExtension(i)));
					break;

				case 131: 
					stringbuffer.append(readStrT());
					break;

				default:
					stringbuffer.append(resolveId(attrValueTable, i));
					break;
				}

			attributes = ensureCapacity(attributes, j + 4);
			attributes[j++] = "";
			attributes[j++] = null;
			attributes[j++] = s;
			attributes[j++] = stringbuffer.toString();
			attributeCount++;
		}
	}

	private int peekId()
		throws IOException
	{
		if (nextId == -2)
			nextId = in.read();
		return nextId;
	}

	protected String resolveWapExtension(int i, Object obj)
	{
		if (obj instanceof byte[])
		{
			StringBuffer stringbuffer = new StringBuffer();
			byte abyte0[] = (byte[])(byte[])obj;
			for (int j = 0; j < abyte0.length; j++)
			{
				stringbuffer.append("0123456789abcdef".charAt(abyte0[j] >> 4 & 0xf));
				stringbuffer.append("0123456789abcdef".charAt(abyte0[j] & 0xf));
			}

			return stringbuffer.toString();
		} else
		{
			return "$(" + obj + ")";
		}
	}

	String resolveId(String as[], int i)
		throws IOException
	{
		int j = (i & 0x7f) - 5;
		if (j == -1)
		{
			wapCode = -1;
			return readStrT();
		}
		if (j < 0 || as == null || j >= as.length || as[j] == null)
		{
			throw new IOException("id " + i + " undef.");
		} else
		{
			wapCode = j + 5;
			return as[j];
		}
	}

	void parseElement(int i)
		throws IOException, XmlPullParserException
	{
		type = 2;
		name = resolveId(tagTable, i & 0x3f);
		attributeCount = 0;
		if ((i & 0x80) != 0)
			readAttr();
		degenerated = (i & 0x40) == 0;
		int j = depth++ << 2;
		elementStack = ensureCapacity(elementStack, j + 4);
		elementStack[j + 3] = name;
		if (depth >= nspCounts.length)
		{
			int ai[] = new int[depth + 4];
			System.arraycopy(nspCounts, 0, ai, 0, nspCounts.length);
			nspCounts = ai;
		}
		nspCounts[depth] = nspCounts[depth - 1];
		for (int k = attributeCount - 1; k > 0; k--)
		{
			for (int l = 0; l < k; l++)
				if (getAttributeName(k).equals(getAttributeName(l)))
					exception("Duplicate Attribute: " + getAttributeName(k));

		}

		if (processNsp)
			adjustNsp();
		else
			namespace = "";
		elementStack[j] = namespace;
		elementStack[j + 1] = prefix;
		elementStack[j + 2] = name;
	}

	private final String[] ensureCapacity(String as[], int i)
	{
		if (as.length >= i)
		{
			return as;
		} else
		{
			String as1[] = new String[i + 16];
			System.arraycopy(as, 0, as1, 0, as.length);
			return as1;
		}
	}

	int readByte()
		throws IOException
	{
		int i = in.read();
		if (i == -1)
			throw new IOException("Unexpected EOF");
		else
			return i;
	}

	int readInt()
		throws IOException
	{
		int i = 0;
		int j;
		do
		{
			j = readByte();
			i = i << 7 | j & 0x7f;
		} while ((j & 0x80) != 0);
		return i;
	}

	String readStrI()
		throws IOException
	{
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		boolean flag = true;
		do
		{
			int i = in.read();
			if (i != 0)
			{
				if (i == -1)
					throw new IOException("Unexpected EOF");
				if (i > 32)
					flag = false;
				bytearrayoutputstream.write(i);
			} else
			{
				isWhitespace = flag;
				String s = new String(bytearrayoutputstream.toByteArray(), encoding);
				bytearrayoutputstream.close();
				return s;
			}
		} while (true);
	}

	String readStrT()
		throws IOException
	{
		int i = readInt();
		if (cacheStringTable == null)
			cacheStringTable = new Hashtable();
		String s = (String)cacheStringTable.get(new Integer(i));
		if (s == null)
		{
			int j;
			for (j = i; j < stringTable.length && stringTable[j] != 0; j++);
			s = new String(stringTable, i, j - i, encoding);
			cacheStringTable.put(new Integer(i), s);
		}
		return s;
	}

	public void setTagTable(int i, String as[])
	{
		setTable(i, TAG_TABLE, as);
	}

	public void setAttrStartTable(int i, String as[])
	{
		setTable(i, ATTR_START_TABLE, as);
	}

	public void setAttrValueTable(int i, String as[])
	{
		setTable(i, ATTR_VALUE_TABLE, as);
	}

	public int getWapCode()
	{
		return wapCode;
	}

	public Object getWapExtensionData()
	{
		return wapExtensionData;
	}
}
