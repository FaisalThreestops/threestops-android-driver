package com.driver.threestops.pojo;

import java.io.Serializable;

public class SinginResponsePojo implements Serializable {
    private int errNum;
    private int errFlag = -1;
    private String errMsg;
    private String message;
    private SigninData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public SigninData getData() {
        return data;
    }

    public void setData(SigninData data) {
        this.data = data;
    }
}
