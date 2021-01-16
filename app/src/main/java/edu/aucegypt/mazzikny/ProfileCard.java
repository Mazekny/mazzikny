package edu.aucegypt.mazzikny;

public class ProfileCard {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private String mText3;

    public ProfileCard(int imageResource, String text1, String text2, String text3)
    {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
    }

    public int getmImageResource()
    {
        return  mImageResource;
    }

    public String getmText1()
    {
        return mText1;
    }

    public String getmText2()
    {
        return mText2;
    }

    public String getmText3()
    {
        return mText3;
    }
}
