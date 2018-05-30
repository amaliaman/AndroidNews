package com.example.ami.androidnews;

import java.net.URL;
import java.util.Date;

/**
 * An {@link Article} is a news item from a news provider website (The Guardian in this case)
 */
public class Article {
    private String mTitle;
    private String mTrail;
    private String mByLine;
    private URL mArticleUrl;
    private URL mThumbnailUrl;
    private Date mPublicationDate;

    /**
     * Create a new {@link Article} object
     *
     * @param title is the article's title
     * @param trail is the article's subheading
     * @param byLine is the name of the author
     * @param articleUrl is the url of the article in the website
     * @param thumbnailUrl is the url of the article's thumbnail image in the website
     * @param publicationDate is when the article was published
     */
    Article(String title, String trail, String byLine, URL articleUrl,
                   URL thumbnailUrl, Date publicationDate) {
        this.mTitle = title;
        this.mTrail = trail;
        this.mByLine = byLine;
        this.mArticleUrl = articleUrl;
        this.mThumbnailUrl = thumbnailUrl;
        this.mPublicationDate = publicationDate;
    }

    // Getters

    public String getTitle() {
        return mTitle;
    }

    public String getTrail() {
        return mTrail;
    }

    public String getByLine() {
        return mByLine;
    }

    public URL getArticleUrl() {
        return mArticleUrl;
    }

    public URL getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public Date getPublicationDate() {
        return mPublicationDate;
    }
}