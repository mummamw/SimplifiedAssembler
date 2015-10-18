import java.util.*;
import java.io.*;
import java.math.*;

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
		// op codes should always be 6 bits
		Hashtable<String, String> jHash = new Hashtable<String, String>();
			jHash.put("j",   "000010");
			jHash.put("jal", "000011");
			
		//hash Table for figuring out the format that will be used
		// Rformat=0 Iformat=1 Jformat=2
		Hashtable<String, Integer> typeHash = new Hashtable<String, Integer>();
			typeHash.put("add",  0);
			typeHash.put("sub",  0);
			typeHash.put("and",  0);
			typeHash.put("or",   0);
			typeHash.put("sll",  0);
			typeHash.put("slt",  0);
			typeHash.put("srl",  0);
			typeHash.put("jr",   0);
			typeHash.put("lw",   1);
			typeHash.put("sw",   1);
			typeHash.put("andi", 1);
			typeHash.put("ori",  1);
			typeHash.put("lui",  1);
			typeHash.put("beq",  1);
			typeHash.put("stli", 1);
			typeHash.put("addi", 1);
			typeHash.put("j",    2);
			typeHash.put("jal",  2);

		// Hash Table for I format instructions 
		// op codes should always be 6 bits
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
	
		//Flawed in the case of an empty file being passed in
		String line = reader.readLine();
		while( (line) != null) {
			
			//Variables that will be used in while loop
			String holderText = "";
			String holderBin = "";
			String holderHex = "";
			String addressHolder;
			String constantHolder;
			
		    //System.out.println(line); //Keeping for testing. 
		    
		    // Splitting string into tokens[] 
		    String[] tokens = line.split(" |, ");
		    
		    switch(typeHash.get(tokens[0])){
		    
		    // RFormat General
		    case 0:
		    	//System.out.println("RFormat");
		    	holderBin += "000000";						//op - 6 bit
		    	
				if(rHash.get(tokens[0]) == "000000"){		//rs - 5 bit
					holderBin += "00000";
		    	} else {
		    		holderBin += registerHash.get(tokens[2]);
		    	}
		    
		    	constantHolder =  registerHash.get(tokens[3]);	//rt - 5 bit
		    	if (constantHolder == null) { 	//Assuming null return means it was a constant
		    		constantHolder = tokens[3];
		    		int n = Integer.parseInt(constantHolder);
		    		constantHolder = Integer.toBinaryString(n);
		    		holderBin += constantHolder + "00000".substring(constantHolder.length());
		    	} else{
		    		holderBin += registerHash.get(tokens[3]);
		    	} 	
		    												
		    	holderBin += registerHash.get(tokens[1]);	//rd - 5 bit
		    	
		    	if(rHash.get(tokens[0]) == "000000"){	   	//shamt - 5 bit
		    		holderBin += constantHolder + "00000".substring(constantHolder.length());
		    	} else {
		    		holderBin += "00000";
		    	}
		    	
		    	holderBin += rHash.get(tokens[0]);			//funct - 5 bit
		    	//System.out.println(holderBin);
		    	break;
		    
		    // IFormat
		    case 1:
		    	//System.out.println("IFormat");
		    	holderBin += iHash.get(tokens[0]);			//op - 6 bit
		    	holderBin += registerHash.get(tokens[1]);	//rs - 5 bit
		    	holderBin += registerHash.get(tokens[2]);	//rt - 5 bit
		    	
		    	//Constant section that needs to be 16 bits long
		    	addressHolder = registerHash.get(tokens[3]);
		    	if (addressHolder == null) { 	//Assuming null return means it was a constant
		    		addressHolder = tokens[3];
		    		int n = Integer.parseInt(addressHolder);
		    		addressHolder = Integer.toBinaryString(n);
		    	}
		    	String padded16 = "0000000000000000";
		    	holderBin += padded16.substring(addressHolder.length()) + addressHolder;	
		    	//System.out.println(holderBin);
		    	break;
		    	
		    // JFormat
		    case 2:
		    	//System.out.println("JFormat");
		    	holderBin += jHash.get(tokens[0]);
		    	
		    	//Constant section the needs to be 26 bits
		    	addressHolder = registerHash.get(tokens[1]);
		    	if (addressHolder == null) { 	//Assuming null return means it was a constant
		    		addressHolder = tokens[1];
		    		int n = Integer.parseInt(addressHolder);
		    		addressHolder = Integer.toBinaryString(n);
		    	}
		    	String padded26 = "00000000000000000000000000";
		    	holderBin += padded26.substring(addressHolder.length()) + addressHolder;		    
		    	//System.out.println(holderBin);
		    	break;
		    }
		    
		    
		    //System.out.println("holderBin is currently: " + holderBin);
		   
		    //Converting Binary String to Long to be writen as Hex to holderHex
			long decimal = Long.parseLong(holderBin,2);
			holderHex = Long.toString(decimal,16);
		    
		    //System.out.println(holderHex);
		    
		    //Writing out 
		    writer.println("0x" + holderHex);
		   
		    //System.out.println("Holderbin length: " + holderBin.length());
		    
		    try{							//responsible for moving lines.
		    	line = reader.readLine();
		    } catch (IOException e) {
		    	System.out.println(e.toString());
		    }
		}
		
		writer.close();
		System.out.println("Completed write to: " + args[1]);
	}
	
}
