package Lab5.task1;
import lab5.task1.Task;
public class CounterOutTask implements Task {
    private static int counter = 0;

    @Override
    public void execute() {
        counter++;
        System.out.println("Counter value: " + counter);
    }
}
