public class TLSClient {
    private String tlsVersion;
    private String verschlüsselung;
    private final int PORT = 8080;
    private final int SIZE;
    private long time = 0;

    public TLSClient(String version, String verschlüsselung, int SIZE) {
        this.tlsVersion = version;
        this.verschlüsselung = verschlüsselung;
        this.SIZE = SIZE;
    }

    public void startTimer() {
        this.time = System.currentTimeMillis();
    }

    public void stopTimer() {
        System.out.println(time - System.currentTimeMillis());
        this.time = 0;
    }
}
