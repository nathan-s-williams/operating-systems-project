package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFifo.BlockingFifo;
import edu.utdallas.taskExecutor.Task;

public class TaskRunner implements Runnable {
	BlockingFifo Fifo;
	boolean stopGo = true;

	// Constructor
	public TaskRunner(BlockingFifo Fifo) {
		this.Fifo = Fifo;
	}

	// takes from fifo and runs
	public void run() {
		while (stopGo) {

			// tries to take from fifo and run it
			try {
				Task doer = Fifo.take();
				doer.execute();
			} catch (Throwable th) {
				System.out.println(th + ": ERROR");
			}
		}
	}
}
