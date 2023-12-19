package com.myworld.enen.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @ClassName UploadOSSFile
 * @Author wkz
 * @Date 2023/12/17 15:35
 * @Version 1.0
 */
@Slf4j
public class UploadOSSFile {
    
    public static void  upload(InputStream in, String objectName, String saveDirectory, String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory
                .newDefaultCredentialProvider(accessKeyId, accessKeySecret);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        try {
//            FileInputStream inputStream = new FileInputStream(filePath);
            ossClient.putObject(bucketName, saveDirectory + objectName, inputStreamToByteArray(in));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    public static ByteArrayInputStream inputStreamToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] byteArray = buffer.toByteArray();
        return new ByteArrayInputStream(byteArray);
    }
}
