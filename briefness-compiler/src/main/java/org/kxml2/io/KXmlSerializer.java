//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kxml2.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlSerializer;

public class KXmlSerializer implements XmlSerializer {
	private Writer writer;
	private boolean pending;
	private int auto;
	private int depth;
	private String[] elementStack = new String[12];
	private int[] nspCounts = new int[4];
	private String[] nspStack = new String[8];
	private boolean[] indent = new boolean[4];
	private boolean unicode;
	private String encoding;

	public KXmlSerializer() {
	}

	private final void check(boolean var1) throws IOException {
		if(this.pending) {
			++this.depth;
			this.pending = false;
			if(this.indent.length <= this.depth) {
				boolean[] var2 = new boolean[this.depth + 4];
				System.arraycopy(this.indent, 0, var2, 0, this.depth);
				this.indent = var2;
			}

			this.indent[this.depth] = this.indent[this.depth - 1];

			for(int var3 = this.nspCounts[this.depth - 1]; var3 < this.nspCounts[this.depth]; ++var3) {
				this.writer.write(32);
				this.writer.write("xmlns");
				if(!"".equals(this.nspStack[var3 * 2])) {
					this.writer.write(58);
					this.writer.write(this.nspStack[var3 * 2]);
				} else if("".equals(this.getNamespace()) && !"".equals(this.nspStack[var3 * 2 + 1])) {
					throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
				}

				this.writer.write("=\"");
				this.writeEscaped(this.nspStack[var3 * 2 + 1], 34);
				this.writer.write(34);
			}

			if(this.nspCounts.length <= this.depth + 1) {
				int[] var4 = new int[this.depth + 8];
				System.arraycopy(this.nspCounts, 0, var4, 0, this.depth + 1);
				this.nspCounts = var4;
			}

			this.nspCounts[this.depth + 1] = this.nspCounts[this.depth];
			this.writer.write(var1?" />":">");
		}
	}

	private final void writeEscaped(String var1, int var2) throws IOException {
		for(int var3 = 0; var3 < var1.length(); ++var3) {
			char var4 = var1.charAt(var3);
			switch(var4) {
				case '\t':
				case '\n':
				case '\r':
					if(var2 == -1) {
						this.writer.write(var4);
					} else {
						this.writer.write("&#" + var4 + ';');
					}
					continue;
				case '"':
				case '\'':
					if(var4 == var2) {
						this.writer.write(var4 == 34?"&quot;":"&apos;");
						continue;
					}
					break;
				case '&':
					this.writer.write("&amp;");
					continue;
				case '<':
					this.writer.write("&lt;");
					continue;
				case '>':
					this.writer.write("&gt;");
					continue;
			}

			if(var4 < 32 || var4 == 64 || var4 >= 127 && !this.unicode) {
				this.writer.write("&#" + var4 + ";");
			} else {
				this.writer.write(var4);
			}
		}

	}

	public void docdecl(String var1) throws IOException {
		this.writer.write("<!DOCTYPE");
		this.writer.write(var1);
		this.writer.write(">");
	}

	public void endDocument() throws IOException {
		while(this.depth > 0) {
			this.endTag(this.elementStack[this.depth * 3 - 3], this.elementStack[this.depth * 3 - 1]);
		}

		this.flush();
	}

	public void entityRef(String var1) throws IOException {
		this.check(false);
		this.writer.write(38);
		this.writer.write(var1);
		this.writer.write(59);
	}

	public boolean getFeature(String var1) {
		return "http://xmlpull.org/v1/doc/features.html#indent-output".equals(var1)?this.indent[this.depth]:false;
	}

	public String getPrefix(String var1, boolean var2) {
		try {
			return this.getPrefix(var1, false, var2);
		} catch (IOException var4) {
			throw new RuntimeException(var4.toString());
		}
	}

