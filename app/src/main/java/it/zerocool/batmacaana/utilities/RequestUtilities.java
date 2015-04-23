/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File it.zerocool.batmacaana.utility/RequestUtilities.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Aux class containing Http request utility
 *
 * @author Marco Battisti
 */
public class RequestUtilities {


    private static final int BUFFER_SIZE = 8;
    private static final String ENCODING = "UTF-8";
    private static final int CONNECTION_TIMEOUT = 15000;

    /**
     * Private constructor
     */
    private RequestUtilities() {
    }

    /**
     * Obtains String from InputStream
     *
     * @param is is the InputStream to elaborate
     * @return InputStream data in String format
     */
    @Nullable
    private static String inputStreamToString(@NonNull InputStream is) {
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, ENCODING), BUFFER_SIZE);
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = reader.readLine();
            }
            is.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static String requestJsonString(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        String res;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            res = inputStreamToString(in);
        } finally {
            urlConnection.disconnect();
        }
        return res;
    }


    /**
     * Check if device is connected to Internet
     *
     * @param context is the context of SearchActivity
     * @return true if device is connected, false otherwise
     */
    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}
