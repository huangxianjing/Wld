package com.wld.net.http;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by syyp on 15/5/6.
 */
public class DownloadTask implements Runnable {
    private long mTotalSize;
    private int mDownloadPercent;
    private String mSaveFolder;
    private String mSavePath;
    private String mURL;

    int count;

    OnDownloadProgressListener mDownloadListener;
    boolean IsDownload = true;


    InputStream inStream = null;
    RandomAccessFile randomAccessFile = null;

    public DownloadTask(Context context, String url, String savePath) {
        this.mSaveFolder = savePath;
        mSavePath = mSaveFolder+"app.apk";
        this.mURL = url;
    }
    @Override
    public void run()
    {
        download();
    }
    ////下载方法
    protected boolean download()
    {
        File file = new File(mSavePath);
        if (file.exists())
        {
            file.delete();
        }

        File folder = new File(mSaveFolder);
        if(!folder.exists())
        {
            folder.mkdirs();
        }

        HttpURLConnection conection = null;
        URL url = null;
        try {
            url = new URL(mURL);
            conection = (HttpURLConnection) url.openConnection();
            conection.connect();
            mTotalSize = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while(IsDownload)
            {
                if ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    mDownloadPercent = (int)((total*100)/mTotalSize);
                    this.mDownloadListener.OnDownProgress(mDownloadPercent);


                    // writing data to file
                    output.write(data, 0, count);
                }
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public void StopDownload()
    {
        IsDownload = false;
    }


    public interface OnDownloadProgressListener
    {
        public void OnDownProgress(int percent);
    }


    public void setDownLoadProgressListener(OnDownloadProgressListener onDownloadListener)
    {
        this.mDownloadListener = onDownloadListener;
    }


}
