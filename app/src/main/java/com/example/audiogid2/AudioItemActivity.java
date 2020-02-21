package com.example.audiogid2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AudioItemActivity extends AppCompatActivity {

    private final int EMPTY_RESOURCE = -1;

    private final int PLAY_STATE = 1;
    private final int PAUSE_STATE = 2;
    private final int RELEASE_STATE = 3;

    private static final String LOG_TAG = ListActivity.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private ImageView playButton;
    private SeekBar seekBar;

    private int playerState;
    private int lastResourceId; // предполагаем, что у ресурсов положительные значения
    private int lastPosition;

    private Handler handler;

    private AudioItem currentAudioItem;

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
//                mediaPlayer.start();
//                playButton.setImageResource(R.drawable.pause);
//                playerState = PLAY_STATE;

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_item_activity);
        handler = new Handler();

        Intent intent = getIntent();
        currentAudioItem = (AudioItem) intent.getExtras().getSerializable("DATA");  //не забыть отправить

        AudioItemActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });

        initViews();

        lastResourceId = EMPTY_RESOURCE;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerState == PLAY_STATE) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.play);
                    playerState = PAUSE_STATE;
                } else if (playerState == PAUSE_STATE) {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.pause);
                    playerState = PLAY_STATE;
                } else {
                    releaseMediaPlayer();

                    int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        // We have audio focus now.

                        // Create and setup the {@link MediaPlayer} for the audio resource associated
                        // with the current word
                        mediaPlayer = MediaPlayer.create(AudioItemActivity.this, currentAudioItem.getResourceId()); // may be bad
                        seekBar.setMax(mediaPlayer.getDuration() / 1000);

                        // Start the audio file
                        mediaPlayer.start();

                        playButton.setImageResource(R.drawable.pause);
                        playerState = PLAY_STATE;
                        // Setup a listener on the media player, so that we can stop and release the
                        // media player once the sound has finished playing.
                        mediaPlayer.setOnCompletionListener(completionListener);
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        });
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

        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    private void initViews() {
        playButton = (ImageView) findViewById(R.id.play_audio_item);
        seekBar = (SeekBar) findViewById(R.id.seek_bar_audio_item);
    }
}
