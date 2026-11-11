import itsc2214.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Project 3 Student Setup ITSC 2214 Fall 2025.
 * 
 * @author Nick Lynch.
 * @version Aug 02, 2025.
 */
public class Project3Test {
    private ExpressionEvaluator runner = null;

    /**
     * setup() method, runs before each of your test methods.
     * Use this method to recreate the objects needed for
     * testing your class.
     */
    @Before
    public void setup() {
        runner = new Project3();
    }

    /**
     * Test balancedParenthesis().
     */
    @Test
    public void testBalancedParenthesis() {
        assertTrue("() should be valid",
                runner.balancedParenthesis("()"));

        assertEquals(false,
                runner.balancedParenthesis("())"));
        // add other tests here to make sure you
        // have covered all of the possible options
        // on how parenthesis are balanced and to
        // get to 100% coverage for your balancedParenthesis
        // method.

        boolean notTrue = runner.balancedParenthesis("(");
        notTrue = runner.balancedParenthesis("(]");
    }

    /**
     * Test infixToPostfix().
     */
    @Test
    public void testInfixToPostfix() {
        QueueADT q = runner.infix2Postfix("10 + 20");
        // postfix should be 10 20 +
        assertEquals(3, q.size()); // number of elements in queue
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("+", q.dequeue());
        assertTrue(q.isEmpty());

        // repeat similar comparisons with other expressions such
        // as...
        // 10 + 20 * 5 --> 10 20 + 5 *
        // 10 + (20 * 5) --> 10 20 5 * +
        // etc
    }

    /**
     * Test evaluatePostfix().
     */
    @Test
    public void testEvaluatePostfix() {
        QueueADT<String> q = runner.infix2Postfix("10 + 20");
        assertEquals(30, runner.evaluatePostfix(q));

        q = runner.infix2Postfix("10 + 20 * 2");
        assertEquals(60, runner.evaluatePostfix(q));

        q = runner.infix2Postfix("10 + (20 * 2)");
        assertEquals(50, runner.evaluatePostfix(q));

        q = runner.infix2Postfix("10 - 2)");
        assertEquals(8, runner.evaluatePostfix(q));

        q = runner.infix2Postfix("10 / 2)");
        assertEquals(5, runner.evaluatePostfix(q));
    }

    /**
     * Tests getting the exception after dividing by zero in EvaluePostfix.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        QueueADT<String> q = runner.infix2Postfix("10 / 0");
        runner.evaluatePostfix(q);
    }

    /**
     * Test evaluateInfix().
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateInfix() {
        assertEquals(30, runner.evaluateInfix("10 + 20"));
        assertEquals(60, runner.evaluateInfix("10 + 20 * 2"));
        assertEquals(50, runner.evaluateInfix("10 + (20 * 2)"));

        runner.evaluateInfix("(10 - 2))");

    }

    /**
     * Tests the tokenize() method.
     */
    @Test
    public void testTokenize() {
        Project3 newRunner = new Project3();

        // whitespace checker
        String boutToTokenize = "( hello )";
        String[] tokenized = newRunner.tokenize(boutToTokenize);
        assertEquals("(", tokenized[0]);

        // empty checker
        String empty = "";
        String[] emptyToken = newRunner.tokenize(empty);
    }
}
