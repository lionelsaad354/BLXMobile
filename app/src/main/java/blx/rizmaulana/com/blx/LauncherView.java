package blx.rizmaulana.com.blx;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import blx.rizmaulana.com.blx.app.model.KabupatenModel;
import blx.rizmaulana.com.blx.app.model.KategoriModel;
import blx.rizmaulana.com.blx.app.view.HomeView;
import blx.rizmaulana.com.blx.app.view.LoginView;
import blx.rizmaulana.com.blx.helper.AppController;
import blx.rizmaulana.com.blx.helper.ConfigUrl;
import blx.rizmaulana.com.blx.helper.Konstanta;
import blx.rizmaulana.com.blx.helper.PreferencesManager;
import io.realm.Realm;

public class LauncherView extends AppCompatActivity {

    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_view);
        realm = Realm.getDefaultInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /* versi Android lebih besar daripada Android M dan membutuhkan
            runtime permission
             */
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE}, 1);
        }else{
            if (realm.where(KabupatenModel.class).findAll().size() > 0){
                splashAndNavigate();
            }else {
                requestMaster();
            }
        }


    }

    private void requestMaster(){
        StringRequest mRequest = new StringRequest(Request.Method.POST, ConfigUrl.getMaster(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Response apabila request berhasil*/
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("result").equals("true")){
                        realm.beginTransaction();
                        JSONArray arrKategori = jsonObject.getJSONArray("kategori");
                        for (int i=0; i<arrKategori.length(); i++){
                            JSONObject mKategori = arrKategori.getJSONObject(i);
                            KategoriModel kategori = new KategoriModel(
                                    mKategori.getString("id_kategori"),
                                    mKategori.getString("nama"),
                                    mKategori.getString("image")
                                    );
                            realm.insert(kategori);
                        }

                        JSONArray arrKabupaten = jsonObject.getJSONArray("kabupaten");
                        for (int j=0; j<arrKabupaten.length(); j++){
                            JSONObject mKabupaten = arrKabupaten.getJSONObject(j);
                            KabupatenModel kabuaten = new KabupatenModel(
                                    mKabupaten.getString("id_kabupaten"),
                                    mKabupaten.getString("nama")
                            );
                            realm.insert(kabuaten);

                        }
                        realm.commitTransaction();
                        splashAndNavigate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LauncherView.this, "Internal server error.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*request apabula request gagal*/
                Toast.makeText(LauncherView.this, "Kesalahan koneksi.", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                return parameter;
            }
        };
        AppController.getInstance().getRequestQueue().add(mRequest);
    }

    private void splashAndNavigate(){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (PreferencesManager.getBoolean(getApplicationContext(), Konstanta.isLogin)){
                    /*sudah login, arahkan ke halaman home*/
                    startActivity(new Intent(getApplicationContext(), HomeView.class));
                    finish();
                }else {
                    /*belum login, arahkan ke halaman login */
                   // startActivity(new Intent(getApplicationContext(), HomeView.class));

                    startActivity(new Intent(getApplicationContext(), LoginView.class));
                    finish();
                }
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 4){

            if (realm.where(KabupatenModel.class).findAll().size() > 0){
                splashAndNavigate();
            }else {
                requestMaster();
            }
        }else {
            Toast.makeText(this, "Izin ditolak, tidak dapat membuka aplikasi.", Toast.LENGTH_SHORT).show();
        }
    }
}
