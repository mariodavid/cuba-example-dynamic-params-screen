package com.rtcab.cedps.web.screens.variant3.params

import com.haulmont.cuba.core.entity.KeyValueEntity
import com.haulmont.cuba.gui.WindowParam
import com.haulmont.cuba.gui.components.AbstractWindow
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.gui.components.Field
import com.haulmont.cuba.gui.components.FieldGroup
import com.haulmont.cuba.gui.components.FieldGroup.FieldConfig
import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.TextInputField
import com.haulmont.cuba.gui.data.DsBuilder
import com.haulmont.cuba.gui.data.impl.ValueCollectionDatasourceImpl
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory

import javax.inject.Inject
import javax.swing.text.EditorKit
import java.util.function.Consumer

class Variant3DynamicParamsScreen extends AbstractWindow {

  @Inject
  ComponentsFactory componentsFactory


  @Inject
  FieldGroup fieldGroup

  Consumer<Map<String, Object>> paramValuesHandler

  public void setParamValuesHandler(Consumer<Map<String, Object>> paramValuesHandler) {
    this.paramValuesHandler = paramValuesHandler
  }

  @Override
  void init(Map<String, Object> params) {
    super.init(params)


    fieldGroup.setSizeFull()

    FieldConfig fieldConfig = fieldGroup.createField("foo")
    fieldConfig.component = componentsFactory.createComponent(TextField.class)
    fieldConfig.caption = "Foo"
    fieldGroup.addField(fieldConfig)
  }

  void saveParams() {

    def fooComponent = fieldGroup.getField("foo").getComponent() as TextField
    def paramValues = [
        foo: fooComponent.value
    ]

    showNotification("Foo before close: " + paramValues['foo'])

    closeAndRun(Editor.COMMIT_ACTION_ID, new Runnable() {
      @Override
      void run() {
        paramValuesHandler.accept(paramValues);
      }
    })
  }
}
