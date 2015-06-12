/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.data;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.vumc.collapsesyntax.shared.Common;
import nl.vumc.collapsesyntax.shared.FileOperations;

// nARMedicatieMedicijnWelke_E2_1_C97_1
// E --> EventID
// 1 --> repeat of Event
// C97 --> CRF ID
// 1 --> Group Repeat

// Item Definitions based on syntaxfile
public class ItemDefs{
	public ItemDefs(String syntaxFile){
		this.dataFile = syntaxFile;
	}

	// get the Type and Width based on the headerList with the CF items
	private String getTypeAndWidthString(List<String> headerList){
		ItemDef itemDef;
		String line="";
		// for each item in our headerList
		for(String aHeader:headerList){
			// get the itemDef
			itemDef = itemTable.get(aHeader);
			// retrieve the type and width and add a newline
			line += itemDef.getTypeAndWidth()+Common.getNewLine();
		}
		return Common.removeFinalLineSeparator(line);
	}

	// get the Labels based on the headerList with the CF items
	private String getLabelsString(List<String> headerList){
		ItemDef itemDef;
		String line="";
		// for each item in our headerList
		for(String aHeader:headerList){
			// get the itemDef
			itemDef = itemTable.get(aHeader);
			// retrieve the label and add a newline
			line += itemDef.getLabel()+Common.getNewLine();
		}
		return Common.removeFinalLineSeparator(line);
	}

	// get the Value Labels based on the headerList with the CF items
	private String getValueLabelsString(List<String> headerList){
		ItemDef itemDef;
		String line="";
		List<String> keys, values;
		// for each item in our headerList
		for(String aHeader:headerList){
			// get the itemDef
			itemDef = itemTable.get(aHeader);
			// check whether it has a Value Label
			if(itemDef.hasValueLabel()){
				// add the headerName and a newline
				line += aHeader+Common.getNewLine();
				// retrieve the keys and values for the itemDef
				keys = itemDef.getKeys();
				values = itemDef.getValues();
				// add the keys and values and newlines
				for(int i=0; i<keys.size(); i++){
					line += keys.get(i)+" "+values.get(i)+Common.getNewLine();
				}
				line += "/"+Common.getNewLine();
			}

		}
		return Common.removeFinalLineSeparator(line);
	}

	// generate the new syntax file, based on the headerList, which contains our new CF items
	public void generateSyntaxFile(List<String> headerList){
		String outFile = FileOperations.generateOutfileName(dataFile);
		BufferedWriter bufferedWriter = FileOperations.openFileWriter(outFile);
		
		String lines;
		// first we print whatever was stored the first couple of lines
		lines = Common.listToString(textBeforeTypes);
		FileOperations.writeLine(bufferedWriter, lines);

		// print the type and width for the headers
		lines = getTypeAndWidthString(headerList);
		FileOperations.writeLine(bufferedWriter, lines);
		
		// print the text between the types and the labels
		lines = Common.listToString(textBetweenTypesAndLabels);
		FileOperations.writeLine(bufferedWriter, lines);

		// print the labels for the headers
		lines = getLabelsString(headerList);
		FileOperations.writeLine(bufferedWriter, lines);
		
		// print the text between the labels and the value-labels
		lines = Common.listToString(textBetweenLabelsAndValueLabels);
		FileOperations.writeLine(bufferedWriter, lines);

		// print the valueLabels
		lines = getValueLabelsString(headerList);
		if(!lines.equals("")) FileOperations.writeLine(bufferedWriter, lines);
		
		// print the text following the value-labels
		lines = Common.listToString(textAfterValueLabels);
		FileOperations.writeLine(bufferedWriter, lines);
		
		FileOperations.closeFileWriter(bufferedWriter);
	}

