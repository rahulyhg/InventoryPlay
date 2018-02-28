package com.dell.inventoryplay.main.inquiry;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 1/6/2018.
 * TableDataObject
 */

public class TableDataObject {

    public ArrayList<String> valueList;
    public String[] valueList1;

    public TableDataObject(ArrayList<String> valueList) {

        this.valueList = valueList;

    }
    public TableDataObject(String[] valueList) {

        this.valueList1 = valueList;

    }
}
