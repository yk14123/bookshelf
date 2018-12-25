package com.chinafocus.bookshelf.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2018/12/25
 * description：
 */
public class SplitListUtil {

    /**
     * @param list 要分割的集合
     * @param len  要分割的长度
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }

        List<List<T>> result = new ArrayList<List<T>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}
