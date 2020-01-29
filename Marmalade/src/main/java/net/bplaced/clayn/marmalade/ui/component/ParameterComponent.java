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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import net.bplaced.clayn.marmalade.core.script.ModuleParameter;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class ParameterComponent extends Control
{

    private final ObjectProperty<ModuleParameter> parameter = new SimpleObjectProperty<>();
    private final ReadOnlyStringWrapper value = new ReadOnlyStringWrapper("");

    public ParameterComponent()
    {
        this(null);
    }
    
    ReadOnlyStringWrapper writeableValueProperty() {
        return value;
    }

    public ParameterComponent(ModuleParameter parameter)
    {
        this.parameter.set(parameter);
    }

    public ObjectProperty<ModuleParameter> parameterProperty()
    {
        return parameter;
    }

    public ModuleParameter getParameter()
    {
        return parameterProperty().get();
    }

    public void setParameter(ModuleParameter parameter)
    {
        parameterProperty().set(parameter);
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
        return new ParameterComponentSkin(this);
    }

    public ReadOnlyStringProperty valueProperty()
    {
        return value.getReadOnlyProperty();
    }

    public String getValue()
    {
        return valueProperty().get();
    }

}
