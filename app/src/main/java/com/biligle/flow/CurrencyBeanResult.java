package com.biligle.flow;

import java.io.PushbackInputStream;
import java.io.Serializable;
import java.util.List;

/** 币种实体
 *  @Author wangguoli
 *  {"head":{CODE:"0"},"data":{
 *   "list": [{
 *     "data": [{
 *       "CURRENCY_NAME": "人民币",
 *       "ID": 49,
 *       "CURRENCY_CODE": "CNY",
 *       "CURRENCY_INDEX": 0
 *     }, {
 *       "CURRENCY_NAME": "港币",
 *       "ID": 53,
 *       "CURRENCY_CODE": "HKD",
 *       "CURRENCY_INDEX": 0
 *     }, {
 *       "CURRENCY_NAME": "人民币",
 *       "ID": 57,
 *       "CURRENCY_CODE": "CNY",
 *       "CURRENCY_INDEX": 0
 *     }, {
 *       "CURRENCY_NAME": "testtttt",
 *       "ID": 64,
 *       "CURRENCY_CODE": "testtest",
 *       "CURRENCY_INDEX": 0
 *     }],
 *     "tag": "热门币种"
 *   }, {
 *     "data": [{
 *       "CURRENCY_NAME": "人民币",
 *       "ID": 49,
 *       "CURRENCY_CODE": "CNY"
 *     }, {
 *       "CURRENCY_NAME": "港币",
 *       "ID": 53,
 *       "CURRENCY_CODE": "HKD"
 *     }, {
 *       "CURRENCY_NAME": "人民币",
 *       "ID": 57,
 *       "CURRENCY_CODE": "CNY"
 *     }],
 *     "tag": "亚洲"
 *   }, {
 *     "data": [{
 *       "CURRENCY_NAME": "文莱元",
 *       "ID": 45,
 *       "CURRENCY_CODE": "BND"
 *     }],
 *     "tag": "非洲"
 *   }, {
 *     "data": [{
 *       "CURRENCY_NAME": "testtttt",
 *       "ID": 64,
 *       "CURRENCY_CODE": "testtest"
 *     }],
 *     "tag": "大洋洲"
 *   }],
 *   "TYPE": "GET_PRD_RANK_HOME_PAGE_CURRENCY",
 *   "home": [{
 *     "CURRENCY_NAME": "人民币",
 *     "ID": 49,
 *     "CURRENCY_CODE": "CNY",
 *     "CURRENCY_INDEX": 0
 *   }, {
 *     "CURRENCY_NAME": "港币",
 *     "ID": 53,
 *     "CURRENCY_CODE": "HKD",
 *     "CURRENCY_INDEX": 0
 *   }, {
 *     "CURRENCY_NAME": "人民币",
 *     "ID": 57,
 *     "CURRENCY_CODE": "CNY",
 *     "CURRENCY_INDEX": 0
 *   }, {
 *     "CURRENCY_NAME": "testtttt",
 *     "ID": 64,
 *     "CURRENCY_CODE": "testtest",
 *     "CURRENCY_INDEX": 0
 *   }]
 * }}
 */

public class CurrencyBeanResult implements Serializable {
    public HeadResult head;
    public CurrencyBean data;
    public static class CurrencyBean {
        public List<CurrencyDetails> list;//所有分类：热门、亚洲、非洲......
        public String TYPE;//GET_PRD_RANK_HOME_PAGE_CURRENCY
        public List<CurrencyName> home;
    }

    /**
     * 币种分类详情
     */
    public static class CurrencyDetails {
        public List<CurrencyName> data;//热门币种
        public String tag;//热门
    }

    public static class CurrencyName {//币种详情，需要返回给首页，便于更新数据
        public String CURRENCY_NAME;//人民币
        public String CURRENCY_CODE;//CNY
        public String ID;
        public boolean isChecked;//false:未选中；true:选中
        public boolean isMiddle;//false:不是中间项
        public boolean isGone;//true:line消失
    }
}
