package com.dell.inventoryplay.main.inquiry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


@SuppressLint("ViewConstructor")
public class TableColumnLayout extends RelativeLayout {


    public TableLayout tableA, tableB, tableC, tableD;
    public HorizontalScrollView horizontalScrollViewB, horizontalScrollViewD;
    public ScrollView scrollViewC, scrollViewD;
    private Context context;
    private ArrayList<String> columnList;
    private ArrayList<ArrayList<String>> valueList;
    private List<TableDataObject> tableDataObjects;
    private int headerCellsWidth[];
    private String[][] myNewArr;


    public TableColumnLayout(Context context, ArrayList<String> columnList, ArrayList<ArrayList<String>> valueList) {

        super(context);
        this.context = context;
        this.columnList = columnList;
        this.valueList = valueList;
        int M = this.valueList.size();
        int N = this.columnList.size();
        myNewArr = new String[N][M];
        for (int i = 0; i < this.valueList.size(); i++) {
            ArrayList<String> vals = this.valueList.get(i);
            for (int j = 0; j < vals.size(); j++) {
                String rowVal = vals.get(j);
                myNewArr[j][i] = rowVal;
            }
        }
        tableDataObjects = this.tableDataObject();
        headerCellsWidth = new int[valueList.size() + 1];
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);
        this.addComponentToMainLayout();
        this.addTableRowToTableA();
        this.addTableRowToTableB();
        this.resizeHeaderHeight();
        this.getTableRowHeaderCellWidth();
        this.generateTableC_AndTable_B();
        this.resizeBodyTableRowHeight();
    }

    List<TableDataObject> tableDataObject() {
        List<TableDataObject> tableDataObject = new ArrayList<>();

        for (String[] aMyNewArr : myNewArr) {
            TableDataObject rowObj = new TableDataObject(aMyNewArr);

            tableDataObject.add(rowObj);
        }

        return tableDataObject;

    }

    private void initComponents() {

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        horizontalScrollViewB.setVerticalScrollBarEnabled(false);
        horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);
        scrollViewC.setVerticalScrollBarEnabled(false);
        scrollViewC.setHorizontalScrollBarEnabled(false);
        scrollViewD.setVerticalScrollBarEnabled(false);
        scrollViewD.setHorizontalScrollBarEnabled(false);

    }

    private void setComponentsId() {
        this.tableA.setId(R.id.inquiry_id1);
        this.horizontalScrollViewB.setId(R.id.inquiry_id2);
        this.scrollViewC.setId(R.id.inquiry_id3);
        this.scrollViewD.setId(R.id.inquiry_id4);
    }

    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    private void addComponentToMainLayout() {
        LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }


    private void addTableRowToTableA() {
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        this.tableB.addView(this.componentBTableRow());
    }

    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView("     Field List      ");
        int bgClr = context.getResources().getColor(R.color.colorPrimary);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        Helper.getInstance(context).createTableHeaderShape(textView);
        textView.setTextColor(bgClr);
        int px = Helper.getInstance(context).dpToPx(10);
        textView.setPadding(2 * px, px, 2 * px, px);
        componentATableRow.addView(textView);
        return componentATableRow;
    }

    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.valueList.size();

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);
        int bgClr = context.getResources().getColor(R.color.colorPrimary);
        for (int x = 0; x < headerFieldCount; x++) {
            TextView textView = this.headerTextView("       SlNo:" + (x + 1) + "        ");
            textView.setLayoutParams(params);
            Helper.getInstance(context).createTableHeaderShape(textView);
            textView.setTextColor(bgClr);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {
        int slNo = 1;
        for (TableDataObject sampleObject : this.tableDataObjects) {

            TableRow tableRowForTableC = this.tableRowForTableC(slNo);
            TableRow taleRowForTableD = this.taleRowForTableD(sampleObject, slNo);

            tableRowForTableC.setBackgroundColor(Color.LTGRAY);
            taleRowForTableD.setBackgroundColor(Color.LTGRAY);

            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
            slNo++;
        }
    }

    TableRow tableRowForTableC(int slNo) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        int px = Helper.getInstance(context).dpToPx(10);
        TextView textView = this.bodyTextView("" + columnList.get(slNo - 1));
        textView.setTag(0);
        textView.setPadding(px, px, px, px);
        textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_off_background, 0, 0, 0);

        textView.setBackgroundResource(R.drawable.border3);


        textView.setOnClickListener(view -> {
            int tag = (int) view.getTag();
            if (tag == 1) {
                view.setTag(0);
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_off_background, 0, 0, 0);

            } else {
                view.setTag(1);
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_on_background, 0, 0, 0);

            }

        });


        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow taleRowForTableD(TableDataObject sampleObject, int slNo) {
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        String[] info = sampleObject.valueList1;
        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(info[x]);
            if (x % 2 == 0) {
                textViewB.setBackgroundResource(R.drawable.border1);
            } else {
                textViewB.setBackgroundResource(R.drawable.border2);
            }
            DataTag dataObj = new DataTag();
            dataObj.col = slNo;
            dataObj.row = x;
            dataObj.text = info[x];
            textViewB.setTag(dataObj);
            textViewB.setOnLongClickListener(view -> {

                DataTag tag1 = (DataTag) view.getTag();
                Timber.e("Long click" + tag1.row + "--" + tag1.col + "--" + tag1.text);
                return true;
            });
            textViewB.setOnClickListener(view -> {
                Timber.e("short click");
                DataTag tag1 = (DataTag) view.getTag();
                ViewGroup vg = (ViewGroup) this.tableC.getChildAt(tag1.col - 1);
                View chkView = vg.getChildAt(0);
                int tag = (int) chkView.getTag();
                if (tag == 1) {
                    chkView.setTag(0);
                    ((TextView) chkView).setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_off_background, 0, 0, 0);

                } else {
                    chkView.setTag(1);
                    ((TextView) chkView).setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_on_background, 0, 0, 0);

                }

            });
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;
    }

    private static class DataTag {
        int row, col;
        String text;
    }

    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        int px = Helper.getInstance(context).dpToPx(10);
        bodyTextView.setPadding(px, px, px, px);

        return bodyTextView;
    }

    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        int px = Helper.getInstance(context).dpToPx(10);
        headerTextView.setPadding(px, px, px, px);
        return headerTextView;
    }

    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();


        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableC.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    private int viewHeight(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    private int viewWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();
            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.smoothScrollTo(l, 0);
            } else {
                horizontalScrollViewB.smoothScrollTo(l, 0);
            }
        }

    }

    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();
            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.smoothScrollTo(0, t);
            } else {
                scrollViewC.smoothScrollTo(0, t);
            }
        }
    }


}