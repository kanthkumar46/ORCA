package com.orca.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * Created by KanthKumar on 2/24/17.
 */
public class TestSetup {

    private long startTime;

    @Rule
    public TestName name = new TestName();

    @Before
    public void beforeMethod() {
        startTime = System.currentTimeMillis();
        System.out.println("Stating test: "+name.getMethodName());
    }

    @After
    public void afterMethod() {
        System.out.println("Finished, Time elapsed: "+(System.currentTimeMillis() - startTime)+"ms\n");
    }
}
