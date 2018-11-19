package com.chinafocus.bookshelf.utils;

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

public class HttpsUtils {


    public static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public static SSLContext getSLLContext() {
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

    public static SSLSocketFactory setCertificatesFromFile(Context context, String fileName) {

        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            return setCertificates(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static SSLSocketFactory setCertificates(InputStream... certificates) {
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

//            //双向认证：客户端生成bks证书，然后加载bks后，给服务端认证
//            //服务器端需要验证的客户端证书，其实就是客户端的keystore
//            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
//            //加载本地的bks流
//            clientKeyStore.load(new FileInputStream(new File("xx")),"1234".toCharArray());
//            //生成KeyManagerFactory
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
//            //通过初始化KeyManagerFactory，来配置客户端
//            keyManagerFactory.init(clientKeyStore,"1234".toCharArray());
//            keyManagerFactory.getKeyManagers();
//            /**
//             * 第一个参数keyManagerFactory：当开启双向认证，需要加载客户端本地的Bks的时候，使用。如果是单向认证传null即可
//             * 第二个参数TrustManager，管理单向认证，信任服务端的策略，这个必须要配置！
//             * 第三个参数SecureRandom 随机安全码，安全要求不高，可以传null
//             */
//            sslContext.init(keyManagerFactory.getKeyManagers(),
//                            trustManagerFactory.getTrustManagers(),
//                            new SecureRandom()
//                            );

            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );

            return sslContext.getSocketFactory();
//            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
