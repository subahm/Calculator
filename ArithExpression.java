
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ArithExpression {

	private TokenList postfixTokens;
	private TokenList infixTokens;

	/**
	 * Sets up a legal standard Arithmetic expression.
	 * The only parentheses accepted are "(" and ")".
	 * @param word An arithmetic expression in standard infix order.
	 * 	An invalid expression is not expressly checked for, but will not be
	 * 	successfully evaluated, when the <b>evaluate</b> method is called.
	 * @throws InvalidExpressionException if the expression cannot be properly parsed,
	 *  	or converted to a postfix expression.
	 */
	public ArithExpression(String word) {
		if (Tools.isBalancedBy("()",word)) {
			tokenizeInfix(word);
			infixToPostfix();
		} else {
			throw new InvalidExpressionException("Parentheses unbalanced");
		}
	}

	
	private void tokenizeInfix(String express) {
		infixTokens  = new TokenList(express.length());

		// regular expression that looks for any operators or parentheses.
		Pattern opParenPattern = Pattern.compile("[-+*/^()]");
		Matcher opMatcher = opParenPattern.matcher(express);

		String matchedBit, nonMatchedBit;
		int lastNonMatchIndex = 0;
		String lastMatch = "";

		// find all occurrences of a matched substring
		while (opMatcher.find()) {
			matchedBit = opMatcher.group();
			// get the substring between matches
			nonMatchedBit = express.substring(lastNonMatchIndex, opMatcher.start());
			nonMatchedBit = nonMatchedBit.trim(); //removes outside whitespace
			// The very first '-' or a '-' that follows another operator is considered a negative sign
			if (matchedBit.charAt(0) == '-') {
				if (opMatcher.start() == 0 || 	
					!lastMatch.equals(")") && nonMatchedBit.equals("")) {
					continue;  
				}
			}
			// nonMatchedBit can be empty when an operator follows a ')'
			if (nonMatchedBit.length() != 0) {
				infixTokens.append(nonMatchedBit);
			}
			lastNonMatchIndex = opMatcher.end();
			infixTokens.append(matchedBit);
			lastMatch = matchedBit;
		}
		// parse the final substring after the last operator or paren:
		if (lastNonMatchIndex < express.length()) {
			nonMatchedBit = express.substring(lastNonMatchIndex,express.length());
			nonMatchedBit = nonMatchedBit.trim();
			infixTokens.append(nonMatchedBit);
		}
	}

	/**
	 * Determines whether a single character string is an operator.
	 * The allowable operators are {+,-,*,/,^,(,)}.
	 * @param op The string in question.
	 * @return True if it is recognized as a an operator.
	 */
	public static boolean isOperator(String op) {
		if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("^")|| op.equals("(") || op.equals(")")) {
				return true;
		} else {
				return false;
		}
	}
	
	private static int checkPrecedence (String item) {
		if(item.equals("+") || item.equals("-")){
			return 1;
		} else if(item.equals("*") || item.equals("/")){
			return 2;
		} else if(item.equals("^")){
			return 3;
		} else{
			return 0;
		}
	}
		

	 /**
	 * A private method that initializes the postfixTokens data field.
	 * It takes the information from the infixTokens data field.
	 * If, during the process, it is determined that the expression is invalid,
	 * an InvalidExpressionException is thrown.
 	 * Note that since the only method that calls this method is the constructor,
	 * the Exception is propogated through the constructor.
	 */
	private void infixToPostfix() {
		postfixTokens=  new TokenList();
		StringStack myStack= new StringStack();
		for(int i=0; i<infixTokens.size(); i++){
			String token= infixTokens.get(i);
			
			if(this.isOperator(token)){
				if(myStack.isEmpty() || checkPrecedence(token)>checkPrecedence(myStack.peek())){
					myStack.push(token);
				} else {
					if(token.equals("(")){
						myStack.push(token);
					} else
					if(token.equals(")")){
						while(!myStack.isEmpty() && !(myStack.peek().equals("("))){
							postfixTokens.append(myStack.pop());
						}
						if(myStack.peek().equals("(")){
							myStack.pop();
						}
					} else {
						while(!myStack.isEmpty() && checkPrecedence(token) <= checkPrecedence(myStack.peek())){
							postfixTokens.append(myStack.pop());
						}
						myStack.push(token);
					}
					
					
				}
			} else {
				postfixTokens.append(token);   
			}
			
		}
		while(!myStack.isEmpty()){
				postfixTokens.append(myStack.pop());
			}
	}

	public String getInfixExpression() {
		return infixTokens.toString();
	}

	public String getPostfixExpression() {
		return postfixTokens.toString();
	}
		
	public double evaluate() {
		StringStack myStack= new StringStack();
		for(int i=0; i<postfixTokens.size(); i++){
			String token = postfixTokens.get(i);
			if(!isOperator(token)){
				myStack.push(token);
			}
			if(isOperator(token)){
				double a = Double.parseDouble(myStack.pop());
				double b = Double.parseDouble(myStack.pop());
				if(token.equals("+")){
					double c = a+b;
					myStack.push(""+c);
				}
				else if(token.equals("-")){
					double c = b-a;
					myStack.push(""+c);
				}
				else if(token.equals("*")){
					double c = a*b;
					myStack.push(""+c);
				}
				else if(token.equals("/")){
					double c = b/a;
					myStack.push(""+c);
				}
				else if(token.equals("^")){
					double c = Math.pow(b,a);
					myStack.push(""+c);
				}
			}
		}
	
		return Double.parseDouble(myStack.pop());
	}
	
						
	public static void main(String[] args) {
		ArithExpression x= new ArithExpression("(6+2)*3+6/2");
		ArithExpression y= new ArithExpression("5^(4-2)");
		ArithExpression z= new ArithExpression("-6*4+(2^1)");
		System.out.println(x.getInfixExpression());
		System.out.println(y.getInfixExpression());
		System.out.println(z.getInfixExpression());
		System.out.println(x.getPostfixExpression());
		System.out.println(y.getPostfixExpression());
		System.out.println(z.getPostfixExpression());
		System.out.println(x.evaluate());
		System.out.println(y.evaluate());
		System.out.println(z.evaluate());
	}
			
}
