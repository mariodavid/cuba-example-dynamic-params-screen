package com.rtcab.cedps.web.screens.variant2.params

import com.haulmont.cuba.core.entity.KeyValueEntity
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.WindowParam
import com.haulmont.cuba.gui.components.AbstractWindow
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.gui.components.FieldGroup
import com.haulmont.cuba.gui.components.FieldGroup.FieldConfig
import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.TextInputField
import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.gui.data.DsBuilder
import com.haulmont.cuba.gui.data.impl.ValueCollectionDatasourceImpl
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory

import javax.inject.Inject

class Variant2DynamicParamsScreen extends AbstractWindow {

    @Inject
    ComponentsFactory componentsFactory

    @WindowParam
    KeyValueEntity keyValueEntity

    @Inject
    FieldGroup fieldGroup

    @Override
    void init(Map<String, Object> params) {
        super.init(params)

        ValueCollectionDatasourceImpl datasource = createDynamicParamsDatasource()

        datasource.addProperty("foo", String)

        datasource.addItem(keyValueEntity)
        datasource.setItem(keyValueEntity)

        fieldGroup.setSizeFull()

        FieldConfig fieldConfig = fieldGroup.createField("foo")
        fieldConfig.setProperty("foo")
        fieldConfig.caption = "Foo"
        fieldGroup.addField(fieldConfig)

        fieldGroup.setDatasource(datasource)
        fieldGroup.bind()
    }

    protected ValueCollectionDatasourceImpl createDynamicParamsDatasource() {
        new DsBuilder(getDsContext())
            .setJavaClass(KeyValueEntity.class)
            .setId("paramsDs")
            .buildValuesCollectionDatasource()
    }

    void saveParams() {

        def datasource = getDsContext().get('paramsDs')

        showNotification("Foo before close: " + datasource.getItem().getValue('foo'))

        close(Editor.COMMIT_ACTION_ID, true)
    }
}