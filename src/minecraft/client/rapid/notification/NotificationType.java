package client.rapid.notification;

import net.minecraft.util.ResourceLocation;

public enum NotificationType {
    INFO(new ResourceLocation("rapid/images/info.png")),
    WARNING(new ResourceLocation("rapid/images/info.png")),
    ERROR(new ResourceLocation("rapid/images/info.png"));

    private final ResourceLocation image;

    NotificationType(ResourceLocation image) {
        this.image = image;
    }

    public ResourceLocation getImage() {
        return image;
    }

}
