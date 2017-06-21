/**
 *
 */
package com.dianping.mobile.core.enums;

/**
 * @author kewen.yao
 */
public class ClientType {

    public static final ClientType MAINAPP_IPHONE = new ClientType(Platform.iPhone, Product.API);
    public static final ClientType MAINAPP_ANDROID = new ClientType(Platform.Android, Product.API);
    public static final ClientType MAINAPP_WINPHONE = new ClientType(Platform.WinPhone, Product.API);
    public static final ClientType MAINAPP_IPADHD = new ClientType(Platform.iPadHd, Product.API);
    public static final ClientType MAINAPP_WIN8PAD = new ClientType(Platform.Win8Pad, Product.API);

    public static final ClientType YPAPP_IPHONE = new ClientType(Platform.iPhone, Product.YELLOWPAGE);
    public static final ClientType YPAPP_ANDROID = new ClientType(Platform.Android, Product.YELLOWPAGE);


    private final int value;
    private final Platform platform;
    private final Product product;

    private String description;

    public ClientType(Platform platform, Product product) {
        this.platform = platform;
        this.product = product;
        this.value = platform.getValue() + product.getValue();
    }

    public int getValue() {
        return value;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClientType) {
            ClientType other = (ClientType) obj;
            return platform == other.getPlatform()
                    && product == other.getProduct();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (platform.hashCode() << 16) + product.hashCode();
    }

    @Override
    public String toString() {
        if (description == null) {
            description = platform != null ? platform.getName() : "unknown";
            description += "_";
            description += product != null ? product.getName() : "unknown";
        }
        return description;
    }

}
