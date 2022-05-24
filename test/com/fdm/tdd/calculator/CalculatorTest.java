package com.fdm.tdd.calculator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {

	Calculator calculator;
	ProcessMathString pms;
	ReversePolishNotation rpn;
	
	@BeforeEach
	void setup()
	{
		calculator = new Calculator();
	}
			
	@Test
	void test_remove_white_space_1()
	{
		pms = new ProcessMathString("2  +  3 * (2-1)     ");
		String result = pms.removeWhiteSpace(0);
		assertEquals("2+3*(2-1)", result);
	}

	@Test
	void test_remove_white_space_2()
	{
		pms = new ProcessMathString("2+3");
		String result = pms.removeWhiteSpace(0);
		assertEquals("2+3", result);
	}
	
	@Test
	void test_char_is_digit_at_index_0_in_expression_2_plus_3()
	{
		pms = new ProcessMathString("2+3");
		boolean result = pms.isDigit(0, "2+3");
		assertTrue(result);
	}
	
	@Test
	void test_char_is_not_a_digit_at_index_1_in_expression_2_plus_3()
	{
		pms = new ProcessMathString("2+3");
		boolean result = pms.isDigit(1, "2+3");
		assertFalse(result);
	}
	
	@Test
	void test_char_is_a_operator_at_index_1_in_expression_2_plus_3()
	{
		pms = new ProcessMathString("2+3");
		boolean result = pms.isOperator(1, "2+3");
		assertTrue(result);
	}
	
	@Test
	void test_char_ahead_at_index_in_expression_2_plus_3()
	{
		pms = new ProcessMathString("2+3");
		boolean result = pms.isCharAhead(0, "2+3");
		assertTrue(result);
	}
		
	@Test
	void test_char_not_ahead_at_index_2_in_expression_2_plus_3()
	{
		pms = new ProcessMathString("2+3");
		boolean result = pms.isCharAhead(2, "2+3");
		assertFalse(result);
	}
	
	@Test
	void test_char_not_behind_at_index_0_in_expression_2_plus_3()
	{
		pms = new ProcessMathString("2+3");
		boolean result = pms.isCharBehind(0, "2+3");
		assertFalse(result);
	}
	
	@Test
	void test_replace_neg_for_m_in_expression_2_minus_neg3()
	{
		pms = new ProcessMathString("2--3");
		String result = pms.replaceCharWithUnary("2--3", '-', 2);
		assertEquals("2-m3", result);
	}
	
	@Test
	void test_identify_negative_unary_neg4()
	{
		pms = new ProcessMathString("-4");
		pms.removeWhiteSpace(0);
		String result = pms.processUnaryOperator(0);
		assertEquals("m4", result);
	}
	
	@Test
	void test_identify_negative_unary_neg4_minus_neg4()
	{
		pms = new ProcessMathString("-4- -4");
		pms.removeWhiteSpace(0);
		String result = pms.processUnaryOperator(0);
		assertEquals("m4-m4", result);
	}
	
	@Test
	void test_identify_negative_unary_operators_neg4_times_neg4_plus_6()
	{
		pms = new ProcessMathString("-4 * -4 + 6");
		pms.removeWhiteSpace(0);
		String result = pms.processUnaryOperator(0);
		assertEquals("m4*m4+6", result);
	}
	
	@Test
	void test_identify_negative_unary_operators_with_brackets()
	{
		pms = new ProcessMathString("-(-4+6)*4-4");
		pms.removeWhiteSpace(0);
		String result = pms.processUnaryOperator(0);
		assertEquals("m(m4+6)*4-4", result);
	}
	
	@Test
	void test_identify_positive_unary_operators_plus4_plus_plus4()
	{
		pms = new ProcessMathString("+4 + +4");
		pms.removeWhiteSpace(0);
		String result = pms.processUnaryOperator(0);
		assertEquals("p4+p4", result);
	}
	
	@Test
	void test_identify_positive_unary_operators_brackets()
	{
		pms = new ProcessMathString("+(+4+6)*4-4");
		pms.removeWhiteSpace(0);
		String result = pms.processUnaryOperator(0);
		assertEquals("p(p4+6)*4-4", result);
	}
	
	@Test
	void test_create_math_expr_array_list_with_multiple_whole_numbers()
	{
		pms = new ProcessMathString("-(-4+6)*4-4");
		pms.removeWhiteSpace(0);
		pms.processUnaryOperator(0);
		ArrayList<String> result = pms.processMathExpr(0, "");
		String[] required = {"m","(","m", "4", "+", "6", ")" ,"*", "4", "-", "4"};
		assertEquals(result, Arrays.asList(required));
	}
	
	@Test
	void test_create_math_expr_array_list_with_multiple_whole_and_decimal_numbers()
	{
		pms = new ProcessMathString("-(-4.123+6)*4.123-4.46");
		pms.removeWhiteSpace(0);
		pms.processUnaryOperator(0);
		ArrayList<String> result = pms.processMathExpr(0, "");
		String[] required = {"m","(","m", "4.123", "+", "6", ")" ,"*", "4.123", "-", "4.46"};
		assertEquals(result, Arrays.asList(required));
	}
	
	@Test
	void test_post_fix_number_manage_stack_when_multiply_pushed_on_plus()
	{
		rpn = new ReversePolishNotation(new ArrayList<String>());
		
		Operator op = new Operator("*");
		
		Stack<Operator> stack = new Stack<>();
		stack.push(new Operator("+"));
		
		Stack<Operator> resultStack = rpn.manageStack(op, stack);
		String result = printOperatorStack(0, resultStack, "");
		assertEquals(result, "+*");
		
	}
	
	@Test
	void test_post_fix_number_manage_stack_when_plus_pushed_on_plus()
	{
		rpn = new ReversePolishNotation(new ArrayList<String>());
		
		Operator op = new Operator("+");
		
		Stack<Operator> stack = new Stack<>();
		stack.push(new Operator("+"));
		
		Stack<Operator> resultStack = rpn.manageStack(op, stack);
		String result = printOperatorStack(0, resultStack, "");
		assertEquals(result, "+");
	}
	
	void test_post_fix_number_manage_stack_when_plus_pushed_on_plus_plus()
	{
		rpn = new ReversePolishNotation(new ArrayList<String>());
		
		Operator op = new Operator("+");
		
		Stack<Operator> stack = new Stack<>();
		stack.push(new Operator("+"));
		stack.push(new Operator("+"));
		
		Stack<Operator> resultStack = rpn.manageStack(op, stack);
		String result = printOperatorStack(0, resultStack, "");
		assertEquals(result, "+");
	}
	
	void test_post_fix_number_manage_stack_when_plus_pushed_on_plus_times_power()
	{
		rpn = new ReversePolishNotation(new ArrayList<String>());
		
		Operator op = new Operator("+");
		
		Stack<Operator> stack = new Stack<>();
		stack.push(new Operator("+"));
		stack.push(new Operator("*"));
		stack.push(new Operator("^"));

		Stack<Operator> resultStack = rpn.manageStack(op, stack);
		String result = printOperatorStack(0, resultStack, "");
		assertEquals(result, "+");
	}
	
	void test_post_fix_number_manage_stack_when_closing_bracket_encountered()
	{
		rpn = new ReversePolishNotation(new ArrayList<String>());
		
		Operator op = new Operator(")");
		
		Stack<Operator> stack = new Stack<>();
		stack.push(new Operator("+"));
		stack.push(new Operator("("));
		stack.push(new Operator("-"));
		stack.push(new Operator("*"));

		Stack<Operator> resultStack = rpn.manageStack(op, stack);
		String result = printOperatorStack(0, resultStack, "");
		assertEquals(result, "+");
	}
	
	void test_post_fix_number_manage_stack_when_plus_pushed_on_plus_inside_brackets()
	{
		rpn = new ReversePolishNotation(new ArrayList<String>());
		
		Operator op = new Operator("+");
		
		Stack<Operator> stack = new Stack<>();
		stack.push(new Operator("+"));
		stack.push(new Operator("("));
		stack.push(new Operator("+"));

		Stack<Operator> resultStack = rpn.manageStack(op, stack);
		String result = printOperatorStack(0, resultStack, "");
		assertEquals(result, "+(+");
	}
	
	@Test
	void text_post_fix_number_1()
	{
		calculator.evaluate("1");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("1", result);
	}
	
	@Test
	void text_post_fix_number_neg1()
	{
		calculator.evaluate("-1");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("1m", result);
	}
	
	@Test
	void test_post_fix_number_conversion_2_plus_3() 
	{
		calculator.evaluate("2+3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("23+", result);
	}

	@Test
	void test_post_fix_number_conversion_neg2_minus_3() 
	{
		calculator.evaluate("-2-3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("2m3-", result);
	}
	
	@Test
	void test_post_fix_number_conversion_8_times_neg3() 
	{
		calculator.evaluate("-8-3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("8m3-", result);
	}
	
	@Test
	void test_post_fix_number_combination_plus_minus_divide_times_whole_numbers() 
	{
		calculator.evaluate("8*6-5*4+6/4-3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("86*54*-64/+3-", result);
	} 
	
	@Test
	void test_post_fix_number_combination_plus_minus_divide_times_decimal_numbers() 
	{
		calculator.evaluate("8.03*6.2-5.0*4+6/4-3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("8.036.2*5.04*-64/+3-", result);
	} 
	
	@Test
	void test_post_fix_number_combination_plus_minus_divide_times_bracket_whole_numbers() 
	{
		calculator.evaluate("8*(6-5*4)+6/4-3*(1+4)");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("8654*-*64/+314+*-", result);
	} 
	
	@Test
	void test_post_fix_number_combination_plus_minus_divide_times_bracket_decimal_numbers() 
	{
		calculator.evaluate("8.03*(6.2-5.0*4)+6/4-3*(1+4)");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("8.036.25.04*-*64/+314+*-", result);
	} 
	
	@Test
	void test_post_fix_number_combination_plus_minus_times_bracket_divide_unary_whole_numbers() 
	{
		calculator.evaluate("-2-3*-(5+6*4-5)+8*6/4-4");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("2m3564*+5-m*-86*4/+4-", result);
	}
	
	@Test
	void test_post_fix_number_combination_plus_minus_times_bracket_divide_unary_decimal_numbers() 
	{
		calculator.evaluate("-2.1-3*-(5+6.6*4-5.5)+8*6/4.3-4");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("2.1m356.64*+5.5-m*-86*4.3/+4-", result);
	}
	
	@Test
	void test_post_fix_number_2_power_3()
	{
		calculator.evaluate("2^3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("23^", result);
	}
	
	@Test
	void test_post_fix_number_2_power_neg3()
	{
		calculator.evaluate("2^-3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("23m^", result);
	}
	
	@Test
	void test_post_fix_number_2_power_neg0point3()
	{
		calculator.evaluate("2^-0.3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("20.3m^", result);
	}
	
	@Test
	void test_post_fix_number_2_power_neg_bracket_1_times_3_minus_1_bracket()
	{
		calculator.evaluate("2^-(1*3-1)");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("213*1-m^", result);
	}
	
	@Test
	void test_post_fix_number_all_operators()
	{
		calculator.evaluate("-2.1-3^(5-5.5)+8*6/4.3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("2.1m355.5-^-86*4.3/+", result);
	}
	
	@Test
	void test_post_fix_number_nested_brackets()
	{
		calculator.evaluate("(((3)))");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("3", result);
	}
	
	@Test
	void test_post_fix_number_nested_brackets2()
	{
		calculator.evaluate("2*(2*(2*(3+3)))");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("22233+***", result);
	}
	
	@Test
	void test_post_fix_number_nested_brackets3()
	{
		calculator.evaluate("2*(2-4*(2*(3+3)))+3");
		String result = calculator.getPostFixArr().stream().collect(Collectors.joining());
		assertEquals("224233+**-*3+", result);
	}
	
	@Test
	void test_calculate_1_point_5()
	{
		double result = calculator.evaluate("1.5");
		assertEquals(1.5, result);
	}
	
	@Test
	void test_calculate_neg1_point_5()
	{
		double result = calculator.evaluate("-1.5");
		assertEquals(-1.5, result);
	}
	
	
	@Test
	void test_calculate_2_plus_3_equals_5() 
	{
		double result = calculator.evaluate("2+3");
		assertEquals(5.0, result);
	}
	
	@Test
	void test_calculate_2point8_plus_3point4_equals_6point2() 
	{
		double result = calculator.evaluate("2.8+3.4");
		assertEquals(6.2, result, 0.0001);
	}
	
	@Test
	void test_calculate_2_plus_plus_3_equals_5()
	{
		double result = calculator.evaluate("2++3");
		assertEquals(5.0, result);
	}
	
	@Test
	void test_calculate_2_minus_3_equals_neg_1()
	{
		double result = calculator.evaluate("2-3");
		assertEquals(-1, result);
	}
	
	@Test
	void test_calculate_2point41_minus_3point82_equals_neg_neg1point41()
	{
		double result = calculator.evaluate("2.41-3.82");
		assertEquals(-1.41, result, 0.0001);
	}
	
	@Test
	void test_calculate_2_minus_minus_3_equals_5()
	{
		double result = calculator.evaluate("2--3");
		assertEquals(5, result);
	}
	
	@Test
	void test_calculate_neg2_minus_mins_3_equals_5()
	{
		double result = calculator.evaluate("-2--3");
		assertEquals(1, result);
	}
	
	@Test
	void test_calculate_10_times_3_equals_30()
	{
		double result = calculator.evaluate("10*3");
		assertEquals(30, result);
	}
	
	@Test
	void test_calculate_10point52_times_3point5_equals_36point82()
	{
		double result = calculator.evaluate("10.52*3.5");
		assertEquals(36.82, result, 0.0001);
	}
	
	@Test
	void test_calculate_10_times_neg3_equals_neg30()
	{
		double result = calculator.evaluate("10*-3");
		assertEquals(-30, result);
	}
	
	@Test
	void test_calculate_neg10_times_neg3_equals_neg30()
	{
		double result = calculator.evaluate("-10*-3");
		assertEquals(30, result);
	}
	
	@Test
	void test_calculate_10_divided_3_equals_3_point_33()
	{
		double result = calculator.evaluate("10/3");
		assertEquals(3.333333, result, 0.0001);
	}
	
	@Test
	void test_calculate_10point5_divided_3_equals_3_point_5()
	{
		double result = calculator.evaluate("10.5/3");
		assertEquals(3.5, result, 0.0001);
	}
	
	@Test
	void test_calculate_combination_plus_minus_times_divide_brackets_1()
	{
		double result = calculator.evaluate("2*(1+1)-4*9/4+6");
		assertEquals(1, result, 0.0001);
	}
	
	@Test
	void test_calculate_combination_plus_minus_times_divide_brackets_2()
	{
		double result = calculator.evaluate("2*(1+1)-4*(9/4+6)");
		assertEquals(-29, result, 0.0001);
	}
	
	@Test
	void test_calculate_combination_plus_minus_times_divide_unary_brackets_1()
	{
		double result = calculator.evaluate("-2*(1+1)--4*9/4+6");
		assertEquals(11, result, 0.0001);
	}
	
	@Test
	void test_calculate_combination_plus_minus_times_divide_nested_brackets_1()
	{
		double result = calculator.evaluate("2*(1+1*2*(4+1*4))-4*(9/4+6)");
		assertEquals(1, result, 0.0001);
	}
	
	@Test
	void test_calculate_2_power_3_equals_8()
	{
		double result = calculator.powerIntExp(2, 3);
		assertEquals(8, result);
	}
	
	@Test
	void test_calculate_2_power_neg3_equals_0point125()
	{
		double result = calculator.powerIntExp(2, -3);
		assertEquals(0.125, result, 0.0001);
	}
	
	@Test
	void test_calculate_1point5_power_2_equals_2point25()
	{
		double result = calculator.powerIntExp(1.5, 2);
		assertEquals(2.25, result, 0.0001);
	}
	
	@Test
	void test_calculate_1point23_power_100()
	{
		double result = calculator.powerIntExp(1.23, 100);
		assertEquals(978388059.773, result, 0.001);
	}
	
	@Test
	void test_absolute_of_2_minus_3_equals_1()
	{
		double result = calculator.getAbsDifference(2, 3);
		assertEquals(1, result);
	}
	
	@Test
	void test_newton_5th_root_of_32() 
	{
		double result = calculator.newtonRoot(5, 2, 0, 32);
		assertEquals(2, result, 0.0001);
	}
	
	@Test
	void test_newton_5th_root_of_8point5()
	{
		double result = calculator.newtonRoot(5, 2, 0, 8.5);
		assertEquals(1.5342, result, 0.0001);
	}
	
	@Test
	void test_calculate_2_power_0point5_equals_1point41412()
	{
		double result = calculator.evaluate("2^0.5");
		assertEquals(1.41412, result, 0.0001);
	}
	
	@Test
	void test_calculate_2_power_0point5_in_fraction_form_equals_1point41412()
	{
		double result = calculator.evaluate("2^(1/2)");
		assertEquals(1.41412, result, 0.0001);
	}
	
	@Test
	void test_calculate_2_power_3point33_equals10point056()
	{
		double result = calculator.evaluate("2^3.33");
		assertEquals(10.056, result, 0.001);
	}
	
	@Test
	void test_calculate_123_power_neg0point03_equals_0point20432()
	{
		double result = calculator.evaluate("123^-0.33");
		assertEquals(0.20432, result, 0.0001);
	}
	
	
	@Test
	void test_calculate_3_power_2divide3()
	{
		double result = calculator.evaluate("3^(2/3)");
		assertEquals(2.080, result, 0.001);
	}
	
	@Test
	void test_calculate_10_power_0() 
	{
		double result = calculator.evaluate("10^0");
		assertEquals(1, result, 0.0001);
	}
	
	@Test
	void test_neg2_power_neg3point4()
	{
		double result = calculator.evaluate("-2^(-3.4)");
		assertEquals(-0.094732, result, 0.0001);
	}
	
	String printOperatorStack(int index, Stack<Operator> stack, String str)
	{
		if(index == stack.size())
		{
			return str;
		}
		
		str += stack.get(index).getSymbol();
		
		return printOperatorStack(index + 1, stack, str);
	}

}
