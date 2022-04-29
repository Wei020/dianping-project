package com.dppojo.utils;

public interface ILock {

    boolean tryLock(long timeoutSec);

    void unlock();
}
