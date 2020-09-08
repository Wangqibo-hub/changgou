package com.changgou.file;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-10 12:41
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FastdfsClientTest {

    //文件上传
    @Test
    public void upload() throws Exception {
        //加载全局配置文件
        ClientGlobal.init("D:\\Software_development\\Workspace\\Idea_Projects\\com.changgou\\com.changgou-parent\\com.changgou-service\\com.changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //执行文件上传
        String[] jpgs = storageClient.upload_file("D:\\test\\test.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }

    //删除文件
    @Test
    public void delete() throws Exception {
        //加载全局配置文件
        ClientGlobal.init("D:\\Software_development\\Workspace\\Idea_Projects\\com.changgou\\com.changgou-parent\\com.changgou-service\\com.changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //执行文件删除
        int group1 = storageClient.delete_file("group1", "M00/00/00/wKjThF8xOzuAQK_kAIf5LMhQyHg215.jpg");
        System.out.println(group1);
    }

    //下载文件
    @Test
    public void download() throws Exception {
        //加载全局配置文件
        ClientGlobal.init("D:\\Software_development\\Workspace\\Idea_Projects\\com.changgou\\com.changgou-parent\\com.changgou-service\\com.changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //执行文件下载
        byte[] bytes = storageClient.download_file("group1", "M00/00/00/wKjThF8xPFqAARnEAIf5LMhQyHg365.jpg");
        File file = new File("D:\\ceshi\\123456.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.close();
        fileOutputStream.close();
    }

    //获取文件的信息数据
    @Test
    public void getFileInfo() throws Exception {
        //加载全局配置文件
        ClientGlobal.init("D:\\Software_development\\Workspace\\Idea_Projects\\com.changgou\\com.changgou-parent\\com.changgou-service\\com.changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //获取文件信息
        FileInfo group1 = storageClient.get_file_info("group1", "M00/00/00/wKjThF8xPFqAARnEAIf5LMhQyHg365.jpg");
        System.out.println(group1);
    }

    //获取组相关信息
    @Test
    public void getGroupInfo() throws Exception {
        //加载全局配置文件
        ClientGlobal.init("D:\\Software_development\\Workspace\\Idea_Projects\\com.changgou\\com.changgou-parent\\com.changgou-service\\com.changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer group1 = trackerClient.getStoreStorage(trackerServer, "group1");
        System.out.println(group1.getStorePathIndex());
        //获取组对应的服务器色地址
        ServerInfo[] group1s = trackerClient.getFetchStorages(trackerServer, "group1", "M00/00/00/wKjThF8xPFqAARnEAIf5LMhQyHg365.jpg");
        for (ServerInfo serverInfo : group1s) {
            System.out.println(serverInfo.getIpAddr());
            System.out.println(serverInfo.getPort());
        }
    }

    @Test
    public void getTrackerInfo() throws Exception {
        //加载全局配置文件
        ClientGlobal.init("D:\\Software_development\\Workspace\\Idea_Projects\\com.changgou\\com.changgou-parent\\com.changgou-service\\com.changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        InetSocketAddress inetSocketAddress = trackerServer.getInetSocketAddress();
        System.out.println(inetSocketAddress);
    }
}
