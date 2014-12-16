package com.ndroidme;

import java.util.concurrent.TimeUnit;

/**
 * Created by Michael on 11/28/2014.
 */
public final class  TaskExecutor {
    private static int CORE_SIZE=4;




    private static TaskExecutorPool executor= new TaskExecutorPool(CORE_SIZE);
    private TaskExecutor()
    {

    }
    public static  void executeTask(Runnable task)
    {
        executor.execute(task);
    }
    public static void repeatTask(Runnable task,long whenToRepeat){
      executor.scheduleAtFixedRate(task,0,whenToRepeat, TimeUnit.MILLISECONDS);

    }
}