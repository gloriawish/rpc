package com.alibaba.middleware.race.rpc.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;
 
public class ResultFuture<T> implements Future<Object> {
  
	private Semaphore semaphore = new Semaphore(0);
	private Throwable cause;   
	private T result;
	private List<ResponseCallbackListener> listeners=new ArrayList<ResponseCallbackListener>();
	private boolean isdone;
	private long timeOut;
	private String requestId;
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ResultFuture(long timeOut) {
		this.timeOut=timeOut;
	}
 
	public void setResult(T result) {
		this.result=result;
		this.isdone=true;
		notifyListeners();
		semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits()); 
	}
  
	public void setCause(Throwable cause) {
		this.cause = cause;
		notifyListeners();
		semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits());  
	}
 
	public Throwable getCause() {
		return cause;
	}
 
	public void addInvokerListener(ResponseCallbackListener listener) {
		this.listeners.add(listener);
	}
 
	private void notifyListeners(){
		for (ResponseCallbackListener listener : listeners) {
			if (cause!=null) {
				listener.onException((Exception) cause);
			}else{
				listener.onResponse(result);
			}
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isdone;
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		try {
			if (!semaphore.tryAcquire(timeOut, TimeUnit.MILLISECONDS)) {
				throw new RuntimeException("time out");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("error");
		}
		 
		if (this.cause!=null) {
			throw new RuntimeException(this.cause.toString());
		}
		return result;
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		try {
			if (!semaphore.tryAcquire(timeout, unit)) {
				throw new RuntimeException("time out");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("error");
		}
		 
		if (this.cause!=null) {
			throw new RuntimeException(this.cause.toString());
		}
		return result;
	}
}
