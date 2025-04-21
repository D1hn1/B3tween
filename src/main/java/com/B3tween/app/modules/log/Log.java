package com.B3tween.app.modules.log;

import java.time.LocalDateTime;

public class Log {
    
    private static String RED     = "\u001B[31m";
    private static String GREEN   = "\u001B[32m";
    private static String BLUE   = "\u001B[34m";
    private static String SUBLINE = "\u001B[4m";
    private static String END     = "\u001B[0m";
    private static String TAB     = "\t";

    private static LocalDateTime time = LocalDateTime.now();

    /**
     * Error function
     * @return void
     */
    public static void e(String message) {
        System.err.println(RED + time + TAB + "| " +
                            message + END);
    }

    /**
     * Log function
     * @return void
     */
    public static void l(String message) {
        System.out.println(GREEN + time + TAB + "| " +
                            message + END);
    }

    /**
     * Critical function
     * @return void
     */
    public static void c(String message) {
        System.err.println(RED + time + TAB + "| " +
                            SUBLINE + message + END);
    }

    /**
     * Informative function
     * @return void
     */
    public static void i(String message) {
        System.out.println(BLUE + time + TAB + "| " +
                            message + END);
    }
}
