package com.example.application.views.list;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;


import java.awt.*;

public class MainLayout extends AppLayout {

    private SecurityService service;

    public MainLayout(SecurityService service){
        this.service = service;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
         H1 logo = new H1("Vaadin CRM");
         logo.addClassNames("text-l","m-m");

         Button logout = new Button("log out", event -> service.logout());
         logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

         HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);

         header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
         header.expand(logo);
         header.setWidthFull();
         header.addClassNames("py-0", "px-0");

         addToNavbar(header);
    }

    private void createDrawer(){
        RouterLink listView = new RouterLink("List", ListView.class);
        RouterLink dashBoardView = new RouterLink("Dashboard", DashboardView.class);
        listView.setHighlightCondition(HighlightConditions.sameLocation());
        dashBoardView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listView,
                dashBoardView
        ));
    }

}
