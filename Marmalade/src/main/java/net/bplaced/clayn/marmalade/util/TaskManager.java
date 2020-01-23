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
package net.bplaced.clayn.marmalade.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * The Taskmanager can be used to execute simple tasks in threads outside the
 * main thread. Tasks can also be scheduled to be called periodically. When
 * shutting down, the shutdown method should be called but all threads used for
 * execution will be deamon threads and won't prevent the application to
 * shutdown
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class TaskManager
{

    private static final TaskManager INSTANCE = new TaskManager();

    private final ExecutorService execService = Executors.newCachedThreadPool(
            new ThreadFactory()
    {
        private int count = 0;

        @Override
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(r,
                    String.format("Marmalade-Service-Thread-%d", (++count)));
            t.setDaemon(true);
            return t;
        }
    });

    private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(
            2, new ThreadFactory()
    {
        private int count = 0;

        @Override
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(r,
                    String.format("Marmalade-Scheduled-Service-Thread-%d",
                            (++count)));
            t.setDaemon(true);
            return t;
        }
    });

    public static TaskManager getTaskManager()
    {
        return INSTANCE;
    }

    public void execute(Runnable run)
    {
        execService.submit(run);
    }

    /**
     * Schedules the given runnable to be run after an initial delay and than
     * every time the perod has expired.
     *
     * @param run
     * @param delay
     * @param period
     * @param unit
     */
    public void schedule(Runnable run, long delay, long period, TimeUnit unit)
    {
        scheduledService.scheduleAtFixedRate(run, delay, period, unit);
    }

    public void shutdown()
    {
        execService.shutdownNow();
        scheduledService.shutdownNow();
    }
}
