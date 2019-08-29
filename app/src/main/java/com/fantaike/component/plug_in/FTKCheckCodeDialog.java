package com.fantaike.component.plug_in;

/**
 * @author guoxinrui
 * <p>
 * 1.实现基本的短信验证码发送，发送倒计时，及校验验证码
 * 2.设置手机号、倒计时时长、短信验证码位数、显示位置
 * 3.dialog的属性设置
 * 4.转场动效
 */
public class FTKCheckCodeDialog {

    /**
     *
     */
    public void create(){

    }

//    private void showSmSCodeDialog() {
//        dialog.show();
//
//        dialog.setContentView(dialogBinding.getRoot());
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        dialog.getWindow().setBackgroundDrawable(null);
//        //AlertDialog中需要和edit相关动作
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
////        dialogBinding.btGetCode.setClickable(false);
////        startTimer(dialogBinding.btGetCode);
////        getSmsCode();
//    }



//    private void initDialog() {
//        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_sms_code, null, false);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        dialog = builder.create();
//
//        dialogBinding.etSmsCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                dialogBinding.btSure.setEnabled(!TextUtils.isEmpty(s) && s.toString().length() == 6);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        dialogBinding.ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//
//        dialogBinding.btGetCode.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                startTimer(dialogBinding.btGetCode);
//                // TODO: 2018/11/15 获取短信验证码
//                getSmsCode();
//            }
//        });
//
//        dialogBinding.btSure.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                // TODO: 2018/11/15 验证短信验证码
//                checkCode();
//
//            }
//        });
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
////                cancelTimer();
//            }
//        });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void checkCode() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("access_token", token);
//        if (!TextUtils.isEmpty(flowNo)) {
//            params.put("flowNo", flowNo);
//        }
//        params.put("checkCode", dialogBinding.etSmsCode.getText().toString());
//        params.put("cnsmpApplSeq", odd);
//        okHttp.stringOkHttp(context, R.id.bt_sure, URLConstant.VERIFY_CODE_CAR_INSURANCE, params
//                , SignPactModel.this);
//    }




}
