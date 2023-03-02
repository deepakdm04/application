package com.example.demo.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.demo.Activity.SplashScreen;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

import static com.rvalerio.fgchecker.Utils.hasUsageStatsPermission;
import static com.rvalerio.fgchecker.Utils.postLollipop;


/**
 * Created by Android-App on 8/6/2016.
 */
public class common {
    public static int month_lead = 0;
    public static int potential_id = 0;
    public static double latitude = 0.0, longitude = 0.0;
    String name, idToken, loginGid, loginBid, refreshtoken, accesstoken, sessiontime, email, gidcomparing, flag, taskmodule, reportingtoNo, module, reportingto;
    String IMEIno, phone, designation;
    public static String str_name = "";
    public static String str_email = "";
    public static String str_company = "";
    public static String str_entiy = "";
    public static String str_bussine = "";
    public static String str_inst = "";
    public static String str_phone = "";
    public static String str_request = "";
    public static String str_quail = "";
    public static String str_lead = "";
    public static String str_followup = "";
    public static String descre = "";
    public static String check_add = "";
    public static String gid = "0";
    public static String bid = "0";
    public static String reporting_to = "0";
    public static String phonenum = "0";
    public static String color = "";
    public static String unknown = "";
    public static String callemail = "";
    public static String callgid = "";
    public static double lat;
    static SharedPreferences Empid_share;
    public static final String Empid_de = "Empid_details";
    SharedPreferences Login_share;
    public static final String Login_de = "Login_details";
    static SharedPreferences Location_share;
    public static final String Location_de = "Location_details";
    private static DevicePolicyManager mDevicePolicyManager;
    private static ComponentName mComponentName;

    public static String unknownMethod(String unknw, String email, String gid) {
        unknown = unknw;
        callemail = email;
        callgid = gid;
        return unknw;
    }

