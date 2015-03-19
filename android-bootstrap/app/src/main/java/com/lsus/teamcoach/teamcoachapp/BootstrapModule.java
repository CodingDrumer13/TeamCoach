package com.lsus.teamcoach.teamcoachapp;

import android.accounts.AccountManager;
import android.content.Context;

import com.lsus.teamcoach.teamcoachapp.authenticator.ApiKeyProvider;
import com.lsus.teamcoach.teamcoachapp.authenticator.RegisterFragment;
import com.lsus.teamcoach.teamcoachapp.authenticator.BootstrapAuthenticatorActivity;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Constants;
import com.lsus.teamcoach.teamcoachapp.core.PostFromAnyThreadBus;
import com.lsus.teamcoach.teamcoachapp.core.RestAdapterRequestInterceptor;
import com.lsus.teamcoach.teamcoachapp.core.RestErrorHandler;
import com.lsus.teamcoach.teamcoachapp.core.TimerService;
import com.lsus.teamcoach.teamcoachapp.core.UserAgentProvider;
import com.lsus.teamcoach.teamcoachapp.ui.AddTeamFragment;
import com.lsus.teamcoach.teamcoachapp.ui.AdminFragment;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapTimerActivity;
import com.lsus.teamcoach.teamcoachapp.ui.CheckInsListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.DrillInfoActivity;
import com.lsus.teamcoach.teamcoachapp.ui.DrillListActivity;
import com.lsus.teamcoach.teamcoachapp.ui.DrillListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.LibraryFragment;
import com.lsus.teamcoach.teamcoachapp.ui.LibraryListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.MainActivity;
import com.lsus.teamcoach.teamcoachapp.ui.NavigationDrawerFragment;
import com.lsus.teamcoach.teamcoachapp.ui.NewsActivity;
import com.lsus.teamcoach.teamcoachapp.ui.NewsListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.TeamMenuListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.TeamsFragment;
import com.lsus.teamcoach.teamcoachapp.ui.TeamsListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.UserActivity;
import com.lsus.teamcoach.teamcoachapp.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        //Have to add new Fragments and Activities here
        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                TimerService.class,
                RegisterFragment.class,
                LibraryListFragment.class,
                TeamMenuListFragment.class,
                TeamsListFragment.class,
                DrillListFragment.class,
                TeamsFragment.class,
                AdminFragment.class,
                DrillListActivity.class,
                DrillInfoActivity.class,
                LibraryFragment.class,
                AddTeamFragment.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
