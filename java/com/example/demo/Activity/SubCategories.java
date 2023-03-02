package com.example.demo.Activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.Utils.Tools;

public class SubCategories extends AppCompatActivity {

    ViewPager view_pager;
    Toolbar toolbar;
    String[] name;
    private int about_images_array[];
    TypedArray drw_arr;

    private String about_title_array[];
    private MyViewPagerAdapter myViewPagerAdapter;
    String getsubcat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_layout);

        view_pager = (ViewPager) findViewById(R.id.view_pager);



        getsubcat=getIntent().getStringExtra("subcat");

        switch (getsubcat.toLowerCase())
        {
            case "appetizer":
                about_title_array=getResources().getStringArray(R.array.appetizer);
                drw_arr = getResources().obtainTypedArray(R.array.appetizer_img);
                break;

            case "breakfast":
                about_title_array=getResources().getStringArray(R.array.breakfast);
                drw_arr=getResources().obtainTypedArray(R.array.breakfast_img);
                break;

            case "burger":
                about_title_array=getResources().getStringArray(R.array.burger);
                drw_arr=getResources().obtainTypedArray(R.array.burger_img);
                break;

            case "coffee section":
                about_title_array=getResources().getStringArray(R.array.coffee_section);
                drw_arr=getResources().obtainTypedArray(R.array.coffee_section_img);
                break;

            case "dessert":
                about_title_array=getResources().getStringArray(R.array.dessert);
                drw_arr=getResources().obtainTypedArray(R.array.dessert_img);
                break;

            case "fresh juices":
                about_title_array=getResources().getStringArray(R.array.fresh_juices);
                drw_arr=getResources().obtainTypedArray(R.array.fresh_juices_img);
                break;

            case "ice tea":
                about_title_array=getResources().getStringArray(R.array.ice_tea);
                drw_arr=getResources().obtainTypedArray(R.array.ice_tea_img);
                break;

            case "milkshake":
                about_title_array=getResources().getStringArray(R.array.milkshake);
                drw_arr=getResources().obtainTypedArray(R.array.milkshake_img);
                break;

            case "mocktails":
                about_title_array=getResources().getStringArray(R.array.mocktails);
                drw_arr=getResources().obtainTypedArray(R.array.mocktails_img);
                break;

            case "mojito":
                about_title_array=getResources().getStringArray(R.array.mojito);
                drw_arr=getResources().obtainTypedArray(R.array.mojito_img);
                break;

            case "pasta":
                about_title_array=getResources().getStringArray(R.array.pasta);
                drw_arr=getResources().obtainTypedArray(R.array.pasta_img);
                break;

            case "salad":
                about_title_array=getResources().getStringArray(R.array.salad);
                drw_arr=getResources().obtainTypedArray(R.array.salad_img);
                break;

            case "special coffee":
                about_title_array=getResources().getStringArray(R.array.special_coffee);
                drw_arr=getResources().obtainTypedArray(R.array.special_coffee_img);
                break;

        }
        //view_pager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap));
        initToolbar();
        myViewPagerAdapter = new MyViewPagerAdapter();
        view_pager.setAdapter(myViewPagerAdapter);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getsubcat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = getLayoutInflater();



            View view = layoutInflater.inflate(R.layout.subcategory_item, container, false);
            ((TextView) view.findViewById(R.id.name)).setText(about_title_array[position]);
            //((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);

            int image=drw_arr.getResourceId(position, -1);

            Tools.displayImageOriginal(getApplicationContext(), ((ImageView) view.findViewById(R.id.image)), image);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}
