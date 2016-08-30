/*  1:   */ package bhz.netty.runtime;
/*  2:   */ 
/*  3:   */ import io.netty.channel.ChannelHandlerAdapter;
/*  4:   */ import io.netty.channel.ChannelHandlerContext;
/*  5:   */ import io.netty.util.ReferenceCountUtil;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ 
/*  8:   */ public class ClientHandler
/*  9:   */   extends ChannelHandlerAdapter
/* 10:   */ {
/* 11:   */   public void channelActive(ChannelHandlerContext ctx)
/* 12:   */     throws Exception
/* 13:   */   {}
/* 14:   */   
/* 15:   */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/* 16:   */     throws Exception
/* 17:   */   {
/* 18:   */     try
/* 19:   */     {
/* 20:17 */       Response resp = (Response)msg;
/* 21:18 */       System.out.println("Client : " + resp.getId() + ", " + resp.getName() + ", " + resp.getResponseMessage());
/* 22:   */     }
/* 23:   */     finally
/* 24:   */     {
/* 25:20 */       ReferenceCountUtil.release(msg);
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void channelReadComplete(ChannelHandlerContext ctx)
/* 30:   */     throws Exception
/* 31:   */   {}
/* 32:   */   
/* 33:   */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 34:   */     throws Exception
/* 35:   */   {
/* 36:31 */     ctx.close();
/* 37:   */   }
/* 38:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.runtime.ClientHandler
 * JD-Core Version:    0.7.0.1
 */