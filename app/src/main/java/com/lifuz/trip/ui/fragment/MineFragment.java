package com.lifuz.trip.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifuz.trip.R;
import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.application.TripApplication;
import com.lifuz.trip.enums.SelfState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.User;
import com.lifuz.trip.module.mine.UserExper;
import com.lifuz.trip.ui.activity.EditUserActivity;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.ui.component.DaggerMineComponent;
import com.lifuz.trip.ui.module.MineModule;
import com.lifuz.trip.ui.presenter.MinePresenter;
import com.lifuz.trip.utils.CircleTransform;
import com.lifuz.trip.utils.SharedPreferencesUtils;
import com.lifuz.trip.utils.SnackBarUtils;
import com.lifuz.trip.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 我的模块的fragment
 *
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/7/12 21:34
 */
public class MineFragment extends BaseFragment {

    private static final String TAG = "MineFragment";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.center_title)
    protected TextView centerTitle;

    @Inject
    MinePresenter minePresenter;

    @BindView(R.id.iv_head)
    ImageView ivHead;

    @BindView(R.id.btn_sign)
    AppCompatButton btnSign;

    @BindView(R.id.level_progress)
    ProgressBar progressBar;

    @BindView(R.id.level_exper)
    TextView levelExper;

    @BindView(R.id.level_name)
    TextView levelName;

    @BindView(R.id.tv_edit)
    TextView tvEdit;

    @BindView(R.id.tv_occu)
    TextView tvOccu;


    private RelativeLayout layout_choose;
    private RelativeLayout layout_photo;
    private RelativeLayout layout_close;

    @BindView(R.id.mine_all)
    LinearLayout layout_all;

    protected int mScreenWidth;

    PopupWindow avatorPop;

    /**
     * 定义三种状态
     */
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CAM = 2;//相机
    private static final int REQUESTCODE_CUT = 3;//图片裁剪

    private Bitmap mBitmap;
    private File mFile;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        centerTitle.setText("李富");

        toolbar.setElevation(0F);

        inject();

        Picasso.with(getContext()).load(R.drawable.test).transform(new CircleTransform()).into(ivHead);

        //获取用户信息
        minePresenter.getUser(getContext());

        minePresenter.userExper(getContext());

    }

    @OnClick({R.id.tv_edit,R.id.tv_occu})
    public void edit(){

        startActivity(new Intent(getActivity(), EditUserActivity.class));
        getActivity().finish();

    }

    /**
     * 签到结果处理
     * @param selfState 签到结果
     */
    public void sginResult(SelfState selfState) {

        if (selfState.getState() == 204) {

            SnackBarUtils.makeShort(toolbar,selfState.getStateInfo()).info();

            minePresenter.userExper(getContext());

        } else {

            SnackBarUtils.makeShort(toolbar,selfState.getStateInfo()).danger();
            btnSign.setEnabled(true);
            btnSign.setText("签到");
        }

    }

    /**
     * 签到按钮点击事件
     */
    @OnClick(R.id.btn_sign)
    public void clickSgin(){
        minePresenter.sgin(getContext(),null);

        btnSign.setEnabled(false);
        btnSign.setText("签到中");
    }

    /**
     * 头像点击事件
     */
    @OnClick(R.id.iv_head)
    public void clickHead() {

        showMyDialog();

    }

    /**
     * 设置经验信息
     * @param userExperSelfResult 经验相关信息
     */
    public void setUserExper(SelfResult<UserExper> userExperSelfResult) {

        if (userExperSelfResult.isSuccess()) {

            UserExper userExper = userExperSelfResult.getData();

            levelName.setText(userExper.getExperLevelName()+" ( lv "+ userExper.getExperLevel() +")");

            if (userExper.getStatus() == 0) {
                btnSign.setEnabled(true);
                btnSign.setText("签到");
            } else {
                btnSign.setEnabled(false);
                btnSign.setText("已签到");
            }

            progressBar.setMax(Integer.parseInt(userExper.getExperMaxValue() + ""));
            progressBar.setProgress(Integer.parseInt(userExper.getExperValue() + ""));

            levelExper.setText (userExper.getExperValue() + "/" + userExper.getExperMaxValue());


        } else {
            SnackBarUtils.makeShort(ivHead,userExperSelfResult.getError()).danger();
        }

    }

    /**
     * 获取到用户信息
     *
     * @param selfResult 用户信息
     */
    public void user(SelfResult<User> selfResult) {

        if (selfResult.isSuccess()) {

            Log.e(TAG, "处理成功数据");

            User user = selfResult.getData();

            centerTitle.setText(user.getUserName());

            if (user.getUserHeadPortrait() != null) {

                Picasso.with(getContext())
                        .load(getString(R.string.base_image_url) + user.getUserHeadPortrait()).transform(new CircleTransform())
                        .into(ivHead);
            }

            if (StringUtils.isEmpty(user.getSignature())) {

                if (!StringUtils.isEmpty(user.getOccupation())){

                    tvEdit.setText(user.getOccupation());
                }

            } else {

                tvEdit.setText(user.getSignature());

                if (!StringUtils.isEmpty(user.getOccupation())){

                    tvOccu.setText(user.getOccupation());
                    tvOccu.setVisibility(View.VISIBLE);
                }

            }

        } else {
            if ("1".equals(selfResult.getError())) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                SnackBarUtils.makeLong(toolbar, selfResult.getError()).danger();
            }
        }
    }

    /**
     * 更新头像结果
     *
     * @param selfState 状态码
     * @param error     错误信息
     */
    public void updateUrl(SelfState selfState, String error) {

        if (!StringUtils.isEmpty(error)) {

            if ("1".equals(error)) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                SnackBarUtils.makeLong(toolbar, error).danger();
            }
            return;
        }

        if (selfState.getState() == 202) {
            Picasso.with(getContext()).load(new File("/sdcard/trip/image/head.png"))
                    .transform(new CircleTransform()).into(ivHead);
        } else {
            SnackBarUtils.makeLong(toolbar, selfState.getStateInfo()).danger();
            return;
        }

    }

    /**
     * 初始化数据层
     */
    private void inject() {

        AppComponent appComponent = ((TripApplication) getActivity().getApplication()).getAppComponent();

        DaggerMineComponent.builder().appComponent(appComponent)
                .mineModule(new MineModule(this)).build().inject(this);

    }

    /**
     * 设置按钮点击事件处理
     */
    @OnClick(R.id.mine_setting)
    public void setting() {


        Map<String, String> map = new HashMap<>();

        map.put("token", "");
        SharedPreferencesUtils.saveTakon(getContext(), map);

        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();

    }


    /**
     * 打开相册
     */
    private void openPic() {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, REQUESTCODE_PIC);
    }

    /**
     * 调用相机
     */
    private void openCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            mFile = new File(file, System.currentTimeMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, REQUESTCODE_CAM);
        } else {
            Toast.makeText(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_CAM:
                    startPhotoZoom(Uri.fromFile(mFile));
                    break;
                case REQUESTCODE_PIC:

                    if (data == null || data.getData() == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());

                    break;
                case REQUESTCODE_CUT:

                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            //这里也可以做文件上传
            mBitmap = bundle.getParcelable("data");

            File file = new File("/sdcard/trip/image");
            if (!file.exists()) {
                file.mkdirs();
            }

            String path = "/sdcard/trip/image/head.png";

            FileOutputStream b = null;

            try {
                b = new FileOutputStream(path);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {

                    if ( b != null) {
                        // 关闭流
                        b.flush();
                        b.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            minePresenter.updateHead(getContext(), new File(path));

        }
    }

    /**
     * 打开系统图片裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUT);

    }

    private void showMyDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_head_dialog,
                null);
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_close = (RelativeLayout) view.findViewById(R.id.layout_close);

        layout_choose.setBackgroundColor(getResources().getColor(
                R.color.icons));
        layout_photo.setBackgroundDrawable(getResources().getDrawable(
                R.color.icons));
        layout_close.setBackgroundColor(getResources().getColor(
                R.color.icons));


        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                layout_choose.setBackgroundColor(getResources().getColor(
                        R.color.icons));
                layout_photo.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                layout_close.setBackgroundColor(getResources().getColor(
                        R.color.icons));


                openCamera();
                avatorPop.dismiss();

            }
        });

        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                layout_photo.setBackgroundColor(getResources().getColor(
                        R.color.icons));
                layout_choose.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                layout_close.setBackgroundColor(getResources().getColor(
                        R.color.icons));
                openPic();
                avatorPop.dismiss();

            }
        });

        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_photo.setBackgroundColor(getResources().getColor(
                        R.color.icons));
                layout_close.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                layout_choose.setBackgroundColor(getResources().getColor(
                        R.color.icons));
                avatorPop.dismiss();
            }
        });


        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        avatorPop = new PopupWindow(view, mScreenWidth, 200);
        avatorPop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    avatorPop.dismiss();
                    return true;
                }
                return false;
            }
        });


        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        avatorPop.setTouchable(true);
        avatorPop.setFocusable(true);

//        avatorPop.setFocusable();
        avatorPop.setOutsideTouchable(true);
        avatorPop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从底部弹起
        avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
        avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
    }


}
