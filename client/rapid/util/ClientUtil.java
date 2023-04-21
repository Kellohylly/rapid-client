package client.rapid.util;

import client.rapid.Client;

import java.net.URL;
import java.util.Scanner;

public class ClientUtil {

    public static boolean isLatest() {
        try {
            String s = new Scanner((new URL("https://raw.githubusercontent.com/geuxy/rapid-client/main/version.txt")).openStream(), "UTF-8").useDelimiter("\\A").next();
            return s.contains(Client.getInstance().getVersion());
        } catch (Exception e) {
            return false;
        }
    }
}
