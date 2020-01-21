package net.bplaced.clayn.marmalade.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.bplaced.clayn.marmalade.app.StartUpListener;
import net.bplaced.clayn.marmalade.conf.AppRuntime;
import net.bplaced.clayn.marmalade.conf.Configurator;
import net.bplaced.clayn.marmalade.io.StaticPaths;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class InitWindowController implements Initializable, StartUpListener
{

    @FXML
    private Parent root;

    @FXML
    private Label taskLabel;

    @FXML
    private ProgressBar progress;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    private void initDone()
    {
        if (root == null)
        {
            return;
        }
        root.getScene().getWindow().hide();
    }

    public void init() throws Exception
    {
        new Thread(() ->
        {
            try
            {
                AppRuntime
                        .getRuntime().getApplication()
                        .configFile(StaticPaths.getConfigurationFile().toFile())
                        .jamDirectory(StaticPaths.getJamDirectory().toFile())
                        .listener(this)
                        .start();
                Configurator.initDefaultConfiguration();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onCurrentTaskChanged(String oldTask, String newTask,
            int currentTask, int allTasks, double progress)
    {
        if (!Platform.isFxApplicationThread())
        {
            Platform.runLater(() -> onCurrentTaskChanged(oldTask, newTask,
                    currentTask, allTasks, progress));
            return;
        }
        taskLabel.setText(newTask);
        this.progress.setProgress(progress);
        if (currentTask >= allTasks)
        {
            initDone();
        }
    }
}
