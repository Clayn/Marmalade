package net.bplaced.clayn.marmalade.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.bplaced.clayn.marmalade.AppRuntime;
import net.bplaced.clayn.marmalade.app.StartUpListener;
import net.bplaced.clayn.marmalade.conf.Configurator;
import net.bplaced.clayn.marmalade.core.health.HealthCenter;
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
    @FXML
    private Text title;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    private void initDone()
    {
        try
        {
            if (root == null)
            {
                return;
            }
            ResourceBundle bundle=ResourceBundle.getBundle("i18n.lang");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/MainWindow.fxml"));
            loader.setResources(bundle);
            Parent rootPane = loader.load();
            Stage st=new Stage();
            Scene scene = new Scene(rootPane);
            st.setTitle("Marmalade");
            st.setScene(scene);
            root.getScene().getWindow().hide();
            st.show();
        } catch (IOException ex)
        {
            Logger.getLogger(InitWindowController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
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
                        .strawberryDirectory(StaticPaths.getStrawberyDirectory().toFile())
                        .listener(this)
                        .start();
                Configurator.initDefaultConfiguration();
            } catch (Exception ex)
            {
                HealthCenter.getErrorCenter().report(ex);
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