    public static boolean isValidEmail(String email)
    {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static ArrayAdapter<String> apdapter_service(Context context2, ArrayList<String> status) {
        String d;
        ArrayAdapter<String> statusadapter = new ArrayAdapter<String>(context2, android.R.layout.simple_spinner_item, status) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setTextSize(17);
                ((TextView) v).setPadding(-2, 0, 0, 0);
                if (position == 0) {
                    ((TextView) v).setTextColor(Color.parseColor("#808080"));
                }
                return v;
            }

            public boolean isEnabled(int position) {

                if (position == 0) {
                    return false;
                }
                return true;
            }

            @SuppressLint("RtlHardcoded")
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(v.getDrawingCacheBackgroundColor());
                ((TextView) v).setGravity(Gravity.LEFT);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setPadding(10, 10, 10, 10);

                if (position == 0) {
                    v.setBackgroundColor(Color.GRAY);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTextColor(Color.WHITE);
                    ((TextView) v).setTextSize(17);

                }

                return v;
            }
        };
        return statusadapter;
    }


    public static void sampleintent(Context context2, Class<?> class1) {
        Intent in = new Intent(context2, class1);
        ((Activity) context2).finish();
        context2.startActivity(in);

    }

    public static void showtoast(String string, Context context) {
        try {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void requestFocus(View view, Window window) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    public void getLocation() {
        Log.v("raja","hello");

    }
    public static double getLatitude(Context applicationContext, int tt) {
        double lk=0;
               if(tt==1){
              lk =common.latitude;
            }else if(tt==2){
              lk=common.longitude;
            }
        return lk;
    }


    public static ArrayAdapter<String> apdapter_workType(final Context context2, ArrayList<String> status) {
        String d;
        ArrayAdapter<String> statusadapter = new ArrayAdapter<String>(context2, android.R.layout.simple_spinner_item, status) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setTextSize(17);
                ((TextView) v).setPadding(-2,0,0,0);
               if(position == 0) {
                   //((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                  // ((TextView) v).setTextColor(context2.getResources().getColor(R.color.colorPrimary));
                }
                return v;
            }
            public boolean isEnabled(int position){

                if (position == 0 ) {
                    return false;
                }
                return true;
            }

            @SuppressLint("RtlHardcoded")
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(v.getDrawingCacheBackgroundColor());
                ((TextView) v).setGravity(Gravity.LEFT);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setPadding(10,10,10,10);

             if (position == 0) {
                    //v.setBackgroundColor(Color.GRAY);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                    ((TextView) v).setTextSize(17);

                }

                return v;
            }
        };
        return statusadapter;
    }
    public static ArrayAdapter<String> apdapter_docType(final Context context2, ArrayList<String> status) {
        String d;
        ArrayAdapter<String> statusadapter = new ArrayAdapter<String>(context2, android.R.layout.simple_spinner_item, status) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setTextSize(17);
                ((TextView) v).setPadding(-2,0,0,0);
               if(position == 0) {
                   //((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                  // ((TextView) v).setTextColor(context2.getResources().getColor(R.color.colorPrimary));
                }
                return v;
            }
            public boolean isEnabled(int position){

                if (position == 0 ) {
                    return false;
                }
                return true;
            }

            @SuppressLint("RtlHardcoded")
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(v.getDrawingCacheBackgroundColor());
                ((TextView) v).setGravity(Gravity.LEFT);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setPadding(10,10,10,10);

             if (position == 0) {
                    //v.setBackgroundColor(Color.GRAY);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                    ((TextView) v).setTextSize(17);

                }

                return v;
            }
        };
        return statusadapter;
    }
    public static ArrayAdapter<String> apdapter_sales(final Context context2, ArrayList<String> status) {
        String d;
        ArrayAdapter<String> statusadapter = new ArrayAdapter<String>(context2, android.R.layout.simple_spinner_item, status) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setTextSize(17);
                ((TextView) v).setPadding(-2,0,0,0);
            if(position == 0) {
               /* ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
               ((TextView) v).setTextColor(context2.getResources().getColor(R.color.colorPrimary));
                    ((TextView) v).setTextColor(Color.parseColor("#808080"));*/
                }
                return v;
            }

            public boolean isEnabled(int position){

                if (position == 0 ) {
                    return false;
                }
                return true;
            }

            @SuppressLint("RtlHardcoded")
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(v.getDrawingCacheBackgroundColor());
                ((TextView) v).setGravity(Gravity.LEFT);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setPadding(10,10,10,10);

                if (position == 0) {
                   // v.setBackgroundColor(Color.GRAY);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                   // ((TextView) v).setTextColor(Color.WHITE);
                    ((TextView) v).setTextSize(17);

                }

                return v;
            }
        };
        return statusadapter;
    }

    public static ArrayAdapter<String> apdapter_returnhome(Context context2, ArrayList<String> status) {
        String d;
        ArrayAdapter<String> statusadapter = new ArrayAdapter<String>(context2, android.R.layout.simple_spinner_item, status) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setTextSize(17);
                ((TextView) v).setPadding(-2,0,0,0);
                if(position == 0) {
                    ((TextView) v).setTextColor(Color.parseColor("#808080"));
                }
                return v;
            }

            public boolean isEnabled(int position){

                if (position == 0 ) {
                    return false;
                }
                return true;
            }

            @SuppressLint("RtlHardcoded")
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(v.getDrawingCacheBackgroundColor());
                ((TextView) v).setGravity(Gravity.LEFT);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setPadding(10,10,10,10);

                if (position == 0) {
                    v.setBackgroundColor(Color.GRAY);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTextColor(Color.WHITE);
                    ((TextView) v).setTextSize(17);

                }

                return v;
            }
        };
        return statusadapter;
    }
    public  static View view(Activity activity){

        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        return  decorView;
    }

    public static ArrayAdapter<String> apdapter_payment(Context context2, ArrayList<String> status) {
        String d;
        ArrayAdapter<String> statusadapter = new ArrayAdapter<String>(context2, android.R.layout.simple_spinner_item, status) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setTextSize(17);
                ((TextView) v).setPadding(-2,0,0,0);
               if(position == 0) {
                   ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                    //((TextView) v).setTextColor(Color.parseColor("#808080"));
                }
                return v;
            }
            public boolean isEnabled(int position){
                if (position == 0 ) {
                    return false;
                }
                return true;
            }
            @SuppressLint("RtlHardcoded")
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(v.getDrawingCacheBackgroundColor());
                ((TextView) v).setGravity(Gravity.LEFT);
                ((TextView) v).setTextColor(Color.BLACK);
                ((TextView) v).setPadding(10,10,10,10);
              if (position == 0) {
                   // v.setBackgroundColor(Color.GRAY);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                    //((TextView) v).setTextColor(Color.WHITE);
                    ((TextView) v).setTextSize(17);
                }
                return v;
            }
        };
        return statusadapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkcondition(Context context) {

        mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, Admin.class);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        final String currentHomePackage = resolveInfo.activityInfo.packageName;

        return mDevicePolicyManager != null &&
                mDevicePolicyManager.isAdminActive(mComponentName) &&
                !needsUsageStatsPermission(context) &&
                currentHomePackage.equals("com.example.demo") /*&&
                Settings.canDrawOverlays(context)*/;
    }

    private static boolean needsUsageStatsPermission(Context context) {
        return postLollipop() && !hasUsageStatsPermission(context);
    }

}
