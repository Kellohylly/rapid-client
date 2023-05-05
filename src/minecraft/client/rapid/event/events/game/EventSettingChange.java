package client.rapid.event.events.game;

import client.rapid.event.events.Event;
import client.rapid.module.settings.Setting;

public class EventSettingChange extends Event {
    private Setting setting;

    public EventSettingChange(Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return setting;
    }
}
