/*  1:   */ package bhz.netty.httpfile;
/*  2:   */ 
/*  3:   */ import bhz.utils.HttpCallerUtils;
/*  4:   */ import java.io.File;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.util.HashMap;
/*  7:   */ import java.util.Map;
/*  8:   */ 
/*  9:   */ public class Test
/* 10:   */ {
/* 11:   */   public static void main(String[] args)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:15 */     Map<String, String> params = new HashMap();
/* 15:16 */     byte[] ret = HttpCallerUtils.getStream("http://192.168.1.111:8765/images/a.doc", params);
/* 16:   */     
/* 17:   */ 
/* 18:   */ 
/* 19:20 */     String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + "a.doc";
/* 20:21 */     FileOutputStream fos = new FileOutputStream(writePath);
/* 21:22 */     fos.write(ret);
/* 22:23 */     fos.close();
/* 23:   */   }
/* 24:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.httpfile.Test
 * JD-Core Version:    0.7.0.1
 */