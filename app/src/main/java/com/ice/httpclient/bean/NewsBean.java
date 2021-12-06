package com.ice.httpclient.bean;

import java.util.List;

/**
 * Desc:
 * Created by icewater on 2021/12/06.
 */
public class NewsBean {
    public int page;
    public List<NewsListBean> newsList;

    @Override
    public String toString() {
        return "NewsBean{" +
                "page=" + page +
                ", newsList=" + newsList +
                '}';
    }

    public static class NewsListBean {

        public String category;
        public String publishTime;
        public int newsId;
        public String title;

        @Override
        public String toString() {
            return "NewsListBean{" +
                    "category='" + category + '\'' +
                    ", publishTime='" + publishTime + '\'' +
                    ", newsId=" + newsId +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
