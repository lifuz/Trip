package com.lifuz.trip.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.lifuz.trip.R;

/**
 *
 * 提醒对话框
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:54
 */
public class CustomDialog extends ProgressDialog {

    TextView loadText;

    private String message;

    public CustomDialog(Context context)
    {
        super(context);
    }

    public CustomDialog(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.load_dialog);

        loadText = (TextView) findViewById(R.id.tv_load_dialog);

        if (null != message && !"".equals(message)) {

            loadText.setText(message);
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }

    public void setMessage(String message) {
        this.message = message;

        if (loadText != null) {
            if (null != message && !"".equals(message)) {

                loadText.setText(message);
            }
        }
    }
}
