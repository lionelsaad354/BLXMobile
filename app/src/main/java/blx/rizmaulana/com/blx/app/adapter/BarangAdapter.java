package blx.rizmaulana.com.blx.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;

import blx.rizmaulana.com.blx.R;
import blx.rizmaulana.com.blx.app.model.BarangModel;
import blx.rizmaulana.com.blx.app.view.DetailBarangView;
import blx.rizmaulana.com.blx.helper.ConfigUrl;


public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder> {

    private List<BarangModel>      model;
    private Context                context;
    private setOnItemClickListener setOnItemClickListener;


    public BarangAdapter(Context context, List<BarangModel> model) {
        this.model = model;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_barang, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BarangModel mBarang = model.get(position);
        holder.mTxtSeller.setText(mBarang.getNamaPenjual());
        holder.mTxtLokasi.setText(mBarang.getKabupaten());
        holder.mTxtHarga.setText("Rp "+ NumberFormat.getIntegerInstance().format(Integer.valueOf(mBarang.getHarga())));
        holder.mTxtJudul.setText(mBarang.getNama());

        Glide.with(context)
                .load(ConfigUrl.imageBarang()+mBarang.getImage1())
                .override(128, 128)
                .error(context.getResources().getDrawable(R.drawable.img_broken))
                .placeholder(context.getResources().getDrawable(R.drawable.img_loading))
                .into(holder.mImgImage);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgImage;
        private TextView mTxtJudul, mTxtHarga, mTxtLokasi, mTxtSeller;

        public MyViewHolder(View view) {
            super(view);
            mImgImage = (ImageView) view.findViewById(R.id.img_barang);
            mTxtJudul = (TextView) view.findViewById(R.id.txt_nama);
            mTxtHarga = (TextView) view.findViewById(R.id.txt_harga);
            mTxtLokasi = (TextView) view.findViewById(R.id.txt_lokasi);
            mTxtSeller = (TextView) view.findViewById(R.id.txt_seller);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailBarangView.class);
                    intent.putExtra("barang", model.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }

    public void setSetOnItemClickListener(setOnItemClickListener setOnItemClickListener) {
        this.setOnItemClickListener = setOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public interface setOnItemClickListener {
        public void onClick(View view, int position);
    }


}
