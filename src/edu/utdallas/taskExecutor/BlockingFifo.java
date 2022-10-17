package edu.utdallas.taskExecutor;

public class BlockingFifo{

	//Instance variables for BlockingFifo
	private int size;								//Size of fifo
	private int count = 0;							//Current items in fifo
	private int nextIn = 0, nextOut = 0;			//Pointers to the front and back of queue
	private Task fifo[];							//Fifo data structure (array)
	private Object notEmptyMonitor = new Object(); 	//Empty monitor
	private Object notFullMonitor = new Object();	//Full monitor
	
	//Constructors
	//Default fifo size = 10
	BlockingFifo(){
		this.size = 10;
		this.fifo = new Task[10];
	}
	
	//Set fifo equal to size parameter
	public BlockingFifo(int size){
		if(size > 100) size = 100; 					//Size cannot be larger than 100 tasks.
		this.size = size;
		this.fifo = new Task[size];
	}
	
	//Put a task into the fifo.
	public void put(Task task) throws Exception {
		//Synchronize on notFullMonitor so that only one thread can wait or notify at any one time.
		synchronized(this.notFullMonitor) {
			//Block threads if fifo is full.
			//Loop rechecks count after thread is unblocked
			while(this.count == this.size) {
				this.notFullMonitor.wait();
			}
		}
		
		//Synchronize all instance variables when adding a task to fifo.
		synchronized(this) {
			this.fifo[this.nextIn] = task;
			this.nextIn = (this.nextIn + 1) % this.size;
			this.count++;
			
			//Synchronize on notEmptyMonitor so that only one thread can wait or notify at any one time.
			synchronized(this.notEmptyMonitor) {
				this.notEmptyMonitor.notify();
			}
		}
	}

	//Take a task from the fifo.
	public Task take() throws Exception {
		//Synchronize on notEmptyMonitor so that only one thread can wait or notify at any one time.
		synchronized(this.notEmptyMonitor) {
			//Block threads if fifo is empty.
			//Loop rechecks count after thread is unblocked
			while(this.count == 0) {
				this.notEmptyMonitor.wait();
			}
		}
		
		//Synchronize all instance variables when taking a task from fifo.
		synchronized(this) {
			Task taskToComplete = this.fifo[nextOut];
			this.nextOut = (this.nextOut + 1) % this.size;
			this.count--;
			//Synchronize on notFullMonitor so that only one thread can wait or notify at any one time.
			synchronized(this.notFullMonitor) {
				this.notFullMonitor.notify();
			}
			return taskToComplete;
		}
	}
}
