# CUBA example - dynamic params screen

This example should show how to create a screen that renders dynamic attributes based on the given input.

## Variant 1: KevValueEntity editor

In the first variant a KV entity editor is used. It will be passed into the dynamic params screen:

```
KeyValueEntity keyValueEntity = createKeyValueEntity();

AbstractEditor editor = openEditor("variant1-dynamic-params-screen", keyValueEntity, OpenType.DIALOG,
    ParamsMap.of("key", "value"));
``` 

This leads to an "Access denied" message in the UI and the following stack trace:
```
2018-12-21 16:43:58.497 ERROR [http-nio-8080-exec-1/app/admin] com.haulmont.cuba.web.log.AppLog - Exception in com.haulmont.cuba.web.toolkit.ui.CubaButton: 
com.vaadin.server.ServerRpcManager$RpcInvocationException: Unable to invoke method click in com.vaadin.shared.ui.button.ButtonServerRpc
 
 ... 38 common frames omitted
 
 
Caused by: com.vaadin.event.ListenerMethod$MethodException: Invocation of method buttonClick in com.haulmont.cuba.web.gui.components.WebButton$$Lambda$90/326805039 failed.
 
 
 ... 42 common frames omitted
Caused by: java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
 at com.haulmont.cuba.gui.xml.DeclarativeAction.actionPerform(DeclarativeAction.java:94) ~[cuba-gui-6.10.4.jar:6.10.4]
 ... 48 common frames omitted
 
Caused by: java.lang.reflect.InvocationTargetException: null
 at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_172]
 at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_172]
 at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_172]
 at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_172]
 at com.haulmont.cuba.gui.xml.DeclarativeAction.actionPerform(DeclarativeAction.java:92) ~[cuba-gui-6.10.4.jar:6.10.4]
 ... 54 common frames omitted
Caused by: com.haulmont.cuba.core.global.EntityAccessException: Unable to load entity sys$KeyValueEntity-a3bda85e-fac8-4056-9756-495a502529ed because it has been deleted or access denied
 at com.haulmont.cuba.client.sys.DataManagerClientImpl.reload(DataManagerClientImpl.java:93) ~[cuba-client-6.10.4.jar:6.10.4]
 at com.haulmont.cuba.gui.data.impl.GenericDataSupplier.reload(GenericDataSupplier.java:55) ~[cuba-gui-6.10.4.jar:6.10.4]
 at com.haulmont.cuba.gui.components.EditorWindowDelegate.setItem(EditorWindowDelegate.java:155) ~[cuba-gui-6.10.4.jar:6.10.4]
 at com.haulmont.cuba.web.gui.WebWindow$Editor.setItem(WebWindow.java:1701) ~[cuba-web-6.10.4.jar:6.10.4]
 at com.haulmont.cuba.gui.components.AbstractEditor.setItem(AbstractEditor.java:71) ~[cuba-gui-6.10.4.jar:6.10.4]
 ... 59 common frames omitted
```


## Variant 2: AbstractWindow with KV entity as a screen param

The screen can be opened there, but the problem is that the reference is not updated:

#### Opening screen
```
KeyValueEntity keyValueEntity = createKeyValueEntity();

    AbstractWindow window = openWindow("variant2-dynamic-params-screen", OpenType.DIALOG,
        ParamsMap.of("keyValueEntity", keyValueEntity, "key", "value"));

    window.addCloseWithCommitListener(() -> showNotification("Foo: " + keyValueEntity.getValue("foo")));
```

#### AbstractWindow
```

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


    fieldGroup.setDatasource(datasource)


    fieldGroup.setSizeFull()

    FieldConfig fieldConfig = fieldGroup.createField("foo")
    fieldConfig.component = componentsFactory.createComponent(TextInputField.class)
    fieldConfig.caption = "Foo"
    fieldGroup.addField(fieldConfig)
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
```

This show the notifications:

1. "Foo before close: bar"
2. "Foo: bar"

Expected behavior:

0. User enters "Hello" for field "foo"
1. "Foo before close: Hello"
2. "Foo: Hello"


