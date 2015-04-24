package com.familyfunctional.colorslider;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView redValue, greenValue, blueValue;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        refreshUI();
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            refreshUI();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void refreshUI() {
        int red, green, blue;

        red = redSeekBar.getProgress();
        green = greenSeekBar.getProgress();
        blue = blueSeekBar.getProgress();

        redValue.setText(String.valueOf(red));
        greenValue.setText(String.valueOf(green));
        blueValue.setText(String.valueOf(blue));

        imageView.setBackgroundColor(Color.rgb(red, green, blue));
    }

}
