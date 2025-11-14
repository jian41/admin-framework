package io.admin.common.utils.tree.drop;

import io.admin.common.antd.DropEvent;
import io.admin.common.antd.TreeNodeItem;
import io.admin.common.utils.tree.TreeTool;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 树拖拽排序
public class TreeDropTool {


    /**
     * 计算拖拽排序
     */

    public static DropResult onDrop(DropEvent e, List<TreeNodeItem> tree) {
        Map<String, TreeNodeItem> keyMap = TreeTool.treeToMap(tree);


        TreeNodeItem dragNode = keyMap.get(e.getDragKey());
        TreeNodeItem dropNode = keyMap.get(e.getDropKey());
        Assert.notNull(dragNode, "拖拽的节点不存在");
        Assert.notNull(dropNode, "放置的节点不存在");

        DropResult result = new DropResult();
        result.parentKey = e.isDropToGap() ? dropNode.getParentKey() : dropNode.getKey();

        TreeNodeItem parentNode = keyMap.get(result.getParentKey());
        List<TreeNodeItem> siblings = keyMap != null ? parentNode.getChildren() : tree; // 如果父节点为空，说明拖拽到了根节点平级了


        List<String> keys = new ArrayList<>();
        for (TreeNodeItem child : siblings) {
            keys.add(child.getKey());
        }
        result.sortedKeys = resort(keys, e);
        return result;
    }


    public static List<String> resort(List<String> list, DropEvent e) {
        int dropPosition = e.getDropPosition();
        String key = e.getDragKey();
        if (dropPosition == -1) {
            list.remove(key);
            list.add(0, key);
            return list;
        }

        return list;
    }



}
