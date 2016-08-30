/*  1:   */ package bhz.netty.upload;
/*  2:   */ 
/*  3:   */ import io.netty.buffer.ByteBuf;
/*  4:   */ import io.netty.channel.Channel;
/*  5:   */ import io.netty.channel.ChannelHandlerContext;
/*  6:   */ import io.netty.channel.SimpleChannelInboundHandler;
/*  7:   */ import io.netty.handler.codec.http.HttpContent;
/*  8:   */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*  9:   */ import io.netty.handler.codec.http.HttpHeaders;
/* 10:   */ import io.netty.handler.codec.http.HttpObject;
/* 11:   */ import io.netty.handler.codec.http.HttpResponse;
/* 12:   */ import io.netty.handler.codec.http.HttpResponseStatus;
/* 13:   */ import io.netty.handler.codec.http.LastHttpContent;
/* 14:   */ import io.netty.util.CharsetUtil;
/* 15:   */ import java.io.PrintStream;
/* 16:   */ import java.util.Iterator;
/* 17:   */ import java.util.List;
/* 18:   */ import java.util.Set;
/* 19:   */ 
/* 20:   */ public class HttpUploadClientHandler
/* 21:   */   extends SimpleChannelInboundHandler<HttpObject>
/* 22:   */ {
/* 23:   */   private boolean readingChunks;
/* 24:   */   
/* 25:   */   public void messageReceived(ChannelHandlerContext ctx, HttpObject msg)
/* 26:   */   {
/* 27:22 */     if ((msg instanceof HttpResponse))
/* 28:   */     {
/* 29:23 */       HttpResponse response = (HttpResponse)msg;
/* 30:   */       
/* 31:25 */       System.err.println("STATUS: " + response.status());
/* 32:26 */       System.err.println("VERSION: " + response.protocolVersion());
/* 33:28 */       if (!response.headers().isEmpty())
/* 34:   */       {
/* 35:   */         Iterator localIterator2;
/* 36:29 */         for (Iterator localIterator1 = response.headers().names().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 37:   */         {
/* 38:29 */           CharSequence name = (CharSequence)localIterator1.next();
/* 39:30 */           localIterator2 = response.headers().getAll(name).iterator(); continue;CharSequence value = (CharSequence)localIterator2.next();
/* 40:31 */           System.err.println("HEADER: " + name + " = " + value);
/* 41:   */         }
/* 42:   */       }
/* 43:36 */       if ((response.status().code() == 200) && (HttpHeaderUtil.isTransferEncodingChunked(response)))
/* 44:   */       {
/* 45:37 */         this.readingChunks = true;
/* 46:38 */         System.err.println("CHUNKED CONTENT {");
/* 47:   */       }
/* 48:   */       else
/* 49:   */       {
/* 50:40 */         System.err.println("CONTENT {");
/* 51:   */       }
/* 52:   */     }
/* 53:43 */     if ((msg instanceof HttpContent))
/* 54:   */     {
/* 55:44 */       HttpContent chunk = (HttpContent)msg;
/* 56:45 */       System.err.println(chunk.content().toString(CharsetUtil.UTF_8));
/* 57:47 */       if ((chunk instanceof LastHttpContent))
/* 58:   */       {
/* 59:48 */         if (this.readingChunks) {
/* 60:49 */           System.err.println("} END OF CHUNKED CONTENT");
/* 61:   */         } else {
/* 62:51 */           System.err.println("} END OF CONTENT");
/* 63:   */         }
/* 64:53 */         this.readingChunks = false;
/* 65:   */       }
/* 66:   */       else
/* 67:   */       {
/* 68:55 */         System.err.println(chunk.content().toString(CharsetUtil.UTF_8));
/* 69:   */       }
/* 70:   */     }
/* 71:   */   }
/* 72:   */   
/* 73:   */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 74:   */   {
/* 75:62 */     cause.printStackTrace();
/* 76:63 */     ctx.channel().close();
/* 77:   */   }
/* 78:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.upload.HttpUploadClientHandler
 * JD-Core Version:    0.7.0.1
 */