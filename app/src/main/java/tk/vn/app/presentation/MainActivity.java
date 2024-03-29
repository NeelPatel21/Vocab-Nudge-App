package tk.vn.app.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.net.LogoutTask;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.com.net.UserDetailFetchTask;
import tk.vn.app.model.UserBean;
import tk.vn.app.presentation.adapters.MainActivityPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainActivityPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    TabLayout tabLayout;

    Toolbar toolbar;

    NavigationView navigationView;

    TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fatchUserDetail();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        headerText = (TextView)headerLayout.findViewById(R.id.nav_header_text2);
//        System.out.println("test :- "+headerLayout);

//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager()
                ,tabLayout,toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(mSectionsPagerAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter.updateTabIcons(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserBean userBean = RunTimeStore.get(Const.USER_DETAIL);
        if(userBean != null) {
            String displayName = "Hi";
            if (userBean.getFirstName() != null && !userBean.getFirstName().isEmpty()) {
                displayName += ", " + userBean.getFirstName();
                if (userBean.getLastName() != null && !userBean.getLastName().isEmpty())
                    displayName += " " + userBean.getLastName();
            }
            headerText.setText(displayName);
        }else{
            fatchUserDetail();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeName(String name){
        toolbar.setTitle(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle course_view_navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_all_courses) {
            Intent i = new Intent(this, CourseListActivity.class);
            startActivity(i);
//        } else if (id == R.id.nav_vn_Science) {

        } else if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, "vocabnudge@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "feedback");
            intent.putExtra(Intent.EXTRA_TEXT, "");

            startActivity(Intent.createChooser(intent, "Send Email"));

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\nI would like to recommend you this application\n\n";
            sAux = sAux + "https://vocabnudge.herokuapp.com \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
//        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_logout) {
            LogoutTask task = new LogoutTask(this, new Consumer<HttpStatus>() {
                @Override
                public void consume(HttpStatus httpStatus) {
                    if(httpStatus.equals(HttpStatus.OK)){
                        Toast.makeText(MainActivity.this,"successfully signed out",Toast.LENGTH_SHORT)
                                .show();
                        SharedPreferences sharedPref = getSharedPreferences(Const.DEF_SHARED_PREF,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(Const.SHARED_PREF_TOKEN);
                        editor.commit();
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this,"error occurred in signing out",Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
            task.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fatchUserDetail(){
        // redirect to main activity if already logged in
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN, "");
        if(token.isEmpty()){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        UserDetailFetchTask task = new UserDetailFetchTask(this, new Consumer<UserBean>() {
            @Override
            public void consume(UserBean userBean) {
                if(userBean != null){
                    RunTimeStore.storeObj(Const.USER_DETAIL,userBean);
                }else{
                    SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove(Const.SHARED_PREF_TOKEN);
                    editor.commit();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                //TODO implement logic for error in fetching profile
            }
        });
        task.fetchUserBean();
    }

}
