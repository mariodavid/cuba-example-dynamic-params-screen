package com.rtcab.cedps.web.screens.variant2.params;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.KeyValueContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController
@UiDescriptor("variant2-dynamic-params-screen.xml")
public class Variant2DynamicParamsScreen extends Screen {

    private KeyValueEntity keyValueEntity;

    @Inject
    private KeyValueContainer container;

    @Inject
    private Form form;

    @Inject
    private UiComponentsGenerator uiComponentsGenerator;

    @Inject
    private ClientConfig clientConfig;

    @Inject
    private Messages messages;

    @Inject
    private Icons icons;

    @Inject
    private ScreenValidation screenValidation;

    public KeyValueEntity getKeyValueEntity() {
        return keyValueEntity;
    }

    public void setKeyValueEntity(KeyValueEntity keyValueEntity) {
        this.keyValueEntity = keyValueEntity;
    }

    @Subscribe
    protected void onInit(InitEvent event) {
        Action commitAndCloseAction = new BaseAction("windowCommitAndClose")
                .withCaption(messages.getMainMessage("actions.Ok"))
                .withIcon(icons.get(CubaIcon.EDITOR_OK))
                .withPrimary(true)
                .withShortcut(clientConfig.getCommitShortcut())
                .withHandler(this::commitAndClose);
        getWindow().addAction(commitAndCloseAction);

        Action closeAction = new BaseAction("windowClose")
                .withIcon(icons.get(CubaIcon.EDITOR_CANCEL))
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withHandler(this::cancel);
        getWindow().addAction(closeAction);

        container.addProperty("foo");
        Field fooField = (Field) uiComponentsGenerator.generate(
                new ComponentGenerationContext(container.getEntityMetaClass(), "foo"));
        fooField.setCaption("Foo");
        fooField.setValueSource(new ContainerValueSource<>(container, "foo"));
        form.add(fooField);
    }

    private void commitAndClose(Action.ActionPerformedEvent actionPerformedEvent) {
        ValidationErrors validationErrors = screenValidation.validateUiComponents(getWindow());
        if (!validationErrors.isEmpty()) {
            screenValidation.showValidationErrors(this, validationErrors);
            return;
        }
        close(WINDOW_COMMIT_AND_CLOSE_ACTION);
    }

    private void cancel(Action.ActionPerformedEvent actionPerformedEvent) {
        close(WINDOW_CLOSE_ACTION);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        container.setItem(keyValueEntity);
    }
}
