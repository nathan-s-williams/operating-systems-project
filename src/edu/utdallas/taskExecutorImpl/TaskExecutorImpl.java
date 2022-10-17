package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFifo.BlockingFifo;
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

public class TaskExecutorImpl implements TaskExecutor {
	// array holding threads
	Thread pool[];
	BlockingFifo Fifo;

	public TaskExecutorImpl(int threadPoolSize) {
		// calls the fifo
		BlockingFifo Temp = new BlockingFifo((2 * threadPoolSize));
		Fifo = Temp;

		// make the thread pool
		creator(threadPoolSize);

	}

	@Override
	public void addTask(Task task) {
		// puts task in the fifo errors if it can't
		try {
			Fifo.put(task);
		} catch (Throwable e) {

			System.out.println(e + ": ERROR");
		}
	}

	// creates the threads and thread pool
	public void creator(int threadCount) {
		pool = new Thread[threadCount];

		for (int sprinter = 0; sprinter < threadCount; sprinter++) {
			String tName = ("Task " + (sprinter + 1));

			TaskRunner run = new TaskRunner(Fifo);
			Thread work = new Thread(run, tName);

			pool[sprinter] = work;

			pool[sprinter].start();
		}
	}

}
