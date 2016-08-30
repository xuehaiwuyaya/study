/*  1:   */ package bhz.netty.http;
/*  2:   */ 
/*  3:   */ import io.netty.bootstrap.ServerBootstrap;
/*  4:   */ import io.netty.channel.Channel;
/*  5:   */ import io.netty.channel.ChannelFuture;
/*  6:   */ import io.netty.channel.ChannelOption;
/*  7:   */ import io.netty.channel.EventLoopGroup;
/*  8:   */ import io.netty.channel.nio.NioEventLoopGroup;
/*  9:   */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/* 10:   */ import io.netty.handler.logging.LogLevel;
/* 11:   */ import io.netty.handler.logging.LoggingHandler;
/* 12:   */ import io.netty.handler.ssl.SslContext;
/* 13:   */ import io.netty.handler.ssl.util.SelfSignedCertificate;
/* 14:   */ import java.io.PrintStream;
/* 15:   */ 
/* 16:   */ public final class HttpHelloWorldServer
/* 17:   */ {
/* 18:21 */   static final boolean SSL = System.getProperty("ssl") != null;
/* 19:22 */   static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
/* 20:   */   
/* 21:   */   public static void main(String[] args)
/* 22:   */     throws Exception
/* 23:   */   {
/* 24:   */     SslContext sslCtx;
/* 25:   */     SslContext sslCtx;
/* 26:27 */     if (SSL)
/* 27:   */     {
/* 28:28 */       SelfSignedCertificate ssc = new SelfSignedCertificate();
/* 29:29 */       sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
/* 30:   */     }
/* 31:   */     else
/* 32:   */     {
/* 33:31 */       sslCtx = null;
/* 34:   */     }
/* 35:35 */     EventLoopGroup bossGroup = new NioEventLoopGroup(1);
/* 36:36 */     EventLoopGroup workerGroup = new NioEventLoopGroup();
/* 37:   */     try
/* 38:   */     {
/* 39:38 */       ServerBootstrap b = new ServerBootstrap();
/* 40:39 */       b.option(ChannelOption.SO_BACKLOG, Integer.valueOf(1024));
/* 41:   */       
/* 42:   */ 
/* 43:42 */       ((ServerBootstrap)((ServerBootstrap)b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)).handler(new LoggingHandler(LogLevel.INFO)))
/* 44:43 */         .childHandler(new HttpHelloWorldServerInitializer(sslCtx));
/* 45:   */       
/* 46:45 */       Channel ch = b.bind(PORT).sync().channel();
/* 47:   */       
/* 48:47 */       System.err.println("Open your web browser and navigate to " + (
/* 49:48 */         SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
/* 50:   */       
/* 51:50 */       ch.closeFuture().sync();
/* 52:   */     }
/* 53:   */     finally
/* 54:   */     {
/* 55:52 */       bossGroup.shutdownGracefully();
/* 56:53 */       workerGroup.shutdownGracefully();
/* 57:   */     }
/* 58:   */   }
/* 59:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.http.HttpHelloWorldServer
 * JD-Core Version:    0.7.0.1
 */