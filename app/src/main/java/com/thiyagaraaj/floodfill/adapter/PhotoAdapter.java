package com.thiyagaraaj.floodfill.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thiyagaraaj.floodfill.R;
import com.thiyagaraaj.floodfill.models.Photo;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> photos;
    private Context context;

    public PhotoAdapter(Context context, List<Photo> photoList) {
        setHasStableIds(true);
        this.photos = photoList;
        this.context = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Glide.with(context)
                .load(photo.getThumbnailUrl())
                .into(holder.imageView);
        holder.textView.setText(photo.getTitle());
        holder.id = photo.getId();
    }

    @Override
    public int getItemCount() {
        return (photos == null) ? 0 : photos.size();
    }

    @Override
    public long getItemId(int position) {
        return photos.get(position).getId();
    }

     public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
         public long id;

        public PhotoViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.text_view);
        }
    }
}
