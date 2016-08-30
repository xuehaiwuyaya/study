/*  1:   */ package bhz.mina.helloworld;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.InetSocketAddress;
/*  5:   */ import java.nio.charset.Charset;
/*  6:   */ import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
/*  7:   */ import org.apache.mina.core.service.IoAcceptor;
/*  8:   */ import org.apache.mina.core.session.IdleStatus;
/*  9:   */ import org.apache.mina.core.session.IoSessionConfig;
/* 10:   */ import org.apache.mina.filter.codec.ProtocolCodecFilter;
/* 11:   */ import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
/* 12:   */ import org.apache.mina.filter.logging.LoggingFilter;
/* 13:   */ import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
/* 14:   */ 
/* 15:   */ public class ServerMain
/* 16:   */ {
/* 17:   */   private static final int PORT = 6488;
/* 18:   */   
/* 19:   */   public static void main(String[] args)
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:26 */     IoAcceptor acceptor = new NioSocketAcceptor();
/* 23:   */     
/* 24:28 */     acceptor.getSessionConfig().setReadBufferSize(2048);
/* 25:29 */     acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
/* 26:   */     
/* 27:   */ 
/* 28:32 */     acceptor.getFilterChain()
/* 29:33 */       .addLast("logger", new LoggingFilter());
/* 30:   */     
/* 31:   */ 
/* 32:36 */     acceptor.getFilterChain()
/* 33:37 */       .addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
/* 34:   */     
/* 35:   */ 
/* 36:40 */     acceptor.setHandler(new TimeServerHandler());
/* 37:   */     
/* 38:   */ 
/* 39:43 */     acceptor.bind(new InetSocketAddress(6488));
/* 40:   */   }
/* 41:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.mina.helloworld.ServerMain
 * JD-Core Version:    0.7.0.1
 */