/*    */ package org.eclipse.jdt.internal.jarinjarloader;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
/*    */ 
/*    */ public class RsrcURLStreamHandler extends URLStreamHandler
/*    */ {
/*    */   private ClassLoader classLoader;
/*    */ 
/*    */   public RsrcURLStreamHandler(ClassLoader classLoader)
/*    */   {
/* 34 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */   protected URLConnection openConnection(URL u) throws IOException {
/* 38 */     return new RsrcURLConnection(u, this.classLoader);
/*    */   }
/*    */ 
/*    */   protected void parseURL(URL url, String spec, int start, int limit)
/*    */   {
/*    */     String file;
/* 43 */     if (spec.startsWith("rsrc:")) {
/* 44 */       file = spec.substring(5);
/*    */     }
/*    */     else
/*    */     {
/* 45 */       if (url.getFile().equals("./")) {
/* 46 */         file = spec;
/*    */       }
/*    */       else
/*    */       {
/* 47 */         if (url.getFile().endsWith("/"))
/* 48 */           file = url.getFile() + spec;
/*    */         else
/* 50 */           file = spec; 
/*    */       }
/*    */     }
/* 51 */     setURL(url, "rsrc", "", -1, null, null, file, null, null);
/*    */   }
/*    */ }

/* Location:           /Users/joe/Downloads/duckencode.jar
 * Qualified Name:     org.eclipse.jdt.internal.jarinjarloader.RsrcURLStreamHandler
 * JD-Core Version:    0.6.0
 */