package com.cse4471;

import brut.androlib.AndrolibException;
import s3.fastapk.APKContext;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    /**
     * Looks at the manifest from an apk file to determine if it has permissions to access
     * BLE beacons.
     * The permissions to look for are:
     *      <uses-permission android:name="android.permission.BLUETOOTH"/>
     *      <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     * Additionally, if the app has this permission it can turn Bluetooth on all on it's own:
     *      <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
     * @param perms An ArrayList of the permissions the app has access to
     * @return true when both required permissions are present, false otherwise
     */
    static Boolean checkPerms(ArrayList<String> perms){
        Boolean hasBluetooth = false;
        Boolean hasLocation = false;

        for(String perm : perms){
            if(perm.equals("android.permission.BLUETOOTH")){
                hasBluetooth = true;
            } else if(perm.equals("android.permission.ACCESS_FINE_LOCATION")){
                hasLocation = true;
            }
        }

        return hasBluetooth && hasLocation;
    }
    /***
     * This function will report on if the app uses Google's built-in code for connecting to
     * BLE beacons.
     *
     * Reference this page: https://developer.android.com/guide/topics/connectivity/bluetooth-le
     *
     * The app will need the BluetoothAdapter object
     * Look for the startLeScan() method
     * There will likely also be a stopLeScan() method since it saves battery
     *
     * @param sms the app being analyzed
     * @return whether the app makes use of BLE scanning for location access
     */
    static Boolean checkNativeBLE(HashMap<String, String> sms){
        // This string is the function call in smali form
        String function = "->startLEScan()";

        for(HashMap.Entry<String, String> smali : sms.entrySet()) {
            String file = smali.getValue();
            if(file.contains(function)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException, AndrolibException {
            try {
                // Load the apk file being examined
                String path = args[0]; // File path is the first argument
                APKContext apk = new APKContext(path, true);

                /* BLE SDKs we know about */
                Boolean nativeBLE = false;

                // Check if the app uses the right permissions, if not we don't care
                Boolean hasPerms = checkPerms(apk.getManifest().getPermissions());

                if(hasPerms) {
                    // Get the Smali code files
                    // Name mapped to content
                    HashMap<String, String> sms = apk.getSmalis();

                    /*
                        Check for each BLE SDK we know about
                     */
                    // Search for native usage of BLE
                    nativeBLE = checkNativeBLE(sms);
                    // Other BLE SDKs can be added here if they come about in the future

                    // Print out the first result: that perms are present for BLE scanning to take place
                    System.out.println("HAS PERMS");
                }
                // Go through the results, printing out what was found
                // This can be very simply tweaked to use a log file or some other solution as
                // some future research may need.
                if(nativeBLE){
                    System.out.println("FOUND NATIVE BLE");
                }
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: The apk file is missing!");
            }

        // Inform the user that the program has completed running
        System.out.println("Process Complete");

    }
}
