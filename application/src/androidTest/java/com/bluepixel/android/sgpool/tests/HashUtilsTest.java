package com.bluepixel.android.sgpool.tests;

import android.test.suitebuilder.annotation.SmallTest;
import com.bluepixel.android.sgpool.util.HashUtils;
import junit.framework.TestCase;

public class HashUtilsTest extends TestCase{



    @SmallTest
    public void testComputeWeakHash() {
        String result = HashUtils.computeWeakHash("helloworld");
        assertNotNull(result);
        assertTrue(result.length() != 0);
    }
}
