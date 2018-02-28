package com.dell.inventoryplay.main.inquiry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@SuppressLint("ViewConstructor")
public class TableRowLayout extends RelativeLayout {


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

    List<TableDataObject> tableDataObjects;


    int headerCellsWidth[];
    InquirySvcTagFragment inquirySvcTagFragment;

    public TableRowLayout(InquirySvcTagFragment inquirySvcTagFragment, Context context, ArrayList<String> columnList, ArrayList<ArrayList<String>> valueList) {

        super(context);
        this.context = context;
        this.columnList = columnList;
        this.valueList = valueList;
        this.inquirySvcTagFragment = inquirySvcTagFragment;
        tableDataObjects = this.tableDataObject();
        headerCellsWidth = new int[columnList.size() + 1];
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
        for (int x = 0; x < valueList.size(); x++) {

            TableDataObject rowObj = new TableDataObject(valueList.get(x));

            tableDataObject.add(rowObj);
        }

        return tableDataObject;

    }

    public void setScrollPos(int posX, int posY) {
        this.scrollViewD.scrollTo(0, 0);
        // this.scrollViewC.scrollTo(0, posY);
        //this.horizontalScrollViewB.scrollTo(posX, 0);
        this.horizontalScrollViewD.scrollTo(posX, 0);

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
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
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
        TextView textView = this.headerTextView("SNo");
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
        int headerFieldCount = this.columnList.size();

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);
        int bgClr = context.getResources().getColor(R.color.colorPrimary);
        for (int x = 0; x < headerFieldCount; x++) {
            TextView textView = this.headerTextView(this.columnList.get(x));
            textView.setLayoutParams(params);
            //  textView.setBackgroundColor(bgClr);
            Helper.getInstance(context).createTableHeaderShape(textView);
            textView.setTextColor(bgClr);
            textView.setTypeface(Typeface.DEFAULT_BOLD);

            int px = Helper.getInstance(context).dpToPx(5);

            Drawable image = context.getResources().getDrawable(android.R.drawable.arrow_up_float);
            int h = image.getIntrinsicHeight();
            int w = image.getIntrinsicWidth();
            textView.setPadding(px, px, w + px, px);


            Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
            transparentDrawable.setBounds(0, 0, w, h);
            textView.setCompoundDrawables(transparentDrawable, null, null, null);

            //   textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_radio, 0, 0, 0);


         /*   if (x == Integer.parseInt(index))
                textView.setTag(sortType + "-" + index);
            else*/
            textView.setTag("0" + "-" + x);
            textView.setOnClickListener(view -> {
                inquirySvcTagFragment.showProgressBar();
                Single.create((SingleOnSubscribe<Integer>) e -> doInBg(view))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> updateView());




            });

            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }
    public void updateView(){
        inquirySvcTagFragment.hideProgressBar();
    }
public  void doInBg(View view){
    String tag = (String) view.getTag();
    resetDrawable(view);
    String[] arr = tag.split("-");
    String sortType = arr[0];
    String index = arr[1];
    if (sortType.contains("2")) {
        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float, 0, 0, 0);

        String myTag = "1" + "-" + index;
        view.setTag(myTag);
    } else {
        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float, 0, 0, 0);
        String myTag = "2" + "-" + index;
        view.setTag(myTag);

    }
    inquirySvcTagFragment.sortCol(sortType, index, posX, posY);
}
    public void resetDrawable(View view) {
        ViewGroup vg = (ViewGroup) view.getParent();
        int cnt = vg.getChildCount();
        Drawable image = context.getResources().getDrawable(android.R.drawable.arrow_up_float);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();

        for (int i = 0; i < cnt; i++) {

            TextView textView = (TextView) vg.getChildAt(i);
            Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
            transparentDrawable.setBounds(0, 0, w, h);
            textView.setCompoundDrawables(transparentDrawable, null, null, null);

            // textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_radio, 0, 0, 0);


        }


    }

    public void regenerateTableD(ArrayList<ArrayList<String>> valueList) {
        this.valueList = valueList;
        this.tableC.removeAllViews();
        this.tableD.removeAllViews();
        int slNo = 1;
        List<TableDataObject> tableDataObject = new ArrayList<>();
        for (int x = 0; x < valueList.size(); x++) {
            TableDataObject rowObj = new TableDataObject(valueList.get(x));
            tableDataObject.add(rowObj);
        }
        this.tableDataObjects = tableDataObject;

        for (TableDataObject sampleObject : this.tableDataObjects) {
            TableRow tableRowForTableC = this.tableRowForTableC(slNo);

            TableRow taleRowForTableD = this.taleRowForTableD(sampleObject, slNo);
            taleRowForTableD.setBackgroundColor(Color.LTGRAY);
            tableRowForTableC.setBackgroundColor(Color.LTGRAY);
            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
            slNo++;
        }

        this.resizeBodyTableRowHeight();

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
        TextView textView = this.bodyTextView("" + slNo);
        if (slNo % 2 == 0)
            textView.setBackgroundResource(R.drawable.border1);
        else
            textView.setBackgroundResource(R.drawable.border2);
        textView.setMaxLines(1);
        textView.setTag(0);
        textView.setPadding(px, px, px, px);
        textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_off_background, 0, 0, 0);

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
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;
    }

    TableRow taleRowForTableD(TableDataObject sampleObject, int slNo) {
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        ArrayList<String> myList = sampleObject.valueList;
        //  String info[] =  sampleObject.valueList.get(0);
        String info[] = myList.toArray(new String[0]);

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(info[x]);
            if (slNo % 2 == 0)
                textViewB.setBackgroundResource(R.drawable.border1);
            else
                textViewB.setBackgroundResource(R.drawable.border2);

            textViewB.setTag(slNo);

            textViewB.setOnClickListener(view -> {
                int tag1 = (int) view.getTag();
                ViewGroup vg = (ViewGroup) this.tableC.getChildAt(tag1 - 1);
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
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
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
                horizontalScrollViewD.smoothScrollTo(l, 0);
            } else {
                horizontalScrollViewB.smoothScrollTo(l, 0);
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
                scrollViewD.smoothScrollTo(0, t);
            } else {
                scrollViewC.smoothScrollTo(0, t);
            }
        }
    }


}