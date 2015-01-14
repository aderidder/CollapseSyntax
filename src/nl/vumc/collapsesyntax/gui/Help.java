/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.vumc.collapsesyntax.shared.FileOperations;

// help is also a panel
class Help extends JPanel implements ListSelectionListener {
	private Help(){
		// panel with borderlayout
		super(new BorderLayout());
		setup();
	}

	private void setup(){
		// setup the help panel
		JSplitPane helpPanel = helpPaneSetup();
		// add the help panel to the center of the borderlayout
		add(helpPanel, BorderLayout.CENTER);
	}

	// setup the help panel, which is a splitpane
	// on the left we will have the topics; on the right the text for the selected topic
	private JSplitPane helpPaneSetup(){
		// create a new list based on the help topics
		// the model is single select and we start by setting the selected index to the first topic
        JList<String> list = new JList<>(helpTopics);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);

		// create a scrollpanel for the list
		JScrollPane listScrollPane = new JScrollPane(list);

		// create an editorpanel which accepts html
		rightArea = new JEditorPane();
		rightArea.setContentType("text/html");
		rightArea.setEditable(false);

		// add the panel to a scroll pane
		JScrollPane rightScrollPane = new JScrollPane(rightArea);

		//Create the split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, rightScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//Provide minimum sizes for the two components in the split pane.
		Dimension minimumSize = new Dimension(200, 100);
		listScrollPane.setMinimumSize(minimumSize);
		rightScrollPane.setMinimumSize(minimumSize);

		//Provide a preferred size for the split pane.
		splitPane.setPreferredSize(new Dimension(1000, 400));

		// show the first help topic
		showHelp(list.getSelectedIndex());
		return splitPane;
	}

	// close the window
	private static void closeWindow(){
		if(helpAlreadyShown){
			helpAlreadyShown = false;
			frame.dispose();
		}
	}

	// create and show the GUI
	private static void createAndShowGUI(){
		// used for check to see if a help window is already open
		helpAlreadyShown = true;
		
		// create the frame and set the close operation to do nothing
//		frame = new JFrame("nl.vumc.collapsesyntax.gui.Help");
        frame = new JFrame("Help");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// add the window listener and call closeWindow when a windowclosing event occurs
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				closeWindow();
			}
		});

		// create the help panel and add it to the frame
		Help help = new Help(); 
		frame.add(help);

		// add the ctmm icon to the frame
		URL url = FileOperations.getURL("ctmmSymbol.jpg");
		ImageIcon img = new ImageIcon(url);
		frame.setIconImage(img.getImage());

		//Display the window.
		frame.pack();
		frame.setVisible(true);

		//		System.out.println(nl.vumc.collapsesyntax.gui.Help.class.getResource("/").getPath());
	}

	// show help, based on the selected index
	private void showHelp(int index){
		rightArea.setText(helpTexts[index]);
		rightArea.setCaretPosition(0);
	}

	// event listener for list
	public void valueChanged(ListSelectionEvent e) {
		JList<?> list = (JList<?>)e.getSource();
		showHelp(list.getSelectedIndex());
	}

	// setup the help topics
	private static String [] setupHelpTopics(){
		helpTopics = new String[4];
		helpTopics[0] = "General";
        helpTopics[1] = "Technical Details";
		helpTopics[2] = "Using the Tool";
		helpTopics[3] = "Example";
		return helpTopics;
	}
	
	// setup the help texts
	private static String [] setupHelpTexts(){
		helpTexts = new String[helpTopics.length];
		helpTexts[0] = HelpTexts.getText0();
		helpTexts[1] = HelpTexts.getText2();
		helpTexts[2] = HelpTexts.getText3();
		helpTexts[3] = HelpTexts.getText1();
		return helpTexts;
	}



	// show help
	public static void showHelp(){
		// check whether a helpwindow already exists
		if(!helpAlreadyShown){
			javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
		}
	}

    private JEditorPane rightArea;

	private static JFrame frame;
	private static boolean helpAlreadyShown = false;
	private static String[] helpTopics = setupHelpTopics();
	private static String[] helpTexts = setupHelpTexts();

//	private static final long serialVersionUID = 331623305138507005L;

}

