package io.boot.config;

import org.ssssssss.magicapi.modules.db.cache.SqlCache;

//@Component
public class MagicCacheImpl implements SqlCache {

    @Override
    public void put(String name, String key, Object value) {
    }

    @Override
    public void put(String name, String key, Object value, long ttl) {
    }

    @Override
    public <T> T get(String name, String key) {
        return null;
    }

    @Override
    public void delete(String name) {

    }
}
