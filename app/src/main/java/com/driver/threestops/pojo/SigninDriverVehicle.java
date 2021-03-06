package com.driver.threestops.pojo;


public class SigninDriverVehicle {
    private boolean selected;
    private String id;
    private String typeId;
    private String vehicleModel;
    private String platNo;
    private String operator;
    private String vehicleType;
    private String[] goodTypes;

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String[] getGoodTypes() {
        return goodTypes;
    }

    public void setGoodTypes(String[] goodTypes) {
        this.goodTypes = goodTypes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + "\"" + id + "\"" +
                ", \"typeId\":" + "\"" + typeId + "\"" +
                ", \"vehicleModel\":" + "\"" + vehicleModel + "\"" +
                ", \"platNo\":" + "\"" + platNo + "\"" +
                ", \"vehicleType\":" + "\"" + vehicleType + "\"" +
                ", \"operator\":" + "\"" + operator + "\"" +
                "}";
    }
}
