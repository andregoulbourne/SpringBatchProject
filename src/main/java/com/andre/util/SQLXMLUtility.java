package com.andre.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SQLXMLUtility {
	
	private static final String SQLXMLPATH = "./src/main/resources/sql.xml";
	
	private static SQLXMLUtility sqlxmlUtility;
	
	private static final Logger logger = Logger.getLogger(SQLXMLUtility.class);
	
	private SQLXMLUtility() {
		super();
	}
	
	public static SQLXMLUtility getInstance() {
		if(sqlxmlUtility == null) {
			sqlxmlUtility = new SQLXMLUtility();
			init();
		}
		return sqlxmlUtility;
	}

	private static Map<String, String> propertyMap;
	
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	public static void setPropertyMap(Map<String, String> propertymap) {
		propertyMap = propertymap;
	}

	private static void init(){
		Map<String, String> rs = new HashMap<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc =builder.parse(SQLXMLPATH);
			NodeList queryList = doc.getElementsByTagName("query");
			for(int i=0;i<queryList.getLength();i++) {
				Node q = queryList.item(i);
				if(q.getNodeType()==Node.ELEMENT_NODE) {
					Element query = (Element) q;
					String name = query.getAttribute("name");
					String value = StringUtil.takeOutWhiteSpaceInFrontAndBack(query.getTextContent());
					rs.put(name, value);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
		setPropertyMap(rs);
	}
	
}
