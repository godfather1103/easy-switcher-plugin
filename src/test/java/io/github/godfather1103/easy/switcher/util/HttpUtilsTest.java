package io.github.godfather1103.easy.switcher.util;

import org.junit.Test;


public class HttpUtilsTest {

    @Test
    public void testDownAutoproxyRule() {
        var resp = HttpUtils.downAutoproxyRule("https://gitlab.com/gfwlist/gfwlist/-/raw/master/gfwlist.txt");
        System.out.println(resp);
    }
}