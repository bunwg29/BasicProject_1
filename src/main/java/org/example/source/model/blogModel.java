package org.example.source.model;

public class blogModel {
    private String blogName;
    private String blogLink;

    private String blogImageLink;

    public blogModel(String blogName, String blogLink, String blogImageLink) {
        this.blogName = blogName;
        this.blogLink = blogLink;
        this.blogImageLink = blogImageLink;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogLink() {
        return blogLink;
    }

    public void setBlogLink(String blogLink) {
        this.blogLink = blogLink;
    }

    public String getBlogImageLink() {
        return blogImageLink;
    }

    public void setBlogImageLink(String blogImageLink) {
        this.blogImageLink = blogImageLink;
    }

    @Override
    public String toString() {
        return "blogModel{" +
                "blogName='" + blogName + '\'' +
                ", blogLink='" + blogLink + '\'' +
                ", blogImageLink='" + blogImageLink + '\'' +
                '}';
    }
}
