package com.offiahchiazor.android.javadeveloperslagos;

/**
 * Created by Offiah Chiazor on 04/08/2017.
 */
public class Developer {

    /**
     * Developer profile image
     */
    private String mProfileImage;

    /**
     * Developer username
     */
    private String mDeveloperUsername;

    /**
     * Developer github url
     */
    private String mDeveloperUrl;


    /**
     * Construct a new {@link Developer} object
     *
     * @param profileImage      is the developer profile image
     * @param developerUsername is the developer username
     */
    public Developer(String profileImage, String developerUsername, String developerUrl) {
        mProfileImage = profileImage;
        mDeveloperUsername = developerUsername;
        mDeveloperUrl = developerUrl;
    }

    /**
     * Returns the developer profile image
     */
    public String getProfileImage() {
        return mProfileImage;
    }

    /**
     * Returns the developer username
     */
    public String getDeveloperUsername() {
        return mDeveloperUsername;
    }

    /**
     * Returns the developer url
     */
    public String getDeveloperUrl() {
        return mDeveloperUrl;
    }

}
