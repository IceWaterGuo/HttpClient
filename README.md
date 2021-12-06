# HttpClient使用文档

### httpClient是基于Retrofit2.0+RxJava3.0+MVP封装的一个网络请求库，多个BaseUrl无缝切换，使用极其简单


#### 1.请求参数(请求参数支持3种方式)

* 单独添加

```
HttpClient.create(this)
                .requestParams("appKey", "")
                .requestParams("value", "")
	
```
* HashMap的形式

```
HashMap<String, Object> params = new HashMap<>(); params.put("appKey","");
params.put("category","要闻");
params.put("updateTime","2018-06-21 00:00:00");
        HttpClient.create(this)
                .params(params)

```

* 直接传对象

```
public class RequestBean {
    public String appKey;
    public String area;

    public RequestBean(String appKey, String area) {
        this.appKey = appKey;
        this.area = area;
    }
}

RequestBean requestBean = new RequestBean("b5baa6d5add44cc3a6f9bd7596953669", "苏州");
        HttpClient.create(this)
                .params(requestBean)
```


#### 2.请求方法
* get

```
如果不指定请求方法的话，get方法为默认请求方法
示例：
HttpClient.create(this)
                .requestParams("appKey", "")
                .requestParams("category", "要闻")
                .requestParams("updateTime", "2018-06-21 00:00:00")
                .heads("a", "aa")
                .showLodding(true)
                .url("api/news/list")
                .builder()
```
* post方法

```
HashMap<String, Object> params = new HashMap<>();
        params.put("appKey", "");
        params.put("area", "苏州");
        HttpClient.create(this)
                .params(params)
                .showLodding(true)
                .url("api/weather/area")
                .heads("a", "aa")
                .heads("b", "bb")
                .post()
                .builder()
```

* 上传Json数据(postJson)

```
RequestBean requestBean = new RequestBean("123", "苏州");
        HttpClient.create(this)
                .params(requestBean)
                .postJson()
                .url("api/weather/area")
                .showLodding(true)
                .builder()
注：params可以传HashMap                
```
* 下载(download)

```
HttpClient.create(this)
                .download()
                .url("software/mobile/MockMobile_v3.4.0.0.apk")
                .baseUrl("http://cdn.mockplus.cn/")
                .builder()
```

* 上传(upload)

```
HttpClient.create(this)
                .url("")
                .upload(new File(""))
                .builder()
                .request(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
```

#### 3.请求参数Callback
* 用于普通请求的HttpCallback
* 用于文件下载的FileCallback

### 4.使用说明

* 这一版没有做全局配置，所以baseUrl需要手动在Application中初始化网络框架时进行指定

```
    /**
     * 初始化网络框架
     */
    private void initHttpClient() {
        HttpConfig.create()
                .baseUrl(BuildConfig.BASE_URL)
                .builder();
    }
```

* 网络请求时不指定baseUrl，默认使用初始化网络框架时指定的baseUrl；需要切换baseUrl时，只需要网络请求时指定baseUrl即可
* 每个请求的方法必须传url,不然就报错

#### 5.使用例子

```
HttpClient.create(this)
                .requestParams("appKey", "")
                .requestParams("category", "")
                .requestParams("updateTime", "")
                .showLodding(true)
                .url("api/news/list")
                .builder()
                .request(new HttpCallback<BaseBean<NewsBean>>() {
                    @Override
                    public void onSuccess(BaseBean<NewsBean> newsBeanBaseBean) {
                        mView.getDataSuccess(newsBeanBaseBean.toString());
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });

```

```                
文件下载
                HttpClient.create(this)
                .download()
                .url("software/mobile/MockMobile_v3.4.0.0.apk")
                .baseUrl("http://cdn.mockplus.cn/")
                .builder()
                .request(new FileCallback() {
                    @Override
                    public void onStart() {
                        Log.e("399", "onStart");
                    }

                    @Override
                    public void onDownloadComplete() {
                        Log.e("399", "onDownloadComplete" + " thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("399", "onError" + " thread: " + Thread.currentThread().getName() + "  msg:" + msg);
                    }

                    @Override
                    public void onDownloading(Progress progress) {
                        mView.onProgress(progress);
                    }
                });
```
