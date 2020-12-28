public class TLSServer {
    private String tlsVersion;
    private String verschlüsselung;
    private final int PORT = 8080;
    private final int SIZE;

    public TLSServer(String version, String verschlüsselung, int SIZE) {
        this.tlsVersion = version;
        this.verschlüsselung = verschlüsselung;
        this.SIZE = SIZE;
    }
}
