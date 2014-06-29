package com.kskkbys.weeeeei.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class StarterActivity extends Activity {

    private static final int REQ_CODE_SIGN_UP = 1000;
    private static final int REQ_CODE_LOG_IN  = 1001;

    private ListView mListView;

    private static final String[] ITEMS = {
            "サインアップ",
            "ログイン"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        ParseAnalytics.trackAppOpened(getIntent());

        // すでにログイン済みなら、フレンド画面へ
        if (ParseUser.getCurrentUser() != null) {
            goToFriendList();
            return;
        }

        mListView = (ListView)findViewById(R.id.starter_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    goToSignUp();
                } else {
                    goToLogIn();
                }
            }
        });

        MyStringAdapter adapter = new MyStringAdapter(this, R.layout.row_item, R.id.row_text, ITEMS);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SIGN_UP || requestCode == REQ_CODE_LOG_IN) {
            if (resultCode == RESULT_OK) {
                goToFriendList();
            }
        }
    }

    private void goToFriendList() {

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        Intent intent = new Intent(this, FriendListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

    private void goToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQ_CODE_SIGN_UP);
    }

    private void goToLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQ_CODE_LOG_IN);
    }
}
