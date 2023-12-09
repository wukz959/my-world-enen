package com.myworld.enen.types;

public enum BlogTypes {
    OPEN_SOURCE(0), BLOG(1);
    private int index;
    private BlogTypes(int index) {
        this.index = index;
    }
}
