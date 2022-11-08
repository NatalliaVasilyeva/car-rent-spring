package com.dmdev.domain.projection;

import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;

public interface ModelView {

    Long getId();

    String getName();

    Transmission getTransmission();

    EngineType getEngineType();

    CategoryView getCategory();
}