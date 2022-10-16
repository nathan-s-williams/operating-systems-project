package edu.utdallas.taskExecutorImpl;

import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;
import edu.utdallas.taskExecutor.BlockingFifo;

public class TaskExecutorImpl implements TaskExecutor
{
	Thread pool[];
	BlockingFifo Fifo;

	public TaskExecutorImpl(int threadPoolSize)
	{
		// TODO Complete the implementation
		BlockingFifo Temp = new BlockingFifo((2*threadPoolSize));
		Fifo = Temp;
		
		creator(threadPoolSize);
		
	}
	
	@Override
	public void addTask(Task task)
	{
		// TODO Complete the implementation
		try 
		{
			Fifo.put(task);
		} 
		catch (Throwable e) 
		{
			// TODO Auto-generated catch block
			System.out.println(e + ": ERROR");
		}
	}
	
	public void creator(int threadCount)
	{
		pool = new Thread[threadCount]; 
		
		for(int sprinter = 0; sprinter < threadCount; sprinter++)
		{
			String tName = ("Task " + (sprinter +1));
			
			TaskRunner run = new TaskRunner(Fifo);
			Thread work = new Thread(run, tName);
			
			pool[sprinter] = work;
			
			pool[sprinter].start();
		}
	}

}
