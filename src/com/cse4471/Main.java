package com.cse4471;

import brut.androlib.AndrolibException;
import s3.fastapk.APKContext;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    /**
     * Looks at the manifest from an apk file to determine if it has permissions to access
     * BLE beacons.
     * The permissions to look for are:
     *      <uses-permission android:name="android.permission.BLUETOOTH"/>
     *      <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     * Additionally, if the app has this permission it can turn Bluetooth on all on it's own:
     *      <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
     * @param apk the manifest of the apk file being analyzed
     * @return
     */
    static Boolean hasPerms(APKContext apk){

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
     * @param apk the app being analyzed
     * @return whether the app makes use of BLE scanning for location access
     */
    static Boolean checkNativeBLE(APKContext apk){
        Boolean out = false;
        // TODO: Code this function
        return out;
    }

    static Boolean checkFacebookBLE(APKContext apk){
        Boolean out = false;
        // TODO: Code this function
        return out;
    }

    public static void main(String[] args) throws IOException, AndrolibException {
        try {
            // Load in the file that contains the strings we are looking out for
            URL stringsPath = Main.class.getResource("Strings");
            Scanner strings = new Scanner(new File(stringsPath.getFile()));

            // Create a file output stream for the generated report
            PrintWriter report = new PrintWriter(new FileWriter("report.txt"));
            try {
                // Load the apk file being examined
                APKContext apk = new APKContext("C:\\Users\\Michael\\IdeaProjects\\UntrackaBLE\\src\\com\\cse4471\\testApps\\Canvas.apk", true);

                // Print out the app package name
                report.println(apk.getManifest());
                Boolean foundBLE = false;

                // Check if the app uses the right permissions, if not we don't care
                Boolean hasPerms = checkPerms(apk.getManifest());

                if(hasPerms) {
                    // Get the Smali code files
                    HashMap<String, String> sms = apk.getSmalis();

                    // Search for native usage of BLE
                    foundBLE = checkNativeBLE(apk);

                    // Search for usage of Facebook's BLE API
                    if (foundBLE) {
                        foundBLE = checkFacebookBLE(apk);
                    }

                    /*
                    for (Map.Entry<String, String> clsName : sms.entrySet()) {
                        report.println("--------------------------------------------------------------------------");
                        report.println(clsName.getKey()); //
                        report.println(clsName.getValue());
                        report.println("--------------------------------------------------------------------------");
                    }
                     */
                }
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: The apk file is missing!");
                // Close the output stream if an error was encountered
                report.close();
            }

            strings.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Strings file is missing!");
        }

        // Inform the user that the program has completed running
        System.out.println("Process Complete");

    }
}
