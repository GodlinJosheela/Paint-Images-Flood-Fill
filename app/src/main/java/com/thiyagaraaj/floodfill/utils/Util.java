package com.thiyagaraaj.floodfill.utils;

import android.content.Context;
import android.util.Log;

import com.thiyagaraaj.floodfill.models.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Util {
    static Util util;

    public static Util getUtilInstance(){
        if(util == null){
            util = new Util();
        }
        return util;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("images.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public List<Photo> loadImages(Context context) {
        List<Photo> photoList = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context));
            photoList.clear();
            Photo photo;

            for (int i = 0; i < m_jArry.length(); i++) {
                photo = new Photo();
                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                Log.d("Details-->", jo_inside.getString("id"));
                photo.setAlbumId(jo_inside.getLong("albumId"));
                photo.setId(jo_inside.getLong("id"));
                photo.setThumbnailUrl(jo_inside.getString("thumbnailUrl"));
                photo.setTitle(jo_inside.getString("title"));
                photo.setUrl(jo_inside.getString("url"));

                photoList.add(photo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photoList;
    }
}
