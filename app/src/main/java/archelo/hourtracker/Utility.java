package archelo.hourtracker;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by rtl1e on 1/17/2018.
 */

public class Utility {

    public static InitDrawerResult initNavigationDrawer(Activity activity, Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
        return new InitDrawerResult(drawer,navigationView);
    }
    public static class InitDrawerResult{
        public NavigationView nav_view;
        public DrawerLayout drawer_layout;
        public InitDrawerResult(DrawerLayout layout, NavigationView view){
            drawer_layout = layout;
            nav_view = view;
        }
    }

    public static String getMoneyValue(String wage){
        if(wage.startsWith("$")){
            return wage.substring(1);
        }
        return wage;
    }
}
