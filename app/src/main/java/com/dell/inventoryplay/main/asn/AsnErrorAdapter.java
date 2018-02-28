package com.dell.inventoryplay.main.asn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.response.AsnAppErrorResponse;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 12/2/2017.
 * AsnErrorAdapter
 */

public class AsnErrorAdapter extends RecyclerView.Adapter<AsnErrorAdapter.MyViewHolder> {

    private ArrayList<AsnAppErrorResponse.Records> recordsArrayList;

    public AsnErrorAdapter(ArrayList<AsnAppErrorResponse.Records> recordsArrayList) {
        this.recordsArrayList = recordsArrayList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView errorMsg, dateTime, svcTag;
        ImageView status;

        MyViewHolder(View view) {
            super(view);
            errorMsg = view.findViewById(R.id.errorMsg);
            dateTime = view.findViewById(R.id.dateTime);
            status = view.findViewById(R.id.status);
            svcTag = view.findViewById(R.id.svcTag);

        }
    }


    @Override
    public AsnErrorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_track_asn_app_error, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AsnErrorAdapter.MyViewHolder holder, int position) {
        AsnAppErrorResponse.Records obj = recordsArrayList.get(position);
        holder.errorMsg.setText(obj.getErrorMsg());
        holder.svcTag.setText(obj.getSvcTag());
        holder.dateTime.setText(obj.getDateTime());
        if (obj.getRecordStatus().contains("O")) {
            holder.status.setImageResource(R.drawable.ic_success_vector);
        } else if (obj.getRecordStatus().contains("C")) {
            holder.status.setImageResource(R.drawable.ic_error_vector);
        } else if (obj.getRecordStatus().contains("S")) {
            holder.status.setImageResource(R.drawable.ic_success_vector);
        }


    }

    @Override
    public int getItemCount() {
        return recordsArrayList.size();
    }


}
