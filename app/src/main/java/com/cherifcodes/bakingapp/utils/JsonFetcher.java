package com.cherifcodes.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JsonFetcher {
    //
    private static final String TAG = JsonFetcher.class.getSimpleName();
    //Define HttpConnection settings
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String HTTP_REQUEST_METHOD = "GET";

    /**
     * Builds a url needed to communicate with the network
     *
     * @param urlString the string represantation of the Json file location
     * @return the built URL
     */
    public static URL buildUrl(String urlString) {

        //build the Uri from the strings
        Uri uri = Uri.parse(urlString);

        //build the URL from the Uri
        URL dbUrl = null;
        try {
            dbUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return dbUrl;
    }


    /**
     * Fetches a JSON response using the specified URL
     *
     * @param url the specified URL
     * @return the JSON response as a String
     */
    public static String getJsonResponse(URL url) {

        String jsonResponseString = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        Scanner scanner = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod(HTTP_REQUEST_METHOD);
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                jsonResponseString = scanner.next();
            } else
                Log.e(TAG, "Empty stream returned from Url: " + url);

        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving JSON response from " + url + "\n", e);
        } finally {
            if (scanner != null) scanner.close();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) urlConnection.disconnect();
        }

        return jsonResponseString;
    }
}
