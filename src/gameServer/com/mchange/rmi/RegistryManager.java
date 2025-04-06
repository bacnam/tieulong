/*    */ package com.mchange.rmi;
/*    */ 
/*    */ import java.rmi.AccessException;
/*    */ import java.rmi.ConnectException;
/*    */ import java.rmi.RemoteException;
/*    */ import java.rmi.registry.LocateRegistry;
/*    */ import java.rmi.registry.Registry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegistryManager
/*    */ {
/*    */   public static Registry ensureRegistry(int paramInt) throws RemoteException {
/* 47 */     Registry registry = findRegistry(paramInt);
/* 48 */     if (registry == null) registry = LocateRegistry.createRegistry(paramInt); 
/* 49 */     return registry;
/*    */   }
/*    */   
/*    */   public static Registry ensureRegistry() throws RemoteException {
/* 53 */     return ensureRegistry(1099);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean registryAvailable(int paramInt) throws RemoteException, AccessException {
/*    */     try {
/* 59 */       Registry registry = LocateRegistry.getRegistry(paramInt);
/* 60 */       registry.list();
/* 61 */       return true;
/*    */     }
/* 63 */     catch (ConnectException connectException) {
/* 64 */       return false;
/*    */     } 
/*    */   }
/*    */   public static boolean registryAvailable() throws RemoteException, AccessException {
/* 68 */     return registryAvailable(1099);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Registry findRegistry(int paramInt) throws RemoteException, AccessException {
/* 75 */     if (!registryAvailable(paramInt)) return null; 
/* 76 */     return LocateRegistry.getRegistry(paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Registry findRegistry() throws RemoteException, AccessException {
/* 83 */     return findRegistry(1099);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/rmi/RegistryManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */