package com.dmdev.domain.projection;


import java.util.List;

public interface BrandFullView {

    Long getId();

    String getName();

    List<ModelView> getModels();
}