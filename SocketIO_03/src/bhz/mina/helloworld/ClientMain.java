/*  1:   */ package bhz.mina.helloworld;
/*  2:   */ 
/*  3:   */ import java.net.InetSocketAddress;
/*  4:   */ import java.nio.charset.Charset;
/*  5:   */ import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
/*  6:   */ import org.apache.mina.core.future.CloseFuture;
/*  7:   */ import org.apache.mina.core.future.ConnectFuture;
/*  8:   */ import org.apache.mina.core.session.IoSession;
/*  9:   */ import org.apache.mina.filter.codec.ProtocolCodecFilter;
/* 10:   */ import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
/* 11:   */ import org.apache.mina.filter.logging.LoggingFilter;
/* 12:   */ import org.apache.mina.transport.socket.nio.NioSocketConnector;
/* 13:   */ 
/* 14:   */ public class ClientMain
/* 15:   */ {
/* 16:   */   public static void main(String[] args)
/* 17:   */   {
/* 18:21 */     NioSocketConnector connnector = new NioSocketConnector();
/* 19:   */     
/* 20:23 */     connnector.getFilterChain()
/* 21:24 */       .addLast("logger", new LoggingFilter());
/* 22:   */     
/* 23:26 */     connnector.getFilterChain()
/* 24:27 */       .addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
/* 25:   */     
/* 26:   */ 
/* 27:30 */     connnector.setConnectTimeoutCheckInterval(30L);
/* 28:31 */     connnector.setHandler(new TimeClientHandler());
/* 29:   */     
/* 30:   */ 
/* 31:34 */     ConnectFuture cf = connnector.connect(new InetSocketAddress("192.168.0.100", 6488));
/* 32:   */     
/* 33:   */ 
/* 34:37 */     cf.awaitUninterruptibly();
/* 35:   */     
/* 36:39 */     cf.getSession().write("Hi Server!");
/* 37:40 */     cf.getSession().write("quit");
/* 38:   */     
/* 39:   */ 
/* 40:43 */     cf.getSession().getCloseFuture().awaitUninterruptibly();
/* 41:44 */     connnector.dispose();
/* 42:   */   }
/* 43:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.mina.helloworld.ClientMain
 * JD-Core Version:    0.7.0.1
 */