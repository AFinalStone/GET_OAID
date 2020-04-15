package com.yr.unique.code.utils;

public enum DeviceTypeEnum {

    HuaShuo("华硕手机", "ASUS"),
    HuaWei("华为手机", "HUAWEI"),
    Lianxiang("联想手机", "LENOVO"),
    Motolora("摩托罗拉手机", "MOTOLORA"),
    Nubia("努比亚手机", "NUBIA"),
    Meizu("魅族手机", "MEIZU"),
    Oppo("Oppo手机", "NUBIA"),
    Samsung("三星手机", "SAMSUNG"),
    Vivo("Vivo手机", "VIVO"),
    XiaoMi("小米手机", "XIAOMI"),
    BlackShark("小米手机", "BLACKSHARK"),
    OnePlus("OnePlus手机", "ONEPLUS"),
    ZTE("中兴手机", "ZTE");


    private String name;
    private String flag;

    DeviceTypeEnum(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public static DeviceTypeEnum getInstance(String flag) {
        DeviceTypeEnum[] list = values();
        for (DeviceTypeEnum item : list) {
            if (item.getFlag().equals(flag)) {
                return item;
            }
        }
        return null;
    }
}
