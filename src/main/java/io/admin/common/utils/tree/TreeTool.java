package io.admin.common.utils.tree;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 将列表转换为树,请使用TreeManager
 */
public class TreeTool {


    public static List<Dict> buildTree(List<Dict> list) {
        return buildTree(list, e -> e.getStr("key"), e -> e.getStr("parentKey"), (e) -> e.get("children", new ArrayList<>()), (e, children) -> e.set("children", children));
    }


    /**
     * 构造树
     *
     * @param list
     * @param idFn
     * @param pidFn
     * @param <E>
     * @return
     */
    public static <E> List<E> buildTree(List<E> list, Function<E, String> idFn, Function<E, String> pidFn, Function<E, List<E>> getChildren, BiConsumer<E, List<E>> setChildren) {
        Map<String, E> idMap = new HashMap<>();
        for (E e : list) {
            idMap.put(idFn.apply(e), e);
        }

        List<E> tree = new ArrayList<>();

        for (E e : list) {
            String pid = pidFn.apply(e);
            E parent = idMap.get(pid);

            if (parent == null) {
                tree.add(e);
                continue;
            }

            List<E> parentChildren = getChildren.apply(parent); // 父节点的children字段
            if (parentChildren == null) {
                parentChildren = new ArrayList<>();
                setChildren.accept(parent, parentChildren);
            }
            parentChildren.add(e);
        }

        return tree;
    }


    public static <E> void cleanEmptyChildren(List<E> list, Function<E, List<E>> getChildren, BiConsumer<E, List<E>> setChildrenFn) {
        walk(list, getChildren, e -> {
            List<E> children = getChildren.apply(e);
            if (CollUtil.isEmpty(children)) {
                setChildrenFn.accept(e, null);
            }
        });
    }


    /**
     * 递归树并处理子树下的节点
     *
     * @param consumer 节点处理器
     */
    public static <E> void walk(List<E> list, Function<E, List<E>> getChildren, Consumer<E> consumer) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (E e : list) {
            consumer.accept(e);
            List<E> children = getChildren.apply(e);
            walk(children, getChildren, consumer);
        }
    }



    /**
     * 递归树并处理子树下的节点
     *
     * @param consumer 节点处理器, 两个参数，分别是节点，节点的父节点
     */
    public static <E> void walk(List<E> list, Function<E, List<E>> getChildren, BiConsumer<E, E> consumer) {
        walk(null, list, getChildren, consumer);
    }

    private static <E> void walk(E parent, List<E> list, Function<E, List<E>> getChildren, BiConsumer<E, E> consumer) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (E e : list) {
            consumer.accept(e, parent);
            List<E> children = getChildren.apply(e);
            walk(e, children, getChildren, consumer);
        }
    }

    /**
     * 获取一棵树的叶子
     *
     * @param <E>
     * @return
     */
    public static <E> List<E> getLeafs(List<E> list, Function<E, List<E>> getChildren) {
        List<E> result = new ArrayList<>();
        walk(list, getChildren, e -> {
            List<E> children = getChildren.apply(e);
            boolean isLeaf = CollUtil.isEmpty(children);
            if (isLeaf) {
                result.add(e);
            }
        });

        return result;
    }


    public static <E> List<E> treeToList(List<E> tree, Function<E, List<E>> getChildren) {
        List<E> list = new ArrayList<>();
        walk(tree, getChildren, e -> list.add(e));
        return list;
    }


    /**
     * 获取节点的父节点列表
     *
     * @param <E>
     * @param list 注意不是树，而是列表
     * @return
     */
    public static <E> List<String> getPids(String nodeId, List<E> list, Function<E, String> idFn, Function<E, String> pidFn) {
        Map<String, E> idMap = new HashMap<>();
        for (E e : list) {
            idMap.put(idFn.apply(e), e);
        }
        E node = idMap.get(nodeId);
        if (node == null) {
            return Collections.emptyList();
        }

        List<String> pids = new ArrayList<>();

        String pid = pidFn.apply(node);
        E parent = idMap.get(pid);
        while (parent != null) {
            pids.add(pid);
            pid = pidFn.apply(parent);
            parent = idMap.get(pid);
        }


        return pids;

    }

}
