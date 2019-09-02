package com.fantaike.component.plug_in;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantaike.component.R;

/**
 * @author guoxinrui
 * <p>
 * 1.实现基本的短信验证码发送，发送倒计时，及校验验证码
 * 2.设置手机号、倒计时时长、短信验证码位数、显示位置
 * 3.dialog的属性设置
 * 4.转场动效
 */
public class FTKCheckCodeDialog {

    public static final int SIX_DIGITS = 6;
    public static final int FOUR_DIGITS = 4;

    private static final long TIME = 120 * 1000L;
    private static final long INTERVAL = 1000L;


    private int padding = 0;
    private int gravity = Gravity.BOTTOM;
    private Context mContext;
    private AlertDialog dialog;
    private View root;
    private String mPhone;
    private boolean isAuto = true;
    private boolean cancelable;
    private int digits = SIX_DIGITS;

    MyCountDownTimer timer;
    private Button btGetCode;
    private long totalTime = TIME;

    public FTKCheckCodeDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void showSmSCodeDialog() {
        dialog.show();
        dialog.setContentView(root);
        Window window = dialog.getWindow();
        window.setGravity(gravity);
        window.getDecorView().setPadding(padding, padding, padding, padding);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(null);
        //AlertDialog中需要和edit相关动作
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        if (isAuto) {
            start();
            if (mListener != null)
                mListener.onSendSmsCodeListener(null, mPhone);
        }
    }


    private void initDialog() {
        root = LayoutInflater.from(mContext).inflate(R.layout.dialog_sms_code, null, false);
        TextView tvTitle = root.findViewById(R.id.tv_title);
        ImageView ivClose = root.findViewById(R.id.iv_close);
        final EditText etSmsCode = root.findViewById(R.id.et_sms_code);
        btGetCode = root.findViewById(R.id.bt_get_code);
        final Button btSure = root.findViewById(R.id.bt_sure);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();
        dialog.setCancelable(cancelable);
        tvTitle.setText(TextUtils.isEmpty(mPhone) ? "发送验证码" : "向" + mPhone + "发送验证码");
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btSure.setEnabled(!TextUtils.isEmpty(s) && s.toString().length() == 6);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                if (mListener != null)
                    mListener.onSendSmsCodeListener(v, mPhone);

            }
        });

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isFastClick()) {
                    String code = etSmsCode.getText().toString();
                    if (mListener != null)
                        mListener.onCheckSmsCodeListener(v, code);
                }
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stop();
            }
        });
    }

    /**
     * 倒计时开始
     */
    private void start() {
        btGetCode.setClickable(false);
        if (timer == null) {
            timer = new MyCountDownTimer(TIME, INTERVAL);
        }
        timer.start();

    }

    /**
     * 倒计时停止
     */
    private void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    private CheckCodeListener mListener;

    public void setListener(CheckCodeListener listener) {
        this.mListener = listener;
    }

    public interface CheckCodeListener {
        void onSendSmsCodeListener(View view, String phone);

        void onCheckSmsCodeListener(View view, String code);
    }

    private class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;

            if (time <= totalTime && dialog != null) {
                btGetCode.setText(time + "秒后重发");
            }
        }

        @Override
        public void onFinish() {
            if (dialog != null) {
                btGetCode.setText("重发验证码");
                btGetCode.setClickable(true);
            }
            stop();
        }
    }

    public static class Builder {
        private Context context;
        private String phone;
        private boolean isAuto;
        private int gravity;
        private boolean cancelable;
        private int padding;
        private long totalTime;
        private int digits;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setAuto(boolean isAuto) {
            this.isAuto = isAuto;
            return this;
        }

        public Builder setCountDownTime(long time) {
            this.totalTime = time;
            return this;
        }

        public Builder setSmsCodeDigits(int digits) {
            this.digits = digits;
            return this;
        }

        public Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public FTKCheckCodeDialog create() {
            FTKCheckCodeDialog dialog = new FTKCheckCodeDialog(this.context);
            dialog.mPhone = this.phone;
            dialog.isAuto = this.isAuto;
            dialog.totalTime = this.totalTime;
            dialog.padding = this.padding;
            dialog.gravity = this.gravity;
            dialog.cancelable = this.cancelable;
            dialog.initDialog();
            return dialog;
        }
    }

}
