import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class TLSClient {
    private String tlsVersion;
    private String verschlüsselung;
    private final int PORT = 8080;
    private final int SIZE;
    private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public static void main(String[] args) {
        TLSClient client = new TLSClient("test","1",25);
    }
    public TLSClient(String version, String verschlüsselung, int SIZE) {
        this.tlsVersion = version;
        this.verschlüsselung = verschlüsselung;
        this.SIZE = SIZE;


        try {

            InputStream stream = this.getClass().getResourceAsStream("/sslclienttrust");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] trustStorePassword = "]3!z2Tb?@EHu%d}Q".toCharArray();
            trustStore.load(stream, trustStorePassword);
            SSLContext context = SSLContext.getInstance("TLSv1.3");
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(trustStore);
            TrustManager[] managers = factory.getTrustManagers();
            context.init(null, managers, null);
            SSLContext.setDefault(context);
            socket = (SSLSocket) context.getSocketFactory().createSocket("localhost", 8082);
            try {
                this.out = new ObjectOutputStream(this.socket.getOutputStream());
            } catch (IOException var4) {
                var4.printStackTrace();

                try {
                    this.out.close();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }

        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | KeyStoreException | CertificateException e) {
            e.printStackTrace();
        }
    }
}