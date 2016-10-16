package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.PlayerListActivity;
import com.irronsoft.aleh_struneuski.audio_back.utils.ResolutionUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alehstruneuski on 9/6/16.
 */
public class GridViewAdapter extends ArrayAdapter<PlayList> implements AdapterView.OnItemClickListener {

    private Context mContext;
    private int layoutResourceId;
    private List<PlayList> mGridData = new ArrayList<PlayList>();

    public static class ViewHolder {
        public TextView titleTextView;
        public ImageView imageView;
    }

    public GridViewAdapter(Context mContext, int layoutResourceId, List<PlayList> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(List<PlayList> mGridData) {
        this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            setDimensOfCardViewItem(row);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        PlayList playList = mGridData.get(position);
        holder.titleTextView.setText(playList.getPermalink());

        Picasso.with(holder.imageView.getContext()).load(playList.getArtworkUrl()).fit().into(holder.imageView);

        return row;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlayList playList = mGridData.get(position);
        List<Track> tracks =  playList.getTracks();

        Intent playerListActivity = new Intent(mContext, PlayerListActivity.class);
        playerListActivity.putParcelableArrayListExtra("album_track_list", (ArrayList<? extends Parcelable>) tracks);
        mContext.startActivity(playerListActivity);
    }


    private void setDimensOfCardViewItem(View row) {
        float hightPer = 27.34375f;
        float widthPer = 48.61111111111111f;

        Context cntx = row.getContext();
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) row.getLayoutParams();
        layoutParams.height = ResolutionUtils.convertPercentToPixelHight(cntx, hightPer);
        layoutParams.width = ResolutionUtils.convertPercentToPixelWidth(cntx, widthPer);
        row.setLayoutParams(layoutParams);
    }
}