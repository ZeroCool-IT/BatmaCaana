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

    /**
     * Private constructor
     */
    private RequestUtilities() {
    }

// --Commented out by Inspection START (11/03/2015 15:55):
//    /**
//     * Calls HTTP GET method for retrieving InputStream from web
//     *
//     * @param uri is the URI to call
//     * @return the InputStream containing infos, null in case of connection
//     * error
//     * @throws IOException
//     * @throws ClientProtocolException
//     */
//    public static InputStream requestInputStream(String uri)
//            throws IOException {
//        InputStream is;
//        HttpClient httpclient = new DefaultHttpClient();
//        uri = uri.replaceAll(" ", "%20");
//        HttpGet httppost = new HttpGet(uri);
//        HttpResponse response = httpclient.execute(httppost);
//        HttpEntity entity = response.getEntity();
//        is = entity.getContent();
//        return is;
//    }
// --Commented out by Inspection STOP (11/03/2015 15:55)

    /**
     * Obtains String from InputStream
     *
     * @param is is the InputStream to elaborate
     * @return InputStream data in String format
     */
    private static String inputStreamToString(InputStream is) {
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

    public static String requestJsonString(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String res;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            res = inputStreamToString(in);
        } finally {
            urlConnection.disconnect();
        }
        return res;
    }

// --Commented out by Inspection START (05/03/2015 16:37):
//    /**
//     * Obtains a Bitmap object from specified URI
//     *
//     * @param url is the URI of Bitmap
//     * @return a Bitmap
//     */
//    public static Bitmap downloadBitmap(String url) {
//        final AndroidHttpClient client = AndroidHttpClient
//                .newInstance("Android");
//        final HttpGet getRequest = new HttpGet(url);
//
//        try {
//            HttpResponse response = client.execute(getRequest);
//            final int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                Log.w("ImageDownloader", "Error " + statusCode
//                        + " while retrieving bitmap from " + url);
//                return null;
//            }
//
//            final HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream inputStream = null;
//                try {
//                    inputStream = entity.getContent();
//                    return BitmapFactory
//                            .decodeStream(inputStream);
//                } finally {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                    entity.consumeContent();
//                }
//            }
//        } catch (Exception e) {
//            getRequest.abort();
//            Log.w("ImageDownloader", "Error while retrieving bitmap from "
//                    + url, e);
//        } finally {
//            if (client != null) {
//                client.close();
//            }
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:37)

    /**
     * Check if device is connected to Internet
     *
     * @param context is the context of SearchActivity
     * @return true if device is connected, false otherwise
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

// --Commented out by Inspection START (05/03/2015 16:37):
//    public static void post(String endpoint, Map<String, String> params) throws IOException {
//        URL url;
//        try {
//            url = new URL(endpoint);
//        } catch (MalformedURLException e) {
//            throw new IllegalArgumentException("invalid url: " + endpoint);
//        }
//        StringBuilder bodyBuilder = new StringBuilder();
//        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> param = iterator.next();
//            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
//            if (iterator.hasNext()) {
//                bodyBuilder.append('&');
//            }
//        }
//        String body = bodyBuilder.toString();
//        byte[] bytes = body.getBytes();
//        HttpURLConnection conn = null;
//        try {
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setFixedLengthStreamingMode(bytes.length);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//            OutputStream out = conn.getOutputStream();
//            out.write(bytes);
//            out.close();
//            int status = conn.getResponseCode();
//            if (status != 200) {
//                throw new IOException("Errore " + status);
//            }
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//    }
// --Commented out by Inspection STOP (05/03/2015 16:37)

}
