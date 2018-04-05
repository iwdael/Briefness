//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.xmlpull.v1;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class XmlPullParserFactory {
    static final Class referenceContextClass;
    public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";
    private static final String RESOURCE_NAME = "/META-INF/services/org.xmlpull.v1.XmlPullParserFactory";
    protected Vector parserClasses;
    protected String classNamesLocation;
    protected Vector serializerClasses;
    protected Hashtable features = new Hashtable();

    protected XmlPullParserFactory() {
    }

    public void setFeature(String name, boolean state) throws XmlPullParserException {
        this.features.put(name, new Boolean(state));
    }

    public boolean getFeature(String name) {
        Boolean value = (Boolean) this.features.get(name);
        return value != null ? value.booleanValue() : false;
    }

    public void setNamespaceAware(boolean awareness) {
        this.features.put("http://xmlpull.org/v1/doc/features.html#process-namespaces", new Boolean(awareness));
    }

    public boolean isNamespaceAware() {
        return this.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
    }

    public void setValidating(boolean validating) {
        this.features.put("http://xmlpull.org/v1/doc/features.html#validation", new Boolean(validating));
    }

    public boolean isValidating() {
        return this.getFeature("http://xmlpull.org/v1/doc/features.html#validation");
    }

    public XmlPullParser newPullParser() throws XmlPullParserException {
        if (this.parserClasses == null) {
            throw new XmlPullParserException("Factory initialization was incomplete - has not tried " + this.classNamesLocation);
        } else if (this.parserClasses.size() == 0) {
            throw new XmlPullParserException("No valid parser classes found in " + this.classNamesLocation);
        } else {
            StringBuffer issues = new StringBuffer();
            int i = 0;

            while (i < this.parserClasses.size()) {
                Class ppClass = (Class) this.parserClasses.elementAt(i);

                try {
                    XmlPullParser pp = (XmlPullParser) ppClass.newInstance();
                    Enumeration e = this.features.keys();

                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        Boolean value = (Boolean) this.features.get(key);
                        if (value != null && value.booleanValue()) {
                            pp.setFeature(key, true);
                        }
                    }

                    return pp;
                } catch (Exception var8) {
                    issues.append(ppClass.getName() + ": " + var8.toString() + "; ");
                    ++i;
                }
            }

            throw new XmlPullParserException("could not create parser: " + issues);
        }
    }

    public XmlSerializer newSerializer() throws XmlPullParserException {
        if (this.serializerClasses == null) {
            throw new XmlPullParserException("Factory initialization incomplete - has not tried " + this.classNamesLocation);
        } else if (this.serializerClasses.size() == 0) {
            throw new XmlPullParserException("No valid serializer classes found in " + this.classNamesLocation);
        } else {
            StringBuffer issues = new StringBuffer();
            int i = 0;

            while (i < this.serializerClasses.size()) {
                Class ppClass = (Class) this.serializerClasses.elementAt(i);

                try {
                    XmlSerializer ser = (XmlSerializer) ppClass.newInstance();
                    return ser;
                } catch (Exception var5) {
                    issues.append(ppClass.getName() + ": " + var5.toString() + "; ");
                    ++i;
                }
            }

            throw new XmlPullParserException("could not create serializer: " + issues);
        }
    }

    public static XmlPullParserFactory newInstance() throws XmlPullParserException {
        return newInstance((String) null, (Class) null);
    }

    public static XmlPullParserFactory newInstance(String classNames, Class context) throws XmlPullParserException {
        if (context == null) {
            context = referenceContextClass;
        }

        String classNamesLocation = null;
        if (classNames != null && classNames.length() != 0 && !"DEFAULT".equals(classNames)) {
            classNamesLocation = "parameter classNames to newInstance() that contained '" + classNames + "'";
        } else {
            try {
                InputStream is=new ByteArrayInputStream("org.kxml2.io.KXmlParser,org.kxml2.io.KXmlSerializer".getBytes());
//				InputStream is = context.getResourceAsStream("/META-INF/services/org.xmlpull.v1.XmlPullParserFactory");
				if(is == null) {
					throw new XmlPullParserException("resource not found: /META-INF/services/org.xmlpull.v1.XmlPullParserFactory make sure that parser implementing XmlPull API is available");
				}

                StringBuffer sb = new StringBuffer();

				while(true) {
					int ch = is.read();
					if(ch < 0) {
						is.close();
						classNames = sb.toString();
						break;
					}

					if(ch > 32) {
						sb.append((char)ch);
					}
				}
//                sb.append("org.kxml2.io.KXmlParser,org.kxml2.io.KXmlSerializer");
            } catch (Exception var13) {
                throw new XmlPullParserException((String) null, (XmlPullParser) null, var13);
            }

            classNamesLocation = "resource /META-INF/services/org.xmlpull.v1.XmlPullParserFactory that contained '" + classNames + "'";
        }

        XmlPullParserFactory factory = null;
        Vector parserClasses = new Vector();
        Vector serializerClasses = new Vector();

        int cut;
        for (int pos = 0; pos < classNames.length(); pos = cut + 1) {
            cut = classNames.indexOf(44, pos);
            if (cut == -1) {
                cut = classNames.length();
            }

            String name = classNames.substring(pos, cut);
            Class candidate = null;
            Object instance = null;

            try {
                candidate = Class.forName(name);
                instance = candidate.newInstance();
            } catch (Exception var12) {
                ;
            }

            if (candidate != null) {
                boolean recognized = false;
                if (instance instanceof XmlPullParser) {
                    parserClasses.addElement(candidate);
                    recognized = true;
                }

                if (instance instanceof XmlSerializer) {
                    serializerClasses.addElement(candidate);
                    recognized = true;
                }

                if (instance instanceof XmlPullParserFactory) {
                    if (factory == null) {
                        factory = (XmlPullParserFactory) instance;
                    }

                    recognized = true;
                }

                if (!recognized) {
                    throw new XmlPullParserException("incompatible class: " + name);
                }
            }
        }

        if (factory == null) {
            factory = new XmlPullParserFactory();
        }

        factory.parserClasses = parserClasses;
        factory.serializerClasses = serializerClasses;
        factory.classNamesLocation = classNamesLocation;
        return factory;
    }

    static {
        XmlPullParserFactory f = new XmlPullParserFactory();
        referenceContextClass = f.getClass();
    }
}
