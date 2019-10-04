package com.cse4471;

import brut.androlib.AndrolibException;
import s3.fastapk.APKContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, AndrolibException {
        try {
            String path = "C:\\Users\\Michael\\IdeaProjects\\UntrackaBLE\\src\\com\\cse4471\\Strings";
            Scanner strings = new Scanner(new File(path));
            String str1 = strings.newLine();

            String appPath = "C:\\Users\\Michael\\IdeaProjects\\UntrackaBLE\\testApps\\Canvas.apk";
            try {
                APKContext apk = new APKContext(appPath, true);
                System.out.println(apk.getManifest().getAppPackage());
                HashMap<String, String> sms = apk.getSmalis();

                for (Map.Entry<String, String> clsName : sms.entrySet()) {
                    System.out.println(clsName.getKey()); //
                    System.out.println(clsName.getValue());
                    System.out.println();
                }
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: The apk file is missing!");
            }

            strings.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Strings file is missing!");
        }


    }
}
