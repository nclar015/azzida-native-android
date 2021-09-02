package com.azzida.common;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.azzida.ui.activity.SplashScreen;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {

    /**
     * The Constant CRASH_REPORT.
     */
    public static final String CRASH_REPORT = "crashReport";
    /**
     * The my context.
     */
    private final Context myContext;

    /**
     * Instantiates a new exception handler.
     *
     * @param context the context
     */
    public ExceptionHandler(Context context) {
        myContext = context;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);// You can use LogCat too
        Intent intent = new Intent(myContext, SplashScreen.class);        //**
        intent.putExtra(CRASH_REPORT, stackTrace.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myContext.startActivity(intent);
        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}
