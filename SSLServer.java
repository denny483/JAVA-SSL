import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLServer {
  private static SSLSocket s;
  public static void testConnectionTo(String aURL) throws Exception {
        URL destinationURL = new URL(aURL);
        HttpsURLConnection conn = (HttpsURLConnection) destinationURL
                .openConnection();
        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
        BufferedWriter writer = new BufferedWriter(out);
        for (Certificate cert : certs) {
            System.out.println("Certificate is: " + cert);
            if(cert instanceof X509Certificate) {
                try {
                    ( (X509Certificate) cert).checkValidity();
                    System.out.println("Certificate is active for current date");
                    writer.write(cert.toString());
                    writer.flush();
                } catch(CertificateExpiredException cee) {
                    System.out.println("Certificate is expired");
                    writer.write("Error");
                    writer.flush();
                }
            }
        }
    }
  public static void main(String[] arstring) {
    int port = 3000;
    try {
      SSLServerSocketFactory f = (SSLServerSocketFactory) SSLServerSocketFactory
          .getDefault();
      SSLServerSocket server = (SSLServerSocket) f.createServerSocket(port);
      s = (SSLSocket) server.accept();
      s.setEnabledCipherSuites(s.getSupportedCipherSuites());
      InputStreamReader in = new InputStreamReader(s.getInputStream());
      BufferedReader reader = new BufferedReader(in);
      String str = null;
      while ((str = reader.readLine()) != null) {
        testConnectionTo(str);
        System.out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
