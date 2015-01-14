/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import nl.vumc.collapsesyntax.data.Data;
import nl.vumc.collapsesyntax.data.ItemDefs;
import nl.vumc.collapsesyntax.shared.Common;
import nl.vumc.collapsesyntax.shared.FileOperations;


// Graphical User Interface. 
// Contains main

// The GUI is a JPanel
public class CollapseSyntaxGUI extends JPanel implements ActionListener {
	CollapseSyntaxGUI(){
		// Setup our panel with a borderlayout
		super(new BorderLayout());
		setup();
	}

	private void setup(){
		//Create a file chooser and filters for xml and html
		dataFilter = new FileNameExtensionFilter("dat files", "dat");
		syntaxFilter = new FileNameExtensionFilter("syntax files", "sps");

		// create the uneditable log area with a scroll pane
		logArea = new JTextArea(30,150);
		logArea.setEditable(false);
		logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		FileOperations.setLogArea(logArea);
		JScrollPane logScrollPane = new JScrollPane(logArea);

		// Create the top panel and the bottom panel
		JPanel topPanel = topPaneSetup();
		JPanel bottomPanel = bottomPaneSetup();

		// Add the panels to the borderlayout
		add(topPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);

	}

	// create the menubar
	private JMenuBar createMenuBar(){
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		menuBar = new JMenuBar();

		// Build the File menu
		menu = new JMenu("File");
		menuItem = new JMenuItem("Exit");
		menuItem.setActionCommand("Exit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		// Build the About menu
		menu = new JMenu("About");
		menuItem = new JMenuItem("Help");
		menuItem.setActionCommand("Help");
		menuItem.addActionListener(this);
		menu.add(menuItem);

        menuItem = new JMenuItem("License");
        menuItem.setActionCommand("License");
        menuItem.addActionListener(this);
        menu.add(menuItem);

		menuItem = new JMenuItem("About");
		menuItem.setActionCommand("About");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		// Return the menuBar
		return menuBar;
	}

	// create the bottom panel
	private JPanel bottomPaneSetup(){
		JPanel buttonPanel = new JPanel();
		JButton button;

		// create the Run Button
		button = new JButton("Run");
		button.setPreferredSize(new Dimension(150,30));
		button.setActionCommand("Run");
		button.addActionListener(this);
		buttonPanel.add(button);

		// create the Exit Button
		button = new JButton("Exit");
		button.setPreferredSize(new Dimension(150,30));
		button.setActionCommand("Exit");
		button.addActionListener(this);
		buttonPanel.add(button);

		return buttonPanel;
	}

	// create standard GridBagConstraints
	private static GridBagConstraints getDefaultConstraints(){
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		// set the xweight for the component. As all components (unless overwritten) have 
		// the same weight, they will all have the same sizes  
		gridBagConstraints.weightx = 0.5;
		// margins
		gridBagConstraints.insets = new Insets(3,3,3,3);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		return gridBagConstraints;
	}

	// create the top Panel
	private JPanel topPaneSetup(){
		JPanel pane = new JPanel();
		JButton button;
		JLabel label;

		GridBagConstraints gridBagConstraints;

		// set the layout to a gridbaglayout
		pane.setLayout(new GridBagLayout());

		// text label
		label = new JLabel("Data File");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		pane.add(label, gridBagConstraints);

		// textfield
		dataFileTextField = new JTextField(20);
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		pane.add(dataFileTextField, gridBagConstraints);

		// button
		button = new JButton("Browse");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.2; // smaller weight
		button.setActionCommand("BrowseDataFile");
		button.addActionListener(this);
		pane.add(button, gridBagConstraints);

		// text label
		label = new JLabel("Syntax File");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		pane.add(label, gridBagConstraints);

		// textfield
		syntaxFileTextField = new JTextField(20);
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		pane.add(syntaxFileTextField, gridBagConstraints);

		// button
		button = new JButton("Browse");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.2; // smaller weight
		button.setActionCommand("BrowseSyntaxFile");
		button.addActionListener(this);
		pane.add(button, gridBagConstraints);

		return pane;
	}


	// create and show the GUI
	private static void createAndShowGUI() {
		// create the frame
		JFrame frame = new JFrame("OC Syntax Collapse");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// create our panel and add it to the frame
		CollapseSyntaxGUI translateGUI = new CollapseSyntaxGUI(); 
		frame.add(translateGUI);
		// add the menubar to the frame
		frame.setJMenuBar(translateGUI.createMenuBar());
		
		// change the icon to a ctmm icon 
		URL url = FileOperations.getURL("ctmmSymbol.jpg");
		ImageIcon img = new ImageIcon(url);
		frame.setIconImage(img.getImage());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	// create a new filechooser based on the directory and the filter
	private JFileChooser getFileChooser(FileNameExtensionFilter filter){
		JFileChooser fileChooser = new JFileChooser(dir);
		fileChooser.setFileFilter(filter);
		return fileChooser;
	}

	// browse file
	private void browseFile(JTextField textField, JFileChooser fileChooser){
		// show the fileChooser
		int returnVal = fileChooser.showOpenDialog(this);

		// if ok is pressed, set the textfield to the selected file and store the directory 
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile(); 
			textField.setText(selectedFile.getAbsolutePath());
			dir = selectedFile.getParent();
		} else {
			logArea.append("Choose command cancelled by user" + newline);
		}
	}

	// check whether both the crfVersionFileTextField and the ruleFileTextField exist 
	private boolean fileExists(String fileName){
		if(!FileOperations.fileExists(fileName)){
			JOptionPane.showMessageDialog(this, "The file "+fileName+" does not exist", "There was a problem", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	// run the translator
	private void runProgram(){
		String dataFile = dataFileTextField.getText().trim();
		String syntaxFile = syntaxFileTextField.getText().trim();
		// check whether both the crfVersionFileTextField and the ruleFileTextField exist
		if(fileExists(dataFile)&&fileExists(syntaxFile)){
			logArea.append("Running Syntax Collapse..."+newline);
			// ensure the directory variable is set properly 
			dir = dataFile.substring(0,dataFile.lastIndexOf(fileSeparator));
			try{
				Data data = new Data(dataFile);
				data.readDataFile();
				data.generateDataFile();
				
				logArea.append(newline);
				
				ItemDefs itemDefs = new ItemDefs(syntaxFile);
				itemDefs.readSyntaxFile();
				itemDefs.generateSyntaxFile(data.getHeaderList());
				
				logArea.append("\nFinshed");
				
			} catch(Exception e){
				// show message if not successful
				logArea.append(e.toString());
				JOptionPane.showMessageDialog(this, "The rule translator encountered a problem:"+newline+e.toString(),
						"There was a problem", JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			logArea.append("Please specify a valid data file and a valid syntax file" + newline);
		}

	}

	// show about information
	private void showAbout(){
		// create image
		URL url = FileOperations.getURL("tracer_trait_vumc.jpg");
		ImageIcon img = new ImageIcon(url);
		// show dialog
		JOptionPane.showMessageDialog(this,
			"Developed at the VU University Medical Center for CTMM TRACER by TRACER ICT"+newline+
			"Developer: Sander de Ridder"+newline+
            "WP-lead: Jeroen Beliën"+newline+
            "Tested by: Rinus Voorham (VU University Medical Center), Marinel Cavelaars (The Hyve)"+newline+
                    "Gerben Rienk Visser (Trial Data Solutions) and Marieke Vianen (University Medical Center Utrecht)"+newline+newline+
            "This tool is now supported by CTMM TraIT."+newline+
			"Please contact servicedesk@ctmm-trait.nl for questions and comments"+newline+newline+
            "Program version "+version+newline+newline,
            "About",
			JOptionPane.INFORMATION_MESSAGE, img);
	}

    private void showLicense(){
        URL url = FileOperations.getURL("CTMM VUmc.jpg");
        ImageIcon img = new ImageIcon(url);
        // show dialog
        JOptionPane.showMessageDialog(this,
            "This program is Copyright 2014 VU University Medical Center / Center for Translational Molecular Medicine, " +newline+
            "and can be reused under the Apache 2.0 license. Unless required by applicable law or agreed to in writing, "+newline+
            "software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR " +newline+
            "CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing " +newline+
            "permissions and limitations under the License."+newline+
            "You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0",
            "License",
            JOptionPane.INFORMATION_MESSAGE, img);
    }

	// show help
	private void showHelp(){
		Help.showHelp();
	}

	// handle actions
	public void actionPerformed(ActionEvent e) {
		// Handle open browse button for rules file.
		if (e.getActionCommand().equalsIgnoreCase("BrowseDataFile")) {
			JFileChooser fileChooser = getFileChooser(dataFilter);
			browseFile(dataFileTextField, fileChooser);
		}
		// Handle open browse button for crfVersion file.
		else if (e.getActionCommand().equalsIgnoreCase("BrowseSyntaxFile")) {
			JFileChooser fileChooser = getFileChooser(syntaxFilter);
			browseFile(syntaxFileTextField, fileChooser);
		}
		// Handle run
		else if (e.getActionCommand().equalsIgnoreCase("Run")) {
			runProgram();
		}
		// Handle exit
		else if (e.getActionCommand().equalsIgnoreCase("Exit")) {
			System.exit(0);
		}
        else if (e.getActionCommand().equalsIgnoreCase("License")) {
            showLicense();
        }
		// Handle about
		else if (e.getActionCommand().equalsIgnoreCase("About")) {
			showAbout();
		}
		// Handle help
		else if (e.getActionCommand().equalsIgnoreCase("Help")) {
			showHelp();
		}
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	// main
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
	}

	private String dir="";
	private JTextField dataFileTextField, syntaxFileTextField;
	private JTextArea logArea;
	private FileNameExtensionFilter dataFilter;
	private FileNameExtensionFilter syntaxFilter;

	private static final String newline = Common.getNewLine();
	private static final String fileSeparator = System.getProperty("file.separator");
//	private static final long serialVersionUID = -7755429999545798394L;
	private static final double version = 0.9;
}

