package com.lij.myqqrobotserver.entity.epidemicsituation;

public class EpidemicSituation {
    public String name;
    public Today today;
    public Total total;

    @Override
    public String toString() {
        return "EpidemicSituation{" +
                "name='" + name + '\'' +
                ", today=" + today +
                ", total=" + total +
                '}';
    }
}

