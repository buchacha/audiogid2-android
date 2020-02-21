package com.example.audiogid2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AudioItemAdapter extends ArrayAdapter<AudioItem> {

    private static final String LOG_TAG = AudioItemAdapter.class.getSimpleName();



    private View.OnClickListener myButtonMoreClickListener = new View.OnClickListener() {
        //мы переопределяем функцию для класса, который имеет единственный экземпляр new
        // внутри переопределенной функции используем функцию и переменные из класса, в котором создается эта переменная
        // как это овзможно, если к них по сути разные области видимости
        @Override
        public void onClick(View v) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);

            AudioItem currentAudioItem = getItem(position); // почему в этом скоупе мы имеем доступ к этой функции ?

            Intent goToAudioItem = new Intent(getContext(), AudioItemActivity.class);
            goToAudioItem.putExtra("DATA", currentAudioItem);
            getContext().startActivity(goToAudioItem);

        }
    };

    // возможно можно включить аудио на плеерее из вызвавшего view и там же взять кнопку плей и остальные.
//    View parentRow = (View) v.getParent();
//    ListView listView = (ListView) parentRow.getParent();
//    final int position = listView.getPositionForView(parentRow);
//
//    AudioItem currentAudioItem = getItem(position); // почему в этом скоупе мы имеем доступ к этой функции ?
//
//    lastResourceId = currentAudioItem.getResourceId();
//
//    releaseMediaPlayer(); // и к этой ?
//
//    int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
//            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//
//            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//        // We have audio focus now.
//
//        // Create and setup the {@link MediaPlayer} for the audio resource associated
//        // with the current word
//        mediaPlayer = MediaPlayer.create(getContext(), currentAudioItem.getResourceId());
//
//        // Start the audio file
//        mediaPlayer.start();
//
//        // Setup a listener on the media player, so that we can stop and release the
//        // media player once the sound has finished playing.
//        mediaPlayer.setOnCompletionListener(completionListener);
//    }


    public AudioItemAdapter(Context context, ArrayList<AudioItem> objects) {

        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        AudioItem currentAudioItem = getItem(position);
        TextView itemLabel = (TextView) listItemView.findViewById(R.id.text);
        Context context = getContext();
        itemLabel.setText(context.getString(currentAudioItem.getLabelId()));
        ImageView moreButton = (ImageView) listItemView.findViewById(R.id.list_item_more);

        moreButton.setOnClickListener(myButtonMoreClickListener);

        return listItemView;
    }

//    private void releaseMediaPlayer() {
//        // If the media player is not null, then it may be currently playing a sound.
//        if (mediaPlayer != null) {
//            // Regardless of the current state of the media player, release its resources
//            // because we no longer need it.
//            mediaPlayer.release();
//
//            // Set the media player back to null. For our code, we've decided that
//            // setting the media player to null is an easy way to tell that the media player
//            // is not configured to play an audio file at the moment.
//            mediaPlayer = null;
//
//            // Regardless of whether or not we were granted audio focus, abandon it. This also
//            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
//            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
//        }
//    }
}
