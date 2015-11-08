package com.example.lytuananh.demowonoloapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lytuananh.demowonoloapp.MyApplication;
import com.example.lytuananh.demowonoloapp.R;
import com.example.lytuananh.demowonoloapp.instagram.InstagramApp;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogin;
    private Button mBtnGetLocation;
    private InstagramApp mApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private LinearLayout mLnProfile;
    private ImageView mImgProfile;
    private TextView mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApp = new InstagramApp(this, getResources().getString(R.string.client_id),
                getResources().getString(R.string.client_secret), getResources().getString(R.string.callback_url));
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                // tvSummary.setText("Connected as " + mApp.getUserName());
                mBtnLogin.setText("Disconnect");
                // userInfoHashmap = mApp.
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        //init View
        findView();

        if (mApp.hasAccessToken()) {
            // tvSummary.setText("Connected as " + mApp.getUserName());
            mBtnLogin.setText("Disconnect");
            mApp.fetchUserName(handler);
            ((MyApplication)getApplication()).setInstgramMap(mApp);
            String image = mApp.getUserInfo().get("profile_picture");
            mLnProfile.setVisibility(View.VISIBLE);
            mUserName.setText(mApp.getUserName());
            if(image!=null && image.trim().length()>0) {
                Picasso.with(getApplicationContext())
                        .load(mApp.getUserInfo().get("profile_picture"))
                        .fit()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(mImgProfile);
            }

        }
    }

    private void findView() {

        mBtnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        mBtnLogin.setOnClickListener(this);
        mBtnGetLocation = (Button) findViewById(R.id.btn_location);
        mBtnGetLocation.setOnClickListener(this);
        mLnProfile = (LinearLayout) findViewById(R.id.activity_login_ln_profile);
        mImgProfile= (ImageView) findViewById(R.id.activity_login_img_profile);
        mUserName= (TextView) findViewById(R.id.activity_login_tv_user_name);

    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
                ((MyApplication)getApplication()).setInstgramMap(mApp);
                mLnProfile.setVisibility(View.VISIBLE);
                String image = mApp.getUserInfo().get("profile_picture");
                mUserName.setText(mApp.getUserName());
                if(image!=null && image.trim().length()>0) {
                    Picasso.with(getApplicationContext())
                            .load(mApp.getUserInfo().get("profile_picture"))
                            .fit()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(mImgProfile);
                }

            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(LoginActivity.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_login_btn_login){
            connectOrDisconnectUser();
        }else if(v.getId() == R.id.btn_location){
            Intent i =  new Intent(LoginActivity.this,MapsActivity.class);
            startActivity(i);
        }
    }

    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                    // btnConnect.setVisibility(View.VISIBLE);
                                    mBtnLogin.setText("Connect");
                                    mLnProfile.setVisibility(View.GONE);
                                    // tvSummary.setText("Not connected");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }
}
