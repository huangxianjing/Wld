package com.wld.net;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wld.net.utils.Utils;
import com.wld.net.view.ptrFrameLayout.PtrFrameLayout;
import com.wld.net.view.ptrFrameLayout.PtrHandler;
import com.wld.net.view.ptrFrameLayout.header.RentalsSunHeaderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final TextView mText = (TextView) findViewById(R.id.main_text);
        final PtrFrameLayout frameLayout = (PtrFrameLayout) findViewById(R.id.main_ptrFrameLayout);
        final RentalsSunHeaderView headerView = new RentalsSunHeaderView(this);
        headerView.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        headerView.setPadding(0, Utils.dip2px(this, 15), 0, Utils.dip2px(this, 10));
        headerView.setUp(frameLayout);
        frameLayout.setLoadingMinTime(1000);
        frameLayout.setDurationToCloseHeader(1500);
        frameLayout.setHeaderView(headerView);
        frameLayout.addPtrUIHandler(headerView);
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.autoRefresh(true);
            }
        }, 100);
        frameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                long delay = 1500;
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, delay);
                mText.setText("测试完啦");
            }
        });
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }


}
