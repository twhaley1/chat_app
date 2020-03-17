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
	
	public synchronized void enqueue(E element) {
		this.queue.add(element);
	}
	
	public synchronized void enqueueAndWait(E element) throws InterruptedException {
		this.queue.add(element);
		this.wait();
	}
	
	public synchronized E dequeue() {
		return this.queue.remove();
	}
	
	public synchronized List<E> transfer() {
		List<E> items = new ArrayList<E>(this.queue);
		this.queue.clear();
		return items;
	}
	
	public synchronized List<E> transferAndNotify() {
		List<E> items = new ArrayList<E>(this.queue);
		this.queue.clear();
		this.notify();
		return items;
	}
	
	public synchronized boolean isEmpty() {
		return this.queue.isEmpty();
	}
	
	public synchronized int size() {
		return this.queue.size();
	}
}
