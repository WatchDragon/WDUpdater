package org.watchdragon.http.downloader;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;

import javax.xml.ws.spi.http.HttpContext;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class DownloadThread {
    private Logger logger = Logger.getLogger("DownloadThread");
    private String url;
    private String fileName;
    private long offset;
    private long length;
    private CountDownLatch onDownloadFinish;
    private CloseableHttpClient httpClient;
    private BasicHttpContext context;
    private int ID;

    public DownloadThread(int id,String url,String file,long offset,long length,CountDownLatch onDownloadFinish,CloseableHttpClient httpClient){
        this.ID = id;
        this.url=url;
        this.fileName=file;
        this.offset=offset;
        this.length=length;
        this.onDownloadFinish=onDownloadFinish;
        this.httpClient=httpClient;
        this.context= new BasicHttpContext();
    }

    public void start() {
        try {
            logger.log(Level.INFO,"Thread "+this.ID + "Start");
            HttpGet httpGet = new HttpGet(this.url);
            httpGet.addHeader("Range", "bytes=" + this.offset + "-" + (this.offset + this.length - 1));
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
            byte[] buffer = new byte[1024];
            int byteReadied;
            File file = new File(this.fileName);
            RandomAccessFile randomAccessFile =  new RandomAccessFile(file, "rw");
            logger.log(Level.INFO,"Thread "+this.ID + "Reading file");
            while ((byteReadied = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.seek(this.offset);
                randomAccessFile.write(buffer, 0, byteReadied);
                this.offset += byteReadied;
            }
            randomAccessFile.close();
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"Thread "+this.ID + "Throws FileNotFound,download failed");
        } catch (ClientProtocolException e) {
            logger.log(Level.SEVERE,"Thread "+this.ID + "Throws ClientProtocolException,download failed");
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Thread "+this.ID + "Throws IOException,download failed");
        }
    }
}
