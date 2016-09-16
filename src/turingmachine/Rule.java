package turingmachine;

/**
 *
 * @author Andrew
 */
public class Rule {

    
    char change;
    State state;
    char move;
    
    //for printout only
    char name;
    
    public Rule(State toState, char changeChar, char moveChar, char printName) {
        state = toState;
        change = changeChar;
        move = moveChar;
        name = printName;
    }

    public State getState() {
        return state;
    }

    public char getChange() {
        return change;
    }

    public char getMove() {
        return move;
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("on '" + name + "' changed to '" + change + "' and moved '" + move + "' on tape. Now in state " + state.getName());
        return sb.toString();
    }
}
