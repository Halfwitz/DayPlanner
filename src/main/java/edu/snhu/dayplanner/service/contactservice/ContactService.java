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
 *****************************************************************************/
package edu.snhu.dayplanner.service.contactservice;

import edu.snhu.dayplanner.service.Service;
import edu.snhu.dayplanner.service.ServiceFileUtility;

import java.util.Arrays;
import java.util.List;

public class ContactService extends Service<Contact, Contact.Field> {

    public ContactService() {
        super(Arrays.asList(Contact.Field.values()));
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
        return add(entity);
    }

    /**
     * Reads stored CSV contents from a file to this storage object.
     * Uses {@code ServiceFileUtility} to read a specified CSV file then convert each line to
     * a contact and return a list of those contacts. Then, the list is added to this object.
     * Should convert contents to the object and add with {@code add} or {@code addAll}
     * Passes a prototype contact object to use for creation of new objects with (fromCsv)
     * @param filePath the file to be read from into this object
     */
    @Override
    public void addFromFile(String filePath) {
        ServiceFileUtility<Contact> fileUtil = new ServiceFileUtility<>(filePath,
                new Contact("p", "p", "0000000000", "0"));
        addAll(fileUtil.readFromFile());
    }

    /**
     * Writes stored objects to a CSV file stored in filePath.
     * Uses {@code ServiceFileUtility} to convert Contact objects into CSV format and writes
     * to the file.
     *
     * @param filePath the file to be written into using contents of this object
     */
    @Override
    public void writeToFile(String filePath) {
        ServiceFileUtility<Contact> fileUtil = new ServiceFileUtility<>(filePath,
                new Contact("p", "p", "0000000000", "0"));
        fileUtil.writeToFile(getAll());
    }

    // UPDATE CONTACT FIELDS
    /**
     * Updates first name of contact with given id to firstName
     * @param id Unique identifier of the contact to update
     * @param firstName new first name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if contact does not exist or firstName is invalid
     */
    public void updateFirstName(String id, String firstName) {
        updateField(id, Contact.Field.FIRST_NAME, firstName);
    }

    /**
     * Updates last name of contact with given id to lastName
     * @param id Unique identifier of the contact to update
     * @param lastName new last name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if contact does not exist or lastName is invalid
     */
    public void updateLastName(String id, String lastName) {
        updateField(id, Contact.Field.LAST_NAME, lastName);
    }

    /**
     * Updates phone number of contact with given id to phoneNumber
     * @param id Unique identifier of the contact to update
     * @param phoneNumber new phone number (non-null, must be 10 chars)
     * @throws IllegalArgumentException if contact does not exist or phoneNumber is invalid
     */
    public void updatePhoneNumber(String id, String phoneNumber) {
        updateField(id, Contact.Field.PHONE_NUMBER, phoneNumber);
    }

    /**
     * Updates address of contact with given id to address
     * @param id Unique identifier of the contact to update
     * @param address new address (non-null, <= 30 chars)
     * @throws IllegalArgumentException if contact does not exist or address is invalid
     */
    public void updateAddress(String id, String address) {
        updateField(id, Contact.Field.ADDRESS, address);

    }
}