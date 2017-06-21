package com.dianping.mobile.core.enums;

public enum Platform {
    Android(1, "Android"),
    iPhone(2, "iPhone"),
    NokiaWidget(3, "NokiaWidget"),
    SymbianS60v3(4, "SymbianS60v3"),
    KJava(5, "KJava"),
    SymbianS60v5(6, "SymbianS60v5"),
    BlackBerry(7, "BlackBerry"),
    Bada(8, "Bada"),
    Symbian3(9, "Symbian3"),
    iPadHd(10, "iPadHd"),
    WinPhone(11, "WinPhone"),
    Meego(12, "Meego"),
    Win8Pad(13, "Win8Pad"),
    WinCE(14, "WinCE"),
    Unknow(0, "unknow");


    private final int value;
    private final String name;

    private Platform(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
