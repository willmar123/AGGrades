package astextractor;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;


public class MiAplicacion extends JFrame
{
	/**
	  * Contador a modificar
	  */
	JTextField txtCont,txtResult;
	/**
	  * Constructor
	 * @param result 
	 * @throws IOException 
	  */
	public MiAplicacion( String result , String fileV1 , String fileV2) throws IOException
	{
		
		FileWriter fileWriter = new FileWriter("result.txt");
		fileWriter.write(result);
		fileWriter.close();
		
		
		setSize(900, 900);
		
		JPanel panelCont = new JPanel();
		JPanel panelCont1 = new JPanel();

		//panelCont.setLayout(null);
		JPanel  panelExam1= new JPanel();
		JPanel panelExam2 = new JPanel();
		//panelExam1.setLayout(null);
		panelExam1.setBorder(BorderFactory.createLineBorder(Color.black));
		panelExam2.setBorder(BorderFactory.createLineBorder(Color.black));;
		TextArea textArea1 , textArea2,CodeRevv1,CodeRev2;
		String line,lineConcat;
		JLabel lblExam1 = new JLabel("First version exam:");
		JLabel lblExam2 = new JLabel("Second version exam:");
		panelExam1.add(lblExam1);
		panelExam2.add(lblExam2);
		
		
		BufferedReader in;

		in = new BufferedReader(new FileReader( fileV1 ));
		line = in.readLine();
		lineConcat = ""; 
		 while(line != null)
         {
               // System.out.println(line);
                lineConcat = lineConcat + line + "\n";
                line = in.readLine();
                
         }
		
		textArea1 = new TextArea(lineConcat);
		//textArea1.setSize(50,50);
		// textArea1.setLocation(100, 100);
        panelExam1.add(textArea1);
        
        BufferedReader in2;
        
        in2 = new BufferedReader(new FileReader( fileV2 ));
		line = in2.readLine();
		lineConcat = ""; 
		 while(line != null)
         {
               // System.out.println(line);
                lineConcat = lineConcat + line + "\n";
                line = in2.readLine();
                
         }
		
		
		textArea2 = new TextArea(lineConcat);
        panelExam2.add(textArea2);
        
        
        BufferedReader in3;
        
        in3 = new BufferedReader(new FileReader( "result.txt" ));
		line = in3.readLine();
		lineConcat = ""; 
		 while(line != null)
         {
               // System.out.println(line);
                lineConcat = lineConcat + line + "\n";
                line = in3.readLine();
                
         }

		
		 CodeRevv1 = new TextArea(lineConcat);
		 CodeRevv1.setBounds(500, 50, 100, 20);
        panelExam1.add(CodeRevv1);
        
        
		
		
		// ****** Panel con el contador ******
		
		//JPanel panelCont = new JPanel();		
		final JLabel lblCont = new JLabel("Contador:");



		lblCont.addMouseListener(new MouseAdapter()
		{
			// Al entrar en la etiqueta hacemos que cambie su texto
			public void mouseEntered(MouseEvent e)
			{
				lblCont.setText("En etiqueta!");
			}

			public void mouseExited(MouseEvent e)
			{
				lblCont.setText("Contador:");
			}
		});
		
		
		

        
        
        
        //textarea1 = lineConcat;
        
        
		txtCont = new JTextField("0");
		panelCont.add(lblCont);
		panelCont.add(txtCont);
		

		
		BufferedReader in4;
		
		in4 = new BufferedReader(new FileReader( "resultAnalises.txt" ));
		line = "";
		line = in4.readLine();
		lineConcat = ""; 
		 while(line != null)
         {
               // System.out.println(line);
                lineConcat = lineConcat + line + "\n";
                line = in4.readLine();
                
         }
		final JLabel lblResults = new JLabel("Results Comparasion:");
		TextArea txtResult; 
		txtResult = new TextArea(lineConcat);
		//textArea1.setSize(50,50);
		// textArea1.setLocation(100, 100);
        

		panelCont1.add(lblResults);
		panelCont1.add(txtResult);
		
		
		
		
		
		
		
		// ****** Panel para el boton ******
		
		JPanel  panelBoton= new JPanel();		
		JLabel lblBoton = new JLabel("Incrementar Contador:");
		JButton btn = new JButton("Incrementar");
		btn.addActionListener(new ActionListener()
		{
			// Al pulsar el boton incrementamos en 1 el contador
			public void actionPerformed(ActionEvent e)
			{
				txtCont.setText("" + (Integer.parseInt(txtCont.getText()) + 1));
			}
		});
		panelBoton.add(lblBoton);
		panelBoton.add(btn);
		
		// ****** Panel para el desplegable ******
		
		JPanel panelChoice = new JPanel();		
		JLabel lblChoice = new JLabel("Establecer Contador:");
		final JComboBox ch = new JComboBox();
		for (int i = 0; i < 10; i++)
			ch.addItem("" + i);
		ch.addItemListener(new ItemListener()
		{
			// Al elegir una opcion, se asigna ese valor al contador
			public void itemStateChanged(ItemEvent e)
			{
				txtCont.setText((String)(ch.getSelectedItem()));
			}
		});
		panelChoice.add(lblChoice);
		panelChoice.add(ch);


		// Colocamos los paneles
		//getContentPane().add(panelCont, "South");
		//getContentPane().add(p);
		getContentPane().add(panelExam1,BorderLayout.NORTH);
		//getContentPane().add(panelCont, BorderLayout.WEST);
		getContentPane().add(panelCont1,BorderLayout.CENTER);
		getContentPane().add(panelExam2,BorderLayout.SOUTH);
		//getContentPane().add(panelExam2, BorderLayout.EAST);
		
		// Evento para cerrar la ventana
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	
	/**
	  * Main
	 * @throws IOException 
	  */
	public static void main (String[] args) throws IOException
	{
		
		String project ="";
		String fileVersion1 = "exams//87698-ESPOSITO-ROBERTO//src//Esempio.java";
		String fileVersion2 = "examsCorrect//87698-ESPOSITO-ROBERTO//src//Esempio.java";
		String properties = "";
		String repr = "JSON";
		if (project.length() > 0 || fileVersion1.length() > 0 || fileVersion2.length() > 0) {
			ASTExtractorProperties.setProperties(properties);
			String result = "";
			if (project.length() > 0)
				result = ASTExtractorCompare.parseFolder(project, repr);
			else if (fileVersion1.length() > 0)
				result = ASTExtractorCompare.parseFile( fileVersion1 , fileVersion2, repr );
				MiAplicacion e = new MiAplicacion( result , fileVersion1 , fileVersion2 );
		    	e.show();
			    //System.out.println(result);
		} else {
			printHelpMessage();
		}

	}
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
}