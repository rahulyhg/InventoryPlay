package com.dell.inventoryplay.main.checkpoint;

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
import com.dell.inventoryplay.main.inquiry.TableDataObject;
import com.dell.inventoryplay.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


@SuppressLint("ViewConstructor")
public class CheckPointTableColumnLayout extends RelativeLayout {


    public TableLayout tableA;
    public TableLayout tableB;
    public TableLayout tableC;
    public TableLayout tableD;

    public HorizontalScrollView horizontalScrollViewB;
    public HorizontalScrollView horizontalScrollViewD;

    public ScrollView scrollViewC;
    public ScrollView scrollViewD;
    int posX = 0, posY = 0;
    Context context;
    private ArrayList<String> columnList;
    private ArrayList<ArrayList<String>> valueList;
    // ArrayList<Integer> ids;
    List<TableDataObject> tableDataObjects;


    int headerCellsWidth[];
    String[][] myNewArr;


    public CheckPointTableColumnLayout(Context context, ArrayList<String> columnList, ArrayList<ArrayList<String>> valueList) {

        super(context);
        this.context = context;
        this.columnList = columnList;

        this.valueList = valueList;

        //  ArrayList<ArrayList<String>> newArrList=new ArrayList<>();
        // ArrayList newArrSubList=new ArrayList<>();
        int M = this.valueList.size();
        int N = this.columnList.size();
        myNewArr = new String[N][M];
        for (int i = 0; i < this.valueList.size(); i++) {
            ArrayList<String> vals = this.valueList.get(i);
            for (int j = 0; j < vals.size(); j++) {
                String rowVal = vals.get(j);
                myNewArr[j][i] = rowVal;
                //  newArrSubList.add(i,rowVal);
                // newArrList.add(j,newArrSubList);

            }
        }
        // ArrayList<ArrayList<String>> newList = twoDArrayToList(myNewArr);

        //  this.inquirySvcTagFragment = inquirySvcTagFragment;
        tableDataObjects = this.tableDataObject();
        headerCellsWidth = new int[valueList.size() + 1];
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        // this.setBackgroundColor(Color.RED);


        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC_AndTable_B();

        this.resizeBodyTableRowHeight();
    }

    // this is just the sample data
    List<TableDataObject> tableDataObject() {

        List<TableDataObject> tableDataObject = new ArrayList<>();

        // use max of 200/300 record. or else time out
        for (String[] aMyNewArr : myNewArr) {
            TableDataObject rowObj = new TableDataObject(aMyNewArr);

            tableDataObject.add(rowObj);
        }

        return tableDataObject;

    }


    // initalized components
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

        //  this.tableA.setBackgroundColor(Color.GREEN);
        //   this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

    }

    // set essential component IDs
    private void setComponentsId() {
        this.tableA.setId(R.id.inquiry_id1);
        this.horizontalScrollViewB.setId(R.id.inquiry_id2);
        this.scrollViewC.setId(R.id.inquiry_id3);
        this.scrollViewD.setId(R.id.inquiry_id4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout() {

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        //  this.tableD.setBackgroundResource(R.drawable.border2);
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

    // generate table row of table A
    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView("     Field List      ");
        int bgClr = context.getResources().getColor(R.color.colorPrimary);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        //  textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        Helper.getInstance(context).createTableHeaderShape(textView);

        textView.setTextColor(bgClr);
        int px = Helper.getInstance(context).dpToPx(10);
        textView.setPadding(2 * px, px, 2 * px, px);
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.valueList.size();

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);
        int bgClr = context.getResources().getColor(R.color.colorPrimary);
        for (int x = 0; x < headerFieldCount; x++) {
            TextView textView = this.headerTextView("       SlNo:" + (x + 1) + "        ");
            textView.setLayoutParams(params);
            //  textView.setBackgroundColor(bgClr);
            Helper.getInstance(context).createTableHeaderShape(textView);
            textView.setTextColor(bgClr);
            textView.setTypeface(Typeface.DEFAULT_BOLD);


            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }


    // generate table row of table C and table D
    private void generateTableC_AndTable_B() {

        // just seeing some header cell width
      /*  for (int x = 0; x < this.headerCellsWidth.length; x++) {
            Log.v("TableMainLayout.java", this.headerCellsWidth[x] + "");
        }*/
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

    // a TableRow for table C
    TableRow tableRowForTableC(int slNo) {



        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        int px = Helper.getInstance(context).dpToPx(10);
        TextView textView = this.bodyTextView("" + columnList.get(slNo - 1));
        textView.setTag(0);
        textView.setPadding(px, px, px, px);
       // textView.setGravity(Gravity.CENTER|Gravity.START);
        //  textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_off_background, 0, 0, 0);

       // textView.setBackgroundResource(R.drawable.border3);



/*
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
*/

        textView.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow taleRowForTableD(TableDataObject sampleObject, int slNo) {
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        String[] info = sampleObject.valueList1;
        //  String info[] =  sampleObject.valueList.get(0);
        //  String info[] = myList.toArray(new String[0]);

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(info[x]);
            if (x % 2 == 0) {
                textViewB.setBackgroundResource(R.drawable.border1);
                //taleRowForTableD.setBackgroundResource(R.drawable.border1);
            } else {
                textViewB.setBackgroundResource(R.drawable.border2);
                // taleRowForTableD.setBackgroundResource(R.drawable.border2);
            }
            if(slNo==7){
                textViewB.setTypeface(Typeface.DEFAULT_BOLD);
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
           /* textViewB.setOnClickListener(view -> {
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

            });*/
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;
    }

    private static class DataTag {
        int row, col;
        String text;
    }

    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        int px = Helper.getInstance(context).dpToPx(10);
        bodyTextView.setPadding(px, px, px, px);

        return bodyTextView;
    }
    TextView bodyTextViewWithGravity(String label) {
       // TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL);
       // params.setMargins(0, 2, 0, 0);

        TextView bodyTextView = new TextView(this.context);
      //  bodyTextView.setLayoutParams(params);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setBackgroundColor(Color.RED);
        int px = Helper.getInstance(context).dpToPx(10);
        bodyTextView.setPadding(px, px, px, px);

        return bodyTextView;
    }
    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        int px = Helper.getInstance(context).dpToPx(10);
        headerTextView.setPadding(px, px, px, px);
        //  headerTextView.setPadding(5, 5, 5, 5);
        return headerTextView;
    }

    // resizing TableRow height starts here
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

    // resize body table row height
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

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
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

    // read a view's height
    private int viewHeight(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();
            posX = l;
            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
              //  horizontalScrollViewD.smoothScrollTo(l, 0);
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();
            posY = t;
            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }


}