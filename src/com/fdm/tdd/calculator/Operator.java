package com.fdm.tdd.calculator;

public class Operator 
{
	
	private int value;
	private String symbol;
	
	public Operator(String symbol)
	{
		this.symbol = symbol;
		initValue();
	}
	
	public void initValue()
	{
		switch(symbol)
		{
			case "*":
				value = Constants.MULTIPLY;
				break;
			case "/":
				value = Constants.DIVIDE;
				break;
			case "+":
				value = Constants.PLUS;
				break;
			case "-":
				value = Constants.MINUS;
				break;
			case "^":
				value = Constants.EXPONENTS;
				break;
			case "p":
				value = Constants.UPLUS;
				break;
			case "m":
				value = Constants.UMINUS;
				break;
		}
	}
	
	public int getValue()
	{
		return value;
	}
	
	public String getSymbol()
	{
		return symbol;
	}
	
}
