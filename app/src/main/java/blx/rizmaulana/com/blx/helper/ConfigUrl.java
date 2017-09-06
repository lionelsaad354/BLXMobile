package blx.rizmaulana.com.blx.helper;

/**
 * Created by Rizki Maulana on 27/8/17.
 * email : rizmaulana@live.com
 * Mobile App Developer
 */

public class ConfigUrl {

    private static final String BASE_URL = "http://192.168.0.10/blx/";

    public static String doLogin(){
        return BASE_URL+"index.php/service/login";
    }

    public static String doRegister(){
        return BASE_URL+"index.php/service/register";
    }

    public static String imageBarang(){
        return BASE_URL+"images/barang/";
    }

    public static String getDataBarang(){
        return BASE_URL+"index.php/service/getBarang";
    }

    public static String getMaster(){
        return BASE_URL+"index.php/service/getmaster";
    }

    public static String postBarang(){
        return BASE_URL+"index.php/service/postBarang";
    }


}
