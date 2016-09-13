package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.PlayerListActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.AlbumViewHolder> {

    private Context mContext;
    private List<PlayList> playLists;


    public PlayListAdapter(Context mContext) {
        this.playLists = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setPlayList(List<PlayList> albumList) {
        this.playLists = albumList;
        notifyItemRangeChanged(0, albumList.size());
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public AlbumViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_card, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position) {
        PlayList playList = playLists.get(position);
        holder.title.setText(playList.getPermalink());
        holder.count.setText(playList.getTrackCount() + " songs");

        Picasso.with(holder.thumbnail.getContext()).load(playList.getArtworkUrl()).fit().into(holder.thumbnail);


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence playListName  = holder.title.getText();
                List<Track> tracks = null;

                for (PlayList playList : playLists) {
                    if (playList.getPermalink().equals(playListName)) {
                        tracks = playList.getTracks();
                        break;
                    }
                }
                Intent playerListActivity = new Intent(mContext, PlayerListActivity.class);
                playerListActivity.putParcelableArrayListExtra("album_track_list", (ArrayList<? extends Parcelable>) tracks);
                mContext.startActivity(playerListActivity);
                //showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {



        // inflate menu
        //PopupMenu popup = new PopupMenu(mContext, view);
        //MenuInflater inflater = popup.getMenuInflater();
        //inflater.inflate(R.menu.menu_album, popup.getMenu());
       // popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
       // popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return this.playLists.size();
    }
}
