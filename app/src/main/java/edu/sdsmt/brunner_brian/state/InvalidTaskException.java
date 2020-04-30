package edu.sdsmt.brunner_brian.state;

/**
 * Thrown when the task that was attempted is not valid in the current state.
 */
public class InvalidTaskException extends IllegalStateException {
    private final State state;
    private final String task;

    public InvalidTaskException(State state, String task) {
        super(String.format("Invalid transition from state %s by task %s", state, task));
        this.state = state;
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public State getState() {
        return state;
    }
}
