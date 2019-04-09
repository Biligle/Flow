package com.biligle.flow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private CurrencyPopupWindow popupWindow;
    private MainCurrencyPopupWindow mainCurrencyPopupWindow;
    private Button currency;
    //测试json数据
    private String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"阿拉伯元\",\"ID\":44,\"CURRENCY_CODE\":\"ALB\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0}]}}";
//    private String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0}]}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);
        currency = (Button) findViewById(R.id.bt_currency);
        mainCurrencyPopupWindow = new MainCurrencyPopupWindow(this, currency.getText().toString());
        popupWindow = new CurrencyPopupWindow(this, mainCurrencyPopupWindow);
        currencyListener();
    }

    /**
     * 币种监听
     */
    private void currencyListener() {
        if (popupWindow != null) {
            popupWindow.getAdapter().setListener(new CurrencyListAdapter.OnTagClickListener() {
                @Override
                public void getTagData(CurrencyBeanResult.CurrencyName data) {
                    Toast.makeText(MainActivity.this,
                            "币种：" + data.CURRENCY_NAME +
                                    "\n币种简称：" + data.CURRENCY_CODE +
                                    "\n币种ID：" + data.ID,
                            Toast.LENGTH_SHORT).show();
                    currency.setText(data.CURRENCY_CODE);
                }
            });
        }
        if (mainCurrencyPopupWindow != null) {
            mainCurrencyPopupWindow.getAdapter().setListener(new CurrencyListMainAdapter.OnTagClickListener() {
                @Override
                public void getTagData(CurrencyBeanResult.CurrencyName data) {
                    Toast.makeText(MainActivity.this,
                            "币种：" + data.CURRENCY_NAME +
                                    "\n币种简称：" + data.CURRENCY_CODE +
                                    "\n币种ID：" + data.ID,
                            Toast.LENGTH_SHORT).show();
                    currency.setText(data.CURRENCY_CODE);
                }
            });
        }

    }

    public void pop(View view) {
        popupWindow.show(currency.getText().toString());
    }

    public void pop2(View view) {
        mainCurrencyPopupWindow.show(currency.getText().toString(), currency);
    }
}
