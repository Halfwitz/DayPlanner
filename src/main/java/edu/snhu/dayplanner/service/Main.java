package edu.snhu.dayplanner.service;

import edu.snhu.dayplanner.service.contactservice.Contact;
import edu.snhu.dayplanner.service.contactservice.ContactService;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Service<Contact, Contact.Field> contacts = new ContactService();
        Contact c = contacts.add(new Contact ("Michael", "Lorenz", "2153039298", "J387 W Broad St."));
        Contact r = contacts.add(new Contact("John", "Smith", "3234567890", "125 Main St."));
        EntityBST<Contact, Contact.Field> contactTree = new EntityBST<>(Arrays.asList(Contact.Field.values()));
        contactTree.insert(r);
        contactTree.insert(new Contact("Larry", "3Lorenzo", "J153039000", "385 W Broad St"));
        contactTree.insert(new Contact("3Johnny", "JAppleseed", "9999999999", "Wallaby Ave"));
        contactTree.insert(c);

        System.out.println();
        for (Contact contact : contactTree.findAllStartingWith("J", Contact.Field.LAST_NAME)) {
            System.out.println(contact);
        }
        System.out.println(contactTree.findStartingWith("3", Contact.Field.ADDRESS));

    }
}
