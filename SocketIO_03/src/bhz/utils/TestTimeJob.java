/*  1:   */ package bhz.utils;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.Executors;
/*  4:   */ import java.util.concurrent.ScheduledExecutorService;
/*  5:   */ import java.util.concurrent.ScheduledFuture;
/*  6:   */ import java.util.concurrent.TimeUnit;
/*  7:   */ 
/*  8:   */ public class TestTimeJob
/*  9:   */ {
/* 10:   */   public static void main(String[] args)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:21 */     Temp command = new Temp();
/* 14:   */     
/* 15:23 */     ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
/* 16:24 */     ScheduledFuture<?> scheduleTask = scheduler.scheduleWithFixedDelay(command, 2L, 3L, TimeUnit.SECONDS);
/* 17:   */   }
/* 18:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.TestTimeJob
 * JD-Core Version:    0.7.0.1
 */