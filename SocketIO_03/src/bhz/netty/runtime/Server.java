/*  1:   */ package bhz.netty.runtime;
/*  2:   */ 
/*  3:   */ import io.netty.bootstrap.ServerBootstrap;
/*  4:   */ import io.netty.channel.Channel;
/*  5:   */ import io.netty.channel.ChannelFuture;
/*  6:   */ import io.netty.channel.ChannelHandler;
/*  7:   */ import io.netty.channel.ChannelInitializer;
/*  8:   */ import io.netty.channel.ChannelOption;
/*  9:   */ import io.netty.channel.ChannelPipeline;
/* 10:   */ import io.netty.channel.EventLoopGroup;
/* 11:   */ import io.netty.channel.nio.NioEventLoopGroup;
/* 12:   */ import io.netty.channel.socket.SocketChannel;
/* 13:   */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/* 14:   */ import io.netty.handler.logging.LogLevel;
/* 15:   */ import io.netty.handler.logging.LoggingHandler;
/* 16:   */ import io.netty.handler.timeout.ReadTimeoutHandler;
/* 17:   */ 
/* 18:   */ public class Server
/* 19:   */ {
/* 20:   */   public static void main(String[] args)
/* 21:   */     throws Exception
/* 22:   */   {
/* 23:19 */     EventLoopGroup pGroup = new NioEventLoopGroup();
/* 24:20 */     EventLoopGroup cGroup = new NioEventLoopGroup();
/* 25:   */     
/* 26:22 */     ServerBootstrap b = new ServerBootstrap();
/* 27:   */     
/* 28:   */ 
/* 29:   */ 
/* 30:   */ 
/* 31:27 */     ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)b.group(pGroup, cGroup).channel(NioServerSocketChannel.class)).option(ChannelOption.SO_BACKLOG, Integer.valueOf(1024))).handler(new LoggingHandler(LogLevel.INFO)))
/* 32:28 */       .childHandler(new ChannelInitializer()
/* 33:   */       {
/* 34:   */         protected void initChannel(SocketChannel sc)
/* 35:   */           throws Exception
/* 36:   */         {
/* 37:30 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingDecoder() });
/* 38:31 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingEncoder() });
/* 39:32 */           sc.pipeline().addLast(new ChannelHandler[] { new ReadTimeoutHandler(5) });
/* 40:33 */           sc.pipeline().addLast(new ChannelHandler[] { new ServerHandler() });
/* 41:   */         }
/* 42:36 */       });
/* 43:37 */     ChannelFuture cf = b.bind(8765).sync();
/* 44:   */     
/* 45:39 */     cf.channel().closeFuture().sync();
/* 46:40 */     pGroup.shutdownGracefully();
/* 47:41 */     cGroup.shutdownGracefully();
/* 48:   */   }
/* 49:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.runtime.Server
 * JD-Core Version:    0.7.0.1
 */