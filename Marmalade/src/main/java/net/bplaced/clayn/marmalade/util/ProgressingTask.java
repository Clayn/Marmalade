package net.bplaced.clayn.marmalade.util;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public abstract class ProgressingTask implements Runnable
{

    protected final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper(
            -1.0);
    protected final StringProperty taskName = new SimpleStringProperty("");

    public final ReadOnlyDoubleProperty progressProperty()
    {
        return progress.getReadOnlyProperty();
    }

    public final double getProgress()
    {
        return progressProperty().get();
    }

    public final StringProperty taskNameProperty()
    {
        return taskName;
    }

    public final String getTaskName()
    {
        return taskNameProperty().get();
    }

    public final void setTaskName(String name)
    {
        taskNameProperty().set(name == null ? "" : name);
    }
}
