/*
 * @arthur: Hua Yang
 * @arthur: Alexio Mota
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import java.math.*;

public class functions {
   
    public static String hardcoded_message() {
        String hardcoded = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
        hardcoded += "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n";
        hardcoded += "<head>\n";
        hardcoded += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n";
        hardcoded += "</head>\n";
        hardcoded += "<body>\n";
        return hardcoded;
    }

    public static String Delete(String url) {
        return null;
    }

    /*
     * Given the file url, provide HTTP reponses accordingly
     */
    public static void Get(String url, DataOutputStream toClient, int port) throws Exception {
        // if attempt to get the favicon, do nothing
        if (url.equals("/favicon.ico")) {
            return;
        }
        // if file path is 'local.html', respond with predefined HTTP 200 OK response
        if (url.equals("/local.html")) {
            String message = hardcoded_message();
            message += "<p> This is the local page on peer sever " + InetAddress.getLocalHost() + " port " + port + "\n";
            message += "</body>\n</html>\n";
            toClient.writeBytes(message);
        }

        return;
    }

    public static String Put(String url) {
        return null;
    }
}
