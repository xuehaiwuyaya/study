/*  1:   */ package bhz.netty.upload;
/*  2:   */ 
/*  3:   */ import io.netty.channel.ChannelInitializer;
/*  4:   */ import io.netty.channel.ChannelPipeline;
/*  5:   */ import io.netty.channel.socket.SocketChannel;
/*  6:   */ import io.netty.handler.codec.http.HttpClientCodec;
/*  7:   */ import io.netty.handler.codec.http.HttpContentDecompressor;
/*  8:   */ import io.netty.handler.ssl.SslContext;
/*  9:   */ import io.netty.handler.stream.ChunkedWriteHandler;
/* 10:   */ 
/* 11:   */ public class HttpUploadClientIntializer
/* 12:   */   extends ChannelInitializer<SocketChannel>
/* 13:   */ {
/* 14:   */   private final SslContext sslCtx;
/* 15:   */   
/* 16:   */   public HttpUploadClientIntializer(SslContext sslCtx)
/* 17:   */   {
/* 18:17 */     this.sslCtx = sslCtx;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void initChannel(SocketChannel ch)
/* 22:   */   {
/* 23:22 */     ChannelPipeline pipeline = ch.pipeline();
/* 24:24 */     if (this.sslCtx != null) {
/* 25:25 */       pipeline.addLast("ssl", this.sslCtx.newHandler(ch.alloc()));
/* 26:   */     }
/* 27:28 */     pipeline.addLast("codec", new HttpClientCodec());
/* 28:   */     
/* 29:   */ 
/* 30:31 */     pipeline.addLast("inflater", new HttpContentDecompressor());
/* 31:   */     
/* 32:   */ 
/* 33:34 */     pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
/* 34:   */     
/* 35:36 */     pipeline.addLast("handler", new HttpUploadClientHandler());
/* 36:   */   }
/* 37:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.upload.HttpUploadClientIntializer
 * JD-Core Version:    0.7.0.1
 */