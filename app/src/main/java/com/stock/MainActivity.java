package com.stock;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.stock.baseclass.BaseAppCompactActivity;
import com.stock.container.BaseContainer;
import com.stock.container.BuyStockContainer;
import com.stock.container.HomeContainer;
import com.stock.container.IPOContainer;
import com.stock.container.MutualFundsContainer;
import com.stock.container.NewsContainer;
import com.stock.container.SmeIPOContainer;
import com.stock.databinding.ActivityMainBinding;
import com.stock.fragments.NSEFragment;
import com.stock.fragments.ProfileFragment;
import com.stock.interfaces.OnSetActinBarTitle;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.MessageResponse;
import com.stock.utility.MyPref;
import com.stock.utility.Utility;

import retrofit2.Call;

public class MainActivity extends BaseAppCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnApiResponseListner, OnSetActinBarTitle {

    private ActivityMainBinding mBinding;
    private static String currentFragment;
    private Call<?> callLogout;
    private AdView mAdview;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, "ca-app-pub-6755350048733241~1715926729");

        mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6755350048733241/1507085300");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        NotificationScheduler.setReminder(this, AlarmReceiver.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
                handler.postDelayed(this, 300*1000);
            }
        }, 300*1000);


        currentFragment = HomeContainer.class.getSimpleName();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new HomeContainer(), HomeContainer.class.getSimpleName());
        ft.commit();

    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callLogout != null && !callLogout.isCanceled())
            callLogout.cancel();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            boolean isPop = false;
            if (currentFragment.equals(HomeContainer.class.getSimpleName())) {
                isPop = ((BaseContainer) getSupportFragmentManager().findFragmentByTag(HomeContainer.class.getSimpleName())).popFragment();
            } else if (currentFragment.equals(NewsContainer.class.getSimpleName())) {
                isPop = ((BaseContainer) getSupportFragmentManager().findFragmentByTag(NewsContainer.class.getSimpleName())).popFragment();
            } else if (currentFragment.equals(BuyStockContainer.class.getSimpleName())) {
                isPop = ((BaseContainer) getSupportFragmentManager().findFragmentByTag(BuyStockContainer.class.getSimpleName())).popFragment();
            } else if (currentFragment.equals(IPOContainer.class.getSimpleName())) {
                isPop = ((BaseContainer) getSupportFragmentManager().findFragmentByTag(IPOContainer.class.getSimpleName())).popFragment();
            } else if (currentFragment.equals(SmeIPOContainer.class.getSimpleName())) {
                isPop = ((BaseContainer) getSupportFragmentManager().findFragmentByTag(SmeIPOContainer.class.getSimpleName())).popFragment();
            } else if (currentFragment.equals(MutualFundsContainer.class.getSimpleName())) {
                isPop = ((BaseContainer) getSupportFragmentManager().findFragmentByTag(MutualFundsContainer.class.getSimpleName())).popFragment();
            } else if (currentFragment.equals(NSEFragment.class.getSimpleName())) {
                isPop = false;
            } else if (currentFragment.equals(ProfileFragment.class.getSimpleName())) {
                isPop = false;
            }

            if (!isPop) {
                super.onBackPressed();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        String title = null;

        switch (id) {
            case R.id.nav_home:
                title = "My Money Place";
                currentFragment = HomeContainer.class.getSimpleName();
                fragment = new HomeContainer();
                break;
            case R.id.nav_profile:
                title = "Profile";
                currentFragment = ProfileFragment.class.getSimpleName();
                fragment = new ProfileFragment();
                break;
            case R.id.nav_buystock:
                title = "Stock";
                currentFragment = BuyStockContainer.class.getSimpleName();
                fragment = new BuyStockContainer();
                break;
            case R.id.nav_ipo:
                title = "IPO";
                currentFragment = IPOContainer.class.getSimpleName();
                fragment = new IPOContainer();
                break;
            case R.id.nav_smeipo:
                title = "SME IPO";
                currentFragment = SmeIPOContainer.class.getSimpleName();
                fragment = new SmeIPOContainer();
                break;
            case R.id.nav_mutualFund:
                title = "Mutual Fund";
                currentFragment = MutualFundsContainer.class.getSimpleName();
                fragment = new MutualFundsContainer();
                break;
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.nav_rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
            case R.id.nav_news:
                title = "News";
                currentFragment = NewsContainer.class.getSimpleName();
                fragment = new NewsContainer();
                break;
            case R.id.nav_NSE:
                title = "NSE";
                Bundle bundle = new Bundle();
                bundle.putString("keyurl", "https://nseindia.com/");
                currentFragment = NSEFragment.class.getSimpleName();
                fragment = new NSEFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.nav_BSE:
                title = "BSE";
                Bundle bundle1 = new Bundle();
                bundle1.putString("keyurl", "http://www.bseindia.com/");
                currentFragment = NSEFragment.class.getSimpleName();
                fragment = new NSEFragment();
                fragment.setArguments(bundle1);
                break;
            case R.id.nav_MSE:
                title = "MSE";
                Bundle bundle2 = new Bundle();
                bundle2.putString("keyurl", "https://www.msei.in/");
                currentFragment = NSEFragment.class.getSimpleName();
                fragment = new NSEFragment();
                fragment.setArguments(bundle2);
                break;
            case R.id.nav_Logout:
                if (Utility.haveNetworkConnection(this)) {
                    showDialog();
                    callLogout = ((StockApp) getApplication()).getApiTask().logout(new APICallback(this, APICall.LOGOUT_REQ_CODE, this));
                } else {
                    Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
                }
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
            ft.commit();
            getSupportActionBar().setTitle(title);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        dismissDialog();
        if (requestCode == APICall.LOGOUT_REQ_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse logout = (MessageResponse) clsGson;
                Utility.showRedSnackBar(mBinding.getRoot(), logout.getMessage());
                getMyPref().setData(MyPref.Keys.TOKEN, "");
                getMyPref().setData(MyPref.Keys.ISLOGIN, false);
                mBinding.getRoot().postDelayed(runnable, 1000);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(MainActivity.this);

        }
    };

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }
    }

    @Override
    public void onSetActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
