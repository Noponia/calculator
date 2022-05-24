package com.fdm.tdd.calculator;

import java.util.ArrayList;
import java.util.Stack;

public class ReversePolishNotation 
{

	private ArrayList<String> mathExprArr;
	private ArrayList<String> postFixArr;
	private Stack<Operator> stack;
	
	public ReversePolishNotation(ArrayList<String> mathExprArr)
	{
		this.mathExprArr = mathExprArr;
		this.postFixArr = new ArrayList<>();
		this.stack = new Stack<>();
		
		infixToPostFix(0);
	}
	
	/*
	 * This function converts infix form of a math expression into the RPN form.
	 */
	public int infixToPostFix(int index)
	{
		// Exit case
		if(index == mathExprArr.size()) 
		{
			// There may be operators still in the stack pop them out until its cleared
			stack = clearStack(stack);
			return 0;
		}
		
		String currItem = mathExprArr.get(index);
		
		
		// Check if an operator is found
		if(isOperator(index))
		{
			Operator op = new Operator(currItem);
			
			// If the stack is empty or is a "(" just add the operator to the top of the stack
			if(stack.size() == 0 || op.getSymbol().equals("("))
			{
				stack.push(op);
			}
			
			// Else manageStack is called to determine how to place the operator in the stack
			else 
			{
				stack = manageStack(op, stack);
			}
			
		}
		
		else
		{
			postFixArr.add(currItem);
		}
		
		return infixToPostFix(index + 1);
	}
	
	
	/*
	 * This function is intended to work with the function infixToPostFix.
	 * For a given iteration in the infixToPostFix function that requires processing of an
	 * operation, the operation will push operations off the stack (if required) until it's
	 * added to the stack.
	 */
	
	public Stack<Operator> manageStack(Operator op, Stack<Operator> stack)
	{
	
		// Exit if the stack is empty
		if(stack.size() == 0)
		{
			stack.push(op);
			return stack;
		}
		
		Operator topOp = stack.peek();
		
		// If operator is ")" keep popping the stack until "(" is found
		if(op.getSymbol().equals(")"))
		{
			// If operator "(" is found, parenthesis has been resolved, pop it off the stack and exit.
			if(topOp.getSymbol().equals("("))
			{
				stack.pop();
				return stack;
			}
			
			else
			{
				postFixArr.add(stack.pop().getSymbol());
			}
		}
		
		/*
		 *  Compare precedence of operators, if current operator greater in precedence
		 *  than operator on top of stack push it on top of stack. Else pop the top of the stack 
		 *  until either the stack is empty or an operator of less precedence is found.
		 */
	
		else if(topOp.getValue() >= op.getValue())
		{
			postFixArr.add(stack.pop().getSymbol());
		}
		
		else if(topOp.getValue() < op.getValue()) 
		{
			stack.push(op);
			return stack;
		}
		
		// Keep calling the function managaStack until the above IFS are resolved
		return manageStack(op, stack);
	}
	
	public Stack<Operator> clearStack(Stack<Operator> stack)
	{
		if(stack.size() == 0)
		{
			return stack;
		}
		
		else
		{
			postFixArr.add(stack.pop().getSymbol());
		}
		
		return clearStack(stack);
	}
	
	public boolean isOperator(int index)
	{
		if("-+^/*()pm".contains(String.valueOf(mathExprArr.get(index))))
		{
			return true;
		}
		return false;
	}
	
	public ArrayList<String> getPostFixArr()
	{
		return postFixArr;
	}
	
}
