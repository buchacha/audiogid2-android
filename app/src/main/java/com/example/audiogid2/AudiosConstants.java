package com.example.audiogid2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class AudiosConstants extends AppCompatActivity {

//    первое аудио
    public static final AudioItem audioItem1 = new AudioItem(R.raw.first, R.string.audio_item_1_label, R.string.audio_item_1_text);
    public static final AudioItem audioItem2 = new AudioItem(R.raw.second, R.string.audio_item_2_label, R.string.audio_item_2_text);
    public static final AudioItem audioItem3 = new AudioItem(R.raw.third, R.string.audio_item_3_label, R.string.audio_item_3_text);
    public static final AudioItem audioItem4 = new AudioItem(R.raw.last, R.string.audio_item_4_label, R.string.audio_item_4_text);

    public static Map<String, AudioItem> audioItemsHashMap = new HashMap<String, AudioItem>();
}
