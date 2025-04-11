package com.zhonglian.server.common.db;

import BaseCommon.CommLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Conditions {
    private final ArrayList<String> orderItemsName = new ArrayList<>();
    private StringBuilder condition;
    private String tablesName;
    private String itemsName;
    private String groupItemsName;
    private String sortType;
    private int countTopLvCondition;
    private int countAllCondition;
    private int limit;

    public Conditions(Class cls) {
        this.condition = new StringBuilder();
        this.tablesName = cls.getSimpleName();
        this.itemsName = "";
        this.orderItemsName.clear();
        this.sortType = "";
        this.countTopLvCondition = 0;
        this.countAllCondition = 0;
        this.limit = 0;
    }

    public Conditions() {
        this.condition = new StringBuilder();
        this.tablesName = "";
        this.itemsName = "";
        this.orderItemsName.clear();
        this.sortType = "";
        this.groupItemsName = "";
        this.countTopLvCondition = 0;
        this.countAllCondition = 0;
        this.limit = 0;
    }

    public void delAllConditions() {
        this.condition = new StringBuilder();
        this.itemsName = "";
        this.orderItemsName.clear();
        this.sortType = "";
        this.groupItemsName = "";
        this.countTopLvCondition = 0;
        this.countAllCondition = 0;
        this.limit = 0;
    }

    public int getCountAllCondition() {
        return this.countAllCondition;
    }

    public int getCountTopLvCondition() {
        return this.countTopLvCondition;
    }

    public String getTablesName() {
        return this.tablesName;
    }

    public void setTablesName(String tablesName) {
        this.tablesName = tablesName;
    }

    public String getItemsName() {
        return this.itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public String getCondition() {
        String orderS = "";
        for (String orderInfo : this.orderItemsName) {
            if (orderS.equals("")) {
                orderS = String.valueOf(orderS) + " order by " + orderInfo;
                continue;
            }
            orderS = String.valueOf(orderS) + ", " + orderInfo;
        }

        String limitS = "";
        if (this.limit > 0) {
            limitS = " limit " + this.limit;
        }

        String sortS = "";
        if (this.sortType != null && !this.sortType.trim().isEmpty()) {
            sortS = " " + this.sortType;
        }

        String groupS = "";
        if (this.groupItemsName != null && !this.groupItemsName.trim().equals("")) {
            orderS = " group by " + this.groupItemsName;
        }
        return String.valueOf(this.condition.toString()) + orderS + sortS + limitS + groupS;
    }

    public String getConditionAndTables() {
        String con = getCondition();
        if (con.equals("") || con.trim().substring(0, 6).equals("order ") || con.trim().substring(0, 6).equals("group ")) {
            return "from " + this.tablesName + " " + con;
        }
        return "from " + this.tablesName + " where " + getCondition();
    }

    public String getSQL() {
        return "select " + this.itemsName + " " + getConditionAndTables() + ";";
    }

    private boolean addSQL(String sql, String operate) {
        try {
            if (this.condition.length() > 0) {
                this.condition.append(operate).append(" ");
            }
            this.condition.append(sql);
            this.countTopLvCondition++;
            this.countAllCondition++;
            return true;
        } catch (Throwable e) {
            CommLog.error("Conditions addConditions Error!---alzq.baseClass.Conditions:");
            CommLog.error(sql);
            return false;
        }
    }

    public boolean addAndEquals(String itemName, Object value) {
        return addSQL(String.valueOf(itemName) + "='" + String.valueOf(value) + "' ", "and");
    }

    public boolean addAndEqualsItem(String itemName, String value) {
        return addSQL(String.valueOf(itemName) + "=" + String.valueOf(value) + " ", "and");
    }

    public boolean addAndNotEquals(String itemName, Object value) {
        return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "and");
    }

    public boolean addAndNotEquals(String itemName, int value) {
        return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "and");
    }

    public boolean addAndNotEqualsItem(String itemName, String value) {
        return addSQL(String.valueOf(itemName) + "!=" + String.valueOf(value) + " ", "and");
    }

    public boolean addAndLike(String itemName, String likeStr) {
        return addSQL(String.valueOf(itemName) + " like '" + likeStr + "' ", "and");
    }

    private boolean addAndNumberJudge(String itemName, String operator, Object value) {
        return addSQL(String.valueOf(itemName) + " " + operator + " " + String.valueOf(value) + " ", "and");
    }

    public boolean addAndSmallThan(String itemName, int value) {
        return addAndNumberJudge(itemName, "<", String.valueOf(value));
    }

    public boolean addSmallThan(String itemName, double value) {
        return addAndNumberJudge(itemName, "<", String.valueOf(value));
    }

    public boolean addSmallThan(String itemName, float value) {
        return addAndNumberJudge(itemName, "<", String.valueOf(value));
    }

    public boolean addAndLardgeThan(String itemName, int value) {
        return addAndNumberJudge(itemName, ">", String.valueOf(value));
    }

    public boolean addAndLardgeThan(String itemName, double value) {
        return addAndNumberJudge(itemName, ">", String.valueOf(value));
    }

    public boolean addAndLardgeThan(String itemName, float value) {
        return addAndNumberJudge(itemName, ">", String.valueOf(value));
    }

    public boolean addAndSmallAndEquals(String itemName, int value) {
        return addAndNumberJudge(itemName, "<=", String.valueOf(value));
    }

    public boolean addAndSmallAndEquals(String itemName, double value) {
        return addAndNumberJudge(itemName, "<=", String.valueOf(value));
    }

    public boolean addAndSmallAndEquals(String itemName, float value) {
        return addAndNumberJudge(itemName, "<=", String.valueOf(value));
    }

    public boolean addAndLardgeAndEquals(String itemName, int value) {
        return addAndNumberJudge(itemName, ">=", String.valueOf(value));
    }

    public boolean addAndLardgeAndEquals(String itemName, double value) {
        return addAndNumberJudge(itemName, ">=", String.valueOf(value));
    }

    public boolean addAndLardgeAndEquals(String itemName, float value) {
        return addAndNumberJudge(itemName, ">=", String.valueOf(value));
    }

    private boolean addAndJudge(String itemName, String operator, Object value) {
        return addSQL(String.valueOf(itemName) + " " + operator + " '" + String.valueOf(value) + "' ", "and");
    }

    public boolean addAndSmallThan(String itemName, Object value) {
        return addAndJudge(itemName, "<", value);
    }

    public boolean addAndLardgeThan(String itemName, Object value) {
        return addAndJudge(itemName, ">", value);
    }

    public boolean addAndSmallAndEquals(String itemName, Object value) {
        return addAndJudge(itemName, "<=", value);
    }

    public boolean addAndLardgeAndEquals(String itemName, Object value) {
        return addAndJudge(itemName, ">=", value);
    }

    private boolean addAndItemJudge(String itemName, String operator, String item) {
        return addSQL(String.valueOf(itemName) + " " + operator + " " + item + " ", "and");
    }

    public boolean addAndSmallThanItem(String itemName, String item) {
        return addAndItemJudge(itemName, "<", item);
    }

    public boolean addAndLardgeThanItem(String itemName, String item) {
        return addAndItemJudge(itemName, ">", item);
    }

    public boolean addAndSmallAndEqualsItem(String itemName, String item) {
        return addAndItemJudge(itemName, "<=", item);
    }

    public boolean addAndLardgeAndEqualsItem(String itemName, String item) {
        return addAndItemJudge(itemName, ">=", item);
    }

    public boolean addAndInList(String itemName, List list) {
        try {
            if (this.condition.length() > 0) {
                this.condition.append("and ");
            }
            this.condition.append(itemName).append(" in ");

            this.condition.append("(");
            for (Iterator<E> iter = list.iterator(); iter.hasNext(); ) {
                this.condition.append("'").append(iter.next().toString()).append("'");
                if (iter.hasNext()) {
                    this.condition.append(",");
                }
            }
            this.condition.append(")");

            this.condition.append(" ");
            this.countTopLvCondition++;
            this.countAllCondition++;
            return true;
        } catch (Throwable e) {
            CommLog.error("Conditions addInList Error!---alzq.baseClass.Conditions:");
            return false;
        }
    }

    public boolean addAndIsNull(String itemName) {
        return addSQL(String.valueOf(itemName) + " is null ", "and");
    }

    public boolean addAndIsNotNull(String itemName) {
        return addSQL(String.valueOf(itemName) + " is not null ", "and");
    }

    public boolean addAndCondition(Conditions con) {
        try {
            if (this.condition.length() > 0) {
                this.condition.append("and ");
            }
            this.condition.append("( ");
            this.condition.append(con.getCondition());
            this.condition.append(") ");
            this.countTopLvCondition++;
            this.countAllCondition += con.getCountAllCondition();
            return true;
        } catch (Throwable e) {
            CommLog.error("Conditions addCondition Error!---alzq.baseClass.Conditions:");
            return false;
        }
    }

    public boolean addOrEquals(String itemName, Object value) {
        return addSQL(String.valueOf(itemName) + "='" + String.valueOf(value) + "' ", "or");
    }

    public boolean addOrEquals(String itemName, int value) {
        return addSQL(String.valueOf(itemName) + "='" + String.valueOf(value) + "' ", "or");
    }

    public boolean addOrEqualsItem(String itemName, String value) {
        return addSQL(String.valueOf(itemName) + "=" + String.valueOf(value) + " ", "or");
    }

    public boolean addOrNotEquals(String itemName, Object value) {
        return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "or");
    }

    public boolean addOrNotEquals(String itemName, int value) {
        return addSQL(String.valueOf(itemName) + "!='" + String.valueOf(value) + "' ", "or");
    }

    public boolean addOrNotEqualsItem(String itemName, String value) {
        return addSQL(String.valueOf(itemName) + "!=" + String.valueOf(value) + " ", "or");
    }

    public boolean addOrLike(String itemName, String likeStr) {
        return addSQL(String.valueOf(itemName) + " like '" + likeStr + "' ", "or");
    }

    private boolean addOrNumberJudge(String itemName, String operator, Object value) {
        return addSQL(String.valueOf(itemName) + " " + operator + " " + String.valueOf(value) + " ", "or");
    }

    public boolean addOrSmallThan(String itemName, int value) {
        return addOrNumberJudge(itemName, "<", String.valueOf(value));
    }

    public boolean addOrSmallThan(String itemName, double value) {
        return addOrNumberJudge(itemName, "<", String.valueOf(value));
    }

    public boolean addOrSmallThan(String itemName, float value) {
        return addOrNumberJudge(itemName, "<", String.valueOf(value));
    }

    public boolean addOrLardgeThan(String itemName, int value) {
        return addOrNumberJudge(itemName, ">", String.valueOf(value));
    }

    public boolean addOrLardgeThan(String itemName, double value) {
        return addOrNumberJudge(itemName, ">", String.valueOf(value));
    }

    public boolean addOrLardgeThan(String itemName, float value) {
        return addOrNumberJudge(itemName, ">", String.valueOf(value));
    }

    public boolean addOrSmallAndEquals(String itemName, int value) {
        return addOrNumberJudge(itemName, "<=", String.valueOf(value));
    }

    public boolean addOrSmallAndEquals(String itemName, double value) {
        return addOrNumberJudge(itemName, "<=", String.valueOf(value));
    }

    public boolean addOrSmallAndEquals(String itemName, float value) {
        return addOrNumberJudge(itemName, "<=", String.valueOf(value));
    }

    public boolean addOrLardgeAndEquals(String itemName, int value) {
        return addOrNumberJudge(itemName, ">=", String.valueOf(value));
    }

    public boolean addOrLardgeAndEquals(String itemName, double value) {
        return addOrNumberJudge(itemName, ">=", String.valueOf(value));
    }

    public boolean addOrLardgeAndEquals(String itemName, float value) {
        return addOrNumberJudge(itemName, ">=", String.valueOf(value));
    }

    private boolean addOrJudge(String itemName, String operator, Object value) {
        return addSQL(String.valueOf(itemName) + " " + operator + " '" + String.valueOf(value) + "' ", "or");
    }

    public boolean addOrSmallThan(String itemName, Object value) {
        return addOrJudge(itemName, "<", value);
    }

    public boolean addOrLardgeThan(String itemName, Object value) {
        return addOrJudge(itemName, ">", value);
    }

    public boolean addOrSmallAndEquals(String itemName, Object value) {
        return addOrJudge(itemName, "<=", value);
    }

    public boolean addOrLardgeAndEquals(String itemName, Object value) {
        return addOrJudge(itemName, ">=", value);
    }

    private boolean addOrItemJudge(String itemName, String operator, String item) {
        return addSQL(String.valueOf(itemName) + " " + operator + " " + item + " ", "or");
    }

    public boolean addOrSmallThanItem(String itemName, String item) {
        return addOrItemJudge(itemName, "<", item);
    }

    public boolean addOrLardgeThanItem(String itemName, String item) {
        return addOrItemJudge(itemName, ">", item);
    }

    public boolean addOrSmallAndEqualsItem(String itemName, String item) {
        return addOrItemJudge(itemName, "<=", item);
    }

    public boolean addOrLardgeAndEqualsItem(String itemName, String item) {
        return addOrItemJudge(itemName, ">=", item);
    }

    public boolean addOrInList(String itemName, List list) {
        try {
            if (this.condition.length() > 0) {
                this.condition.append("or ");
            }
            this.condition.append(itemName).append(" in ");

            this.condition.append("(");
            for (Iterator<E> iter = list.iterator(); iter.hasNext(); ) {
                this.condition.append("'").append(iter.next().toString()).append("'");
                if (iter.hasNext()) {
                    this.condition.append(",");
                }
            }
            this.condition.append(")");

            this.condition.append(" ");
            this.countTopLvCondition++;
            this.countAllCondition++;
            return true;
        } catch (Throwable e) {
            CommLog.error("Conditions addOrInList Error!---alzq.baseClass.Conditions:");
            return false;
        }
    }

    public boolean addOrIsNull(String itemName) {
        return addSQL(String.valueOf(itemName) + " is null ", "or");
    }

    public boolean addOrIsNotNull(String itemName) {
        return addSQL(String.valueOf(itemName) + " is not null ", "or");
    }

    public boolean addOrCondition(Conditions con) {
        try {
            if (this.condition.length() > 0) {
                this.condition.append("or ");
            }
            this.condition.append("( ");
            this.condition.append(con.getCondition());
            this.condition.append(") ");
            this.countTopLvCondition++;
            this.countAllCondition += con.getCountAllCondition();
            return true;
        } catch (Throwable e) {
            CommLog.error("Conditions addOrCondition Error!---alzq.baseClass.Conditions:");
            return false;
        }
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSortType() {
        return this.sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void addOrderItemsInfo(String orderItems) {
        this.orderItemsName.add(orderItems);
    }

    public void clearOrderItemsInfo() {
        this.orderItemsName.clear();
    }

    public String getGroupItemsName() {
        return this.groupItemsName;
    }

    public void setGroupItemsName(String groupItemsName) {
        this.groupItemsName = groupItemsName;
    }
}

