/*  1:   */ package bhz.netty.heartBeat;
/*  2:   */ 
/*  3:   */ import io.netty.channel.ChannelFuture;
/*  4:   */ import io.netty.channel.ChannelFutureListener;
/*  5:   */ import io.netty.channel.ChannelHandlerAdapter;
/*  6:   */ import io.netty.channel.ChannelHandlerContext;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import java.util.HashMap;
/*  9:   */ 
/* 10:   */ public class ServerHeartBeatHandler
/* 11:   */   extends ChannelHandlerAdapter
/* 12:   */ {
/* 13:13 */   private static HashMap<String, String> AUTH_IP_MAP = new HashMap();
/* 14:   */   private static final String SUCCESS_KEY = "auth_success_key";
/* 15:   */   
/* 16:   */   static
/* 17:   */   {
/* 18:17 */     AUTH_IP_MAP.put("192.168.0.100", "1234");
/* 19:   */   }
/* 20:   */   
/* 21:   */   private boolean auth(ChannelHandlerContext ctx, Object msg)
/* 22:   */   {
/* 23:22 */     String[] ret = ((String)msg).split(",");
/* 24:23 */     String auth = (String)AUTH_IP_MAP.get(ret[0]);
/* 25:24 */     if ((auth != null) && (auth.equals(ret[1])))
/* 26:   */     {
/* 27:25 */       ctx.writeAndFlush("auth_success_key");
/* 28:26 */       return true;
/* 29:   */     }
/* 30:28 */     ctx.writeAndFlush("auth failure !").addListener(ChannelFutureListener.CLOSE);
/* 31:29 */     return false;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/* 35:   */     throws Exception
/* 36:   */   {
/* 37:35 */     if ((msg instanceof String))
/* 38:   */     {
/* 39:36 */       auth(ctx, msg);
/* 40:   */     }
/* 41:37 */     else if ((msg instanceof RequestInfo))
/* 42:   */     {
/* 43:39 */       RequestInfo info = (RequestInfo)msg;
/* 44:40 */       System.out.println("--------------------------------------------");
/* 45:41 */       System.out.println("当前主机ip为: " + info.getIp());
/* 46:42 */       System.out.println("当前主机cpu情况: ");
/* 47:43 */       HashMap<String, Object> cpu = info.getCpuPercMap();
/* 48:44 */       System.out.println("总使用率: " + cpu.get("combined"));
/* 49:45 */       System.out.println("用户使用率: " + cpu.get("user"));
/* 50:46 */       System.out.println("系统使用率: " + cpu.get("sys"));
/* 51:47 */       System.out.println("等待率: " + cpu.get("wait"));
/* 52:48 */       System.out.println("空闲率: " + cpu.get("idle"));
/* 53:   */       
/* 54:50 */       System.out.println("当前主机memory情况: ");
/* 55:51 */       HashMap<String, Object> memory = info.getMemoryMap();
/* 56:52 */       System.out.println("内存总量: " + memory.get("total"));
/* 57:53 */       System.out.println("当前内存使用量: " + memory.get("used"));
/* 58:54 */       System.out.println("当前内存剩余量: " + memory.get("free"));
/* 59:55 */       System.out.println("--------------------------------------------");
/* 60:   */       
/* 61:57 */       ctx.writeAndFlush("info received!");
/* 62:   */     }
/* 63:   */     else
/* 64:   */     {
/* 65:59 */       ctx.writeAndFlush("connect failure!").addListener(ChannelFutureListener.CLOSE);
/* 66:   */     }
/* 67:   */   }
/* 68:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.heartBeat.ServerHeartBeatHandler
 * JD-Core Version:    0.7.0.1
 */