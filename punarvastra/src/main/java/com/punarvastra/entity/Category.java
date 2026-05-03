package com.punarvastra.entity;

import java.io.Serializable;

/**
 * Product category (Men, Women, Kids, Accessories).
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String slug;
    private String description;

    public Category() {
    }

    /**
     * @return category id
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return display name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return URL-safe slug
     */
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * @return optional long description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
