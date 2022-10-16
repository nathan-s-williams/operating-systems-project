package edu.utdallas.taskExecutor;

public class BlockingFifo{

	private int size;
	private int count = 0;
	private int nextIn = 0, nextOut = 0;
	private Task fifo[];
	private Object notEmptyMonitor = new Object();
	private Object notFullMonitor = new Object();
	
	//Constructors
	BlockingFifo(){
		this.size = 10;
		this.fifo = new Task[10];
	}
	
	public BlockingFifo(int size){
		if(size > 100) size = 100; //Size cannot be larger than 100 tasks.
		this.size = size;
		this.fifo = new Task[size];
	}
	

	public void put(Task task) throws Exception {
		
		synchronized(this.notFullMonitor) {
			while(this.count == this.size) {
				this.notFullMonitor.wait();
			}
		}
		
		synchronized(this) {
			this.fifo[this.nextIn] = task;
			this.nextIn = (this.nextIn + 1) % this.size;
			this.count++;
			synchronized(this.notEmptyMonitor) {
				this.notEmptyMonitor.notify();
			}
		}
	}


	public Task take() throws Exception {
		
		synchronized(this.notEmptyMonitor) {
			while(this.count == 0) {
				this.notEmptyMonitor.wait();
			}
		}
		
		synchronized(this) {
			Task taskToComplete = this.fifo[nextOut];
			this.nextOut = (this.nextOut + 1) % this.size;
			this.count--;
			synchronized(this.notFullMonitor) {
				this.notFullMonitor.notify();
			}
			return taskToComplete;
		}
	}
}
