package com.kskkbys.weeeeei.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

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
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNameEdit.getText())) {
                    Toast.makeText(SignUpActivity.this, R.string.error_input_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mPassEdit.getText())) {
                    Toast.makeText(SignUpActivity.this, R.string.error_input_pass, Toast.LENGTH_SHORT).show();
                    return;
                }
                signUp();
            }
        });
        mProgressBar = (ProgressBar)findViewById(R.id.signup_progress_bar);
    }

    private void signUp() {
        mProgressBar.setVisibility(View.VISIBLE);
        mButton.setVisibility(View.INVISIBLE);

        ParseUser user = new ParseUser();
        user.setUsername(mNameEdit.getText().toString());
        user.setPassword(mPassEdit.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "エラーが発生しました", Toast.LENGTH_SHORT).show();
                }

                mProgressBar.setVisibility(View.INVISIBLE);
                mButton.setVisibility(View.VISIBLE);
            }
        });
    }


}
