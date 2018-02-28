package com.dell.inventoryplay.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.AsnResponse;
import com.dell.inventoryplay.utils.AppConstants;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 12/15/2017.
 * AppListAdapter
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.CustomViewHolder> {
    String mInputAsnId, mInputAppName, mInputTransType, mInputDateTime, mInputMsgType;

    AsnResponse res;
    private MainActivity mActivity;
    int count = 0;
    ArrayList<AsnResponse.Records> mRecordList;


    public AppListAdapter(MainActivity mContext, AsnResponse res, String inputTransType) {



        mActivity = mContext;
        this.res=res;
        mRecordList = res.getRecords();

        count = mRecordList.size();
        mInputTransType = inputTransType;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_asn_app_list, parent, false);
        return new CustomViewHolder(v);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        AsnResponse.Records obj = mRecordList.get(position);

        holder.appName.setText(obj.getAppName());
        holder.dateTime.setText(obj.getDateTime());
        if (obj.getMsgType().contains("SUCCESS")) {
            holder.msgType.setImageResource(R.drawable.ic_success_vector);
        } else {
            holder.msgType.setImageResource(R.drawable.ic_error_vector);
        }
        holder.itemContainer.setTag(obj);
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mActivity, "done", Toast.LENGTH_SHORT).show();
                mInputAppName = ((AsnResponse.Records) view.getTag()).getAppName();
                mInputDateTime = ((AsnResponse.Records) view.getTag()).getDateTime();
                mInputMsgType = ((AsnResponse.Records) view.getTag()).getMsgType();
                resultPage();
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView appName, dateTime;
        private LinearLayout itemContainer;
        private ImageView msgType;

        CustomViewHolder(View itemView) {
            super(itemView);
            msgType= itemView.findViewById(R.id.msgType);
            appName = itemView.findViewById(R.id.appName);
            dateTime = itemView.findViewById(R.id.dateTime);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }
    }


    public void resultPage() {
        Bundle bundle = new Bundle();
        bundle.putString("ASN_ID", mInputAsnId);
        bundle.putString("APP_NAME", mInputAppName);
        bundle.putString("TRANS_TYPE", mInputTransType);
        bundle.putSerializable("HEADER",res.getHeaderInfo());


        FragmentManager fm = mActivity.getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_TRACK_ASN_APP_DETAILS_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (frag == null)
            frag = TrackAsnAppDetailsFragment.newInstance(mActivity);
        ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_TRACK_ASN_APP_DETAILS_TAG);
        ft.addToBackStack(AppConstants.FRAG_TRACK_ASN_APP_DETAILS_TAG);
        frag.setArguments(bundle);
        ft.commit();
    }

}
