// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package org.kxml2.wap.syncml;

import org.kxml2.wap.WbxmlParser;
import org.kxml2.wap.WbxmlSerializer;

public abstract class SyncML
{

	public static final String TAG_TABLE_0[] = {
		"Add", "Alert", "Archive", "Atomic", "Chal", "Cmd", "CmdID", "CmdRef", "Copy", "Cred", 
		"Data", "Delete", "Exec", "Final", "Get", "Item", "Lang", "LocName", "LocURI", "Map", 
		"MapItem", "Meta", "MsgID", "MsgRef", "NoResp", "NoResults", "Put", "Replace", "RespURI", "Results", 
		"Search", "Sequence", "SessionID", "SftDel", "Source", "SourceRef", "Status", "Sync", "SyncBody", "SyncHdr", 
		"SyncML", "Target", "TargetRef", "Reserved for future use", "VerDTD", "VerProto", "NumberOfChanged", "MoreData", "Field", "Filter", 
		"Record", "FilterType", "SourceParent", "TargetParent", "Move", "Correlator"
	};
	public static final String TAG_TABLE_1[] = {
		"Anchor", "EMI", "Format", "FreeID", "FreeMem", "Last", "Mark", "MaxMsgSize", "Mem", "MetInf", 
		"Next", "NextNonce", "SharedMem", "Size", "Type", "Version", "MaxObjSize", "FieldLevel"
	};
	public static final String TAG_TABLE_2_DM[] = {
		"AccessType", "ACL", "Add", "b64", "bin", "bool", "chr", "CaseSense", "CIS", "Copy", 
		"CS", "date", "DDFName", "DefaultValue", "Delete", "Description", "DDFFormat", "DFProperties", "DFTitle", "DFType", 
		"Dynamic", "Exec", "float", "Format", "Get", "int", "Man", "MgmtTree", "MIME", "Mod", 
		"Name", "Node", "node", "NodeName", "null", "Occurence", "One", "OneOrMore", "OneOrN", "Path", 
		"Permanent", "Replace", "RTProperties", "Scope", "Size", "time", "Title", "TStamp", "Type", "Value", 
		"VerDTD", "VerNo", "xml", "ZeroOrMore", "ZeroOrN", "ZeroOrOne"
	};

	public SyncML()
	{
	}

	public static WbxmlParser createParser()
	{
		WbxmlParser wbxmlparser = new WbxmlParser();
		wbxmlparser.setTagTable(0, TAG_TABLE_0);
		wbxmlparser.setTagTable(1, TAG_TABLE_1);
		return wbxmlparser;
	}

	public static WbxmlSerializer createSerializer()
	{
		WbxmlSerializer wbxmlserializer = new WbxmlSerializer();
		wbxmlserializer.setTagTable(0, TAG_TABLE_0);
		wbxmlserializer.setTagTable(1, TAG_TABLE_1);
		return wbxmlserializer;
	}

	public static WbxmlParser createDMParser()
	{
		WbxmlParser wbxmlparser = createParser();
		wbxmlparser.setTagTable(2, TAG_TABLE_2_DM);
		return wbxmlparser;
	}

	public static WbxmlSerializer createDMSerializer()
	{
		WbxmlSerializer wbxmlserializer = createSerializer();
		wbxmlserializer.setTagTable(2, TAG_TABLE_2_DM);
		return wbxmlserializer;
	}

}
