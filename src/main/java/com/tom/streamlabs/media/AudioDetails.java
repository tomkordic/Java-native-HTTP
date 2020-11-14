package com.tom.streamlabs.media;

import com.google.gson.JsonObject;

public class AudioDetails {

    static final String SAMPLE_RATE_JSON_KEY = "sampl_erate";
    static final String CHANNELS_JSON_KEY = "channels";
    static final String BIT_RATE_JSON_KEY = "bit_rate";
    static final String PROFILE_JSON_KEY = "profile";
    static final String CODEC_NAME_JSON_KEY = "codec";

    int mSampleRate;
    int mChannels;
    int mBitRate;
    String mCodecName;
    String mProfile;

    public AudioDetails(int aSampleRate, int aChannels, int aBitrate,
                        String aCodecName, String aProfile) {
        mSampleRate = aSampleRate;
        mChannels = aChannels;
        mBitRate = aBitrate;
        mCodecName = aCodecName;
        mProfile = aProfile;
    }

    public int getSampleRate() {
        return mSampleRate;
    }

    public void setSampleRate(int aSampleRate) {
        mSampleRate = aSampleRate;
    }

    public int getChannels() {
        return mChannels;
    }

    public void setChannels(int aChannels) {
        mChannels = aChannels;
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
        result.addProperty(SAMPLE_RATE_JSON_KEY, mSampleRate);
        result.addProperty(CHANNELS_JSON_KEY, mChannels);
        result.addProperty(BIT_RATE_JSON_KEY, mBitRate);
        result.addProperty(PROFILE_JSON_KEY, mProfile);
        result.addProperty(CODEC_NAME_JSON_KEY, mCodecName);
        return result;
    }
}
