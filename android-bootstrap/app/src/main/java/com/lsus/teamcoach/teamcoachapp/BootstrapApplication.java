

package com.lsus.teamcoach.teamcoachapp;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.lsus.teamcoach.teamcoachapp.core.DrillObject;
import com.parse.Parse;
import com.parse.ParseObject;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Http.PARSE_APP_ID;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Http.PARSE_CLIENT_KEY_ID;

/**
 * teamcoach application
 */
public class BootstrapApplication extends Application {

    private static BootstrapApplication instance;

    /**
     * Create main application
     */
    public BootstrapApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;



        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(DrillObject.class);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY_ID);

        // Perform injection
        Injector.init(getRootModule(), this);

    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static BootstrapApplication getInstance() {
        return instance;
    }
}
