/*  1:   */ package bhz.netty.runtime;
/*  2:   */ 
/*  3:   */ import io.netty.channel.ChannelHandlerAdapter;
/*  4:   */ import io.netty.channel.ChannelHandlerContext;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ 
/*  7:   */ public class ServerHandler
/*  8:   */   extends ChannelHandlerAdapter
/*  9:   */ {
/* 10:   */   public void channelActive(ChannelHandlerContext ctx)
/* 11:   */     throws Exception
/* 12:   */   {}
/* 13:   */   
/* 14:   */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/* 15:   */     throws Exception
/* 16:   */   {
/* 17:15 */     Request request = (Request)msg;
/* 18:16 */     System.out.println("Server : " + request.getId() + ", " + request.getName() + ", " + request.getRequestMessage());
/* 19:17 */     Response response = new Response();
/* 20:18 */     response.setId(request.getId());
/* 21:19 */     response.setName("response" + request.getId());
/* 22:20 */     response.setResponseMessage("响应内容" + request.getId());
/* 23:21 */     ctx.writeAndFlush(response);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void channelReadComplete(ChannelHandlerContext ctx)
/* 27:   */     throws Exception
/* 28:   */   {}
/* 29:   */   
/* 30:   */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 31:   */     throws Exception
/* 32:   */   {
/* 33:31 */     ctx.close();
/* 34:   */   }
/* 35:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.runtime.ServerHandler
 * JD-Core Version:    0.7.0.1
 */