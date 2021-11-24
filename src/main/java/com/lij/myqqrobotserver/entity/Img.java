package com.lij.myqqrobotserver.entity;

/**
 * @author Celphise
 * @Package demo.bean
 * @date 2020/12/28 10:42
 */
public class Img {
    private String imgId;
    private Integer workId;
    private Integer saveType;
    private String url;

    public Img(String imgId, Integer workId, Integer saveType, String url) {
        this.imgId = imgId;
        this.workId = workId;
        this.saveType = saveType;
        this.url = url;
    }

    public Img() {

    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getSaveType() {
        return saveType;
    }

    public void setSaveType(Integer saveType) {
        this.saveType = saveType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
