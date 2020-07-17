package com.example.android.songs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.songdetailstart.R;
import com.example.android.songdetailstart.content.SongUtils;

public class SongDetailFragment extends Fragment {

    public SongUtils.Song mSong;


    public SongDetailFragment() {
        // Required empty public constructor
    }

    public static SongDetailFragment newInstance(int selectedSong){
        SongDetailFragment fragment = new SongDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(SongUtils.SONG_ID_KEY, selectedSong);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song_detail, container, false);
        if (mSong != null) {
            ((TextView) rootView.findViewById(R.id.song_detail))
                    .setText(mSong.details);
        }
        return rootView;
    }
}