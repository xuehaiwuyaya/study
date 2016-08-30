/*  1:   */ package bhz.netty.http;
/*  2:   */ 
/*  3:   */ import io.netty.buffer.ByteBuf;
/*  4:   */ import io.netty.buffer.Unpooled;
/*  5:   */ import io.netty.channel.ChannelFuture;
/*  6:   */ import io.netty.channel.ChannelFutureListener;
/*  7:   */ import io.netty.channel.ChannelHandlerAdapter;
/*  8:   */ import io.netty.channel.ChannelHandlerContext;
/*  9:   */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/* 10:   */ import io.netty.handler.codec.http.FullHttpResponse;
/* 11:   */ import io.netty.handler.codec.http.HttpHeaderNames;
/* 12:   */ import io.netty.handler.codec.http.HttpHeaderUtil;
/* 13:   */ import io.netty.handler.codec.http.HttpHeaderValues;
/* 14:   */ import io.netty.handler.codec.http.HttpHeaders;
/* 15:   */ import io.netty.handler.codec.http.HttpRequest;
/* 16:   */ import io.netty.handler.codec.http.HttpResponseStatus;
/* 17:   */ import io.netty.handler.codec.http.HttpVersion;
/* 18:   */ 
/* 19:   */ public class HttpHelloWorldServerHandler
/* 20:   */   extends ChannelHandlerAdapter
/* 21:   */ {
/* 22:18 */   private static final byte[] CONTENT = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
/* 23:   */   
/* 24:   */   public void channelReadComplete(ChannelHandlerContext ctx)
/* 25:   */   {
/* 26:22 */     ctx.flush();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/* 30:   */   {
/* 31:27 */     if ((msg instanceof HttpRequest))
/* 32:   */     {
/* 33:28 */       HttpRequest req = (HttpRequest)msg;
/* 34:30 */       if (HttpHeaderUtil.is100ContinueExpected(req)) {
/* 35:31 */         ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
/* 36:   */       }
/* 37:33 */       boolean keepAlive = HttpHeaderUtil.isKeepAlive(req);
/* 38:34 */       FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(CONTENT));
/* 39:35 */       response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
/* 40:36 */       response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
/* 41:38 */       if (!keepAlive)
/* 42:   */       {
/* 43:39 */         ctx.write(response).addListener(ChannelFutureListener.CLOSE);
/* 44:   */       }
/* 45:   */       else
/* 46:   */       {
/* 47:41 */         response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
/* 48:42 */         ctx.write(response);
/* 49:   */       }
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 54:   */   {
/* 55:49 */     cause.printStackTrace();
/* 56:50 */     ctx.close();
/* 57:   */   }
/* 58:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.http.HttpHelloWorldServerHandler
 * JD-Core Version:    0.7.0.1
 */