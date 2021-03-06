
/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package nl.vumc.collapsesyntax.gui;

import nl.vumc.collapsesyntax.shared.FileOperations;

class HelpTexts {
    static String getCSS(){
        String cssString=
            "<style>" +
                "table, th, td {" +
                "   border: 1px solid black;" +
                "   border-collapse: collapse;" +
                "}" +
                ".quote{" +
                "   margin: 0px 25px;" +
                "   font-style: italic;" +
                "   font-size: small;" +
                "}" +
                "</style>";
        return cssString;
    }

    static String getText0(){
        String file = FileOperations.getURLString("preCollapse.jpg");
        String file2 = FileOperations.getURLString("postCollapse.jpg");
        String text;
//        try{
            text="<html><head></head><body>"+
                    "<h1>Introduction</h1>" +
                    "<h2>Background</h2>" +
                    "Multiple version of a CRF may be used in a study. When creating an " +
                    "SPSS export containing multiple CRF versions, each selected item " +
                    "of each version will get its own column in the export. This " +
                    "can make analysing the data very complicated and often the user " +
                    "will have to collapse the data, to make it workable." +
                    "<h2>Aim</h2>"+
                    "The aim of this tool is to collapse the columns of an SPSS export " +
                    "generated by OpenClinica, when multiple CRF versions are exported. " +
                    "The tool collapses CRF data within an event." +

                    "<h2>General Idea</h2>" +
                    "In a nutshell, the collapse tool does the following:<br>" +
                    "Let's say we have a CRF with one item, personWeight, and that when " +
                    "we create and export, OC calls it personWeight_E2_C97. " +
                    "Now let's say we create a new version of this CRF, and that when " +
                    "we create an export, OC calls it personWeight_E2_C98. " +
                    "In an export that contains both versions of the CRF, you will get " +
                    "both these columns: <br><br>" +
                    "<img border=\"0\" src=\""+file+"\" alt=\"PreCollapse\"><br>"+
                    "By running the collapse tool on the exported data, a collapsed " +
                    "datafile and syntaxfile are created in which " +
                    "these columns are merged into one column called personWeight_E2_CF:<br><br>" +
                    "<img border=\"0\" src=\""+file2+"\" alt=\"PostCollapse\"><br>"+
                    "<h2>Caution!</h2>" +
                    "The tool wil <u>not</u> work correctly when items that should " +
                    "not be collapsed have an identical name. " +
                    "Reason for this is that, in the column names, OC does not differentiate " +
                    "between two versions of a single CRF and two unrelated CRFs. " +
                    "Hence, if an export contains startDate_E2_C97 and startDate_E2_C98 " +
                    "it could mean they are items in completely unrelated CRFs. The program, however, " +
                    "will always assume they are items in two versions of the same CRF.<br>" +
                    "Furthermore, please be aware that when an option from the RESPONSE_OPTIONS_TEXT " +
                    "and RESPONSE_VALUES_OR_CALCULATIONS is removed in a new CRF version, " +
                    "OC also loses the removed value and code for the old CRF versions. This is a bug in "+
                    "OC's export. The collapse tool creates a superset of the coding and text "+
                    "to ensure all codes and text as generated by the export are present.<br>"+
            "</body></html>";
//        } catch(Exception e){
//            System.out.println("couldn't find picture");
//            System.out.println(gui.Help.class.getResource("/").getPath());
//        }

        return text;
    }

