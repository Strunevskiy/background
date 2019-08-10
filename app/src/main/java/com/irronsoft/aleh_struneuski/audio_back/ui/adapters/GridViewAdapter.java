package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

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

    public GridViewAdapter(Context mContext, int layoutResourceId, List<PlayList> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(List<PlayList> mGridData) {
        this.clear();
        this.addAll(mGridData);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            setDimensOfCardViewItem(row);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        PlayList playList = getItem(position);
        holder.titleTextView.setText(playList.getDescription());

        Picasso.with(holder.imageView.getContext()).load(playList.getArtworkUrl()).fit().into(holder.imageView);

        return row;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        PlayList playList = getItem(position);
        List<Track> tracks = playList.getTracks();

        Intent playerListActivity = new Intent(getContext(), PlayerListActivity.class);
        playerListActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        playerListActivity.putParcelableArrayListExtra("album_track_list", (ArrayList<? extends Parcelable>) tracks);
        getContext().startActivity(playerListActivity);
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

    public static class ViewHolder {
        public TextView titleTextView;
        public ImageView imageView;
    }
}