package com.example.audiogid2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GetNumberActivity extends AppCompatActivity {

    private EditText editTextView;
    private Button confirmButton;

    private TextView numOfAudioItemsTextView;

    final int numOfAudioItems = AudiosConstants.audioItemsHashMap.size();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_number_activity);

        editTextView = (EditText) findViewById(R.id.edit_text_get_number);
        confirmButton = (Button) findViewById(R.id.button_get_number);
        numOfAudioItemsTextView = (TextView) findViewById(R.id.num_of_items_get_number);

        numOfAudioItemsTextView.setText(Integer.toString(numOfAudioItems));

        setListeners();
    }

    private void setListeners() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idx = editTextView.getText().toString();
                int idxInt = 0;

                try {
                    idxInt = Integer.parseInt(idx);
                } catch (NumberFormatException nfe) {
                    editTextView.setText("");
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_if_wrong_number_get_number), Toast.LENGTH_SHORT).show();
                }

                if (idx != "" && idxInt > 0 && idxInt > 0 && idxInt <= numOfAudioItems )
                {
                    AudioItem audioItemData = AudiosConstants.audioItemsHashMap.get(idx);
                    Intent goToAudioItem = new Intent(GetNumberActivity.this, AudioItemActivity.class);
                    goToAudioItem.putExtra("DATA", audioItemData);
                    startActivity(goToAudioItem);
                }
            }
        });
    }
}
