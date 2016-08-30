/*  1:   */ package bhz.netty.httpfile;
/*  2:   */ 
/*  3:   */ import io.netty.bootstrap.ServerBootstrap;
/*  4:   */ import io.netty.channel.Channel;
/*  5:   */ import io.netty.channel.ChannelFuture;
/*  6:   */ import io.netty.channel.ChannelInitializer;
/*  7:   */ import io.netty.channel.ChannelPipeline;
/*  8:   */ import io.netty.channel.EventLoopGroup;
/*  9:   */ import io.netty.channel.nio.NioEventLoopGroup;
/* 10:   */ import io.netty.channel.socket.SocketChannel;
/* 11:   */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/* 12:   */ import io.netty.handler.codec.http.HttpObjectAggregator;
/* 13:   */ import io.netty.handler.codec.http.HttpRequestDecoder;
/* 14:   */ import io.netty.handler.codec.http.HttpResponseEncoder;
/* 15:   */ import io.netty.handler.stream.ChunkedWriteHandler;
/* 16:   */ import java.io.PrintStream;
/* 17:   */ 
/* 18:   */ public class HttpFileServer
/* 19:   */ {
/* 20:   */   private static final String DEFAULT_URL = "/sources/";
/* 21:   */   
/* 22:   */   public void run(int port, final String url)
/* 23:   */     throws Exception
/* 24:   */   {
/* 25:20 */     EventLoopGroup bossGroup = new NioEventLoopGroup();
/* 26:21 */     EventLoopGroup workerGroup = new NioEventLoopGroup();
/* 27:   */     try
/* 28:   */     {
/* 29:23 */       ServerBootstrap b = new ServerBootstrap();
/* 30:   */       
/* 31:25 */       ((ServerBootstrap)b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class))
/* 32:26 */         .childHandler(new ChannelInitializer()
/* 33:   */         {
/* 34:   */           protected void initChannel(SocketChannel ch)
/* 35:   */             throws Exception
/* 36:   */           {
/* 37:31 */             ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
/* 38:   */             
/* 39:33 */             ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
/* 40:   */             
/* 41:35 */             ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
/* 42:   */             
/* 43:37 */             ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
/* 44:   */             
/* 45:39 */             ch.pipeline().addLast("fileServerHandler", 
/* 46:40 */               new HttpFileServerHandler(url));
/* 47:   */           }
/* 48:42 */         });
/* 49:43 */       ChannelFuture future = b.bind("192.168.1.111", port).sync();
/* 50:44 */       System.out.println("HTTP文件目录服务器启动，网址是 : http://192.168.1.111:" + port + url);
/* 51:45 */       future.channel().closeFuture().sync();
/* 52:   */     }
/* 53:   */     finally
/* 54:   */     {
/* 55:47 */       bossGroup.shutdownGracefully();
/* 56:48 */       workerGroup.shutdownGracefully();
/* 57:   */     }
/* 58:   */   }
/* 59:   */   
/* 60:   */   public static void main(String[] args)
/* 61:   */     throws Exception
/* 62:   */   {
/* 63:53 */     int port = 8765;
/* 64:54 */     String url = "/sources/";
/* 65:55 */     new HttpFileServer().run(port, url);
/* 66:   */   }
/* 67:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.httpfile.HttpFileServer
 * JD-Core Version:    0.7.0.1
 */