package de.helaba.jets.g2.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * access main view via:
 * http://localhost:8080/
 */
@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Button("Click me", e -> Notification.show("Hello, this is a G2 prototype!")));
    }

}
