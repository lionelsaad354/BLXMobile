package blx.rizmaulana.com.blx.app.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import blx.rizmaulana.com.blx.R;
import blx.rizmaulana.com.blx.app.model.BarangModel;
import blx.rizmaulana.com.blx.helper.ConfigUrl;

import static android.R.attr.x;

public class DetailBarangView extends AppCompatActivity {

    private BarangModel mBarang;
    private ImageView mImgBarang;
    private TextView mTxtHarga, mTxtNama, mTxtDeskripsi, mTxtSeller, mTxtAlamat, mTxtTime;
    private FloatingActionButton mFabMap, mFabTelpon, mFabSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang_view);
        mBarang = getIntent().getParcelableExtra("barang");
        initToolbar();
        initView();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Barang");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        mImgBarang = (ImageView) findViewById(R.id.img_barang);
        mTxtHarga = (TextView) findViewById(R.id.txt_harga);
        mTxtNama = (TextView) findViewById(R.id.txt_nama);
        mTxtDeskripsi = (TextView) findViewById(R.id.txt_deskripsi);
        mTxtSeller = (TextView) findViewById(R.id.txt_seller);
        mTxtAlamat = (TextView) findViewById(R.id.txt_alamat);
        mTxtTime = (TextView) findViewById(R.id.txt_time);
        mFabMap = (FloatingActionButton) findViewById(R.id.m_fab_map);
        mFabSms = (FloatingActionButton) findViewById(R.id.m_fab_sms);
        mFabTelpon = (FloatingActionButton) findViewById(R.id.m_fab_telpon);


        mTxtSeller.setText(mBarang.getNamaPenjual());
        mTxtAlamat.setText(mBarang.getLokasi());
        mTxtHarga.setText("Rp "+ NumberFormat.getIntegerInstance().format(Integer.valueOf(mBarang.getHarga())));
        mTxtNama.setText(mBarang.getNama());
        mTxtDeskripsi.setText(mBarang.getDeskripsi());
        mTxtTime.setText(getRelativeTime(mBarang.getTanggal()));

        Glide.with(getApplicationContext())
                .load(ConfigUrl.imageBarang()+mBarang.getImage1())
                .override(128, 128)
                .error(getApplication().getResources().getDrawable(R.drawable.img_broken))
                .into(mImgBarang);

        mFabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+mBarang.getLat()+","+mBarang.getLon()));
                startActivity(intent);
            }
        });

        mFabTelpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String   number     = "tel:" + mBarang.getNomorTelp();
                Intent   callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
            }
        });

        mFabSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mBarang.getNomorTelp();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));

            }
        });

    }

    private String getRelativeTime(String timestamp){
        String     data   = "";
        try {
            DateFormat   format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date         date   = format.parse(timestamp);
            CharSequence chars  = DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            return String.valueOf(chars);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }


}
