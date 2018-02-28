package com.dell.inventoryplay.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.AsnResponse;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/28/2017.
 * Helper
 */

public class Helper {
    private static Helper singleton;
    private static Context mContext;

    public static Helper getInstance(Context context) {
        if (singleton == null)
            singleton = new Helper();
        mContext = context;
        return singleton;
    }

    public int getOrientation() {
        return mContext.getResources().getConfiguration().orientation;
    }

    public void createTableHeaderShape(View view) {
        try {
            ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {

                    LinearGradient lg = new LinearGradient(0, 0, 0, view.getHeight(),
                            new int[]{
                                    mContext.getResources().getColor(R.color.row1Start),
                                    mContext.getResources().getColor(R.color.row1End)},
                            new float[]{
                                    0, 0.45f},
                            Shader.TileMode.MIRROR);
                    return lg;
                }
            };
            PaintDrawable p = new PaintDrawable();
            p.setShape(new RectShape());
            p.setShaderFactory(sf);
            view.setBackground((Drawable) p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createShape(View view) {
        try {
            ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {

                    LinearGradient lg = new LinearGradient(0, 0, 0, view.getHeight(),
                            new int[]{
                                    mContext.getResources().getColor(R.color.blockTitleStart),
                                    mContext.getResources().getColor(R.color.blockTitleEnd)},
                            new float[]{
                                    0, 0.45f},
                            Shader.TileMode.MIRROR);
                    return lg;
                }
            };
            PaintDrawable p = new PaintDrawable();
            p.setShape(new RectShape());
            p.setShaderFactory(sf);
            view.setBackground((Drawable) p);
            ((TextView) view).setTextColor(Color.WHITE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int setAnimation(View viewToAnimate, int position, int lastPosition) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation;

            animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);

            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
        return lastPosition;
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return (info != null && info.isConnected());
    }
    public Bitmap loadBitmapFromView(View v) {
        Bitmap b = null;
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }
    public void combineImages(Bitmap c, Bitmap s, String fileName) {

        if (c != null && s != null) {
            Bitmap cs = null;
            int width, height = 0;
            if (c.getWidth() > s.getWidth()) {
                width = c.getWidth();

            } else {
                width = s.getWidth();

            }
            height = c.getHeight() + s.getHeight();
            cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas comboImage = new Canvas(cs);
            comboImage.drawColor(Color.WHITE);
            comboImage.drawBitmap(c, 0f, 0f, null);
            comboImage.drawBitmap(s, 0, c.getHeight(), null);

            writeFile(cs,fileName);
        }
    }
    public void singleImages(Bitmap c, String fileName) {
        if (c != null) {
            Bitmap bitmap = null;
            int width, height = 0;
            width = c.getWidth();
            height = c.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas comboImage = new Canvas(bitmap);
            comboImage.drawBitmap(c, 0f, 0f, null);

            writeFile(bitmap, fileName);
        }
    }
    public void writeFile(Bitmap bitmap, String fileName) {
        File dir = new File(mContext.getFilesDir(), "images");
        dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void showToast(String msg, int showType, int toastType) {
        int type = FancyToast.SUCCESS;
        switch (toastType) {
            case 1:
                type = FancyToast.SUCCESS;
                break;
            case 2:
                type = FancyToast.WARNING;
                break;
            case 3:
                type = FancyToast.ERROR;
                break;
            case 4:
                type = FancyToast.INFO;
                break;
            case 5:
                type = FancyToast.DEFAULT;
                break;
            case 6:
                type = FancyToast.CONFUSING;
                break;

        }
        Toast toast = FancyToast.makeText(mContext, msg, showType, type, false);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup toastView = (ViewGroup) toast.getView();
        TextView toast_text = toastView.findViewById(R.id.toast_text);
        toast_text.setTypeface(Typeface.DEFAULT_BOLD);
        toast_text.setTextColor(mContext.getResources().getColor(R.color.white));
        toast.show();
    }

    public ArrayList<Integer> getChartColor() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(mContext.getResources().getColor(R.color.chart1));
        colors.add(mContext.getResources().getColor(R.color.chart2));
        colors.add(mContext.getResources().getColor(R.color.chart3));
        colors.add(mContext.getResources().getColor(R.color.chart4));
        colors.add(mContext.getResources().getColor(R.color.chart5));
        colors.add(mContext.getResources().getColor(R.color.chart6));
        colors.add(mContext.getResources().getColor(R.color.chart7));
        colors.add(mContext.getResources().getColor(R.color.chart8));
        colors.add(mContext.getResources().getColor(R.color.chart9));
        colors.add(mContext.getResources().getColor(R.color.chart10));
        return colors;

    }

    public void setHeaderInfo(LinearLayout headerContainer, ArrayList<AsnResponse.HeaderInfo> headerInfo) {
        int maxVisible=Helper.getInstance(mContext).getOrientation()==2?2:4;
        headerContainer.removeAllViews();
        int cnt = 0;
        for (AsnResponse.HeaderInfo obj : headerInfo) {
            ViewGroup infoView = (ViewGroup) ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.header_info, null, false);
            TextView title = infoView.findViewById(R.id.title);
            TextView value = infoView.findViewById(R.id.value);
            title.setText(obj.getKey());
            value.setText(obj.getValue());

            if (cnt == maxVisible && headerInfo.size() > maxVisible) {
                TextView more = new TextView(mContext);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                more.setLayoutParams(param);
                more.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                more.setText("more");
                more.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                //  more.setTypeface(Typeface.DEFAULT_BOLD);
                more.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(android.R.drawable.arrow_down_float), null);

                headerContainer.addView(more);
                more.setOnClickListener(view -> moreLess(1, headerContainer));
            }

            if (headerInfo.size() > maxVisible && cnt >=maxVisible) {
                infoView.setVisibility(View.GONE);
            }
            headerContainer.addView(infoView);
            cnt++;
        }
        if (headerInfo.size() > maxVisible) {

            TextView less = new TextView(mContext);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            less.setLayoutParams(param);
            less.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            less.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(android.R.drawable.arrow_up_float), null);
            less.setText("less");
            less.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            //  less.setTypeface(Typeface.DEFAULT_BOLD);
            less.setVisibility(View.GONE);
            headerContainer.addView(less);

            less.setOnClickListener(view -> moreLess(0, headerContainer));
        }


    }

    private void moreLess(int type, LinearLayout headerContainer) {
        int child = headerContainer.getChildCount();
        int maxVisible=Helper.getInstance(mContext).getOrientation()==2?2:4;
        for (int i = maxVisible; i < child; i++) {
            if (type == 1) {
                headerContainer.getChildAt(i).setVisibility(View.VISIBLE);
            } else if (type == 0) {
                headerContainer.getChildAt(i).setVisibility(View.GONE);
            }
        }
        if (type == 1) {
            headerContainer.getChildAt(maxVisible).setVisibility(View.GONE);
            headerContainer.getChildAt(child - 1).setVisibility(View.VISIBLE);
        } else {
            headerContainer.getChildAt(maxVisible).setVisibility(View.VISIBLE);
            headerContainer.getChildAt(child - 1).setVisibility(View.GONE);
        }

    }
}
