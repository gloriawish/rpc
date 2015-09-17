package com.alibaba.middleware.race.rpc.netty;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class InvokeFuture<T> {
  
	private Semaphore semaphore = new Semaphore(0);
	private Throwable cause;   
	private T result;
	private List<InvokeListener<T>> listeners=new ArrayList<InvokeListener<T>>();
	private String method;
	/**
	 * @return the method
	 */
	private boolean isRelase=false;
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	public InvokeFuture() {
	}
 
	public void setResult(T result) {
		this.result=result;
		notifyListeners();
		synchronized (semaphore) {
			if(!isRelase)
			{
				semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits()); 
				isRelase=true;
			}
		}
	}

	public Object getResult(long timeout, TimeUnit unit){ 
		try {
			if (!semaphore.tryAcquire(timeout, unit)) {
				throw new RuntimeException();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		 
		if (this.cause!=null) {
			throw new RuntimeException(this.cause);
		}
		return result;
	}
  
	public void setCause(Throwable cause) {
		this.cause = cause;
		notifyListeners();
		if(!isRelase)
		{
			semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits()); 
			isRelase=true;
		}
	}
 
	public Throwable getCause() {
		return cause;
	}
 
	public void addInvokerListener(InvokeListener<T> listener) {
		this.listeners.add(listener);
	}
 
	private void notifyListeners(){
		for (InvokeListener<T> listener : listeners) {
			if (cause!=null) {
				listener.failure(cause);
			}else{
				listener.success(result);
			}
		}
	}
}
interface InvokeListener<T> {

	void success(T t);
	
	void failure(Throwable e);
	
}