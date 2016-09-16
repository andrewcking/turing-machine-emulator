package turingmachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class TuringMachine {

    //Filename
    private String FILENAME = "tinput.txt";
    private HashMap<String, State> states;
    private State currentState;
    private char inputs[];

    private int tapeLocation = 0;
    //tells us when to halt
    private boolean halt;

    public static void main(String[] args) throws ParseException {
        new TuringMachine();
    }

    public TuringMachine() throws ParseException {
        states = new HashMap();
        readTextFile();
        test();
    }

    private void test() throws ParseException {
        printOut();
        while (halt == false) {
            //get rule
            if (currentState.getRule(inputs[tapeLocation]) != null) {
                Rule testRule = currentState.getRule(inputs[tapeLocation]);
                //change the input tape
                inputs[tapeLocation] = testRule.getChange();
                //move to new state
                currentState = testRule.getState();

                //move tape
                if (testRule.getMove() == 'R') {
                    tapeLocation++;
                } else if (testRule.getMove() == 'L') {
                    tapeLocation--;
                } else if (testRule.getMove() == 'S') {
                    //do nothing
                } else {
                    throw new ParseException("Invalid tape move detected");
                }
                System.out.println("Rule: " + testRule);
                printOut();
                checkState();
            } else {
                System.out.println("No Rule Found, assumed to Halt Rejecting");
                System.out.println("Halt Rejected");
                break;
            }
        }
    }

    private void checkState() {
        if (currentState == states.get("0")) {
            System.out.println("Halt Accepted");
            halt = true;
        } else if (currentState == states.get("1")) {
            System.out.println("Halt Rejected");
            halt = true;
        }
    }

    public void printOut() {
        System.out.println("The current state: " + currentState.getName());
        System.out.println(inputs);
        for (int i = 0; i < tapeLocation; i++) {
            System.out.print(" ");
        }
        System.out.println('^');
    }

    private void createState(String name) {
        //create states
        states.put(name, new State());
        //this is only for output in console
        states.get(name).setName(name);
    }

    private void readTextFile() throws ParseException {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            String theLine = br.readLine();
            while (theLine != null) {
                //get rid of white space
                theLine = theLine.replaceAll("\\s+", "");
                //readline will increment for us
                lines.add(theLine);
                theLine = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(TuringMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < lines.size(); i++) {
            //CREATE STATES AND RULES
            if (lines.get(i).startsWith("Rule") == true) {
                String[] rule = lines.get(i).split(",|:");

                //rule[0]=Rule
                //rule[1]=start state
                //rule[2]=on symbol
                //rule[3]=go to state
                //rule[4]=change to symbol
                //rule[5]=move direction
                //create states
                if (states.get(rule[1]) == null) {
                    createState(rule[1]);
                }
                //create tostates
                if (states.get(rule[3]) == null) {
                    createState(rule[3]);
                }

                //error catch
                if (rule[2] == null || rule[4] == null || rule[5] == null) {
                    throw new ParseException("Problem with tape character or move direction char on line " + (i + 1));
                }
                
                //create the new rule  
                //                     (toState,             change char,       move direction,    name used for printout)
                Rule newRule = new Rule(states.get(rule[3]), rule[4].charAt(0), rule[5].charAt(0), rule[2].charAt(0));
                //add it to the state
                states.get(rule[1]).addRule(rule[2].charAt(0), newRule);
                //SET START STATE
            } else if (lines.get(i).startsWith("Start") == true) {
                String[] startState = lines.get(i).split(":");
                //if in some weird case there are no rules, create start state
                if (states.get(startState[1]) == null) {
                    createState(startState[1]);
                }
                currentState = states.get(startState[1]);
                //CREATE INPUT ARRAY
            } else if (lines.get(i).startsWith("Input") == true) {
                String[] input = lines.get(i).split(":");
                inputs = input[1].toCharArray();
                //ERROR CATCH
            } else if (lines.get(i).equals("") == true) {
                //added support for blank lines
            } else {
                //if we have something other than Rule,Start or Input
                throw new ParseException("Invalid format in turing specification on line " + (i + 1));
            }

        }
    }

}
