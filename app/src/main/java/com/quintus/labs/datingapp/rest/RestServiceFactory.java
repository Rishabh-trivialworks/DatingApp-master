package com.quintus.labs.datingapp.rest;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.rest.services.ApiService;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Narendra on 10/20/2016.
 */

public class RestServiceFactory {

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit;

    private static ApiService apiService;
    private static X509TrustManager x509TrustManager;

    private static <S> S createService(Class<S> serviceClass) {
        if (apiService == null) {

            if (MyApplication.RETROFIT_SHOW_LOG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);
            }

            httpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    boolean value = true;
                    return value;
                }
            });

            httpClient.readTimeout(2, TimeUnit.MINUTES);
            httpClient.connectTimeout(2, TimeUnit.MINUTES);
            httpClient.writeTimeout(10, TimeUnit.MINUTES);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String auth;
                    String lang = Locale.getDefault().getDisplayLanguage();
                    if (!lang.equalsIgnoreCase("English")) {
                        lang = "ar";
                    } else {
                        lang = "en";
                    }

                    if (TempStorage.getAuthToken() ==null || TempStorage.getAuthToken().equalsIgnoreCase("")) {
                        auth = "no-token";
                    } else {
                        auth = TempStorage.getAuthToken();

                        LogUtils.debug("okhttp " + AppConstants.ApiParamKey.USTADJI_AUTH_TOKEN + " : " + auth);
                    }
                    TimeZone tz = TimeZone.getDefault();
                    Request request = chain.request().newBuilder()
                            .addHeader(AppConstants.ApiParamKey.USTADJI_AUTH_TOKEN, auth)
                            .addHeader(AppConstants.ApiParamKey.APP_LANGUAGE, lang)
                            .addHeader(AppConstants.ApiParamKey.APP_TIMEZONE,tz.getID())
                            .addHeader(AppConstants.ApiParamKey.APP_VERSION, "").build();

                    if (MyApplication.API_DEBUG) {
                        HttpUrl url = request.url().newBuilder().addQueryParameter(AppConstants.ApiParamKey.DEBUG, MyApplication.API_DEBUG + "").build();
                        request = request.newBuilder().url(url).build();
                    }
                    if (request.url().toString().contains("ustadji.co")) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                httpClient.sslSocketFactory(getSSLConfig(AppContext.getInstance().getContext()).getSocketFactory(), x509TrustManager);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return chain.proceed(request);
                }
            });

            String baseUrl = "";

            switch (MyApplication.apiMode) {
                case TESTING_BETA:
                    baseUrl = AppConstants.Url.BASE_SERVICE_BETA;
                    break;
                case TESTING_ALPHA:
                    baseUrl = AppConstants.Url.BASE_SERVICE_ALPHA;
                    break;
                case LIVE:
                    baseUrl = AppConstants.Url.BASE_SERVICE_LIVE;
                    break;
                default:
            }

            Retrofit.Builder builder =
                    new Retrofit.Builder()

                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.client(httpClient.build()).build();
            apiService = (ApiService) retrofit.create(serviceClass);

        }
        return (S) apiService;
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static ApiService createService() {
        return createService(ApiService.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca = null;
        // I'm using Java7. If you used Java6 close it manually with finally.
        try (InputStream cert = context.getResources().openRawResource(R.raw.ustadji)) {
            ca = cf.generateCertificate(cert);
        }

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        x509TrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        return sslContext;
    }
}
