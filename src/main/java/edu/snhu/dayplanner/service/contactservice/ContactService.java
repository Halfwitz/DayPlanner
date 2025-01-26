/******************************************************************************
 * Module Three Milestone
 * [ContactService.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class supports managing Contact objects, supports functionality:
 * - Add new contacts with unique id
 * - Update fields of a contact by its id (first name, last name, phone number, address)
 * - Delete contacts by their id
 *
 * Date: Due 9/22/2024
 * Modified: 9/25/2024 to extend Service
 * Modified: 10/09/2024 to remove outer package dependencies
 * Modified: 10/11/2024 to merge with superclass
 *****************************************************************************/
package edu.snhu.dayplanner.service.contactservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactService {
    /**
     * Container for set of entities, maps entity id to entity object.
     * (Use entity extensions (Task, Contact, Appointment))
     */
    private final Map<String, Contact> entityMap = new HashMap<>();

    /**
     * Adds an object to the service storage, mapped to its id.
     * @param object object to add to service.
     */
    protected void add(Contact object) {
        entityMap.put(object.getId(), object);
    }

    /**
     * Adds a contact object mapped to its unique id storage.
     * @param firstName Contact's first name
     * @param lastName Contact's last name
     * @param phoneNumber Contacts phone number
     * @param address Contacts address
     * @return reference to Contact object created
     * @throws IllegalArgumentException in Contact object if parameters are invalid format
     */
    public Contact add(String firstName, String lastName, String phoneNumber, String address) {
        Contact entity = new Contact(firstName, lastName, phoneNumber, address);
        add(entity);
        return entity;
    }

    /**
     * Removes object with given id from contacts map
     * @param object object to be removed from lsit
     * @return object that was removed
     * @throws IllegalArgumentException if contact does not exist
     */
    public Contact delete(Contact object) {
        return entityMap.remove(object.getId());
    }

    /**
     * Removes object with given id from contacts map
     * @param id identifier of object to be removed from contacts map
     * @return object that was removed
     * @throws IllegalArgumentException if object does not exist
     */
    public Contact delete(String id) {
        return delete(getEntityById(id));
    }

    // UPDATE CONTACT FIELDS
    /**
     * Update specified string field implemented in updatedField method implemented from Entity
     * @param id Unique identifier of the object to delete
     * @param field the field that is being modified (must be firstName, lastName, phone, address)
     * @param value new value to change specified field to
     * @throws IllegalArgumentException if object does not exist or field string is invalid
     */
    public void updateEntityField(String id, Contact.Field field, String value) {
        Contact entity = getEntityById(id); // throws exception if entity not found
        entity.updateField(field, value); // throws exception if fieldname or value invalid
    }

    /**
     * Updates first name of contact with given id to firstName
     * @param id Unique identifier of the contact to update
     * @param firstName new first name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if contact does not exist or firstName is invalid
     */
    public void updateFirstName(String id, String firstName) {
        updateEntityField(id, Contact.Field.FIRST_NAME, firstName);
    }

    /**
     * Updates last name of contact with given id to lastName
     * @param id Unique identifier of the contact to update
     * @param lastName new last name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if contact does not exist or lastName is invalid
     */
    public void updateLastName(String id, String lastName) {
        updateEntityField(id, Contact.Field.LAST_NAME, lastName);
    }

    /**
     * Updates phone number of contact with given id to phoneNumber
     * @param id Unique identifier of the contact to update
     * @param phoneNumber new phone number (non-null, must be 10 chars)
     * @throws IllegalArgumentException if contact does not exist or phoneNumber is invalid
     */
    public void updatePhoneNumber(String id, String phoneNumber) {
        updateEntityField(id, Contact.Field.PHONE_NUMBER, phoneNumber);
    }

    /**
     * Updates address of contact with given id to address
     * @param id Unique identifier of the contact to update
     * @param address new address (non-null, <= 30 chars)
     * @throws IllegalArgumentException if contact does not exist or address is invalid
     */
    public void updateAddress(String id, String address) {
        updateEntityField(id, Contact.Field.ADDRESS, address);

    }

    /**
     * Return an entity of type Contact from the stored map
     * @param id the unique id used to identity entity in map
     * @return entity of type T associated with given id key from map
     * @throws IllegalArgumentException if entity with specified id can't be found
     */
    public Contact getEntityById(String id) {
        Contact entity = entityMap.get(id);
        if (entity == null) {
            throw new IllegalArgumentException("Object with ID [" + id + "] does not exist");
        }
        return entity;
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(entityMap.values());
    }
}