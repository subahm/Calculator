
public class Tools {

	public static boolean isBalancedBy( String tokens, String text ) {
		return genBalanced(tokens,text);
	}
		
	/** 
	 * Private recursive method that does all the work for the isBalancedBy method.
	 * Based on the notion that every externally balanced matching set of parentheses must contain
	 * a balanced string.
	 * Also, a balanced string may have several non-nesting sets that are treated as separate strings.
	 * @param textPortion The text to check
	 * @param tokens The parentheses.
	 */
	private static boolean genBalanced(String tokens, String textPortion) {
		// base case: substring is empty
		if (textPortion.length() == 0) {
			return true;
		}
		// look for first open paren: everything before that can be discarded.
		// if closed paren found first: bad string
		int openIndex = -1;
		int openerIndex = -1;
		boolean stop = false;
		for (int i=0; i<textPortion.length() && !stop; i++) {
			// check each open paren as a possibility
			for (int k=0; k<tokens.length(); k+=2) {
				if (textPortion.charAt(i)==tokens.charAt(k+1)) {
					// closed paren found
					return false;
				}
				if (textPortion.charAt(i)==tokens.charAt(k)) {
					openIndex = i;
					openerIndex = k;
					stop = true;
					break;
				}
			}
		}
		if (openIndex == -1) {
			// no paren found
			return true;
		}
		// find the matching closing paren
		char opener = tokens.charAt(openerIndex);
		char closer = tokens.charAt(openerIndex+1);
		int closeIndex = -1;
		int balance = 1;
		for (int i=openIndex+1; i<textPortion.length(); i++) {
			if (textPortion.charAt(i) == opener) {
				balance++;
			} else if (textPortion.charAt(i) == closer) {
				balance--;
				if (balance == 0) {
					closeIndex = i;
					break;
				}
			}
		}
		if (closeIndex == -1) {
			// no matching closing paren
			return false;
		}
		// recurse on the substring inside the parentheses and 
		// then on any remaining substring proceeding these parentheses that we have not looked at yet.
		boolean firstResult = genBalanced(tokens,textPortion.substring(openIndex+1,closeIndex));
		boolean secondResult = true;
		if (closeIndex+1 < textPortion.length()) {
			secondResult = genBalanced(tokens,textPortion.substring(closeIndex+1,textPortion.length()));
		}
		return firstResult && secondResult;
	}
			
	/**
	 * Test harness to check methods.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		System.out.println("testing the isBalancedBy method:");
		String tokens = "(){}[]<>";
		String test = "()()(){()()}[]<>";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		test = "hello";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		test = "<<><<>>>>";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		test = "((?)";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		// and the one that was initially missing:
		test = "({)}";
		System.out.println(test+" result is "+isBalancedBy("{}()<>",test));
	}
}
