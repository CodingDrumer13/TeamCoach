

package com.lsus.teamcoach.teamcoachapp.ui.Framework;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.ui.Admin.AdminFragment;
import com.lsus.teamcoach.teamcoachapp.ui.News.NewsListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Calender.CalendarFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Team.TeamsFragment;

/**
 * Pager adapter
 */
public class BootstrapPagerAdapterAdmin extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public BootstrapPagerAdapterAdmin(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    //important
    //Number of Fragments on the Carousel has to be set
    @Override
    public int getCount() { return 6; }

    // Gets each fragment for the Carousel
    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new NewsListFragment();
                break;
            case 1:
                result = new CalendarFragment();
                break;
            case 2:
                result = new TeamsFragment();
                break;
            case 3:
                result = new LibraryFragment();
                break;
            case 4:
                result = new AdminFragment();
                break;
            case 5:
                result = new UserListFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    //Gets the title of each page/fragment on the Carousel
    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_messages);
            case 1:
                return resources.getString(R.string.page_calendar);
            case 2:
                return resources.getString(R.string.page_team);
            case 3:
                return resources.getString(R.string.page_library);
            case 4:
                return resources.getString(R.string.page_admin);
            case 5:
                return resources.getString(R.string.page_users);
            default:
                return null;
        }
    }
}
