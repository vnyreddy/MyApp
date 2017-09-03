package com.vinay.wizdem.myapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vinay.wizdem.myapp.FireBase.LoadData;
import com.vinay.wizdem.myapp.Interfaces.FetchData;
import com.vinay.wizdem.myapp.Models.AppData;
import com.vinay.wizdem.myapp.R;
import com.vinay.wizdem.myapp.Utils.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FetchData {

    private TextView textView, nav_name, nav_email;
    private FloatingActionButton phone, message, linkedin, stack;
    private static final int CALL_PERMEISSIONS_REQUEST = 1;
    private static final int SMS_PERMISSION_REQUEST = 2;
    private String phone_number;
    private String s;
    private LoadData loadData;
    private AppData mAppData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.content_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone = (FloatingActionButton) findViewById(R.id.phone);
        message = (FloatingActionButton) findViewById(R.id.message);
        linkedin = (FloatingActionButton) findViewById(R.id.ln);
        stack = (FloatingActionButton) findViewById(R.id.stack);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        nav_name = (TextView) headerLayout.findViewById(R.id.nav_name);
        nav_email = (TextView) headerLayout.findViewById(R.id.nav_email);
        navigationView.setNavigationItemSelectedListener(this);

        if (isNetworkConnected()) {

            loadData = new LoadData();
            //  loadData.getFirebaseData();
            loadData.getAssignData(this);

        } else {
            Util.noInternetToast(MainActivity.this);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneRequestPermission();

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageRequestPermission();
            }
        });
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAppData != null && mAppData.getLn_link() != null) {
                    if (isNetworkConnected()) {
                        openWebView(mAppData.getLn_link());
                    } else {
                        Util.noInternetToast(MainActivity.this);
                    }
                } else {
                    Util.nullMessageToast(MainActivity.this);
                }

            }
        });
        stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppData != null && mAppData.getStack_link() != null) {
                    if (isNetworkConnected()) {
                        openWebView(mAppData.getStack_link());
                    } else {
                        Util.noInternetToast(MainActivity.this);
                    }
                } else {
                    Util.nullMessageToast(MainActivity.this);
                }

            }
        });
    }

    private void messageComposeDialog() {
        if (mAppData != null && mAppData.getPhone_number() != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SMS");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            input.setHint("Compose Text for Vinay..");
            builder.setView(input);

            builder.setPositiveButton("Send", null);
            builder.setNegativeButton("Cancel", null);
            final AlertDialog mAlertdlg = builder.create();
            mAlertdlg.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button send = mAlertdlg.getButton(AlertDialog.BUTTON_POSITIVE);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = input.getText().toString();
                            if (text != null && !text.isEmpty()) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(mAppData.getPhone_number(), null, text, null, null);
                                Toast.makeText(MainActivity.this, "Sending SMS to Vinay..", Toast.LENGTH_SHORT).show();
                                mAlertdlg.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Enter text message..", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    Button cancel = mAlertdlg.getButton(AlertDialog.BUTTON_NEGATIVE);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAlertdlg.dismiss();
                        }
                    });
                }
            });

            mAlertdlg.show();
        } else {
            Util.noContactToast(MainActivity.this);
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void callConfirmDialog() {
        if (mAppData !=null && mAppData.getPhone_number() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Please Confirm..");
            builder.setMessage("You want to call Vinay Reddy?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse(mAppData.getPhone_number()));
                    startActivity(phoneIntent);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Util.noContactToast(MainActivity.this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_PERMEISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callConfirmDialog();
                } else {
                    Toast.makeText(MainActivity.this, "MyApp was denied call permissions.", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case SMS_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    messageComposeDialog();
                } else {
                    Toast.makeText(MainActivity.this, "MyApp was denied SMS permissions.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    void callPhoneRequestPermission() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMEISSIONS_REQUEST);
        } else {
            callConfirmDialog();
        }
    }

    void messageRequestPermission() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST);
        } else {
            messageComposeDialog();
        }
    }

    void openWebView(String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.share_link) {
            shareIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(mAppData != null){
            if (id == R.id.profile) {
                textView.setText(mAppData.getAbout_me());
                textView.setTextSize(25);
            } else if (id == R.id.work) {
                textView.setText(mAppData.getExperience());
                textView.setTextSize(25);
            } else if (id == R.id.contact) {
                if(mAppData.getPhone_number() != null && mAppData.getSkype() != null && mAppData.getEmail() != null){
                    textView.setText(mAppData.getPhone_number() + " \n" + "Skype: " +
                            mAppData.getSkype() + " \n" + mAppData.getEmail());
                    textView.setTextSize(20);
                }else {
                    textView.setText("Hu! No Contact Available At This Time.");
                    Util.noContactToast(MainActivity.this);
                }
            } else if (id == R.id.nav_share) {
                shareIntent();
            }
        }else {
            Util.nullMessageToast(MainActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void shareIntent() {
        if(mAppData != null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mAppData.getApp_link());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_hint)));
            progressDialog.dismiss();

        }else {
            Util.nullMessageToast(MainActivity.this);
        }

    }

    @Override
    public void assignData(AppData appData) {
        this.mAppData = appData;
        textView.setText(mAppData.getHome());
        textView.setTextSize(25);
        textView.setMovementMethod(new ScrollingMovementMethod());
        nav_name.setText(mAppData.getName());
        nav_email.setText(mAppData.getEmail());
    }
    @Override
    public void databaseError() {
        Toast.makeText(MainActivity.this,"Sorry!! There is a Database Error!!",Toast.LENGTH_SHORT).show();
    }
}
