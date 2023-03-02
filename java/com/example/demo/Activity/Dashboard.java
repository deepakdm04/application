package com.example.demo.Activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.demo.Adapter.AdapterListCategories;
import com.example.demo.Adapter.AdapterListDashboard;
import com.example.demo.R;
import com.example.demo.Service.BackgroundService;
import com.example.demo.model.Constants;
import com.example.demo.model.category_model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    Handler collapseNotificationHandler;
    boolean currentFocus, isPaused;
    Toolbar toolbar;
    View breakfast_view,lunch_view,dinner_view,snacks_view;
    SharedPreferences permission_share;
    public String permission_det = "permission_details";
    SharedPreferences Login_share;
    public String Login_de = "Login_details";
    SharedPreferences.Editor login_edit;
    SharedPreferences Demo_share;
    public String Demo_de = "Demo_details";
    SharedPreferences.Editor demo_edit;
    ArrayList<category_model> dashboard_Arr=new ArrayList<>();
    String listtype="0";
    int list_pos=0;
    AlertDialog.Builder builder;
    String[] list={"Grid","List"};
    AdapterListDashboard mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        builder=new AlertDialog.Builder(Dashboard.this);

        breakfast_view=findViewById(R.id.breakfast_mat);
        lunch_view=findViewById(R.id.lunch_mat);
        snacks_view=findViewById(R.id.snacks_mat);
        dinner_view=findViewById(R.id.dinner_mat);
        permission_share = getApplicationContext().getSharedPreferences(permission_det, Context.MODE_PRIVATE);
        Login_share = getApplicationContext().getSharedPreferences(Login_de, Context.MODE_PRIVATE);
        Demo_share = getApplicationContext().getSharedPreferences(Demo_de,Context.MODE_PRIVATE);
        login_edit=Login_share.edit();
        demo_edit=Demo_share.edit();

        Intent i = new Intent(Dashboard.this, BackgroundService.class);
        i.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(i);

        initToolbar();

        initComponent();

    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void initComponent() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        TypedArray drw_arr = getResources().obtainTypedArray(R.array.lunch_img);
        String name_arr[] = getResources().getStringArray(R.array.lunch);

        dashboard_Arr.clear();
        for (int i = 0; i < drw_arr.length(); i++) {
            category_model obj = new category_model();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            dashboard_Arr.add(obj);
        }
        //set data and list adapter


        listtype=Demo_share.getString("display","0");

        if(listtype.equals("1"))
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            mAdapter = new AdapterListDashboard(this, dashboard_Arr,listtype);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            invalidateOptionsMenu();
        }
        else
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.setHasFixedSize(true);
            mAdapter = new AdapterListDashboard(this, dashboard_Arr,listtype);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            invalidateOptionsMenu();
        }



        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListDashboard.OnItemClickListener() {
            @Override
            public void onItemClick(View view, category_model obj, int position) {
                /*Snackbar.make(parent_view, "Item " + position + " clicked", Snackbar.LENGTH_SHORT).show();*/
                MoveNext(obj.name);

            }
        });

    }



    @Override
    public void onClick(View view) {
        int getId=view.getId();

        switch (getId)
        {
            case R.id.breakfast_mat:
                MoveNext("breakfast");
                break;
            case R.id.lunch_mat:
                MoveNext("lunch");
                break;
            case R.id.dinner_mat:
                MoveNext("dinner");
                break;
            case R.id.snacks_mat:
                MoveNext("snacks");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        MenuItem griditem=menu.findItem(R.id.actionGrid);
        MenuItem listitem=menu.findItem(R.id.actionList);
        listitem.setVisible(true);
        griditem.setVisible(false);
        if(listtype.equals("1"))
        {
            griditem.setVisible(true);
            listitem.setVisible(false);
        }
        else
        {
            griditem.setVisible(false);
            listitem.setVisible(true);
        }
        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setVisibility(View.GONE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(mAdapter!=null) {
                    if (s.length() > 1) {
                        mAdapter.getFilter().filter(s);
                    } else {
                        mAdapter.getFilter().filter("");
                    }

                }
                else
                {
                    Toast.makeText(Dashboard.this,"No data in the list",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(mAdapter!=null) {
                    if (s.length() > 1) {
                        mAdapter.getFilter().filter(s);
                    } else {
                        mAdapter.getFilter().filter("");
                    }

                }
                else
                {
                    Toast.makeText(Dashboard.this,"No data in the list",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.actionLogout)
        {
            Intent intent=new Intent(Dashboard.this, LogoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            SharedPreferences.Editor perEdit1 = permission_share.edit();
            perEdit1.putString("permissions", "false");
            perEdit1.putString("setting", "true");
            perEdit1.apply();
            startActivity(intent);
        }
        else if(id==R.id.actionList)
        {
            list();
        }
        else if(id==R.id.actionGrid)
        {
            list();
        }


        return super.onOptionsItemSelected(item);
    }

    public void MoveNext(String category)
    {
        Log.d("TestTag","aaaaa");
        Intent intent=new Intent(Dashboard.this,SubCategories.class);
        intent.putExtra("subcat",category);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        currentFocus = hasFocus;
        if (!hasFocus) {
            // Method that handles loss of window focus
            collapseNow();
        }
    }

    public void collapseNow() {
        // Initialize 'collapseNotificationHandler'
        if (collapseNotificationHandler == null) {
            collapseNotificationHandler = new Handler();
        }
        if (!currentFocus && !isPaused) {
            // Post a Runnable with some delay - currently set to 300 ms
            collapseNotificationHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Use reflection to trigger a method from 'StatusBarManager'
                    @SuppressLint("WrongConstant") Object statusBarService = getSystemService("statusbar");
                    Class<?> statusBarManager = null;
                    try {
                        statusBarManager = Class.forName("android.app.StatusBarManager");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Method collapseStatusBar = null;
                    try {
                        // Prior to API 17, the method to call is 'collapse()'
                        // API 17 onwards, the method to call is `collapsePanels()`
                        collapseStatusBar = statusBarManager.getMethod("collapsePanels");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    collapseStatusBar.setAccessible(true);
                    try {
                        collapseStatusBar.invoke(statusBarService);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    // Check if the window focus has been returned
                    // If it hasn't been returned, post this Runnable again
                    // Currently, the delay is 100 ms. You can change this
                    // value to suit your needs.
                    if (!currentFocus && !isPaused) {
                        collapseNotificationHandler.postDelayed(this, 100L);
                    }

                }
            }, 300L);
        }
    }

    public void list()
    {
        String getpos=Demo_share.getString("display","0");
        if(getpos.equals("0"))
        {
            list_pos=0;
        }
        else
        {
            list_pos=1;
        }

        builder.setCancelable(true);
        builder.setSingleChoiceItems(list,list_pos, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                if(position==0)
                {
                    demo_edit.putString("display","0");
                    demo_edit.commit();
                    initComponent();
                }
                else if(position==1)
                {
                    demo_edit.putString("display","1");
                    demo_edit.commit();
                    initComponent();
                }

                list_pos=position;

                dialogInterface.dismiss();

            }
        });
        builder.show();
    }

}
