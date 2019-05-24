package ru.currcalc;

import java.math.BigDecimal;
import java.util.*;

public final class Processor {
	
	//method navigates through tree of commands in command line and returns the summ of all operands
	public static Currency lineParser(String line) {		
		List<Currency> operandsList = new ArrayList<Currency>();
		Currency summOfAll = new Currency(new BigDecimal(0),0);
		
		// parser body
		for (int i=0; i<line.length(); i++) {			
			switch (line.charAt(i)) {
			// if parser meets dollars on its way
			case '$':
				if (i>0) {// to avoid exception
					
					if (line.charAt(i-1)=='-') {// if operand starts with '-'
						// calculate length of substring, that contains amount
						int numOfDigits = 0;
						for (int j=i+1; j<line.length(); j++) {
							if ((line.charAt(j) == '+') || (line.charAt(j) == '-') || (line.charAt(j) == ')')) {
								break;
							}
							else {
								numOfDigits++;
							}
						}
						// add currency into list
						String amountStr = new String('-' + line.substring(i+1, i+1+numOfDigits));
						BigDecimal amount = new BigDecimal(amountStr);
						operandsList.add(new Currency(amount, Currency.DOLLARS));
					}
					else {// if operand starts with '+'
						// calculate length of substring, that contains amount
						int numOfDigits = 0;
						for (int j=i+1; j<line.length(); j++) {
							if ((line.charAt(j) == '+') || (line.charAt(j) == '-') || (line.charAt(j) == ')')) {
								break;
							}
							else {
								numOfDigits++;
							}
						}
						// add currency into list
						String amountStr = line.substring(i+1, i+1+numOfDigits);
						BigDecimal amount = new BigDecimal(amountStr);
						operandsList.add(new Currency(amount, Currency.DOLLARS));
					}
				}
				else { //if operand stands first in line
					// calculate length of substring, that contains amount
					int numOfDigits = 0;
					for (int j=i+1; j<line.length(); j++) {
						if ((line.charAt(j) == '+') || (line.charAt(j) == '-') || (line.charAt(j) == ')')) {
							break;
						}
						else {
							numOfDigits++;
						}
					}
					// add currency into list
					String amountStr = line.substring(i+1, i+1+numOfDigits);
					BigDecimal amount = new BigDecimal(amountStr);
					operandsList.add(new Currency(amount, Currency.DOLLARS));
				}
				break;
			
			// if parser meets rubles on its way: 'ð' in cyrillic
			case 'ð':
				int numOfDigits = 0;
				for (int j=i-1; j>=0; j--) {
					if ((line.charAt(j) == '+') || (line.charAt(j) == '(')) {
						break;
					}
					else {
						if ((line.charAt(j) == '-')) {
							numOfDigits++;
							break;
							}
						else {
							numOfDigits++;
						}
					}
				}
				// add currency into list
				String amountStr = line.substring(i-numOfDigits, i);
				BigDecimal amount = new BigDecimal(amountStr);
				operandsList.add(new Currency(amount, Currency.RUBLES));
				break;
			
			// if parser meets convertation command on the way
			case 't':
				int typeFinal = 0;
				int startIndex = 0;
				int endIndex = 0;
				
				if (line.regionMatches(i+1, "oDollars", 0, 8)) {
					typeFinal = Currency.DOLLARS;
					startIndex = i+10; // 'oDollars('
				}
				if (line.regionMatches(i+1, "oRubles", 0, 7)) {
					typeFinal = Currency.RUBLES;
					startIndex = i+9; // 'oRubles('
				}
				// here you can add another types of convertation commands through adding if ()
				
				// 
				if (typeFinal != 0) {
					// counting length of substring inside convertation command
					int bracketsCounter = 1;
					for (int j=startIndex; j<line.length(); j++) {
						if (line.charAt(j) == '(') {
							bracketsCounter++;
						}
						if (line.charAt(j) == ')') {
							bracketsCounter--;
						}
						if (bracketsCounter == 0) {
							endIndex = j;
							break;
						}
					}
					// recursively dig into substring and writes subsumm into list					
					Currency subSumm = Processor.lineParser(line.substring(startIndex, endIndex));
					try {
						operandsList.add(subSumm.convertTo(typeFinal));
					} catch (CurrencyException e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
				else {
					//exception! invalid or unsupported convertation command
					System.out.println("invalid or unsupported convertation command!!!");
				}
				
				// important! to prevent counting operands twice!
				i=endIndex+1;
				break;
			}	
		}
		
		//let summOfAll`s type be the same as first element`s
		summOfAll.setType(operandsList.get(0).getType());
		
		
		// collecting all items in list
		try {
			for (int i=0; i<operandsList.size();i++) {
				summOfAll.add(operandsList.get(i));
			}
		} catch (CurrencyException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		//System.out.println( summOfAll.getAmount().toString() );
		return summOfAll;
	}
	
	
	// prepares string to be processed
	public static String linePreparator(String rawLine) {
		// delete all white spaces from string
		StringBuilder preparedLine = new StringBuilder(rawLine);
		for (int i=0; i<preparedLine.length(); i++) {
			if (preparedLine.charAt(i) == ' ') {
				preparedLine.deleteCharAt(i);
			}
		}
		return preparedLine.substring(0);
	}
}
