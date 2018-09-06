package com.rezaduty.fun;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.farsitel.bazaar.IUpdateCheckService;
import com.rezaduty.fun.adapter.PersonAdapter;
import com.rezaduty.fun.database.SqliteDatabase;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SqliteDatabase mDatabase;
    private ResideMenu resideMenu;
    private MainActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private  MediaPlayer player;
    private  boolean ok;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestSmsPermission();





        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText,String messageAdress) {
                Log.d("Text",messageText);
                Log.d("Address",messageAdress);
                if(messageAdress.startsWith("+")){
                    messageAdress = messageAdress.substring(3,messageAdress.length());
                    Log.d("Address",messageAdress);
                }
                ok = Boolean.valueOf(mDatabase.findPerson(messageAdress,messageText));

                Log.d("ok", String.valueOf(ok));
                if(ok){
                        Log.d("ok address",messageAdress);
                        try {
                            AudioManager am =
                                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                            am.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                    0);
                            AssetFileDescriptor afd = getAssets().openFd("beep.mp3");
                            player = new MediaPlayer();
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                            Toast.makeText(MainActivity.this,messageAdress +" "+messageText,Toast.LENGTH_LONG).show();

                        }catch (Exception e){

                        }


                }



            }
        });




        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);

        RecyclerView productView = (RecyclerView)findViewById(R.id.person_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productView.setLayoutManager(linearLayoutManager);
        productView.setHasFixedSize(true);

        mDatabase = new SqliteDatabase(this);
        List<Person> allPeople = mDatabase.listPerson();

        if(allPeople.size() > 0){
            productView.setVisibility(View.VISIBLE);
            PersonAdapter mAdapter = new PersonAdapter(this, allPeople);
            productView.setAdapter(mAdapter);

        }else {
            productView.setVisibility(View.GONE);
            Toast.makeText(this, "هیچ شماره ای موجود نیست", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new quick task
                addTaskDialog();
            }
        });

        setUpMenu();

    }


    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }



    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
            resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);

        }
        return super.onOptionsItemSelected(item);
    }
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy h:m:s");
/*        Calendar calendar = Calendar.getInstance();


        Log.e("date", "" + calendar.get(Calendar.DAY_OF_MONTH));

        Log.e("month", ""+calendar.get(Calendar.MONTH));

        Log.e("year", ""+calendar.get(Calendar.YEAR));

        Log.e("hour", ""+calendar.get(Calendar.HOUR));

        Log.e("minutes", ""+calendar.get(Calendar.MINUTE));

        Log.e("seconds", ""+calendar.get(Calendar.SECOND));*/
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Log.e("hour", ""+String.valueOf(hour));

        if(hour<7 || hour>=20){
            resideMenu.setBackground(R.drawable.menu_background_night);
        }else{
            resideMenu.setBackground(R.drawable.menu_background_day);
        }





        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        //itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        //itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        //itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Calendar");
        itemHome = new ResideMenuItem(this, R.drawable.icon_home, "صفحه اصلی");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_about, "درباره من");

        itemHome.setOnClickListener(this);
        //itemProfile.setOnClickListener(this);
        //itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_RIGHT);
        //resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        /*findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });*/

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (view == itemSettings){
            changeFragment(new AboutFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_to_do, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_person_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText titleField = (EditText)subView.findViewById(R.id.enter_title);
        nameField.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("اضافه کردن");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("اضافه کردن", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleField.getText().toString();
                if(TextUtils.isEmpty(title)){
                    titleField.setText("بیدار شو");
                    title = titleField.getText().toString();
                }
                String name = String.valueOf(nameField.getText().toString());

                if(TextUtils.isEmpty(name) || title.length() <= 0){

                    Toast.makeText(MainActivity.this, "لطفا مقادیر را بررسی کنید", Toast.LENGTH_LONG).show();
                }
                else{
                    if(name.startsWith("0")){
                        name = name.substring(1,name.length()).toString();
                    }
                    Log.d("asasasa",title);

                    Log.d("asasasa",name);
                    Person newPerson = new Person(name,title);
                    mDatabase.addPerson(newPerson);

                    //refresh the activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "لغو شد.", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
}
