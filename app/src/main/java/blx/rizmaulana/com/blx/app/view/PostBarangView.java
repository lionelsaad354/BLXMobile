package blx.rizmaulana.com.blx.app.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blx.rizmaulana.com.blx.R;
import blx.rizmaulana.com.blx.app.model.KabupatenModel;
import blx.rizmaulana.com.blx.app.model.KategoriModel;
import blx.rizmaulana.com.blx.helper.AppController;
import blx.rizmaulana.com.blx.helper.BitmapHelper;
import blx.rizmaulana.com.blx.helper.ConfigUrl;
import blx.rizmaulana.com.blx.helper.DrawableToFile;
import blx.rizmaulana.com.blx.helper.ImageFilePath;
import blx.rizmaulana.com.blx.helper.Konstanta;
import blx.rizmaulana.com.blx.helper.PreferencesManager;
import blx.rizmaulana.com.blx.network.VolleyMultipartRequest;
import io.realm.Realm;

public class PostBarangView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner  mSpinnerKab;
    private Spinner  mSpinnerKategori;
    private EditText mTxtJudul, mTxtHarga, mTxtDeksrpsi;
    private Button    mBtnSimpan;
    private ImageView mImg1, mImg2, mImg3;
    private Bitmap mPhoto1, mPhoto2, mPhoto3;
    private ProgressDialog mProgressDialog;

    private int mSpinnerKabPost        = 0;
    private int mSpinnerKategoriPost = 0;
    private Realm realm;

    private List<KategoriModel>  mKategoriModels  = new ArrayList<>();
    private List<KabupatenModel> mKabupatenModels = new ArrayList<>();
    private String mCurentPhotoPath;
    private String mParamJudul, mParamHarga, mParamDeksripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_barang_view);
        realm = Realm.getDefaultInstance();
        mKabupatenModels.addAll(realm.copyFromRealm(realm.where(KabupatenModel.class).findAll()));
        mKategoriModels.addAll(realm.copyFromRealm(realm.where(KategoriModel.class).findAll()));

        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post Barang");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        mSpinnerKategori = (Spinner) findViewById(R.id.spinner_kategori);
        mSpinnerKab = (Spinner) findViewById(R.id.spinner_kab);
        mTxtJudul = (EditText) findViewById(R.id.txt_judul);
        mTxtDeksrpsi = (EditText) findViewById(R.id.txt_deskripsi);
        mTxtHarga = (EditText) findViewById(R.id.txt_harga);
        mBtnSimpan = (Button) findViewById(R.id.m_btn_simpan);
        mImg1 = (ImageView) findViewById(R.id.m_img_1);
        mImg2 = (ImageView) findViewById(R.id.m_img_2);
        mImg3 = (ImageView) findViewById(R.id.m_img_3);
        mProgressDialog = new ProgressDialog(this);

        /*inisiasi spinner*/
        initSpinnerKab();
        initSpinnerKategori();

        mImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraIntent(1);
            }
        });
        mImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraIntent(2);
            }
        });
        mImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraIntent(3);
            }
        });
        mBtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParamJudul = mTxtJudul.getText().toString();
                mParamHarga = mTxtHarga.getText().toString();
                mParamDeksripsi = mTxtDeksrpsi.getText().toString();
                if (mParamJudul.isEmpty() || mParamHarga.isEmpty() || mParamDeksripsi.isEmpty()) {
                    Toast.makeText(PostBarangView.this, "Semua field harus terisi", Toast.LENGTH_SHORT).show();
                } else if (mPhoto1 == null || mPhoto2 == null || mPhoto3 == null) {
                    Toast.makeText(PostBarangView.this, "Foto harus terisi sebanyak 3 buah.", Toast.LENGTH_SHORT).show();
                } else {
                    postBarang();
                }

            }
        });
    }

    private void postBarang() {
        mProgressDialog.setMessage("Mengirimkan barang, harap tunggu ...");
        mProgressDialog.show();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ConfigUrl.postBarang(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    Log.d("TAG", "onResponse: " + jsonObject.toString());
                    switch (jsonObject.getString("result")) {
                        case "true":
                            Toast.makeText(PostBarangView.this, "Barang anda berhasil di post.", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            break;
                        default:
                            Toast.makeText(PostBarangView.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PostBarangView.this, "Internal server error.", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(PostBarangView.this, "Kesalahan koneksi.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", mParamJudul);
                params.put("deskripsi", mParamDeksripsi);
                params.put("harga", mParamHarga);
                params.put("alamat", "");
                params.put("latitude", "-3.4871496");
                params.put("longitude", "115.9973593");
                params.put("id_kategori", mKategoriModels.get(mSpinnerKategoriPost).getId());
                params.put("id_kabupaten",mKabupatenModels.get(mSpinnerKabPost).getId());
                params.put("id_user", PreferencesManager.getString(getApplicationContext(), Konstanta.USER_ID));
                Log.d("TAG", "getParams: "+params.toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image_1", new DataPart("1.jpg", DrawableToFile.getFileDataFromDrawable(getApplicationContext(), mImg1.getDrawable()), "image/jpeg"));
                params.put("image_2", new DataPart("2.jpg", DrawableToFile.getFileDataFromDrawable(getApplicationContext(), mImg2.getDrawable()), "image/jpeg"));
                params.put("image_3", new DataPart("3.jpg", DrawableToFile.getFileDataFromDrawable(getApplicationContext(), mImg3.getDrawable()), "image/jpeg"));
                Log.d("TAG", "getByteData: "+params.toString());
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().add(multipartRequest);
    }

    private void initSpinnerKab() {
        final List<String> datas = new ArrayList<>();
        for (KabupatenModel sellerModel : mKabupatenModels) {
            datas.add(sellerModel.getNama());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, datas);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mSpinnerKab.setAdapter(dataAdapter);
        mSpinnerKab.setOnItemSelectedListener(this);
    }

    private void initSpinnerKategori() {
        final List<String> datas = new ArrayList<>();
        for (KategoriModel sellerModel : mKategoriModels) {
            datas.add(sellerModel.getNama());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, datas);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mSpinnerKategori.setAdapter(dataAdapter);
        mSpinnerKategori.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
            case R.id.spinner_kab:
                mSpinnerKabPost = i;
                break;

            case R.id.spinner_kategori:
                mSpinnerKategoriPost = i;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void launchCameraIntent(int CAMERA_CODE) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, new Date().toString());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Mobile APKT");
        Uri    mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        mCurentPhotoPath = ImageFilePath.getPath(getApplicationContext(), mImageUri);
        startActivityForResult(intent, CAMERA_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                switch (requestCode) {
                    case 1:
                        mPhoto1 = BitmapHelper.compress(mCurentPhotoPath);
                        mImg1.setImageBitmap(mPhoto1);
                        break;
                    case 2:
                        mPhoto2 = BitmapHelper.compress(mCurentPhotoPath);
                        mImg2.setImageBitmap(mPhoto2);
                        break;
                    case 3:
                        mPhoto3 = BitmapHelper.compress(mCurentPhotoPath);
                        mImg3.setImageBitmap(mPhoto2);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
