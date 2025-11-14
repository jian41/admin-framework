package io.admin.common.utils.tree.drop;

import cn.hutool.core.collection.CollUtil;
import io.admin.common.antd.DropEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TreeDropToolTest {
    List<String> LIST = CollUtil.newArrayList("ABCDE".split(""));
    @Test
    void resort() {
        check(new DropEvent("E","A",true,-1),"EABCD");
        check(new DropEvent("E","D",true,5),"ABCDE");


    }

    void check( DropEvent e, String expected){
        List<String> result = TreeDropTool.resort(LIST, e);
        String resultStr = String.join("", result);
        Assertions.assertEquals(expected,resultStr);
    }
}
