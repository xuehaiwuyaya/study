/*  1:   */ package bhz.mina.helloworld;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import org.apache.mina.core.service.IoHandlerAdapter;
/*  5:   */ import org.apache.mina.core.session.IdleStatus;
/*  6:   */ import org.apache.mina.core.session.IoSession;
/*  7:   */ 
/*  8:   */ public class TimeServerHandler
/*  9:   */   extends IoHandlerAdapter
/* 10:   */ {
/* 11:   */   public void sessionCreated(IoSession session)
/* 12:   */   {
/* 13:19 */     System.out.println(session.getRemoteAddress());
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void exceptionCaught(IoSession session, Throwable cause)
/* 17:   */     throws Exception
/* 18:   */   {
/* 19:27 */     cause.printStackTrace();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void messageReceived(IoSession session, Object message)
/* 23:   */     throws Exception
/* 24:   */   {
/* 25:36 */     String strMsg = message.toString();
/* 26:37 */     if (strMsg.trim().equalsIgnoreCase("quit"))
/* 27:   */     {
/* 28:38 */       session.close(true);
/* 29:39 */       return;
/* 30:   */     }
/* 31:42 */     session.write("Hi Client");
/* 32:   */     
/* 33:44 */     System.out.println("Message written : " + strMsg);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void sessionIdle(IoSession session, IdleStatus status)
/* 37:   */     throws Exception
/* 38:   */   {
/* 39:52 */     System.out.println("IDLE" + session.getIdleCount(status));
/* 40:   */   }
/* 41:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.mina.helloworld.TimeServerHandler
 * JD-Core Version:    0.7.0.1
 */