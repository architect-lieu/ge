package com.huhuhu.project.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * @program: tang-yuan-interaction
 * @description:
 * @author: KangXin
 * @create: 2022/7/6
 **/
public class SearchIpUtil {
    private static final byte[] C_BUFF;
    static {
        ClassPathResource pathResource = new ClassPathResource("ip2region.xdb");
        C_BUFF = pathResource.readBytes();
    }
    public static String searchIpRegion(String ipAddr) {
        ClassPathResource pathResource = new ClassPathResource("ip2region.xdb");
        // 1、从 dbPath 加载整个 xdb 到内存。
        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        Searcher searcher;
        try {
            searcher = Searcher.newWithBuffer(C_BUFF);
        } catch (Exception e) {
            System.out.printf("failed to create content cached searcher: %s\n", e);
            return null;
        }
        // 3、查询
        try {
            return searcher.searchByStr(ipAddr);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ipAddr, e);
        }
        return null;
    }

}
