/*   1:    */ package bhz.netty.heartBeat;
/*   2:    */ 
/*   3:    */ import io.netty.channel.ChannelHandlerAdapter;
/*   4:    */ import io.netty.channel.ChannelHandlerContext;
/*   5:    */ import io.netty.util.ReferenceCountUtil;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.net.InetAddress;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.concurrent.Executors;
/*  10:    */ import java.util.concurrent.ScheduledExecutorService;
/*  11:    */ import java.util.concurrent.ScheduledFuture;
/*  12:    */ import java.util.concurrent.TimeUnit;
/*  13:    */ import org.hyperic.sigar.CpuPerc;
/*  14:    */ import org.hyperic.sigar.Mem;
/*  15:    */ import org.hyperic.sigar.Sigar;
/*  16:    */ 
/*  17:    */ public class ClienHeartBeattHandler
/*  18:    */   extends ChannelHandlerAdapter
/*  19:    */ {
/*  20: 24 */   private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
/*  21:    */   private ScheduledFuture<?> heartBeat;
/*  22:    */   private InetAddress addr;
/*  23:    */   private static final String SUCCESS_KEY = "auth_success_key";
/*  24:    */   
/*  25:    */   public void channelActive(ChannelHandlerContext ctx)
/*  26:    */     throws Exception
/*  27:    */   {
/*  28: 34 */     this.addr = InetAddress.getLocalHost();
/*  29: 35 */     String ip = this.addr.getHostAddress();
/*  30: 36 */     String key = "1234";
/*  31: 37 */     String auth = ip + "," + key;
/*  32: 38 */     ctx.writeAndFlush(auth);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*  36:    */     throws Exception
/*  37:    */   {
/*  38:    */     try
/*  39:    */     {
/*  40: 44 */       if ((msg instanceof String))
/*  41:    */       {
/*  42: 45 */         String ret = (String)msg;
/*  43: 46 */         if ("auth_success_key".equals(ret))
/*  44:    */         {
/*  45: 48 */           this.heartBeat = this.scheduler.scheduleWithFixedDelay(new HeartBeatTask(ctx), 0L, 2L, TimeUnit.SECONDS);
/*  46: 49 */           System.out.println(msg);
/*  47:    */         }
/*  48:    */         else
/*  49:    */         {
/*  50: 52 */           System.out.println(msg);
/*  51:    */         }
/*  52:    */       }
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56: 56 */       ReferenceCountUtil.release(msg);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   private class HeartBeatTask
/*  61:    */     implements Runnable
/*  62:    */   {
/*  63:    */     private final ChannelHandlerContext ctx;
/*  64:    */     
/*  65:    */     public HeartBeatTask(ChannelHandlerContext ctx)
/*  66:    */     {
/*  67: 64 */       this.ctx = ctx;
/*  68:    */     }
/*  69:    */     
/*  70:    */     public void run()
/*  71:    */     {
/*  72:    */       try
/*  73:    */       {
/*  74: 70 */         RequestInfo info = new RequestInfo();
/*  75:    */         
/*  76: 72 */         info.setIp(ClienHeartBeattHandler.this.addr.getHostAddress());
/*  77: 73 */         Sigar sigar = new Sigar();
/*  78:    */         
/*  79: 75 */         CpuPerc cpuPerc = sigar.getCpuPerc();
/*  80: 76 */         HashMap<String, Object> cpuPercMap = new HashMap();
/*  81: 77 */         cpuPercMap.put("combined", Double.valueOf(cpuPerc.getCombined()));
/*  82: 78 */         cpuPercMap.put("user", Double.valueOf(cpuPerc.getUser()));
/*  83: 79 */         cpuPercMap.put("sys", Double.valueOf(cpuPerc.getSys()));
/*  84: 80 */         cpuPercMap.put("wait", Double.valueOf(cpuPerc.getWait()));
/*  85: 81 */         cpuPercMap.put("idle", Double.valueOf(cpuPerc.getIdle()));
/*  86:    */         
/*  87: 83 */         Mem mem = sigar.getMem();
/*  88: 84 */         HashMap<String, Object> memoryMap = new HashMap();
/*  89: 85 */         memoryMap.put("total", Long.valueOf(mem.getTotal() / 1024L));
/*  90: 86 */         memoryMap.put("used", Long.valueOf(mem.getUsed() / 1024L));
/*  91: 87 */         memoryMap.put("free", Long.valueOf(mem.getFree() / 1024L));
/*  92: 88 */         info.setCpuPercMap(cpuPercMap);
/*  93: 89 */         info.setMemoryMap(memoryMap);
/*  94: 90 */         this.ctx.writeAndFlush(info);
/*  95:    */       }
/*  96:    */       catch (Exception e)
/*  97:    */       {
/*  98: 93 */         e.printStackTrace();
/*  99:    */       }
/* 100:    */     }
/* 101:    */     
/* 102:    */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 103:    */       throws Exception
/* 104:    */     {
/* 105: 98 */       cause.printStackTrace();
/* 106: 99 */       if (ClienHeartBeattHandler.this.heartBeat != null)
/* 107:    */       {
/* 108:100 */         ClienHeartBeattHandler.this.heartBeat.cancel(true);
/* 109:101 */         ClienHeartBeattHandler.this.heartBeat = null;
/* 110:    */       }
/* 111:103 */       ctx.fireExceptionCaught(cause);
/* 112:    */     }
/* 113:    */   }
/* 114:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.heartBeat.ClienHeartBeattHandler
 * JD-Core Version:    0.7.0.1
 */