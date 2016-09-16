package turingmachine;

import java.util.HashMap;

/**
 *
 * @author Andrew
 */
public class State {

    HashMap<Character, Rule> transitions;
    String name;

    public State() {
        transitions = new HashMap();
    }

    public void addRule(char character, Rule rule) {
        transitions.put(character, rule);
    }

    public Rule getRule(char character) {
        return transitions.get(character);
    }


    //these are for output
    public void setName(String pName) {
        name = pName;
    }

    public String getName() {
        return name;
    }

}
