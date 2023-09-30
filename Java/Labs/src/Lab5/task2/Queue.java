package Lab5.task2;

import lab5.task2.Container;
import lab5.task1.Task;
import java.util.ArrayList;

public class Queue implements Container {
    private final ArrayList<Task> queue;

    public Queue() {
        queue = new ArrayList<>();
    }

    @Override
    public Task pop() {
        if(!isEmpty()) {
          int firstIndex = 0;
          Task removedTask = queue.get(firstIndex);
          queue.remove(removedTask);
          return removedTask;
        }
        else return null;
    }

    @Override
    public void push(Task task) {
        queue.add(task);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void transferFrom(Container container) {
        while(!container.isEmpty()) {
            Task task = container.pop();
            if (task != null) {
                push(task);
            }
        }
        container.getTasks().clear();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return queue;
    }
}
