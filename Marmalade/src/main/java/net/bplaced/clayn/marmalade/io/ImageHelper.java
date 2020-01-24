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
package net.bplaced.clayn.marmalade.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class ImageHelper
{

    public static Image loadImage(URI uri)
    {
        try (InputStream in = uri.toURL().openStream())
        {
            return new Image(in);
        } catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static ImageView createImageView(URI uri, double width, double heigth)
    {
        Image img = loadImage(uri);

        return createImageView(img, width, heigth);
    }

    public static ImageView createImageView(Image img, double width,
            double heigth)
    {
        ImageView back = new ImageView(img);
        if (width > 0 && heigth > 0)
        {
            back.setFitHeight(heigth);
            back.setFitWidth(width);
        }
        return back;
    }

    public static ImageView createImageView(Images img, double width,
            double heigth)
    {
        return createImageView(img.loadImage(), width, heigth);
    }

    public static ImageView createImageView(Images img)
    {
        return createImageView(img, -1, -1);
    }

    public static ImageView createImageView(Images img, double size)
    {
        return createImageView(img, size, size);
    }
}
