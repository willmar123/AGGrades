package astparser;

import static org.junit.Assert.assertEquals;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jface.text.Document;
import java.util.Arrays;

import astextractor.ASTExtractorProperties;

/**
 * Handles the parsing of java files and the extraction of their Abstract Syntax Trees (ASTs).
 * 
 * @author themis
 */
public class JavaASTParserCompare {

	/**
	 * Retrieves the children of an ASTNode.
	 * 
	 * @param node the ASTNode of which the children are retrieved.
	 * @return the children of the given ASTNode.
	 */
	public static ArrayList<ASTNode> childrenLeafV1 = new ArrayList<ASTNode>();
	public static ArrayList<ASTNode> childrenLeafV2 = new ArrayList<ASTNode>();
	public static ArrayList<ASTNode> differencesFound = new ArrayList<ASTNode>();
	public static ArrayList<ASTNode> differencesFoundExamA = new ArrayList<ASTNode>();
	static int totalImports,totalTypeDeclaration,Modifier,MethodDeclaration = 0;
	String Description;

	
	@SuppressWarnings("unchecked")
	private static ArrayList<ASTNode> getChildren(ASTNode node) {
		ArrayList<ASTNode> flist = new ArrayList<ASTNode>();
		List<Object> list = node.structuralPropertiesForType();
		for (int i = 0; i < list.size(); i++) {
			StructuralPropertyDescriptor curr = (StructuralPropertyDescriptor) list.get(i);
			Object child = node.getStructuralProperty(curr);
			if (child instanceof List) {
				flist.addAll((Collection<? extends ASTNode>) child);
			} else if (child instanceof ASTNode) {
				flist.add((ASTNode) child);
			} else {
			}
		}
		return flist;
	}

