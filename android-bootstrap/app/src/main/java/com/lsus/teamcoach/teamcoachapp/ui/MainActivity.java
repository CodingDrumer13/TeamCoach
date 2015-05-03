

package com.lsus.teamcoach.teamcoachapp.ui;

import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.events.NavItemSelectedEvent;
import com.lsus.teamcoach.teamcoachapp.ui.AboutUs.AboutUsActivity;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.BootstrapTimerActivity;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapFragmentActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.CarouselFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.NavigationDrawerFragment;
import com.lsus.teamcoach.teamcoachapp.util.Ln;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.lsus.teamcoach.teamcoachapp.util.UIUtils;
import com.parse.Parse;
import com.parse.ParseUser;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Views;
import retrofit.RetrofitError;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link com.lsus.teamcoach.teamcoachapp.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    private static final String FORCE_REFRESH = "forceRefresh";

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected BootstrapService bootstrapService;
    @Inject protected LogoutService logoutService;


    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;
    private SafeAsyncTask<Boolean> authenticationTask;

    protected Singleton singleton = Singleton.getInstance();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife

      Views.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();
        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        checkAuth();
    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }


    private void initScreen() throws IOException, AccountsException {
        if (userHasAuthenticated) {

            Ln.d("Foo");

            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //TODO set the team, sessions and drills here!!!!
                    ArrayList < Team > teams = new ArrayList<Team>();
                    teams.addAll(serviceProvider.getService(MainActivity.this).getTeams(singleton.getCurrentUser().getEmail()));
                    singleton.setUserTeams(teams);

                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if(!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if(cause != null) {
                            Toaster.showLong(MainActivity.this, cause.getMessage());
                            Log.d("Error",cause.getMessage());
                        }
                    }
                }

                @Override
                protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                    final FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new CarouselFragment())
                            .commit();
                }
            };
            authenticationTask.execute();

        }
    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final BootstrapService svc = serviceProvider.getService(MainActivity.this);
                //Finish set up of Singleton
                Singleton singleton = Singleton.getInstance();
                if(singleton.getToken() != null){
                    singleton.setCurrentUser(svc.currentUser(singleton.getToken()));
                }
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            case R.id.profile:
                navigateToProfile();
                return true;
            case R.id.timer:
                navigateToTimer();
                return true;
            case R.id.aboutus:
                navigateToAboutScreen();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }

    private void navigateToAboutScreen(){
        final Intent i = new Intent(this, AboutUsActivity.class);
        startActivity(i);
    }

    private void navigateToProfile() {
        User user = singleton.getCurrentUser();
        startActivity(new Intent(this, UserActivity.class).putExtra(USER, user));
    }

    private void logout() {
            new LogoutService(this, AccountManager.get(this)).logout(new Runnable() {
                @Override
                public void run() {
                    // Calling a refresh will force the service to look for a logged in user
                    // and when it finds none the user will be requested to log in again.
                    forceRefresh();
                }
            });
    }

    /**
     * Force a refresh of the items displayed ignoring any cached items
     */
    protected void forceRefresh() {
        getActionBarActivity().setSupportProgressBarIndeterminateVisibility(true);
        try {
            serviceProvider.getService(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AccountsException e) {
            e.printStackTrace();
        }
    }

    private ActionBarActivity getActionBarActivity() {
        return ((ActionBarActivity) this);
    }

    /**
     * Is this fragment still part of an activity and usable from the UI-thread?
     *
     * @return true if usable on the UI-thread, false otherwise
     */
    protected boolean isUsable() {
        return this != null;
    }


    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                // Profile
                navigateToProfile();
                break;
            case 2:
                // Timer
                navigateToTimer();
                break;
            case 3:
                // About US
                navigateToAboutScreen();
                break;
            case 4:
                // Logout
                logout();
                break;
        }
    }

}