	private final String getPrefix(String var1, boolean var2, boolean var3) throws IOException {
		for(int var4 = this.nspCounts[this.depth + 1] * 2 - 2; var4 >= 0; var4 -= 2) {
			if(this.nspStack[var4 + 1].equals(var1) && (var2 || !this.nspStack[var4].equals(""))) {
				String var5 = this.nspStack[var4];

				for(int var6 = var4 + 2; var6 < this.nspCounts[this.depth + 1] * 2; ++var6) {
					if(this.nspStack[var6].equals(var5)) {
						var5 = null;
						break;
					}
				}

				if(var5 != null) {
					return var5;
				}
			}
		}

		if(!var3) {
			return null;
		} else {
			String var7;
			if("".equals(var1)) {
				var7 = "";
			} else {
				do {
					var7 = "n" + this.auto++;

					for(int var8 = this.nspCounts[this.depth + 1] * 2 - 2; var8 >= 0; var8 -= 2) {
						if(var7.equals(this.nspStack[var8])) {
							var7 = null;
							break;
						}
					}
				} while(var7 == null);
			}

			boolean var9 = this.pending;
			this.pending = false;
			this.setPrefix(var7, var1);
			this.pending = var9;
			return var7;
		}
	}

	public Object getProperty(String var1) {
		throw new RuntimeException("Unsupported property");
	}

	public void ignorableWhitespace(String var1) throws IOException {
		this.text(var1);
	}

	public void setFeature(String var1, boolean var2) {
		if("http://xmlpull.org/v1/doc/features.html#indent-output".equals(var1)) {
			this.indent[this.depth] = var2;
		} else {
			throw new RuntimeException("Unsupported Feature");
		}
	}

	public void setProperty(String var1, Object var2) {
		throw new RuntimeException("Unsupported Property:" + var2);
	}

	public void setPrefix(String var1, String var2) throws IOException {
		this.check(false);
		if(var1 == null) {
			var1 = "";
		}

		if(var2 == null) {
			var2 = "";
		}

		String var3 = this.getPrefix(var2, true, false);
		if(!var1.equals(var3)) {
			int var10001 = this.depth + 1;
			int var10003 = this.nspCounts[this.depth + 1];
			this.nspCounts[var10001] = this.nspCounts[this.depth + 1] + 1;
			int var4 = var10003 << 1;
			if(this.nspStack.length < var4 + 1) {
				String[] var5 = new String[this.nspStack.length + 16];
				System.arraycopy(this.nspStack, 0, var5, 0, var4);
				this.nspStack = var5;
			}

			this.nspStack[var4++] = var1;
			this.nspStack[var4] = var2;
		}
	}

	public void setOutput(Writer var1) {
		this.writer = var1;
		this.nspCounts[0] = 2;
		this.nspCounts[1] = 2;
		this.nspStack[0] = "";
		this.nspStack[1] = "";
		this.nspStack[2] = "xml";
		this.nspStack[3] = "http://www.w3.org/XML/1998/namespace";
		this.pending = false;
		this.auto = 0;
		this.depth = 0;
		this.unicode = false;
	}

	public void setOutput(OutputStream var1, String var2) throws IOException {
		if(var1 == null) {
			throw new IllegalArgumentException();
		} else {
			this.setOutput(var2 == null?new OutputStreamWriter(var1):new OutputStreamWriter(var1, var2));
			this.encoding = var2;
			if(var2 != null && var2.toLowerCase().startsWith("utf")) {
				this.unicode = true;
			}

		}
	}

	public void startDocument(String var1, Boolean var2) throws IOException {
		this.writer.write("<?xml version='1.0' ");
		if(var1 != null) {
			this.encoding = var1;
			if(var1.toLowerCase().startsWith("utf")) {
				this.unicode = true;
			}
		}

		if(this.encoding != null) {
			this.writer.write("encoding='");
			this.writer.write(this.encoding);
			this.writer.write("' ");
		}

		if(var2 != null) {
			this.writer.write("standalone='");
			this.writer.write(var2.booleanValue()?"yes":"no");
			this.writer.write("' ");
		}

		this.writer.write("?>");
	}

