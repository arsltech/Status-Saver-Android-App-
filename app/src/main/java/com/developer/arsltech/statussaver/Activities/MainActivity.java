package com.developer.arsltech.statussaver.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.developer.arsltech.statussaver.Adapters.StoryAdapter;
import com.developer.arsltech.statussaver.Model.StoryModel;
import com.developer.arsltech.statussaver.R;
import com.developer.arsltech.statussaver.Utils.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.google.android.gms.ads.MobileAds.*;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    Toolbar mToolbar;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private StoryAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private File[] files;
    private SwipeRefreshLayout recyclerLayout;
    Drawer resultDrawer;
    AccountHeader headerResult;
    public static final int ITEMS_PER_AD = 6;
    ArrayList<Object> filesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initialize(this,
                "ca-app-pub-8863088720647115~6026758042");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        if (getAppIntro(this)) {
            Intent i = new Intent(this, IntroActivity.class);
            startActivity(i);
        }
        initComponents();
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        boolean result = checkPermission();
        if (result) {
           setUpRecyclerView();
        }
       // Typeface typeface = Typeface.createFromAsset(getAssets(), "sintony-regular.otf");
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withAlternativeProfileHeaderSwitching(false)
                .withCompactStyle(false)
                .withHeaderBackground(R.color.colorPrimary)
                .withDividerBelowHeader(false)
                .withProfileImagesVisible(true)
                //.withTypeface(typeface)
                .addProfiles(new ProfileDrawerItem().withIcon(R.drawable.icon).withName(getResources().getString(R.string.app_name)).withEmail(getResources()
                        .getString(R.string.developer_email)))
                .build();
        resultDrawer = new DrawerBuilder()
                .withActivity(this)
                .withSelectedItem(-1)
                .withFullscreen(true)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withCloseOnClick(true)
                .withMultiSelect(false)
                .withTranslucentStatusBar(true)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withSelectable(false).withName("Gallery").withIcon(R.drawable.ic_home_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                                startActivity(intent);
                                return false;
                            }
                        })//.withTypeface(typeface),

                        ,new PrimaryDrawerItem().withSelectable(false).withName("Rate Us").withIcon(R.drawable.ic_star_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                try{

                                    Uri uri = Uri.parse("market://details?id="+getPackageName());
                                    Intent gotoMarket = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(gotoMarket);
                                }
                                catch (ActivityNotFoundException e) {

                                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName());
                                    Intent gotoMarket = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(gotoMarket);
                                }
                                return false;
                            }
                        })//.withTypeface(typeface),

                        ,new PrimaryDrawerItem().withSelectable(false).withName("More Apps").withIcon(R.drawable.ic_share_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                try{
                                    Uri uri = Uri.parse("market://search?q=pub:ArslTech");
                                    Intent gotoMarket = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(gotoMarket);
                                }
                                catch (ActivityNotFoundException e) {

                                    Uri uri = Uri.parse("http://play.google.com/store/search?q=pub:ArslTech");
                                    Intent gotoMarket = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(gotoMarket);
                                }
                                return false;
                            }
                        })//.withTypeface(typeface),
                        ,
                        new PrimaryDrawerItem().withSelectable(false).withName("How to use?").withIcon(R.drawable.ic_info_outline_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                startActivity(new Intent(getApplicationContext(),IntroActivity.class));
                                return false;
                            }
                        })
                        ,new PrimaryDrawerItem().withSelectable(false).withName("Exit").withIcon(R.drawable.ic_exit_to_app_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                onBackPressed();
                                return false;
                            }
                        })
                        ,new PrimaryDrawerItem().withSelectable(false).withName("Privacy Policy").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("PRIVACY POLICY")
                                        .setMessage(R.string.privacy_message)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(R.drawable.ic_info_black_24dp)
                                        .show();
                                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                textView.setScroller(new Scroller(MainActivity.this));
                                textView.setVerticalScrollBarEnabled(true);
                                textView.setMovementMethod(new ScrollingMovementMethod());


                                return false;
                            }
                        })//.withTypeface(typeface)
                ).withSavedInstance(savedInstanceState)
                .build();

    }


    private boolean getAppIntro(MainActivity mainActivity) {
        SharedPreferences preferences;
        preferences = mainActivity.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean("AppIntro", true);
    }



    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private void initComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRecyclerView);
        recyclerLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerLayout.setRefreshing(true);
                setUpRecyclerView();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        setUpRecyclerView();
                } else {
                    checkAgain();
                }
                break;
        }
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new StoryAdapter(MainActivity.this, getData());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    private ArrayList<Object> getData() {
        StoryModel f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + "Media/.Statuses";
        File targetDirector = new File(targetPath);
        files = targetDirector.listFiles();
        if (files == null) {
//            noImageText.setVisibility(View.INVISIBLE);
        }
        try {
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                f = new StoryModel();
                f.setName("Status: "+(i));
                f.setUri(Uri.fromFile(file));
                f.setPath(files[i].getAbsolutePath());
                f.setFilename(file.getName());
                filesList.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filesList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_whatapp) {
            Intent launchIntent = MainActivity.this.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            startActivity(launchIntent);

        }

        if (item.getItemId() == R.id.ic_tutorial) {
            startActivity(new Intent(getApplicationContext(),IntroActivity.class));

        }
        if (item.getItemId() == R.id.ic_about) {
            startActivity(new Intent(getApplicationContext(),about_mine.class));

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

    }
}
