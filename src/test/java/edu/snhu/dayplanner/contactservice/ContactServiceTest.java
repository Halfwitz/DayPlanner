/******************************************************************************
 * Module Three Milestone
 * [ContactServiceTest.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class contains unit tests for ContactService class. Verifies functionality
 * of updating Contact fields as well as validation and exception handling.
 *
 * Date: Due 9/22/2024
 * Modified: 9/25/2024 to support code restructure
 *****************************************************************************/
package edu.snhu.dayplanner.contactservice;

import edu.snhu.dayplanner.service.IdGenerator;
import org.junit.jupiter.api.*;
import edu.snhu.dayplanner.service.contactservice.Contact;
import edu.snhu.dayplanner.service.contactservice.ContactService;

import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest
{
    ContactService contactService;

    // Initialize a new ContactService for each test
    @BeforeEach
    void setUp() {
        contactService = new ContactService();
    }
    // Reset the unique id incrementer to 0 after each test
    @AfterEach
    void tearDown() {
        IdGenerator.resetCounter();
    }

    //Requirement: Test adding contacts with unique IDs
    @Test
    @DisplayName("Test adding one contact works")
    void testAddOneContact() {
        contactService.add("Jon", "Snow", "1234567890", "The Wall");

        // contact with id "0" should exist
        assertNotNull(contactService.getById("0"));
        // verify each field matches given input
        assertEquals(contactService.getById("0").getFirstName(), "Jon");
        assertEquals(contactService.getById("0").getLastName(), "Snow");
        assertEquals(contactService.getById("0").getPhone(), "1234567890");
        assertEquals(contactService.getById("0").getAddress(), "The Wall");
    }
    @Test
    @DisplayName("Test adding contacts uses unique IDs")
    void testAddUsesUniqueId() {
        contactService.add("Jon", "Snow", "1234567890", "The Wall");
        contactService.add("Bugs", "Bunny", "0000000000", "Looney Town");

        // Ensure both contacts exist and have unique ids
        Contact contact1 = contactService.getById("0");
        Contact contact2 = contactService.getById("1");
        assertNotNull(contact1);
        assertNotNull(contact2);
        // check both contacts' id are not equal
        assertNotEquals(contact1.getId(), contact2.getId());
    }

    // Requirement 2: delete contacts per contactId
    @DisplayName("Test deleting contact by valid ID")
    @Test
    void testDeleteContactWithId() {
        contactService.add("Jon", "Snow", "1234567890", "The Wall");
        String id = "0";
        // verify contact with id '0' exists
        assertNotNull(contactService.getById(id));
        // delete contact by id 0 and verify no longer exists if exception is thrown
        contactService.delete(id);
        assertThrows(IllegalArgumentException.class, () -> contactService.getById(id));
    }

    // Requirement 3: Update contact fields per contactId
    // VALID ID
    @Nested
    @DisplayName("Tests for Updating Contact Fields")
    class UpdateContactTests
    {
        @Nested
        @DisplayName("Test Update Methods with Valid Input")
        class ValidUpdateTests {
            // Add contact to modify before each test
            @BeforeEach
            void add() {
                contactService.add("Jon", "Snow", "1234567890", "The Wall");
            }

            @DisplayName("Test updating firstName with 10 characters")
            @Test
            void testUpdateFirstNameWithValidName() {
                String id = "0";
                contactService.updateFirstName(id, "JonTenChar");
                // contact with id "0" should match updated first name field
                assertEquals("JonTenChar", contactService.getById("0").getFirstName());
            }

            @DisplayName("Test updating lastName with 10 characters")
            @Test
            void testUpdateLastNameWithValidName() {
                String id = "0";
                contactService.updateLastName(id, "Snow10Char");
                // contact with id "0" should have updated last name field
                assertEquals("Snow10Char", contactService.getById(id).getLastName());
            }

            @DisplayName("Test updating phone number with 10 characters")
            @Test
            void testUpdatePhoneNumberWithValidPhone() {
                String id = "0";
                contactService.updatePhoneNumber(id, "0000000000");
                // contact with id "0" should have updated phone number field
                assertEquals("0000000000", contactService.getById(id).getPhone());
            }

            @DisplayName("Test updating address with 30 characters")
            @Test
            void testUpdateAddressWithValidAddress() {
                String id = "0";
                contactService.updateAddress(id, "123456 Some Keep In Winterfell");
                // contact with id "0" should have updated last name field
                assertEquals("123456 Some Keep In Winterfell", contactService.getById(id).getAddress());
            }
        }

        @Nested
        @DisplayName("Test Update Methods with Invalid Input")
        class InvalidUpdateTests {
            // Add contact to modify before each test
            @BeforeEach
            void add() {
                contactService.add("Jon", "Snow", "1234567890", "The Wall");
            }

            @Nested
            @DisplayName("Test Update Methods with Invalid Id")
            class InvalidUpdateTestsWithInvalidId
            {

                @DisplayName("Test updating firstName with invalid id")
                @Test
                void testUpdateFirstNameWithInvalidId() {
                    // contact with id "1" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateFirstName("1", "Johnny"));
                }

                @DisplayName("Test updating lastName with invalid id")
                @Test
                void testUpdateLastNameWithInvalidId() {
                    // contact with id "1" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateLastName("1", "Ice"));
                }

                @DisplayName("Test updating phone number with invalid id")
                @Test
                void testUpdatePhoneNumberWithInvalidId() {
                    // contact with id "1" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updatePhoneNumber("1", "0000000000"));
                }

                @DisplayName("Test updating address with invalid id")
                @Test
                void testUpdateAddressWithInvalidId() {
                    // contact with id "1" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateAddress("1", "Winter is Coming"));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Invalid Character Lengths")
            class InvalidUpdateTestsWithInvalidLengths {
                @DisplayName("Test updating firstName with 11 characters")
                @Test
                void testUpdateFirstNameWithInvalidName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateFirstName("0", "JonElevenCh"));
                }
                @DisplayName("Test updating firstName with 0 characters")
                @Test
                void testUpdateFirstNameWithEmptyName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateFirstName("0", ""));
                }

                @DisplayName("Test updating lastName with 11 characters")
                @Test
                void testUpdateLastNameWithInvalidName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateLastName("0", "SnowingLots"));
                }
                @DisplayName("Test updating lastName with 0 characters")
                @Test
                void testUpdateLastNameWithEmptyName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateLastName("0", ""));
                }

                @DisplayName("Test updating phone number with invalid 9 characters")
                @Test
                void testUpdatePhoneNumberWith9Chars() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updatePhoneNumber("0", "123456789"));
                }

                @DisplayName("Test updating phone number with invalid 11 characters")
                @Test
                void testUpdatePhoneNumberWith11Chars() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updatePhoneNumber("0", "12345678910"));
                }

                @DisplayName("Test updating address with 31 characters")
                @Test
                void testUpdateAddressWithInvalidAddress() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateAddress("0", "1234567 Some Keep In Winterfell"));
                }
                @DisplayName("Test updating address with 0 characters")
                @Test
                void testUpdateAddressWithEmptyAddress() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateAddress("0", ""));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Null Input")
            class InvalidUpdateTestsWithNullInput {
                @DisplayName("Test updating firstName with null")
                @Test
                void testUpdateFirstNameWithNullName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateFirstName("0", null));
                }

                @DisplayName("Test updating lastName with null")
                @Test
                void testUpdateLastNameWithNullName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateLastName("0", null));
                }

                @DisplayName("Test updating phone number with null")
                @Test
                void testUpdatePhoneNumberWithNull() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updatePhoneNumber("0", null));
                }

                @DisplayName("Test updating address with null")
                @Test
                void testUpdateAddressWithNullAddress() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateAddress("0", null));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Whitespace Input")
            class InvalidUpdateTestsWithWhitespaceInput {
                @DisplayName("Test updating firstName with white space")
                @Test
                void testUpdateFirstNameWithWhitespaceName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateFirstName("0", "          "));
                }

                @DisplayName("Test updating lastName with white space")
                @Test
                void testUpdateLastNameWithWhitespaceName() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateLastName("0", "          "));
                }

                @DisplayName("Test updating phone number with white space")
                @Test
                void testUpdatePhoneNumberWithWhitespace() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updatePhoneNumber("0", "          "));
                }

                @DisplayName("Test updating address with white space")
                @Test
                void testUpdateAddressWithWhitespaceAddress() {
                    // contact with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> contactService.updateAddress("0", "                              "));
                }
            }

            @DisplayName("Test updating unknown field")
            @Test
            void testUpdateUnknownField() {
                assertThrows(IllegalArgumentException.class, ()-> contactService.updateField("0", Contact.Field.valueOf("unknown"), "value"));
            }
        }
    }
}