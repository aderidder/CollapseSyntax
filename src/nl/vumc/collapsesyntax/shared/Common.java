/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.shared;
import java.util.List;

public class Common{

	// translate the 
	// nARMedicatieMedicijnWelke_E2_1_C97_1
	// E --> EventID
	// 1 --> repeat of Event
	// C97 --> CRF ID
	// 1 --> Group Repeat
	//
	// to
	// nARMedicatieMedicijnWelke_E2_1_CF_1
	public static String getGeneralName(String splitItem){
		return getGeneralName(splitItem.split("_"));
	}

	public static String getGeneralName(String [] splitItem){
		String generalItemName = splitItem[0];
		String curItem;
		for(int j=1; j<splitItem.length; j++){
			curItem = splitItem[j];
			if(curItem.startsWith("C")){
				curItem = "CF";
			}
			generalItemName+="_"+curItem;
		}
		return generalItemName.trim();
	}

	public static String listToString(List<String> aList){
		String listAsString="";
		for(String aString:aList){
			listAsString+=aString+newLine;
		}
		return removeFinalLineSeparator(listAsString);
	}

	public static String[] getCleanSplit(String aString, String splitBy){
		aString = aString.trim();
		aString = aString.replace("  ", " ");
		return aString.split(splitBy);
	}

	public static String getNewLine(){
		return newLine;
	}
	
	public static String removeFinalLineSeparator(String line){
		if(line.length()>0) return line.substring(0, line.length()-newLine.length());
		return "";
	}
	
	private final static String newLine = System.getProperty("line.separator");
}