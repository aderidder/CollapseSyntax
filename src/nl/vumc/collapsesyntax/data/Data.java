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

public class Data{
//	public Data(String workDir, String dataFile){
	public Data(String dataFile){
		this.dataFile = dataFile;
	}


	public void readDataFile(){
//		String fileName = workDir+dataFile;
		String fileName = dataFile;
		String line;
		BufferedReader bufferedReader = FileOperations.openFileReader(fileName);

		// nARMedicatieMedicijnWelke_E2_1_C97_1
		// E --> EventID
		// 1 --> repeat of Event
		// C97 --> CRF ID
		// 1 --> Group Repeat
		try {
			line=bufferedReader.readLine();
			parseHeader(line);
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
	// at the moment we're just printing to screen
	public void generateDataFile(){
		String line="";
		String outFile = FileOperations.generateOutfileName(dataFile);
		BufferedWriter bufferedWriter = FileOperations.openFileWriter(outFile);
		
		// print the new header
        for (String aHeaderList : headerList) {
            line += aHeaderList + "\t";
        }
		FileOperations.writeLine(bufferedWriter, line);

		// print the new nl.vumc.collapsesyntax.data
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
		String curItem, prevGeneralItemName="", generalItemName;

		// Split the header by tab
		splitHeader = line.split("\t");
		
		// For all header items
        for (String aSplitHeader : splitHeader) {
            curItem = aSplitHeader;
            // OC does something odd with the ProtocolID, which in the nl.vumc.collapsesyntax.data is named Protocol ID and in the syntax ProtocolID. Remove the space.
            curItem = curItem.replace(" ", "");
            // split by "_"
            splitItem = curItem.split("_");

            // translate the name to the general (CF) name
            generalItemName = Common.getGeneralName(splitItem);

            if (!headerList.contains(generalItemName)) {
                int index = headerList.indexOf(prevGeneralItemName);
                headerList.add(index + 1, generalItemName.trim());
            }

            prevGeneralItemName = generalItemName;
        }
	}

	private String [] getNewOutputArray(){
		// length of output is defined by the size of the new header
		String [] output = new String[headerList.size()];
		for(int i=0; i<output.length; i++){
			output[i]="";
		}
		return output;
	}
	
	// Parse the nl.vumc.collapsesyntax.data
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
                // use the generalHeaderName to get the index, which defines at which position our nl.vumc.collapsesyntax.data should now be located
                index = headerList.indexOf(generalHeaderName);
                // store the nl.vumc.collapsesyntax.data at that position
                output[index] = curVal;
			}
		}
		
		dataList.add(output);
	}
	
	public List<String> getHeaderList(){
		return headerList;
	}

	private String [] splitHeader;
	private final List<String> headerList = new LinkedList<>();
	private final List<String[]> dataList = new LinkedList<>();
	
//	private String workDir;
	private final String dataFile;
}