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
package net.bplaced.clayn.marmalade.ui.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import net.bplaced.clayn.marmalade.AppRuntime;
import net.bplaced.clayn.marmalade.core.Game;
import net.bplaced.clayn.marmalade.io.ImageHelper;
import net.bplaced.clayn.marmalade.jar.api.Library;
import net.bplaced.clayn.marmalade.tasks.Task;
import net.bplaced.clayn.marmalade.tasks.TaskManager;

/**
 * FXML Controller class
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class MainWindowController implements Initializable
{

    @FXML
    private Pane gamePane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        TaskManager.getTaskManager().schedule(this::loadGames, 0, 10,
                TimeUnit.SECONDS);
        TaskManager.getTaskManager().register(Task.REFRESH_GAMES, this::loadGames);
    }

    private synchronized void loadGames()
    {
        if (Platform.isFxApplicationThread())
        {
            gamePane.getChildren().clear();
            gamePane.getChildren().addAll(AppRuntime
                    .getRuntime().getApplication().getApplicationHandle()
                    .getLibraries().stream()
                    .map(Library::getGames).flatMap(Set::stream)
                    .sorted(Comparator.comparing(Game::getName))
                    .map(this::convertGameToNode).collect(Collectors.toList()));
        } else
        {
            Platform.runLater(this::loadGames);
        }

    }

    private Node convertGameToNode(Game g)
    {
        System.out.println("Loaded game: "+g);
        URI imgUri=g.getImage();
        Image img=null;
        ImageView view=null;
        if(imgUri!=null) {
           view=ImageHelper.createImageView(imgUri, 64, 64);
        }else {
            view=new ImageView();
            view.setFitWidth(64);
            view.setFitHeight(64);
            Icon icon=FileSystemView.getFileSystemView().getSystemIcon(g.getExecutable());
        BufferedImage bi = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_RGB);
        Graphics gr = bi.createGraphics();
        icon.paintIcon(null,gr,0,0);
        gr.dispose();
        Image swingImg = SwingFXUtils.toFXImage(bi,null);
        view.setImage(swingImg);
        }
        
        Tooltip tip = new Tooltip(g.getName());
        Button b=new Button();
        b.setGraphic(view);
        b.getStyleClass().add("gameButton");
        view.setPreserveRatio(true);
        Tooltip.install(b, tip);
        return b;
    }

}
