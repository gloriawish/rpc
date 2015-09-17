package com.alibaba.middleware.race.rpc.serializer;
import java.util.List;

import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;
import com.alibaba.middleware.race.rpc.tool.Tool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RpcDecoder extends ByteToMessageDecoder{
	
	private static byte[] requestCacheName=null;
	private static RpcRequest requestCacheValue=null;
	private static byte[] responseCacheName=null;
	private static RpcResponse responseCacheValue=null;
	private Class<?> genericClass;
	private KryoSerialization kryo;
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
        kryo=new KryoSerialization();
        kryo.register(genericClass);
    }
	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("decode:"+in.readableBytes());
		int HEAD_LENGTH=4;
        if (in.readableBytes() < HEAD_LENGTH) {
            return;
        }
        in.markReaderIndex();               
        int dataLength = in.readInt();       
        if (dataLength < 0) { 				 
            ctx.close();
        }
        if (in.readableBytes() < dataLength) { 
            in.resetReaderIndex();
            return;
        }
 				
        if(genericClass.equals(RpcResponse.class))
        {
        	int requestIdLength=in.readInt();//获取到requestId的长度
        	
        	byte[] requestIdBytes=new byte[requestIdLength];
        	in.readBytes(requestIdBytes);
        	
        	int bodyLength=dataLength-4-requestIdLength;
        	
        	byte[] body = new byte[bodyLength]; 
        	in.readBytes(body);
        	String requestId=new String(requestIdBytes);
        	
        	if(responseCacheName!=null&&cacheEqual(responseCacheName,body))
        	{
        		RpcResponse obj=new RpcResponse();
        		obj.setRequestId(requestId);
        		obj.setAppResponse(responseCacheValue.getAppResponse());
        		obj.setClazz(responseCacheValue.getClazz());
        		obj.setExption(responseCacheValue.getExption());
        		
        		out.add(obj);
        	}
        	else
        	{
	        	RpcResponse obj=(RpcResponse) Tool.deserialize(body, genericClass);
	        	obj.setRequestId(requestId);//设置requestId
	        	out.add(obj);
	        	
	        	responseCacheName=body;
	        	responseCacheValue=new RpcResponse();
	        	responseCacheValue.setAppResponse(obj.getAppResponse());
	        	responseCacheValue.setClazz(obj.getClazz());
	        	responseCacheValue.setExption(obj.getExption());
	        	
        	}
        }
        else if(genericClass.equals(RpcRequest.class))
        {
        	int requestIdLength=in.readInt();//获取到requestId的长度
        	
        	byte[] requestIdBytes=new byte[requestIdLength];
        	in.readBytes(requestIdBytes);
        	
        	int bodyLength=dataLength-4-requestIdLength;
        	
        	byte[] body = new byte[bodyLength]; 
        	in.readBytes(body);
        	String requestId=new String(requestIdBytes);
        	
        	if(requestCacheName!=null&&cacheEqual(requestCacheName,body))
        	{
        		RpcRequest obj=new RpcRequest();
        		obj.setClassName(requestCacheValue.getClassName());
        		obj.setContext(requestCacheValue.getContext());
        		obj.setMethodName(requestCacheValue.getMethodName());
        		obj.setParameters(requestCacheValue.getParameters());
        		obj.setParameterTypes(requestCacheValue.getParameterTypes());
        		obj.setRequestId(requestId);
        		
        		out.add(obj);
        		
        	}
        	else
        	{
	        	RpcRequest obj=(RpcRequest) Tool.deserialize(body, genericClass);
	        	obj.setRequestId(requestId);//设置requestId
	        	out.add(obj);
	        	
	        	requestCacheName=body;
	        	requestCacheValue=new RpcRequest();
	        	requestCacheValue.setClassName(obj.getClassName());
	        	requestCacheValue.setContext(obj.getContext());
	        	requestCacheValue.setMethodName(obj.getMethodName());
	        	requestCacheValue.setParameters(obj.getParameters());
	        	requestCacheValue.setParameterTypes(obj.getParameterTypes());
        	}
        }
        else
        {
        	byte[] body = new byte[dataLength]; 
          	in.readBytes(body); 
          	Object obj=Tool.deserialize(body, genericClass);
            out.add(obj);
        }
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
	
}
