/*  1:   */ package bhz.netty.heartBeat;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class RequestInfo
/*  7:   */   implements Serializable
/*  8:   */ {
/*  9:   */   private String ip;
/* 10:   */   private HashMap<String, Object> cpuPercMap;
/* 11:   */   private HashMap<String, Object> memoryMap;
/* 12:   */   
/* 13:   */   public String getIp()
/* 14:   */   {
/* 15:14 */     return this.ip;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setIp(String ip)
/* 19:   */   {
/* 20:17 */     this.ip = ip;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public HashMap<String, Object> getCpuPercMap()
/* 24:   */   {
/* 25:20 */     return this.cpuPercMap;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setCpuPercMap(HashMap<String, Object> cpuPercMap)
/* 29:   */   {
/* 30:23 */     this.cpuPercMap = cpuPercMap;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public HashMap<String, Object> getMemoryMap()
/* 34:   */   {
/* 35:26 */     return this.memoryMap;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void setMemoryMap(HashMap<String, Object> memoryMap)
/* 39:   */   {
/* 40:29 */     this.memoryMap = memoryMap;
/* 41:   */   }
/* 42:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.heartBeat.RequestInfo
 * JD-Core Version:    0.7.0.1
 */