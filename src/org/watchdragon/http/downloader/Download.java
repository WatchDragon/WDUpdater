package org.watchdragon.http.downloader;

import java.io.IOException;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class Download {
    public static void downloadFile(String url,String path) throws IOException {
        DownloadHandler downloadHandler =new DownloadHandler(url,null);
        downloadHandler.startDownload();
    }

}
