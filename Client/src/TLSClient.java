public class TLSClient {
    private String tlsVersion;
    private String verschlüsselung;
    private final int PORT = 8080;
    private final int SIZE;

    public TLSClient(String version, String verschlüsselung, int SIZE) {
        this.tlsVersion = version;
        this.verschlüsselung = verschlüsselung;
        this.SIZE = SIZE;
    }
}
