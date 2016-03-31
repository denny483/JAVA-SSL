import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {
  public static void main(String[] arstring) {

    int port = 3000;

    try {
      // 取得factory : 亦可由SSLContext產生
      SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory
          .getDefault();
      SSLSocket s = (SSLSocket) f.createSocket("localhost", port);

      // 使用anonymous的cipher suite (測試用)
      s.setEnabledCipherSuites(s.getSupportedCipherSuites());

      InputStreamReader in = new InputStreamReader(System.in);
      BufferedReader reader = new BufferedReader(in);

      OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
      BufferedWriter writer = new BufferedWriter(out);
      InputStreamReader serverin = new InputStreamReader(s.getInputStream());
      BufferedReader serverreader = new BufferedReader(serverin);
      String str = null;
      while ((str = reader.readLine()) != null) {
        writer.write(str + '\n');
        writer.flush();
        if(true)break;
      }
      str = null;
      while ((str = serverreader.readLine()) != null) {
        System.out.println(str);
        System.out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
