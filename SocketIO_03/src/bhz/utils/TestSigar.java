/*   1:    */ package bhz.utils;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.net.InetAddress;
/*   5:    */ import java.net.UnknownHostException;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Properties;
/*   8:    */ import org.hyperic.sigar.CpuInfo;
/*   9:    */ import org.hyperic.sigar.CpuPerc;
/*  10:    */ import org.hyperic.sigar.FileSystem;
/*  11:    */ import org.hyperic.sigar.FileSystemUsage;
/*  12:    */ import org.hyperic.sigar.Mem;
/*  13:    */ import org.hyperic.sigar.NetInterfaceConfig;
/*  14:    */ import org.hyperic.sigar.NetInterfaceStat;
/*  15:    */ import org.hyperic.sigar.OperatingSystem;
/*  16:    */ import org.hyperic.sigar.Sigar;
/*  17:    */ import org.hyperic.sigar.SigarException;
/*  18:    */ import org.hyperic.sigar.Swap;
/*  19:    */ import org.hyperic.sigar.Who;
/*  20:    */ 
/*  21:    */ public class TestSigar
/*  22:    */ {
/*  23:    */   public static void main(String[] args)
/*  24:    */   {
/*  25:    */     try
/*  26:    */     {
/*  27: 27 */       property();
/*  28: 28 */       System.out.println("----------------------------------");
/*  29:    */       
/*  30: 30 */       cpu();
/*  31: 31 */       System.out.println("----------------------------------");
/*  32:    */       
/*  33: 33 */       memory();
/*  34: 34 */       System.out.println("----------------------------------");
/*  35:    */       
/*  36: 36 */       os();
/*  37: 37 */       System.out.println("----------------------------------");
/*  38:    */       
/*  39: 39 */       who();
/*  40: 40 */       System.out.println("----------------------------------");
/*  41:    */       
/*  42: 42 */       file();
/*  43: 43 */       System.out.println("----------------------------------");
/*  44:    */       
/*  45: 45 */       net();
/*  46: 46 */       System.out.println("----------------------------------");
/*  47:    */       
/*  48: 48 */       ethernet();
/*  49: 49 */       System.out.println("----------------------------------");
/*  50:    */     }
/*  51:    */     catch (Exception e1)
/*  52:    */     {
/*  53: 51 */       e1.printStackTrace();
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   private static void property()
/*  58:    */     throws UnknownHostException
/*  59:    */   {
/*  60: 56 */     Runtime r = Runtime.getRuntime();
/*  61: 57 */     Properties props = System.getProperties();
/*  62:    */     
/*  63: 59 */     InetAddress addr = InetAddress.getLocalHost();
/*  64: 60 */     String ip = addr.getHostAddress();
/*  65: 61 */     Map<String, String> map = System.getenv();
/*  66: 62 */     String userName = (String)map.get("USERNAME");
/*  67: 63 */     String computerName = (String)map.get("COMPUTERNAME");
/*  68: 64 */     String userDomain = (String)map.get("USERDOMAIN");
/*  69: 65 */     System.out.println("用户名:    " + userName);
/*  70: 66 */     System.out.println("计算机名:    " + computerName);
/*  71: 67 */     System.out.println("计算机域名:    " + userDomain);
/*  72: 68 */     System.out.println("本地ip地址:    " + ip);
/*  73: 69 */     System.out.println("本地主机名:    " + addr.getHostName());
/*  74: 70 */     System.out.println("JVM可以使用的总内存:    " + r.totalMemory());
/*  75: 71 */     System.out.println("JVM可以使用的剩余内存:    " + r.freeMemory());
/*  76: 72 */     System.out.println("JVM可以使用的处理器个数:    " + r.availableProcessors());
/*  77: 73 */     System.out.println("Java的运行环境版本：    " + props.getProperty("java.version"));
/*  78: 74 */     System.out.println("Java的运行环境供应商：    " + props.getProperty("java.vendor"));
/*  79: 75 */     System.out.println("Java供应商的URL：    " + props.getProperty("java.vendor.url"));
/*  80: 76 */     System.out.println("Java的安装路径：    " + props.getProperty("java.home"));
/*  81: 77 */     System.out.println("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version"));
/*  82: 78 */     System.out.println("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor"));
/*  83: 79 */     System.out.println("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name"));
/*  84: 80 */     System.out.println("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version"));
/*  85: 81 */     System.out.println("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor"));
/*  86: 82 */     System.out.println("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name"));
/*  87: 83 */     System.out.println("Java运行时环境规范版本：    " + props.getProperty("java.specification.version"));
/*  88: 84 */     System.out.println("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender"));
/*  89: 85 */     System.out.println("Java运行时环境规范名称：    " + props.getProperty("java.specification.name"));
/*  90: 86 */     System.out.println("Java的类格式版本号：    " + props.getProperty("java.class.version"));
/*  91: 87 */     System.out.println("Java的类路径：    " + props.getProperty("java.class.path"));
/*  92: 88 */     System.out.println("加载库时搜索的路径列表：    " + props.getProperty("java.library.path"));
/*  93: 89 */     System.out.println("默认的临时文件路径：    " + props.getProperty("java.io.tmpdir"));
/*  94: 90 */     System.out.println("一个或多个扩展目录的路径：    " + props.getProperty("java.ext.dirs"));
/*  95: 91 */     System.out.println("操作系统的名称：    " + props.getProperty("os.name"));
/*  96: 92 */     System.out.println("操作系统的构架：    " + props.getProperty("os.arch"));
/*  97: 93 */     System.out.println("操作系统的版本：    " + props.getProperty("os.version"));
/*  98: 94 */     System.out.println("文件分隔符：    " + props.getProperty("file.separator"));
/*  99: 95 */     System.out.println("路径分隔符：    " + props.getProperty("path.separator"));
/* 100: 96 */     System.out.println("行分隔符：    " + props.getProperty("line.separator"));
/* 101: 97 */     System.out.println("用户的账户名称：    " + props.getProperty("user.name"));
/* 102: 98 */     System.out.println("用户的主目录：    " + props.getProperty("user.home"));
/* 103: 99 */     System.out.println("用户的当前工作目录：    " + props.getProperty("user.dir"));
/* 104:    */   }
/* 105:    */   
/* 106:    */   private static void memory()
/* 107:    */     throws SigarException
/* 108:    */   {
/* 109:103 */     Sigar sigar = new Sigar();
/* 110:104 */     Mem mem = sigar.getMem();
/* 111:    */     
/* 112:106 */     System.out.println("内存总量:    " + mem.getTotal() / 1024L + "K av");
/* 113:    */     
/* 114:108 */     System.out.println("当前内存使用量:    " + mem.getUsed() / 1024L + "K used");
/* 115:    */     
/* 116:110 */     System.out.println("当前内存剩余量:    " + mem.getFree() / 1024L + "K free");
/* 117:111 */     Swap swap = sigar.getSwap();
/* 118:    */     
/* 119:113 */     System.out.println("交换区总量:    " + swap.getTotal() / 1024L + "K av");
/* 120:    */     
/* 121:115 */     System.out.println("当前交换区使用量:    " + swap.getUsed() / 1024L + "K used");
/* 122:    */     
/* 123:117 */     System.out.println("当前交换区剩余量:    " + swap.getFree() / 1024L + "K free");
/* 124:    */   }
/* 125:    */   
/* 126:    */   private static void cpu()
/* 127:    */     throws SigarException
/* 128:    */   {
/* 129:121 */     Sigar sigar = new Sigar();
/* 130:122 */     CpuInfo[] infos = sigar.getCpuInfoList();
/* 131:123 */     CpuPerc[] cpuList = null;
/* 132:    */     
/* 133:125 */     System.out.println("cpu 总量参数情况:" + sigar.getCpu());
/* 134:126 */     System.out.println("cpu 总百分比情况:" + sigar.getCpuPerc());
/* 135:    */     
/* 136:128 */     cpuList = sigar.getCpuPercList();
/* 137:129 */     for (int i = 0; i < infos.length; i++)
/* 138:    */     {
/* 139:130 */       CpuInfo info = infos[i];
/* 140:131 */       System.out.println("第" + (i + 1) + "块CPU信息");
/* 141:132 */       System.out.println("CPU的总量MHz:    " + info.getMhz());
/* 142:133 */       System.out.println("CPU生产商:    " + info.getVendor());
/* 143:134 */       System.out.println("CPU类别:    " + info.getModel());
/* 144:135 */       System.out.println("CPU缓存数量:    " + info.getCacheSize());
/* 145:136 */       printCpuPerc(cpuList[i]);
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   private static void printCpuPerc(CpuPerc cpu)
/* 150:    */   {
/* 151:141 */     System.out.println("CPU用户使用率:    " + CpuPerc.format(cpu.getUser()));
/* 152:142 */     System.out.println("CPU系统使用率:    " + CpuPerc.format(cpu.getSys()));
/* 153:143 */     System.out.println("CPU当前等待率:    " + CpuPerc.format(cpu.getWait()));
/* 154:144 */     System.out.println("CPU当前错误率:    " + CpuPerc.format(cpu.getNice()));
/* 155:145 */     System.out.println("CPU当前空闲率:    " + CpuPerc.format(cpu.getIdle()));
/* 156:146 */     System.out.println("CPU总的使用率:    " + CpuPerc.format(cpu.getCombined()));
/* 157:    */   }
/* 158:    */   
/* 159:    */   private static void os()
/* 160:    */   {
/* 161:150 */     OperatingSystem OS = OperatingSystem.getInstance();
/* 162:    */     
/* 163:152 */     System.out.println("操作系统:    " + OS.getArch());
/* 164:153 */     System.out.println("操作系统CpuEndian():    " + OS.getCpuEndian());
/* 165:154 */     System.out.println("操作系统DataModel():    " + OS.getDataModel());
/* 166:    */     
/* 167:156 */     System.out.println("操作系统的描述:    " + OS.getDescription());
/* 168:    */     
/* 169:    */ 
/* 170:    */ 
/* 171:    */ 
/* 172:161 */     System.out.println("操作系统的卖主:    " + OS.getVendor());
/* 173:    */     
/* 174:163 */     System.out.println("操作系统的卖主名:    " + OS.getVendorCodeName());
/* 175:    */     
/* 176:165 */     System.out.println("操作系统名称:    " + OS.getVendorName());
/* 177:    */     
/* 178:167 */     System.out.println("操作系统卖主类型:    " + OS.getVendorVersion());
/* 179:    */     
/* 180:169 */     System.out.println("操作系统的版本号:    " + OS.getVersion());
/* 181:    */   }
/* 182:    */   
/* 183:    */   private static void who()
/* 184:    */     throws SigarException
/* 185:    */   {
/* 186:173 */     Sigar sigar = new Sigar();
/* 187:174 */     Who[] who = sigar.getWhoList();
/* 188:175 */     if ((who != null) && (who.length > 0)) {
/* 189:176 */       for (int i = 0; i < who.length; i++)
/* 190:    */       {
/* 191:178 */         Who _who = who[i];
/* 192:179 */         System.out.println("用户控制台:    " + _who.getDevice());
/* 193:180 */         System.out.println("用户host:    " + _who.getHost());
/* 194:    */         
/* 195:    */ 
/* 196:183 */         System.out.println("当前系统进程表中的用户名:    " + _who.getUser());
/* 197:    */       }
/* 198:    */     }
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static void file()
/* 202:    */     throws Exception
/* 203:    */   {
/* 204:189 */     Sigar sigar = new Sigar();
/* 205:190 */     FileSystem[] fslist = sigar.getFileSystemList();
/* 206:191 */     for (int i = 0; i < fslist.length; i++)
/* 207:    */     {
/* 208:192 */       System.out.println("分区的盘符名称" + i);
/* 209:193 */       FileSystem fs = fslist[i];
/* 210:    */       
/* 211:195 */       System.out.println("盘符名称:    " + fs.getDevName());
/* 212:    */       
/* 213:197 */       System.out.println("盘符路径:    " + fs.getDirName());
/* 214:198 */       System.out.println("盘符标志:    " + fs.getFlags());
/* 215:    */       
/* 216:200 */       System.out.println("盘符类型:    " + fs.getSysTypeName());
/* 217:    */       
/* 218:202 */       System.out.println("盘符类型名:    " + fs.getTypeName());
/* 219:    */       
/* 220:204 */       System.out.println("盘符文件系统类型:    " + fs.getType());
/* 221:205 */       FileSystemUsage usage = null;
/* 222:206 */       usage = sigar.getFileSystemUsage(fs.getDirName());
/* 223:207 */       switch (fs.getType())
/* 224:    */       {
/* 225:    */       case 0: 
/* 226:    */         break;
/* 227:    */       case 1: 
/* 228:    */         break;
/* 229:    */       case 2: 
/* 230:214 */         System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal() + "KB");
/* 231:    */         
/* 232:216 */         System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree() + "KB");
/* 233:    */         
/* 234:218 */         System.out.println(fs.getDevName() + "可用大小:    " + usage.getAvail() + "KB");
/* 235:    */         
/* 236:220 */         System.out.println(fs.getDevName() + "已经使用量:    " + usage.getUsed() + "KB");
/* 237:221 */         double usePercent = usage.getUsePercent() * 100.0D;
/* 238:    */         
/* 239:223 */         System.out.println(fs.getDevName() + "资源的利用率:    " + usePercent + "%");
/* 240:224 */         break;
/* 241:    */       case 3: 
/* 242:    */         break;
/* 243:    */       case 4: 
/* 244:    */         break;
/* 245:    */       case 5: 
/* 246:    */         break;
/* 247:    */       }
/* 248:234 */       System.out.println(fs.getDevName() + "读出：    " + usage.getDiskReads());
/* 249:235 */       System.out.println(fs.getDevName() + "写入：    " + usage.getDiskWrites());
/* 250:    */     }
/* 251:    */   }
/* 252:    */   
/* 253:    */   private static void net()
/* 254:    */     throws Exception
/* 255:    */   {
/* 256:241 */     Sigar sigar = new Sigar();
/* 257:242 */     String[] ifNames = sigar.getNetInterfaceList();
/* 258:243 */     for (int i = 0; i < ifNames.length; i++)
/* 259:    */     {
/* 260:244 */       String name = ifNames[i];
/* 261:245 */       NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
/* 262:246 */       System.out.println("网络设备名:    " + name);
/* 263:247 */       System.out.println("IP地址:    " + ifconfig.getAddress());
/* 264:248 */       System.out.println("子网掩码:    " + ifconfig.getNetmask());
/* 265:249 */       if ((ifconfig.getFlags() & 1L) <= 0L)
/* 266:    */       {
/* 267:250 */         System.out.println("!IFF_UP...skipping getNetInterfaceStat");
/* 268:    */       }
/* 269:    */       else
/* 270:    */       {
/* 271:253 */         NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
/* 272:254 */         System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());
/* 273:255 */         System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());
/* 274:256 */         System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());
/* 275:257 */         System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());
/* 276:258 */         System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());
/* 277:259 */         System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());
/* 278:260 */         System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());
/* 279:261 */         System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());
/* 280:    */       }
/* 281:    */     }
/* 282:    */   }
/* 283:    */   
/* 284:    */   private static void ethernet()
/* 285:    */     throws SigarException
/* 286:    */   {
/* 287:266 */     Sigar sigar = null;
/* 288:267 */     sigar = new Sigar();
/* 289:268 */     String[] ifaces = sigar.getNetInterfaceList();
/* 290:269 */     for (int i = 0; i < ifaces.length; i++)
/* 291:    */     {
/* 292:270 */       NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
/* 293:271 */       if ((!"127.0.0.1".equals(cfg.getAddress())) && ((cfg.getFlags() & 0x8) == 0L) && 
/* 294:272 */         (!"00:00:00:00:00:00".equals(cfg.getHwaddr())))
/* 295:    */       {
/* 296:275 */         System.out.println(cfg.getName() + "IP地址:" + cfg.getAddress());
/* 297:276 */         System.out.println(cfg.getName() + "网关广播地址:" + cfg.getBroadcast());
/* 298:277 */         System.out.println(cfg.getName() + "网卡MAC地址:" + cfg.getHwaddr());
/* 299:278 */         System.out.println(cfg.getName() + "子网掩码:" + cfg.getNetmask());
/* 300:279 */         System.out.println(cfg.getName() + "网卡描述信息:" + cfg.getDescription());
/* 301:280 */         System.out.println(cfg.getName() + "网卡类型" + cfg.getType());
/* 302:    */       }
/* 303:    */     }
/* 304:    */   }
/* 305:    */ }


/* Location:           E:\BaiduYunDownload\尚学堂架构师\008-互联网架构视频第二期(011)\SocketIO_03\bin\
 * Qualified Name:     bhz.utils.TestSigar
 * JD-Core Version:    0.7.0.1
 */