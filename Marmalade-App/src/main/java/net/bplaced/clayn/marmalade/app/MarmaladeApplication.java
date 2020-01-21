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
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public final class MarmaladeApplication
{

    private static final Logger LOG = LoggerFactory.getLogger(
            MarmaladeApplication.class);

    private BiConsumer<String, Object[]> output = LOG::debug;
    private File configurationFile;
    private File jamDirectory;
    private String currentTask = "";
    private int currentTaskCount = 0;
    private final int maxTaskCount = 2;
    private StartUpListener listener;

    private void changeTask(String newTask)
    {

        if (currentTask != null && !currentTask.isBlank())
        {
            currentTaskCount++;
        }
        double progress = (currentTaskCount * 1.0) / (maxTaskCount * 1.0);
        if (listener != null)
        {
            listener.onCurrentTaskChanged(currentTask == null ? "" : currentTask,
                    newTask == null ? "" : newTask, currentTaskCount,
                    maxTaskCount, progress);
        }
        if (newTask != null && !newTask.isBlank())
        {
            currentTask = newTask;
        }

    }

    public void setConfigurationFile(File configurationFile)
    {
        this.configurationFile = configurationFile;
    }

    public MarmaladeApplication configFile(File configurationFile)
    {
        setConfigurationFile(configurationFile);
        return this;
    }

    public void setJamDirectory(File jamDirectory)
    {
        this.jamDirectory = jamDirectory;
    }

    public MarmaladeApplication jamDirectory(File jamDirectory)
    {
        setJamDirectory(jamDirectory);
        return this;
    }

    public MarmaladeApplication()
    {
        print("Preparing a new Marmalade Application");
    }

    private void print(String mes, Object... args)
    {
        output.accept(mes, args);
    }

    public static MarmaladeApplication prepare()
    {
        return new MarmaladeApplication();
    }

    public void setOutput(BiConsumer<String, Object[]> output)
    {
        this.output = output == null ? LOG::debug : output;
    }

    public void setListener(StartUpListener listener)
    {
        this.listener = listener;
    }

    public MarmaladeApplication listener(StartUpListener listener) {
        setListener(listener);
        return this;
    }
    public MarmaladeApplication output(BiConsumer<String, Object[]> output)
    {
        setOutput(output);
        return this;
    }

    public void start() throws Exception
    {
        FSHandler handler = new FSHandler();
        changeTask("Creating configuration file");
        if (configurationFile != null)
        {
            print("Creating configuration file: {}",
                    configurationFile.getAbsolutePath());
            handler.prepareFile(configurationFile.getAbsolutePath(), false);
        }
        Thread.sleep(1000);
        changeTask("Creating jam directory");
        if (jamDirectory != null)
        {
            print("Creating jam directory: {}",
                    jamDirectory.getAbsolutePath());
            handler.prepareDirectory(jamDirectory.getAbsolutePath(), false);
        }
        Thread.sleep(1000);
        changeTask(null);
    }
}
