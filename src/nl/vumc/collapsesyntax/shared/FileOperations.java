/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.shared;

// SharedFileOperations.java
// Class for file operations
import nl.vumc.collapsesyntax.gui.CollapseSyntaxGUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
//import java.io.FilenameFilter;
import java.net.URL;

import javax.swing.JTextArea;

public class FileOperations{
	public static String getURLString(String fileName){
        URL url = CollapseSyntaxGUI.class.getResource("images/" +fileName);
        return url.toString();
	}
	
	public static URL getURL(String fileName){
        return CollapseSyntaxGUI.class.getResource("images/" +fileName);
	}
	

	public static String generateOutfileName(String fileName){
		return fileName.substring(0, fileName.lastIndexOf(".")) + "_new"+fileName.substring(fileName.lastIndexOf("."));
	}
	
	// open file and return buffered reader
	public static BufferedReader openFileReader(String fileName){
		BufferedReader in = null;
		try{
			in = new BufferedReader(new FileReader(fileName));
		} catch (Exception e){
			System.out.println("Error opening file "+fileName+"\nError is: "+e.toString());
			System.exit(1);
		}
		return in;
	}

	public static BufferedWriter openFileWriter(String fileName){
		return openFileWriter(fileName, false);
	}
	
	// open file and return buffered writer
	public static BufferedWriter openFileWriter(String fileName, Boolean append){
		BufferedWriter out = null;
		try {
			//Construct the BufferedWriter object
			out = new BufferedWriter(new FileWriter(fileName, append));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return out;
	}

	// close filereader
	public static void closeFileReader(BufferedReader in){
		try{
			in.close();
		} catch (Exception e){
			System.out.println("Error closing file\nError is: "+e.toString());
			System.exit(1);
		}
	}

	// close filewriter
	public static void closeFileWriter(BufferedWriter out){
		try {
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void writeLine(BufferedWriter out, String line){
		try{
			logArea.append(line+Common.getNewLine());
			out.write(line);
			out.newLine();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean fileExists(String fileName){
		File file = new File(fileName);
		return file.exists();
	}
	
	public static void setLogArea(JTextArea logArea){
		FileOperations.logArea = logArea;
	}
	private static JTextArea logArea;
}
