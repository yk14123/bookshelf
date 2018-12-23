package com.chinafocus.bookshelf.model.base.network;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

class HttpsUtils {


    static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    static SSLContext getSLLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    static SSLSocketFactory setCertificatesFromFile(Context context, String fileName) {

        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            return setCertificatesFromStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    static SSLSocketFactory setCertificatesFromStream(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            //使用默认证书
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //去掉系统默认证书
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                //设置自己的证书
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            //通过信任管理器获取一个默认的算法
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            //算法工厂创建
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(defaultAlgorithm);
            //算法工厂初始化证书
            trustManagerFactory.init(keyStore);

            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );

            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
