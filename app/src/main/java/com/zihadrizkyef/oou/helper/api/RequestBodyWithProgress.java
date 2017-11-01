package com.zihadrizkyef.oou.helper.api;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 15/09/17.
 */

public class RequestBodyWithProgress extends RequestBody {
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private MediaType contentType;
    private File mFile;
    private UploadCallbacks mListener;
    private String text;

    public RequestBodyWithProgress(MediaType contentType, final File file, final UploadCallbacks listener, String text) {
        this.contentType = contentType;
        mFile = file;
        mListener = listener;
        this.text = text;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                handler.post(new ProgressUpdater(uploaded, fileLength, text));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage, String text);
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded = 0;
        private long mTotal = 1;

        public ProgressUpdater(long uploaded, long total, String text) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate((int) (100 * mUploaded / mTotal), text);
        }
    }
}