package com.example.lytuananh.demowonoloapp.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lytuananh on 11/8/15.
 */
public class Utilities {
    //Map Constant
    public static int DEAFAULT_MAP_ZOOM = 15;
    public static long DELAY_TIME_MOVING_APP = 1000;
    public static int GET_IMAGE = 100;

    //Instagram
    public static String INS_META = "meta";
    public static String INS_CODE = "code";
    public static String INS_DATA =  "data";
    public static int INS_SUCCESS_META_CODE = 200;
    public static String INS_IMAGE_TYPE = "image";
    public static String INS_IMAGE_LOW = "low_resolution";
    public static String INS_IMAGE_STANDARD = "standard_resolution";
    //SERVER API
    public static String BASE_URL = "https://api.instagram.com/v1/media/search?";

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public static String getUrlContents(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return content.toString();
    }
}
