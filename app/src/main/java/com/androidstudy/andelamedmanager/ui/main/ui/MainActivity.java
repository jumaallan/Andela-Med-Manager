package com.androidstudy.andelamedmanager.ui.main.ui;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.andelamedmanager.R;
import com.androidstudy.andelamedmanager.data.model.Medicine;
import com.androidstudy.andelamedmanager.data.model.MenuView;
import com.androidstudy.andelamedmanager.data.model.User;
import com.androidstudy.andelamedmanager.databinding.ActivityMainBinding;
import com.androidstudy.andelamedmanager.settings.Settings;
import com.androidstudy.andelamedmanager.ui.auth.ui.AuthActivity;
import com.androidstudy.andelamedmanager.ui.main.adapter.MainDashboardAdapter;
import com.androidstudy.andelamedmanager.ui.main.viewmodel.MainViewModel;
import com.androidstudy.andelamedmanager.ui.medicine.adapter.DailyMedicineAdapter;
import com.androidstudy.andelamedmanager.ui.medicine.adapter.DailyMedicineStatisticsAdapter;
import com.androidstudy.andelamedmanager.ui.medicine.ui.AddMedicineActivity;
import com.androidstudy.andelamedmanager.ui.medicine.ui.MedicineActivity;
import com.androidstudy.andelamedmanager.ui.medicine.ui.MonthlyIntakeActivity;
import com.androidstudy.andelamedmanager.ui.medicine.ui.SearchMedsActivity;
import com.androidstudy.andelamedmanager.ui.medicine.viewmodel.MedicineViewModel;
import com.androidstudy.andelamedmanager.util.CirclePagerIndicatorDecoration;
import com.androidstudy.andelamedmanager.util.ItemOffsetDecoration;
import com.androidstudy.andelamedmanager.view.ProfileDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tingyik90.snackprogressbar.SnackProgressBar;
import com.tingyik90.snackprogressbar.SnackProgressBarManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int NOTIFICATION_ID = 0;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.recyclerViewDailyMedicineStatistics)
    RecyclerView recyclerViewDailyMedicineStatistics;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerViewDailyMedicine)
    RecyclerView recyclerViewDailyMedicine;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.text_empty)
    TextView emptyText;
    @BindView(R.id.layout_empty)
    FrameLayout emptyFrame;
    @BindView(R.id.cardMedDaily)
    CardView cardMedDaily;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    User user;
    AlarmManager alarmManager;
    private MainViewModel mainViewModel;
    private ProfileDialog profileDialog;
    private SnackProgressBarManager snackProgressBarManager;
    private GoogleApiClient mGoogleApiClient;
    private List<Medicine> medicineList;
    private List<MenuView> menuViewList;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        user = mainViewModel.getUserLiveData();
        binding.setUser(user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In Api and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        init();

        profileDialog = ProfileDialog.newInstance(((dialog, which) -> logout()));

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        date.setText(currentDate);

        //Initialize Snackbar Manager -> Attach/pin to the bottom of the layout :)
        snackProgressBarManager = new SnackProgressBarManager(coordinatorLayout)
                .setProgressBarColor(R.color.colorAccent)
                .setOverlayLayoutAlpha(0.6f);

        menuViewList = getMenuOptions();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        MainDashboardAdapter mainDashboardAdapter = new MainDashboardAdapter(this, menuViewList, (v, position) -> {
            MenuView role = menuViewList.get(position);
            String menuName = role.getName();
            switch (menuName) {
                case "Add Medicine":
                    Intent addMedicine = new Intent(getApplicationContext(), AddMedicineActivity.class);
                    startActivity(addMedicine);
                    break;
                case "Search Meds":
                    Intent searchMeds = new Intent(getApplicationContext(), SearchMedsActivity.class);
                    startActivity(searchMeds);
                    break;
                case "Monthly Intake":
                    Intent monthlyIntake = new Intent(getApplicationContext(), MonthlyIntakeActivity.class);
                    startActivity(monthlyIntake);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Sorry, It's Under development", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        recyclerView.setAdapter(mainDashboardAdapter);

        MedicineViewModel medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);
        medicineViewModel.getMedicineList().observe(this, medicines -> {
            if (MainActivity.this.medicineList == null) {
                setListData(medicines);
            }
        });
    }

    public void setListData(final List<Medicine> medicineList) {
        this.medicineList = medicineList;
        if (medicineList.isEmpty()) {
            emptyFrame.setVisibility(View.VISIBLE);
        } else {

            cardMedDaily.setVisibility(View.VISIBLE);

            DailyMedicineStatisticsAdapter dailyMedicineStatisticsAdapter = new DailyMedicineStatisticsAdapter(this, medicineList, (v, position) -> {
                Medicine medicine = medicineList.get(position);
                Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("MEDICINE", medicine);

                intent.putExtras(b);
                startActivity(intent);
            });

            recyclerViewDailyMedicineStatistics.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));
            // add pager behavior
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerViewDailyMedicineStatistics);
            // pager indicator
            recyclerViewDailyMedicineStatistics.addItemDecoration(new CirclePagerIndicatorDecoration());
            recyclerViewDailyMedicineStatistics.setAdapter(dailyMedicineStatisticsAdapter);

            DailyMedicineAdapter dailyMedicineAdapter = new DailyMedicineAdapter(this, medicineList, (v, position) -> {
                Medicine medicine = medicineList.get(position);
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), MedicineActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("MEDICINE", medicine);
                intent.putExtras(b);
                MainActivity.this.startActivity(intent);
            });

            recyclerViewDailyMedicine.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
            recyclerViewDailyMedicine.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));
            recyclerViewDailyMedicine.setAdapter(dailyMedicineAdapter);
        }
    }

    private void init() {
        emptyText.setText(Html.fromHtml(getString(R.string.text_empty_message)));

        //Alarms
//        //Set up the Notification Broadcast Intent
//        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
//
//        //Check if the Alarm is already set, and check the toggle accordingly
//        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent,
//                PendingIntent.FLAG_NO_CREATE) != null);
//
//        //Set up the PendingIntent for the AlarmManager
//        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
//                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        long triggerTime = SystemClock.elapsedRealtime()
//                + 60 * 1000;
//
//        long repeatInterval = 60 * 1000;
////        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
//
//        //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                triggerTime, repeatInterval, notifyPendingIntent);

        //TODO :: Rework this
        //Cancel the alarm and notification if the alarm is turned off
//        alarmManager.cancel(notifyPendingIntent);
//        mNotificationManager.cancelAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem profileItem = menu.findItem(R.id.action_profile);
        Glide.with(this)
                .asBitmap()
                .load(user.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        profileItem.setIcon(new BitmapDrawable(getResources(), resource));
                    }
                });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            profileDialog.show(getSupportFragmentManager(), "profile");
            return true;
        } else if (id == R.id.action_settings) {
            // startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Mock Data for UI Cards
    private List<MenuView> getMenuOptions() {
        List<MenuView> listViewItems = new ArrayList<>();
        listViewItems.add(new MenuView(1, "Add Medicine", R.drawable.ic_add_medicine));
        listViewItems.add(new MenuView(2, "Monthly Intake", R.drawable.ic_monthly_intake));
        listViewItems.add(new MenuView(3, "Search Meds", R.drawable.ic_search));
        return listViewItems;
    }

    private void logout() {
        if (!Settings.isLoggedIn()) {
            return;
        }

        SnackProgressBar snackProgressBar = new SnackProgressBar(
                SnackProgressBar.TYPE_INDETERMINATE,
                "Logging Out...")
                .setSwipeToDismiss(false);

        // Show snack progress during logout
        snackProgressBarManager.dismissAll();
        snackProgressBarManager.show(snackProgressBar, SnackProgressBarManager.LENGTH_INDEFINITE);

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
            //Clear Shared Pref File
            Settings.setLoggedInSharedPref(false);
            if (status.isSuccess()) {
                //Clear Local DB
                mainViewModel.deleteAll();
                //Redirect User to Login Page
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Unreachable anyway
        snackProgressBarManager.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
