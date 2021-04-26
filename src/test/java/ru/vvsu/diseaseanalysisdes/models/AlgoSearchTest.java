package ru.vvsu.diseaseanalysisdes.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AlgoSearchTest {
    private AlgoSearch algoSearch;
    @Before
    public void setUp() {
        algoSearch = new AlgoSearch();
    }

    @Test
    public void getCombinations() {
        int expected = algoSearch.getCombinations(4,1);

        int actual = 4;

        Assert.assertEquals(expected, actual);
    }
}