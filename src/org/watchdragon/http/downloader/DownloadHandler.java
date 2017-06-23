package org.watchdragon.http.downloader;

import org.apache.http.impl.client.CloseableHttpClient;
import org.watchdragon.http.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class DownloadHandler {
    private long threadSize;
    private Logger logger = Logger.getLogger("DownloadHandler");
    private CloseableHttpClient httpClient;
    protected String url;
    protected String file;
    protected int maxThread;
    protected int currentThread;
    protected boolean useDS;
    public DownloadHandler (String url,String localFile){
        this(url,localFile,20,true);
    }

    public DownloadHandler(String url,String localFile,int maxThread,boolean useDS){
        this.url=url;
        this.file=localFile;
        this.maxThread=maxThread;
        this.currentThread=0;
        this.useDS=useDS;
    }

    public void startDownload() throws IOException {
        logger.log(Level.INFO,"Start handler");
        String remoteFileName = new URL(this.url).getFile();
        logger.log(Level.INFO,"RemoteUrl:"+remoteFileName+",LocalFilePath:"+this.file);
        long fileSize = this.getRemotFileLength();
        logger.log(Level.INFO,"FileSize:"+fileSize);
    }

    private long getRemotFileLength() throws IOException {
        HttpUtils http = new HttpUtils(this.url);
        return Long.parseLong(http.head().getHeader("Content-Length").get(0));
    }
}
