/*
 * @arthur: Hua Yang
 * @arthur: Alexio Mota
 */
import java.io.*;
import java.net.*;
import java.util.*;

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
        String message = hardcoded_message();

        // if attempt to get the favicon, do nothing
        if (url.equals("/favicon.ico")) {
            return;
        }

        // if file path is 'local.html', respond with predefined HTTP 200 OK response
        if (url.equals("/local.html")) {
            message += "<p> This is the local page on peer sever " + InetAddress.getLocalHost() + " port " + port + "\n";
            message += "</body>\n</html>\n";
            toClient.writeBytes(message);
            return;
        }
        
        boolean found = true;
        // if file path is within this peer
        if (true) {
            // if file doesn't exist, respond with 404 Not Found
            if (found) {
                message += "<p>HTTP/1.1 404 Not Found</p>\n";
            } else { // if file exist, respond with the file content
                message += "<p>HTTP/1.1 200 OK</p>\n";
                message += "<p>Content-Length:</p>\n";
            }
        } else { // search peer for the file
            message += "<p>HTTP/1.1 301 Moved Permanently</p>\n";
            message += "<p>Location: http://newhost:new.port/path(url)</p>\n";
        }

        message += "</body>\n</html>\n";
        System.out.println(message);
        toClient.writeBytes(message);
        return;
    }

    public static String Put(String url) {
        return null;
    }
}
