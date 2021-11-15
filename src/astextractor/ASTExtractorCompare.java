package astextractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.XML;

import astparser.JavaASTParserCompare;
import helpers.FileSystemHelpers;
import helpers.XMLHelpers;

/**
 * Contains all functions for extracting Abstract Syntax Trees (ASTs) from java files.
 * 
 * @author themis
 */
public class ASTExtractorCompare {

	/**
	 * Parses the contents of a java file and returns its AST.
	 * 
	 * @param fileContents the contents of a java file, given as a String.
	 * @return a String containing the AST of the java file in XML format.
	 * @throws IOException 
	 */
	public static String parseString(String fileContents1, String fileContents2) throws IOException {
		return parseString(fileContents1,fileContents2, "XML");
	}

	/**
	 * Parses the contents of a java file and returns its AST.
	 * 
	 * @param fileContents the contents of a java file, given as a String.
	 * @param astFormat the format of the returned AST, either "XML" or "JSON".
	 * @return a String containing the AST of the java file in XML or JSON format.
	 * @throws IOException 
	 */
	public static String parseString(String fileContents1, String fileContents2, String astFormat) throws IOException {
		String result = JavaASTParserCompare.parse(fileContents1,fileContents2);
		if (astFormat.equals("JSON"))
			return XML.toJSONObject(result).toString(3);
		else
			return XMLHelpers.formatXML(result, 3);
	}

	/**
	 * Parses a java file and returns its AST.
	 * 
	 * @param filename the filename of the java file to be parsed.
	 * @return a String containing the AST of the java file in XML format.
	 * @throws IOException 
	 */
	public static String parseFile(String filename1 , String filename2) throws IOException {
		return parseFile(filename1, filename2,"XML");
	}

	/**
	 * Parses a java file and returns its AST.
	 * 
	 * @param filename the filename of the java file to be parsed.
	 * @param astFormat the format of the returned AST, either "XML" or "JSON".
	 * @return a String containing the AST of the java file in XML or JSON format.
	 * @throws IOException 
	 */
	public static String parseFile(String filename1,String filename2, String astFormat) throws IOException {
		String result = parseString(FileSystemHelpers.readFileToString(filename1), FileSystemHelpers.readFileToString(filename2));
		if (astFormat.equals("JSON"))
			return XML.toJSONObject(result).toString(3);
		else
			return XMLHelpers.formatXML(result, 3);
	}

	/**
	 * Parses all the files of a folder and returns a unified AST.
	 * 
	 * @param folderName the path of the folder of which the files are parsed.
	 * @return an AST containing all the files of a folder in XML format.
	 * @throws IOException 
	 */
	public static String parseFolder(String folderName) throws IOException {
		return parseFolder(folderName, "XML");
	}

	/**
	 * Parses all the files of a folder and returns a unified AST.
	 * 
	 * @param folderName the path of the folder of which the files are parsed.
	 * @param astFormat the format of the returned AST, either "XML" or "JSON".
	 * @return an AST containing all the files of a folder in XML or JSON format.
	 * @throws IOException 
	 */
	public static String parseFolder(String folderName, String astFormat) throws IOException {
		String folderAbsolutePath = new File(folderName).getAbsolutePath();
		ArrayList<File> files = FileSystemHelpers.getJavaFilesOfFolderRecursively(folderName);
		StringBuilder results = new StringBuilder("<folder>\n");
		for (File file : files) {
			String fileAbsolutePath = file.getAbsolutePath();
			String filePath = FileSystemHelpers.getRelativePath(folderAbsolutePath, fileAbsolutePath);
			String result = parseFile(fileAbsolutePath);
			results.append("<file>\n<path>" + filePath + "</path>\n<ast>\n" + result + "</ast>\n</file>\n");
		}
		results.append("</folder>\n");
		if (astFormat.equals("JSON"))
			return XML.toJSONObject(results.toString()).toString(3);
		else
			return XMLHelpers.formatXML(results.toString(), 3);
	}
}
