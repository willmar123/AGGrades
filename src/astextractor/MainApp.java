package astextractor;

import helpers.ParseHelpers;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Contains the main class of the application.
 * 
 * @author themis
 */
public class MainApp {

	/**
	 * Prints the help message of the command line interface.
	 */
	private static void printHelpMessage() {
		System.out.println("ASTExtractor: Abstract Syntax Tree Extractor for Java Source Code\n");
		System.out.println("Run as:\n java -jar ASTExtractor.jar -project=\"path/to/project\""
				+ " -properties=\"path/to/propertiesfile\" -repr=XML|JSON");
		System.out.println("Or as:\n java -jar ASTExtractor.jar -file=\"path/to/file\""
				+ " -properties=\"path/to/propertiesfile\" -repr=XML|JSON");
		System.out.println("where -properties allows setting the location of the properties file"
				+ " (default is no properties so all syntax tree nodes are returned)");
		System.out.println("and -repr allows selecting the representation of the tree (default is XML)");
	}

	/**
	 * Executes the application.
	 * 
	 * @param args arguments for executing in command line mode.
	 */
	public static void main(String args[]) {
		if (args.length > 0) {
			String project ="";
			String file = "exams//87698-ESPOSITO-ROBERTO//src//Esempio.java";
			String properties = "ASTExtractor.properties";
			String repr = "JSON";
			if (project.length() > 0 || file.length() > 0) {
				ASTExtractorProperties.setProperties(properties);
				String result = "";
				if (project.length() > 0)
					result = ASTExtractor.parseFolder(project, repr);
				else if (file.length() > 0)
					result = ASTExtractor.parseFile(file, repr);
				    System.out.println(result);
			} else {
				printHelpMessage();
			}
		} else {
			printHelpMessage();
		}
	}
}
