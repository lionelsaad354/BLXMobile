package blx.rizmaulana.com.blx.app.model;

import io.realm.RealmObject;

/**
 * Created by Rizki Maulana on 29/8/17.
 * email : rizmaulana@live.com
 * Mobile App Developer
 */

public class KabupatenModel extends RealmObject {
    String id;
    String nama;

    public KabupatenModel(){}

    public KabupatenModel(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}
