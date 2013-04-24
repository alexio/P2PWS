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
	
    /*
     * Given the file url, calculate and return the MD5 hash value
     */
    public static int MD5(String url) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(url.getBytes(), 0, url.length());
        return (new BigInteger(1, md.digest())).intValue();
    }

    public static String Delete(String url) {
        return null;
    }

    /*
     * Given the file url, provide HTTP reponses accordingly
     */
    public static String Get(String url) throws Exception {
        // if file path is 'local.html', return with HTTP 200 OK response,
        if (url.equals("local.html")) {
            
        }

        int hashvalue = MD5(url);
        System.out.println("MD5: " + hashvalue);
        return null;
    }

    public static String Put(String url) {
        return null;
    }

    public static void main(String args[]) throws Exception {
        Get("local.html");
    }
}
