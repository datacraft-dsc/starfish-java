package sg.dex.starfish.connection_check;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class ConnectionChecker {

    private String uri;

    public ConnectionChecker(String uri) {
        this.uri = uri;
    }

    public boolean connect() {

        return isServerReachable();
    }

    /**
     * Checks HTTP URL and returns <code>true</code> a connection can be established
     * to the corresponding host and port
     *
     * @return boolean if endpoint is up within the timeout
     */
    public boolean isServerReachable() {

        try {
            URL url = new URL(uri);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(url.getHost(), url.getPort()), 1000);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
