package com.chinafocus.bookshelf.base;

import java.util.List;

/**
 * Created by 馒头大人 on 2017/11/3.
 */

public interface PermissionListener {
    /**
     * 当权限全部申请通过后，调用的方法
     */
    void onGranted();

    /**
     * 当有一个或者一组权限被拒绝的时候，调用的方法
     * @param deniedPermission  拒绝权限的list集合
     */
    void onDenied(List<String> deniedPermission);
}
