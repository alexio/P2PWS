/*
 * @arthur: Hua Yang
 * @arthur: Alexio Mota
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Object;

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

    public String Delete(String url) {
        return null;
    }

    /*
     * Given the file url, provide HTTP reponses accordingly
     */
    public static void HTTP_Get(String url, DataOutputStream toClient, int port, Hashtable<String, String> files) throws Exception {
        String message = hardcoded_message();
        // if attempt to get the favicon, do nothing
        if (url.equals("/favicon.ico")) {
            return;
        }

        // if file path is 'local.html', respond with predefined HTTP 200 OK response
        if (url.equals("/local.html")) {
            String ip_addr = InetAddress.getLocalHost() + "";
            String[] ip = ip_addr.split("/");
            message += "<p> This is the local page on peer sever " + ip[1] + " port " + port + "\n";
        } else  {
            boolean found = true;
            String hashkey, hash_result = null;
            try {
                hashkey = p2pws.md5Hash(url);
                System.out.println("Hashed " + url + " = " + hashkey);
                hash_result = files.get(hashkey);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            
            // if file path is within this peer
            if (hash_result != null) {
                // if file doesn't exist, respond with 404 Not Found
                if (!found) {
                    message += "<p>HTTP/1.1 404 Not Found</p>\n";
                } else { // if file exist, respond with the file content
                    message += "<p>HTTP/1.1 200 OK</p>\n";
                    message += "<p>Content-Length:</p>\n";
                }
            } else { // search peer for the file
                message += "<p>HTTP/1.1 301 Moved Permanently</p>\n";
                message += "<p>Location: http://newhost:new.port/path(url)</p>\n";
            }
        }

        message += "</body>\n</html>\n";
        toClient.writeBytes(message);
        return;
    }

    public String Put(String url) {
        return null;
    }
}
