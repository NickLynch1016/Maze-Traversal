import java.util.Arrays;
//import java.util.StringTokenizer;
import itsc2214.*;

/**
 * Project 3 Setup ITSC 2214 Fall 2025.
 * 
 * @author Nick Lynch.
 */
public class Project3 implements ExpressionEvaluator {

    /**
     * Parsew an expression in a string into an array of string tokens.
     * 
     * @param expr is the string to be parsed into an array of Strings.
     * @return a String array of String expr separated.
     */
    public String[] tokenize(String expr) {
        String newExpr = expr.replaceAll("\\s+", ""); // remove whitespace first
        String[] tokens = newExpr.split("(?<=[-+*/()])|(?=[-+*/()])");
        return Arrays.stream(tokens)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * Use a stack to check for balanced parenthesis.
     * 
     * @param expr is the string to be checked.
     * @return true if the parenthesis are balanced and false is otherwise.
     */
    @Override
    public boolean balancedParenthesis(String expr) {
        StackADT<String> newStack = Factory.makeStackArrayList();
        String[] tokenized = tokenize(expr);

        for (String word : tokenized) {
            if (word.equals("(")) {
                newStack.push(word);
            }

            if (word.equals(")")) {
                if (newStack.isEmpty()) {
                    return false;
                }

                newStack.pop();

                // if (!justPopped.equals("(")) {
                // return false;
                // }
            }
        }

        if (newStack.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Convert from infix to postfix, solving all parenthesis while preserving the
     * order of evaluation, returning a queue of Strings containing values and
     * operators (no parenthesis).
     * 
     * @param expr is the string to be simplfied.
     * @return a Queue of Strings containing only the values and operators.
     */
    @Override
    public QueueADT<String> infix2Postfix(String expr) {
        StackADT<String> newStack = Factory.makeStackArrayList();
        QueueADT<String> newQueue = Factory.makeQueueArrayList();
        String[] tokenized = tokenize(expr);

        for (String word : tokenized) {
            if (word.matches("[a-zA-Z0-9]+")) {
                newQueue.enqueue(word);
            }

            if (word.equals("(")) {
                newStack.push(word);
            }

            if (word.equals(")")) {
                while (!newStack.isEmpty() && !newStack.peek().equals("(")) {
                    newQueue.enqueue(newStack.pop());
                }
                if (!newStack.isEmpty()) {
                    newStack.pop();
                }
            }

            else if ("+-*/^".contains(word)) {
                while (!newStack.isEmpty() && !newStack.peek().equals("(")) {
                    newQueue.enqueue(newStack.pop());
                }

                newStack.push(word);
            }
        }

        while (!newStack.isEmpty()) {
            newQueue.enqueue(newStack.pop());
        }

        return newQueue;
    }

    /**
     * Use a stack to evaluate postfix throwing an exception if division by 0
     * occurs.
     * 
     * @param givenQ is the QueueADT of strings to be checked for exceptions.
     * @return 0 if division by 0 occurs, otherwise return the result of the
     *         operations.
     */
    @Override
    public int evaluatePostfix(QueueADT<String> givenQ)
            throws ArithmeticException, IllegalArgumentException {
        StackADT<String> newStack = Factory.makeStackArrayList();

        while (!givenQ.isEmpty()) {
            String deToken = givenQ.dequeue();

            if (deToken.matches("[a-zA-Z0-9]+")) {
                newStack.push(deToken);
            }

            else if (deToken.equals("+")) {
                int two = Integer.parseInt(newStack.pop());
                int one = Integer.parseInt(newStack.pop());
                newStack.push(String.valueOf(one + two));
            }

            else if (deToken.equals("-")) {
                int two = Integer.parseInt(newStack.pop());
                int one = Integer.parseInt(newStack.pop());
                newStack.push(String.valueOf(one - two));
            }

            else if (deToken.equals("*")) {
                int two = Integer.parseInt(newStack.pop());
                int one = Integer.parseInt(newStack.pop());
                newStack.push(String.valueOf(one * two));
            }

            else if (deToken.equals("/")) {
                int two = Integer.parseInt(newStack.pop());
                int one = Integer.parseInt(newStack.pop());
                if (two == 0) {
                    throw new ArithmeticException("Can't divide by zero");
                }

                newStack.push(String.valueOf(one / two));
            }
        }

        return Integer.parseInt(newStack.peek());
    }

    /**
     * Checks if the parenthesis are balanced, then run the evaluatePostfix method
     * to get the result of the operations.
     * 
     * @param expr is the string to be passed to balancedParenthesis to be checked.
     * @return the result from evluatedPostFix() if the string expr doesn't throw an
     *         IllegalArgumentException.
     */
    @Override
    public int evaluateInfix(String expr) throws ArithmeticException, IllegalArgumentException {
        if (!balancedParenthesis(expr)) {
            throw new IllegalArgumentException("The paraenthesis aren't balanced");
        }

        QueueADT<String> newQueue = infix2Postfix(expr);

        return evaluatePostfix(newQueue);
    }
}