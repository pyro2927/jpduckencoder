/*    */ package org.eclipse.jdt.internal.jarinjarloader;
/*    */ 
/*    */ import java.net.URLStreamHandler;
/*    */ import java.net.URLStreamHandlerFactory;
/*    */ 
/*    */ public class RsrcURLStreamHandlerFactory
/*    */   implements URLStreamHandlerFactory
/*    */ {
/*    */   private ClassLoader classLoader;
/*    */   private URLStreamHandlerFactory chainFac;
/*    */ 
/*    */   public RsrcURLStreamHandlerFactory(ClassLoader cl)
/*    */   {
/* 31 */     this.classLoader = cl;
/*    */   }
/*    */ 
/*    */   public URLStreamHandler createURLStreamHandler(String protocol) {
/* 35 */     if ("rsrc".equals(protocol))
/* 36 */       return new RsrcURLStreamHandler(this.classLoader);
/* 37 */     if (this.chainFac != null)
/* 38 */       return this.chainFac.createURLStreamHandler(protocol);
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   public void setURLStreamHandlerFactory(URLStreamHandlerFactory fac)
/*    */   {
/* 51 */     this.chainFac = fac;
/*    */   }
/*    */ }

/* Location:           /Users/joe/Downloads/duckencode.jar
 * Qualified Name:     org.eclipse.jdt.internal.jarinjarloader.RsrcURLStreamHandlerFactory
 * JD-Core Version:    0.6.0
 */