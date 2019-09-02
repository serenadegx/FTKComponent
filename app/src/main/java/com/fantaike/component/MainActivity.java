package com.fantaike.component;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fantaike.component.plug_in.FTKCheckCodeDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt1 = findViewById(R.id.bt_1);

        bt1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:

                FTKCheckCodeDialog dialog = new FTKCheckCodeDialog(this);
                dialog = new FTKCheckCodeDialog.Builder(this)
                        .setAuto(true)
                        .setSmsCodeDigits(FTKCheckCodeDialog.SIX_DIGITS)
                        .setCountDownTime(60 * 1000L)
                        .create();
                dialog.setListener(new FTKCheckCodeDialog.CheckCodeListener() {
                    @Override
                    public void onSendSmsCodeListener(View view, String phone) {
                        Toast.makeText(MainActivity.this, phone + "发送验证码", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onCheckSmsCodeListener(View view, String code) {
                        Log.i("mango", "code:" + code);
                        Toast.makeText(MainActivity.this, "校验验证码" + code, Toast.LENGTH_SHORT);
                    }
                });
                dialog.showSmSCodeDialog();
                break;
        }
    }
}
