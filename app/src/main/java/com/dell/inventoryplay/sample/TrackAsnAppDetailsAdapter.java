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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.response.AsnAppDetailsResponse;
import com.dell.inventoryplay.response.AsnResponse;
import com.dell.inventoryplay.utils.AppConstants;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 12/2/2017.
 * TrackAsnAppDetailsAdapter
 */

public class TrackAsnAppDetailsAdapter extends RecyclerView.Adapter<TrackAsnAppDetailsAdapter.MyViewHolder> {

    ArrayList<AsnAppDetailsResponse.Records> recordsArrayList;
    String inputAsnId, inputAppName, inputTransType, inputSubTransName, inputSvcTag, inputRegion, inputTransFlag, inputDateTime, inputMsgType, inputSubTransDate;
    MainActivity mContext;
    ArrayList<AsnResponse.HeaderInfo> headerInfo;
    public TrackAsnAppDetailsAdapter(MainActivity mContext, ArrayList<AsnAppDetailsResponse.Records> recordsArrayList, String inputAsnId, String inputAppName, String inputTransType, ArrayList<AsnResponse.HeaderInfo> headerInfo) {
        this.recordsArrayList = recordsArrayList;
        this.inputAsnId = inputAsnId;
        this.inputAppName = inputAppName;
        this.inputTransType = inputTransType;

        this.headerInfo = headerInfo;
        this.mContext = mContext;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, dateTime;
        ImageView statusView;
        public RelativeLayout viewBackground;
        public RelativeLayout viewForeground;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            dateTime = view.findViewById(R.id.dateTime);
            statusView = view.findViewById(R.id.statusView);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    @Override
    public TrackAsnAppDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_track_asn_app_details, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(TrackAsnAppDetailsAdapter.MyViewHolder holder, int position) {
        AsnAppDetailsResponse.Records obj = recordsArrayList.get(position);
        holder.name.setText(obj.getSubTransName());
        holder.dateTime.setText(obj.getProcessingDateTime());
        if (obj.getMsgType().contains("ERROR")) {
            holder.statusView.setImageResource(R.drawable.ic_success_vector);
        } else {
            holder.statusView.setImageResource(R.drawable.ic_error_vector);
        }
        holder.viewForeground.setTag(obj);
        holder.viewForeground.setOnClickListener(view -> {
            inputSubTransName = ((AsnAppDetailsResponse.Records) view.getTag()).getSubTransName();
            inputSubTransDate = ((AsnAppDetailsResponse.Records) view.getTag()).getProcessingDateTime();
            resultPage();
        });
    }

    public void resultPage() {
        Bundle bundle = new Bundle();
        bundle.putString("ASN_ID", inputAsnId);
        bundle.putString("APP_NAME", inputAppName);
        bundle.putString("TRANS_TYPE", inputTransType);
        bundle.putString("SUB_TRANS_NAME", inputSubTransName);
        bundle.putSerializable("HEADER", headerInfo);


        FragmentManager fm = mContext.getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_TRACK_ASN_APP_ERROR_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (frag == null)
            frag = TrackAsnAppErrorFragment.newInstance(mContext);
        ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_TRACK_ASN_APP_ERROR_TAG);
        ft.addToBackStack(AppConstants.FRAG_TRACK_ASN_APP_ERROR_TAG);
        frag.setArguments(bundle);
        ft.commit();
    }

    @Override
    public int getItemCount() {
        return recordsArrayList.size();
    }

    public void removeItem(int position) {
        recordsArrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(AsnAppDetailsResponse.Records item, int position) {
        recordsArrayList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
