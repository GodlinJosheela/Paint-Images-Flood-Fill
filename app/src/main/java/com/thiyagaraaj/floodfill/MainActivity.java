package com.thiyagaraaj.floodfill;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.thiyagaraaj.floodfill.adapter.PhotoAdapter;
import com.thiyagaraaj.floodfill.models.Photo;
import com.thiyagaraaj.floodfill.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList = new ArrayList<>();
    SelectionTracker<Long> selectedImagetracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        photoList.clear();
        photoList.addAll(Util.getUtilInstance().loadImages(getApplicationContext()));
        photoAdapter = new PhotoAdapter(getApplicationContext(), photoList);
        recyclerView.setAdapter(photoAdapter);

        selectedImagetracker = new SelectionTracker.Builder<>(
                "selectedSettingTrackerId",
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new SettingsDetailsLookup(),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).
                build();

        selectedImagetracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onSelectionChanged() {
//                super.onSelectionChanged();

                try {
                    Photo selection = null;
                    for (Photo photo : photoList) {
                        if (photo.getId() == selectedImagetracker.getSelection().iterator().next()) {
                            selection = photo;
                        }
                    }

                    if (selection != null) {
                        Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                        intent.putExtra("id", selection.getId());
                        intent.putExtra("albumId", selection.getAlbumId());
                        intent.putExtra("thumbnailUrl", selection.getThumbnailUrl());
                        intent.putExtra("title", selection.getTitle());
                        intent.putExtra("url", selection.getUrl());
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        selectedImagetracker.select(photoList.get(0).getId());
    }

    private class SettingsDetailsLookup extends ItemDetailsLookup<Long> {

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent event) {
            View view = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (view != null) {
                final RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                if (viewHolder instanceof PhotoAdapter.PhotoViewHolder) {
                    final PhotoAdapter.PhotoViewHolder settingViewHolder = (PhotoAdapter.PhotoViewHolder) viewHolder;
                    return new ItemDetailsLookup.ItemDetails<Long>() {
                        @Override
                        public int getPosition() {
                            return viewHolder.getAdapterPosition();
                        }

                        @Nullable
                        @Override
                        public Long getSelectionKey() {
                            return settingViewHolder.id;
                        }
                    };
                }
            }
            return null;
        }
    }
}