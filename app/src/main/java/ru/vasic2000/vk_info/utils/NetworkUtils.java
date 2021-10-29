package ru.vasic2000.vk_info.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    private static final String VK_API_URL = "https://api.vk.com/";
    private static final String VK_USERS_ID = "method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String PARAM_VERSION = "v";
    private static final String PARAM_KEY = "access_token";
    private static final String VERS = "5.131";
    private static final String KEY = "206f9089206f9089206f90895f2003a9672206f206f90897d3f9616467caea7261a6752";

    public static URL generateURL(String userID) {
        Uri builtUri = Uri.parse(VK_API_URL + VK_USERS_ID)
                .buildUpon()
                .appendQueryParameter(PARAM_USER_ID, userID)
                .appendQueryParameter(PARAM_KEY, KEY)
                .appendQueryParameter(PARAM_VERSION, VERS)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream ins = urlConnection.getInputStream();

            Scanner scan = new Scanner(ins);
            scan.useDelimiter("\\A");

            if (scan.hasNext()) {
                return scan.next();
            } else {
                return null;
            }
        } catch (UnknownHostException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
