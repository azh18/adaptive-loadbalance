package com.aliware.tianchi;


import org.junit.Test;

/**
 * @author zyz
 * @version 2019-07-15
 */
public class ProviderStatusTest {
    @Test
    public void testNext() {
        for (int i = 0; i < 3; i ++) {
            System.out.println(ProviderStatus.next());
        }
    }
}
