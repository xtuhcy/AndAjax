package com.geccocrawler.andajax;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 必须在UI线程中生成AndAjax实例
 *
 * Created by huchengyi on 2016/4/22.
 */
public class AndAjax {

    private OkHttpClient client;

    private ExecutorService executorService;

    private Handler uiHandler;

    private AndAjax(int thread) {
        uiHandler = new Handler();
        client = new OkHttpClient();
        executorService = Executors.newFixedThreadPool(thread);
    }

    public static AndAjax thread(int thread) {
        return new AndAjax(thread);
    }

    public void ajax(final String url, final Callback<String> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    final String jsonString = response.body().string();
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(jsonString);
                        }
                    });
                } catch (final Exception ex) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(-1, ex);
                        }
                    });
                }
            }
        });
    }

    public void image(final String url, final Callback<Bitmap> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    byte[] bitmapBytes = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(bitmap);
                        }
                    });
                } catch (final Exception ex) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(-1, ex);
                        }
                    });
                }
            }
        });
    }

    public interface Callback<T> {

        void onSuccess(T o);

        void onError(int state, Exception ex);

    }

    public void close() {
        executorService.shutdown();
    }
}
