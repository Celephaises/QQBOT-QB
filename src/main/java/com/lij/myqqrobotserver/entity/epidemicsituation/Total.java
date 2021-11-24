
package com.lij.myqqrobotserver.entity.epidemicsituation;

public class Total extends Today {
    public Integer nowConfirm;
    public Integer dead;
    public String deadRate;
    public Integer heal;
    public String healRate;
    public String grade;
    public Total(Integer confirm, Integer nowConfirm, Integer dead, String deadRate, Integer heal, String healRate, String grade) {
        super(confirm);
        this.nowConfirm = nowConfirm;
        this.dead = dead;
        this.deadRate = deadRate;
        this.heal = heal;
        this.healRate = healRate;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Total{" +
                "nowConfirm=" + nowConfirm +
                ", dead=" + dead +
                ", deadRate='" + deadRate + '\'' +
                ", heal=" + heal +
                ", healRate='" + healRate + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}