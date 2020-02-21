package com.example.audiogid2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String LOG_TAG = ListActivity.class.getSimpleName();

    private final int EMPTY_RESOURCE = -1;

    private final int PLAY_STATE = 1;
    private final int PAUSE_STATE = 2;
    private final int RELEASE_STATE = 3;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private ImageView previousButton;
    private ImageView playButton;
    private ImageView nextButton;
    private SeekBar seekBar;
    private TextView playerLabel;

    private int playerState;
    private int lastResourceId; // предполагаем, что у ресурсов положительные значения
    private int lastPosition;

    private Handler handler;

    final ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                playButton.setImageResource(R.drawable.pause);
                playerState = PLAY_STATE;

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        handler = new Handler();

        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
//                https://medium.com/@yossisegev/understanding-activity-runonuithread-e102d388fe93
            }
        });

        initViews();

        lastResourceId = EMPTY_RESOURCE;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        for (int i = 0; i < AudiosConstants.audioItemsHashMap.size(); i++) {
            audioItems.add(AudiosConstants.audioItemsHashMap.get(Integer.toString(i+1)));
        }

        AudioItemAdapter itemsAdapter = new AudioItemAdapter(this, audioItems);

        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioItem currentAudioItem = audioItems.get(position);
                playThis(currentAudioItem);
                lastPosition = position;
            }
        });

        setPlayerButtonListeners();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.play);
            playerState = PAUSE_STATE;
        }

    }

    private void releaseMediaPlayer() {

        playButton.setImageResource(R.drawable.play);
        playerState = RELEASE_STATE;

        if (mediaPlayer != null) {
            mediaPlayer.release();

            mediaPlayer = null;

            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    private void initViews() {
        previousButton = (ImageView) findViewById(R.id.player_previous);
        playButton = (ImageView) findViewById(R.id.player_play);
        nextButton = (ImageView) findViewById(R.id.player_next);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        playerLabel = (TextView) findViewById(R.id.list_activity_player_label);
        playerLabel.setVisibility(View.GONE);

    }

    void setPlayerButtonListeners() {

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseMediaPlayer();
                if (lastPosition - 1 >= 0) {
                    AudioItem currentAudioItem = audioItems.get(lastPosition - 1);
                    playThis(currentAudioItem);
                    lastPosition -= 1;
                } else {
                    AudioItem currentAudioItem = audioItems.get(audioItems.size()-1);
                    playThis(currentAudioItem);
                    lastPosition = audioItems.size()-1;

                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerState == PLAY_STATE) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.play);
                    playerState = PAUSE_STATE;
                } else if (playerState == PAUSE_STATE) {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.pause);
                    playerState = PLAY_STATE;
                } else if (playerState == RELEASE_STATE) {
                    Toast.makeText(ListActivity.this, "Выберите композицию для прослушивания из списка", Toast.LENGTH_LONG).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition + 1 < audioItems.size()) {
                    AudioItem currentAudioItem = audioItems.get(lastPosition + 1);
                    playThis(currentAudioItem);
                    lastPosition += 1;
                } else {
                    AudioItem currentAudioItem = audioItems.get(0);
                    playThis(currentAudioItem);
                    lastPosition = 0;
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        });

    }

    void playThis(AudioItem item) {
        releaseMediaPlayer();

        int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            mediaPlayer = MediaPlayer.create(ListActivity.this, item.getResourceId());

            seekBar.setMax(mediaPlayer.getDuration()/1000);

            mediaPlayer.start();

            playButton.setImageResource(R.drawable.pause);
            playerState = PLAY_STATE;

            mediaPlayer.setOnCompletionListener(completionListener);

            playerLabel.setVisibility(View.VISIBLE);
            playerLabel.setText(item.getLabelId());
        }

    }
}
