import java.io.*;
import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.*;

public class TLSServer {
    private String tlsVersion;
    private int PORT = 8082;
    private boolean isServerDone = false;

    public static void main(String[] args) {
        TLSServer server = new TLSServer("TLSv1.2", "", 0);
        server.run();
    }


    TLSServer(String version, String encryption, int SIZE) {
        this.tlsVersion = version;
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext() {
        try {
            InputStream stream = TLSServer.class.getResourceAsStream("/sslserverkeys");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(stream, "7x*;^C(HU~5}@P?h".toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "7x*;^C(HU~5}@P?h".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance(tlsVersion);
            sslContext.init(km, tm, null);
            sslContext.getClientSessionContext().setSessionCacheSize(1);

            return sslContext;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    public void run() {
        SSLContext sslContext = this.createSSLContext();

        try {
            // Create server socket factory
            assert sslContext != null;
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.PORT);
            sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());

            System.out.println("SSL server started");
            while (!isServerDone) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                sslSocket.setEnabledProtocols(new String[]{tlsVersion});
                //sslSocket.setNeedClientAuth(true);

                // Start the server thread
                SSLSession sslSession = sslSocket.getSession();

                sslSocket.startHandshake();
                //sslSession.invalidate();

                System.out.println("SSLSession :"+ Arrays.toString(sslSocket.getSession().getId()));
                System.out.println("\tProtocol : " + sslSession.getProtocol());
                System.out.println("\tCipher suite : " + sslSession.getCipherSuite());

                sslSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}