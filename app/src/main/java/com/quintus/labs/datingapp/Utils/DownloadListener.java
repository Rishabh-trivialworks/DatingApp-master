package com.quintus.labs.datingapp.Utils;

public interface DownloadListener {
    public void onDownLoadSuccess(String filePath);
    public void onDownLoadFail(String reason);

}
