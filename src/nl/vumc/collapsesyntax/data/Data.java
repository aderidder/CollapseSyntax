/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.data;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nl.vumc.collapsesyntax.shared.Common;
import nl.vumc.collapsesyntax.shared.FileOperations;

// Class for data file
public class Data{
	public Data(String dataFile){
		this.dataFile = dataFile;
	}

	// read the dataFile
	public void readDataFile(){
		String line;
		BufferedReader bufferedReader = FileOperations.openFileReader(dataFile);

		// nARMedicatieMedicijnWelke_E2_1_C97_1
		// E --> EventID
		// 1 --> repeat of Event
		// C97 --> CRF ID
		// 1 --> Group Repeat
		try {
			// first line of the dataFile contains the header
			line=bufferedReader.readLine();
			parseHeader(line);
			// next read until the end of the file
			while((line=bufferedReader.readLine())!=null){
				parseLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			FileOperations.closeFileReader(bufferedReader);
		}
	}
	
	// generate the dataFile
	public void generateDataFile(){
		String line="";
		String outFile = FileOperations.generateOutfileName(dataFile);
		BufferedWriter bufferedWriter = FileOperations.openFileWriter(outFile);
		
		// print the new header
        for (String aHeaderList : headerList) {
            line += aHeaderList + "\t";
        }
		FileOperations.writeLine(bufferedWriter, line);

		// print the new data
		for(String[] output:dataList){
			line="";
            for (String anOutput : output) {
                line += anOutput + "\t";
            }
			FileOperations.writeLine(bufferedWriter, line);
		}
		
		FileOperations.closeFileWriter(bufferedWriter);
	}

	private void parseHeader(String line){
		// the header of the datafile will form our basis:
		// StudySubjectID Protocol ID nARMedicatieMedicijnWelke_E2_1_C97_1 nARMedicatieMedicijnWelke_E2_1_C97_2 nARMedicatieMedicijnWelke_E2_1_C98_1
		//
		// The idea is that the C97 and C98 fields can be merged, thus becoming CF:
		// StudySubjectID Protocol ID nARMedicatieMedicijnWelke_E2_1_CF_1 nARMedicatieMedicijnWelke_E2_1_CF_2
		//
		// The first C therefore more or less defines the structure, unless the first C is incomplete (missing fields or fewer groups), which are added
		String [] splitItem;
		String prevGeneralItemName="", generalItemName;

		// Split the header by tab
		splitHeader = line.split("\t");
		
		// For all header items
        for (String curItem : splitHeader) {
            // The curItem shouldn't contain any spaces. However, OC does something odd with the ProtocolID,
            // which in the data is named Protocol ID and in the syntax ProtocolID.
            // Remove space in curItem if present
            curItem = curItem.replace(" ", "");
            // split by "_"
            splitItem = curItem.split("_");

            // translate the name to the general (CF) name
            generalItemName = Common.getGeneralName(splitItem);

			// check whether the headerList contains the generalItemName
			// if it doesn't, check the location of the previous generalItemName and add this item
			// at the next position
			// reason for this, is that if e.g. Event 1 has two repeats of a group and Event 2 has three repeats,
			// the third repeat's generalName will now follow the second repeat's generalName in the headerList
            if (!headerList.contains(generalItemName)) {
                int index = headerList.indexOf(prevGeneralItemName);
                headerList.add(index + 1, generalItemName.trim());
            }

            prevGeneralItemName = generalItemName;
        }
	}

	// create an array filled with empty Strings
	private String [] getNewOutputArray(){
		// length of output is defined by the size of the new header
		String [] output = new String[headerList.size()];
		for(int i=0; i<output.length; i++){
			output[i]="";
		}
		return output;
	}
	
	// Parse a line of data
	private void parseLine(String line){
		String [] splitLine = line.split("\t");
		String headerName, generalHeaderName, curVal;
		int index;
		
		String[] output = getNewOutputArray();

		for(int i=0; i<splitLine.length; i++){
			curVal = splitLine[i];
			if(!curVal.equalsIgnoreCase("")){
                // find the original header
                headerName = splitHeader[i].replace(" ", "");
                // retrieve the generalHeaderName (CF)
                generalHeaderName = Common.getGeneralName(headerName);
                // use the generalHeaderName to get the index, which defines at which position our data should now be located
                index = headerList.indexOf(generalHeaderName);
                // store the data at that position
                output[index] = curVal;
			}
		}
		// add the output to the dataList
		dataList.add(output);
	}

	// get the headerList
	public List<String> getHeaderList(){
		return headerList;
	}

	private String [] splitHeader;
	private final List<String> headerList = new LinkedList<>();
	private final List<String[]> dataList = new LinkedList<>();
	private final String dataFile;
}