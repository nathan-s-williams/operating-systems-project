package edu.utdallas.taskExecutorImpl;

import edu.utdallas.taskExecutor.BlockingFifo;
import edu.utdallas.taskExecutor.Task;

public class TaskRunner implements Runnable
{
BlockingFifo Fifo;
boolean stopGo = true;

public TaskRunner(BlockingFifo Fifo) 
{
	this.Fifo = Fifo;
}

public void run()
{
	while(stopGo)
	{
		
		
		try
		{
			Task doer = Fifo.take();
			doer.execute();
		}
		catch(Throwable th)
		{
			System.out.println(th + ": ERROR");
		}
	}
}

}
