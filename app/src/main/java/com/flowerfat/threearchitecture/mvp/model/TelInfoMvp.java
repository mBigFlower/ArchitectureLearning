package com.flowerfat.threearchitecture.mvp.model;

/**
 * Created by 明明大美女 on 2016/5/13.
 */
public class TelInfoMvp {
    int errNum;
    String errMsg;
    RetData retData;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public RetData getRetData() {
        return retData;
    }

    public void setRetData(RetData retData) {
        this.retData = retData;
    }

    public class RetData {
        String telString;
        String province;
        String carrier;

        public String getTelString() {
            return telString;
        }

        public void setTelString(String telString) {
            this.telString = telString;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }
    }
}
