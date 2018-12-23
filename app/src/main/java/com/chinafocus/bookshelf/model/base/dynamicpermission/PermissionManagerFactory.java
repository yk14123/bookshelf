package com.chinafocus.bookshelf.model.base.dynamicpermission;

import com.chinafocus.bookshelf.model.base.lru.LruMap;

public class PermissionManagerFactory {

    public static IPermissionManager getPermissionManager() {
//        return new AbstractPermissionManager() {
//            @Override
//            public void requestRuntimePermission(Activity activity, String[] permissions, IPermissionListener listener) {
//                super.requestRuntimePermission(activity, permissions, listener);
//            }
//        };
        LruMap lruMap = LruMap.getInstance();
        String name = PermissionManager.class.getName();
        PermissionManager permissionManager = (PermissionManager) lruMap.get(name);
        if (permissionManager == null) {
            permissionManager = new PermissionManager();
            lruMap.put(name, permissionManager);
        }
        return permissionManager;
    }

}
