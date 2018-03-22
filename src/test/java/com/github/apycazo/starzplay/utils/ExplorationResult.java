package com.github.apycazo.starzplay.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ExplorationResult
{
    public AtomicInteger uncensoredMediaCount = new AtomicInteger(0);
    public AtomicInteger censoredMediaCount = new AtomicInteger(0);
    public AtomicBoolean containsGui31MFR = new AtomicBoolean(false);
    public AtomicBoolean containsGui31C = new AtomicBoolean(false);
}
