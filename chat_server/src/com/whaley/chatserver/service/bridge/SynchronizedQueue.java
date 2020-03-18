package com.whaley.chatserver.service.bridge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SynchronizedQueue<E> {

	private Queue<E> queue;
	
	public SynchronizedQueue() {
		this.queue = new LinkedList<E>();
	}
	
	public void enqueue(E element) {
		synchronized (this) {
			this.queue.add(element);
		}
	}
	
	public void enqueueAndWait(E element) throws InterruptedException {
		synchronized (this) {
			this.enqueue(element);
			this.wait();
		}
	}
	
	public E dequeue() {
		synchronized (this) {
			return this.queue.remove();
		}
	}
	
	public E dequeueAndNotify() {
		synchronized (this) {
			E item = this.dequeue();
			this.notify();
			return item;
		}
	}
	
	public List<E> transfer() {
		synchronized (this) {
			List<E> items = new ArrayList<E>(this.queue);
			this.queue.clear();
			return items;
		}
	}
	
	public List<E> transferAndNotify() {
		synchronized (this) {
			List<E> items = this.transfer();
			this.notify();
			return items;
		}
	}
	
	public boolean isEmpty() {
		synchronized (this) {
			return this.queue.isEmpty();
		}
	}
	
	public int size() {
		synchronized (this) {
			return this.queue.size();
		}
	}
}
