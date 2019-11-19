package com.example.mobile_deadline_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter arrayAdapter;

    ArrayList<String> title;
    ArrayList<String> date;
    ArrayList<String> description;
    ArrayList<String> link;

    ArrayList<news_item> news;

    Service_DownloadNews newsDownloader;
    boolean isBound = false;

    String temp;

    private class ReceiverClassName extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            news.clear();
            news = (ArrayList<news_item>) intent.getSerializableExtra("news");
            createData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh) {
            newsDownloader.refresh();
        }
        else if(id == R.id.action_exit) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        news = new ArrayList<news_item>();
        title = new ArrayList<String>();
        date = new ArrayList<String>();
        description = new ArrayList<String>();
        link = new ArrayList<String>();

        IntentFilter filter = new IntentFilter();
        filter.addAction("action");
        registerReceiver(new ReceiverClassName(), filter);

        //create service
        Intent intent = new Intent(this, Service_DownloadNews.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, title);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _title = title.get(position);
                String _date = date.get(position);
                String _des = description.get(position);
                String _link = link.get(position);

                Intent intent = new Intent(MainActivity.this, newsDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.putExtra("title", _title);
                intent.putExtra("date", _date);
                intent.putExtra("description", _des);
                intent.putExtra("link", _link);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        isBound = false;
    }

    private void createData() {
        title.clear();
        date.clear();
        description.clear();
        link.clear();

        for(int i=0; i<news.size(); i++) {
            title.add(news.get(i).getTitle());
            date.add(news.get(i).getDate());
            description.add(news.get(i).getDescription());
            link.add(news.get(i).getLink());
        }

        arrayAdapter.notifyDataSetChanged();

        Toast.makeText(MainActivity.this, "refreshed news", Toast.LENGTH_LONG).show();
}

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Service_DownloadNews.LocalBinder binder = (Service_DownloadNews.LocalBinder) service;
            newsDownloader = binder.getService();
            isBound = true;

            newsDownloader.refresh();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

}
