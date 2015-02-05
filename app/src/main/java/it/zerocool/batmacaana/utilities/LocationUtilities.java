/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.utilities;

import android.location.Location;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import it.zerocool.batmacaana.model.Place;

/**
 * Created by Marco on 19/01/2015.
 */
public class LocationUtilities {

    public static void setPlaceDistance(Place place, Location current) {
        if (current != null) {
            Gson gson = new Gson();
            float distance = place.getLocation().distanceTo(current);
//            float distance = getDistance(current, place.getLocation());
            place.setDistanceFromCurrentPosition(distance);
        }
    }

    public static float getDistance(Location loc1, Location loc2) {
        Double lat1 = loc1.getLatitude();
        Double lon1 = loc1.getLongitude();
        Double lat2 = loc2.getLatitude();
        Double lon2 = loc2.getLongitude();
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                result_in_kms = String.valueOf(args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result_in_kms = result_in_kms.replaceAll(" km", "");
        Float f = Float.valueOf(result_in_kms);
        return f * 1000;
    }
}
