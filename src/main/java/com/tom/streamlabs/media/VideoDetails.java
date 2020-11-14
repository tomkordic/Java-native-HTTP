package com.tom.streamlabs.media;

import com.google.gson.JsonObject;

public class VideoDetails {

    static final String WIDTH_JSON_KEY = "width";
    static final String HEIGHT_JSON_KEY = "height";
    static final String BIT_RATE_JSON_KEY = "bit_rate";
    static final String PROFILE_JSON_KEY = "profile";
    static final String CODEC_NAME_JSON_KEY = "codec";

    int mWidth;
    int mHeight;
    int mBitRate;
    String mCodec;
    String mProfile;

    public VideoDetails(int aWidth, int aHeight, int aBitRate,
                        String aCodec, String aProfile) {
        mWidth = aWidth;
        mHeight = aHeight;
        mBitRate = aBitRate;
        mCodec = aCodec;
        mProfile = aProfile;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int aWidth) {
        mWidth = aWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int aHeight) {
        mHeight = aHeight;
    }

    public int getBitRate() {
        return mBitRate;
    }

    public void setBitRate(int aBitRate) {
        mBitRate = aBitRate;
    }

    public String getProfile() {
        return mProfile;
    }

    public void setProfile(String aProfile) {
        mProfile = aProfile;
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty(WIDTH_JSON_KEY, mWidth);
        result.addProperty(HEIGHT_JSON_KEY, mHeight);
        result.addProperty(BIT_RATE_JSON_KEY, mBitRate);
        result.addProperty(PROFILE_JSON_KEY, mProfile);
        result.addProperty(CODEC_NAME_JSON_KEY, mCodec);
        return result;
    }
}
