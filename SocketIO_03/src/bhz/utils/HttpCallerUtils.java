/*   1:    */ package bhz.utils;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ public final class HttpCallerUtils
/*   7:    */ {
/*   8:    */   public static String request(String url, Map<String, String> params)
/*   9:    */     throws IOException
/*  10:    */   {
/*  11: 36 */     HttpCaller c = new HttpCaller(url, "POST", params);
/*  12: 37 */     return (String)c.request(String.class);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public static String get(String url, Map<String, String> params)
/*  16:    */     throws IOException
/*  17:    */   {
/*  18: 50 */     HttpCaller c = new HttpCaller(url, "GET", params);
/*  19: 51 */     return (String)c.request(String.class);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static byte[] getStream(String url, Map<String, String> params)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 55 */     HttpCaller c = new HttpCaller(url, "GET", true, params);
/*  26: 56 */     return (byte[])c.request(Byte.class);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static String post(String url, Map<String, String> params)
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 69 */     HttpCaller c = new HttpCaller(url, "POST", params);
/*  33: 70 */     return (String)c.request(String.class);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static byte[] postStream(String url, Map<String, String> params)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 74 */     HttpCaller c = new HttpCaller(url, "POST", true, params);
/*  40: 75 */     return (byte[])c.request(Byte.class);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static String put(String url, Map<String, String> params)
/*  44:    */     throws IOException
/*  45:    */   {
/*  46: 89 */     HttpCaller c = new HttpCaller(url, "PUT", params);
/*  47: 90 */     return (String)c.request(String.class);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static String delete(String url, Map<String, String> params)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53:103 */     HttpCaller c = new HttpCaller(url, "DELETE", params);
/*  54:104 */     return (String)c.request(String.class);
/*  55:    */   }
/*  56:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.HttpCallerUtils
 * JD-Core Version:    0.7.0.1
 */