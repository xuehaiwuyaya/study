/*  1:   */ package bhz.netty.heartBeat;
/*  2:   */ 
/*  3:   */ import io.netty.bootstrap.Bootstrap;
/*  4:   */ import io.netty.channel.Channel;
/*  5:   */ import io.netty.channel.ChannelFuture;
/*  6:   */ import io.netty.channel.ChannelHandler;
/*  7:   */ import io.netty.channel.ChannelInitializer;
/*  8:   */ import io.netty.channel.ChannelPipeline;
/*  9:   */ import io.netty.channel.EventLoopGroup;
/* 10:   */ import io.netty.channel.nio.NioEventLoopGroup;
/* 11:   */ import io.netty.channel.socket.SocketChannel;
/* 12:   */ import io.netty.channel.socket.nio.NioSocketChannel;
/* 13:   */ 
/* 14:   */ public class Client
/* 15:   */ {
/* 16:   */   public static void main(String[] args)
/* 17:   */     throws Exception
/* 18:   */   {
/* 19:16 */     EventLoopGroup group = new NioEventLoopGroup();
/* 20:17 */     Bootstrap b = new Bootstrap();
/* 21:   */     
/* 22:19 */     ((Bootstrap)((Bootstrap)b.group(group)).channel(NioSocketChannel.class))
/* 23:20 */       .handler(new ChannelInitializer()
/* 24:   */       {
/* 25:   */         protected void initChannel(SocketChannel sc)
/* 26:   */           throws Exception
/* 27:   */         {
/* 28:23 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingDecoder() });
/* 29:24 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingEncoder() });
/* 30:25 */           sc.pipeline().addLast(new ChannelHandler[] { new ClienHeartBeattHandler() });
/* 31:   */         }
/* 32:28 */       });
/* 33:29 */     ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();
/* 34:   */     
/* 35:31 */     cf.channel().closeFuture().sync();
/* 36:32 */     group.shutdownGracefully();
/* 37:   */   }
/* 38:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.heartBeat.Client
 * JD-Core Version:    0.7.0.1
 */