package com.kskkbys.weeeeei.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {

    private static final String TAG = MenuActivity.class.getSimpleName();

    private ListView mListView;
    private MyStringAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mListView = (ListView)findViewById(R.id.menu_list_view);
        List<String> items = new ArrayList<String>();
        items.add("ユーザー：" + ParseUser.getCurrentUser().getUsername());
        items.add("友だちを招待");
        items.add("ログアウト");
        mAdapter = new MyStringAdapter(this, R.layout.row_item, R.id.row_text, items);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    invite();
                } else if (position == 2) {
                    logout();
                }
            }
        });

    }

    private void invite() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "ｳｪｰｲwwwwww");
        String url = "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName() + "&hl=ja";
        share.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(share, "友だちを招待"));
    }

    private void logout() {
        ParseUser.logOut();

        Intent intent = new Intent(this, StarterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.slide_in_bottom, R.animator.slide_out_top);
    }
}
