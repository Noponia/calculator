package com.fdm.tdd.calculator;

import java.util.ArrayList;

public class ProcessMathString 
{
	private String userMathExpr;
	private String mathExpr;
	private ArrayList<String> mathExprArr;


	public ProcessMathString(String userMathExpr)
	{
		this.userMathExpr = userMathExpr;
		this.mathExpr = "";
		this.mathExprArr = new ArrayList<>();
	}
	
	public void normalizeMathExpression()
	{
		removeWhiteSpace(0);
		processUnaryOperator(0);
		processMathExpr(0, "");
	}
	
	public String removeWhiteSpace(int index)
	{
		// Exit case
		if(index == userMathExpr.length())
		{
			return mathExpr;
		}
		
		char currChar = currentChar(index, userMathExpr);
		
		if(!(currChar == ' '))
		{
			mathExpr += currChar;
		}
		
		return removeWhiteSpace(index + 1);
	}
	
	/*
	 * This function will process the math expression string.
	 * Unary operators will be replaced with a:
	 * 1) 'm' if it's a negative unary operator
	 * 2) 'p' if it's a positive unary operator
	 * The function assumes no blank spaces, a valid expression and length > 1
	 */	
	public String processUnaryOperator(int index)
	{	
		// Exit case
		if(index == mathExpr.length())
		{
			return mathExpr;
		}
		
		char currChar = currentChar(index, mathExpr);
		
		// This checks for any unary operator at the start of a math expression
		if(isCharAhead(index, mathExpr) && !isCharBehind(index, mathExpr))
		{
			char charAhead = charAhead(index, mathExpr);
			if("0123456789(.".contains(String.valueOf(charAhead)) && (currChar == '+' || currChar == '-'))
			{
				mathExpr = replaceCharWithUnary(mathExpr, currChar, index);	
			}
		}
		
		// This checks for any unary operator in the middle of the math expression
		else if(isCharAhead(index, mathExpr) && isCharBehind(index, mathExpr))
		{	
			if("-+^/*(".contains(String.valueOf(charBehind(index, mathExpr))) &&
			  (currChar == '+' || currChar == '-') &&
			  (isDigit(index + 1, mathExpr) || charAhead(index, mathExpr) == '('))
			{
				mathExpr = replaceCharWithUnary(mathExpr, currChar, index);	
			}
					
		}
				
		return processUnaryOperator(index + 1);
	}
	
	/*
	 * This function will separate operators and operands.
	 * The function loops through string left to right. currString variable will begin as
	 * an empty string. Continuous sequence of numbers will be appended to currString. If an operator
	 * is encountered it signifies the end of a number sequence and therefore is reset
	 * as a blank string.
	 * 
	 * This function only assumes a valid string i.e no white spaces or characters that aren't:
	 * 0-9, . , * , - , / , ^ , ( , ) , +
	 */
	public ArrayList<String> processMathExpr(int index, String currString)
	{
		// Exit case
		if(index == mathExpr.length())
		{
			if(currString.length() > 0)
			{
				mathExprArr.add(currString);
			}
			return mathExprArr;
		}
		
		char currChar = currentChar(index, mathExpr);
		
		// Check if current char is an operator, if so add to array list
		if(isOperator(index, mathExpr))
		{
			if(currString.length() > 0)
			{
				mathExprArr.add(currString);
				currString = "";
			}

			mathExprArr.add(String.valueOf(currChar));
		}
		
		else
		{
			currString += currChar;
		}
		
		return processMathExpr(index + 1, currString);
	}
	
	public ArrayList<String> getMathExprArr()
	{
		return mathExprArr;
	}
	
	// This function just checks if there's a character ahead of the current position
	public boolean isCharAhead(int index, String mathExpr)
	{
		if(index + 1 <= mathExpr.length() - 1)
		{
			return true;
		}
		return false;
	}
	
	// This function returns the char behind an index
	public boolean isCharBehind(int index, String mathExpr)
	{
		if(index - 1 >= 0)
		{
			return true;
		}
		return false;
	}
	
	// This function returns the char ahead of an index
	public char charAhead(int index, String mathExpr)
	{
		
		return mathExpr.charAt(index + 1);
	}
	
	// This function returns the char behind an index
	public char charBehind(int index, String mathExpr)
	{
		return mathExpr.charAt(index - 1);
	}
	
	// This function returns the char in the current position
	public char currentChar(int index, String mathExpr)
	{
		return mathExpr.charAt(index);
	}
	
	// This function check if char is a digit
	public boolean isDigit(int index, String mathExpr)
	{
		if("1234567890.".contains(String.valueOf(mathExpr.charAt(index))))
		{
			return true;
		}
		return false;
	}
	
	// This function check if char is an operator
	public boolean isOperator(int index, String mathExpr)
	{
		if("-+^/*()pm".contains(String.valueOf(mathExpr.charAt(index))))
		{
			return true;
		}
		return false;
	}
	
	// This function replaces any unary operators with 'm' if '-' and 'p' if '+'
	public String replaceCharWithUnary(String mathExpr, char unary, int index)
	{
		if(unary == '+')
		{
			mathExpr = mathExpr.substring(0, index) + "p" + mathExpr.substring(index + 1);
		}
		else
		{
			mathExpr = mathExpr.substring(0, index) + "m" + mathExpr.substring(index + 1);
		}
		
		return mathExpr;
	}
	
}
