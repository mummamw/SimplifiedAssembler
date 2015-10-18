import java.util.*;
import java.io.*;

public class Assembler {
	
	
	public static void main(String[] args) throws IOException{
		
		// Opening input file 
		File inputFile = new File(args[0]);
		//Scanner fileScanner = new Scanner(new File(args[0]));
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));

		// Creating output file
		File outputFile = new File(args[1]);
		PrintWriter writer = new PrintWriter(outputFile);
		
		//Generating Hash tables for maps
		//registerHash used for getting register's values
		Hashtable<String, String> registerHash = new Hashtable<String, String>();
			registerHash.put("$zero", "00000");
			registerHash.put("$at",   "00001");
			registerHash.put("$v0",   "00010");
			registerHash.put("$v1",   "00011");
			registerHash.put("$a0",   "00100");
			registerHash.put("$a1",   "00101");
			registerHash.put("$a2",   "00110");
			registerHash.put("$a3",   "00111");
			registerHash.put("$t0",   "01000");
			registerHash.put("$t1",   "01001");
			registerHash.put("$t2",   "01010");
			registerHash.put("$t3",   "01011");
			registerHash.put("$t4",   "01100");
			registerHash.put("$t5",   "01101");
			registerHash.put("$t6",   "01110");
			registerHash.put("$t7",   "01111");
			registerHash.put("$s0",   "10000");
			registerHash.put("$s1",   "10001");
			registerHash.put("$s2",   "10010");
			registerHash.put("$s3",   "10011");
			registerHash.put("$s4",   "10100");
			registerHash.put("$s5",   "10101");
			registerHash.put("$s6",   "10110");
			registerHash.put("$s7",   "10111");
			registerHash.put("$t8",   "11000");
			registerHash.put("$t9",   "11001");
	
		// Hash Table for J format instructions
		//op codes should always be 6 bits
		Hashtable<String, String> jHash = new Hashtable<String, String>();
			jHash.put("j",   "000010");
			jHash.put("jal", "000011");

		// Hash Table for I format instructions 
		//op codes should always be 6 bits
		Hashtable<String, String> iHash = new Hashtable<String, String>();
			iHash.put("lw",   "100011");
			iHash.put("sw",   "101011");
			iHash.put("andi", "001100");
			iHash.put("ori",  "001101");
			iHash.put("lui",  "001111");
			iHash.put("beq",  "000100");
			iHash.put("stli", "001010");
			iHash.put("addi", "001000");
		
		// Hash Table for R format instructions
		// Mapping "function" field here
		Hashtable<String, String> rHash = new Hashtable<String, String>();
			rHash.put("add", "100000");
			rHash.put("sub", "100001");
			rHash.put("and", "100100");
			rHash.put("or",  "100101");
			rHash.put("sll", "000000");
			rHash.put("slt", "101010");
			rHash.put("srl", "000010");
			rHash.put("jr",  "001000");
	
		//While loop containing more lines
		//Variables that will be used in while loop
		String holderText = "";
		String holderHex = "";
			
		//Flawed in the case of an empty file being passed in
		String line = reader.readLine();
		while( (line) != null) {
			
		    System.out.println(line); //Keeping for testing. 
		    
		    // Splitting string into tokens[] 
		    String[] tokens = line.split(" |, ");
		    
		    for(int i = 0; i<4; i++){
			    if(tokens[i] != null){
			    	System.out.println("Token "+ i +": " + tokens[i]);
			    	holderText += tokens[i];
			    }
			    if(i == 0){
			    	System.out.println("Token[" + i + "]: " + rHash.get(tokens[i]));
			    	holderHex += rHash.get(tokens[i]);
			    }
			    if(i == 1) {
			    	System.out.println("Token[" + i + "]: " + registerHash.get(tokens[i]));
			    	holderHex += registerHash.get(tokens[i]);
			    }
			    if(i == 2) {
			    	System.out.println("Token[" + i + "]: " + registerHash.get(tokens[i]));
			    	holderHex += registerHash.get(tokens[i]);
			    }
			    if(i == 3){
			    	System.out.println("Token[" + i + "]: " + registerHash.get(tokens[i]));
			    	holderHex += registerHash.get(tokens[i]);
			    }
		    
			    System.out.println("holderText is currently: " + holderText);
			    System.out.println("holderHex is currently:" + holderHex);
		    }

//		    System.out.println("Token 0: " + tokens[0]);
//		    System.out.println("Token 1: " + tokens[1]);
//		    System.out.println("Token 2: " + tokens[2]);
//		    System.out.println("Token 3: " + tokens[3]);
		    
		    
		    //Writing out 
		    writer.println("Hello World");
		    
		    try{							//responsible for moving lines.
		    	line = reader.readLine();
		    } catch (IOException e) {
		    	System.out.println(e.toString());
		    }
		}
		
		//System.out.println(registerHash.get("$zero"));
	
		writer.close();
			
	}
	
}