	// Collapse the types structure
	//  nARMedicatieMedicijnWelke_E2_1_C97_1  F4.0
	private void collapseTypes(BufferedReader bufferedReader) throws IOException{
		String line, name, typeAndWidth, generalName;
		String [] splitLine;
		ItemDef itemDef;

		// read until we find a "."
		while(!(line=bufferedReader.readLine()).equalsIgnoreCase(".")){
			splitLine = Common.getCleanSplit(line, " ");
			
			// name at position 0, transform it to the generalName
			name = splitLine[0];
			generalName = Common.getGeneralName(name);
			
			// type and width at position 1
			typeAndWidth = splitLine[1];
			
			// if no item exists for the generalName, create and store one
			if(!itemTable.containsKey(generalName)){
				itemTable.put(generalName, new ItemDef(generalName));
			}
			
			// retrieve the item and set its type and width
			itemDef = itemTable.get(generalName);
			itemDef.setTypeAndWidth(typeAndWidth);
		}
		textBetweenTypesAndLabels.add(line);
	}

	// collapse the labels structure
	//  nARMedicatieMedicijnWelke_E2_1_C97_1  "PatientStudie_Moment_Consult_Medicatie_nAR_MedicijnWelke" /
	private void collapseLabels(BufferedReader bufferedReader) throws IOException{
		String line, name, label, generalName;
		String [] splitLine;
		ItemDef itemDef;

		// read until "."
		while(!(line=bufferedReader.readLine()).equalsIgnoreCase(".")){
			// if splitting like this does not suffice: switch to a regular expression to catch the name and add the label as the rest of the string
			splitLine = Common.getCleanSplit(line, " \"");
			
			// translate name (position 0) to generalName and retrieve itemDef
			name = splitLine[0];
			generalName = Common.getGeneralName(name);
			itemDef = itemTable.get(generalName);
			
			// store the label
			label = splitLine[1];
			itemDef.setLabel(label);
		}
		textBetweenLabelsAndValueLabels.add(line);
	}
	
	// collapse the value-labels structure
	// BiologicalMedicijnWelke_E2_1_C98_2
	//	'1004' "Adalimumab"
	//	'1001' "Anakinra"
	//	'-1' "Geen informatie"
	//	/
	private void collapseValueLabels(BufferedReader bufferedReader) throws IOException{
		String line, name, generalName;
		String [] splitLine;
		ItemDef itemDef=null;

		// read until "."
		while(!(line=bufferedReader.readLine()).equalsIgnoreCase(".")){
            // A value label looks something like '1' "Some Text"
            // Split the line by using ' "
			splitLine = Common.getCleanSplit(line, "' \"");

			// if the splitLine has length 1 it's either the headername or a /
			// a / will give an itemDef = null, but that's not a problem as it will be overwritten next iteration.
			if(splitLine.length==1){
				name = splitLine[0];
				generalName = Common.getGeneralName(name);
				itemDef = itemTable.get(generalName); 
			}
			else{
				// add the value-label to the itemDef
				itemDef.addValueLabel(splitLine[0], splitLine[1]);
			}
		}
		textAfterValueLabels.add(line);
	}

	// There was some confusion with respect to the fileName mentioned in the syntax file,
	// which was still pointing to the original fileName.
	// This part replaces the original fileName by the new fileName.
    private String replaceDataFileName (String line){
        Matcher matcher = filePattern.matcher(line);
        if(matcher.matches()) {
            // this is something like " = 'd:/temp/somedir/spss.dat'"
            String fileName = matcher.group(1);
			// add the _new and remove the whatever-follows-the-last-dot
            String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_new";
			// add the whatever-follows-the-last-dot
            newFileName += fileName.substring(fileName.lastIndexOf("."), fileName.length());
            line = line.replace(fileName, newFileName);
        }
        return line;
    }

