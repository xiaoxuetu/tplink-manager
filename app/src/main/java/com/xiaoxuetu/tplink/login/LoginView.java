package com.xiaoxuetu.tplink.login;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.main.DeviceActivity;

/**
 * Created by kevin on 2017/3/28.
 */

public class LoginView extends FrameLayout implements LoginContract.View{

    LoginContract.Presenter mPresenter;

    public LoginView(Context context) {
        super(context);
        init();
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.login_view, this);

        findViewById(R.id.login_account_password_login_button)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText hostEditText = (EditText) findViewById(R.id.login_account_host_editor);
                        EditText passwordEditText = (EditText) findViewById(R.id.login_account_password_password_editor);
                        String ip = hostEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        mPresenter.login(ip, password);
                    }
                });
    }

    @Override
    public void showFailureMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnLineDevices() {
        Intent intent = new Intent(getContext(), DeviceActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
