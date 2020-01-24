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
package net.bplaced.clayn.marmalade.ui.dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import net.bplaced.clayn.marmalade.AppRuntime;
import net.bplaced.clayn.marmalade.core.Game;
import net.bplaced.clayn.marmalade.core.health.HealthCenter;
import net.bplaced.clayn.marmalade.jar.api.Library;
import net.bplaced.clayn.marmalade.jar.err.GameAlreadyExistsException;
import net.bplaced.clayn.marmalade.ui.controller.util.GameDetailsController;
import net.bplaced.clayn.marmalade.ui.controller.util.GameInputDialogContentController;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class GameInputDialogController implements Initializable
{

    class GameInputInformationComponent
    {

        private GameInputDialogContentController controller;
        private Node content;
        private final StringProperty text = new SimpleStringProperty("");
        private final ObjectProperty<Node> graphic = new SimpleObjectProperty<>();
    }
    @FXML
    private BorderPane content;
    @FXML
    private TreeView<GameInputInformationComponent> tree;

    private ResourceBundle resources;
    private final TreeItem<GameInputInformationComponent> root = new TreeItem<>();
    private final ObjectProperty<Game> currentGame = new SimpleObjectProperty<>();

    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private GameDetailsController detailsController;
    private Node detailsContent;

    private GameInputInformationComponent getDetailsComponent()
    {
        GameInputInformationComponent comp = new GameInputInformationComponent();
        comp.content = detailsContent;
        comp.controller = detailsController;
        comp.text.bind(Bindings.createStringBinding(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                String name=detailsController.nameProperty().get();
                return name==null||name.isBlank()?"No Name":name;
            }
        }, detailsController.nameProperty()));
        detailsController.currentGameProperty().bind(currentGame);
        return comp;
    }

    public void setGame(Game g)
    {
        currentGame.set(g);
    }

    public void setEditMode(boolean edit)
    {
        editMode.set(edit);
    }

    private TreeItem<GameInputInformationComponent> createTreeItem(
            GameInputInformationComponent comp)
    {
        TreeItem<GameInputInformationComponent> item = new TreeItem<>(comp);
        return item;
    }

    private void prepareDetails()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/component/GameDetails.fxml"));
            loader.setResources(resources);
            Parent node = loader.load();
            detailsContent = node;
            detailsController = loader.getController();
           
        } catch (IOException ex)
        {
            HealthCenter.getErrorCenter().report(ex, RuntimeException::new);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        resources = rb;
        prepareDetails();
        currentGame.addListener(new ChangeListener<Game>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Game> ov, Game t, Game t1)
            {
                root.getChildren().clear();
                if (t1 != null)
                {
                    root.getChildren().add(createTreeItem(getDetailsComponent()));
                }
            }
        });
        if (currentGame != null)
        {
            root.getChildren().clear();
            root.getChildren().add(createTreeItem(getDetailsComponent()));

        }
        tree.setCellFactory((TreeView<GameInputInformationComponent> p) ->
        {
            TreeCell<GameInputInformationComponent> cell = new TreeCell<GameInputInformationComponent>()
            {
                @Override
                protected void updateItem(GameInputInformationComponent t,
                        boolean bln)
                {
                    graphicProperty().unbind();
                    textProperty().unbind();
                    super.updateItem(t, bln);
                    if (t != null && !bln)
                    {
                        graphicProperty().bind(t.graphic);
                        textProperty().bind(t.text);
                    }
                }
            };
            return cell;
        });
        tree.setRoot(root);
        tree.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<TreeItem<GameInputDialogController.GameInputInformationComponent>>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends TreeItem<GameInputInformationComponent>> ov,
                    TreeItem<GameInputInformationComponent> t,
                    TreeItem<GameInputInformationComponent> t1)
            {
                if (t1 == null)
                {
                    content.setCenter(null);
                } else
                {
                    content.setCenter(t1.getValue().content);
                    content.getScene().getWindow().sizeToScene();
                }
            }
        });
    }
    
    @FXML
    private void onCancel() {
        tree.getScene().getWindow().hide();
    }
    
    @FXML
    private void onSave() {
        Library lib=AppRuntime.getRuntime().getApplication().getApplicationHandle().getLibraries().get(
                0);
        Game g=currentGame.get();
        try{
            lib.createGame(g);
            onCancel();
        }catch(GameAlreadyExistsException ex) {
            
        }
    }

}
