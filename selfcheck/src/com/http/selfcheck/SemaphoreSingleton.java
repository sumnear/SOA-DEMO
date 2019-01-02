package com.http.selfcheck;

import java.util.concurrent.Semaphore;

public class SemaphoreSingleton {
	private static Semaphore semaphore = new Semaphore(10);
	public static Semaphore getSemaphore(){
		return semaphore;
	}
}
