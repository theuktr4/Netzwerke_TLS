import java.io.*;
import java.security.KeyStore;

import javax.net.ssl.*;

public class TLSServer {
    private int port = 3122;
    private boolean isServerDone = false;

    public static void main(String[] args){
        TLSServer server = new TLSServer();
        server.run();
    }

    TLSServer(){
    }

    TLSServer(String version, String encryption, int SIZE){
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
        try{
            InputStream stream = TLSServer.class.getResourceAsStream("/sslserverkeys");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(stream,"7x*;^C(HU~5}@P?h".toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "7x*;^C(HU~5}@P?h".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(km,  tm, null);

            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    public void run(){
        SSLContext sslContext = this.createSSLContext();

        try{
            // Create server socket factory
            assert sslContext != null;
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);
            sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());


            System.out.println("SSL server started");
            while(!isServerDone){
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

                // Start the server thread
                sslSocket.startHandshake();
                SSLSession sslSession = sslSocket.getSession();
                System.out.println("SSLSession :");
                System.out.println("\tProtocol : "+sslSession.getProtocol());
                System.out.println("\tCipher suite : "+sslSession.getCipherSuite());

            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }




}