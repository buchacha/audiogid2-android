package com.example.audiogid2;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView websiteView;
    private TextView audiogidView;
    private TextView audioNumberView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        setListeners();
        initExistingAudioItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lang_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.russian_item:
                changeLocale("ru");
                Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.english_item:
                changeLocale("en");
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.chinese_item:
                changeLocale("zh");
                Toast.makeText(this, "不客氣", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        websiteView = (TextView) findViewById(R.id.website);
        audiogidView = (TextView) findViewById(R.id.audiogid);
        audioNumberView = (TextView) findViewById(R.id.audio_number);
    }

    private void setAppLocale(String localeCode) {

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ) {
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf, dm);
    }

    private void setListeners() {
        websiteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nabokov.museums.spbu.ru/"));
                startActivity(browserIntent);
            }
        });
        audiogidView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listViewIntent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(listViewIntent);
            }
        });
        audioNumberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listViewIntent = new Intent(MainActivity.this, GetNumberActivity.class);
                startActivity(listViewIntent);
            }
        });
    }

    private  void initExistingAudioItems(){
        AudiosConstants.audioItemsHashMap.put("1", AudiosConstants.audioItem1);
        AudiosConstants.audioItemsHashMap.put("2", AudiosConstants.audioItem2);
        AudiosConstants.audioItemsHashMap.put("3", AudiosConstants.audioItem3);
        AudiosConstants.audioItemsHashMap.put("4", AudiosConstants.audioItem4);
    }

    private void changeLocale(String localCode) {
        Intent intent;
        setAppLocale(localCode);
        intent = getIntent();
        finish();
        startActivity(intent);
    }
}
