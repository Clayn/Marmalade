package net.bplaced.clayn.marmalade.util;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class TaskProgresserImpl implements TaskProgresser
{

    private final Object lock = new Object();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ObservableList<ProgressingTask> taskList = FXCollections.observableArrayList();

    private final Thread executorThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
            "TaskProgresser-Thread");

    @Override
    public ObservableList<ProgressingTask> getTaskList()
    {
        return taskList;
    }

    @Override
    public void start()
    {
        running.set(true);
        synchronized (lock)
        {
            lock.notifyAll();
        }
    }

    @Override
    public void stop()
    {
        running.set(false);
    }
}
