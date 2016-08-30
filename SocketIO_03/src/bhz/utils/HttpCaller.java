/*   1:    */ package bhz.utils;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.InputStreamReader;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.OutputStreamWriter;
/*   9:    */ import java.net.HttpURLConnection;
/*  10:    */ import java.net.URL;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.Map.Entry;
/*  13:    */ 
/*  14:    */ public class HttpCaller
/*  15:    */ {
/*  16:    */   public static final String REQUEST_METHOD_GET = "GET";
/*  17:    */   public static final String REQUEST_METHOD_POST = "POST";
/*  18:    */   public static final String REQUEST_METHOD_PUT = "PUT";
/*  19:    */   public static final String REQUEST_METHOD_DELETE = "DELETE";
/*  20:    */   private HttpCallerConfig config;
/*  21:    */   
/*  22:    */   public HttpCaller(String url, String method)
/*  23:    */   {
/*  24: 51 */     this.config = new HttpCallerConfig();
/*  25: 52 */     this.config.setUrl(url);
/*  26: 53 */     this.config.setMethod(method);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public HttpCaller(String url, Map<String, String> params)
/*  30:    */   {
/*  31: 63 */     this.config = new HttpCallerConfig();
/*  32: 64 */     this.config.setUrl(url);
/*  33: 65 */     this.config.setMethod("POST");
/*  34: 66 */     this.config.setParams(params);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public HttpCaller(String url, String method, Map<String, String> params)
/*  38:    */   {
/*  39: 77 */     this.config = new HttpCallerConfig();
/*  40: 78 */     this.config.setUrl(url);
/*  41: 79 */     this.config.setMethod(method);
/*  42: 80 */     this.config.setParams(params);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public HttpCaller(String url, String method, boolean isStream, Map<String, String> params)
/*  46:    */   {
/*  47: 91 */     this.config = new HttpCallerConfig();
/*  48: 92 */     this.config.setUrl(url);
/*  49: 93 */     this.config.setMethod(method);
/*  50: 94 */     this.config.setStream(isStream);
/*  51: 95 */     this.config.setParams(params);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public HttpCaller(HttpCallerConfig config)
/*  55:    */   {
/*  56:104 */     this.config = config;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public <T> T request(Object T)
/*  60:    */     throws IOException
/*  61:    */   {
/*  62:116 */     URL targetUrl = new URL(this.config.getUrl());
/*  63:117 */     HttpURLConnection conn = (HttpURLConnection)targetUrl.openConnection();
/*  64:118 */     conn.setRequestMethod(this.config.getMethod());
/*  65:119 */     conn.setConnectTimeout(this.config.getConnTimeout());
/*  66:120 */     conn.setReadTimeout(this.config.getReadTimeout());
/*  67:    */     
/*  68:122 */     conn.setDoInput(true);
/*  69:123 */     conn.setDoOutput(true);
/*  70:124 */     conn.setUseCaches(false);
/*  71:125 */     conn.connect();
/*  72:126 */     write(conn);
/*  73:127 */     if (this.config.isStream()) {
/*  74:128 */       return readStream(conn);
/*  75:    */     }
/*  76:130 */     return read(conn);
/*  77:    */   }
/*  78:    */   
/*  79:    */   private byte[] readStream(HttpURLConnection conn)
/*  80:    */     throws IOException
/*  81:    */   {
/*  82:142 */     InputStream is = null;
/*  83:143 */     ByteArrayOutputStream bos = null;
/*  84:    */     try
/*  85:    */     {
/*  86:145 */       is = conn.getInputStream();
/*  87:146 */       byte[] buf = new byte[this.config.getMaxBufferSize()];
/*  88:147 */       int num = -1;
/*  89:148 */       bos = new ByteArrayOutputStream();
/*  90:149 */       while ((num = is.read(buf, 0, buf.length)) != -1) {
/*  91:150 */         bos.write(buf, 0, num);
/*  92:    */       }
/*  93:    */     }
/*  94:    */     finally
/*  95:    */     {
/*  96:154 */       if (bos != null) {
/*  97:155 */         bos.close();
/*  98:    */       }
/*  99:157 */       if (is != null) {
/* 100:158 */         is.close();
/* 101:    */       }
/* 102:    */     }
/* 103:161 */     return bos.toByteArray();
/* 104:    */   }
/* 105:    */   
/* 106:    */   private String read(HttpURLConnection conn)
/* 107:    */     throws IOException
/* 108:    */   {
/* 109:172 */     StringBuffer str = new StringBuffer();
/* 110:173 */     InputStream is = null;
/* 111:174 */     InputStreamReader reader = null;
/* 112:    */     try
/* 113:    */     {
/* 114:176 */       is = conn.getInputStream();
/* 115:177 */       reader = new InputStreamReader(is, this.config.getCharset());
/* 116:178 */       char[] buffer = new char[this.config.getMaxBufferSize()];
/* 117:179 */       int c = 0;
/* 118:180 */       while ((c = reader.read(buffer)) >= 0) {
/* 119:181 */         str.append(buffer, 0, c);
/* 120:    */       }
/* 121:    */     }
/* 122:    */     finally
/* 123:    */     {
/* 124:185 */       if (reader != null) {
/* 125:186 */         reader.close();
/* 126:    */       }
/* 127:188 */       if (is != null) {
/* 128:189 */         is.close();
/* 129:    */       }
/* 130:    */     }
/* 131:192 */     if (str.length() < 1) {
/* 132:193 */       return null;
/* 133:    */     }
/* 134:195 */     return str.toString();
/* 135:    */   }
/* 136:    */   
/* 137:    */   private void write(HttpURLConnection conn)
/* 138:    */     throws IOException
/* 139:    */   {
/* 140:206 */     Map<String, String> params = this.config.getParams();
/* 141:207 */     if ((params == null) || (params.isEmpty())) {
/* 142:208 */       return;
/* 143:    */     }
/* 144:210 */     OutputStream out = null;
/* 145:211 */     OutputStreamWriter writer = null;
/* 146:    */     try
/* 147:    */     {
/* 148:213 */       out = conn.getOutputStream();
/* 149:214 */       writer = new OutputStreamWriter(out, this.config.getCharset());
/* 150:215 */       for (Map.Entry<String, String> param : params.entrySet())
/* 151:    */       {
/* 152:216 */         writer.write("&");
/* 153:217 */         writer.write((String)param.getKey());
/* 154:218 */         writer.write("=");
/* 155:219 */         if (param.getValue() != null) {
/* 156:220 */           writer.write((String)param.getValue());
/* 157:    */         }
/* 158:    */       }
/* 159:    */     }
/* 160:    */     finally
/* 161:    */     {
/* 162:225 */       if (writer != null) {
/* 163:226 */         writer.close();
/* 164:    */       }
/* 165:228 */       if (out != null) {
/* 166:229 */         out.close();
/* 167:    */       }
/* 168:    */     }
/* 169:    */   }
/* 170:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.HttpCaller
 * JD-Core Version:    0.7.0.1
 */