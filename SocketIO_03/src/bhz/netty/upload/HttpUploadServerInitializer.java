/*  1:   */ package bhz.netty.upload;
/*  2:   */ 
/*  3:   */ import io.netty.channel.ChannelHandler;
/*  4:   */ import io.netty.channel.ChannelInitializer;
/*  5:   */ import io.netty.channel.ChannelPipeline;
/*  6:   */ import io.netty.channel.socket.SocketChannel;
/*  7:   */ import io.netty.handler.codec.http.HttpContentCompressor;
/*  8:   */ import io.netty.handler.codec.http.HttpRequestDecoder;
/*  9:   */ import io.netty.handler.codec.http.HttpResponseEncoder;
/* 10:   */ import io.netty.handler.ssl.SslContext;
/* 11:   */ 
/* 12:   */ public class HttpUploadServerInitializer
/* 13:   */   extends ChannelInitializer<SocketChannel>
/* 14:   */ {
/* 15:   */   private final SslContext sslCtx;
/* 16:   */   
/* 17:   */   public HttpUploadServerInitializer(SslContext sslCtx)
/* 18:   */   {
/* 19:17 */     this.sslCtx = sslCtx;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void initChannel(SocketChannel ch)
/* 23:   */   {
/* 24:22 */     ChannelPipeline pipeline = ch.pipeline();
/* 25:24 */     if (this.sslCtx != null) {
/* 26:25 */       pipeline.addLast(new ChannelHandler[] { this.sslCtx.newHandler(ch.alloc()) });
/* 27:   */     }
/* 28:28 */     pipeline.addLast(new ChannelHandler[] { new HttpRequestDecoder() });
/* 29:29 */     pipeline.addLast(new ChannelHandler[] { new HttpResponseEncoder() });
/* 30:   */     
/* 31:   */ 
/* 32:32 */     pipeline.addLast(new ChannelHandler[] { new HttpContentCompressor() });
/* 33:   */     
/* 34:34 */     pipeline.addLast(new ChannelHandler[] { new HttpUploadServerHandler() });
/* 35:   */   }
/* 36:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.upload.HttpUploadServerInitializer
 * JD-Core Version:    0.7.0.1
 */