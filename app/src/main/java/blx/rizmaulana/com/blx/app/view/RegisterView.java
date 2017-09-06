package blx.rizmaulana.com.blx.app.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import blx.rizmaulana.com.blx.R;
import blx.rizmaulana.com.blx.helper.AppController;
import blx.rizmaulana.com.blx.helper.ConfigUrl;
import blx.rizmaulana.com.blx.helper.Konstanta;
import blx.rizmaulana.com.blx.helper.PreferencesManager;

public class RegisterView extends AppCompatActivity {
    private EditText mTxtNama, mTxtPassword, mTxtEmail, mTxtTelp;
    private Button         mBtnRegister;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        mTxtNama = (EditText) findViewById(R.id.txt_nama);
        mTxtPassword = (EditText) findViewById(R.id.txt_password);
        mTxtEmail = (EditText) findViewById(R.id.txt_email);
        mTxtTelp = (EditText) findViewById(R.id.txt_telepon);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mProgressDialog = new ProgressDialog(this);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, nama, telp;
                email = mTxtEmail.getText().toString();
                password = mTxtPassword.getText().toString();
                nama = mTxtNama.getText().toString();
                telp = mTxtTelp.getText().toString();
                
                if (email.isEmpty() || password.isEmpty() || nama.isEmpty() || telp.isEmpty()){
                    Toast.makeText(RegisterView.this, "Semua field harus terisi.", Toast.LENGTH_SHORT).show();
                }else {
                    doRegister(email, password, nama, telp);
                }
            }
        });
    }

    private void doRegister(final String email, final String password, final String nama, final String telp){
        mProgressDialog.setMessage("Resgistrasi, harap tunggu ...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        StringRequest mLoginRequest = new StringRequest(Request.Method.POST, ConfigUrl.doRegister(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: "+response);
                /*Response apabila request berhasil*/
                mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("result").equals("true")){
                        /*simpan data user*/
                        JSONObject userData = jsonObject.getJSONObject("data");
                        PreferencesManager.set(getApplicationContext(), Konstanta.USER_ID, userData.getString("id_user"));
                        PreferencesManager.set(getApplicationContext(), Konstanta.USER_NAMA, userData.getString("nama"));
                        PreferencesManager.set(getApplicationContext(), Konstanta.USER_EMAIL, userData.getString("email"));
                        PreferencesManager.set(getApplicationContext(), Konstanta.USER_PHONE, userData.getString("telepon"));

                        /*set login*/
                        PreferencesManager.set(getApplicationContext(), Konstanta.isLogin, true);

                        /*restart aplikasi*/
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else {
                        Toast.makeText(RegisterView.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterView.this, "Internal server error.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.toString());
                /*request apabula request gagal*/
                mProgressDialog.dismiss();
                Toast.makeText(RegisterView.this, "Kesalahan koneksi.", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("email", email);
                parameter.put("password", password);
                parameter.put("nama", nama);
                parameter.put("telp", telp);

                return parameter;
            }
        };
        AppController.getInstance().getRequestQueue().add(mLoginRequest);
    }

}
