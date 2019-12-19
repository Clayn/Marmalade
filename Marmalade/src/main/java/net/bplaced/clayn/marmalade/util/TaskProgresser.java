package net.bplaced.clayn.marmalade.util;

import javafx.collections.ObservableList;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public interface TaskProgresser
{

    public ObservableList<ProgressingTask> getTaskList();

    public void start();

    public void stop();
}
