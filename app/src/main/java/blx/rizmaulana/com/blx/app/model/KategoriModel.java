package blx.rizmaulana.com.blx.app.model;

import io.realm.RealmObject;

/**
 * Created by Rizki Maulana on 29/8/17.
 * email : rizmaulana@live.com
 * Mobile App Developer
 */

public class KategoriModel extends RealmObject {
    String id;
    String nama;
    String icon;

    public KategoriModel(){}

    public KategoriModel(String id, String nama, String icon) {
        this.id = id;
        this.nama = nama;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getIcon() {
        return icon;
    }
}
