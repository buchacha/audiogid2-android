package com.example.audiogid2;

import java.io.Serializable;

public class AudioItem implements Serializable {

    private int resourceId;
    private int labelId;
    private int textId;

    public AudioItem(int resourceId, int labelId, int textId) {
        this.resourceId = resourceId;
        this.labelId = labelId;
        this.textId = textId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getLabelId() {
        return labelId;
    }

    public int getTextId() {
        return textId;
    }
}
