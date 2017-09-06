package blx.rizmaulana.com.blx.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizki Maulana on 27/8/17.
 * email : rizmaulana@live.com
 * Mobile App Developer
 */

public class BarangModel implements Parcelable {
    private String id;
    private String nama;
    private String image1;
    private String image2;
    private String image3;
    private String deskripsi;
    private String harga;
    private String lokasi;
    private String tanggal;
    private String namaPenjual;
    private String nomorTelp;
    private String tanggalDaftar;
    private String kabupaten;
    private String kategori;
    private String lat;
    private String lon;

    public BarangModel(String id, String nama, String image1, String image2, String image3, String deskripsi, String harga, String lokasi, String tanggal, String namaPenjual, String nomorTelp, String tanggalDaftar, String kabupaten, String kategori, String lat, String lon) {
        this.id = id;
        this.nama = nama;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.namaPenjual = namaPenjual;
        this.nomorTelp = nomorTelp;
        this.tanggalDaftar = tanggalDaftar;
        this.kabupaten = kabupaten;
        this.kategori = kategori;
        this.lat = lat;
        this.lon = lon;
    }

    protected BarangModel(Parcel in) {
        id = in.readString();
        nama = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        image3 = in.readString();
        deskripsi = in.readString();
        harga = in.readString();
        lokasi = in.readString();
        tanggal = in.readString();
        namaPenjual = in.readString();
        nomorTelp = in.readString();
        tanggalDaftar = in.readString();
        kabupaten = in.readString();
        kategori = in.readString();
        lat = in.readString();
        lon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
        dest.writeString(image1);
        dest.writeString(image2);
        dest.writeString(image3);
        dest.writeString(deskripsi);
        dest.writeString(harga);
        dest.writeString(lokasi);
        dest.writeString(tanggal);
        dest.writeString(namaPenjual);
        dest.writeString(nomorTelp);
        dest.writeString(tanggalDaftar);
        dest.writeString(kabupaten);
        dest.writeString(kategori);
        dest.writeString(lat);
        dest.writeString(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BarangModel> CREATOR = new Creator<BarangModel>() {
        @Override
        public BarangModel createFromParcel(Parcel in) {
            return new BarangModel(in);
        }

        @Override
        public BarangModel[] newArray(int size) {
            return new BarangModel[size];
        }
    };

    public String getKategori() {
        return kategori;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNamaPenjual() {
        return namaPenjual;
    }

    public String getNomorTelp() {
        return nomorTelp;
    }

    public String getTanggalDaftar() {
        return tanggalDaftar;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
