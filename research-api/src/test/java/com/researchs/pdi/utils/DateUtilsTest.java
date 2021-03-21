package com.researchs.pdi.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateUtilsTest {

    @Test
    void getParse() {
        LocalDate dataLocal = DateUtils.getParse("01/12/2016");

        assertEquals(2016, dataLocal.getYear());
        assertEquals(12, dataLocal.getMonthValue());
        assertEquals(1, dataLocal.getDayOfMonth());
    }

    @Test
    void getDate() {
        LocalDate now = DateUtils.getParse("01/12/2016");;
        Date date = DateUtils.getDate(now);

        assertEquals("Thu Dec 01 00:00:00 BRST 2016", date.toString());
    }

    @Test
    void media() {
        assertTrue(DateUtils.isAlmostPalindrome("abccba"));
        assertTrue(DateUtils.isAlmostPalindrome("abccbx"));
        assertFalse(DateUtils.isAlmostPalindrome("abccfg"));
    }

    @Test
    void media2() {

        assertEquals(34, DateUtils.getMostPopularNumber(new int[]{34,31,34,77,82}));
        assertEquals(101, DateUtils.getMostPopularNumber(new int[]{22,101,102,101,102,525,88}));
        assertEquals(66, DateUtils.getMostPopularNumber(new int[]{66}));
        assertEquals(2342, DateUtils.getMostPopularNumber(new int[]{14,14,2342,2342,2342}));

    }

}