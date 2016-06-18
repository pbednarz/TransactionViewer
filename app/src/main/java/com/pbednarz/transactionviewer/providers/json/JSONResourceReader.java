package com.pbednarz.transactionviewer.providers.json;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * An object for reading from a JSON resource file and constructing an object from that resource file using Gson.
 * Created by pbednarz on 2016-06-18.
 */
public class JSONResourceReader {

    private final Gson gson;
    private final Resources resources;

    public JSONResourceReader(@NonNull Gson gson, @NonNull Resources resources) {
        this.gson = gson;
        this.resources = resources;
    }

    public <T> T readFromRaw(@RawRes int id, Type type) throws JsonSyntaxException {
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
            Timber.e(e, "Unhandled exception while using JSONResourceReader");
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Timber.e(e, "Unhandled exception while using JSONResourceReader");
            }
        }

        String jsonString = writer.toString();
        return gson.fromJson(jsonString, type);
    }
}