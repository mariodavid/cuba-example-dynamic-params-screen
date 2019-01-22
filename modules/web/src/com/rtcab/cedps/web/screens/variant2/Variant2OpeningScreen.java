package com.rtcab.cedps.web.screens.variant2;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.rtcab.cedps.web.screens.variant2.params.Variant2DynamicParamsScreen;

import javax.inject.Inject;
import java.util.UUID;

@UiController("variant2-opening-screen")
@UiDescriptor("variant2-opening-screen.xml")
public class Variant2OpeningScreen extends Screen {

  @Inject
  protected Metadata metadata;

  @Inject
  private ScreenBuilders screenBuilders;
  @Inject
  private Notifications notifications;

  @Subscribe("openBtn")
  public void openParamsScreen(Button.ClickEvent clickEvent) {

    KeyValueEntity keyValueEntity = createKeyValueEntity();

    Variant2DynamicParamsScreen editor = screenBuilders.screen(this)
            .withScreenClass(Variant2DynamicParamsScreen.class)
            .build();
    editor.setKeyValueEntity(keyValueEntity);

    editor.addAfterCloseListener(afterCloseEvent -> {
      if (afterCloseEvent.getCloseAction().equals(WINDOW_COMMIT_AND_CLOSE_ACTION)) {
        notifications.create().withCaption(editor.getKeyValueEntity().getValue("foo")).show();
      }
    });
    editor.show();

//    AbstractWindow window = openWindow("variant2-dynamic-params-screen", OpenType.DIALOG,
//        ParamsMap.of("keyValueEntity", keyValueEntity, "key", "value"));
//
//    window.addCloseWithCommitListener(() -> showNotification("Foo: " + keyValueEntity.getValue("foo")));
  }

  private KeyValueEntity createKeyValueEntity() {
    KeyValueEntity keyValueEntity = metadata.create(KeyValueEntity.class);

    keyValueEntity.setValue("foo", "bar");
    keyValueEntity.setIdName("id");
    keyValueEntity.setId(UUID.randomUUID());
    return keyValueEntity;
  }
}