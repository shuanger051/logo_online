package com.qinghua.website.mobile.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BeanUtils
 * @Description 创建工具类
 **/
public class BeanToolsUtil {

    /**
     * @param orig 源对象
     * @param dest 目标对象
     */
    public static void copyProperties(final Object orig, final Object dest) {
        if (orig == null || dest == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param origs          源list对象
     * @param dests          目标list对象
     * @param destElementTpe 目标集合存储的Class对象名,例如:User.class
     * @param <T1>           源list元素类型
     * @param <T2>           目标list元素类型
     * @Description： 拷贝list元素对象，将origs中的元素信息，拷贝覆盖至dests中
     */
    public static <T1, T2> void copyProperties(final List<T1> origs, final List<T2> dests, Class<T2> destElementTpe) {
        if (origs == null || dests == null) {
            return;
        }
        if (origs.size() == 0) {
            return;
        }
        if (dests.size() != 0) {
            //防止目标对象被覆盖，要求必须长度为零
            throw new RuntimeException("目标对象存在值");
        }
        try {
            for (T1 orig : origs) {
                T2 t = destElementTpe.newInstance();
                dests.add(t);
                copyProperties(orig, t);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 对象转换，如果转换前的对象是null，则返回null
     *
     * @param source      原对象
     * @param targetClazz 目标对象字节码
     * @param <T>         返回目标对象实例
     * @return
     */
    public static <T> T copyOrReturnNull(final Object source, final Class<T> targetClazz) {
        if (source == null)
            return null;
        try {
            T result = targetClazz.newInstance();
            BeanUtils.copyProperties(result, source);
            return result;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new RuntimeException("对象转换异常", ex);
        }

    }

    /**
     * 对象转换，如果转换前的对象是null，则返回空对象
     *
     * @param source      原对象
     * @param targetClazz 目标对象字节码
     * @param <T>         返回目标对象实例
     * @return
     */
    public static <T> T copy(final Object source, final Class<T> targetClazz) {
        try {
            if (source == null)
                return targetClazz.newInstance();

            T result = targetClazz.newInstance();
            BeanUtils.copyProperties(result, source);
            return result;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new RuntimeException("对象转换异常", ex);
        }

    }

    /**
     * 转换数组对象
     *
     * @param source      原对象数组
     * @param targetClazz 目标对象字节码
     * @param <T>         目标对象数组
     * @return
     */
    public static <T, E> List<T> copyAsList(final List<E> source, final Class<T> targetClazz) {
        List<T> results = new ArrayList<>();
        if (source == null)
            return results;
        try {
            for (E obj : source) {
                T result = targetClazz.newInstance();
                BeanUtils.copyProperties(result, obj);
                results.add(result);
            }
            return results;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new RuntimeException("数组对象转换异常", ex);
        }
    }

    /**
     * 从List<A> copy到List<B>
     * @param list List<B>
     * @param clazz B
     * @return List<B>
     */
    public static <T> List<T> copyList(List<?> list,Class<T> clazz){
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }

}
