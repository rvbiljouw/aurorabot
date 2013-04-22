package ms.aurora.api.script.task;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public abstract class Combinator implements Task {
    private final List<Task> tasks = new ArrayList<Task>();

    public Combinator(Task... tasks) {
        this.tasks.addAll(newArrayList(tasks));
    }

    public abstract boolean canRun();

    @Override
    public void run() {
        if (canRun()) {
            for (Task task : tasks) {
                task.run();
            }
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
