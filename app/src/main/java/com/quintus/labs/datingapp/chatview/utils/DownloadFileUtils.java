package com.quintus.labs.datingapp.chatview.utils;


import com.downloader.Error;
import com.downloader.OnDownloadListener;

import com.downloader.PRDownloader;
import com.downloader.utils.Utils;
import com.quintus.labs.datingapp.Utils.DownloadListener;

public class DownloadFileUtils {
    public static int downloadFile(String url, String dirPath, String fileName, DownloadListener listener){
       int  downloadIdOne = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(() -> {

                })
                .setOnPauseListener(() -> {
                 listener.onDownLoadFail("Unable to download");
                })
                .setOnCancelListener(() -> {
                    listener.onDownLoadFail("Unable to download");

                })
                .setOnProgressListener(progress -> {

                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        final String path = Utils.getPath(dirPath, fileName);
                        listener.onDownLoadSuccess(path);

                    }

                    @Override
                    public void onError(Error error) {
                        listener.onDownLoadFail("Unable to download");

                    }
                });

        return downloadIdOne;
    }
}
