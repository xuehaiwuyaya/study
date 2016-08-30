/*  1:   */ package bhz.netty.heartBeat;
/*  2:   */ 
/*  3:   */ import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
/*  4:   */ import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
/*  5:   */ import io.netty.handler.codec.marshalling.MarshallerProvider;
/*  6:   */ import io.netty.handler.codec.marshalling.MarshallingDecoder;
/*  7:   */ import io.netty.handler.codec.marshalling.MarshallingEncoder;
/*  8:   */ import io.netty.handler.codec.marshalling.UnmarshallerProvider;
/*  9:   */ import org.jboss.marshalling.MarshallerFactory;
/* 10:   */ import org.jboss.marshalling.Marshalling;
/* 11:   */ import org.jboss.marshalling.MarshallingConfiguration;
/* 12:   */ 
/* 13:   */ public final class MarshallingCodeCFactory
/* 14:   */ {
/* 15:   */   public static MarshallingDecoder buildMarshallingDecoder()
/* 16:   */   {
/* 17:27 */     MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
/* 18:   */     
/* 19:29 */     MarshallingConfiguration configuration = new MarshallingConfiguration();
/* 20:30 */     configuration.setVersion(5);
/* 21:   */     
/* 22:32 */     UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
/* 23:   */     
/* 24:34 */     MarshallingDecoder decoder = new MarshallingDecoder(provider, 1048576);
/* 25:35 */     return decoder;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static MarshallingEncoder buildMarshallingEncoder()
/* 29:   */   {
/* 30:43 */     MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
/* 31:44 */     MarshallingConfiguration configuration = new MarshallingConfiguration();
/* 32:45 */     configuration.setVersion(5);
/* 33:46 */     MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
/* 34:   */     
/* 35:48 */     MarshallingEncoder encoder = new MarshallingEncoder(provider);
/* 36:49 */     return encoder;
/* 37:   */   }
/* 38:   */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.heartBeat.MarshallingCodeCFactory
 * JD-Core Version:    0.7.0.1
 */