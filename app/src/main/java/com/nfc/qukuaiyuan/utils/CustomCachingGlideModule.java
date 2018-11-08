package com.nfc.qukuaiyuan.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.nfc.qukuaiyuan.utils.jutils.JFileManager;

/**
 * Created by cwj on 16/7/17.
 */
public class CustomCachingGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        String path= JFileManager.getInstance().getFolder(JFileManager.Dir.image).getFile().getAbsolutePath();
        builder.setDiskCache(new DiskLruCacheFactory(path, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
