package com.kskkbys.weeeeei.app;

import android.app.Application;
import com.parse.Parse;
import com.parse.PushService;

/**
 * Created by keisuke on 2014/06/29.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, MyConfig.PARSE_ID, MyConfig.PARSE_KEY);
        PushService.setDefaultPushCallback(this, StarterActivity.class);
    }
}
