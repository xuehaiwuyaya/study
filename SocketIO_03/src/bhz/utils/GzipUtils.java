/*  1:   */ package bhz.utils;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.io.ByteArrayOutputStream;
/*  5:   */ import java.io.File;
/*  6:   */ import java.io.FileInputStream;
/*  7:   */ import java.io.FileOutputStream;
/*  8:   */ import java.io.PrintStream;
/*  9:   */ import java.util.zip.GZIPInputStream;
/* 10:   */ import java.util.zip.GZIPOutputStream;
/* 11:   */ 
/* 12:   */ public class GzipUtils
/* 13:   */ {
/* 14:   */   public static byte[] gzip(byte[] data)
/* 15:   */     throws Exception
/* 16:   */   {
/* 17:14 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 18:15 */     GZIPOutputStream gzip = new GZIPOutputStream(bos);
/* 19:16 */     gzip.write(data);
/* 20:17 */     gzip.finish();
/* 21:18 */     gzip.close();
/* 22:19 */     byte[] ret = bos.toByteArray();
/* 23:20 */     bos.close();
/* 24:21 */     return ret;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static byte[] ungzip(byte[] data)
/* 28:   */     throws Exception
/* 29:   */   {
/* 30:25 */     ByteArrayInputStream bis = new ByteArrayInputStream(data);
/* 31:26 */     GZIPInputStream gzip = new GZIPInputStream(bis);
/* 32:27 */     byte[] buf = new byte[1024];
/* 33:28 */     int num = -1;
/* 34:29 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 35:30 */     while ((num = gzip.read(buf, 0, buf.length)) != -1) {
/* 36:31 */       bos.write(buf, 0, num);
/* 37:   */     }
/* 38:33 */     gzip.close();
/* 39:34 */     bis.close();
/* 40:35 */     byte[] ret = bos.toByteArray();
/* 41:36 */     bos.flush();
/* 42:37 */     bos.close();
/* 43:38 */     return ret;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public static void main(String[] args)
/* 47:   */     throws Exception
/* 48:   */   {
/* 49:44 */     String readPath = System.getProperty("user.dir") + File.separatorChar + "sources" + File.separatorChar + "006.jpg";
/* 50:45 */     File file = new File(readPath);
/* 51:46 */     FileInputStream in = new FileInputStream(file);
/* 52:47 */     byte[] data = new byte[in.available()];
/* 53:48 */     in.read(data);
/* 54:49 */     in.close();
/* 55:   */     
/* 56:51 */     System.out.println("文件原始大小:" + data.length);
/* 57:   */     
/* 58:   */ 
/* 59:54 */     byte[] ret1 = gzip(data);
/* 60:55 */     System.out.println("压缩之后大小:" + ret1.length);
/* 61:   */     
/* 62:57 */     byte[] ret2 = ungzip(ret1);
/* 63:58 */     System.out.println("还原之后大小:" + ret2.length);
/* 64:   */     
/* 65:   */ 
/* 66:61 */     String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + "006.jpg";
/* 67:62 */     FileOutputStream fos = new FileOutputStream(writePath);
/* 68:63 */     fos.write(ret2);
/* 69:64 */     fos.close();
/* 70:   */   }
/* 71:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.GzipUtils
 * JD-Core Version:    0.7.0.1
 */