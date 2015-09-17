package com.alibaba.middleware.race.rpc.netty;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;
import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;
import com.alibaba.middleware.race.rpc.serializer.KryoSerialization;
import com.alibaba.middleware.race.rpc.tool.ByteObjConverter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
public class RpcClientHandler extends ChannelInboundHandlerAdapter  {
	private static byte[] cacheName=null;
	private static Object cacheValue=null;
	private RpcConnection connect;
	private Throwable cause;
	KryoSerialization kryo=new KryoSerialization();
	private Map<String,ResponseCallbackListener> listenermap;
	RpcClientHandler()
	{
		//TODO nothing
	}
	private static boolean cacheEqual(byte[] data1,byte[] data2)
	{
		if(data1==null)
		{
			if(data2!=null)
				return false;
		}
		else
		{
			if(data2==null)
				return false;
			
			if(data1.length!=data2.length)
				return false;
			
			for (int i = 0; i < data1.length; i++) {
				if(data1[i]!=data2[i])
					return false;
			}
		}
		return true;
	}
	public RpcClientHandler(RpcConnection conn)
	{
		this.connect=conn;
		listenermap=new HashMap<String, ResponseCallbackListener>();
	}
	private void notifyListenerResponse(String method,Object result)
	{
		//System.out.println("begin notify:"+listenermap.size());
		if(listenermap!=null&&listenermap.containsKey(method)&&listenermap.get(method)!=null)
		{
			listenermap.get(method).onResponse(result);
			System.out.println("notify:"+method);
			
		}
	}
	private void notifyListenerException(String method,Throwable caus)
	{
		if(listenermap!=null&&listenermap.containsKey(method)&&listenermap.get(method)!=null)
		{
			listenermap.get(method).onResponse(cause);
		}
	}
	public void setAsynMethod(Map<String,ResponseCallbackListener> map)
	{
		this.listenermap.putAll(map);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println("connected on server:"+ctx.channel().localAddress().toString());
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		super.channelRead(ctx, msg);
		RpcResponse response=(RpcResponse)msg;
		String key = response.getRequestId();
		if(connect.containsFuture(key))
		{
			
			InvokeFuture<Object> future = connect.removeFuture(key);
			if (future==null)
			{
				return;
			}
			if(this.cause!=null)
			{
				future.setResult(this.cause);
				notifyListenerException(future.getMethod(),cause);
				cause.printStackTrace();
			}
			else
			{
				byte[] data=(byte[]) response.getAppResponse();
				if(data!=null)
				{
					if(cacheName!=null&&cacheEqual(data, cacheName))
					{
						response.setAppResponse(cacheValue);
					}
					else
					{
						response.setAppResponse(ByteObjConverter.ByteToObject(data));
						cacheName=data;
						cacheValue=ByteObjConverter.ByteToObject(data);
					}
				}
				
				future.setResult(response);
				this.connect.setResult(response);
				notifyListenerResponse(future.getMethod(),response.getAppResponse());
				
				for (InvokeFuture<Object> f : connect.getFutures(future.getMethod())) {
					f.setResult(response);
				}
			}
		}
		else
		{
			//System.out.println("not int future");
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//super.exceptionCaught(ctx, cause);
		this.cause=cause;
		//cause.printStackTrace();
	}
}
