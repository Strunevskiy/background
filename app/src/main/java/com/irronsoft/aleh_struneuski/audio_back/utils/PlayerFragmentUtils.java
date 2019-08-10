package com.irronsoft.aleh_struneuski.audio_back.utils;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.ui.fragments.components.PlayerFragment;

import java.util.List;

public final class PlayerFragmentUtils {

    private PlayerFragmentUtils() {
    }

    public static void getTrack(PlayerFragment playerFragment, List<Track> mListItems, int index, boolean isNext) {
        if (isNext) {
            index++;
            if (index >= mListItems.size()) {
                playerFragment.handleClickOnTrack(mListItems.get(0), 0);
            } else {
                playerFragment.handleClickOnTrack(mListItems.get(index), index);
            }
        } else {
            index--;
            if (index == -1) {
                index = mListItems.size() - 1;
                playerFragment.handleClickOnTrack(mListItems.get(index), index);
            } else {
                playerFragment.handleClickOnTrack(mListItems.get(index), index);
            }
        }
    }

}
