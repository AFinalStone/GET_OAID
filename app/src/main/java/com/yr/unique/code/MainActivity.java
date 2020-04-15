package com.yr.unique.code;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yr.unique.code.utils.AppIdsUpdater;
import com.yr.unique.code.utils.OAIDHelper;

public class MainActivity extends AppCompatActivity {

    TextView mTvOAID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvOAID = findViewById(R.id.tv_OAID);
        findViewById(R.id.btn_get_OAID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OAIDHelper helper = new OAIDHelper(new AppIdsUpdater() {
                    @Override
                    public void OnIdsAvalid(@NonNull final String ids) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvOAID.setText("成功获取OAID，具体值为:\n" + ids);
                            }
                        });
                    }
                });
                helper.getOAid(MainActivity.this);
            }
        });

    }
}
