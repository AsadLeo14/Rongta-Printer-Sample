package com.printer.example;


import com.printer.example.utils.FuncUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        byte[] bts = FuncUtils.HexToByteArr("03 FF 2f 00");
        System.out.println("str:" + FuncUtils.ByteArrToHex(bts));
    }
}