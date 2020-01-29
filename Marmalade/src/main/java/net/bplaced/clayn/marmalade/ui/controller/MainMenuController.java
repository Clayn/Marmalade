package net.bplaced.clayn.marmalade.ui.controller;

/*
 * The MIT License
 *
 * Copyright 2020 Clayn <clayn_osmato@gmx.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.bplaced.clayn.marmalade.core.Game;
import net.bplaced.clayn.marmalade.tasks.Task;
import net.bplaced.clayn.marmalade.tasks.TaskManager;
import net.bplaced.clayn.marmalade.ui.dialog.GameInputDialogController;
import net.bplaced.clayn.marmalade.ui.dialog.MarmaladeDialog;

/**
 * FXML Controller class
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class MainMenuController implements Initializable
{

    private ResourceBundle resources;
    @FXML
    private MenuBar menu;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        resources = rb;
    }

    @FXML
    private void onNewGame() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/GameInputDialog.fxml"));
        loader.setResources(resources);
        Parent p = loader.load();
        GameInputDialogController controller = loader.getController();
        controller.setGame(new Game());
        controller.setEditMode(false);
        Scene sc = new Scene(p);
        sc.getStylesheets().addAll("/styles/styles.css");
        Stage st = new Stage();
        st.setScene(sc);
        st.initModality(Modality.WINDOW_MODAL);
        st.initOwner(menu.getScene().getWindow());
        st.showAndWait();
        TaskManager.getTaskManager().trigger(Task.REFRESH_GAMES);
    }

    @FXML
    private void onExit()
    {
        MarmaladeDialog.exitDialog()
                .owner(menu.getScene().getWindow()
                ).show();
    }
}
