package com.example.demo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.demo.Adapter.AdapterListCategories;
import com.example.demo.R;
import com.example.demo.model.category_model;


import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    private View parent_view;
    Toolbar toolbar;
    String getCategory="";
    String[] name_arr;
    ArrayList<category_model> categoryArr=new ArrayList<>();
    String listtype="0";
    SharedPreferences Demo_share;
    public String Demo_de = "Demo_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_layout);
        parent_view = findViewById(android.R.id.content);
        Demo_share = getApplicationContext().getSharedPreferences(Demo_de, Context.MODE_PRIVATE);
        getCategory=getIntent().getStringExtra("categoryType");

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        TypedArray drw_arr = getResources().obtainTypedArray(R.array.sample_images);

        switch (getCategory.toLowerCase()) {
            case "breakfast":
                name_arr = getResources().getStringArray(R.array.breakfast);
                break;
            case "lunch":
                name_arr = getResources().getStringArray(R.array.lunch);
                break;
            case "snacks":
                name_arr = getResources().getStringArray(R.array.snacks);
                break;
            case "dinner":
                name_arr = getResources().getStringArray(R.array.breakfast);
                break;
        }

        categoryArr.clear();
        for (int i = 0; i < drw_arr.length(); i++) {
            category_model obj = new category_model();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            categoryArr.add(obj);
        }
        //set data and list adapter
        /*AdapterListCategories mAdapter = new AdapterListCategories(this, categoryArr);
        recyclerView.setAdapter(mAdapter);*/

        // on item list clicked



        AdapterListCategories mAdapter;
        listtype=Demo_share.getString("display","0");
        if(listtype.equals("1"))
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            mAdapter = new AdapterListCategories(this, categoryArr,listtype);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            invalidateOptionsMenu();
        }
        else
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.setHasFixedSize(true);
            mAdapter = new AdapterListCategories(this, categoryArr,listtype);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            invalidateOptionsMenu();
        }
        mAdapter.setOnItemClickListener(new AdapterListCategories.OnItemClickListener() {
            @Override
            public void onItemClick(View view, category_model obj, int position) {
                /*Snackbar.make(parent_view, "Item " + position + " clicked", Snackbar.LENGTH_SHORT).show();*/
                Intent intent=new Intent(Categories.this,SubCategories.class);
                intent.putExtra("subcat",obj.name);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
