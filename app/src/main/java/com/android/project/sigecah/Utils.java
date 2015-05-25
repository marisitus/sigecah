package com.android.project.sigecah;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    /*
    * Metodo que realiza una peticion GET al servidor para obtener datos.
    * */
    public static String peticionGet(String URL) {
        String respStr = null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet resp = new HttpGet(URL);
        resp.setHeader("content-type", "application/json");

        try {
            HttpResponse response = httpClient.execute(resp);
            respStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respStr;
    }

    /*
    * Metodo que realizar una peticion GET a una pagina web/servidor que devuelve
    * una imagen.
    * */
    public static InputStream peticionGetImage (String URL){
        InputStream inputStream = null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet resp = new HttpGet(URL);
        resp.setHeader("content-type", "application/json");

        try {
        HttpResponse response = httpClient.execute(resp);
        HttpEntity entity = response.getEntity();
        inputStream = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /*
    * Metodo que realizar una peticion POST al servidor para insertar nuevos datos
    * */
    public static void peticionPost(String URL, String entidad){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        post.setHeader("content-type", "application/json");

        try{
            StringEntity entity = new StringEntity(entidad);
            post.setEntity(entity);
            httpClient.execute(post);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Metodo que formatea la fecha obtenida por el servidor a un formato de fecha
    * dd/mm/aaaa
    * */
    public static String formatearFecha(String fechaSinFormatear){
        String result;

        String[] partesFecha = fechaSinFormatear.split("-|T");
        result = partesFecha[2]+"/"+partesFecha[1]+"/"+partesFecha[0];
        return result;
    }
}