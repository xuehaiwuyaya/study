/*   1:    */ package bhz.netty.upload;
/*   2:    */ 
/*   3:    */ import io.netty.buffer.ByteBuf;
/*   4:    */ import io.netty.buffer.Unpooled;
/*   5:    */ import io.netty.channel.Channel;
/*   6:    */ import io.netty.channel.ChannelFuture;
/*   7:    */ import io.netty.channel.ChannelFutureListener;
/*   8:    */ import io.netty.channel.ChannelHandlerContext;
/*   9:    */ import io.netty.channel.SimpleChannelInboundHandler;
/*  10:    */ import io.netty.handler.codec.http.Cookie;
/*  11:    */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*  12:    */ import io.netty.handler.codec.http.FullHttpResponse;
/*  13:    */ import io.netty.handler.codec.http.HttpContent;
/*  14:    */ import io.netty.handler.codec.http.HttpHeaderNames;
/*  15:    */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*  16:    */ import io.netty.handler.codec.http.HttpHeaderValues;
/*  17:    */ import io.netty.handler.codec.http.HttpHeaders;
/*  18:    */ import io.netty.handler.codec.http.HttpMethod;
/*  19:    */ import io.netty.handler.codec.http.HttpObject;
/*  20:    */ import io.netty.handler.codec.http.HttpRequest;
/*  21:    */ import io.netty.handler.codec.http.HttpResponseStatus;
/*  22:    */ import io.netty.handler.codec.http.HttpVersion;
/*  23:    */ import io.netty.handler.codec.http.LastHttpContent;
/*  24:    */ import io.netty.handler.codec.http.QueryStringDecoder;
/*  25:    */ import io.netty.handler.codec.http.ServerCookieDecoder;
/*  26:    */ import io.netty.handler.codec.http.ServerCookieEncoder;
/*  27:    */ import io.netty.handler.codec.http.multipart.Attribute;
/*  28:    */ import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
/*  29:    */ import io.netty.handler.codec.http.multipart.FileUpload;
/*  30:    */ import io.netty.handler.codec.http.multipart.HttpDataFactory;
/*  31:    */ import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
/*  32:    */ import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
/*  33:    */ import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
/*  34:    */ import io.netty.handler.codec.http.multipart.InterfaceHttpData;
/*  35:    */ import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
/*  36:    */ import io.netty.util.CharsetUtil;
/*  37:    */ import java.io.File;
/*  38:    */ import java.io.IOException;
/*  39:    */ import java.io.PrintStream;
/*  40:    */ import java.net.URI;
/*  41:    */ import java.util.Collections;
/*  42:    */ import java.util.Iterator;
/*  43:    */ import java.util.List;
/*  44:    */ import java.util.Map;
/*  45:    */ import java.util.Map.Entry;
/*  46:    */ import java.util.Set;
/*  47:    */ import java.util.logging.Level;
/*  48:    */ import java.util.logging.Logger;
/*  49:    */ 
/*  50:    */ public class HttpUploadServerHandler
/*  51:    */   extends SimpleChannelInboundHandler<HttpObject>
/*  52:    */ {
/*  53: 56 */   private static final Logger logger = Logger.getLogger(HttpUploadServerHandler.class.getName());
/*  54:    */   private HttpRequest request;
/*  55:    */   private boolean readingChunks;
/*  56: 62 */   private final StringBuilder responseContent = new StringBuilder();
/*  57: 65 */   private static final HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
/*  58:    */   private HttpPostRequestDecoder decoder;
/*  59:    */   
/*  60:    */   static
/*  61:    */   {
/*  62: 70 */     io.netty.handler.codec.http.multipart.DiskFileUpload.deleteOnExitTemporaryFile = true;
/*  63:    */     
/*  64:    */ 
/*  65: 73 */     io.netty.handler.codec.http.multipart.DiskFileUpload.baseDirectory = "D:" + File.separatorChar + "aa";
/*  66: 74 */     io.netty.handler.codec.http.multipart.DiskAttribute.deleteOnExitTemporaryFile = true;
/*  67:    */     
/*  68: 76 */     io.netty.handler.codec.http.multipart.DiskAttribute.baseDirectory = "D:" + File.separatorChar + "aa";
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void channelInactive(ChannelHandlerContext ctx)
/*  72:    */     throws Exception
/*  73:    */   {
/*  74: 81 */     if (this.decoder != null) {
/*  75: 82 */       this.decoder.cleanFiles();
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void messageReceived(ChannelHandlerContext ctx, HttpObject msg)
/*  80:    */     throws Exception
/*  81:    */   {
/*  82: 88 */     if ((msg instanceof HttpRequest))
/*  83:    */     {
/*  84: 89 */       HttpRequest request = this.request = (HttpRequest)msg;
/*  85: 90 */       URI uri = new URI(request.uri());
/*  86: 91 */       if (!uri.getPath().startsWith("/form"))
/*  87:    */       {
/*  88: 93 */         writeMenu(ctx);
/*  89: 94 */         return;
/*  90:    */       }
/*  91: 96 */       this.responseContent.setLength(0);
/*  92: 97 */       this.responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
/*  93: 98 */       this.responseContent.append("===================================\r\n");
/*  94:    */       
/*  95:100 */       this.responseContent.append("VERSION: " + request.protocolVersion().text() + "\r\n");
/*  96:    */       
/*  97:102 */       this.responseContent.append("REQUEST_URI: " + request.uri() + "\r\n\r\n");
/*  98:103 */       this.responseContent.append("\r\n\r\n");
/*  99:106 */       for (Map.Entry<CharSequence, CharSequence> entry : request.headers()) {
/* 100:107 */         this.responseContent.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
/* 101:    */       }
/* 102:109 */       this.responseContent.append("\r\n\r\n");
/* 103:    */       
/* 104:    */ 
/* 105:    */ 
/* 106:113 */       String value = (String)request.headers().getAndConvert(HttpHeaderNames.COOKIE);
/* 107:    */       Set<Cookie> cookies;
/* 108:    */       Set<Cookie> cookies;
/* 109:114 */       if (value == null) {
/* 110:115 */         cookies = Collections.emptySet();
/* 111:    */       } else {
/* 112:117 */         cookies = ServerCookieDecoder.decode(value);
/* 113:    */       }
/* 114:119 */       for (Cookie cookie : cookies) {
/* 115:120 */         this.responseContent.append("COOKIE: " + cookie + "\r\n");
/* 116:    */       }
/* 117:122 */       this.responseContent.append("\r\n\r\n");
/* 118:    */       
/* 119:124 */       QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
/* 120:125 */       Object uriAttributes = decoderQuery.parameters();
/* 121:    */       Iterator localIterator4;
/* 122:126 */       for (Iterator localIterator3 = ((Map)uriAttributes).entrySet().iterator(); localIterator3.hasNext(); localIterator4.hasNext())
/* 123:    */       {
/* 124:126 */         Map.Entry<String, List<String>> attr = (Map.Entry)localIterator3.next();
/* 125:127 */         localIterator4 = ((List)attr.getValue()).iterator(); continue;String attrVal = (String)localIterator4.next();
/* 126:128 */         this.responseContent.append("URI: " + (String)attr.getKey() + '=' + attrVal + "\r\n");
/* 127:    */       }
/* 128:131 */       this.responseContent.append("\r\n\r\n");
/* 129:134 */       if (request.method().equals(HttpMethod.GET))
/* 130:    */       {
/* 131:137 */         this.responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
/* 132:    */         
/* 133:139 */         return;
/* 134:    */       }
/* 135:    */       try
/* 136:    */       {
/* 137:142 */         this.decoder = new HttpPostRequestDecoder(factory, request);
/* 138:    */       }
/* 139:    */       catch (HttpPostRequestDecoder.ErrorDataDecoderException e1)
/* 140:    */       {
/* 141:144 */         e1.printStackTrace();
/* 142:145 */         this.responseContent.append(e1.getMessage());
/* 143:146 */         writeResponse(ctx.channel());
/* 144:147 */         ctx.channel().close();
/* 145:148 */         return;
/* 146:    */       }
/* 147:151 */       this.readingChunks = HttpHeaderUtil.isTransferEncodingChunked(request);
/* 148:152 */       this.responseContent.append("Is Chunked: " + this.readingChunks + "\r\n");
/* 149:153 */       this.responseContent.append("IsMultipart: " + this.decoder.isMultipart() + "\r\n");
/* 150:154 */       if (this.readingChunks)
/* 151:    */       {
/* 152:156 */         this.responseContent.append("Chunks: ");
/* 153:157 */         this.readingChunks = true;
/* 154:    */       }
/* 155:    */     }
/* 156:163 */     if (this.decoder != null)
/* 157:    */     {
/* 158:164 */       if ((msg instanceof HttpContent))
/* 159:    */       {
/* 160:166 */         HttpContent chunk = (HttpContent)msg;
/* 161:    */         try
/* 162:    */         {
/* 163:168 */           this.decoder.offer(chunk);
/* 164:    */         }
/* 165:    */         catch (HttpPostRequestDecoder.ErrorDataDecoderException e1)
/* 166:    */         {
/* 167:170 */           e1.printStackTrace();
/* 168:171 */           this.responseContent.append(e1.getMessage());
/* 169:172 */           writeResponse(ctx.channel());
/* 170:173 */           ctx.channel().close();
/* 171:174 */           return;
/* 172:    */         }
/* 173:176 */         this.responseContent.append('o');
/* 174:    */         
/* 175:    */ 
/* 176:179 */         readHttpDataChunkByChunk();
/* 177:181 */         if ((chunk instanceof LastHttpContent))
/* 178:    */         {
/* 179:182 */           writeResponse(ctx.channel());
/* 180:183 */           this.readingChunks = false;
/* 181:    */           
/* 182:185 */           reset();
/* 183:    */         }
/* 184:    */       }
/* 185:    */     }
/* 186:    */     else {
/* 187:189 */       writeResponse(ctx.channel());
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   private void reset()
/* 192:    */   {
/* 193:194 */     this.request = null;
/* 194:    */     
/* 195:    */ 
/* 196:197 */     this.decoder.destroy();
/* 197:198 */     this.decoder = null;
/* 198:    */   }
/* 199:    */   
/* 200:    */   private void readHttpDataChunkByChunk()
/* 201:    */     throws Exception
/* 202:    */   {
/* 203:    */     try
/* 204:    */     {
/* 205:206 */       while (this.decoder.hasNext())
/* 206:    */       {
/* 207:207 */         InterfaceHttpData data = this.decoder.next();
/* 208:208 */         if (data != null) {
/* 209:    */           try
/* 210:    */           {
/* 211:211 */             writeHttpData(data);
/* 212:    */           }
/* 213:    */           finally
/* 214:    */           {
/* 215:213 */             data.release();
/* 216:    */           }
/* 217:    */         }
/* 218:    */       }
/* 219:    */     }
/* 220:    */     catch (HttpPostRequestDecoder.EndOfDataDecoderException e1)
/* 221:    */     {
/* 222:219 */       this.responseContent.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
/* 223:    */     }
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void writeHttpData(InterfaceHttpData data)
/* 227:    */     throws Exception
/* 228:    */   {
/* 229:224 */     if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
/* 230:    */     {
/* 231:225 */       Attribute attribute = (Attribute)data;
/* 232:    */       try
/* 233:    */       {
/* 234:228 */         value = attribute.getValue();
/* 235:    */       }
/* 236:    */       catch (IOException e1)
/* 237:    */       {
/* 238:    */         String value;
/* 239:231 */         e1.printStackTrace();
/* 240:    */         
/* 241:233 */         this.responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + 
/* 242:234 */           attribute.getName() + " Error while reading value: " + e1.getMessage() + "\r\n"); return;
/* 243:    */       }
/* 244:    */       String value;
/* 245:237 */       if (value.length() > 100) {
/* 246:238 */         this.responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + 
/* 247:239 */           attribute.getName() + " data too long\r\n");
/* 248:    */       } else {
/* 249:241 */         this.responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + 
/* 250:242 */           attribute + "\r\n");
/* 251:    */       }
/* 252:    */     }
/* 253:    */     else
/* 254:    */     {
/* 255:245 */       this.responseContent.append("\r\n -----------start-------------\r\n");
/* 256:246 */       this.responseContent.append("\r\nBODY FileUpload: " + data.getHttpDataType().name() + ": " + data + 
/* 257:247 */         "\r\n");
/* 258:248 */       this.responseContent.append("\r\n ------------end------------\r\n");
/* 259:249 */       if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
/* 260:    */       {
/* 261:250 */         FileUpload fileUpload = (FileUpload)data;
/* 262:251 */         if (fileUpload.isCompleted())
/* 263:    */         {
/* 264:253 */           System.out.println("file name : " + fileUpload.getFilename());
/* 265:254 */           System.out.println("file length: " + fileUpload.length());
/* 266:255 */           System.out.println("file maxSize : " + fileUpload.getMaxSize());
/* 267:256 */           System.out.println("file path :" + fileUpload.getFile().getPath());
/* 268:257 */           System.out.println("file absolutepath :" + fileUpload.getFile().getAbsolutePath());
/* 269:258 */           System.out.println("parent path :" + fileUpload.getFile().getParentFile());
/* 270:260 */           if (fileUpload.length() < 10485760L)
/* 271:    */           {
/* 272:261 */             this.responseContent.append("\tContent of file\r\n");
/* 273:    */             
/* 274:    */ 
/* 275:    */ 
/* 276:    */ 
/* 277:    */ 
/* 278:    */ 
/* 279:268 */             this.responseContent.append("\r\n");
/* 280:    */           }
/* 281:    */           else
/* 282:    */           {
/* 283:270 */             this.responseContent.append("\tFile too long to be printed out:" + fileUpload.length() + "\r\n");
/* 284:    */           }
/* 285:274 */           fileUpload.renameTo(new File(fileUpload.getFile().getPath()));
/* 286:    */         }
/* 287:    */         else
/* 288:    */         {
/* 289:279 */           this.responseContent.append("\tFile to be continued but should not!\r\n");
/* 290:    */         }
/* 291:    */       }
/* 292:    */     }
/* 293:    */   }
/* 294:    */   
/* 295:    */   private void writeResponse(Channel channel)
/* 296:    */   {
/* 297:287 */     ByteBuf buf = Unpooled.copiedBuffer(this.responseContent.toString(), CharsetUtil.UTF_8);
/* 298:288 */     this.responseContent.setLength(0);
/* 299:    */     
/* 300:    */ 
/* 301:291 */     boolean close = (this.request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)) || (
/* 302:292 */       (this.request.protocolVersion().equals(HttpVersion.HTTP_1_0)) && 
/* 303:293 */       (!this.request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true)));
/* 304:    */     
/* 305:    */ 
/* 306:296 */     FullHttpResponse response = new DefaultFullHttpResponse(
/* 307:297 */       HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
/* 308:298 */     response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
/* 309:300 */     if (!close) {
/* 310:303 */       response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
/* 311:    */     }
/* 312:307 */     String value = (String)this.request.headers().getAndConvert(HttpHeaderNames.COOKIE);
/* 313:    */     Set<Cookie> cookies;
/* 314:    */     Set<Cookie> cookies;
/* 315:308 */     if (value == null) {
/* 316:309 */       cookies = Collections.emptySet();
/* 317:    */     } else {
/* 318:311 */       cookies = ServerCookieDecoder.decode(value);
/* 319:    */     }
/* 320:313 */     if (!cookies.isEmpty()) {
/* 321:315 */       for (Cookie cookie : cookies) {
/* 322:316 */         response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode(cookie));
/* 323:    */       }
/* 324:    */     }
/* 325:320 */     ChannelFuture future = channel.writeAndFlush(response);
/* 326:322 */     if (close) {
/* 327:323 */       future.addListener(ChannelFutureListener.CLOSE);
/* 328:    */     }
/* 329:    */   }
/* 330:    */   
/* 331:    */   private void writeMenu(ChannelHandlerContext ctx)
/* 332:    */   {
/* 333:330 */     this.responseContent.setLength(0);
/* 334:    */     
/* 335:    */ 
/* 336:333 */     this.responseContent.append("<html>");
/* 337:334 */     this.responseContent.append("<head>");
/* 338:335 */     this.responseContent.append("<title>Netty Test Form</title>\r\n");
/* 339:336 */     this.responseContent.append("</head>\r\n");
/* 340:337 */     this.responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
/* 341:    */     
/* 342:339 */     this.responseContent.append("<table border=\"0\">");
/* 343:340 */     this.responseContent.append("<tr>");
/* 344:341 */     this.responseContent.append("<td>");
/* 345:342 */     this.responseContent.append("<h1>Netty Test Form</h1>");
/* 346:343 */     this.responseContent.append("Choose one FORM");
/* 347:344 */     this.responseContent.append("</td>");
/* 348:345 */     this.responseContent.append("</tr>");
/* 349:346 */     this.responseContent.append("</table>\r\n");
/* 350:    */     
/* 351:    */ 
/* 352:349 */     this.responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
/* 353:350 */     this.responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
/* 354:351 */     this.responseContent.append("<input type=hidden name=getform value=\"GET\">");
/* 355:352 */     this.responseContent.append("<table border=\"0\">");
/* 356:353 */     this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
/* 357:354 */     this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
/* 358:355 */     this.responseContent
/* 359:356 */       .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
/* 360:357 */     this.responseContent.append("</td></tr>");
/* 361:358 */     this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
/* 362:359 */     this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
/* 363:360 */     this.responseContent.append("</table></FORM>\r\n");
/* 364:361 */     this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
/* 365:    */     
/* 366:    */ 
/* 367:364 */     this.responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
/* 368:365 */     this.responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
/* 369:366 */     this.responseContent.append("<input type=hidden name=getform value=\"POST\">");
/* 370:367 */     this.responseContent.append("<table border=\"0\">");
/* 371:368 */     this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
/* 372:369 */     this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
/* 373:370 */     this.responseContent
/* 374:371 */       .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
/* 375:372 */     this.responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> <input type=file name=\"myfile\">");
/* 376:    */     
/* 377:374 */     this.responseContent.append("</td></tr>");
/* 378:375 */     this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
/* 379:376 */     this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
/* 380:377 */     this.responseContent.append("</table></FORM>\r\n");
/* 381:378 */     this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
/* 382:    */     
/* 383:    */ 
/* 384:381 */     this.responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
/* 385:382 */     this.responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
/* 386:383 */     this.responseContent.append("<input type=hidden name=getform value=\"POST\">");
/* 387:384 */     this.responseContent.append("<table border=\"0\">");
/* 388:385 */     this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
/* 389:386 */     this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
/* 390:387 */     this.responseContent
/* 391:388 */       .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
/* 392:389 */     this.responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
/* 393:390 */     this.responseContent.append("</td></tr>");
/* 394:391 */     this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
/* 395:392 */     this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
/* 396:393 */     this.responseContent.append("</table></FORM>\r\n");
/* 397:394 */     this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
/* 398:    */     
/* 399:396 */     this.responseContent.append("</body>");
/* 400:397 */     this.responseContent.append("</html>");
/* 401:    */     
/* 402:399 */     ByteBuf buf = Unpooled.copiedBuffer(this.responseContent.toString(), CharsetUtil.UTF_8);
/* 403:    */     
/* 404:401 */     FullHttpResponse response = new DefaultFullHttpResponse(
/* 405:402 */       HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
/* 406:    */     
/* 407:404 */     response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
/* 408:405 */     response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
/* 409:    */     
/* 410:    */ 
/* 411:408 */     ctx.channel().writeAndFlush(response);
/* 412:    */   }
/* 413:    */   
/* 414:    */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/* 415:    */     throws Exception
/* 416:    */   {
/* 417:413 */     logger.log(Level.WARNING, this.responseContent.toString(), cause);
/* 418:414 */     ctx.channel().close();
/* 419:    */   }
/* 420:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.netty.upload.HttpUploadServerHandler
 * JD-Core Version:    0.7.0.1
 */