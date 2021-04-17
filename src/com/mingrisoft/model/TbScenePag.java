package com.mingrisoft.model;

public class TbScenePag {
    private Integer scenePagId;

    private Integer flipCodeId;

    private Integer num;

    private String sceneCode;

    private String pagename;

    private String contentText;

    public Integer getScenePagId() {
        return scenePagId;
    }

    public void setScenePagId(Integer scenePagId) {
        this.scenePagId = scenePagId;
    }

    public Integer getFlipCodeId() {
        return flipCodeId;
    }

    public void setFlipCodeId(Integer flipCodeId) {
        this.flipCodeId = flipCodeId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode == null ? null : sceneCode.trim();
    }

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename == null ? null : pagename.trim();
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText == null ? null : contentText.trim();
    }
}