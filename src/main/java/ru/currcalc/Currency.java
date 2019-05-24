package ru.currcalc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Currency {
	//instances of that class are parsed objects from query string
	//uses BigDecimal, because using float or double is not appropriate for money, since it keeps numbers as summ of binary exponents
	
	// Global constants
	public static final int DOLLARS = 1;
	public static final int RUBLES = 2;
	
	// Currency`s static settings parameters
	public static BigDecimal ru_DOLL = new BigDecimal(64.61);
	public static int scale = 2;
	public static RoundingMode roundingMode = RoundingMode.HALF_UP;
	
	// fields
	/* type == 1 - dollars
	 * type == 2 - rubles
	 * later you may add another types
	 */
	private int type;
	private BigDecimal amount;
	
	
	public Currency(BigDecimal amount, int type) {
		
		//constructor just sets fields of currency object
		this.amount = amount.setScale(2, roundingMode);
		this.type = type;
	}
	
	// just adds one operand to another
	public void add(Currency addend) throws CurrencyException {
		
		if ((this.type == addend.type) || (this.type == 0)) {
			this.amount = this.amount.add(addend.amount);
		}
		else {
			throw new CurrencyException("Arithmetic operands should be of the same type!");
		}
	}
	
	
	// getter of amount
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	
	// getter of type
	public int getType() {
		return this.type;
	}
	
	
	// setter of amount
	public void setAmount(BigDecimal newAmount) {
		this.amount = newAmount;
	}
	
	
	//setter of type
	public void setType(int newType) {
		this.type = newType;
	}
	
	
	// load settings to overwrite defaults
	public static void loadSettings() {
		// get hashtable from file
		FileInputStream settingsFile;
		Properties source = new Properties();
		try {
			// read the settings file and extract needed settings
			settingsFile = new FileInputStream("src/main/resources/settings.properties");
			source.load(settingsFile);
			settingsFile.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Settings file not found!");
			e.printStackTrace();
		}
		catch (IOException ioEx) {
			System.out.println("Settings file corrupted!");
			ioEx.printStackTrace();
		}
		
		// set static fields
		Currency.scale = Integer.parseInt(source.getProperty("Digits_after_point"));
		Currency.ru_DOLL = new BigDecimal(source.getProperty("RUDOLL_ratio"));
		if ( source.getProperty("Rounding_mode").contentEquals("HALF_UP") ) {
			Currency.roundingMode = RoundingMode.HALF_UP;
		}
		else if (source.getProperty("Rounding_mode").contentEquals("HALF_DOWN")) {
			Currency.roundingMode = RoundingMode.HALF_DOWN;
		}
		else if (source.getProperty("Rounding_mode").contentEquals("HALF_EVEN")) {
			Currency.roundingMode = RoundingMode.HALF_EVEN;
		}
		else {
			System.out.println("Invalid rounding mode!");
			System.out.printf("rounding mode= " + source.getProperty("Rounding_mode"));
		}
	}
	
	
	// converts currencies
	public Currency convertTo(int type) throws CurrencyException {
		
		BigDecimal newAmount = new BigDecimal(0);
		
		switch (this.type) {
		//
		case (0):
			throw new CurrencyException ("You trying to convert currency without type!");
		
		// Dollars convert to something
		case (Currency.DOLLARS):
			switch (type) {
			case Currency.RUBLES:
				newAmount = this.amount.multiply(Currency.ru_DOLL);
				break;
				
			// add cases if you want to add types of currencies
			}
			break;
		
		// Rubles convert to something
		case (Currency.RUBLES):
			switch (type) {
			case Currency.DOLLARS:
				newAmount = this.amount.divide(Currency.ru_DOLL, 2, roundingMode);
				break;
				
			// add cases if you want to add types of currencies
			}
			break;
			
		//
		//add cases if you increasing number of currencies
		//
		}
		return new Currency(newAmount, type);
	}
	
	
	@Override
	// converts currency container to string in order to print it
	public String toString() {
		// filling in string and returning it
		String outCome = new String();
		switch (this.type) {
			// currency type 'dollars' converts to a string
			case Currency.DOLLARS: 
				if (this.amount.compareTo(new BigDecimal(0)) == -1) {
					outCome = '-' + '$' + (this.amount.abs()).toString();
				}
				else {
					outCome = '$' + (this.amount.abs()).toString();
				}
				break;
			// currency type 'dollars' converts to a string	
			case Currency.RUBLES:
				outCome = (this.amount.abs()).toString() + 'ð';
				break;
			//
			// add here another case for other currencies 	
			//	
			case 0:
				System.out.println("Unknown type of currency!");
		}
		return (outCome);
	}
}


@SuppressWarnings("serial")
class CurrencyException extends Exception {

	public CurrencyException(String message) {
		super(message);
	}
}
