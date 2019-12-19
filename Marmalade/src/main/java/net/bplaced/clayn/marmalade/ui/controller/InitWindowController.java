package net.bplaced.clayn.marmalade.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class InitWindowController implements Initializable
{

    @FXML
    private Parent root;
   
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                    Platform.runLater(InitWindowController.this::initDone);
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void initDone()
    {
        if (root == null)
        {
            return;
        }
        root.getScene().getWindow().hide();
    }
}
