/*  1:   */ package bhz.netty.upload;
/*  2:   */ 
/*  3:   */ import io.netty.bootstrap.ServerBootstrap;
/*  4:   */ import io.netty.channel.Channel;
/*  5:   */ import io.netty.channel.ChannelFuture;
/*  6:   */ import io.netty.channel.EventLoopGroup;
/*  7:   */ import io.netty.channel.nio.NioEventLoopGroup;
/*  8:   */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*  9:   */ import io.netty.handler.logging.LogLevel;
/* 10:   */ import io.netty.handler.logging.LoggingHandler;
/* 11:   */ import io.netty.handler.ssl.SslContext;
/* 12:   */ import io.netty.handler.ssl.util.SelfSignedCertificate;
/* 13:   */ import java.io.PrintStream;
/* 14:   */ 
/* 15:   */ public final class HttpUploadServer
/* 16:   */ {
/* 17:19 */   static final boolean SSL = System.getProperty("ssl") != null;
/* 18:20 */   static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
/* 19:   */   
/* 20:   */   public static void main(String[] args)
/* 21:   */     throws Exception
/* 22:   */   {
/* 23:   */     SslContext sslCtx;
/* 24:   */     SslContext sslCtx;
/* 25:25 */     if (SSL)
/* 26:   */     {
/* 27:26 */       SelfSignedCertificate ssc = new SelfSignedCertificate();
/* 28:27 */       sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
/* 29:   */     }
/* 30:   */     else
/* 31:   */     {
/* 32:29 */       sslCtx = null;
/* 33:   */     }
/* 34:32 */     EventLoopGroup bossGroup = new NioEventLoopGroup(1);
/* 35:33 */     EventLoopGroup workerGroup = new NioEventLoopGroup();
/* 36:   */     try
/* 37:   */     {
/* 38:35 */       ServerBootstrap b = new ServerBootstrap();
/* 39:36 */       b.group(bossGroup, workerGroup);
/* 40:37 */       b.channel(NioServerSocketChannel.class);
/* 41:38 */       b.handler(new LoggingHandler(LogLevel.INFO));
/* 42:39 */       b.childHandler(new HttpUploadServerInitializer(sslCtx));
/* 43:   */       
/* 44:41 */       Channel ch = b.bind(PORT).sync().channel();
/* 45:   */       
/* 46:43 */       System.err.println("Open your web browser and navigate to " + (
/* 47:44 */         SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
/* 48:   */       
/* 49:46 */       ch.closeFuture().sync();
/* 50:   */     }
/* 51:   */     finally
/* 52:   */     {
/* 53:48 */       bossGroup.shutdownGracefully();
/* 54:49 */       workerGroup.shutdownGracefully();
/* 55:   */     }
/* 56:   */   }
/* 57:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.upload.HttpUploadServer
 * JD-Core Version:    0.7.0.1
 */