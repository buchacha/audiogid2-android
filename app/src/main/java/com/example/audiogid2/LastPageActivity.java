package com.example.audiogid2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LastPageActivity extends AppCompatActivity {


    private Button writeComment;
    private Button makeDonation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_page);

        writeComment = (Button) findViewById(R.id.write_comment);
        makeDonation = (Button) findViewById(R.id.make_donation);

        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nabokov.museums.spbu.ru/"));
                startActivity(browserIntent);
            }
        });

        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nabokov.museums.spbu.ru/"));
                startActivity(browserIntent);
            }
        });

    }
}
