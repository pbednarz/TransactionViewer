package com.pbednarz.transactionviewer.providers;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * An object for reading from a JSON resource file and constructing an object from that resource file using Gson.
 */
public class JSONResourceReader {

    private static final String LOGTAG = JSONResourceReader.class.getSimpleName();
    private final Gson gson;
    private final Resources resources;

    public JSONResourceReader(@NonNull Gson gson, @NonNull Resources resources) {
        this.gson = gson;
        this.resources = resources;
    }

    public <T> T readFromRaw(@RawRes int id, Type type) {
        InputStream resourceReader = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
            }
        }

        String jsonString = writer.toString();
        return gson.fromJson(jsonString, type);
    }
}