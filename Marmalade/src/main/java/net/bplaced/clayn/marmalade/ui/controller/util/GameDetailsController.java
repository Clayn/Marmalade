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
package net.bplaced.clayn.marmalade.ui.controller.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import net.bplaced.clayn.marmalade.core.Game;
import net.bplaced.clayn.marmalade.core.health.HealthCenter;
import net.bplaced.clayn.marmalade.io.ImageHelper;
import net.bplaced.clayn.marmalade.io.Images;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class GameDetailsController extends GameInputDialogContentController
{

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField executableField;
    @FXML
    private TextField imageField;
    @FXML
    private Button imageButton;
    @FXML
    private Button executableButton;
    @FXML
    private ImageView image;

    private boolean noImage = true;
    private boolean execChange = false;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ImageView imageImg=ImageHelper.createImageView(Images.DIRECTORY, 24);
        ImageView exec=ImageHelper.createImageView(Images.DIRECTORY, 24);
        imageButton.setText("");
        executableButton.setText("");
        imageButton.getStyleClass().add("gameButton");
        executableButton.getStyleClass().add("gameButton");
        imageButton.setGraphic(imageImg);
        executableButton.setGraphic(exec);
        image.imageProperty().bind(Bindings.createObjectBinding(
                new Callable<Image>()
        {
            @Override
            public Image call() throws Exception
            {
                Image img = null;
                noImage = true;
                try
                {

                    if (!execChange)
                    {
                        URI uri = new URI(imageField.getText());

                        img = ImageHelper.loadImage(uri);
                        noImage = false;
                    } else
                    {
                        if (currentGame.get().getExecutable() != null && currentGame.get().getExecutable().exists())
                        {
                            Icon icon = FileSystemView.getFileSystemView().getSystemIcon(
                                    currentGame.get().getExecutable());
                            BufferedImage bi = new BufferedImage(
                                    icon.getIconWidth(),
                                    icon.getIconHeight(),
                                    BufferedImage.TYPE_INT_RGB);
                            Graphics gr = bi.createGraphics();
                            icon.paintIcon(null, gr, 0, 0);
                            gr.dispose();
                            Image swingImg = SwingFXUtils.toFXImage(bi, null);
                            noImage = true;
                            return swingImg;
                        }
                    }
                } catch (Exception ex)
                {

                    img = Images.NO_GAME_ICON.loadImage();
                    noImage = true;
                }
                return img;
            }

        }, imageField.textProperty()));
        installListener(nameField.textProperty(), String::toString,
                Game::setName);
        installListener(descriptionArea.textProperty(), String::toString,
                Game::setDescription);
        installListener(executableField.textProperty(), File::new,
                Game::setExecutable);
        installListener(imageField.textProperty(), new Function<String, URI>()
        {
            @Override
            public URI apply(String t)
            {
                try
                {
                    return new URI(t);
                } catch (URISyntaxException ex)
                {
                    HealthCenter.getErrorCenter().report(ex);
                    return null;
                }
            }
        }, Game::setImage);
    }

    private <T> void installListener(StringProperty src,
            Function<String, T> mapper, BiConsumer<Game, T> setter)
    {
        src.addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends String> ov, String t, String t1)
            {
                setter.accept(currentGame.get(), mapper.apply(t1));
            }
        });
    }

    public final StringProperty nameProperty()
    {
        return nameField.textProperty();
    }

    @FXML
    private void chooseImage()
    {
        File dir = null;
        try
        {
            URI uri = new URI(imageField.getText());
            File f = new File(uri);
            if (f.exists() && f.isFile())
            {
                dir = f.getParentFile();
            }
        } catch (URISyntaxException | IllegalArgumentException ex)
        {
            dir = new File(System.getProperty("user.home"));
        }
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(dir);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image files", "*.png", "*.jpg", "jpeg"));
        File f = chooser.showOpenDialog(nameField.getScene().getWindow());
        if (f != null && f.isFile() && f.exists())
        {
            execChange = true;
            imageField.setText(f.toURI().toString());
        }
    }

    @FXML
    private void chooseExec()
    {
        File f = new File(executableField.getText());
        File dir = new File(System.getProperty("user.home"));
        if (f.exists() && f.isFile())
        {
            dir = f.getParentFile();
        }
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(dir);
        File newFile = chooser.showOpenDialog(
                executableField.getScene().getWindow());
        if (newFile != null && newFile.isFile() && newFile.exists())
        {
            executableField.setText(newFile.getAbsolutePath());
            if (noImage)
            {
                execChange=true;
                imageField.setText(newFile.toURI().toString());
                execChange=false;
            }
        }
    }

}
