package com.jinn.projecty.main.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 每日精选tab栏数据
 * Created by jinnlee on 2021/1/27.
 */
public class RecommandData implements Serializable{
    private ArrayList<Issue>issueList;
    private String nextPageUrl;
    private String nextPublishTime;
    private String newestIssueType;
    private String dialog;

    class Issue implements Serializable{
        private String releaseTime;
        private String type;
        private String date;
        private String publishTime;
        private ArrayList<Item>itemList;

        class Item implements Serializable{
             private String type;
             private Data data;
             private String trackingData;
             private String tag;
             private String id;
             private String adIndex;

             class Data implements Serializable {
                  private String dataType;
                  private String title;
                  private String description;


             }

            @Override
            public String toString() {
                return "Item{" +
                        "type='" + type + '\'' +
                        ", data=" + data +
                        ", trackingData='" + trackingData + '\'' +
                        ", tag='" + tag + '\'' +
                        ", id='" + id + '\'' +
                        ", adIndex='" + adIndex + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Issue{" +
                    "releaseTime='" + releaseTime + '\'' +
                    ", type='" + type + '\'' +
                    ", date='" + date + '\'' +
                    ", publishTime='" + publishTime + '\'' +
                    ", itemList=" + itemList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RecommandData{" +
                "issueList=" + issueList +
                ", nextPageUrl='" + nextPageUrl + '\'' +
                ", nextPublishTime='" + nextPublishTime + '\'' +
                ", newestIssueType='" + newestIssueType + '\'' +
                ", dialog='" + dialog + '\'' +
                '}';
    }
}
