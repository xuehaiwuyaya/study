/*  1:   */ package bhz.netty.http;
/*  2:   */ 
/*  3:   */ import io.netty.channel.ChannelHandler;
/*  4:   */ import io.netty.channel.ChannelInitializer;
/*  5:   */ import io.netty.channel.ChannelPipeline;
/*  6:   */ import io.netty.channel.socket.SocketChannel;
/*  7:   */ import io.netty.handler.codec.http.HttpServerCodec;
/*  8:   */ import io.netty.handler.ssl.SslContext;
/*  9:   */ 
/* 10:   */ public class HttpHelloWorldServerInitializer
/* 11:   */   extends ChannelInitializer<SocketChannel>
/* 12:   */ {
/* 13:   */   private final SslContext sslCtx;
/* 14:   */   
/* 15:   */   public HttpHelloWorldServerInitializer(SslContext sslCtx)
/* 16:   */   {
/* 17:15 */     this.sslCtx = sslCtx;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void initChannel(SocketChannel ch)
/* 21:   */   {
/* 22:20 */     ChannelPipeline p = ch.pipeline();
/* 23:21 */     if (this.sslCtx != null) {
/* 24:22 */       p.addLast(new ChannelHandler[] { this.sslCtx.newHandler(ch.alloc()) });
/* 25:   */     }
/* 26:24 */     p.addLast(new ChannelHandler[] { new HttpServerCodec() });
/* 27:25 */     p.addLast(new ChannelHandler[] { new HttpHelloWorldServerHandler() });
/* 28:   */   }
/* 29:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.http.HttpHelloWorldServerInitializer
 * JD-Core Version:    0.7.0.1
 */