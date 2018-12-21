package com.rtcab.cedps.web.screens.variant1;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.AbstractWindow;
import java.util.UUID;
import javax.inject.Inject;

public class Variant1OpeningScreen extends AbstractWindow {


  @Inject
  protected Metadata metadata;

  public void openParamsScreen() {

    KeyValueEntity keyValueEntity = createKeyValueEntity();

    AbstractEditor editor = openEditor("variant1-dynamic-params-screen", keyValueEntity, OpenType.DIALOG,
        ParamsMap.of("key", "value"));

    editor.addCloseWithCommitListener(() -> showNotification("Foo: " + keyValueEntity.getValue("foo")));
  }

  private KeyValueEntity createKeyValueEntity() {
    KeyValueEntity keyValueEntity = metadata.create(KeyValueEntity.class);

    keyValueEntity.setValue("foo", "bar");
    keyValueEntity.setIdName("id");
    keyValueEntity.setId(UUID.randomUUID());
    return keyValueEntity;
  }
}