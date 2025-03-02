package com.huhuhu.project.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


@Component
public class OssUtil {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    //文件存储目录
    @Value("${aliyun.oss.filedir}")
    private String filedir;

    @Resource
    private OSSClientBuilder clientBuilder;

    /**
     * 1、单个文件上传
     *
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String fileUrl = uploadFile2Oss(file.getInputStream(), file.getOriginalFilename());
        String str = getFileUrl(fileUrl);
        return str.trim();
    }

    public Map<String, Object> uploadFile2(InputStream stream,String filename) throws IOException {
        String fileUrl = uploadFile2Oss(stream, filename);
        String str = getFileUrl(fileUrl);
        Map<String, Object> result = new HashMap<>();
        String[] split = fileUrl.split("/");
        result.put("fileKey", this.filedir + split[split.length - 1]);
        result.put("urlData", str.trim());
        return result;
    }

    /**
     * 1、单个文件上传(指定文件名（带后缀）)
     *
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, fileName);
            return fileName;
        } catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * 2、多文件上传
     *
     * @param fileList
     * @return 返回完整URL，逗号分隔
     */
    public String uploadFile(List<MultipartFile> fileList) throws IOException {
        String fileUrl = "";
        String str = "";
        String photoUrl = "";
        for (int i = 0; i < fileList.size(); i++) {
            fileUrl = uploadFile2Oss(fileList.get(i).getInputStream(), fileList.get(i).getOriginalFilename());
            str = getFileUrl(fileUrl);
            if (i == 0) {
                photoUrl = str;
            } else {
                photoUrl += "," + str;
            }
        }
        return photoUrl.trim();
    }

    /**
     * 3、通过文件名获取文完整件路径
     * @param fileUrl
     * @return 完整URL路径
     */
    public String getFileUrl(String fileUrl) {
        if (fileUrl != null && fileUrl.length() > 0) {
            String[] split = fileUrl.split("/");
            String url = this.getUrl(this.filedir + split[split.length - 1]);
            return url;
        }
        return null;
    }

    //获取去掉参数的完整路径
    private String getShortUrl(String url) {
        String[] imgUrls = url.split("\\?");
        return imgUrls[0].trim();
    }

    // 获得url链接
    private String getUrl(String key) {
        // 设置URL过期时间为20年 3600l* 1000*24*365*20
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 20);
        // 生成URL
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
//        if (url != null) {
//            return getShortUrl(url.toString());
//        }
//        return null;
        return url.toString();
    }

    // 获得url链接
    public String getUrlForExpiration(String key, Date expiration) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        return url.toString();
    }

    // 上传文件
    private String uploadFile2Oss(InputStream stream,String filename) throws IOException {
        //1、限制最大文件为2G
        if (stream.available() > 1024 * 1024 * 100) {
            return "文件太大了！";
        }
        String suffix = filename.substring(filename.lastIndexOf(".")).toLowerCase(); //文件后缀
        String uuid = UUID.randomUUID().toString();
        String name = uuid + suffix;
        try {
            this.uploadFile2OSS(stream, name);
            return name;
        } catch (Exception e) {
            return "上传失败";
        }
    }

    // 上传文件（指定文件名）
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
//            objectMetadata.setContentType("image/jpg");
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件

            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        //PDF
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        return "image/jpeg";
    }


    public static String getFileType(String FilenameExtension) {
        if (FilenameExtension.endsWith(".bmp")) {
            return "bmp";
        }
        if (FilenameExtension.endsWith(".gif")) {
            return "gif";
        }
        if (FilenameExtension.endsWith(".jpeg") ||
                FilenameExtension.endsWith(".jpg") ||
                FilenameExtension.endsWith(".png")) {
            return "jpeg";
        }
        if (FilenameExtension.endsWith(".html")) {
            return "html";
        }
        if (FilenameExtension.endsWith(".txt")) {
            return "txt";
        }
        if (FilenameExtension.endsWith(".vsd")) {
            return "vsd";
        }
        if (FilenameExtension.endsWith(".pptx") ||
                FilenameExtension.endsWith(".ppt")) {
            return "pptx";
        }
        if (FilenameExtension.endsWith(".docx") ||
                FilenameExtension.endsWith(".doc")) {
            return ".docx";
        }
        if (FilenameExtension.endsWith(".xml")) {
            return "xml";
        }
        //PDF
        if (FilenameExtension.endsWith(".pdf")) {
            return "pdf";
        }
        return "jpeg";
    }

    public OSSObject download(String downloadUrlKey) {
        OSSClient ossClient = null;
        try {
            ossClient = createClient();
            return ossClient.getObject(bucketName, downloadUrlKey);
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.DOWNLOAD_ERROR);
        }
    }

    public OSSClient createClient() {
        return (OSSClient) clientBuilder.build(endpoint, accessKeyId, accessKeySecret);
    }

}




