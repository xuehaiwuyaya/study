/*  1:   */ package bhz.netty.runtime;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ 
/*  5:   */ public class Request
/*  6:   */   implements Serializable
/*  7:   */ {
/*  8:   */   private static final long SerialVersionUID = 1L;
/*  9:   */   private String id;
/* 10:   */   private String name;
/* 11:   */   private String requestMessage;
/* 12:   */   
/* 13:   */   public String getId()
/* 14:   */   {
/* 15:14 */     return this.id;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setId(String id)
/* 19:   */   {
/* 20:17 */     this.id = id;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getName()
/* 24:   */   {
/* 25:20 */     return this.name;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setName(String name)
/* 29:   */   {
/* 30:23 */     this.name = name;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String getRequestMessage()
/* 34:   */   {
/* 35:26 */     return this.requestMessage;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void setRequestMessage(String requestMessage)
/* 39:   */   {
/* 40:29 */     this.requestMessage = requestMessage;
/* 41:   */   }
/* 42:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.runtime.Request
 * JD-Core Version:    0.7.0.1
 */