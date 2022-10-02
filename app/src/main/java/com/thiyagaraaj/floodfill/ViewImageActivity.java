package com.thiyagaraaj.floodfill;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.thiyagaraaj.floodfill.floodFillUtils.FloodFillThread;

import java.io.IOException;
import java.net.URL;

public class ViewImageActivity extends AppCompatActivity {
    final String TAG = ViewImageActivity.class.getSimpleName();
    Paint paint;
    public Point p1;
    Bitmap mBitmap;
    float x;
    float y;
    ConstraintLayout parentLayout;
    ColorPickerDialogBuilder colorPickerDialogBuilder;
    private long id;
    private long albumId;
    private String title;
    private String thumbnailUrl;
    private String url = "";
    private Bitmap imageBitmap = null;
    private Point clickedPoint;
    private Runnable runnable;
    private RelativeLayout drawingLayout;
    private MyView myView;
    private int currentBackgroundColor = 0xffffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        id = getIntent().getLongExtra("id", 0);
        albumId = getIntent().getLongExtra("albumId", 0);
        title = getIntent().getStringExtra("title");
        thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");
        url = getIntent().getStringExtra("url");

        drawingLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        parentLayout = findViewById(R.id.parent);

        clickedPoint = new Point();
        new DonwloadImageAsynctask().execute();

        runnable = () -> Log.d(TAG, "Completed");

    }

    void showColorPicker() {
        p1 = new Point();
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Pick your color")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(selectedColor -> {
                    // Handle on color change
                })
                .setOnColorSelectedListener(selectedColor -> {
                    paint.setColor(selectedColor);
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    currentBackgroundColor = selectedColor;
                    paint.setColor(selectedColor);

                    p1.x = (int) x;
                    p1.y = (int) y;
                    final int sourceColor = mBitmap.getPixel((int) x, (int) y);
                    final int targetColor = paint.getColor();
                    new FloodFillThread(new ProgressDialog(getApplicationContext()), runnable,
                            mBitmap, p1, sourceColor, targetColor).run();
                    myView.invalidate();
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(ViewImageActivity.this, android.R.color.holo_blue_bright))
                .build()
                .show();
    }

    class DonwloadImageAsynctask extends AsyncTask<Void, Bitmap, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {

            try {
                URL u = new URL(url);
                imageBitmap = BitmapFactory.decodeStream(u.openConnection().getInputStream());
                imageBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
            } catch (IOException e) {
                System.out.println(e);
            }
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            myView = new MyView(getApplicationContext());
            drawingLayout.addView(myView);
        }
    }

    public class MyView extends View {
        ProgressDialog pd;
        Canvas canvas;
        private final Path path;

        public MyView(Context context) {
            super(context);

            paint = new Paint();
            paint.setAntiAlias(true);
            pd = new ProgressDialog(context);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(5f);
            paint.setColor(Color.RED);
            Bitmap temp = imageBitmap
                    .copy(Bitmap.Config.ARGB_8888, true);
            mBitmap = Bitmap.createScaledBitmap(temp, 1000, 1000, true);

            this.path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            this.canvas = canvas;
            canvas.drawBitmap(mBitmap, 0, 0, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            try {
                x = event.getX();
                y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showColorPicker();
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

}