	// read the syntaxFile
	public void readSyntaxFile(){
		String line;
		BufferedReader bufferedReader = FileOperations.openFileReader(dataFile);

		//	nARMedicatieMedicijnWelke_E2_1_C97_1
		// E --> EventID
		// 1 --> repeat of Event
		// C97 --> CRF ID
		// 1 --> Group Repeat

		// to ensure we properly replicate the syntax file, break it down to pieces
		try {
			// read the first couple of lines, until we encounter "GET DATA"
			while(!(line=bufferedReader.readLine()).startsWith("GET DATA")){
				textBeforeTypes.add(line);
			}
			// the line with the GET DATA will have the fileName replaced
			line = replaceDataFileName(line);
			textBeforeTypes.add(line);

			while (!line.contains("/VARIABLES")){
				line=bufferedReader.readLine();
				textBeforeTypes.add(line);
			}

			// collapse the Types part of the syntax
			collapseTypes(bufferedReader);

			// add whatever is between the types and the labels part
			textBetweenTypesAndLabels.add(bufferedReader.readLine());
			// collapse the labels
			collapseLabels(bufferedReader);

			// Next lines should be either EXECUTE or VALUE LABELS
			line=bufferedReader.readLine();
			// it it's VALUE LABELS, collapse the valueLabels
			if(line.startsWith("VALUE LABELS")){
				textBetweenLabelsAndValueLabels.add(line);
				collapseValueLabels(bufferedReader);
			}
			else textAfterValueLabels.add(line);

			// add the remaining text
			while ((line = bufferedReader.readLine()) != null) {
				textAfterValueLabels.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			FileOperations.closeFileReader(bufferedReader);
		}
	}


	private final String dataFile;
	private final List<String> textBeforeTypes = new LinkedList<>();
	private final List<String> textBetweenTypesAndLabels = new LinkedList<>();
	private final List<String> textBetweenLabelsAndValueLabels = new LinkedList<>();
	private final List<String> textAfterValueLabels = new LinkedList<>();
	private final Hashtable <String, ItemDef> itemTable = new Hashtable<>();
//    private final static Pattern filePattern= Pattern.compile(".*FILE(.+)\\s*/DELCASE.*");
private final static Pattern filePattern= Pattern.compile(".*(\'.+\\.dat\').*");
}

// ItemDef is the definition of a single CF item
class ItemDef{
	ItemDef(String generalName){
		this.generalName = generalName;
	}

	List<String> getKeys(){
		return keyList;
	}

	List<String> getValues(){
		return valueList;
	}

	String getLabel(){
		return generalName+" "+label;
	}

	// return the type and width as a single string
	// if the dataType is a Float we want a decimal, otherwise we first convert to an int.
	String getTypeAndWidth(){
		if(!dataType.equalsIgnoreCase("F")){
			int width = (int) this.width;
			return generalName+" "+dataType+width;	
		}
		return generalName+" "+dataType+width;
	}

	// due to the split statement, the key on entrance is something like '1
	// and the value is something like Ja"
	// before adding the value we first add the missing ' and "
	void addValueLabel(String key, String value){
		key = key+"'";
		if(!keyList.contains(key)){
			keyList.add(key);
			valueList.add("\""+value);
		}
	}

	// set the variable label
	// due to the split, the label will be something like myLabel"
	// hence, we add the "
	void setLabel(String label){
		this.label="\""+label;
	}

	// set the type and width of the item
	void setTypeAndWidth(String typeAndWidth){
		// split the type and width
		Matcher matcher = typeAndWidthPattern.matcher(typeAndWidth);
		if(matcher.matches()){
			// type is first match; width the second
			String dataType = matcher.group(1);
			double width = Double.parseDouble(matcher.group(2));

			// in theory the only times this can happen is from "" to a dataType or from pData to Date
			if(!this.dataType.equalsIgnoreCase(dataType)){
				this.dataType = dataType;
			}
			// store the max width for the field
			if(this.width<width){
				this.width=width;
			}
		}
	}

    // only selection items have value labels
	boolean hasValueLabel(){
		return (!(keyList.size()==0)); 
	}

	private String label="";
	private String generalName="";
	private String dataType="";
	private double width=-1;
	private final List<String> keyList = new LinkedList<>();
	private final List<String> valueList = new LinkedList<>();

	// one or more non-digits, followed by at least one digit and zero or more characters
	// pattern will match e.g. ADATE10, F4.1, etc. and the groups will be "ADATE" and "10", "F" and "4.1", ...
	private final static Pattern typeAndWidthPattern = Pattern.compile("(\\D+)(\\d+.*)");
}