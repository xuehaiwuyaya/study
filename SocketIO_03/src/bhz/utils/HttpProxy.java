/*   1:    */ package bhz.utils;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import org.apache.http.Consts;
/*  10:    */ import org.apache.http.HttpEntity;
/*  11:    */ import org.apache.http.NameValuePair;
/*  12:    */ import org.apache.http.StatusLine;
/*  13:    */ import org.apache.http.client.HttpClient;
/*  14:    */ import org.apache.http.client.config.RequestConfig;
/*  15:    */ import org.apache.http.client.config.RequestConfig.Builder;
/*  16:    */ import org.apache.http.client.entity.EntityBuilder;
/*  17:    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*  18:    */ import org.apache.http.client.methods.HttpGet;
/*  19:    */ import org.apache.http.client.methods.HttpPost;
/*  20:    */ import org.apache.http.entity.ContentType;
/*  21:    */ import org.apache.http.entity.StringEntity;
/*  22:    */ import org.apache.http.entity.mime.MultipartEntityBuilder;
/*  23:    */ import org.apache.http.entity.mime.content.FileBody;
/*  24:    */ import org.apache.http.entity.mime.content.StringBody;
/*  25:    */ import org.apache.http.impl.client.CloseableHttpClient;
/*  26:    */ import org.apache.http.impl.client.HttpClientBuilder;
/*  27:    */ import org.apache.http.impl.client.HttpClients;
/*  28:    */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*  29:    */ import org.apache.http.message.BasicNameValuePair;
/*  30:    */ import org.apache.http.util.EntityUtils;
/*  31:    */ 
/*  32:    */ public class HttpProxy
/*  33:    */ {
/*  34:    */   private static CloseableHttpClient httpClient;
/*  35:    */   private static final String CONTENT_TYPE_JSON = "application/json";
/*  36:    */   
/*  37:    */   private static class SingletonHolder
/*  38:    */   {
/*  39: 32 */     static final HttpProxy instance = new HttpProxy();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static HttpProxy getInstance()
/*  43:    */   {
/*  44: 36 */     return SingletonHolder.instance;
/*  45:    */   }
/*  46:    */   
/*  47:    */   static
/*  48:    */   {
/*  49: 44 */     PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
/*  50: 45 */     cm.setMaxTotal(HttpProxyConfig.MAX_TOTAL_CONNECTIONS);
/*  51: 46 */     cm.setDefaultMaxPerRoute(HttpProxyConfig.MAX_ROUTE_CONNECTIONS);
/*  52:    */     
/*  53:    */ 
/*  54: 49 */     RequestConfig requestConfig = RequestConfig.custom()
/*  55: 50 */       .setSocketTimeout(HttpProxyConfig.CONNECT_TIMEOUT)
/*  56: 51 */       .setConnectTimeout(HttpProxyConfig.CONNECT_TIMEOUT)
/*  57: 52 */       .build();
/*  58:    */     
/*  59:    */ 
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65: 60 */     httpClient = HttpClients.custom()
/*  66: 61 */       .setDefaultRequestConfig(requestConfig)
/*  67: 62 */       .setConnectionManager(cm)
/*  68: 63 */       .build();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static HttpClient getHttpClient()
/*  72:    */   {
/*  73: 67 */     return httpClient;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static byte[] get4Stream(String requestUrl)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79: 78 */     byte[] ret = null;
/*  80: 79 */     HttpGet httpGet = new HttpGet(requestUrl);
/*  81: 80 */     CloseableHttpResponse response = httpClient.execute(httpGet);
/*  82:    */     try
/*  83:    */     {
/*  84: 82 */       HttpEntity entity = response.getEntity();
/*  85: 83 */       if (entity != null)
/*  86:    */       {
/*  87: 84 */         long len = entity.getContentLength();
/*  88: 85 */         ret = EntityUtils.toByteArray(entity);
/*  89: 86 */         EntityUtils.consume(entity);
/*  90:    */       }
/*  91: 88 */       return ret;
/*  92:    */     }
/*  93:    */     finally
/*  94:    */     {
/*  95: 90 */       response.close();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static String get4String(String requestUrl)
/* 100:    */     throws Exception
/* 101:    */   {
/* 102:102 */     String ret = null;
/* 103:103 */     HttpGet httpGet = new HttpGet(requestUrl);
/* 104:104 */     CloseableHttpResponse response = httpClient.execute(httpGet);
/* 105:    */     try
/* 106:    */     {
/* 107:106 */       HttpEntity entity = response.getEntity();
/* 108:107 */       if (entity != null)
/* 109:    */       {
/* 110:108 */         long len = entity.getContentLength();
/* 111:109 */         ret = EntityUtils.toString(entity);
/* 112:110 */         EntityUtils.consume(entity);
/* 113:    */       }
/* 114:112 */       return ret;
/* 115:    */     }
/* 116:    */     finally
/* 117:    */     {
/* 118:114 */       response.close();
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static String post(String requestUrl, String requestContent)
/* 123:    */     throws IOException
/* 124:    */   {
/* 125:127 */     StringEntity requestEntity = new StringEntity(requestContent, Consts.UTF_8);
/* 126:128 */     return execute(requestUrl, requestEntity);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static String postJson(String requestUrl, String jsonContent)
/* 130:    */     throws IOException
/* 131:    */   {
/* 132:140 */     StringEntity requestEntity = new StringEntity(jsonContent, Consts.UTF_8);
/* 133:141 */     requestEntity.setContentEncoding("UTF-8");
/* 134:142 */     requestEntity.setContentType("application/json");
/* 135:143 */     return execute(requestUrl, requestEntity);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static String post(String requestUrl, Map<String, String> params)
/* 139:    */     throws IOException
/* 140:    */   {
/* 141:155 */     List<NameValuePair> nvps = new ArrayList();
/* 142:156 */     if (params != null) {
/* 143:157 */       for (Map.Entry<String, String> entry : params.entrySet()) {
/* 144:158 */         nvps.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
/* 145:    */       }
/* 146:    */     }
/* 147:161 */     EntityBuilder builder = EntityBuilder.create();
/* 148:162 */     builder.setParameters(nvps);
/* 149:163 */     HttpEntity httpEntity = builder.build();
/* 150:164 */     return execute(requestUrl, httpEntity);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public static String upload(String requestUrl, String localFile, String username, String password)
/* 154:    */     throws IOException
/* 155:    */   {
/* 156:178 */     HttpPost httpPost = new HttpPost(requestUrl);
/* 157:    */     
/* 158:180 */     FileBody fileBody = new FileBody(new File(localFile));
/* 159:181 */     StringBody usernameInp = new StringBody(username, ContentType.create("text/plain", Consts.UTF_8));
/* 160:182 */     StringBody passwordInp = new StringBody(password, ContentType.create("text/plain", Consts.UTF_8));
/* 161:183 */     HttpEntity httpEntity = MultipartEntityBuilder.create()
/* 162:    */     
/* 163:185 */       .addPart("file", fileBody)
/* 164:    */       
/* 165:187 */       .addPart("username", usernameInp)
/* 166:188 */       .addPart("password", passwordInp)
/* 167:189 */       .build();
/* 168:190 */     return execute(requestUrl, httpEntity);
/* 169:    */   }
/* 170:    */   
/* 171:    */   private static String execute(String requestUrl, HttpEntity httpEntity)
/* 172:    */     throws IOException
/* 173:    */   {
/* 174:202 */     String result = null;
/* 175:203 */     HttpPost httpPost = new HttpPost(requestUrl);
/* 176:204 */     httpPost.setEntity(httpEntity);
/* 177:    */     try
/* 178:    */     {
/* 179:206 */       CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
/* 180:    */       try
/* 181:    */       {
/* 182:208 */         HttpEntity entity = httpResponse.getEntity();
/* 183:210 */         if ((httpResponse.getStatusLine().getReasonPhrase().equals("OK")) && (httpResponse.getStatusLine().getStatusCode() == 200)) {
/* 184:211 */           result = EntityUtils.toString(entity, "UTF-8");
/* 185:    */         }
/* 186:214 */         EntityUtils.consume(entity);
/* 187:    */       }
/* 188:    */       finally
/* 189:    */       {
/* 190:216 */         if (httpResponse != null) {
/* 191:217 */           httpResponse.close();
/* 192:    */         }
/* 193:    */       }
/* 194:    */     }
/* 195:    */     finally
/* 196:    */     {
/* 197:221 */       if (httpPost != null) {
/* 198:222 */         httpPost.releaseConnection();
/* 199:    */       }
/* 200:    */     }
/* 201:225 */     return result;
/* 202:    */   }
/* 203:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.HttpProxy
 * JD-Core Version:    0.7.0.1
 */