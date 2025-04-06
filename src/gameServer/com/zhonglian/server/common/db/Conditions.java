/*     */ package com.zhonglian.server.common.db;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Conditions
/*     */ {
/*     */   private StringBuilder condition;
/*     */   private String tablesName;
/*     */   private String itemsName;
/*  21 */   private final ArrayList<String> orderItemsName = new ArrayList<>();
/*     */   
/*     */   private String groupItemsName;
/*     */   private String sortType;
/*     */   private int countTopLvCondition;
/*     */   private int countAllCondition;
/*     */   private int limit;
/*     */   
/*     */   public Conditions(Class cls) {
/*  30 */     this.condition = new StringBuilder();
/*  31 */     this.tablesName = cls.getSimpleName();
/*  32 */     this.itemsName = "";
/*  33 */     this.orderItemsName.clear();
/*  34 */     this.sortType = "";
/*  35 */     this.countTopLvCondition = 0;
/*  36 */     this.countAllCondition = 0;
/*  37 */     this.limit = 0;
/*     */   }
/*     */   
/*     */   public Conditions() {
/*  41 */     this.condition = new StringBuilder();
/*  42 */     this.tablesName = "";
/*  43 */     this.itemsName = "";
/*  44 */     this.orderItemsName.clear();
/*  45 */     this.sortType = "";
/*  46 */     this.groupItemsName = "";
/*  47 */     this.countTopLvCondition = 0;
/*  48 */     this.countAllCondition = 0;
/*  49 */     this.limit = 0;
/*     */   }
/*     */   
/*     */   public void delAllConditions() {
/*  53 */     this.condition = new StringBuilder();
/*  54 */     this.itemsName = "";
/*  55 */     this.orderItemsName.clear();
/*  56 */     this.sortType = "";
/*  57 */     this.groupItemsName = "";
/*  58 */     this.countTopLvCondition = 0;
/*  59 */     this.countAllCondition = 0;
/*  60 */     this.limit = 0;
/*     */   }
/*     */   
/*     */   public int getCountAllCondition() {
/*  64 */     return this.countAllCondition;
/*     */   }
/*     */   
/*     */   public int getCountTopLvCondition() {
/*  68 */     return this.countTopLvCondition;
/*     */   }
/*     */   
/*     */   public String getTablesName() {
/*  72 */     return this.tablesName;
/*     */   }
/*     */   
/*     */   public void setTablesName(String tablesName) {
/*  76 */     this.tablesName = tablesName;
/*     */   }
/*     */   
/*     */   public String getItemsName() {
/*  80 */     return this.itemsName;
/*     */   }
/*     */   
/*     */   public void setItemsName(String itemsName) {
/*  84 */     this.itemsName = itemsName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCondition() {
/*  93 */     String orderS = "";
/*  94 */     for (String orderInfo : this.orderItemsName) {
/*  95 */       if (orderS.equals("")) {
/*  96 */         orderS = String.valueOf(orderS) + " order by " + orderInfo; continue;
/*     */       } 
/*  98 */       orderS = String.valueOf(orderS) + ", " + orderInfo;
/*     */     } 
/*     */ 
/*     */     
/* 102 */     String limitS = "";
/* 103 */     if (this.limit > 0) {
/* 104 */       limitS = " limit " + this.limit;
/*     */     }
/*     */     
/* 107 */     String sortS = "";
/* 108 */     if (this.sortType != null && !this.sortType.trim().isEmpty()) {
/* 109 */       sortS = " " + this.sortType;
/*     */     }
/*     */     
/* 112 */     String groupS = "";
/* 113 */     if (this.groupItemsName != null && !this.groupItemsName.trim().equals("")) {
/* 114 */       orderS = " group by " + this.groupItemsName;
/*     */     }
/* 116 */     return String.valueOf(this.condition.toString()) + orderS + sortS + limitS + groupS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConditionAndTables() {
/* 125 */     String con = getCondition();
/* 126 */     if (con.equals("") || con.trim().substring(0, 6).equals("order ") || con.trim().substring(0, 6).equals("group ")) {
/* 127 */       return "from " + this.tablesName + " " + con;
/*     */     }
/* 129 */     return "from " + this.tablesName + " where " + getCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSQL() {
/* 138 */     return "select " + this.itemsName + " " + getConditionAndTables() + ";";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addSQL(String sql, String operate) {
/*     */     try {
/* 150 */       if (this.condition.length() > 0) {
/* 151 */         this.condition.append(operate).append(" ");
/*     */       }
/* 153 */       this.condition.append(sql);
/* 154 */       this.countTopLvCondition++;
/* 155 */       this.countAllCondition++;
/* 156 */       return true;
/* 157 */     } catch (Throwable e) {
/* 158 */       CommLog.error("Conditions addConditions Error!---alzq.baseClass.Conditions:");
/* 159 */       CommLog.error(sql);
/* 160 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndEquals(String itemName, Object value) {
/* 172 */     return addSQL(String.valueOf(itemName) + "='" + String.valueOf(value) + "' ", "and");
/*     */   }
/*     */   
/*     */   public boolean addAndEqualsItem(String itemName, String value) {
/* 176 */     return addSQL(String.valueOf(itemName) + "=" + String.valueOf(value) + " ", "and");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndNotEquals(String itemName, Object value) {
/* 187 */     return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "and");
/*     */   }
/*     */   
/*     */   public boolean addAndNotEquals(String itemName, int value) {
/* 191 */     return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "and");
/*     */   }
/*     */   
/*     */   public boolean addAndNotEqualsItem(String itemName, String value) {
/* 195 */     return addSQL(String.valueOf(itemName) + "!=" + String.valueOf(value) + " ", "and");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndLike(String itemName, String likeStr) {
/* 206 */     return addSQL(String.valueOf(itemName) + " like '" + likeStr + "' ", "and");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addAndNumberJudge(String itemName, String operator, Object value) {
/* 218 */     return addSQL(String.valueOf(itemName) + " " + operator + " " + String.valueOf(value) + " ", "and");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndSmallThan(String itemName, int value) {
/* 229 */     return addAndNumberJudge(itemName, "<", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addSmallThan(String itemName, double value) {
/* 233 */     return addAndNumberJudge(itemName, "<", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addSmallThan(String itemName, float value) {
/* 237 */     return addAndNumberJudge(itemName, "<", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndLardgeThan(String itemName, int value) {
/* 248 */     return addAndNumberJudge(itemName, ">", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeThan(String itemName, double value) {
/* 252 */     return addAndNumberJudge(itemName, ">", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeThan(String itemName, float value) {
/* 256 */     return addAndNumberJudge(itemName, ">", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndSmallAndEquals(String itemName, int value) {
/* 267 */     return addAndNumberJudge(itemName, "<=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addAndSmallAndEquals(String itemName, double value) {
/* 271 */     return addAndNumberJudge(itemName, "<=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addAndSmallAndEquals(String itemName, float value) {
/* 275 */     return addAndNumberJudge(itemName, "<=", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndLardgeAndEquals(String itemName, int value) {
/* 286 */     return addAndNumberJudge(itemName, ">=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeAndEquals(String itemName, double value) {
/* 290 */     return addAndNumberJudge(itemName, ">=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeAndEquals(String itemName, float value) {
/* 294 */     return addAndNumberJudge(itemName, ">=", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addAndJudge(String itemName, String operator, Object value) {
/* 306 */     return addSQL(String.valueOf(itemName) + " " + operator + " '" + String.valueOf(value) + "' ", "and");
/*     */   }
/*     */   
/*     */   public boolean addAndSmallThan(String itemName, Object value) {
/* 310 */     return addAndJudge(itemName, "<", value);
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeThan(String itemName, Object value) {
/* 314 */     return addAndJudge(itemName, ">", value);
/*     */   }
/*     */   
/*     */   public boolean addAndSmallAndEquals(String itemName, Object value) {
/* 318 */     return addAndJudge(itemName, "<=", value);
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeAndEquals(String itemName, Object value) {
/* 322 */     return addAndJudge(itemName, ">=", value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addAndItemJudge(String itemName, String operator, String item) {
/* 333 */     return addSQL(String.valueOf(itemName) + " " + operator + " " + item + " ", "and");
/*     */   }
/*     */   
/*     */   public boolean addAndSmallThanItem(String itemName, String item) {
/* 337 */     return addAndItemJudge(itemName, "<", item);
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeThanItem(String itemName, String item) {
/* 341 */     return addAndItemJudge(itemName, ">", item);
/*     */   }
/*     */   
/*     */   public boolean addAndSmallAndEqualsItem(String itemName, String item) {
/* 345 */     return addAndItemJudge(itemName, "<=", item);
/*     */   }
/*     */   
/*     */   public boolean addAndLardgeAndEqualsItem(String itemName, String item) {
/* 349 */     return addAndItemJudge(itemName, ">=", item);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndInList(String itemName, List list) {
/*     */     try {
/* 362 */       if (this.condition.length() > 0) {
/* 363 */         this.condition.append("and ");
/*     */       }
/* 365 */       this.condition.append(itemName).append(" in ");
/*     */       
/* 367 */       this.condition.append("(");
/* 368 */       for (Iterator<E> iter = list.iterator(); iter.hasNext(); ) {
/* 369 */         this.condition.append("'").append(iter.next().toString()).append("'");
/* 370 */         if (iter.hasNext()) {
/* 371 */           this.condition.append(",");
/*     */         }
/*     */       } 
/* 374 */       this.condition.append(")");
/*     */       
/* 376 */       this.condition.append(" ");
/* 377 */       this.countTopLvCondition++;
/* 378 */       this.countAllCondition++;
/* 379 */       return true;
/* 380 */     } catch (Throwable e) {
/* 381 */       CommLog.error("Conditions addInList Error!---alzq.baseClass.Conditions:");
/* 382 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndIsNull(String itemName) {
/* 393 */     return addSQL(String.valueOf(itemName) + " is null ", "and");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndIsNotNull(String itemName) {
/* 403 */     return addSQL(String.valueOf(itemName) + " is not null ", "and");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAndCondition(Conditions con) {
/*     */     try {
/* 414 */       if (this.condition.length() > 0) {
/* 415 */         this.condition.append("and ");
/*     */       }
/* 417 */       this.condition.append("( ");
/* 418 */       this.condition.append(con.getCondition());
/* 419 */       this.condition.append(") ");
/* 420 */       this.countTopLvCondition++;
/* 421 */       this.countAllCondition += con.getCountAllCondition();
/* 422 */       return true;
/* 423 */     } catch (Throwable e) {
/* 424 */       CommLog.error("Conditions addCondition Error!---alzq.baseClass.Conditions:");
/* 425 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrEquals(String itemName, Object value) {
/* 437 */     return addSQL(String.valueOf(itemName) + "='" + String.valueOf(value) + "' ", "or");
/*     */   }
/*     */   
/*     */   public boolean addOrEquals(String itemName, int value) {
/* 441 */     return addSQL(String.valueOf(itemName) + "='" + String.valueOf(value) + "' ", "or");
/*     */   }
/*     */   
/*     */   public boolean addOrEqualsItem(String itemName, String value) {
/* 445 */     return addSQL(String.valueOf(itemName) + "=" + String.valueOf(value) + " ", "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrNotEquals(String itemName, Object value) {
/* 456 */     return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "or");
/*     */   }
/*     */   
/*     */   public boolean addOrNotEquals(String itemName, int value) {
/* 460 */     return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "or");
/*     */   }
/*     */   
/*     */   public boolean addOrNotEqualsItem(String itemName, String value) {
/* 464 */     return addSQL(String.valueOf(itemName) + "!=" + String.valueOf(value) + " ", "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrLike(String itemName, String likeStr) {
/* 475 */     return addSQL(String.valueOf(itemName) + " like '" + likeStr + "' ", "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addOrNumberJudge(String itemName, String operator, Object value) {
/* 487 */     return addSQL(String.valueOf(itemName) + " " + operator + " " + String.valueOf(value) + " ", "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrSmallThan(String itemName, int value) {
/* 498 */     return addOrNumberJudge(itemName, "<", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrSmallThan(String itemName, double value) {
/* 502 */     return addOrNumberJudge(itemName, "<", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrSmallThan(String itemName, float value) {
/* 506 */     return addOrNumberJudge(itemName, "<", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrLardgeThan(String itemName, int value) {
/* 517 */     return addOrNumberJudge(itemName, ">", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeThan(String itemName, double value) {
/* 521 */     return addOrNumberJudge(itemName, ">", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeThan(String itemName, float value) {
/* 525 */     return addOrNumberJudge(itemName, ">", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrSmallAndEquals(String itemName, int value) {
/* 536 */     return addOrNumberJudge(itemName, "<=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrSmallAndEquals(String itemName, double value) {
/* 540 */     return addOrNumberJudge(itemName, "<=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrSmallAndEquals(String itemName, float value) {
/* 544 */     return addOrNumberJudge(itemName, "<=", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrLardgeAndEquals(String itemName, int value) {
/* 555 */     return addOrNumberJudge(itemName, ">=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeAndEquals(String itemName, double value) {
/* 559 */     return addOrNumberJudge(itemName, ">=", String.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeAndEquals(String itemName, float value) {
/* 563 */     return addOrNumberJudge(itemName, ">=", String.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addOrJudge(String itemName, String operator, Object value) {
/* 575 */     return addSQL(String.valueOf(itemName) + " " + operator + " '" + String.valueOf(value) + "' ", "or");
/*     */   }
/*     */   
/*     */   public boolean addOrSmallThan(String itemName, Object value) {
/* 579 */     return addOrJudge(itemName, "<", value);
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeThan(String itemName, Object value) {
/* 583 */     return addOrJudge(itemName, ">", value);
/*     */   }
/*     */   
/*     */   public boolean addOrSmallAndEquals(String itemName, Object value) {
/* 587 */     return addOrJudge(itemName, "<=", value);
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeAndEquals(String itemName, Object value) {
/* 591 */     return addOrJudge(itemName, ">=", value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addOrItemJudge(String itemName, String operator, String item) {
/* 603 */     return addSQL(String.valueOf(itemName) + " " + operator + " " + item + " ", "or");
/*     */   }
/*     */   
/*     */   public boolean addOrSmallThanItem(String itemName, String item) {
/* 607 */     return addOrItemJudge(itemName, "<", item);
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeThanItem(String itemName, String item) {
/* 611 */     return addOrItemJudge(itemName, ">", item);
/*     */   }
/*     */   
/*     */   public boolean addOrSmallAndEqualsItem(String itemName, String item) {
/* 615 */     return addOrItemJudge(itemName, "<=", item);
/*     */   }
/*     */   
/*     */   public boolean addOrLardgeAndEqualsItem(String itemName, String item) {
/* 619 */     return addOrItemJudge(itemName, ">=", item);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrInList(String itemName, List list) {
/*     */     try {
/* 632 */       if (this.condition.length() > 0) {
/* 633 */         this.condition.append("or ");
/*     */       }
/* 635 */       this.condition.append(itemName).append(" in ");
/*     */       
/* 637 */       this.condition.append("(");
/* 638 */       for (Iterator<E> iter = list.iterator(); iter.hasNext(); ) {
/* 639 */         this.condition.append("'").append(iter.next().toString()).append("'");
/* 640 */         if (iter.hasNext()) {
/* 641 */           this.condition.append(",");
/*     */         }
/*     */       } 
/* 644 */       this.condition.append(")");
/*     */       
/* 646 */       this.condition.append(" ");
/* 647 */       this.countTopLvCondition++;
/* 648 */       this.countAllCondition++;
/* 649 */       return true;
/* 650 */     } catch (Throwable e) {
/* 651 */       CommLog.error("Conditions addOrInList Error!---alzq.baseClass.Conditions:");
/* 652 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrIsNull(String itemName) {
/* 663 */     return addSQL(String.valueOf(itemName) + " is null ", "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrIsNotNull(String itemName) {
/* 673 */     return addSQL(String.valueOf(itemName) + " is not null ", "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addOrCondition(Conditions con) {
/*     */     try {
/* 684 */       if (this.condition.length() > 0) {
/* 685 */         this.condition.append("or ");
/*     */       }
/* 687 */       this.condition.append("( ");
/* 688 */       this.condition.append(con.getCondition());
/* 689 */       this.condition.append(") ");
/* 690 */       this.countTopLvCondition++;
/* 691 */       this.countAllCondition += con.getCountAllCondition();
/* 692 */       return true;
/* 693 */     } catch (Throwable e) {
/* 694 */       CommLog.error("Conditions addOrCondition Error!---alzq.baseClass.Conditions:");
/* 695 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getLimit() {
/* 700 */     return this.limit;
/*     */   }
/*     */   
/*     */   public void setLimit(int limit) {
/* 704 */     this.limit = limit;
/*     */   }
/*     */   
/*     */   public String getSortType() {
/* 708 */     return this.sortType;
/*     */   }
/*     */   
/*     */   public void setSortType(String sortType) {
/* 712 */     this.sortType = sortType;
/*     */   }
/*     */   
/*     */   public void addOrderItemsInfo(String orderItems) {
/* 716 */     this.orderItemsName.add(orderItems);
/*     */   }
/*     */   
/*     */   public void clearOrderItemsInfo() {
/* 720 */     this.orderItemsName.clear();
/*     */   }
/*     */   
/*     */   public String getGroupItemsName() {
/* 724 */     return this.groupItemsName;
/*     */   }
/*     */   
/*     */   public void setGroupItemsName(String groupItemsName) {
/* 728 */     this.groupItemsName = groupItemsName;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/Conditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */