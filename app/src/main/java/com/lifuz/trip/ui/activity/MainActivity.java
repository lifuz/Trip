package com.lifuz.trip.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.lifuz.trip.R;
import com.lifuz.trip.ui.fragment.ContactFragment;
import com.lifuz.trip.ui.fragment.KnowledgeFragment;
import com.lifuz.trip.ui.fragment.MineFragment;
import com.lifuz.trip.ui.fragment.WealthFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 110;

    private BottomBar bottomBar;

    private FragmentManager fm;

    private KnowledgeFragment knowledgeFragment;
    private WealthFragment wealthFragment;
    private ContactFragment contactFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItems(R.menu.bottombar_menu);

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.primary));
        bottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.primary));
        bottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.primary));
        bottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.primary));

//        bottomBar.noNavBarGoodness();


        Intent it = getIntent();

        int page = it.getIntExtra("page",-1);

        if (page != -1 && page < 4){
            bottomBar.selectTabAtPosition(page,true);
        }

        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                Log.e(TAG,menuItemId + "onMenuTabSelected");

                FragmentTransaction transaction = fm.beginTransaction();

                hideFragment(transaction);

                switch (menuItemId){
                    case R.id.bottom_one:

                        if (knowledgeFragment == null) {
                            knowledgeFragment = new KnowledgeFragment();
                            transaction.add(R.id.fragment_content,knowledgeFragment);
                        } else {
                            transaction.show(knowledgeFragment);
                        }

                        break;

                    case R.id.bottom_two:

                        if (contactFragment == null) {
                            contactFragment = new ContactFragment();
                            transaction.add(R.id.fragment_content,contactFragment);
                        } else {
                            transaction.show(contactFragment);
                        }

                        break;

                    case R.id.bottom_three:

                        if (wealthFragment == null) {
                            wealthFragment = new WealthFragment();
                            transaction.add(R.id.fragment_content,wealthFragment);
                        } else {
                            transaction.show(wealthFragment);
                        }

                        break;

                    case R.id.bottom_four:

                        Log.e(TAG,"zhixingl");

                        if (mineFragment == null) {
                            mineFragment = new MineFragment();
                            transaction.add(R.id.fragment_content,mineFragment);
                        } else {
                            transaction.show(mineFragment);
                        }

                        break;
                }

                transaction.commit();



            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

                Log.e(TAG,menuItemId + "onMenuTabReSelected");

            }
        });

        //安卓6.0开始某些权限需要动态获取，以下就是动态获取授权的方法
        if (Build.VERSION.SDK_INT >= 23) {

            //判断权限是否已经授权
            int checkACCESSCOARSELOCATION = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            int checkACCESSFINELOCATION = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int checkREADPHONESTATE = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);
            int checkREADSMS = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_SMS);
//
//            int checkREADCONTACTS = ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_CONTACTS);

            int checkWRITEEXTERNALSTORAGE = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            int checkRECEIVESMS = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECEIVE_SMS);


            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_SMS};

            //如果没有授权，则调用授权方法
            if (checkACCESSCOARSELOCATION != PackageManager.PERMISSION_GRANTED
                    || checkACCESSFINELOCATION != PackageManager.PERMISSION_GRANTED
                    || checkREADPHONESTATE != PackageManager.PERMISSION_GRANTED
                    || checkREADSMS != PackageManager.PERMISSION_GRANTED
//                    || checkREADCONTACTS != PackageManager.PERMISSION_GRANTED
                    || checkWRITEEXTERNALSTORAGE != PackageManager.PERMISSION_GRANTED
                    || checkRECEIVESMS != PackageManager.PERMISSION_GRANTED
                    ) {

                //请求权限
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
                return;

            }


        }

    }

    /**
     * 授权结果返回方法
     *
     * @param requestCode  授权请求的返回吗
     * @param permissions  请求那些权限
     * @param grantResults 每个权限的授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //根据授权的返回码，确定授权的调用者
        switch (requestCode) {
            case REQUEST_CODE:

                Log.e(TAG, grantResults[0] + "  " + grantResults[4] + "  " + PackageManager.PERMISSION_GRANTED);

                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

    private void hideFragment(FragmentTransaction transaction){
        if (knowledgeFragment !=null) {
            transaction.hide(knowledgeFragment);
        }

        if (contactFragment != null) {
            transaction.hide(contactFragment);
        }

        if (wealthFragment != null) {
            transaction.hide(wealthFragment);
        }

        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    private long firstTime=0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();
                if(secondTime-firstTime>2000){
                    Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }else{
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
