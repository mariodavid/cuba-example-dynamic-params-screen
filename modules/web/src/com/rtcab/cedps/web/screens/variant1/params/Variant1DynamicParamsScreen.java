package com.rtcab.cedps.web.screens.variant1.params;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.screen.*;

@UiController
@UiDescriptor("variant1-dynamic-params-screen.xml")
@EditedEntityContainer("keyValueEntityDc")
@LoadDataBeforeShow
public class Variant1DynamicParamsScreen extends StandardEditor<KeyValueEntity> {
}