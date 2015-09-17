package com.alibaba.middleware.race.rpc.serializer;

import java.util.List;

import org.nustaq.serialization.FSTConfiguration;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FSTNettyDecode extends ByteToMessageDecoder {

   private FSTConfiguration conf = FSTConfiguration.getDefaultConfiguration();

   @Override
   protected void decode(ChannelHandlerContext ctx, ByteBuf in,
         List<Object> out) throws Exception {
      // TODO Auto-generated method stub
      if (in.readableBytes() < 4) {
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

      byte[] body = new byte[dataLength];
      in.readBytes(body);
      Object o = conf.asObject(body); 
      out.add(o);
   }

}