	public XmlSerializer startTag(String var1, String var2) throws IOException {
		this.check(false);
		int var3;
		if(this.indent[this.depth]) {
			this.writer.write("\r\n");

			for(var3 = 0; var3 < this.depth; ++var3) {
				this.writer.write("  ");
			}
		}

		var3 = this.depth * 3;
		if(this.elementStack.length < var3 + 3) {
			String[] var4 = new String[this.elementStack.length + 12];
			System.arraycopy(this.elementStack, 0, var4, 0, var3);
			this.elementStack = var4;
		}

		String var6 = var1 == null?"":this.getPrefix(var1, true, true);
		if("".equals(var1)) {
			for(int var5 = this.nspCounts[this.depth]; var5 < this.nspCounts[this.depth + 1]; ++var5) {
				if("".equals(this.nspStack[var5 * 2]) && !"".equals(this.nspStack[var5 * 2 + 1])) {
					throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
				}
			}
		}

		this.elementStack[var3++] = var1;
		this.elementStack[var3++] = var6;
		this.elementStack[var3] = var2;
		this.writer.write(60);
		if(!"".equals(var6)) {
			this.writer.write(var6);
			this.writer.write(58);
		}

		this.writer.write(var2);
		this.pending = true;
		return this;
	}

	public XmlSerializer attribute(String var1, String var2, String var3) throws IOException {
		if(!this.pending) {
			throw new IllegalStateException("illegal position for attribute");
		} else {
			if(var1 == null) {
				var1 = "";
			}

			String var4 = "".equals(var1)?"":this.getPrefix(var1, false, true);
			this.writer.write(32);
			if(!"".equals(var4)) {
				this.writer.write(var4);
				this.writer.write(58);
			}

			this.writer.write(var2);
			this.writer.write(61);
			int var5 = var3.indexOf(34) == -1?34:39;
			this.writer.write(var5);
			this.writeEscaped(var3, var5);
			this.writer.write(var5);
			return this;
		}
	}

	public void flush() throws IOException {
		this.check(false);
		this.writer.flush();
	}

	public XmlSerializer endTag(String var1, String var2) throws IOException {
		if(!this.pending) {
			--this.depth;
		}

		if((var1 != null || this.elementStack[this.depth * 3] == null) && (var1 == null || var1.equals(this.elementStack[this.depth * 3])) && this.elementStack[this.depth * 3 + 2].equals(var2)) {
			if(this.pending) {
				this.check(true);
				--this.depth;
			} else {
				if(this.indent[this.depth + 1]) {
					this.writer.write("\r\n");

					for(int var3 = 0; var3 < this.depth; ++var3) {
						this.writer.write("  ");
					}
				}

				this.writer.write("</");
				String var4 = this.elementStack[this.depth * 3 + 1];
				if(!"".equals(var4)) {
					this.writer.write(var4);
					this.writer.write(58);
				}

				this.writer.write(var2);
				this.writer.write(62);
			}

			this.nspCounts[this.depth + 1] = this.nspCounts[this.depth];
			return this;
		} else {
			throw new IllegalArgumentException("</{" + var1 + "}" + var2 + "> does not match start");
		}
	}

	public String getNamespace() {
		return this.getDepth() == 0?null:this.elementStack[this.getDepth() * 3 - 3];
	}

	public String getName() {
		return this.getDepth() == 0?null:this.elementStack[this.getDepth() * 3 - 1];
	}

	public int getDepth() {
		return this.pending?this.depth + 1:this.depth;
	}

	public XmlSerializer text(String var1) throws IOException {
		this.check(false);
		this.indent[this.depth] = false;
		this.writeEscaped(var1, -1);
		return this;
	}

	public XmlSerializer text(char[] var1, int var2, int var3) throws IOException {
		this.text(new String(var1, var2, var3));
		return this;
	}

	public void cdsect(String var1) throws IOException {
		this.check(false);
		this.writer.write("<![CDATA[");
		this.writer.write(var1);
		this.writer.write("]]>");
	}

	public void comment(String var1) throws IOException {
		this.check(false);
		this.writer.write("<!--");
		this.writer.write(var1);
		this.writer.write("-->");
	}

	public void processingInstruction(String var1) throws IOException {
		this.check(false);
		this.writer.write("<?");
		this.writer.write(var1);
		this.writer.write("?>");
	}
}
