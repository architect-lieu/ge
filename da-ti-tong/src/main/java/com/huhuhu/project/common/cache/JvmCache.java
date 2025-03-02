package com.huhuhu.project.common.cache;

import com.huhuhu.project.entity.VipConfig;
import com.huhuhu.project.mapper.VipConfigMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/5/2 15:28
 */
@Component
public class JvmCache {

    private static JvmCache jvmCache;
    private final VipConfigMapper vipConfigMapper;
    private static final Map<String, VipConfig> VIP_CONFIG_CACHE_MAP = new HashMap<>();
    private static final ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    public JvmCache(VipConfigMapper vipConfigMapper) {
        this.vipConfigMapper = vipConfigMapper;
        jvmCache = this;
        initJvmCache();
    }

    private void initJvmCache() {
        // 初始化vip配置
        initVipConfigCache();
    }

    private void initVipConfigCache() {
        List<VipConfig> vipConfigs = vipConfigMapper.selectList(null);
        Map<String, VipConfig> vipConfigMap = vipConfigs.stream().collect(Collectors.toMap(VipConfig::getVipType, Function.identity()));
        VIP_CONFIG_CACHE_MAP.putAll(vipConfigMap);
    }

    /**
     * 获取vip配置
     * @return
     */
    private static void vipConfigCache() {
        if (!VIP_CONFIG_CACHE_MAP.isEmpty()) {
            return;
        }
        try {
            READ_LOCK.lock();
            if (!VIP_CONFIG_CACHE_MAP.isEmpty()) {
                return;
            }
            jvmCache.initVipConfigCache();
        }finally {
            READ_LOCK.unlock();
        }
    }

    public static VipConfig getVipConfig(String key) {
        vipConfigCache();
        return VIP_CONFIG_CACHE_MAP.get(key);
    }

    /**
     * 清除vip配置
     */
    public static void clearVipConfig() {
        try {
            WRITE_LOCK.lock();
            VIP_CONFIG_CACHE_MAP.clear();
        }finally {
            WRITE_LOCK.unlock();
        }

    }
}
