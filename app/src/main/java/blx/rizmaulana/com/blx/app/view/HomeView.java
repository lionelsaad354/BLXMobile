package blx.rizmaulana.com.blx.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blx.rizmaulana.com.blx.R;
import blx.rizmaulana.com.blx.app.adapter.BarangAdapter;
import blx.rizmaulana.com.blx.app.model.BarangModel;
import blx.rizmaulana.com.blx.helper.AppController;
import blx.rizmaulana.com.blx.helper.ConfigUrl;


public class HomeView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView       mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private LinearLayout       mLayoutNodata;
    private List<BarangModel> mBarangModelList = new ArrayList<>();
    private FloatingActionButton mFabPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
        initToolbarAndNavigation();
        initView();

    }

    private void initToolbarAndNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BLX");
        setSupportActionBar(toolbar);

        DrawerLayout          drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mLayoutNodata = (LinearLayout) findViewById(R.id.layout_no_data);
        BarangAdapter adapter = new BarangAdapter(getApplicationContext(), mBarangModelList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mFabPost = (FloatingActionButton) findViewById(R.id.fab_post);
        mFabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PostBarangView.class));
            }
        });

        getDataBarang();
    }

    private void getDataBarang() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });

        StringRequest mRequest = new StringRequest(Request.Method.POST, ConfigUrl.getDataBarang(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Response apabila request berhasil*/
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("result").equals("true")) {
                        /*barang ada*/
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mLayoutNodata.setVisibility(View.GONE);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        mBarangModelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mBarang = jsonArray.getJSONObject(i);
                            BarangModel barangModel = new BarangModel(
                                    mBarang.getString("id_barang"),
                                    mBarang.getString("nama"),
                                    mBarang.getString("image_1"),
                                    mBarang.getString("image_2"),
                                    mBarang.getString("image_3"),
                                    mBarang.getString("deskripsi"),
                                    mBarang.getString("harga"),
                                    mBarang.getString("alamat"),
                                    mBarang.getString("tanggal"),
                                    mBarang.getString("seller"),
                                    mBarang.getString("telepon"),
                                    mBarang.getString("user_register"),
                                    mBarang.getString("kabupaten"),
                                    mBarang.getString("kategori"),
                                    mBarang.getString("latitude"),
                                    mBarang.getString("longitude"));

                            mBarangModelList.add(barangModel);
                        }
                        mRecyclerView.getAdapter().notifyDataSetChanged();

                    } else {
                        /*barang tidak ada*/
                        mRecyclerView.setVisibility(View.GONE);
                        mLayoutNodata.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeView.this, "Internal server error.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*request apabula request gagal*/
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
                Toast.makeText(HomeView.this, "Kesalahan koneksi.", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                return parameter;
            }
        }; AppController.getInstance().getRequestQueue().add(mRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
