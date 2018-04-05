// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.wap;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlSerializer;

public class WbxmlSerializer
	implements XmlSerializer
{

	Hashtable stringTable;
	OutputStream out;
	ByteArrayOutputStream buf;
	ByteArrayOutputStream stringTableBuf;
	String pending;
	int depth;
	String name;
	String namespace;
	Vector attributes;
	Hashtable attrStartTable;
	Hashtable attrValueTable;
	Hashtable tagTable;
	private int attrPage;
	private int tagPage;
	private String encoding;

	public WbxmlSerializer()
	{
		stringTable = new Hashtable();
		buf = new ByteArrayOutputStream();
		stringTableBuf = new ByteArrayOutputStream();
		attributes = new Vector();
		attrStartTable = new Hashtable();
		attrValueTable = new Hashtable();
		tagTable = new Hashtable();
	}

	public XmlSerializer attribute(String s, String s1, String s2)
	{
		attributes.addElement(s1);
		attributes.addElement(s2);
		return this;
	}

	public void cdsect(String s)
		throws IOException
	{
		text(s);
	}

	public void comment(String s)
	{
	}

	public void docdecl(String s)
	{
		throw new RuntimeException("Cannot write docdecl for WBXML");
	}

	public void entityRef(String s)
	{
		throw new RuntimeException("EntityReference not supported for WBXML");
	}

	public int getDepth()
	{
		return depth;
	}

	public boolean getFeature(String s)
	{
		return false;
	}

	public String getNamespace()
	{
		throw new RuntimeException("NYI");
	}

	public String getName()
	{
		throw new RuntimeException("NYI");
	}

	public String getPrefix(String s, boolean flag)
	{
		throw new RuntimeException("NYI");
	}

	public Object getProperty(String s)
	{
		return null;
	}

	public void ignorableWhitespace(String s)
	{
	}

	public void endDocument()
		throws IOException
	{
		writeInt(out, stringTableBuf.size());
		out.write(stringTableBuf.toByteArray());
		out.write(buf.toByteArray());
		out.flush();
	}

	public void flush()
	{
	}

	public void checkPending(boolean flag)
		throws IOException
	{
		if (pending == null)
			return;
		int i = attributes.size();
		int ai[] = (int[])(int[])tagTable.get(pending);
		if (ai == null)
		{
			buf.write(i != 0 ? flag ? 132 : 196 : ((int) (flag ? 4 : 68)));
			writeStrT(pending, false);
		} else
		{
			if (ai[0] != tagPage)
			{
				tagPage = ai[0];
				buf.write(0);
				buf.write(tagPage);
			}
			buf.write(i != 0 ? flag ? ai[1] | 0x80 : ai[1] | 0xc0 : flag ? ai[1] : ai[1] | 0x40);
		}
		for (int j = 0; j < i; j++)
		{
			int ai1[] = (int[])(int[])attrStartTable.get(attributes.elementAt(j));
			if (ai1 == null)
			{
				buf.write(4);
				writeStrT((String)attributes.elementAt(j), false);
			} else
			{
				if (ai1[0] != attrPage)
				{
					attrPage = ai1[0];
					buf.write(0);
					buf.write(attrPage);
				}
				buf.write(ai1[1]);
			}
			ai1 = (int[])(int[])attrValueTable.get(attributes.elementAt(++j));
			if (ai1 == null)
			{
				writeStr((String)attributes.elementAt(j));
				continue;
			}
			if (ai1[0] != attrPage)
			{
				attrPage = ai1[0];
				buf.write(0);
				buf.write(attrPage);
			}
			buf.write(ai1[1]);
		}

		if (i > 0)
			buf.write(1);
		pending = null;
		attributes.removeAllElements();
	}

	public void processingInstruction(String s)
	{
		throw new RuntimeException("PI NYI");
	}

	public void setFeature(String s, boolean flag)
	{
		throw new IllegalArgumentException("unknown feature " + s);
	}

	public void setOutput(Writer writer)
	{
		throw new RuntimeException("Wbxml requires an OutputStream!");
	}

	public void setOutput(OutputStream outputstream, String s)
		throws IOException
	{
		encoding = s != null ? s : "UTF-8";
		out = outputstream;
		buf = new ByteArrayOutputStream();
		stringTableBuf = new ByteArrayOutputStream();
	}

	public void setPrefix(String s, String s1)
	{
		throw new RuntimeException("NYI");
	}

	public void setProperty(String s, Object obj)
	{
		throw new IllegalArgumentException("unknown property " + s);
	}

	public void startDocument(String s, Boolean boolean1)
		throws IOException
	{
		out.write(3);
		out.write(1);
		if (s != null)
			encoding = s;
		if (encoding.toUpperCase().equals("UTF-8"))
			out.write(106);
		else
		if (encoding.toUpperCase().equals("ISO-8859-1"))
			out.write(4);
		else
			throw new UnsupportedEncodingException(s);
	}

	public XmlSerializer startTag(String s, String s1)
		throws IOException
	{
		if (s != null && !"".equals(s))
		{
			throw new RuntimeException("NSP NYI");
		} else
		{
			checkPending(false);
			pending = s1;
			depth++;
			return this;
		}
	}

	public XmlSerializer text(char ac[], int i, int j)
		throws IOException
	{
		checkPending(false);
		writeStr(new String(ac, i, j));
		return this;
	}

	public XmlSerializer text(String s)
		throws IOException
	{
		checkPending(false);
		writeStr(s);
		return this;
	}

	private void writeStr(String s)
		throws IOException
	{
		int i = 0;
		int j = 0;
		int k;
		int l;
		for (k = s.length(); i < k; i = l)
		{
			for (; i < k && s.charAt(i) < 'A'; i++);
			for (l = i; l < k && s.charAt(l) >= 'A'; l++);
			if (l - i <= 10)
				continue;
			if (i > j && s.charAt(i - 1) == ' ' && stringTable.get(s.substring(i, l)) == null)
			{
				buf.write(131);
				writeStrT(s.substring(j, l), false);
			} else
			{
				if (i > j && s.charAt(i - 1) == ' ')
					i--;
				if (i > j)
				{
					buf.write(131);
					writeStrT(s.substring(j, i), false);
				}
				buf.write(131);
				writeStrT(s.substring(i, l), true);
			}
			j = l;
		}

		if (j < k)
		{
			buf.write(131);
			writeStrT(s.substring(j, k), false);
		}
	}

	public XmlSerializer endTag(String s, String s1)
		throws IOException
	{
		if (pending != null)
			checkPending(true);
		else
			buf.write(1);
		depth--;
		return this;
	}

	public void writeWapExtension(int i, Object obj)
		throws IOException
	{
		checkPending(false);
		buf.write(i);
		switch (i)
		{
		case 195: 
			byte abyte0[] = (byte[])(byte[])obj;
			writeInt(buf, abyte0.length);
			buf.write(abyte0);
			break;

		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
			writeStrI(buf, (String)obj);
			break;

		case 128: 
		case 129: 
		case 130: 
			writeStrT((String)obj, false);
			break;

		default:
			throw new IllegalArgumentException();

		case 192: 
		case 193: 
		case 194: 
			break;
		}
	}

	static void writeInt(OutputStream outputstream, int i)
		throws IOException
	{
		byte abyte0[] = new byte[5];
		int j = 0;
		do
		{
			abyte0[j++] = (byte)(i & 0x7f);
			i >>= 7;
		} while (i != 0);
		while (j > 1) 
			outputstream.write(abyte0[--j] | 0x80);
		outputstream.write(abyte0[0]);
	}

	void writeStrI(OutputStream outputstream, String s)
		throws IOException
	{
		byte abyte0[] = s.getBytes(encoding);
		outputstream.write(abyte0);
		outputstream.write(0);
	}

	private final void writeStrT(String s, boolean flag)
		throws IOException
	{
		Integer integer = (Integer)stringTable.get(s);
		if (integer != null)
		{
			writeInt(buf, integer.intValue());
		} else
		{
			int i = stringTableBuf.size();
			if (s.charAt(0) >= '0' && flag)
			{
				s = ' ' + s;
				writeInt(buf, i + 1);
			} else
			{
				writeInt(buf, i);
			}
			stringTable.put(s, new Integer(i));
			if (s.charAt(0) == ' ')
				stringTable.put(s.substring(1), new Integer(i + 1));
			int j = s.lastIndexOf(' ');
			if (j > 1)
			{
				stringTable.put(s.substring(j), new Integer(i + j));
				stringTable.put(s.substring(j + 1), new Integer(i + j + 1));
			}
			writeStrI(stringTableBuf, s);
			stringTableBuf.flush();
		}
	}

	public void setTagTable(int i, String as[])
	{
		for (int j = 0; j < as.length; j++)
			if (as[j] != null)
			{
				int ai[] = {
					i, j + 5
				};
				tagTable.put(as[j], ai);
			}

	}

	public void setAttrStartTable(int i, String as[])
	{
		for (int j = 0; j < as.length; j++)
			if (as[j] != null)
			{
				int ai[] = {
					i, j + 5
				};
				attrStartTable.put(as[j], ai);
			}

	}

	public void setAttrValueTable(int i, String as[])
	{
		for (int j = 0; j < as.length; j++)
			if (as[j] != null)
			{
				int ai[] = {
					i, j + 133
				};
				attrValueTable.put(as[j], ai);
			}

	}
}
