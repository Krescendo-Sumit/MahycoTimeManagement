package com.mahyco.time.timemanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

public class Messageclass {



        private Context context;
        private Boolean fg=false;
        public Messageclass(Context context)
        {
            this.context=context;
        }


    public void showMessage(String message) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            // Setting Dialog Title
            alertDialog.setTitle("Mahyco");
            // Setting Dialog Message
            alertDialog.setMessage(message);
            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.tick);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        public boolean showMessageConfirm(String message)
        {

            // fg=false;
            // AlertDialog alertDialog = new AlertDialog.Builder(VisitorInformation.this).create();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle("Mahyco");
            // Setting Dialog Message
            alertDialog.setMessage(message);
            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.tick);
            // Setting OK Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do do my action here
                    fg=true;
                    dialog.dismiss();

                }

            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // I do not need any action here you might
                    fg=false;
                    dialog.dismiss();

                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();
            return fg;
        }

    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }

    public void showNoInternet() {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle("Mahyco");
        // Setting Dialog Message
        alertDialog.setMessage("Internet is Not Available");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_info);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