	/**
	 * Recursively visits all nodes of the AST and exports it as an XML StringBuffer.
	 * 
	 * @param result the result as a StringBuffer.
	 * @param indent the indent at the current level.
	 * @param node the current ASTNode that is examined.
	 */
	private static void visitNode(StringBuffer result, String indent, ASTNode node) {
		ArrayList<ASTNode> children = getChildren(node);
		String nodeType = ASTNode.nodeClassForType(node.getNodeType()).getSimpleName();
		if (ASTExtractorProperties.OMIT.contains(nodeType)) {
			// Do nothing
		} else if (ASTExtractorProperties.LEAF.contains(nodeType)) {
			result.append(indent + "<" + nodeType + ">");
			result.append(node.toString().trim().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
			result.append("</" + nodeType + ">\n");
		} else if (children.size() > 0) {
			result.append(indent + "<" + nodeType + ">\n");
			for (ASTNode child : children) {
				visitNode(result, indent + "   ", child);
			}
			result.append(indent + "</" + nodeType + ">\n");
		} else {
			result.append(indent + "<" + nodeType + ">");
			result.append(node.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
			result.append("</" + nodeType + ">\n");
		}
	}

	/**
	 * Visits an AST and exports it as an XML string.
	 * 
	 * @param root the root ASTNode of the tree.
	 * @return an XML string representation of the tree.
	 */
	protected static String visitTree(ASTNode root) {
		StringBuffer result = new StringBuffer("");
		visitNode(result, "", root);
		return result.toString();
	}

	
	/*
	 * private static String getImportPackageName(ImportDeclaration
	 * importDeclaration) { String importDescription; IBinding importBinding;
	 * importBinding= importDeclaration.resolveBinding(); //importDescription =
	 * importBinding.getName(); importDescription = "$"+importDeclaration.getName();
	 * List<StructuralPropertyDescriptor> props =
	 * importDeclaration.structuralPropertiesForType(); for
	 * (StructuralPropertyDescriptor property : props) { Object leftVal =
	 * importDeclaration.getStructuralProperty(property); if
	 * (property.isSimpleProperty()) {
	 * System.out.print("\n simple import "+property.toString()+" "+
	 * leftVal.toString()); }else if (property.isChildProperty()) {
	 * System.out.print("\nchild import "+property.toString()+" "); }else if
	 * (property.isChildListProperty()) { Iterator<ASTNode> leftValIt =
	 * ((Iterable<ASTNode>) leftVal) .iterator(); while (leftValIt.hasNext()) { //
	 * recursively call this function on child nodes
	 * System.out.print("\nchildList import"); ASTNode childListNode =
	 * leftValIt.next(); String nodeTypeChild =
	 * ASTNode.nodeClassForType(childListNode.getNodeType()).getSimpleName(); } } }
	 * return importDescription; }
	 */
	
	
	/**
	 * Parses the contents of a java file and returns its AST as an XML string.
	 * 
	 * @param str the contents of a java file given as a string.
	 * @return an XML string representation of the AST of the java file contents.
	 * @throws IOException 
	 */
	public static String parse(String str1,String str2) throws IOException {
		ASTParser parser1 = ASTParser.newParser(AST.JLS14);
		parser1.setSource(str1.toCharArray());
		parser1.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu1 = (CompilationUnit) parser1.createAST(null);
		
		ASTParser parser2 = ASTParser.newParser(AST.JLS14);
		parser2.setSource(str2.toCharArray());
		parser2.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu2 = (CompilationUnit) parser2.createAST(null);
		
	     // assertTypeBindingsEqual("Unexpected interfaces", "I", typeBinding.getInterfaces());
		visitTreeTest2(cu1,cu2);
		//ICompilationUnit unit;
		//Document doc = new Document(cu.getSource());
	    //equals(cu1,  cu2);
		//differences();
		saveFileAnalize("resultAnalises.txt");
		
		return visitTree(cu1);
	}
	

	@SuppressWarnings("unchecked")
	static void differences(){
		Boolean controlPrint;
		if(differencesFoundExamA.size()>0) {
			System.out.print("\nDIFFERENCES INITIAL VERSION:");
			for(int i = 0; i<differencesFoundExamA.size(); i++)
			    {
					ASTNode nodeV2 = (ASTNode) differencesFoundExamA.get(i);
					controlPrint = true;
					
					System.out.print("\nDifference " + (i+1) + ": ");
					//System.out.print(nodeV2.toString()+" ");
					
					System.out.print(" \n   start in the position: ");
					System.out.print(nodeV2.getStartPosition()+" ");
					typeNode(nodeV2);
					ASTNode nodeFather = (ASTNode) nodeV2.getParent();
					while(nodeFather != null) {
						//if(controlPrint == true) {
							//if(typeNode(nodeFather)==true) {
								System.out.print(" \n The father  start in the position: ");
								System.out.print(nodeFather.getStartPosition()+" value: ");
								typeNode(nodeFather);
							//}
						//	else {
								//controlPrint = false;
							//}
						//}
						//else {
							//controlPrint = false;
						//}
						nodeFather = nodeFather.getParent();
					}
		    	}
		    }	
		
	
//		if(differencesFound.size()>0) {
//			System.out.print("\nDIFFERENCES:");
//			for(int i = 0; i<differencesFound.size(); i++)
//			    {
//					ASTNode nodeV2 = (ASTNode) differencesFound.get(i);
//					controlPrint = true;
//					
//					System.out.print("\nDifference " + (i+1) + ": ");
//					//System.out.print(nodeV2.toString()+" ");
//					typeNode(nodeV2);
//					System.out.print(" \n   start in the position: ");
//					System.out.print(nodeV2.getStartPosition()+" ");
//					
//					ASTNode nodeFather = (ASTNode) nodeV2.getParent();
//					while(nodeFather != null) {
//						if(controlPrint == true) {
//							if(typeNode(nodeFather)==true) {
//							System.out.print(" \n   start in the position: ");
//							System.out.print(nodeFather.getStartPosition()+" ");
//							}
//							else {
//								controlPrint = false;
//							}
//						}
//						else {
//							controlPrint = false;
//						}
//						nodeFather = nodeFather.getParent();
//					}
//		    	}
//		    }
	}
	
	
    static void typeNode(ASTNode left) {

    String nodeType = ASTNode.nodeClassForType(left.getNodeType()).getSimpleName();
    if (left instanceof ImportDeclaration) {
    	System.out.print("\n SIMPLE ImportDeclaration type parent "+nodeType+" value:  " +left);
    }else if (left instanceof TypeDeclaration) {
    	System.out.print("\n SIMPLE TypeDeclaration type: "+nodeType +" value: ");
    }else if (left instanceof MethodDeclaration) {
    	System.out.print("\n SIMPLE MethodDeclaration type: "+nodeType +" value:  ");
    }else if (left instanceof Modifier) {
    	System.out.print("\n SIMPLE Modifier type: "+nodeType+" value: " +left);
    }else if (left instanceof SingleVariableDeclaration) {
    	System.out.print("\n SIMPLE SingleVariableDeclaration type: "+nodeType+" value:  " +left);
    }else if (left instanceof PrimitiveType) {
    	System.out.print("\n SIMPLE PrimitiveType: "+nodeType+" value: " +left);
    }else if (left instanceof StringLiteral) {
    	System.out.print("\n SIMPLE StringLiteral type: "+nodeType+" value:  " +left);
    }else if (left instanceof InfixExpression) {
    	System.out.print("\n SIMPLE InfixExpression type: "+nodeType+" value: " +left);
    }else if (left instanceof Assignment) {
    	System.out.print("\n SIMPLE Assignment type: "+nodeType+" value: " +left);
    }else if (left instanceof NumberLiteral) {
    	System.out.print("\n SIMPLE NumberLiteral type parent: "+nodeType+" value:" +left);
    }else if (left instanceof SimpleName) {
        System.out.print("\n SimpleName type parent: "+nodeType+ " value "+left );
        System.out.print("\n tostring() : "+left.toString());
//        List<StructuralPropertyDescriptor> props = left.structuralPropertiesForType();
//        for (StructuralPropertyDescriptor property : props) {
//            System.out.print("\n Is simple : "+property.isSimpleProperty() );
//            System.out.print("\n Is child : "+property.isChildProperty() );
//            System.out.print("\n Is childList : "+property.isChildListProperty() );
//        }
    }else if (left instanceof CompilationUnit) {
    	System.out.print("\n SIMPLE CompilationUnit type: "+ nodeType +" value:  " );
    }else if (left instanceof QualifiedName) {
    	System.out.print("\n SIMPLE QualifiedName type: "+ nodeType + " value:  " + left);
    }else if (left instanceof MethodInvocation) {
    	System.out.print("\n SIMPLE MethodInvocation type: "+ nodeType + " value:  " + left);
    }else if (left instanceof Block) {
    	//controlPrint = false;
    	List statementsExamenA = ((Block) left).statements();
    	for(int i=0;i<statementsExamenA.size();i++){
    	  //  System.out.println( statementsExamenA.get(i));
    	} 
    	System.out.print("\n SIMPLE Block type: "+ nodeType + " value: ");
      List<StructuralPropertyDescriptor> props = left.structuralPropertiesForType();
      for (StructuralPropertyDescriptor property : props) {
    	  Object leftVal = left.getStructuralProperty(property);
          if(property.isChildListProperty()) {
          Iterator<ASTNode> leftValIt = ((Iterable<ASTNode>) leftVal)
                  .iterator();
          while (leftValIt.hasNext()) {
              // recursively call this function on child nodes
        	  ASTNode astNodeBlock = leftValIt.next();
        	  System.out.print("\n Is child : "+ astNodeBlock.getStartPosition());
              System.out.print(" "+ astNodeBlock.toString() );
          }
         }
      }
    }else if (left instanceof ExpressionStatement) {
    	System.out.print("\n SIMPLE ExpressionStatement type: "+ nodeType + " value: " + left);
    }else if (left instanceof EnhancedForStatement) {
    	System.out.print("\n SIMPLE EnhancedForStatement type: "+ nodeType + " value: " + left);
    }else {
    	 System.out.print("\n SIMPLE OTHERS type: "+nodeType+" value "+ left);
    }
}
	
	
	@SuppressWarnings("unchecked")
    static void equals(ASTNode left, ASTNode right) {
        // if both are null, they are equal, but if only one, they aren't
        if (left == null && right == null) {
        	// if(!(right==null)) {
        	// 	differencesFound.add((ASTNode) right);
        	// 	System.out.print("\nfirst node: ");
        	// }
        } else if (left == null || right == null) {
        	if(!(right==null)) {
        		differencesFound.add((ASTNode) right);
        		differencesFoundExamA.add((ASTNode) left);
        		//System.out.print("\nsecond node: ");
        	}
        }
        // if node types are the same we can assume that they will have the same
        // properties
        else if (left.getNodeType() != right.getNodeType()) {
        	//System.out.print("\nif different nodetype || add: ");
        	differencesFound.add((ASTNode) right);
        	differencesFoundExamA.add((ASTNode) left);
        	//System.out.print("\nthree node:(Esto es cambiando el tipo de variable) ");
        }
        else {
        
	        List<StructuralPropertyDescriptor> props = left.structuralPropertiesForType();
	        for (StructuralPropertyDescriptor property : props) {
	            Object leftVal = left.getStructuralProperty(property);
	            Object rightVal = right.getStructuralProperty(property);
	            if (property.isSimpleProperty()) {
	                // check for simple properties (primitive types, Strings, ...)
	                // with normal equality
	                if (!leftVal.equals(rightVal)) {
	                	differencesFound.add((ASTNode) right);
	                	differencesFoundExamA.add((ASTNode) left);
	                	//System.out.print("\nfour node(esto es cambiando el nombre): ");
	                }
	            } else if (property.isChildProperty()) {
	                // recursively call this function on child nodes
					  ASTNode nodeV1 = (ASTNode) left.getStructuralProperty(property);
					  ASTNode nodeV2 = (ASTNode) right.getStructuralProperty(property);
	                  equals((ASTNode) nodeV1, (ASTNode) nodeV2);
	            } else if (property.isChildListProperty()) {
	            	
	                Iterator<ASTNode> leftValIt = ((Iterable<ASTNode>) leftVal)
	                        .iterator();
	                Iterator<ASTNode> rightValIt = ((Iterable<ASTNode>) rightVal)
	                        .iterator();
	                while (leftValIt.hasNext() && rightValIt.hasNext()) {
	                    // recursively call this function on child nodes
	                    equals(leftValIt.next(), rightValIt.next());
	                }
	                // one of the value lists have additional elements
	                if (leftValIt.hasNext() || rightValIt.hasNext()) {
	                	differencesFound.add((ASTNode) right);
	                	differencesFoundExamA.add((ASTNode) left);
	                	//System.out.print("\nfive node: ");
	                }      
	            }
	        }
	   }
    }
	
	
	
	
	
	
	  private static void saveFileAnalize(String nameFile) throws IOException {
		  FileWriter fileWriter = new FileWriter("resultAnalises.txt");
			fileWriter.write("total of Imports: "+Integer.toString(totalImports));
			fileWriter.write("\ntotal of types declarations : "+Integer.toString(totalTypeDeclaration));
			fileWriter.write("\ntotal of methods declarations : "+Integer.toString(MethodDeclaration));
			fileWriter.close();
	  }

	static void lineInformationNode(Statement statementA , Statement statementB) {
		System.out.print(statementA.getStartPosition());
		System.out.print("\n    " + statementA.toString());
		simpleStmt(statementA, statementB,"");
		
	}
	public static String deleteSpace(String cadena) {
		  // Para el reemplazo usamos un string vacío 
		  return cadena.replaceAll("\n", " "); 
		}
	private static void simpleStmt(ASTNode stmtA ,ASTNode stmtB, String separator) {
		 String caracterSeparator = "     " ;
		 caracterSeparator = caracterSeparator + separator;
		 System.out.print("\n");
		 System.out.print( stmtA.getNodeType());
		  switch (stmtA.getNodeType()) {
		  case ASTNode.IF_STATEMENT:
			  break;
		  case ASTNode.DO_STATEMENT:
			  break;
		  case ASTNode.WHILE_STATEMENT:
			  break;
		  case ASTNode.FOR_STATEMENT:
			  break;
		  case ASTNode.	EXPRESSION_STATEMENT:
			  System.out.print(caracterSeparator + "EXPRESSION_STATEMENT");
			  break;
		  case ASTNode.ASSIGNMENT:
			  System.out.print(caracterSeparator + "ASSIGNMENT");
			  break;
		  case ASTNode.NUMBER_LITERAL:
			  System.out.print(caracterSeparator + "NUMBER_LITERAL");
			  break;
		  case ASTNode.ENHANCED_FOR_STATEMENT:
			  System.out.print(caracterSeparator + "ENHANCED_FOR_STATEMENT");
			  break;
		  case ASTNode.METHOD_INVOCATION:
			  System.out.print(caracterSeparator + "METHOD_INVOCATION");
			  break;
		  case ASTNode.QUALIFIED_NAME:
			  System.out.print(caracterSeparator + "QUALIFIED_NAME");
			  break;
		  case ASTNode.STRING_LITERAL:
			  System.out.print(caracterSeparator + "STRING_LITERAL");
			  break;
		  case ASTNode.SIMPLE_NAME:
			  System.out.print(caracterSeparator + "SIMPLE_NAME");
			  break;
		  case ASTNode.INFIX_EXPRESSION:
			  System.out.print(caracterSeparator + "INFIX_EXPRESSION");
			  break;
		  case ASTNode.SIMPLE_TYPE:
			  System.out.print(caracterSeparator + "SIMPLE_TYPE STATEMENT");
			  break;
		  case ASTNode.VARIABLE_DECLARATION_STATEMENT:
			  System.out.print(caracterSeparator + "VARIABLE_DECLARATION_STATEMENT");
			  break;
		  case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
			  System.out.print(caracterSeparator + "VARIABLE_DECLARATION_FRAGMENT");
			  break;
		  case ASTNode.TRY_STATEMENT:
			  break;
			  
		  default:
			  
			  break;
		  }
		  System.out.print(" " + deleteSpace(stmtA.toString()));
		  ArrayList<ASTNode> children = getChildren(stmtA);
		  if (children.size() > 0) {
				for (ASTNode child : children) {
					simpleStmt(child, caracterSeparator);
				}
		  }
		}
  	static void MethodDeclarationsDetails(ASTNode left,ASTNode rigth){
    	//System.out.print(((MethodDeclaration) left).getName());
        if (!(rigth == null)) {
        List<StructuralPropertyDescriptor> props = rigth.structuralPropertiesForType();
        for (StructuralPropertyDescriptor property : props) {
            if (property.isSimpleProperty()) {
            	if (rigth instanceof MethodDeclaration) {
            		//COMPARAR SI TIENEN EL MISMO NOMBRE
            		System.out.print("\nNode examen A "+((MethodDeclaration) left).getName());
            		System.out.print("\nNode examen B "+((MethodDeclaration) rigth).getName());
            		//System.out.print("\nNode examen B "+((MethodDeclaration) rigth).getBody());
            		Block leftBlock = ((MethodDeclaration) left).getBody();
                	List statementsExamenA = leftBlock.statements();
                	Block rigthBlock = ((MethodDeclaration) rigth).getBody();
                	List statementsExamenB = rigthBlock.statements();
                	String lineaExamen1,lineaExamen2 = "";
                	int totalLinesError = 0 ; 
                	for(int i=0;i<statementsExamenB.size();i++){
                		lineaExamen1 = statementsExamenA.get(i).toString();
                		lineaExamen2 = statementsExamenB.get(i).toString();
            			//System.out.println( "\n"+ lineaExamen2);
                		if(!(lineaExamen1.contentEquals(lineaExamen2))) {
                			totalLinesError = totalLinesError + 1;
                    		System.out.print("\n******************************************************\n");
                    		System.out.print("Difference "+(totalLinesError)+")");
                    		System.out.print("\n");
                    		System.out.print("    Version A: position: ");
                    		lineInformationNode((Statement) statementsExamenA.get(i),(Statement) statementsExamenB.get(i));
                    		System.out.print("\n\n");
                    		System.out.print("    Version B: position: ");
                    		lineInformationNode((Statement) statementsExamenA.get(i) , (Statement) statementsExamenB.get(i));
                    		System.out.print("\n");
                		}
                	}
        			System.out.println( "\nlines with changes: "+ totalLinesError);
            	}
            } else if (property.isChildProperty()) {
            	//MethodDeclarationsDetails(left, rigth);
            }else if (property.isChildListProperty()) {
            	Object rigthVal = rigth.getStructuralProperty(property);
                Iterator<ASTNode> rigthValIt = ((Iterable<ASTNode>) rigthVal)
                        .iterator();
                while (rigthValIt.hasNext()) {
                	MethodDeclarationsDetails(left, rigthValIt.next());
                }
            }
  		}
       }
  	}
	  
	  
	  
	  
	  
	  
	  
	  
	@SuppressWarnings("unchecked")
    static boolean visitTreeTest2(ASTNode left, ASTNode rigth) {
        if (left == null) {
            return true;
        } else if (left == null) {
            return false;
        }
        String nodeType = ASTNode.nodeClassForType(left.getNodeType()).getSimpleName();
 /*       if (left instanceof CompilationUnit) {
        	//getImportPackageName((ImportDeclaration) left);
        	totalImports = totalImports + 1;
        	System.out.print("\n type "+ nodeType +" value:" );
        	//System.out.print("\n"+((ImportDeclaration) left).getName());
        	//System.out.println("Source file " + ((ICompilationUnit) left).getElementName());
        	//System.out.print("\n"+((ImportDeclaration) left).isOnDemand());
        }else if (left instanceof ImportDeclaration) {
        	//getImportPackageName((ImportDeclaration) left);
        	totalImports = totalImports + 1;
        	System.out.print("\nImportDeclaration type "+nodeType+" value:");
        	System.out.print("\n"+((ImportDeclaration) left).getName());
        	System.out.print("\n"+((ImportDeclaration) left).isOnDemand());
        }else if (left instanceof TypeDeclaration) {
        	totalTypeDeclaration = totalTypeDeclaration + 1;
        	System.out.print("\nType Declaration type: "+nodeType +" value: ");
        	System.out.print("\n"+((TypeDeclaration) left).getName());
        	System.out.print("\n"+((TypeDeclaration) left).isInterface());
        	//MethodDeclaration[] arrayDeclarations = ((TypeDeclaration) left).getMethods();
        	//System.out.println("\n"+Arrays.toString(arrayDeclarations));
       }else
       */ 
		if (left instanceof MethodDeclaration) {
        	MethodDeclaration = MethodDeclaration + 1;
        	//System.out.print("\ntype: "+nodeType+" value: ");
        	//System.out.print(((MethodDeclaration) left).getName());
        	//System.out.print("\n"+((MethodDeclaration) left).getBody());
        	MethodDeclarationsDetails( left, rigth);
        	
        	
			/*
			 * }else if (left instanceof QualifiedName) {
			 * System.out.print("\ntype: "+nodeType+" value: ");
			 * System.out.print(((QualifiedName) left).getName()); // }else if (left
			 * instanceof SingleVariableDeclaration) {
			 * //System.out.print("\n SIMPLE SingleVariableDeclaration type: "
			 * +nodeType+" value: " +left); }else if (left instanceof PrimitiveType) {
			 * System.out.print("\ntype: "+nodeType+" value: ");
			 * System.out.print(((PrimitiveType) left).toString()); }else if (left
			 * instanceof Block) { System.out.print("\nBlock: "+nodeType+" value: "); //
			 * }else if (left instanceof InfixExpression) {
			 * //System.out.print("\n SIMPLE InfixExpression type: "+nodeType+" value: "
			 * +left); // }else if (left instanceof Assignment) {
			 * //System.out.print("\n SIMPLE Assignment type: "+nodeType+" value: " +left);
			 * // }else if (left instanceof NumberLiteral) {
			 * //System.out.print("\n SIMPLE NumberLiteral type parent: "
			 * +nodeType+" value: " +left); }else if (left instanceof SimpleName) {
			 * System.out.print("\ntype: " + nodeType + " value " );
			 * System.out.print(((SimpleName) left).toString()); // }else if (left
			 * instanceof CompilationUnit) {
			 * //System.out.print("\n SIMPLE CompilationUnit type: "+ nodeType +" value: "
			 * ); // }else if (left instanceof QualifiedName) {
			 * //System.out.print("\n SIMPLE QualifiedName type: "+ nodeType + " value: " +
			 * left); }else if (left instanceof Modifier) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((Modifier) left).toString());
			 * //System.out.print("\n"+((Modifier) left).getName()); }else if (left
			 * instanceof VariableDeclarationStatement) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((VariableDeclarationStatement)
			 * left).toString());
			 * 
			 * }else if (left instanceof SimpleType) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((SimpleType) left).toString());
			 * 
			 * }else if (left instanceof ExpressionStatement) { System.out.print("\ntype: "
			 * + nodeType + " value " ); System.out.print(((ExpressionStatement)
			 * left).toString());
			 * 
			 * }else if (left instanceof StringLiteral) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((StringLiteral) left).toString());
			 * 
			 * }else if (left instanceof MethodInvocation) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((MethodInvocation)
			 * left).toString());
			 * 
			 * }else if (left instanceof SingleVariableDeclaration) {
			 * System.out.print("\ntype: " + nodeType + " value " );
			 * System.out.print(((SingleVariableDeclaration) left).toString());
			 * 
			 * }else if (left instanceof VariableDeclarationFragment) {
			 * System.out.print("\ntype: " + nodeType + " value " );
			 * System.out.print(((VariableDeclarationFragment) left).toString());
			 * 
			 * }else if (left instanceof ArrayType) { System.out.print("\ntype: " + nodeType
			 * + " value " ); System.out.print(((ArrayType) left).toString());
			 * 
			 * }else if (left instanceof Assignment) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((Assignment) left).toString());
			 * 
			 * }else if (left instanceof ClassInstanceCreation) {
			 * System.out.print("\ntype: " + nodeType + " value " );
			 * System.out.print(((ClassInstanceCreation) left).toString());
			 * 
			 * }else if (left instanceof InfixExpression) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((InfixExpression)
			 * left).toString());
			 * 
			 * }else if (left instanceof EnhancedForStatement) { System.out.print("\ntype: "
			 * + nodeType + " value " ); System.out.print(((EnhancedForStatement)
			 * left).toString());
			 * 
			 * }else if (left instanceof IfStatement) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((IfStatement) left).toString());
			 * 
			 * }else if (left instanceof InstanceofExpression) { System.out.print("\ntype: "
			 * + nodeType + " value " ); System.out.print(((InstanceofExpression)
			 * left).toString());
			 * 
			 * }else if (left instanceof ParameterizedType) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((ParameterizedType)
			 * left).toString());
			 * 
			 * }else if (left instanceof NumberLiteral) { System.out.print("\ntype: " +
			 * nodeType + " value " ); System.out.print(((NumberLiteral) left).toString());
			 */
        }else {
        	//System.out.print("\n SIMPLE OTHERS type: "+nodeType+" value ");
        }
        
       
        
        List<StructuralPropertyDescriptor> props = left.structuralPropertiesForType();
        for (StructuralPropertyDescriptor property : props) {
            Object leftVal = left.getStructuralProperty(property);
            if (property.isSimpleProperty()) {
                
            } else if (property.isChildProperty()) {
                // recursively call this function on child nodes
   	
            	if (!visitTreeTest2((ASTNode) leftVal,(ASTNode)rigth)) {
                    return false;
                }
            } else if (property.isChildListProperty()) {
                Iterator<ASTNode> leftValIt = ((Iterable<ASTNode>) leftVal)
                        .iterator();
                while (leftValIt.hasNext()) {
                    // recursively call this function on child nodes
                	
                	ASTNode childListNode = leftValIt.next();
                    String nodeTypeChild = ASTNode.nodeClassForType(childListNode.getNodeType()).getSimpleName();
 
                    if (!visitTreeTest2(childListNode,rigth)) {
                        return false;
                    }
                }
                // one of the value lists have additional elements
                if (leftValIt.hasNext()) {
                    return false;
                }
            }
    		//System.out.print("\n");
        }
        return true;
    }

}
