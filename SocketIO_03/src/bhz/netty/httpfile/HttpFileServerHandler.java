/*   1:    */ package bhz.netty.httpfile;
/*   2:    */ 
/*   3:    */ import io.netty.buffer.ByteBuf;
/*   4:    */ import io.netty.buffer.Unpooled;
/*   5:    */ import io.netty.channel.Channel;
/*   6:    */ import io.netty.channel.ChannelFuture;
/*   7:    */ import io.netty.channel.ChannelFutureListener;
/*   8:    */ import io.netty.channel.ChannelHandlerContext;
/*   9:    */ import io.netty.channel.ChannelProgressiveFuture;
/*  10:    */ import io.netty.channel.ChannelProgressiveFutureListener;
/*  11:    */ import io.netty.channel.SimpleChannelInboundHandler;
/*  12:    */ import io.netty.handler.codec.DecoderResult;
/*  13:    */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*  14:    */ import io.netty.handler.codec.http.DefaultHttpResponse;
/*  15:    */ import io.netty.handler.codec.http.FullHttpRequest;
/*  16:    */ import io.netty.handler.codec.http.FullHttpResponse;
/*  17:    */ import io.netty.handler.codec.http.HttpHeaderNames;
/*  18:    */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*  19:    */ import io.netty.handler.codec.http.HttpHeaderValues;
/*  20:    */ import io.netty.handler.codec.http.HttpHeaders;
/*  21:    */ import io.netty.handler.codec.http.HttpMethod;
/*  22:    */ import io.netty.handler.codec.http.HttpResponse;
/*  23:    */ import io.netty.handler.codec.http.HttpResponseStatus;
/*  24:    */ import io.netty.handler.codec.http.HttpVersion;
/*  25:    */ import io.netty.handler.codec.http.LastHttpContent;
/*  26:    */ import io.netty.handler.stream.ChunkedFile;
/*  27:    */ import io.netty.util.CharsetUtil;
/*  28:    */ import java.io.File;
/*  29:    */ import java.io.FileNotFoundException;
/*  30:    */ import java.io.PrintStream;
/*  31:    */ import java.io.RandomAccessFile;
/*  32:    */ import java.io.UnsupportedEncodingException;
/*  33:    */ import java.net.URLDecoder;
/*  34:    */ import java.util.regex.Matcher;
/*  35:    */ import java.util.regex.Pattern;
/*  36:    */ import javax.activation.MimetypesFileTypeMap;
/*  37:    */ 
/*  38:    */ public class HttpFileServerHandler
/*  39:    */   extends SimpleChannelInboundHandler<FullHttpRequest>
/*  40:    */ {
/*  41:    */   private final String url;
/*  42:    */   
/*  43:    */   public HttpFileServerHandler(String url)
/*  44:    */   {
/*  45: 51 */     this.url = url;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request)
/*  49:    */     throws Exception
/*  50:    */   {
/*  51: 57 */     if (!request.decoderResult().isSuccess())
/*  52:    */     {
/*  53: 59 */       sendError(ctx, HttpResponseStatus.BAD_REQUEST);
/*  54: 60 */       return;
/*  55:    */     }
/*  56: 63 */     if (request.method() != HttpMethod.GET)
/*  57:    */     {
/*  58: 65 */       sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
/*  59: 66 */       return;
/*  60:    */     }
/*  61: 69 */     String uri = request.uri();
/*  62:    */     
/*  63: 71 */     String path = sanitizeUri(uri);
/*  64: 73 */     if (path == null)
/*  65:    */     {
/*  66: 75 */       sendError(ctx, HttpResponseStatus.FORBIDDEN);
/*  67: 76 */       return;
/*  68:    */     }
/*  69: 79 */     File file = new File(path);
/*  70: 81 */     if ((file.isHidden()) || (!file.exists()))
/*  71:    */     {
/*  72: 83 */       sendError(ctx, HttpResponseStatus.NOT_FOUND);
/*  73: 84 */       return;
/*  74:    */     }
/*  75: 87 */     if (file.isDirectory())
/*  76:    */     {
/*  77: 88 */       if (uri.endsWith("/")) {
/*  78: 90 */         sendListing(ctx, file);
/*  79:    */       } else {
/*  80: 93 */         sendRedirect(ctx, uri + '/');
/*  81:    */       }
/*  82: 95 */       return;
/*  83:    */     }
/*  84: 98 */     if (!file.isFile())
/*  85:    */     {
/*  86:100 */       sendError(ctx, HttpResponseStatus.FORBIDDEN);
/*  87:101 */       return;
/*  88:    */     }
/*  89:105 */     RandomAccessFile randomAccessFile = null;
/*  90:    */     try
/*  91:    */     {
/*  92:107 */       randomAccessFile = new RandomAccessFile(file, "r");
/*  93:    */     }
/*  94:    */     catch (FileNotFoundException fnfe)
/*  95:    */     {
/*  96:110 */       sendError(ctx, HttpResponseStatus.NOT_FOUND);
/*  97:111 */       return;
/*  98:    */     }
/*  99:115 */     long fileLength = randomAccessFile.length();
/* 100:    */     
/* 101:117 */     HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
/* 102:    */     
/* 103:119 */     HttpHeaderUtil.setContentLength(response, fileLength);
/* 104:    */     
/* 105:121 */     setContentTypeHeader(response, file);
/* 106:123 */     if (HttpHeaderUtil.isKeepAlive(request)) {
/* 107:124 */       response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
/* 108:    */     }
/* 109:127 */     ctx.write(response);
/* 110:    */     
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:132 */     ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0L, fileLength, 8192), ctx.newProgressivePromise());
/* 115:    */     
/* 116:134 */     sendFileFuture.addListener(new ChannelProgressiveFutureListener()
/* 117:    */     {
/* 118:    */       public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
/* 119:    */       {
/* 120:137 */         if (total < 0L) {
/* 121:138 */           System.err.println("Transfer progress: " + progress);
/* 122:    */         } else {
/* 123:140 */           System.err.println("Transfer progress: " + progress + " / " + total);
/* 124:    */         }
/* 125:    */       }
/* 126:    */       
/* 127:    */       public void operationComplete(ChannelProgressiveFuture future)
/* 128:    */         throws Exception
/* 129:    */       {
/* 130:145 */         System.out.println("Transfer complete.");
/* 131:    */       }
/* 132:149 */     });
/* 133:150 */     ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
/* 134:152 */     if (!HttpHeaderUtil.isKeepAlive(request)) {
/* 135:153 */       lastContentFuture.addListener(ChannelFutureListener.CLOSE);
/* 136:    */     }
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 140:    */     throws Exception
/* 141:    */   {
/* 142:160 */     if (ctx.channel().isActive())
/* 143:    */     {
/* 144:161 */       sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
/* 145:162 */       ctx.close();
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:167 */   private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
/* 150:    */   
/* 151:    */   private String sanitizeUri(String uri)
/* 152:    */   {
/* 153:    */     try
/* 154:    */     {
/* 155:178 */       uri = URLDecoder.decode(uri, "UTF-8");
/* 156:    */     }
/* 157:    */     catch (UnsupportedEncodingException e)
/* 158:    */     {
/* 159:    */       try
/* 160:    */       {
/* 161:182 */         uri = URLDecoder.decode(uri, "ISO-8859-1");
/* 162:    */       }
/* 163:    */       catch (UnsupportedEncodingException e1)
/* 164:    */       {
/* 165:185 */         throw new Error();
/* 166:    */       }
/* 167:    */     }
/* 168:190 */     if (!uri.startsWith(this.url)) {
/* 169:191 */       return null;
/* 170:    */     }
/* 171:194 */     if (!uri.startsWith("/")) {
/* 172:195 */       return null;
/* 173:    */     }
/* 174:198 */     uri = uri.replace('/', File.separatorChar);
/* 175:200 */     if ((uri.contains(File.separator + '.')) || 
/* 176:201 */       (uri.contains('.' + File.separator)) || (uri.startsWith(".")) || 
/* 177:202 */       (uri.endsWith(".")) || (INSECURE_URI.matcher(uri).matches())) {
/* 178:203 */       return null;
/* 179:    */     }
/* 180:206 */     return System.getProperty("user.dir") + File.separator + uri;
/* 181:    */   }
/* 182:    */   
/* 183:210 */   private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
/* 184:    */   
/* 185:    */   private static void sendListing(ChannelHandlerContext ctx, File dir)
/* 186:    */   {
/* 187:214 */     FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
/* 188:    */     
/* 189:216 */     response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
/* 190:    */     
/* 191:218 */     StringBuilder ret = new StringBuilder();
/* 192:219 */     String dirPath = dir.getPath();
/* 193:220 */     ret.append("<!DOCTYPE html>\r\n");
/* 194:221 */     ret.append("<html><head><title>");
/* 195:222 */     ret.append(dirPath);
/* 196:223 */     ret.append(" 目录：");
/* 197:224 */     ret.append("</title></head><body>\r\n");
/* 198:225 */     ret.append("<h3>");
/* 199:226 */     ret.append(dirPath).append(" 目录：");
/* 200:227 */     ret.append("</h3>\r\n");
/* 201:228 */     ret.append("<ul>");
/* 202:229 */     ret.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
/* 203:232 */     for (File f : dir.listFiles()) {
/* 204:234 */       if ((!f.isHidden()) && (f.canRead()))
/* 205:    */       {
/* 206:237 */         String name = f.getName();
/* 207:239 */         if (ALLOWED_FILE_NAME.matcher(name).matches())
/* 208:    */         {
/* 209:243 */           ret.append("<li>链接：<a href=\"");
/* 210:244 */           ret.append(name);
/* 211:245 */           ret.append("\">");
/* 212:246 */           ret.append(name);
/* 213:247 */           ret.append("</a></li>\r\n");
/* 214:    */         }
/* 215:    */       }
/* 216:    */     }
/* 217:249 */     ret.append("</ul></body></html>\r\n");
/* 218:    */     
/* 219:251 */     ByteBuf buffer = Unpooled.copiedBuffer(ret, CharsetUtil.UTF_8);
/* 220:    */     
/* 221:253 */     response.content().writeBytes(buffer);
/* 222:    */     
/* 223:255 */     buffer.release();
/* 224:    */     
/* 225:257 */     ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
/* 226:    */   }
/* 227:    */   
/* 228:    */   private static void sendRedirect(ChannelHandlerContext ctx, String newUri)
/* 229:    */   {
/* 230:263 */     FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
/* 231:    */     
/* 232:265 */     response.headers().set(HttpHeaderNames.LOCATION, newUri);
/* 233:    */     
/* 234:267 */     ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
/* 235:    */   }
/* 236:    */   
/* 237:    */   private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status)
/* 238:    */   {
/* 239:273 */     FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
/* 240:    */     
/* 241:275 */     response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
/* 242:    */     
/* 243:277 */     ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
/* 244:    */   }
/* 245:    */   
/* 246:    */   private static void setContentTypeHeader(HttpResponse response, File file)
/* 247:    */   {
/* 248:282 */     MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
/* 249:283 */     response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
/* 250:    */   }
/* 251:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.httpfile.HttpFileServerHandler
 * JD-Core Version:    0.7.0.1
 */