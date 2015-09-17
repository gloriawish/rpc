package com.alibaba.middleware.race.rpc.netty;

import java.util.List;
import java.util.Map;

import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;
import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;

/**
 * 描述与服务器的连接
 * @author sei.zz
 *
 */
public interface RpcConnection {
	void init();
	void connect();
	void connect(String host,int port);
	Object Send(RpcRequest request,boolean async);
	void close();
	boolean isConnected();
	boolean isClosed();
	public boolean containsFuture(String key);
	public InvokeFuture<Object> removeFuture(String key);
	public void setResult(Object ret);
	public void setTimeOut(long timeout);
	public void setAsyncMethod(Map<String,ResponseCallbackListener> map);
	public List<InvokeFuture<Object>> getFutures(String method);
}
