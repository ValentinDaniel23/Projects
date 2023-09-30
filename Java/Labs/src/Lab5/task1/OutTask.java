package Lab5.task1;
import lab5.task1.Task;

public class OutTask implements Task {
    private String message;

    public OutTask(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
}
