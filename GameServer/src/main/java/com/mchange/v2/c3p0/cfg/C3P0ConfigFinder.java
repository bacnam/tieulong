package com.mchange.v2.c3p0.cfg;

public interface C3P0ConfigFinder {
  C3P0Config findConfig() throws Exception;
}

