package com.turing.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月22日 11:45:28
 */
@Slf4j
public class FTPUtils
{
    private static FTPClient client = null;
    private static final String FTP_ADDRESS = "120.25.145.86";
    private static final int FTP_PORT = 21;
    private static String FTP_USERNAME = "user";
    private static String FTP_PASSWORD = "123456";
    private static String DATE = "/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/";
    private static String FTP_BASEPATH = "/home/user"+DATE;
    private static String FTP_DELETE_BASEPATH = "/home/user";

    public static String upload(MultipartFile file) throws Exception
    {
        InputStream inputStream = null;
        try {
            init();
            inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            filename = UUID.randomUUID() + "-" + filename;
            client.storeFile(filename,inputStream);
            return DATE+filename;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            destroy();
        }
        throw new Exception("文件上传失败!");
    }

    public static void delete(String filePath) throws Exception
    {
        init();
        String filepath = filePath.substring(0, filePath.lastIndexOf("/")+1);
        System.out.println("filepath："+FTP_DELETE_BASEPATH+filepath);
        client.changeWorkingDirectory(FTP_DELETE_BASEPATH+filepath);
        String filename = filePath.substring(filePath.lastIndexOf("/")+1);
        System.out.println("filename："+filename);
        boolean deleteStates = client.deleteFile(filename);
        if (!deleteStates)
        {
            log.warn("文件[{}]删除失败",filepath+filename);
        }else
        {
            log.info("文件[{}]删除成功",filepath+filename);
        }
        destroy();
    }

    private static void init() throws Exception
    {
        client = new FTPClient();
        client.setControlEncoding("utf-8");
        client.connect(FTP_ADDRESS,FTP_PORT);
        client.login(FTP_USERNAME,FTP_PASSWORD);
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            client.disconnect();
            log.error("FTPClient Connected Error");
            throw new Exception("FTP服务器连接失败!");
        }
        log.info("FTPClient Connected Success!");
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        if (!client.changeWorkingDirectory(FTP_BASEPATH))
        {
            client.makeDirectory(FTP_BASEPATH);
            client.changeWorkingDirectory(FTP_BASEPATH);
        }
    }


    private static void destroy() throws Exception
    {
        client.logout();
        if (client.isConnected())
        {
            client.disconnect();
        }
    }




}
