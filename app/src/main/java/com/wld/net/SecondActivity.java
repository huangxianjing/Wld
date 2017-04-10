package com.wld.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wld.net.utils.Utils;
import com.wld.net.view.MainHeadView;
import com.wld.net.view.RotateLayout;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class SecondActivity extends Activity implements MainHeadView.MoveListener, View.OnClickListener {
    private MainHeadView mainHeadView;
    private RotateLayout rotateLayout;
    private ImageView mess;
    private ImageView scan;
    private ImageView task;
    private ImageView discover_assistant;
    private ImageView discover_assistant_pop_bg;
    private int headHeight;
    private int messAndScanTop;
    private int taskTop;
    private int dp8;
    private int dp16;
    private boolean assistantIsShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        mainHeadView.setMoveListener(this);
        discover_assistant.setOnClickListener(this);
    }

    private void initData() {
        headHeight = Utils.dip2px(this, 120);
        messAndScanTop = Utils.dip2px(this, 8);
        taskTop = Utils.dip2px(this, 52);
        dp8 = Utils.dip2px(this, 8);
        dp16 = Utils.dip2px(this, 16);
    }

    private void initView() {
        mainHeadView = (MainHeadView) findViewById(R.id.main_head_view);
        rotateLayout = (RotateLayout) findViewById(R.id.second_rotate);
        mess = (ImageView) findViewById(R.id.discover_icon_mess);
        scan = (ImageView) findViewById(R.id.discover_icon_scan);
        task = (ImageView) findViewById(R.id.discover_icon_task);
        discover_assistant = (ImageView) findViewById(R.id.discover_assistant);
        discover_assistant_pop_bg = (ImageView) findViewById(R.id.discover_assistant_pop_bg);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void moveListener(float move) {
        rotateLayout.scrollTo(0, -(int) move);
        mainHeadView.scrollTo(0, -(int) move);
        int messAndScanMove = (int) (move - messAndScanTop);
        if (messAndScanMove > 0) {
            ViewGroup.MarginLayoutParams messLayoutParams = (ViewGroup.MarginLayoutParams) mess.getLayoutParams();
            messLayoutParams.rightMargin = dp8 - messAndScanMove;
            mess.requestLayout();
            ViewGroup.MarginLayoutParams scanLayoutParams = (ViewGroup.MarginLayoutParams) scan.getLayoutParams();
            scanLayoutParams.leftMargin = messAndScanTop - messAndScanMove;
            scan.requestLayout();
        }
        int taskMove = (int) (move - taskTop);
        if (taskMove > 0) {
            ViewGroup.MarginLayoutParams taskLayoutParams = (ViewGroup.MarginLayoutParams) task.getLayoutParams();
            taskLayoutParams.rightMargin = dp8 - taskMove;
            task.requestLayout();
        }
        ViewGroup.MarginLayoutParams assistantLayoutParams = (ViewGroup.MarginLayoutParams) discover_assistant.getLayoutParams();
        assistantLayoutParams.bottomMargin = (int) (dp16 - move);
        discover_assistant.requestLayout();
        ViewGroup.MarginLayoutParams assistantPopLp = (ViewGroup.MarginLayoutParams) discover_assistant_pop_bg.getLayoutParams();
        assistantPopLp.bottomMargin = (int) (dp16 - move);
        discover_assistant_pop_bg.requestLayout();
    }

    @Override
    public void menuReset() {
        ViewGroup.MarginLayoutParams messLayoutParams = (ViewGroup.MarginLayoutParams) mess.getLayoutParams();
        messLayoutParams.rightMargin = dp8;
        mess.requestLayout();
        ViewGroup.MarginLayoutParams scanLayoutParams = (ViewGroup.MarginLayoutParams) scan.getLayoutParams();
        scanLayoutParams.leftMargin = messAndScanTop;
        scan.requestLayout();
        ViewGroup.MarginLayoutParams taskLayoutParams = (ViewGroup.MarginLayoutParams) task.getLayoutParams();
        taskLayoutParams.rightMargin = dp8;
        task.requestLayout();
        ViewGroup.MarginLayoutParams assistantLayoutParams = (ViewGroup.MarginLayoutParams) discover_assistant.getLayoutParams();
        assistantLayoutParams.bottomMargin = dp16;
        discover_assistant.requestLayout();
        ViewGroup.MarginLayoutParams assistantPopLp = (ViewGroup.MarginLayoutParams) discover_assistant_pop_bg.getLayoutParams();
        assistantPopLp.bottomMargin = dp16;
        discover_assistant_pop_bg.requestLayout();
    }


    @Override
    public void skip() {
//        Intent intent = new Intent(this, ThreeActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.discover_assistant:
                if (assistantIsShow) {
                    discover_assistant_pop_bg.setVisibility(View.GONE);
                    assistantIsShow = false;
                } else {
                    discover_assistant_pop_bg.setVisibility(View.VISIBLE);
                    assistantIsShow = true;
                }
                break;
        }
    }
}
