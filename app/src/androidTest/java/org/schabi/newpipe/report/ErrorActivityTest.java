package org.schabi.newpipe.report;

import android.content.Context;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ErrorActivityTest {

    // Android context generated as required
    private Context context = ApplicationProvider.getApplicationContext();

    private static ErrorActivity errorActivity;

    @BeforeClass
    public static void instantiateErrorActivity() {
        // Looper thread creation necessary to use ErrorActivity
        Looper.prepare();
        errorActivity = new ErrorActivity();
    }

    /**
     * Tests that the new getStackTrace() function is both accessible, and returns the correct
     * results for a given Exception.
     */
    @Test
    public void getStackTraceTest() {

        final Exception exception = new Exception();

        final String expectedStackTraceStart = "java.lang.Exception\n\tat "
                + "org.schabi.newpipe.report.ErrorActivityTest.getStackTraceTest"
                + "(ErrorActivityTest.java:41)\n\tat java.lang.reflect.Method.invoke(Native Method)"
                + "\n\tat org.junit.runners.model.FrameworkMethod$1"
                + ".runReflectiveCall(FrameworkMethod.java:50)";

        // assert getStackTrace returns the correct stack trace given an Exception
        // (based on first 4 lines)
        assertEquals(expectedStackTraceStart, errorActivity.getStackTrace(exception).substring(0,
                expectedStackTraceStart.length()));
    }

    /**
     * Tests that the new elTosl() function is both accessible, and returns the correct
     * results for a given list of Throwables.
     */
    @Test
    public void elToSlTest() {

        final List<Throwable> throwables = new ArrayList<>();
        throwables.add(new Exception());
        throwables.add(new IOException());

        final String expectedStackTraceStart1 = "java.lang.Exception\n\tat "
                + "org.schabi.newpipe.report.ErrorActivityTest.elToSlTest"
                + "(ErrorActivityTest.java:63)\n\tat java.lang.reflect.Method.invoke(Native Method)"
                + "\n\tat org.junit.runners.model.FrameworkMethod$1"
                + ".runReflectiveCall(FrameworkMethod.java:50)";

        final String expectedStackTraceStart2 = "java.io.IOException\n\tat "
                + "org.schabi.newpipe.report.ErrorActivityTest.elToSlTest"
                + "(ErrorActivityTest.java:64)\n\tat java.lang.reflect.Method.invoke(Native Method)"
                + "\n\tat org.junit.runners.model.FrameworkMethod$1"
                + ".runReflectiveCall(FrameworkMethod.java:50)";

        // assert elToSl returns the correct string representation of the two throwables added to
        // the list. (based on the first 4 lines)
        assertEquals(expectedStackTraceStart1, errorActivity.elToSl(throwables)[0].substring(0,
                expectedStackTraceStart1.length()));
        assertEquals(expectedStackTraceStart2, errorActivity.elToSl(throwables)[1].substring(0,
                expectedStackTraceStart2.length()));
    }

    /**
     * Tests that the new formErrorText() function is both accessible, and returns the correct
     * results for a given list of error strings.
     */
    @Test
    public void formErrorTextTest() {

        final List<Throwable> throwables = new ArrayList<>();
        throwables.add(new Exception());
        throwables.add(new IOException());

        final String[] errorList = errorActivity.elToSl(throwables);

        final String expectedStackTraceStart = "-------------------------------------\n"
                + "java.lang.Exception\n\tat "
                + "org.schabi.newpipe.report.ErrorActivityTest.formErrorTextTest"
                + "(ErrorActivityTest.java:94)\n\tat java.lang.reflect.Method.invoke(Native Method)"
                + "\n\tat org.junit.runners.model.FrameworkMethod$1"
                + ".runReflectiveCall(FrameworkMethod.java:50)";

        final String formErrorTextOutput = errorActivity.formErrorText(errorList);

        // assert the string returned by formErrorText() correctly adds the dotted lines to
        // the output and contains the correct Exception output
        assertEquals(expectedStackTraceStart, formErrorTextOutput.substring(0,
                expectedStackTraceStart.length()));
        final String substring = formErrorTextOutput.substring(formErrorTextOutput.length() - 37);
        assertEquals("-------------------------------------", substring);

    }

}
