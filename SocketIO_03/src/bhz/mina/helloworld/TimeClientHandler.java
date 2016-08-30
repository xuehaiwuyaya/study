/*  1:   */ package bhz.mina.helloworld;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import org.apache.mina.core.service.IoHandlerAdapter;
/*  5:   */ import org.apache.mina.core.session.IoSession;
/*  6:   */ 
/*  7:   */ public class TimeClientHandler
/*  8:   */   extends IoHandlerAdapter
/*  9:   */ {
/* 10:   */   public void messageReceived(IoSession session, Object message)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:12 */     String content = message.toString();
/* 14:13 */     System.out.println("client receive a message is :" + content);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void messageSent(IoSession session, Object message)
/* 18:   */     throws Exception
/* 19:   */   {
/* 20:20 */     System.out.println("message - > : " + message);
/* 21:   */   }
/* 22:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.mina.helloworld.TimeClientHandler
 * JD-Core Version:    0.7.0.1
 */