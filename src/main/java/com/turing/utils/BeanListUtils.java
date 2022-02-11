package com.turing.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 19:39:18
 */
public class BeanListUtils
{
    public static String transform(List list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if ( list == null || list.isEmpty() )
        {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1)
            {
                stringBuilder.append(list.get(i) + ",");
            }else
            {
                stringBuilder.append(list.get(i));
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args)
    {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        String transform = BeanListUtils.transform(list);
        System.out.println(transform);
    }
}
