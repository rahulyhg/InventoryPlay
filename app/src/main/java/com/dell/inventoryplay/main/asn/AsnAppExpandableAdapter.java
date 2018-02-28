package com.dell.inventoryplay.main.asn;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.response.AsnAppDetailsResponse;
import com.dell.inventoryplay.response.AsnResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasikanta_Sahoo on 12/15/2017.
 * AsnAppExpandableAdapter
 */

public class AsnAppExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AsnResponse.Records> listDataHeader;
    private ArrayMap<AsnResponse.Records, ArrayList<AsnAppDetailsResponse.Records>> listDataChild;

    AsnAppExpandableAdapter(Context context, List<AsnResponse.Records> listDataHeader,
                            ArrayMap<AsnResponse.Records, ArrayList<AsnAppDetailsResponse.Records>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup parent) {
        AsnResponse.Records obj = (AsnResponse.Records) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater != null ? layoutInflater.inflate(R.layout.inflate_asn_app_list, parent, false) : null;
        }
        if (convertView != null) {
            ImageView msgType = convertView.findViewById(R.id.msgType);
            TextView appName = convertView.findViewById(R.id.appName);
            TextView dateTime = convertView.findViewById(R.id.dateTime);
            LinearLayout itemContainer = convertView.findViewById(R.id.itemContainer);


            appName.setText(obj.getAppName());
            dateTime.setText(obj.getDateTime());
            if (obj.getMsgType().contains("SUCCESS")) {
                msgType.setBackgroundResource(R.drawable.ic_success_vector);
            } else {
                msgType.setBackgroundResource(R.drawable.ic_error_vector);
            }
            itemContainer.setTag(obj);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final AsnAppDetailsResponse.Records obj = (AsnAppDetailsResponse.Records) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater != null ? layoutInflater.inflate(R.layout.inflate_asn_app_details_list, parent, false) : null;

        }


        if (convertView != null) {
            TextView name = convertView.findViewById(R.id.name);
            TextView dateTime = convertView.findViewById(R.id.dateTime);
            TextView error = convertView.findViewById(R.id.error);
            name.setText(obj.getSubTransName());
            dateTime.setText(obj.getProcessingDateTime());

            if (obj.getMsgType().contains("ERROR")) {
                error.setVisibility(View.VISIBLE);
            } else {
                error.setVisibility(View.GONE);
            }
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
