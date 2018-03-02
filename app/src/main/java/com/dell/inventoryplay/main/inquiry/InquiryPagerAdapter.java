package com.dell.inventoryplay.main.inquiry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dell.inventoryplay.R;

/**
 * Created by Sasikanta_Sahoo on 1/3/2018.
 * InquiryPagerAdapter
 */

class InquiryPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;

    InquiryPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm);
        pageTitle = activity.getResources().getStringArray(R.array.tab_scan_svc);


    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = InquiryPagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(InquiryPagerFragment.ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];

        /*SpannableStringBuilder sb = new SpannableStringBuilder(pageTitle[position]); // space added before text for convenience
        Drawable myDrawable = pageIcon.get(position);
        myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
        */

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}