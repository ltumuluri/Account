package org.uftwf.account.model;

/**
 * Created by xyang on 3/27/17.
 */
public class ProjectVersion {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String version;
    private String description;
}
