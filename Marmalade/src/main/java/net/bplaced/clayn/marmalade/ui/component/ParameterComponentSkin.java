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
package net.bplaced.clayn.marmalade.ui.component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import net.bplaced.clayn.marmalade.core.script.ModuleParameter;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
class ParameterComponentSkin extends SkinBase<ParameterComponent>
{

    private static final Map<ModuleParameter.ParameterType, Consumer<ParameterComponentSkin>> SKIN_METHODS = new HashMap<>();

    static
    {
        SKIN_METHODS.put(ModuleParameter.ParameterType.STRING,
                ParameterComponentSkin::buildStringInput);
    }

    private final ObjectProperty<Node> configNode = new SimpleObjectProperty<>();

    public ParameterComponentSkin(ParameterComponent c)
    {
        super(c);
        ParameterComponentSkin skin=this;
        c.parameterProperty().addListener(new ChangeListener<ModuleParameter>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends ModuleParameter> ov,
                    ModuleParameter t, ModuleParameter t1)
            {
                if (t1 != null && SKIN_METHODS.containsKey(
                        t1.getType()))
                {
                    SKIN_METHODS.get(t1.getType()).accept(skin);
                }
            }
        });

        if (c.getParameter() != null && SKIN_METHODS.containsKey(
                c.getParameter().getType()))
        {
            SKIN_METHODS.get(c.getParameter().getType()).accept(skin);
        }
    }

    private Node createStringNode() {
        TextField field=new TextField();
        field.textProperty().bindBidirectional(getSkinnable().writeableValueProperty());
        return field;
    }
    
    private void buildStringInput()
    {
        VBox box=new VBox(5.0);
        Label l=new Label();
        l.textProperty().bind(Bindings.createStringBinding(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                return getSkinnable().getParameter()==null?"":getSkinnable().getParameter().getName();
            }
        }, getSkinnable().parameterProperty()));
        box.getChildren().addAll(l,createStringNode());
        configNode.set(box);
    }
}
