// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.io;

import java.io.*;
import java.util.Hashtable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class KXmlParser
	implements XmlPullParser
{

	private Object location;
	private static final String UNEXPECTED_EOF = "Unexpected EOF";
	private static final String ILLEGAL_TYPE = "Wrong event type";
	private static final int LEGACY = 999;
	private static final int XML_DECL = 998;
	private String version;
	private Boolean standalone;
	private boolean processNsp;
	private boolean relaxed;
	private Hashtable entityMap;
	private int depth;
	private String elementStack[];
	private String nspStack[];
	private int nspCounts[];
	private Reader reader;
	private String encoding;
	private char srcBuf[];
	private int srcPos;
	private int srcCount;
	private int line;
	private int column;
	private char txtBuf[];
	private int txtPos;
	private int type;
	private boolean isWhitespace;
	private String namespace;
	private String prefix;
	private String name;
	private boolean degenerated;
	private int attributeCount;
	private String attributes[];
	private int stackMismatch;
	private String error;
	private int peek[];
	private int peekCount;
	private boolean wasCR;
	private boolean unresolved;
	private boolean token;

	public KXmlParser()
	{
		elementStack = new String[16];
		nspStack = new String[8];
		nspCounts = new int[4];
		txtBuf = new char[128];
		attributes = new String[16];
		stackMismatch = 0;
		peek = new int[2];
		srcBuf = new char[Runtime.getRuntime().freeMemory() < 0x100000L ? '\200' : 8192];
	}

	private final boolean isProp(String s, boolean flag, String s1)
	{
		if (!s.startsWith("http://xmlpull.org/v1/doc/"))
			return false;
		if (flag)
			return s.substring(42).equals(s1);
		else
			return s.substring(40).equals(s1);
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
				error("illegal empty namespace");
			System.arraycopy(attributes, i + 4, attributes, i, (--attributeCount << 2) - i);
			i -= 4;
		}

		if (flag)
		{
			for (int j = (attributeCount << 2) - 4; j >= 0; j -= 4)
			{
				String s1 = attributes[j + 2];
				int i1 = s1.indexOf(':');
				if (i1 == 0 && !relaxed)
					throw new RuntimeException("illegal attribute name: " + s1 + " at " + this);
				if (i1 == -1)
					continue;
				String s3 = s1.substring(0, i1);
				s1 = s1.substring(i1 + 1);
				String s4 = getNamespace(s3);
				if (s4 == null && !relaxed)
					throw new RuntimeException("Undefined Prefix: " + s3 + " in " + this);
				attributes[j] = s4;
				attributes[j + 1] = s3;
				attributes[j + 2] = s1;
			}

		}
		int k = name.indexOf(':');
		if (k == 0)
			error("illegal tag name: " + name);
		if (k != -1)
		{
			prefix = name.substring(0, k);
			name = name.substring(k + 1);
		}
		namespace = getNamespace(prefix);
		if (namespace == null)
		{
			if (prefix != null)
				error("undefined prefix: " + prefix);
			namespace = "";
		}
		return flag;
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

	private final void error(String s)
		throws XmlPullParserException
	{
		if (relaxed)
		{
			if (error == null)
				error = "ERR: " + s;
		} else
		{
			exception(s);
		}
	}

	private final void exception(String s)
		throws XmlPullParserException
	{
		throw new XmlPullParserException(s.length() >= 100 ? s.substring(0, 100) + "\n" : s, this, null);
	}

	private final void nextImpl()
		throws IOException, XmlPullParserException
	{
		if (reader == null)
			exception("No Input specified");
		if (type == 3)
			depth--;
		do
		{
			attributeCount = -1;
			if (degenerated)
			{
				degenerated = false;
				type = 3;
				return;
			}
			if (error != null)
			{
				for (int i = 0; i < error.length(); i++)
					push(error.charAt(i));

				error = null;
				type = 9;
				return;
			}
			if (relaxed && (stackMismatch > 0 || peek(0) == -1 && depth > 0))
			{
				int j = depth - 1 << 2;
				type = 3;
				namespace = elementStack[j];
				prefix = elementStack[j + 1];
				name = elementStack[j + 2];
				if (stackMismatch != 1)
					error = "missing end tag /" + name + " inserted";
				if (stackMismatch > 0)
					stackMismatch--;
				return;
			}
			prefix = null;
			name = null;
			namespace = null;
			type = peekType();
			switch (type)
			{
			case 6: // '\006'
				pushEntity();
				return;

			case 2: // '\002'
				parseStartTag(false);
				return;

			case 3: // '\003'
				parseEndTag();
				return;

			case 1: // '\001'
				return;

			case 4: // '\004'
				pushText(60, !token);
				if (depth == 0 && isWhitespace)
					type = 7;
				return;

			case 5: // '\005'
			default:
				type = parseLegacy(token);
				break;
			}
		} while (type == 998);
	}

	private final int parseLegacy(boolean flag)
		throws IOException, XmlPullParserException
	{
		String s = "";
		int i = 0;
		read();
		int j = read();
		byte byte0;
		byte byte1;
		if (j == 63)
		{
			if ((peek(0) == 120 || peek(0) == 88) && (peek(1) == 109 || peek(1) == 77))
			{
				if (flag)
				{
					push(peek(0));
					push(peek(1));
				}
				read();
				read();
				if ((peek(0) == 108 || peek(0) == 76) && peek(1) <= 32)
				{
					if (line != 1 || column > 4)
						error("PI must not start with xml");
					parseStartTag(true);
					if (attributeCount < 1 || !"version".equals(attributes[2]))
						error("version expected");
					version = attributes[3];
					int l = 1;
					if (l < attributeCount && "encoding".equals(attributes[6]))
					{
						encoding = attributes[7];
						l++;
					}
					if (l < attributeCount && "standalone".equals(attributes[4 * l + 2]))
					{
						String s1 = attributes[3 + 4 * l];
						if ("yes".equals(s1))
							standalone = new Boolean(true);
						else
						if ("no".equals(s1))
							standalone = new Boolean(false);
						else
							error("illegal standalone value: " + s1);
						l++;
					}
					if (l != attributeCount)
						error("illegal xmldecl");
					isWhitespace = true;
					txtPos = 0;
					return 998;
				}
			}
			byte0 = 63;
			byte1 = 8;
		} else
		if (j == 33)
		{
			if (peek(0) == 45)
			{
				byte1 = 9;
				s = "--";
				byte0 = 45;
			} else
			if (peek(0) == 91)
			{
				byte1 = 5;
				s = "[CDATA[";
				byte0 = 93;
				flag = true;
			} else
			{
				byte1 = 10;
				s = "DOCTYPE";
				byte0 = -1;
			}
		} else
		{
			error("illegal: <" + j);
			return 9;
		}
		for (int i1 = 0; i1 < s.length(); i1++)
			read(s.charAt(i1));

		if (byte1 == 10)
		{
			parseDoctype(flag);
		} else
		{
			do
			{
				int k = read();
				if (k == -1)
				{
					error("Unexpected EOF");
					return 9;
				}
				if (flag)
					push(k);
				if ((byte0 == 63 || k == byte0) && peek(0) == byte0 && peek(1) == 62)
					break;
				i = k;
			} while (true);
			if (byte0 == 45 && i == 45)
				error("illegal comment delimiter: --->");
			read();
			read();
			if (flag && byte0 != 63)
				txtPos--;
		}
		return byte1;
	}

	private final void parseDoctype(boolean flag)
		throws IOException, XmlPullParserException
	{
		int i = 1;
		boolean flag1 = false;
		do
		{
			int j;
			do
			{
				j = read();
				switch (j)
				{
				case -1: 
					error("Unexpected EOF");
					return;

				case 39: // '\''
					flag1 = !flag1;
					break;

				case 60: // '<'
					if (!flag1)
						i++;
					break;

				case 62: // '>'
					if (!flag1 && --i == 0)
						return;
					break;
				}
			} while (!flag);
			push(j);
		} while (true);
	}

	private final void parseEndTag()
		throws IOException, XmlPullParserException
	{
		read();
		read();
		name = readName();
		skip();
		read('>');
		int i = depth - 1 << 2;
		if (depth == 0)
		{
			error("element stack empty");
			type = 9;
			return;
		}
		if (!name.equals(elementStack[i + 3]))
		{
			error("expected: /" + elementStack[i + 3] + " read: " + name);
			int j;
			for (j = i; j >= 0 && !name.toLowerCase().equals(elementStack[j + 3].toLowerCase()); j -= 4)
				stackMismatch++;

			if (j < 0)
			{
				stackMismatch = 0;
				type = 9;
				return;
			}
		}
		namespace = elementStack[i];
		prefix = elementStack[i + 1];
		name = elementStack[i + 2];
	}

	private final int peekType()
		throws IOException
	{
		switch (peek(0))
		{
		case -1: 
			return 1;

		case 38: // '&'
			return 6;

		case 60: // '<'
			switch (peek(1))
			{
			case 47: // '/'
				return 3;

			case 33: // '!'
			case 63: // '?'
				return 999;
			}
			return 2;
		}
		return 4;
	}

	private final String get(int i)
	{
		return new String(txtBuf, i, txtPos - i);
	}

	private final void push(int i)
	{
		isWhitespace &= i <= 32;
		if (txtPos == txtBuf.length)
		{
			char ac[] = new char[(txtPos * 4) / 3 + 4];
			System.arraycopy(txtBuf, 0, ac, 0, txtPos);
			txtBuf = ac;
		}
		txtBuf[txtPos++] = (char)i;
	}

	private final void parseStartTag(boolean flag)
		throws IOException, XmlPullParserException
	{
		if (!flag)
			read();
		name = readName();
		attributeCount = 0;
		do
		{
			skip();
			int i = peek(0);
			if (flag)
			{
				if (i == 63)
				{
					read();
					read('>');
					return;
				}
			} else
			{
				if (i == 47)
				{
					degenerated = true;
					read();
					skip();
					read('>');
					break;
				}
				if (i == 62 && !flag)
				{
					read();
					break;
				}
			}
			if (i == -1)
			{
				error("Unexpected EOF");
				return;
			}
			String s = readName();
			if (s.length() == 0)
			{
				error("attr name expected");
				break;
			}
			int k = attributeCount++ << 2;
			attributes = ensureCapacity(attributes, k + 4);
			attributes[k++] = "";
			attributes[k++] = null;
			attributes[k++] = s;
			skip();
			if (peek(0) != 61)
			{
				error("Attr.value missing f. " + s);
				attributes[k] = "1";
			} else
			{
				read('=');
				skip();
				int l = peek(0);
				if (l != 39 && l != 34)
				{
					error("attr value delimiter missing!");
					l = 32;
				} else
				{
					read();
				}
				int i1 = txtPos;
				pushText(l, true);
				attributes[k] = get(i1);
				txtPos = i1;
				if (l != 32)
					read();
			}
		} while (true);
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
		if (processNsp)
			adjustNsp();
		else
			namespace = "";
		elementStack[j] = namespace;
		elementStack[j + 1] = prefix;
		elementStack[j + 2] = name;
	}

	private final void pushEntity()
		throws IOException, XmlPullParserException
	{
		push(read());
		int i = txtPos;
		do
		{
			int j = read();
			if (j == 59)
				break;
			if (j < 128 && (j < 48 || j > 57) && (j < 97 || j > 122) && (j < 65 || j > 90) && j != 95 && j != 45 && j != 35)
			{
				if (!relaxed)
					error("unterminated entity ref");
				if (j != -1)
					push(j);
				return;
			}
			push(j);
		} while (true);
		String s = get(i);
		txtPos = i - 1;
		if (token && type == 6)
			name = s;
		if (s.charAt(0) == '#')
		{
			int k = s.charAt(1) != 'x' ? Integer.parseInt(s.substring(1)) : Integer.parseInt(s.substring(2), 16);
			push(k);
			return;
		}
		String s1 = (String)entityMap.get(s);
		unresolved = s1 == null;
		if (unresolved)
		{
			if (!token)
				error("unresolved: &" + s + ";");
		} else
		{
			for (int l = 0; l < s1.length(); l++)
				push(s1.charAt(l));

		}
	}

	private final void pushText(int i, boolean flag)
		throws IOException, XmlPullParserException
	{
		int j = peek(0);
		int k = 0;
		for (; j != -1 && j != i && (i != 32 || j > 32 && j != 62); j = peek(0))
		{
			if (j == 38)
			{
				if (!flag)
					break;
				pushEntity();
			} else
			if (j == 10 && type == 2)
			{
				read();
				push(32);
			} else
			{
				push(read());
			}
			if (j == 62 && k >= 2 && i != 93)
				error("Illegal: ]]>");
			if (j == 93)
				k++;
			else
				k = 0;
		}

	}

	private final void read(char c)
		throws IOException, XmlPullParserException
	{
		int i = read();
		if (i != c)
			error("expected: '" + c + "' actual: '" + (char)i + "'");
	}

	private final int read()
		throws IOException
	{
		int i;
		if (peekCount == 0)
		{
			i = peek(0);
		} else
		{
			i = peek[0];
			peek[0] = peek[1];
		}
		peekCount--;
		column++;
		if (i == 10)
		{
			line++;
			column = 1;
		}
		return i;
	}

	private final int peek(int i)
		throws IOException
	{
		while (i >= peekCount) 
		{
			int j;
			if (srcBuf.length <= 1)
				j = reader.read();
			else
			if (srcPos < srcCount)
			{
				j = srcBuf[srcPos++];
			} else
			{
				srcCount = reader.read(srcBuf, 0, srcBuf.length);
				if (srcCount <= 0)
					j = -1;
				else
					j = srcBuf[0];
				srcPos = 1;
			}
			if (j == 13)
			{
				wasCR = true;
				peek[peekCount++] = 10;
			} else
			{
				if (j == 10)
				{
					if (!wasCR)
						peek[peekCount++] = 10;
				} else
				{
					peek[peekCount++] = j;
				}
				wasCR = false;
			}
		}
		return peek[i];
	}

	private final String readName()
		throws IOException, XmlPullParserException
	{
		int i = txtPos;
		int j = peek(0);
		if ((j < 97 || j > 122) && (j < 65 || j > 90) && j != 95 && j != 58 && j < 192 && !relaxed)
			error("name expected");
		do
		{
			push(read());
			j = peek(0);
		} while (j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 95 || j == 45 || j == 58 || j == 46 || j >= 183);
		String s = get(i);
		txtPos = i;
		return s;
	}

	private final void skip()
		throws IOException
	{
		do
		{
			int i = peek(0);
			if (i <= 32 && i != -1)
				read();
			else
				return;
		} while (true);
	}

	public void setInput(Reader reader1)
		throws XmlPullParserException
	{
		reader = reader1;
		line = 1;
		column = 0;
		type = 0;
		name = null;
		namespace = null;
		degenerated = false;
		attributeCount = -1;
		encoding = null;
		version = null;
		standalone = null;
		if (reader1 == null)
		{
			return;
		} else
		{
			srcPos = 0;
			srcCount = 0;
			peekCount = 0;
			depth = 0;
			entityMap = new Hashtable();
			entityMap.put("amp", "&");
			entityMap.put("apos", "'");
			entityMap.put("gt", ">");
			entityMap.put("lt", "<");
			entityMap.put("quot", "\"");
			return;
		}
	}

	public void setInput(InputStream inputstream, String s)
		throws XmlPullParserException
	{
		srcPos = 0;
		srcCount = 0;
		String s1 = s;
		if (inputstream == null)
			throw new IllegalArgumentException();
		try
		{
label0:
			{
				int i;
label1:
				{
					if (s1 != null)
						break label0;
					i = 0;
					do
					{
						if (srcCount >= 4)
							break;
						int k = inputstream.read();
						if (k == -1)
							break;
						i = i << 8 | k;
						srcBuf[srcCount++] = (char)k;
					} while (true);
					if (srcCount != 4)
						break label0;
					switch (i)
					{
					default:
						break;

					case 65279: 
						s1 = "UTF-32BE";
						srcCount = 0;
						break label0;

					case -131072: 
						s1 = "UTF-32LE";
						srcCount = 0;
						break label0;

					case 60: // '<'
						s1 = "UTF-32BE";
						srcBuf[0] = '<';
						srcCount = 1;
						break label0;

					case 1006632960: 
						s1 = "UTF-32LE";
						srcBuf[0] = '<';
						srcCount = 1;
						break label0;

					case 3932223: 
						s1 = "UTF-16BE";
						srcBuf[0] = '<';
						srcBuf[1] = '?';
						srcCount = 2;
						break label0;

					case 1006649088: 
						s1 = "UTF-16LE";
						srcBuf[0] = '<';
						srcBuf[1] = '?';
						srcCount = 2;
						break label0;

					case 1010792557: 
						int l;
						do
						{
							l = inputstream.read();
							if (l == -1)
								break label1;
							srcBuf[srcCount++] = (char)l;
						} while (l != 62);
						String s2 = new String(srcBuf, 0, srcCount);
						int i1 = s2.indexOf("encoding");
						if (i1 != -1)
						{
							for (; s2.charAt(i1) != '"' && s2.charAt(i1) != '\''; i1++);
							char c = s2.charAt(i1++);
							int j1 = s2.indexOf(c, i1);
							s1 = s2.substring(i1, j1);
						}
						break;
					}
				}
				if ((i & 0xffff0000) == 0xfeff0000)
				{
					s1 = "UTF-16BE";
					srcBuf[0] = (char)(srcBuf[2] << 8 | srcBuf[3]);
					srcCount = 1;
				} else
				if ((i & 0xffff0000) == 0xfffe0000)
				{
					s1 = "UTF-16LE";
					srcBuf[0] = (char)(srcBuf[3] << 8 | srcBuf[2]);
					srcCount = 1;
				} else
				if ((i & 0xffffff00) == 0xefbbbf00)
				{
					s1 = "UTF-8";
					srcBuf[0] = srcBuf[3];
					srcCount = 1;
				}
			}
			if (s1 == null)
				s1 = "UTF-8";
			int j = srcCount;
			setInput(((Reader) (new InputStreamReader(inputstream, s1))));
			encoding = s;
			srcCount = j;
		}
		catch (Exception exception1)
		{
			throw new XmlPullParserException("Invalid stream or encoding: " + exception1.toString(), this, exception1);
		}
	}

	public boolean getFeature(String s)
	{
		if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(s))
			return processNsp;
		if (isProp(s, false, "relaxed"))
			return relaxed;
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
		if (entityMap == null)
		{
			throw new RuntimeException("entity replacement text must be defined after setInput!");
		} else
		{
			entityMap.put(s, s1);
			return;
		}
	}

	public Object getProperty(String s)
	{
		if (isProp(s, true, "xmldecl-version"))
			return version;
		if (isProp(s, true, "xmldecl-standalone"))
			return standalone;
		if (isProp(s, true, "location"))
			return location == null ? reader.toString() : location;
		else
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
		stringbuffer.append("@" + line + ":" + column);
		if (location != null)
		{
			stringbuffer.append(" in ");
			stringbuffer.append(location);
		} else
		if (reader != null)
		{
			stringbuffer.append(" in ");
			stringbuffer.append(reader.toString());
		}
		return stringbuffer.toString();
	}

	public int getLineNumber()
	{
		return line;
	}

	public int getColumnNumber()
	{
		return column;
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
		return type >= 4 && (type != 6 || !unresolved) ? get(0) : null;
	}

	public char[] getTextCharacters(int ai[])
	{
		if (type >= 4)
		{
			if (type == 6)
			{
				ai[0] = 0;
				ai[1] = name.length();
				return name.toCharArray();
			} else
			{
				ai[0] = 0;
				ai[1] = txtPos;
				return txtBuf;
			}
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
		txtPos = 0;
		isWhitespace = true;
		int i = 9999;
		token = false;
		do
		{
			nextImpl();
			if (type < i)
				i = type;
		} while (i > 6 || i >= 4 && peekType() >= 4);
		type = i;
		if (type > 4)
			type = 4;
		return type;
	}

	public int nextToken()
		throws XmlPullParserException, IOException
	{
		isWhitespace = true;
		txtPos = 0;
		token = true;
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

	public void require(int i, String s, String s1)
		throws XmlPullParserException, IOException
	{
		if (i != type || s != null && !s.equals(getNamespace()) || s1 != null && !s1.equals(getName()))
			exception("expected: " + XmlPullParser.TYPES[i] + " {" + s + "}" + s1);
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

	public void setFeature(String s, boolean flag)
		throws XmlPullParserException
	{
		if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(s))
			processNsp = flag;
		else
		if (isProp(s, false, "relaxed"))
			relaxed = flag;
		else
			exception("unsupported feature: " + s);
	}

	public void setProperty(String s, Object obj)
		throws XmlPullParserException
	{
		if (isProp(s, true, "location"))
			location = obj;
		else
			throw new XmlPullParserException("unsupported property: " + s);
	}

	public void skipSubTree()
		throws XmlPullParserException, IOException
	{
		require(2, null, null);
		int i = 1;
		do
		{
			if (i <= 0)
				break;
			int j = next();
			if (j == 3)
				i--;
			else
			if (j == 2)
				i++;
		} while (true);
	}
}