    static String getText1(){
        String text;
        text="<html>" +
                "<head>" +
                "<style>" +
                "table, th, td {" +
                "    border: 1px solid black;" +
                "    border-collapse: collapse;" +
                "}" +
                ".quote{" +
                "margin: 0px 25px;" +
                "font-style: italic;" +
                "font-size: small;" +
                "}" +
                "" +
                "</style>" +
                "</head>" +
                "<body>"+
                "<h1>Example</h1>" +
                "<h2>Case</h2>" +
                "Consider the case where we have a very simple CRF which " +
                "stores which Biologicals a rheumatoid arthritis patient is using, as well as " +
                "the start date of the medication. In this example a patient can " +
                "have an unknown number of Biologicals and start dates and the CRF therefore " +
                "contains a single repeating group with two items. Furthermore, the event " +
                "itself is a Repeating Event.<br>" +
                "There are two versions of this CRF and our SPSS export contains all items of both version." +

                "<h2>The Export Files</h2>" +
                "OpenClinica's SPSS export creates two files, a datafile called 'SPSS_DAT.dat' " +
                "and a syntax file called 'SPSS_SPS.sps'. For " +
                "our example the datafile contains the following:<br><br>" +
                "<div class='quote'>" +
                "<table>" +
                "<tr>" +
                    "<th>StudySubjectID</th>" +
                    "<th>ProtocolID</th>" +
                    "<th>BiologicalWhich_E2_1_C97_1</th>" +
                    "<th>BiologicalStartDate_E2_1_C97_1</th>" +
                    "<th>BiologicalWhich_E2_1_C97_2</th>" +
                    "<th>BiologicalStartDate_E2_1_C97_2</th>" +
                    "<th>BiologicalWhich_E2_1_C98_1</th>" +
                    "<th>BiologicalStartDate_E2_1_C98_2</th>" +
                "</tr>" +
                "<tr>" +
                    "<td>Patient1</td>" +
                    "<td>1234567</td>" +
                    "<td>Adalimumab</td>" +
                    "<td>06/02/2007</td>" +
                    "<td>Rituximab</td>" +
                    "<td>06/28/2014</td>" +
                    "<td></td>" +
                    "<td></td>" +
                "</tr>" +
                "<tr>" +
                    "<td>Patient2</td>" +
                    "<td>1234567</td>" +
                    "<td>Kineret</td>" +
                    "<td>06/01/2013</td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                "</tr>" +
                "<tr>" +
                    "<td>Patient3</td>" +
                    "<td>1234567</td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td>Rituximab</td>" +
                    "<td><06/18/2014</td>" +
                "</tr>" +
                "</table>" +
                "</div>" +
                "<br>" +
                "The two CRF versions are C97 and C98. " +
                "Patient1 has 2 repeats in the old CRF version, Patient2 " +
                "has 1 repeat in the old CRF version and Patient3 has 1 repeat in the new CRF version.<br>" +

                "The original Syntax File, as generated by OpenClinica, contains the following information:<br><br>" +
                "<div class='quote'>"+
                "* NOTE: If you have put this file in a different folder <br>" +
                "* from the associated data file, you will have to change the FILE <br>" +
                "* location on the line below to point to the physical location of your data file.<br>" +
                "GET DATA  /TYPE = TXT/FILE = 'SPSS_DAT.dat' /DELCASE = LINE /DELIMITERS = \"\\t\" /ARRANGEMENT = DELIMITED /FIRSTCASE = 2 /IMPORTCASE = ALL /VARIABLES =<br>" +
                "StudySubjectID A15<br>" +
                "ProtocolID A32<br>" +
                " BiologicalWhich_E2_1_C97_1  F4.0<br>" +
                " BiologicalStartDate_E2_1_C97_1  ADATE10<br>" +
                " BiologicalWhich_E2_1_C97_2  F4.0<br>" +
                " BiologicalStartDate_E2_1_C97_2  ADATE10<br>" +
                " BiologicalWhich_E2_1_C98_1  F4.0<br>" +
                " BiologicalStartDate_E2_1_C98_1  ADATE10<br>" +
                ".<br>" +
                "VARIABLE LABELS<br>" +
                "StudySubjectID \"Study Subject ID\" /<br>" +
                "ProtocolID \"Protocol ID\" /<br>" +
                " BiologicalWhich_E2_1_C97_1  \"TRACER_Medication_Biologicals_Which\" /<br>" +
                " BiologicalStartDate_E2_1_C97_1  \"TRACER_Medication_Biologicals_StartDate\" /<br>" +
                " BiologicalWhich_E2_1_C97_2  \"TRACER_Medication_Biologicals_Which\" /<br>" +
                " BiologicalStartDate_E2_1_C97_2  \"TRACER_Medication_Biologicals_StartDate\" /<br>" +
                " BiologicalWhich_E2_1_C98_1  \"TRACER_Medication_Biologicals_Which\" /<br>" +
                " BiologicalStartDate_E2_1_C98_1  \"TRACER_Medication_Biologicals_StartDate\" /<br>" +
                ".<br>" +
                "VALUE LABELS<br>" +
                "BiologicalWhich_E2_1_C97_1<br>" +
                "'1' \"Rituximab\"<br>" +
                "'2' \"Adalimumab\"<br>" +
                "'3' \"Infliximab\"<br>" +
                "'4' \"Kineret\"<br>" +
                "'-1' \"No Information\"<br>" +
                "/<br>" +
                "BiologicalWhich_E2_1_C97_2<br>" +
                "'1' \"Rituximab\"<br>" +
                "'2' \"Adalimumab\"<br>" +
                "'3' \"Infliximab\"<br>" +
                "'4' \"Kineret\"<br>" +
                "'-1' \"No Information\"<br>" +
                "/<br>" +
                "BiologicalWhich_E2_1_C98_1<br>" +
                "'1' \"Rituximab\"<br>" +
                "'2' \"Adalimumab\"<br>" +
                "'3' \"Infliximab\"<br>" +
                "'4' \"Kineret\"<br>" +
                "'-1' \"No Information\"<br>" +
                "/<br>" +
                ".<br>" +
                "EXECUTE.<br>"+
                "</div>"+

                "For our analysis we would like patient3's data to be in the same columns as patient1 " +
                "and patient2's data." +

                "<h2>After Collapse</h2>" +
                "After running the tool, we have two new files: 'SPSS_DAT_new.dat' and 'SPSS_SPS_new.sps'. " +
                "The datafile now contains CF instead of C97 and C98. Patient3's data, which was in C98's " +
                "first repeat, is now located in CF's first repeat.<br>" +
                "<div class='quote'>" +
                "<table>" +
                "<tr>" +
                "<th>StudySubjectID</th>" +
                "<th>ProtocolID</th>" +
                "<th>BiologicalWhich_E2_1_CF_1</th>" +
                "<th>BiologicalStartDate_E2_1_CF_1</th>" +
                "<th>BiologicalWhich_E2_1_CF_2</th>" +
                "<th>BiologicalStartDate_E2_1_CF_2</th>" +
                "</tr>" +
                "<tr>" +
                "<td>Patient1</td>" +
                "<td>1234567</td>" +
                "<td>Adalimumab</td>" +
                "<td>06/02/2007</td>" +
                "<td>Rituximab</td>" +
                "<td>06/28/2014</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Patient2</td>" +
                "<td>1234567</td>" +
                "<td>Kineret</td>" +
                "<td>06/01/2013</td>" +
                "<td></td>" +
                "<td></td>" +
                "</tr>" +
                "<tr>" +
                "<td>Patient3</td>" +
                "<td>1234567</td>" +
                "<td>Rituximab</td>" +
                "<td><06/18/2014</td>" +
                "<td></td>" +
                "<td></td>" +
                "</tr>" +
                "</table>" +
                "</div>" +
                "<br>" +
                "The syntax file is also updated to the generated CF structure:<br><br>" +
                "<div class='quote'>" +
                "* NOTE: If you have put this file in a different folder <br>" +
                "* from the associated data file, you will have to change the FILE <br>" +
                "* location on the line below to point to the physical location of your data file.<br>" +
                "GET DATA  /TYPE = TXT/FILE = 'D:/temp/syntaxTest/simple/SPSS_DAT_new.dat' /DELCASE = LINE /DELIMITERS = \"\\t\" /ARRANGEMENT = DELIMITED /FIRSTCASE = 2 /IMPORTCASE = ALL /VARIABLES =<br>" +
                "StudySubjectID A15<br>" +
                "ProtocolID A32<br>" +
                "BiologicalWhich_E2_1_CF_1 F4.0<br>" +
                "BiologicalStartDate_E2_1_CF_1 ADATE10<br>" +
                "BiologicalWhich_E2_1_CF_2 F4.0<br>" +
                "BiologicalStartDate_E2_1_CF_2 ADATE10<br>" +
                ".<br>" +
                "VARIABLE LABELS<br>" +
                "StudySubjectID \"Study Subject ID\" /<br>" +
                "ProtocolID \"Protocol ID\" /<br>" +
                "BiologicalWhich_E2_1_CF_1 \"TRACER_Medication_Biologicals_Which\" /<br>" +
                "BiologicalStartDate_E2_1_CF_1 \"TRACER_Medication_Biologicals_StartDate\" /<br>" +
                "BiologicalWhich_E2_1_CF_2 \"TRACER_Medication_Biologicals_Which\" /<br>" +
                "BiologicalStartDate_E2_1_CF_2 \"TRACER_Medication_Biologicals_StartDate\" /<br>" +
                ".<br>" +
                "VALUE LABELS<br>" +
                "BiologicalWhich_E2_1_CF_1<br>" +
                "'1' \"Rituximab\"<br>" +
                "'2' \"Adalimumab\"<br>" +
                "'3' \"Infliximab\"<br>" +
                "'4' \"Kineret\"<br>" +
                "'-1' \"No Information\"<br>" +
                "/<br>" +
                "BiologicalWhich_E2_1_CF_2<br>" +
                "'1' \"Rituximab\"<br>" +
                "'2' \"Adalimumab\"<br>" +
                "'3' \"Infliximab\"<br>" +
                "'4' \"Kineret\"<br>" +
                "'-1' \"No Information\"<br>" +
                "/<br>" +
                ".<br>" +
                "EXECUTE." +
                "</div>" +
                "<br>" +

                "</body></html>";
        return text;
    }

