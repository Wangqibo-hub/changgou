package com.changgou.util;

import com.changgou.file.FastDFSFile;
import com.thoughtworks.xstream.core.util.FastField;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-10 14:25
 * @description
 */
public class FastDFSClient {

    //初始化tracker信息
    static {
        try {
            //获取tracker的配置文件的信息
            String path = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker配置信息
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description: 文件上传
    * @Param: [fastDFSFile]
    * @Return: java.lang.String[]
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public static String[] upload(FastDFSFile fastDFSFile){
        //获取文件作者
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair(fastDFSFile.getAuthor());

        //文件上传后的返回值
        String[] uploadResults = null;
        try {
            //获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            //执行文件上传
            uploadResults = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadResults;
    }

    /**
    * @Description: 获取文件信息
    * @Param: [groupName, remoteFileName]
    * @Return: org.csource.fastdfs.FileInfo
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public static FileInfo getFileInfo(String groupName,String remoteFileName){
        FileInfo file_info = null;
        try {
            //获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            //获取文件信息
            file_info = storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_info;
    }

    /**
    * @Description: 文件下载
    * @Param: [groupName, remoteFileName]
    * @Return: java.io.InputStream
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public static InputStream downloadFile(String groupName,String remoteFileName){
        try {
            //创建StorageClient
            StorageClient storageClient = getStorageClient();
            //通过StorageClient下载文件
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            //将字节数组转换成字节输入流
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @Description: 文件删除
    * @Param: [groupName, remoteFileName]
    * @Return: void
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public static void deleteFile(String groupName,String remoteFileName){
        try {
            //获取StorageClient
            StorageClient storageClient = getStorageClient();
            //通过StorageClient删除文件
            storageClient.delete_file(groupName,remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description: 获取组信息
    * @Param: [groupName]
    * @Return: org.csource.fastdfs.StorageServer
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public static StorageServer getStorage(String groupName){
        StorageServer storeStorage = null;
        try {
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient获取TrackerServer对象
            TrackerServer trackerServer = trackerClient.getConnection();
            //通过trackerClient获取Storage组信息
            storeStorage = trackerClient.getStoreStorage(trackerServer, groupName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return storeStorage;
    }

    /**
    * @Description: 根据文件组名和文件存储路径获取Storage服务的IP、端口信息
    * @Param: [groupName, remoteFileName]
    * @Return: org.csource.fastdfs.ServerInfo[]
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public ServerInfo[] getServerInfo(String groupName, String remoteFileName){
        ServerInfo[] fetchStorages = null;
        try {
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient获取TrackerServer对象
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取服务信息
            fetchStorages = trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fetchStorages;
    }

    /**
    * @Description: 获取Tracker服务地址
    * @Param: []
    * @Return: java.lang.String
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    public static String getTrackerUrl(){
        String url = null;
        try {
            //获取TrackerServer对象
            TrackerServer trackerServer = getTrackerServer();
            //获取tracker地址
            url = "http://"+trackerServer.getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    //获取TrackerServer
    private static TrackerServer getTrackerServer() throws Exception {
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获取TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    //获取StorageClient
    private static StorageClient getStorageClient() throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();
        //通过TrackerServer创建StorageClient
        StorageClient storageClient = new StorageClient(trackerServer,null);
        return storageClient;
    }
}
