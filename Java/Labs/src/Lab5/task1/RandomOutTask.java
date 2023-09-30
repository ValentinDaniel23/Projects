package Lab5.task1;
import java.util.Random;
import lab5.task1.Task;
public class RandomOutTask implements Task {
    private Random random = new Random(12345);

    @Override
    public void execute() {
        int randomNumber = random.nextInt();
        System.out.println("Random number: " + randomNumber);
    }
}