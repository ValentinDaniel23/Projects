package Lab5.task2;

import lab5.task2.Container;
import lab5.task1.Task;
import java.util.ArrayList;

public class Stack implements Container {
    private final ArrayList<Task> stack;

    public Stack() {
        stack = new ArrayList<>();
    }
    public Task pop() {
        if(!isEmpty()) {
          int lastIndex = stack.size() - 1;
          Task removedTask = stack.get(lastIndex);
          stack.remove(lastIndex);
          return removedTask;
        }
        else return null;
    }

    @Override
    public void push(Task task) {
        stack.add(task);
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public void transferFrom(Container container) {
        while (!container.isEmpty()) {
            Task task = container.pop();
            if (task != null) {
                push(task);
            }
        }
        container.getTasks().clear();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return stack;
    }
}
