package com.db;

import java.util.List;

/**
 * @author xiao_cong
 */
public class TableInfo {

    private String tableName;


    private List<List<String>>  columns;

    private List<String> primaryKeys;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }



    public List<List<String>> getColumns() {
        return columns;
    }

    public void setColumns(List<List<String>> columns) {
        this.columns = columns;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
