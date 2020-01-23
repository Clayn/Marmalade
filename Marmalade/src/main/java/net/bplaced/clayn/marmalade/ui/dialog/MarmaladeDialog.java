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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;
import net.bplaced.clayn.marmalade.conf.AppRuntime;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class MarmaladeDialog
{

    public static MarmaladeDialog exitDialog()
    {
        return new MarmaladeDialog("exit", null, true,
                Alert.AlertType.CONFIRMATION)
                .clearButtons()
                .addButton(ButtonType.YES, AppRuntime.getRuntime()::close)
                .addButton(ButtonType.NO);
    }

    private final String resourceName;
    private final Node grapic;
    private final Alert.AlertType type;
    private final Alert alert;
    private Runnable nullAction;

    private final Map<ButtonType, Runnable> buttonTypeActions = new HashMap<>();

    private void setText(String key, Consumer<String> setter, ResourceBundle res)
    {
        setter.accept(res.containsKey(key) ? res.getString(key) : null);
    }

    private MarmaladeDialog(String resourceName, Node grapic,
            boolean useDefaultGraphic,
            Alert.AlertType type)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(
                "i18n.dialog." + resourceName);

        this.resourceName = resourceName;
        this.grapic = grapic;
        this.type = type;
        this.alert = new Alert(type);
        this.alert.initModality(Modality.APPLICATION_MODAL);
        if (!useDefaultGraphic)
        {
            alert.setGraphic(grapic);
        }
        setText("title", alert::setTitle, bundle);
        setText("header", alert::setHeaderText, bundle);
        setText("message", alert::setContentText, bundle);
    }

    public MarmaladeDialog owner(Window w)
    {
        alert.initOwner(w);
        return this;
    }

    public MarmaladeDialog clearButtons()
    {
        alert.getButtonTypes().clear();
        buttonTypeActions.clear();
        nullAction = null;
        return this;
    }

    public MarmaladeDialog removeButton(ButtonType bt)
    {
        if (bt == null)
        {
            nullAction = null;
        } else
        {
            alert.getButtonTypes().remove(bt);
            buttonTypeActions.remove(bt);
        }
        return this;
    }

    public MarmaladeDialog addButton(ButtonType bt, Runnable action)
    {
        if (bt != null)
        {
            if (!alert.getButtonTypes().contains(bt))
            {
                alert.getButtonTypes().add(bt);
            } else if (buttonTypeActions.containsKey(bt))
            {
                buttonTypeActions.remove(bt);
            }
            if (action != null)
            {
                buttonTypeActions.put(bt, action);
            }
        } else if (action != null)
        {
            nullAction = action;
        }

        return this;
    }

    public MarmaladeDialog addButton(ButtonType bt)
    {
        return addButton(bt, null);
    }

    /**
     * Shows the dialog and waiting for a user input. If an action was
     * registered with the button type, this action will be called.
     *
     * @return the button type selected by the user
     */
    public ButtonType show()
    {
        Optional<ButtonType> input = alert.showAndWait();
        ButtonType back = input.orElse(null);
        if (back == null && nullAction != null)
        {
            nullAction.run();
        } else if (buttonTypeActions.containsKey(back))
        {
            buttonTypeActions.get(back).run();
        }
        return back;
    }
}
