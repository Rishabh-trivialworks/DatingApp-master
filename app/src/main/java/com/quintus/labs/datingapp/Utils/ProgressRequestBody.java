package com.quintus.labs.datingapp.Utils;

import android.os.Handler;
import android.os.Looper;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by MyU10 on 1/11/2017.
 */

public class ProgressRequestBody extends RequestBody {
    private File mFile;
    private String mPath;
    private UploadCallbacks mListener;

    //    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    private long uploaded;
    private int fileLength;

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);

        void onError();

        void onFinish();
    }

    public ProgressRequestBody(final File file, final UploadCallbacks listener) {
        mFile = file;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
//        return MediaType.parse("image/*");
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        fileLength = in.available();
        LogUtils.debug("uploading file size " + fileLength / 1000 + " " + uploaded);
        uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                uploaded += read;
                // update onProgress on UI thread
                LogUtils.debug("uploading " + uploaded / 1000 + " of " + fileLength / 1000);
                handler.post(new ProgressUpdater(uploaded, fileLength));
                sink.write(buffer, 0, read);
//                sink.flush();
//                sink.write(buffer);
                sink.emit();
            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate((int) (100 * mUploaded / mTotal));
        }
    }
}
