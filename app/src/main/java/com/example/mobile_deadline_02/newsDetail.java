package com.example.mobile_deadline_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class newsDetail extends AppCompatActivity {

    TextView title, date, description, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = (TextView) findViewById(R.id.d_title);
        date = (TextView) findViewById(R.id.d_date);
        description = (TextView) findViewById(R.id.d_des);
        link = (TextView) findViewById(R.id.d_link);

        Intent intent = this.getIntent();
        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        description.setText(Html.fromHtml(intent.getStringExtra("description")));
        link.setText(intent.getStringExtra("link"));
    }

    @Override
    public void onBackPressed() {
        newsDetail.this.finish();
    }

    public void linkClicked(View view) {
        TextView tv = (TextView) view;
        String link = tv.getText().toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}
