package com.biligle.flow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

/**
 * @Author wangguoli
 */
public class CurrencyPopupWindow extends PopupWindow {
    private Activity mActivity;
    private PopupWindow mPop;
    private MainCurrencyPopupWindow mainCurrencyPopupWindow;
    private View rootView;
    private List<CurrencyBeanResult.CurrencyDetails> datas;
    private CurrencyListAdapter adapter;

    public CurrencyPopupWindow(Activity mActivity, MainCurrencyPopupWindow mainCurrencyPopupWindow) {
        this.mActivity = mActivity;
        this.mainCurrencyPopupWindow = mainCurrencyPopupWindow;
        if (mPop == null) {
            init();
        }
    }

    private void init() {
        rootView = View.inflate(mActivity,
                R.layout.view_currency_pop, null);
        mPop = new PopupWindow(rootView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPop.setAnimationStyle(R.style.AnimBottomIn);
        mPop.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mPop.setTouchable(true);
        mPop.setOnDismissListener(new poponDismissListener());
        ListView listView = rootView.findViewById(R.id.currency_list);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        initData();
        adapter = new CurrencyListAdapter(mPop,mActivity,datas);
        listView.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取全部币种
        String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"人民币人民币最牛\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币人民币最牛\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"阿拉伯元\",\"ID\":44,\"CURRENCY_CODE\":\"ALB\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0}]}}";
//        String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"阿达加斯哈\",\"ID\":557,\"CURRENCY_CODE\":\"SEFT\"},{\"CURRENCY_NAME\":\"安联达的奥数\",\"ID\":587,\"CURRENCY_CODE\":\"ALAIS\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"阿拉伯元\",\"ID\":44,\"CURRENCY_CODE\":\"ALB\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0}]}}";
        CurrencyBeanResult currencyBeanResult = new Gson().fromJson(json, CurrencyBeanResult.class);
        if (currencyBeanResult != null && currencyBeanResult.data != null) {
            datas = currencyBeanResult.data.list;
        }
    }

    /**
     * 获取Adapter
     */
    public CurrencyListAdapter getAdapter() {
        return adapter;
    }

    public void show(String code){
        if(mPop != null && !mPop.isShowing()) {
            defaultCheckedState(code);
            backgroundAlpha(0.5f);
            mPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
//            updateDatas(code);
        }
    }


    /**
     * 获取最新币种数据
     */
    private void updateDatas(String code) {
        //以下是模拟访问网络
        final String str = code;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                datas.clear();
                String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"阿达加斯哈\",\"ID\":557,\"CURRENCY_CODE\":\"SEFT\"},{\"CURRENCY_NAME\":\"安联达的奥数\",\"ID\":587,\"CURRENCY_CODE\":\"ALAIS\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"阿拉伯元\",\"ID\":44,\"CURRENCY_CODE\":\"ALB\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0}]}}";
                CurrencyBeanResult currencyBeanResult = new Gson().fromJson(json,CurrencyBeanResult.class);
                if (currencyBeanResult != null && currencyBeanResult.data != null) {
                    datas.addAll(currencyBeanResult.data.list);
                    defaultCheckedState(str);
                }
            }
        }, 2000);
    }

    /**
     * 默认选中币种
     * @param code 币种简称
     */
    private void defaultCheckedState(String code) {
        for (CurrencyBeanResult.CurrencyDetails details : datas) {
            for (int index = 0; index < details.data.size(); index++) {
                details.data.get(index).isChecked = code.equals(details.data.get(index).CURRENCY_CODE);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 背景透明度1.0
     */
    private class poponDismissListener implements OnDismissListener {
        @Override
        public void onDismiss() {
            mainCurrencyPopupWindow.isAlpha = true;
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {

            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        mActivity.getWindow().setAttributes(lp);
    }
}
