package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ContactForm extends FormLayout {

    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    public TextField firstName = new TextField("First Name");
    public TextField lastName = new TextField("Last Name");
    public TextField email = new TextField("Email");
    public ComboBox<Status> status = new ComboBox<>("Status");
    public ComboBox<Company> company = new ComboBox<>("Company");

    public Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Contact contact;

    public ContactForm(List<Company> companies, List<Status> statuses) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);
        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        add(firstName, lastName, email, company, status, createButtonLayout());

    }

    public void setContact(Contact contact){
        this.contact = contact;
        binder.readBean(contact);
    }

    public Component createButtonLayout(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try {
          binder.writeBean(contact);
          fireEvent(new SaveEvent(this, contact));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm>{
        private Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact){
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact(){
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact){
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact){
            super(source, contact);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source){
            super(source, null);
        }
    }

    public <T extends  ComponentEvent<?>> Registration addlistener (Class<T> eventType, ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
