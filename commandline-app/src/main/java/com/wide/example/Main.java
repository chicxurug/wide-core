package com.wide.example;

import com.google.common.collect.ImmutableSet;

public class Main {

    public static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of("red", "orange", "yellow", "green", "blue", "purple");

    public static void main(String[] args) {
        System.out.println("It works!");
        System.out.println(COLOR_NAMES);
    }

}
