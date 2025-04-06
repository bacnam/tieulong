/*     */ package core.config.refdata.ref;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.data.RefAssert;
/*     */ import com.zhonglian.server.common.data.RefContainer;
/*     */ import com.zhonglian.server.common.data.RefField;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import jsc.distributions.Binomial;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RefReward
/*     */   extends RefBaseGame
/*     */ {
/*     */   @RefField(iskey = true)
/*     */   public int id;
/*     */   public List<Integer> RegularItemIdList;
/*     */   public List<Integer> RegularCountList;
/*     */   public List<Integer> ItemIdList;
/*     */   public List<Integer> CountList;
/*     */   public List<Integer> WeightList;
/*     */   
/*     */   public Reward genReward() {
/*  32 */     Reward reward = new Reward();
/*  33 */     for (int i = 0; i < this.RegularItemIdList.size(); i++) {
/*  34 */       reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), ((Integer)this.RegularCountList.get(i)).intValue());
/*     */     }
/*     */     
/*  37 */     for (int index = 0; index < this.WeightList.size(); index++) {
/*  38 */       if (Random.nextInt(10000) <= ((Integer)this.WeightList.get(index)).intValue())
/*     */       {
/*     */         
/*  41 */         reward.add(((Integer)this.ItemIdList.get(index)).intValue(), ((Integer)this.CountList.get(index)).intValue()); } 
/*     */     } 
/*  43 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward genLimitReward(int num) {
/*  52 */     Reward reward = new Reward();
/*  53 */     for (int i = 0; i < this.RegularItemIdList.size(); i++) {
/*  54 */       reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), ((Integer)this.RegularCountList.get(i)).intValue());
/*     */     }
/*     */     
/*  57 */     for (int index = 0; index < this.WeightList.size(); index++) {
/*  58 */       if (Random.nextInt(10000) <= ((Integer)this.WeightList.get(index)).intValue() * num / 10000)
/*     */       {
/*     */         
/*  61 */         reward.add(((Integer)this.ItemIdList.get(index)).intValue(), ((Integer)this.CountList.get(index)).intValue()); } 
/*     */     } 
/*  63 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward genReward(int times) {
/*  72 */     Reward reward = new Reward();
/*  73 */     for (int i = 0; i < this.RegularItemIdList.size(); i++) {
/*  74 */       reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), ((Integer)this.RegularCountList.get(i)).intValue() * times);
/*     */     }
/*     */     
/*  77 */     for (int index = 0; index < this.WeightList.size(); index++) {
/*  78 */       Binomial binoimal = new Binomial(times, ((Integer)this.WeightList.get(index)).intValue() / 10000.0D);
/*  79 */       int count = (int)binoimal.random();
/*  80 */       reward.add(((Integer)this.ItemIdList.get(index)).intValue(), ((Integer)this.CountList.get(index)).intValue() * count);
/*     */     } 
/*  82 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward genReward(int times, int max) {
/*  91 */     Reward reward = new Reward();
/*  92 */     int sum = 0;
/*  93 */     while (times > 0) {
/*  94 */       for (int i = 0; i < this.RegularItemIdList.size(); i++) {
/*  95 */         int count = ((Integer)this.RegularCountList.get(i)).intValue();
/*  96 */         if (sum + count >= max) {
/*  97 */           return reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), max - sum);
/*     */         }
/*  99 */         reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), count);
/* 100 */         sum += count;
/*     */       } 
/*     */       
/* 103 */       for (int index = 0; index < this.WeightList.size(); index++) {
/* 104 */         if (Random.nextInt(10000) <= ((Integer)this.WeightList.get(index)).intValue()) {
/*     */ 
/*     */           
/* 107 */           int count = ((Integer)this.CountList.get(index)).intValue();
/* 108 */           if (sum + count >= max) {
/* 109 */             return reward.add(((Integer)this.ItemIdList.get(index)).intValue(), max - sum);
/*     */           }
/* 111 */           reward.add(((Integer)this.ItemIdList.get(index)).intValue(), count);
/* 112 */           sum += count;
/*     */         } 
/*     */       } 
/* 115 */       times--;
/*     */     } 
/* 117 */     return reward;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean Assert() {
/* 122 */     for (Iterator<Integer> iterator = this.WeightList.iterator(); iterator.hasNext(); ) { int w = ((Integer)iterator.next()).intValue();
/* 123 */       if (w >= 10000 || w <= 0) {
/* 124 */         CommLog.error("掉落权重只能在(0,10000)之间.");
/* 125 */         return false;
/*     */       }  }
/*     */     
/* 128 */     RefAssert.inRef(this.RegularItemIdList, RefUniformItem.class, new Object[0]);
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (!RefAssert.listSize(this.ItemIdList, this.CountList, new List[] { this.WeightList })) {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     if (!RefAssert.listSize(this.RegularItemIdList, this.RegularCountList, new List[0])) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean AssertAll(RefContainer<?> all) {
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 150 */     int repeat = 100000;
/*     */     
/* 152 */     int times = 1;
/* 153 */     double p = 0.1D;
/*     */     
/* 155 */     Map<Double, Integer> rslt = new HashMap<>();
/*     */ 
/*     */ 
/*     */     
/* 159 */     long begin = CommTime.nowMS();
/* 160 */     Binomial binoimal = new Binomial(times, p);
/* 161 */     int sum = 0;
/* 162 */     for (int i = 0; i < repeat; i++) {
/*     */ 
/*     */       
/* 165 */       double count = binoimal.random();
/* 166 */       Integer precount = rslt.get(Double.valueOf(count));
/* 167 */       if (precount == null) {
/* 168 */         rslt.put(Double.valueOf(count), Integer.valueOf(1));
/*     */       } else {
/* 170 */         rslt.put(Double.valueOf(count), Integer.valueOf(precount.intValue() + 1));
/*     */       } 
/* 172 */       sum = (int)(sum + count);
/*     */     } 
/* 174 */     System.out.println("time:" + (CommTime.nowMS() - begin));
/* 175 */     System.out.println(rslt.toString());
/* 176 */     System.out.println(String.valueOf(sum) + "/" + (repeat * times) + "=" + (1.0D * sum / (repeat * times)));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */