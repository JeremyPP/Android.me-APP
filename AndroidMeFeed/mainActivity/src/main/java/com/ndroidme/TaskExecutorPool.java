package com.ndroidme;

import android.util.Log;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Michael on 11/30/2014.
 */
public class TaskExecutorPool extends ScheduledThreadPoolExecutor {
    public TaskExecutorPool(int corePoolSize) {
        super(corePoolSize);
    }

    @Override
    protected void afterExecute(Runnable task, Throwable t) {
        super.afterExecute(task, t);
        if (t == null && task instanceof Future<?>) {
            try {
                Object result = ((Future<?>) task).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }
        if (t != null) {
            Log.d("TaskExecutorPool", t.toString());
        }

    }
}
