package com.alibaba.middleware.race.rpc.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.nustaq.serialization.FSTConfiguration;
public class FSTNettyEncode extends MessageToByteEncoder {


  private FSTConfiguration conf = FSTConfiguration.getDefaultConfiguration();

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
        throws Exception {
     // TODO Auto-generated method stub

     byte[] bytes = conf.asByteArray(msg);

     out.writeInt(bytes.length);
     out.writeBytes(bytes);
  }

}
