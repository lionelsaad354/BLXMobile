package blx.rizmaulana.com.blx.app.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LoginView extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private EditText       mTxtEmail, mTxtPassword;
    private Button mBtnLogin, mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        initView();
    }

    private void initView() {
        mTxtEmail = (EditText) findViewById(R.id.txt_email);
        mTxtPassword = (EditText) findViewById(R.id.txt_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mProgressDialog = new ProgressDialog(this);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*lakukan action apabila tombol login di klil*/
                String email, password;
                email = mTxtEmail.getText().toString();
                password = mTxtPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginView.this, "Email dan password harus terisi.", Toast.LENGTH_SHORT).show();
                }else {
                    doLogin(email, password);
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*lakukan action apabila tombol register di klil*/
                startActivity(new Intent(getApplicationContext(), RegisterView.class));
            }
        });
    }

    private void doLogin(final String email, final String password){
        mProgressDialog.setMessage("Autentikasi pengguna, harap tunggu ...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        StringRequest mLoginRequest = new StringRequest(Request.Method.POST, ConfigUrl.doLogin(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                        Toast.makeText(LoginView.this, "Kombinasi username dan password salah.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginView.this, "Internal server error.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*request apabula request gagal*/
                mProgressDialog.dismiss();
                Toast.makeText(LoginView.this, "Kesalahan koneksi.", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("email", email);
                parameter.put("password", password);
                return parameter;
            }
        };
        AppController.getInstance().getRequestQueue().add(mLoginRequest);
    }
}
