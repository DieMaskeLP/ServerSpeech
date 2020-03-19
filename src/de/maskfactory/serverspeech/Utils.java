package de.maskfactory.serverspeech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Created by Frederic | DieMaskeLP at 16.03.2020, 19:17
public class Utils {

    public static String encode(byte[] bytes){
        return Arrays.toString(bytes);
    }

    public static byte[] decode(String bytes){
        byte[] decode;
        String[] splitted = bytes.replace("[", "").replace("]", "").split(", ");
        decode = new byte[splitted.length];

        for (int i = 0; i < splitted.length; i++){
            decode[i] = Byte.parseByte(splitted[i]);
        }
        return decode;
    }

    public static List<byte[]> decode(String[] bytes){
        List<byte[]> decodes = new ArrayList<>();
        for (String bytess : bytes){
            byte[] decode;
            String[] splitted = bytess.replace("[", "").replace("]", "").split(", ");
            decode = new byte[splitted.length];

            for (int i = 0; i < splitted.length; i++){
                decode[i] = Byte.parseByte(splitted[i]);
            }
            decodes.add(decode);
        }

        return decodes;
    }

}
