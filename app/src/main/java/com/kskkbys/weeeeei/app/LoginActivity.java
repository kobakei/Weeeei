package com.kskkbys.weeeeei.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

    private EditText mNameEdit;
    private EditText mPassEdit;
    private Button mButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mNameEdit = (EditText)findViewById(R.id.signup_name);
        mPassEdit = (EditText)findViewById(R.id.signup_pass);
        mButton   = (Button)findViewById(R.id.signup_button);
        mButton.setText("ログイン");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNameEdit.getText())) {
                    Toast.makeText(LoginActivity.this, R.string.error_input_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mPassEdit.getText())) {
                    Toast.makeText(LoginActivity.this, R.string.error_input_pass, Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
            }
        });

        mProgressBar = (ProgressBar)findViewById(R.id.signup_progress_bar);
    }

    private void login() {
        mProgressBar.setVisibility(View.VISIBLE);
        mButton.setVisibility(View.INVISIBLE);
        ParseUser.logInInBackground(mNameEdit.getText().toString(), mPassEdit.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(LoginActivity.this, "エラーが発生しました", Toast.LENGTH_SHORT).show();
                }

                mProgressBar.setVisibility(View.INVISIBLE);
                mButton.setVisibility(View.VISIBLE);
            }
        });
    }
}
