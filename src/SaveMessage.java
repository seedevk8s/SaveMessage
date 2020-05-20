import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Vector;

public class SaveMessage {
    public static void main(String[] args) {
/*        if (args.length < 2) {
            System.out.println("Syntax: <url> <file>");
            return;
        }

        String url = args[0];
        String filePath = args[1];*/

        // call url
        String url = "https://facebook.com";
        // message
        String filePath = "fb3.html";


        try {

            URL urlObj = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();

            int responseCode = httpCon.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Server returned response code " + responseCode + ". Download failed.");
                System.exit(0);
            }

            InputStream inputStream = httpCon.getInputStream();
            BufferedInputStream reader = new BufferedInputStream(inputStream);

            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(filePath));

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }

            writer.close();
            reader.close();

            System.out.println("message saved");

        } catch (MalformedURLException e) {
            System.out.println("The specified URL is malformed: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An I/O error occurs: " + e.getMessage());
        }
    }

    public byte[] sendMessage(String sendUrl,String message,String encType){
        try{


            URL url = new URL(sendUrl);
            HttpURLConnection mConn = (HttpURLConnection)url.openConnection();
            mConn.setRequestProperty("Content-type", "text/plain;charset=".concat(encType));
            mConn.setRequestMethod("POST");
            mConn.setConnectTimeout(10000);
            mConn.setDoOutput(true);
            mConn.setFixedLengthStreamingMode(message.getBytes().length);
            OutputStream out = mConn.getOutputStream();
            out.write(message.getBytes());
            out.flush();
            out.close();

            InputStream in = mConn.getInputStream();
            ByteArrayOutputStream messageStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024* 512];
            byte[] ret = null;
            int length;
            int totalLen = 0;
            Vector<byte[]> vb = new Vector<byte[]>();
            while((length = in.read(buf)) != -1){

                messageStream.write(buf,0,length);
                ret = new byte[length];
                System.arraycopy(buf, 0, ret, 0, length);

                vb.add(ret);
                totalLen += length;
            }
            ret = new byte[totalLen];
            int increase = 0;
            for(int i = 0 ; i < vb.size() ; i++){
                byte[] temp = vb.get(i) ;
                System.arraycopy(temp, 0, ret, increase, temp.length);
                increase += temp.length;
            }

            return ret;
        }catch(Exception e){
            return null;
        }
    }

}