    static String getText2() {
        String text;
        text =  "<html><head></head>" +
                "<body>"+
                "<h1>Technical Details</h1>" +
                "Basically, the tool transforms the C&lt;Nr&gt; part of the name to CF " +
                "and moves the data to the appropriate CF column. The following examples show " +
                "the possible naming extension and to which general name they are transformed." +
                "<ul>" +
                "<li>No repeats: personWeight_E2_C97 &#8594; personWeight_E2_CF</li>"+
                "<li>Repeating event: personWeight_E2_1_C97 &#8594; personWeight_E2_1_CF</li>" +
                "<li>Repeating group: personWeight_E2_C97_1 &#8594; personWeight_E2_CF_1</li>" +
                "<li>Repeating event and group: personWeight_E2_1_C97_1 &#8594; personWeight_E2_1_CF_1</li>" +
                "</ul>" +
                "Furthermore, the syntaxfile is updated:" +
                "<ul>" +
                "<li>The name of the datafile mentioned in the syntaxfile is updated to _new.dat</li>" +
                "<li>Width of the new field is set to the maximum found for the uncollapsed fields</li>" +
                "<li>Value labels are set to the union of the uncollapsed fields, to ensure all codes have labels</li>" +
                "</ul>" +
                "</body>" +
                "</html>";
        return text;
    }

    static String getText3() {
        String file = FileOperations.getURLString("UseCollapseTool1.jpg");
        String text;
        text =  "<html><head></head>" +
                "<h1>Using the Tool</h1>" +
                "Using the tool is quite easy. Select your datafile and syntaxfile by " +
                "clicking the browse buttons. After the files have been selected " +
                "click the run button and the program will collapse the files. " +
                "The Log Area will show the result of the collapse. " +
                "Furthermore, after running the program, your directory will contain collapsed versions " +
                "of both the data and the syntax file.<br><br>" +
                "<img border=\"0\" src=\""+file+"\"  height=\"441\" width=\"800\" alt=\"UseCollapse\"><br>"+
                "</body>" +
                "</html>";
        return text;
    }
}
