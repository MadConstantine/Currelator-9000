package ru.currcalc;

import java.io.*;


public class Main {
	
	public static void main(String[] args) {
		//upload settings from file settings.properties
		Currency.loadSettings();
		
		// main loop, command-line interface for user
		boolean exitFlag = false;
		while (!exitFlag) {
			System.out.print("@>>");
			try {
				//receiving string with commands from user
				InputStreamReader reader = new InputStreamReader(System.in);
				BufferedReader buffer = new BufferedReader(reader);
				String income = buffer.readLine();
				if (income.contains("quit")) {
					System.out.println("thanks for using CurrCalc!");
					break;
				}
				
				Currency result = Processor.lineParser(Processor.linePreparator(income));
				System.out.println(result.toString());
			}
			catch (IOException ioEx) {
				System.out.print("Invalid query! (IOE)");
			}
			catch (ProcessorException pe) {
				System.out.println(pe.getMessage());
				pe.printStackTrace();
			}
			System.out.print("\n");
		}
	}

}
