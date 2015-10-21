package com.familyfunctional.colorslider;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView redValue, greenValue, blueValue, colorName;
    private ImageView imageView;
    private String hexValue;
    private ColourLoversApi api;
    int red, green, blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //<editor-fold desc="networking">
        setupApi();
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
    }

    //<editor-fold desc="networking">
    private void setupApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.colourlovers.com/api")
                .build();

        api = restAdapter.create(ColourLoversApi.class);
    }
    //</editor-fold>

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            refreshUI();
        }

        //<editor-fold desc="unused methods">
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            requestWebColorName();
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
                    colorName.setText(colours.get(0).getTitle());
                } else {
                    colorName.setText("#" + hexValue);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("fail", error.toString());
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="networking">
    private String createHexValue(int red, int green, int blue) {
        return String.format("%02X", red) + String.format("%02X", green) + String.format("%02X", blue);
    }
    //</editor-fold>

}
