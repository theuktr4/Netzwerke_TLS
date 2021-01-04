import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class TLSClient {
    private String tlsVersion;
    private String verschlüsselung;
    private final int PORT = 8082;
    private final int SIZE;
    private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String IP = "localhost";

    public static void main(String[] args) {
        TLSClient client = new TLSClient("TLSv1.2","1",25);

    }
    public TLSClient(String version, String verschlüsselung, int SIZE) {
        this.tlsVersion = version;
        this.verschlüsselung = verschlüsselung;
        this.SIZE = SIZE;


        try {
            long startTime = System.currentTimeMillis();
            InputStream stream = this.getClass().getResourceAsStream("/sslclienttrust");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(trustStore, "]3!z2Tb?@EHu%d}Q".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            char[] trustStorePassword = "]3!z2Tb?@EHu%d}Q".toCharArray();
            trustStore.load(stream, trustStorePassword);
            SSLContext context = SSLContext.getInstance(tlsVersion);
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //TrustManagerFactory factory = TrustManagerFactory.getInstance(verschlüsselung);

            factory.init(trustStore);
            TrustManager[] managers = factory.getTrustManagers();
            context.init(km, managers, null);
            SSLContext.setDefault(context);
            socket = (SSLSocket) context.getSocketFactory().createSocket("localhost", 8082);
            socket.setEnabledProtocols(new String[]{tlsVersion});
            socket.startHandshake();
            System.out.println("Successfully connected");
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            System.out.println("Elapsed time in milli seconds: " + timeElapsed );

        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | KeyStoreException | CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}