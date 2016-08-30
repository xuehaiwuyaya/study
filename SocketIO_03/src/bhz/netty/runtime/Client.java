/*   1:    */ package bhz.netty.runtime;
/*   2:    */ 
/*   3:    */ import io.netty.bootstrap.Bootstrap;
/*   4:    */ import io.netty.channel.Channel;
/*   5:    */ import io.netty.channel.ChannelFuture;
/*   6:    */ import io.netty.channel.ChannelHandler;
/*   7:    */ import io.netty.channel.ChannelInitializer;
/*   8:    */ import io.netty.channel.ChannelPipeline;
/*   9:    */ import io.netty.channel.EventLoopGroup;
/*  10:    */ import io.netty.channel.nio.NioEventLoopGroup;
/*  11:    */ import io.netty.channel.socket.SocketChannel;
/*  12:    */ import io.netty.channel.socket.nio.NioSocketChannel;
/*  13:    */ import io.netty.handler.logging.LogLevel;
/*  14:    */ import io.netty.handler.logging.LoggingHandler;
/*  15:    */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*  16:    */ import java.io.PrintStream;
/*  17:    */ import java.util.concurrent.TimeUnit;
/*  18:    */ 
/*  19:    */ public class Client
/*  20:    */ {
/*  21:    */   private EventLoopGroup group;
/*  22:    */   private Bootstrap b;
/*  23:    */   private ChannelFuture cf;
/*  24:    */   
/*  25:    */   private static class SingletonHolder
/*  26:    */   {
/*  27: 23 */     static final Client instance = new Client(null);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static Client getInstance()
/*  31:    */   {
/*  32: 27 */     return SingletonHolder.instance;
/*  33:    */   }
/*  34:    */   
/*  35:    */   private Client()
/*  36:    */   {
/*  37: 35 */     this.group = new NioEventLoopGroup();
/*  38: 36 */     this.b = new Bootstrap();
/*  39:    */     
/*  40:    */ 
/*  41: 39 */     ((Bootstrap)((Bootstrap)((Bootstrap)this.b.group(this.group)).channel(NioSocketChannel.class)).handler(new LoggingHandler(LogLevel.INFO)))
/*  42: 40 */       .handler(new ChannelInitializer()
/*  43:    */       {
/*  44:    */         protected void initChannel(SocketChannel sc)
/*  45:    */           throws Exception
/*  46:    */         {
/*  47: 43 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingDecoder() });
/*  48: 44 */           sc.pipeline().addLast(new ChannelHandler[] { MarshallingCodeCFactory.buildMarshallingEncoder() });
/*  49:    */           
/*  50: 46 */           sc.pipeline().addLast(new ChannelHandler[] { new ReadTimeoutHandler(5) });
/*  51: 47 */           sc.pipeline().addLast(new ChannelHandler[] { new ClientHandler() });
/*  52:    */         }
/*  53:    */       });
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void connect()
/*  57:    */   {
/*  58:    */     try
/*  59:    */     {
/*  60: 54 */       this.cf = this.b.connect("127.0.0.1", 8765).sync();
/*  61: 55 */       System.out.println("远程服务器已经连接, 可以进行数据交换..");
/*  62:    */     }
/*  63:    */     catch (Exception e)
/*  64:    */     {
/*  65: 57 */       e.printStackTrace();
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public ChannelFuture getChannelFuture()
/*  70:    */   {
/*  71: 63 */     if (this.cf == null) {
/*  72: 64 */       connect();
/*  73:    */     }
/*  74: 66 */     if (!this.cf.channel().isActive()) {
/*  75: 67 */       connect();
/*  76:    */     }
/*  77: 70 */     return this.cf;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static void main(String[] args)
/*  81:    */     throws Exception
/*  82:    */   {
/*  83: 74 */     Client c = getInstance();
/*  84:    */     
/*  85:    */ 
/*  86: 77 */     ChannelFuture cf = c.getChannelFuture();
/*  87: 78 */     for (int i = 1; i <= 3; i++)
/*  88:    */     {
/*  89: 79 */       Request request = new Request();
/*  90: 80 */       request.setId(i);
/*  91: 81 */       request.setName("pro" + i);
/*  92: 82 */       request.setRequestMessage("数据信息" + i);
/*  93: 83 */       cf.channel().writeAndFlush(request);
/*  94: 84 */       TimeUnit.SECONDS.sleep(4L);
/*  95:    */     }
/*  96: 87 */     cf.channel().closeFuture().sync();
/*  97:    */     
/*  98:    */ 
/*  99: 90 */     new Thread(new Runnable()
/* 100:    */     {
/* 101:    */       public void run()
/* 102:    */       {
/* 103:    */         try
/* 104:    */         {
/* 105: 94 */           System.out.println("进入子线程...");
/* 106: 95 */           ChannelFuture cf = Client.this.getChannelFuture();
/* 107: 96 */           System.out.println(cf.channel().isActive());
/* 108: 97 */           System.out.println(cf.channel().isOpen());
/* 109:    */           
/* 110:    */ 
/* 111:100 */           Request request = new Request();
/* 112:101 */           request.setId("4");
/* 113:102 */           request.setName("pro4");
/* 114:103 */           request.setRequestMessage("数据信息4");
/* 115:104 */           cf.channel().writeAndFlush(request);
/* 116:105 */           cf.channel().closeFuture().sync();
/* 117:106 */           System.out.println("子线程结束.");
/* 118:    */         }
/* 119:    */         catch (InterruptedException e)
/* 120:    */         {
/* 121:108 */           e.printStackTrace();
/* 122:    */         }
/* 123:    */       }
/* 124:112 */     }).start();
/* 125:113 */     System.out.println("断开连接,主线程结束..");
/* 126:    */   }
/* 127:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.runtime.Client
 * JD-Core Version:    0.7.0.1
 */