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
package net.bplaced.clayn.marmalade.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class FSHandler
{

    public void prepareFile(String path, boolean removeOld) throws IOException
    {
        Path p = Paths.get(path);
        if (Files.exists(p))
        {
            if (removeOld)
            {
                Files.delete(p);
                prepareFile(path, removeOld);
            }
        } else
        {
            Files.createDirectories(p.getParent());
            Files.createFile(p);
        }
    }

    public void prepareDirectory(String path, boolean removeOld) throws IOException
    {
        Path p = Paths.get(path);
        if (Files.exists(p))
        {
            if (removeOld)
            {
                deleteDirectory(p.toFile());
                prepareDirectory(path, removeOld);
            }
        } else
        {
            Files.createDirectories(p);
        }
    }

    private void deleteDirectory(File dir) throws IOException
    {
        if (!dir.exists() || dir.isFile())
        {
            return;
        }
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (File f : files)
            {
                if (f.isFile())
                {
                    Files.delete(f.toPath());
                } else
                {
                    deleteDirectory(f);
                }
            }
        }
        if (!dir.delete())
        {
            throw new IOException(
                    "Failed to delete directory: " + dir.getAbsolutePath());
        }
    }
}
