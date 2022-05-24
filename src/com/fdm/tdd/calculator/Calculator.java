package com.fdm.tdd.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator implements ICalculator
{
	private final double NEWTONROOTEPS = 1E-10;				// Allowable margin of error when using Newton's Root Method
	private double newtonPrevValue;							// Value to keep track of the previous iteration value when using Newton's Root Method
	
	private Stack<Double> numStack;							// Number stack to evaluate a RPN expression
	private ArrayList<String> postFixArr;					// Array for the math expression in RPN form
	
	public Calculator()
	{
		this.numStack = new Stack<>();
	}
	
	/*
	 * This function will evaluate a mathematical expression in post-fix-notation. Numbers are pushed onto a stack called numStack and operations will
	 * pop the top 2 elements in a stack to be then operated on.
	 */
	public double calculate(int index)
	{
		// Exit case
		if(index == postFixArr.size())
		{
			return numStack.get(0).doubleValue();
		}
		
		String currItem = postFixArr.get(index);
		
		// Check unary operator '+'
		if(currItem.equals("p"))
		{
			numStack.push(numStack.pop());
		}
		
		// Check unary operator '-'
		else if(currItem.equals("m"))
		{
			numStack.push(numStack.pop().doubleValue() * -1);
		}
		
		else if(isOperator(currItem))
		{
			// Process operators in postFixArr
			double rightOperand = numStack.pop().doubleValue();
			double leftOperand = numStack.pop().doubleValue();
			
			switch(currItem)
			{
				case "*":
					numStack.push(leftOperand * rightOperand);
					break;
				case "/":
					numStack.push(leftOperand / rightOperand);
					break;
				case "+":
					numStack.push(leftOperand + rightOperand);
					break;
				case "-":
					numStack.push(leftOperand - rightOperand);
					break;
				case "^":
					numStack.push(power(leftOperand, rightOperand));
					break;
			}
		}
		
		else
		{
			// Element is a number so just push it to the stack
			numStack.push((Double) Double.valueOf(currItem));
		}

		return calculate(index + 1);
	}
	
	public boolean isOperator(String str)
	{
		if("*/+-^".contains(str))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * This function calculate base^exp. To allow for fractional exponents base^exp can be expressed as:
	 * (x^(1/n))^y where exp = y/n. ALso, 'n' and 'y' are whole numbers.
	 * x^(1/n) i.e the nth root of 'x' can be calculated using Newton's Root Method
	 */
	public double power(double base, double exp)
	{
		double[] fraction = expDecimalToFraction(exp);
		if(base < 0)
		{
			return  -1 * powerIntExp(newtonRoot(fraction[1], 1, 0, getAbs(base)), fraction[0]);
		}
		else
		{
			return  powerIntExp(newtonRoot(fraction[1], 1, 0, base), fraction[0]);
		}
	}
	
	/*
	 *  This function converts a decimal to a fraction and returns an array of two numbers in the format:
	 *  [ numerator, denominator ]
	 *  The numerator will be the exp value with the '.' character stripped off i.e only have the digits
	 *  The denominator will be 10 ^ number of decimal places
	 */
	public double[] expDecimalToFraction(double exp)
	{
		// Rounding to 3 decimal places
		BigDecimal expBD = new BigDecimal(exp);
		MathContext mc = new MathContext(3);
		expBD = expBD.round(mc);
		double denominator;

		String expStr = expBD.toString();

		double numerator = Double.valueOf(expStr.replace(".", ""));

		// The string expStr will contain a '.' if is a decimal number, but will not if it's a whole number. 
		if(expStr.contains("."))
		{
			String denominatorStr = "1" + "0".repeat(expStr.substring(expStr.indexOf('.') + 1).length());
			denominator = Double.valueOf(denominatorStr);
		}
		else
		{
			denominator = 1;
		}

		double[] arr = {numerator, denominator};
		
		return arr;
	}
	
	
	/*
	 * This function calculates x^y using an integer exponent. Double data type
	 * used for larger integers.
	 */
	public double powerIntExp(double base, double exp)
	{
		if(exp == 0)
		{
			return 1;
		}
		
		else if(exp == 1)
		{
			return base;
		}
		
		else if(exp > 1)
		{
			return base * powerIntExp(base, exp - 1);
		}
		
		else
		{
			return  powerIntExp(base, exp + 1) / base;
		}
	}
	
	/*
	 * This is an implementation of newton's root which returns the 'nth' root of a number 'A'.
	 * n = 'nth' root
	 * x = calculated value
	 * A = number being processed
	 * The function recursively calls itself until the difference between the calculated value and the value before it
	 * is close to 0 or within the range -NEWTONROOTEPS <= 0 <= NEWTONROOTEPS
	 */
	public double newtonRoot(double n, double x, int num, double A)
	{
		if(getAbsDifference(x, newtonPrevValue) >= 0 && 
		   getAbsDifference(x, newtonPrevValue) <= NEWTONROOTEPS &&
		   num != 0)
		{
			return x;
		}
				
		newtonPrevValue = x;
		double xNext = (n - 1)/ n * x + A / n * 1 / powerIntExp(x , n - 1);
		return newtonRoot(n, xNext, num + 1, A);
	}

	/*
	 * This function gets the absolute difference between two numbers.
	 */
	public double getAbsDifference(double a, double b)
	{
		if(a < b)
		{
			return b - a;
		}
		else
		{
			return a - b;
		}
	}
	
	public double getAbs(double a)
	{
		if(a < 0)
		{
			return a * -1;
		}
		else
		{
			return a;
		}
	}
	
	public ArrayList<String> getPostFixArr()
	{
		return postFixArr;
	}
	
	/*
	 * This function returns the calculated output from the math expression provided by User. It processes the string
	 * using functions from the class 'ProcessMathString'. The processed string is then parsed into the class 
	 * 'ReversePolishNotation' which turns the math expression into RPN form. The expression in RPN form is then evaluated.
	 */
	@Override
	public double evaluate(String expression) 
	{		
		// Process user string
		ProcessMathString pms = new ProcessMathString(expression);
		pms.normalizeMathExpression();
		
		// Translate infix to post-fix form
		ReversePolishNotation rpn = new ReversePolishNotation(pms.getMathExprArr());
		postFixArr = rpn.getPostFixArr();
		
		return calculate(0);
	}

}
