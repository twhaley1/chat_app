package com.whaley.chatserver.test.unittest.service.bridge.synchronizedqueue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.whaley.chatserver.service.bridge.SynchronizedQueue;

public class TestOperations {

	private volatile Integer singleEnqueueTestNumber = null;
	private volatile Integer transferTestNumber = null;
	
	@Test
	public void testAddsToQueue() {
		SynchronizedQueue<Integer> queue = new SynchronizedQueue<Integer>();
		queue.enqueue(1);
		
		assertAll(() -> assertEquals(1, queue.size()),
				() -> assertEquals(1, queue.dequeue()));
	}

	@Test
	public void testAddsToQueueAndWaits() throws InterruptedException {
		SynchronizedQueue<Integer> queue = new SynchronizedQueue<Integer>();
		Thread dequeueingThread = new Thread(() -> {
			while (true) {
				synchronized (queue) {
					if (!queue.isEmpty()) {
						this.singleEnqueueTestNumber = queue.dequeueAndNotify();
						break;
					}
				}
			}
		});
		dequeueingThread.start();
		queue.enqueueAndWait(4);
		assertEquals(4, this.singleEnqueueTestNumber);
	}
	
	@Test
	public void testAddsMultipleToQueue() {
		SynchronizedQueue<Integer> queue = new SynchronizedQueue<Integer>();
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		
		assertAll(() -> assertEquals(3, queue.size()),
				() -> assertEquals(1, queue.dequeue()),
				() -> assertEquals(2, queue.dequeue()),
				() -> assertEquals(3, queue.dequeue()));
	}
	
	@Test
	public void testTransferWaitNotify() throws InterruptedException {
		SynchronizedQueue<Integer> queue = new SynchronizedQueue<Integer>();
		Thread dequeueingThread = new Thread(() -> {
			while (true) {
				synchronized (queue) {
					if (!queue.isEmpty()) {
						List<Integer> results = queue.transferAndNotify();
						this.transferTestNumber = results.get(0);
						break;
					}
				}
			}
		});
		
		dequeueingThread.start();
		queue.enqueueAndWait(5);
		assertEquals(5, this.transferTestNumber);
	}
}
