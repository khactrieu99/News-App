package com.example.mobile_deadline_02;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.annotation.Nullable;

public class Service_DownloadNews extends Service {
    private ArrayList<news_item> news;

    private final IBinder localBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        Service_DownloadNews getService() {
            return Service_DownloadNews.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    public void refresh(){
        new refresh_news().execute("https://www.fit.hcmus.edu.vn/vn/feed.aspx");
    }

    private class refresh_news extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            s = s.replaceAll("<img .*?</img>", "").replaceAll("<img .*?/>", "").replaceAll("<img .*?>", "");


            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDes = document.getElementsByTagName("description");

            news = new ArrayList<news_item>();

            for(int i=0; i<nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String title = parser.getValue(element, "title");
                String date = parser.getValue(element, "pubDate");
                String link = parser.getValue(element, "link");
                String description = nodeListDes.item(i+1).getTextContent();

                news.add(new news_item(title, date, description, link));
            }

            Intent done = new Intent();
            done.setAction("action");
            done.putExtra("news", news);
            sendBroadcast(done);
        }

        private String getCharacterDataFromElement(Element e) {
            Node child = e.getFirstChild();
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
            return "";
        }
    }
}
