/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.shared;
import java.util.List;
import java.util.stream.Collectors;

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
		// search for the "C" part, which isn't in a fixed position
		for(int j=1; j<splitItem.length; j++){
			curItem = splitItem[j];
			// if we found the C item, set the curItem's name to CF
			if(curItem.startsWith("C")) curItem = "CF";
			// add curItem to the generalItemName
			generalItemName+="_"+curItem;
		}
		return generalItemName.trim();
	}

	// create a stream from the list, collect, joining by newLine
	public static String listToString(List<String> aList){
		return aList.stream().collect(Collectors.joining(newLine));
	}

	// clean the string and split it
	// cleaning, in this case, is trimming and replacing double spaces by a single space
	public static String[] getCleanSplit(String aString, String splitBy){
		aString = aString.trim();
		aString = aString.replace("  ", " ");
		return aString.split(splitBy);
	}

	// return the system's line separator
	public static String getNewLine(){
		return newLine;
	}

	// if a line ends with the line separator, return a line without it
	public static String removeFinalLineSeparator(String line){
		if(line.length()>0) {
			if (line.endsWith(newLine)) return line.substring(0, line.length() - newLine.length());
			return line;
		}
		return "";
	}
	
	private final static String newLine = System.getProperty("line.separator");
}