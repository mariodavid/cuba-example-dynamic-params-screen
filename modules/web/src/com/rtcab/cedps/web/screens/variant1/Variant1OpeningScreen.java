package com.rtcab.cedps.web.screens.variant1;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.rtcab.cedps.web.screens.variant1.params.Variant1DynamicParamsScreen;

import javax.inject.Inject;
import java.util.UUID;

@UiController("variant1-opening-screen")
@UiDescriptor("variant1-opening-screen.xml")
public class Variant1OpeningScreen extends Screen {

  @Inject
  protected Metadata metadata;

  @Inject
  private ScreenBuilders screenBuilders;
  @Inject
  private Notifications notifications;

  @Subscribe("openBtn")
  public void openParamsScreen(Button.ClickEvent clickEvent) {
    KeyValueEntity keyValueEntity = createKeyValueEntity();

    Variant1DynamicParamsScreen editor = screenBuilders.editor(KeyValueEntity.class, this)
            .withScreenClass(Variant1DynamicParamsScreen.class)
            .editEntity(keyValueEntity)
            .build();
    editor.addAfterCloseListener(afterCloseEvent -> {
      if (afterCloseEvent.getCloseAction().equals(WINDOW_COMMIT_AND_CLOSE_ACTION)) {
        notifications.create().withCaption(editor.getEditedEntity().getValue("foo")).show();
      }
    });
    editor.show();

//    AbstractEditor editor = openEditor("variant1-dynamic-params-screen", keyValueEntity, OpenType.DIALOG,
//        ParamsMap.of("key", "value"));
//
//    editor.addCloseWithCommitListener(() -> showNotification("Foo: " + keyValueEntity.getValue("foo")));
  }

  private KeyValueEntity createKeyValueEntity() {
    KeyValueEntity keyValueEntity = metadata.create(KeyValueEntity.class);

    keyValueEntity.setValue("foo", "bar");
    keyValueEntity.setIdName("id");
    keyValueEntity.setId(UUID.randomUUID());
    return keyValueEntity;
  }
}