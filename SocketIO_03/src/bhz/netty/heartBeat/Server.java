/*  1:   */ package bhz.netty.heartBeat;
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
/* 16:   */ 
/* 17:   */ public class Server
/* 18:   */ {
/* 19:   */   public static void main(String[] args)
/* 20:   */     throws Exception
/* 21:   */   {
/* 22:18 */     EventLoopGroup pGroup = new NioEventLoopGroup();
/* 23:19 */     EventLoopGroup cGroup = new NioEventLoopGroup();
/* 24:   */     
/* 25:21 */     ServerBootstrap b = new ServerBootstrap();
/* 26:   */     
/* 27:   */ 
/* 28:   */ 
/* 29:   */ 
/* 30:26 */     ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)b.group(pGroup, cGroup).channel(NioServerSocketChannel.class)).option(ChannelOption.SO_BACKLOG, Integer.valueOf(1024))).handler(new LoggingHandler(LogLevel.INFO)))
/* 31:27 */       .childHandler(new ChannelInitializer()
/* 32:   */       {
/* 33:   */         protected void initChannel(SocketChannel sc)
/* 34:   */           throws Exception
/* 35:   */         {
/* 36:29 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingDecoder() });
/* 37:30 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingEncoder() });
/* 38:31 */           sc.pipeline().addLast(new ChannelHandler[] { new ServerHeartBeatHandler() });
/* 39:   */         }
/* 40:34 */       });
/* 41:35 */     ChannelFuture cf = b.bind(8765).sync();
/* 42:   */     
/* 43:37 */     cf.channel().closeFuture().sync();
/* 44:38 */     pGroup.shutdownGracefully();
/* 45:39 */     cGroup.shutdownGracefully();
/* 46:   */   }
/* 47:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.heartBeat.Server
 * JD-Core Version:    0.7.0.1
 */