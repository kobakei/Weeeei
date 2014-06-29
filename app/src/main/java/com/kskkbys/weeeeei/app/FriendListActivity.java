package com.kskkbys.weeeeei.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends Activity {

    private static final String TAG = FriendListActivity.class.getSimpleName();

    private Button mInviteButton;
    private ListView mListView;
    private Button mPlusButton;
    private EditText mEdit;
    private View mMenuView;

    private List<String> mFriends;
    private MyStringAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        mFriends = new ArrayList<String>();

        mInviteButton = (Button)findViewById(R.id.friend_invite_button);
        mInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite();
            }
        });

        mListView = (ListView)findViewById(R.id.friend_list_view);
        mAdapter = new MyStringAdapter(this, R.layout.row_item, R.id.row_text, mFriends);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendPush(mAdapter.getItem(position));
            }
        });
        fixListViewHeight();

        mPlusButton = (Button)findViewById(R.id.friend_plus_button);
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlusButton.setVisibility(View.INVISIBLE);
                mEdit.setVisibility(View.VISIBLE);
            }
        });

        mEdit = (EditText)findViewById(R.id.friend_edit);
        mEdit.setVisibility(View.INVISIBLE);
        mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    InputMethodManager ime = (InputMethodManager)FriendListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    ime.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);

                    String name = mEdit.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        return true;
                    }
                    if (mFriends.contains(name)) {
                        Toast.makeText(FriendListActivity.this, "すでに追加されています", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    mAdapter.add(name);
                    mAdapter.notifyDataSetChanged();
                    mEdit.setText("");
                    mEdit.setVisibility(View.INVISIBLE);
                    mPlusButton.setVisibility(View.VISIBLE);
                    fixListViewHeight();

                    //
                    saveFriends();

                    return true;
                }
                return false;
            }
        });

        mMenuView = findViewById(R.id.friend_menu);
        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });

        loadFriends();
    }

    private void loadFriends() {
        mFriends.clear();
        ParseUser user = ParseUser.getCurrentUser();
        List<String> friends = user.getList("friends");
        if (friends != null) {
            for (String f : friends) {
                mFriends.add(f);
            }
        }
        mAdapter.notifyDataSetChanged();
        fixListViewHeight();
        if (mFriends.size() > 0) {
            mInviteButton.setVisibility(View.GONE);
        }
    }

    private void saveFriends() {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("friends", mFriends);
        user.saveInBackground();
    }

    private void fixListViewHeight() {
        ListAdapter la = mListView.getAdapter();
        if (la == null)
        {
            return;
        }

        int i;
        int h = 0; // ListView トータルの高さ

        for (i = 0; i < la.getCount(); i++)
        {
            View item = la.getView(i, null, mListView);
            item.measure(0, 0);
            h += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams p = mListView.getLayoutParams();
        p.height = h + (mListView.getDividerHeight() * (la.getCount() - 1));
        mListView.setLayoutParams(p);
    }

    /**
     * アプリを招待
     */
    private void invite() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "ｳｪｰｲwwwwww");
        String url = "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName() + "&hl=ja";
        share.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(share, "友だちを招待"));
    }

    /**
     * 指定したユーザーにPush送信
     * @param user
     */
    private void sendPush(final String user) {
        // Find users near a given location
        ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", user);

        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        String message = "ｳｪｰｲwwwwww from " + ParseUser.getCurrentUser().getUsername();
        push.setMessage(message);
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(FriendListActivity.this, "送信しました", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FriendListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 裏でユーザーがいるか検索
        userQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Toast.makeText(FriendListActivity.this, "ユーザーが存在しません", Toast.LENGTH_SHORT).show();
                        mAdapter.remove(user);
                        saveFriends();
                        fixListViewHeight();
                    }
                }
            }
        });
    }

    private void goToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_in_top, R.animator.slide_out_bottom);
    }
}
