/*   1:    */ package bhz.utils;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ public class HttpCallerConfig
/*   7:    */   implements Serializable
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 1L;
/*  10:    */   public static final String DEFAULT_CONFIG_CHARSET = "UTF-8";
/*  11:    */   public static final int DEFAULT_CONFIG_CONN_TIMEOUT = 30000;
/*  12:    */   public static final int DEFAULT_CONFIG_READ_TIMEOUT = 30000;
/*  13:    */   public static final int DEFAULT_CONFIG_MAX_BUFFER_SIZE = 500;
/*  14:    */   private String url;
/*  15:    */   private String method;
/*  16: 42 */   private String charset = "UTF-8";
/*  17: 45 */   private int connTimeout = 30000;
/*  18: 48 */   private int readTimeout = 30000;
/*  19: 51 */   private int maxBufferSize = 500;
/*  20:    */   private Map<String, String> params;
/*  21:    */   private boolean isStream;
/*  22:    */   
/*  23:    */   public String getUrl()
/*  24:    */   {
/*  25: 65 */     return this.url;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setUrl(String url)
/*  29:    */   {
/*  30: 74 */     this.url = url;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getMethod()
/*  34:    */   {
/*  35: 83 */     return this.method;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setMethod(String method)
/*  39:    */   {
/*  40: 92 */     this.method = method;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getCharset()
/*  44:    */   {
/*  45:101 */     return this.charset;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setCharset(String charset)
/*  49:    */   {
/*  50:110 */     this.charset = charset;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int getConnTimeout()
/*  54:    */   {
/*  55:119 */     return this.connTimeout;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setConnTimeout(int connTimeout)
/*  59:    */   {
/*  60:128 */     this.connTimeout = connTimeout;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getReadTimeout()
/*  64:    */   {
/*  65:137 */     return this.readTimeout;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setReadTimeout(int readTimeout)
/*  69:    */   {
/*  70:146 */     this.readTimeout = readTimeout;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getMaxBufferSize()
/*  74:    */   {
/*  75:155 */     return this.maxBufferSize;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setMaxBufferSize(int maxBufferSize)
/*  79:    */   {
/*  80:164 */     this.maxBufferSize = maxBufferSize;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Map<String, String> getParams()
/*  84:    */   {
/*  85:173 */     return this.params;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setParams(Map<String, String> params)
/*  89:    */   {
/*  90:182 */     this.params = params;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean isStream()
/*  94:    */   {
/*  95:186 */     return this.isStream;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setStream(boolean isStream)
/*  99:    */   {
/* 100:190 */     this.isStream = isStream;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String toString()
/* 104:    */   {
/* 105:202 */     return 
/* 106:    */     
/* 107:204 */       "HttpCallerConfig [url=" + this.url + ", method=" + this.method + ", charset=" + this.charset + ", connTimeout=" + this.connTimeout + ", readTimeout=" + this.readTimeout + ", maxBufferSize=" + this.maxBufferSize + ", params=" + (this.params == null ? null : Integer.valueOf(this.params.size())) + "]";
/* 108:    */   }
/* 109:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.HttpCallerConfig
 * JD-Core Version:    0.7.0.1
 */