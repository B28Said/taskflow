package kg.megalab.taskflow.entity;

public enum TaskStatus {

    NEW, IN_PROGRESS, DONE, CANCELLED;

    public boolean isFinal() {
        return this == DONE || this == CANCELLED;
    }

    public boolean canTransitionTo(TaskStatus next) {
        if (this == next) {
            return true;
        }

        return switch (this) {
            case NEW -> next == IN_PROGRESS || next == CANCELLED;
            case IN_PROGRESS -> next == DONE || next == CANCELLED;
            case DONE, CANCELLED -> false;
        };
    }
}
