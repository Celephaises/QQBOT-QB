package com.lij.myqqrobotserver.entity.epidemicsituation;

public class Today {
    public Integer confirm;
    public Today(Integer confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "Today{" +
                "confirm=" + confirm +
                '}';
    }
}