package com.familyfunctional.colorslider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView redValue, greenValue, blueValue, colorName;
    private ImageView imageView;
    int red, green, blue;

    //<editor-fold desc="advanced variables"
    private String hexValue, colorTitle;
    private ColourLoversApi api;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //<editor-fold desc="networking and Toast">
        setupApi();
        if (savedInstanceState == null) {
            toastToTheCreators();
        }
        //</editor-fold>

        redSeekBar = (SeekBar) findViewById(R.id.seekBar_red);
        greenSeekBar = (SeekBar) findViewById(R.id.seekBar_green);
        blueSeekBar = (SeekBar) findViewById(R.id.seekBar_blue);

        redValue = (TextView) findViewById(R.id.red_value);
        greenValue = (TextView) findViewById(R.id.green_value);
        blueValue = (TextView) findViewById(R.id.blue_value);

        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        imageView = (ImageView) findViewById(R.id.imageView);
        colorName = (TextView) findViewById(R.id.name);

        //<editor-fold desc="notification/wear button">
        View hiddenButton = findViewById(R.id.hiddenButton);
        hiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 1;
                WeakReference<Context> contextRef = new WeakReference<>(v.getContext());
                Bitmap color = Bitmap.createBitmap(320, 320, Bitmap.Config.ARGB_8888);
                color.eraseColor(Color.rgb(red, green, blue));

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(contextRef.get())
                                .setSmallIcon(R.drawable.ic_palette_white_24dp)
                                .setColor(Color.rgb(red, green, blue))
                                .setContentTitle("#" + hexValue)
                                .setContentText(colorTitle);

                NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
                extender.setBackground(color);
                notificationBuilder.extend(extender);

                NotificationManagerCompat.from(contextRef.get()).notify(id, notificationBuilder.build());
            }
        });
        //</editor-fold>
    }

    //<editor-fold desc="networking">
    private void setupApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.colourlovers.com/api")
                .build();

        api = restAdapter.create(ColourLoversApi.class);
    }
    //</editor-fold>

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    //<editor-fold desc="menu:randomize"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_random:
                randomizeColors();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void randomizeColors() {
        Random random = new Random();

        blueSeekBar.setProgress(random.nextInt(255));
        redSeekBar.setProgress(random.nextInt(255));
        greenSeekBar.setProgress(random.nextInt(255));
    }
    //</editor-fold>

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            refreshUI();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            requestWebColorName();
        }

        //<editor-fold desc="unused method">
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //do nothing
        }
        //</editor-fold>
    };

    private void refreshUI() {

        red = redSeekBar.getProgress();
        green = greenSeekBar.getProgress();
        blue = blueSeekBar.getProgress();

        redValue.setText(String.valueOf(red));
        greenValue.setText(String.valueOf(green));
        blueValue.setText(String.valueOf(blue));

        imageView.setBackgroundColor(Color.rgb(red, green, blue));
    }

    //<editor-fold desc="networking">
    private void requestWebColorName() {
        hexValue = createHexValue(red, green, blue);
        api.singleColour(hexValue, new Callback<List<Colour>>() {
            @Override
            public void success(List<Colour> colours, Response response) {
                if (colours.size() > 0) {
                    colorTitle = colours.get(0).getTitle();
                    colorName.setText(colorTitle);
                } else {
                    colorName.setText("#" + hexValue);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("network", error.toString());
            }
        });
    }

    private String createHexValue(int red, int green, int blue) {
        return String.format("%02X", red) + String.format("%02X", green) + String.format("%02X", blue);
    }

    private void toastToTheCreators() {
        Toast.makeText(this, getString(R.string.thank_you) + " " + getString(R.string.colourlovers), Toast.LENGTH_LONG).show();
    }
    //</editor-fold>
}
