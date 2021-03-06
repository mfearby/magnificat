package com.marcfearby.utils;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/* This is probably a naughty name for a class, but what are you gonna do? ;-) */
public class Global {

    private static FileSystem testFileSystem = null;
    private static boolean testMode = false;
    public static String TESTING_PATH_HOME = "/Users/Magnificat";


    public static void setTestMode() {
        testMode = true;
    }


    /**
     * Get the current file system in effect (default for normal use or Jimfs for testing)
     * @return A FileSystem object
     */
    public static FileSystem getFileSystem() {
        if (testMode) {
            if (testFileSystem == null) {
                System.out.println("Creating new Jimfs...");
                testFileSystem = Jimfs.newFileSystem(Configuration.unix());
            }
            return testFileSystem;
        } else {
            return FileSystems.getDefault();
        }
    }

    /**
     * Overwrites any existing filesystem with an empty Jimfs file system for a testing clean-slate
     */
    public static void resetTestFileSystem() {
        System.out.println("Resetting Jimfs...");
        testFileSystem = Jimfs.newFileSystem(Configuration.unix());
    }


    public static Path getUserHomeFolder() {
        FileSystem fs = getFileSystem();
        // Note: Don't use Paths.get() because it is hard wired to the default file system
        if (testMode) {
            return fs.getPath(TESTING_PATH_HOME);
        } else {
            return fs.getPath(System.getProperty("user.home"));
        }
    }


}
