/******************************************************************************
 * Module Five Milestone
 * [AppointmentServiceTest.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class contains unit tests for AppointmentService class. Verifies functionality
 * of updating Appointment fields as well as validation and exception handling.
 *
 * Date: Due 9/29/2024
 *****************************************************************************/
package edu.snhu.dayplanner.appointmentservice;

import edu.snhu.dayplanner.service.IdGenerator;
import org.junit.jupiter.api.*;
import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import edu.snhu.dayplanner.service.appointmentservice.AppointmentService;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


class AppointmentServiceTest
{
    AppointmentService appointmentService;
    LocalDateTime date;

    // Initialize a new AppointmentService for each test
    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService();
        date = LocalDateTime.now().plusMinutes(1); // creates a date 1 min after current time
    }
    // Reset the unique id incrementer to 0 after each test
    @AfterEach
    void tearDown() {
        IdGenerator.resetCounter();
    }

    //Requirement 1: Test adding appointments with unique IDs
    @Test
    @DisplayName("Test adding one appointment works and retrievable from service")
    void testAddOneAppointment() {
        appointmentService.add(date, "have a meeting");

        // appointment with id "0" should exist
        assertNotNull(appointmentService.getById("0"));
        // verify each field matches given input
        assertEquals(appointmentService.getById("0").getDate(), date);
        assertEquals("have a meeting", appointmentService.getById("0").getDescription());
    }
    @Test
    @DisplayName("Test adding one appointment set to current time works and retrievable from service")
    void testAddOneAppointmentWithCurrentTime() {
        appointmentService.add("have a meeting"); // uses current time without date (checked in other tests)

        // appointment with id "0" should exist
        assertNotNull(appointmentService.getById("0"));
        // verify each field matches given input
        assertEquals("have a meeting", appointmentService.getById("0").getDescription());
    }
    @Test
    @DisplayName("Test adding multiple appointments work and all are retrievable from service")
    void testAddMultipleAppointment() {
        appointmentService.add(date, "have a meeting");
        appointmentService.add(date, "another meeting");
        appointmentService.add(date, "another...");
        appointmentService.add(date, "too many meeting");
        appointmentService.add(date, "all the meetings");

        // each appointment should exist and contain specific description
        assertNotNull(appointmentService.getById("0"));
        assertEquals("have a meeting", appointmentService.getById("0").getDescription());
        assertNotNull(appointmentService.getById("1"));
        assertEquals("another meeting", appointmentService.getById("1").getDescription());
        assertNotNull(appointmentService.getById("2"));
        assertEquals("another...", appointmentService.getById("2").getDescription());
        assertNotNull(appointmentService.getById("3"));
        assertEquals("too many meeting", appointmentService.getById("3").getDescription());
        assertNotNull(appointmentService.getById("4"));
        assertEquals("all the meetings", appointmentService.getById("4").getDescription());
    }

    @Test
    @DisplayName("Test adding appointments uses unique IDs")
    void testAddUsesUniqueId() {
        appointmentService.add(date, "meet with joanne");
        appointmentService.add(date, "meet with fred");

        // Ensure both appointments exist and have unique ids
        Appointment appointment1 = appointmentService.getById("0");
        Appointment appointment2 = appointmentService.getById("1");
        assertNotNull(appointment1);
        assertNotNull(appointment2);
        // check both appointments' id are not equal
        assertNotEquals(appointment1.getId(), appointment2.getId());
    }

    // Requirement 2: delete appointments per appointmentId
    @DisplayName("Test deleting appointment by valid ID")
    @Test
    void testDeleteAppointmentWithId() {
        appointmentService.add(date, "have a meeting");
        String id = "0";
        // verify appointment with id '0' exists
        assertNotNull(appointmentService.getById(id));
        // delete appointment by id 0 and verify no longer exists if exception is thrown
        appointmentService.delete(id);
        assertThrows(IllegalArgumentException.class, () -> appointmentService.getById(id));
    }

    @DisplayName("Test deleting an appointment does not alter other appointments")
    @Test
    void testDeleteAppointmentDoesNotAffectOthers() {
        appointmentService.add(date, "have a meeting"); // id = 0
        appointmentService.add(date, "have a meeting"); // id = 1

        // verify both appointments exist
        assertNotNull(appointmentService.getById("0"));
        assertNotNull(appointmentService.getById("1"));

        // delete appointment by id 0 and verify no longer exists if exception is thrown
        appointmentService.delete("0");
        assertThrows(IllegalArgumentException.class, () -> appointmentService.getById("0"));

        // verify other appointment still exists
        assertNotNull(appointmentService.getById("1"));
    }

    // Requirement 3: Update appointment fields per appointmentId
    @Nested
    @DisplayName("Tests for Updating Appointment Fields")
    class UpdateAppointmentTests
    {
        // Add appointment to modify before each test
        @BeforeEach
        void add() {
            appointmentService.add(date, "have a meeting");
        }

        @Nested
        @DisplayName("Test Update Methods with Valid Input")
        class ValidUpdateTests {
            @DisplayName("Test updating date with current time, should not throw exception")
            @Test
            void testUpdateDateWithCurrentTime() {
                LocalDateTime current = appointmentService.updateDate("0"); // update date with current date for id = 0
                // verifies date is within 5ms of current system time--accounts for difference in time between statement
                assertTrue(Math.abs(current.getNano() - LocalDateTime.now().getNano()) <= 5000000);
                // time for appointment with id"0" should match updated date field
                assertEquals(current, appointmentService.getById("0").getDate());
            }
            @DisplayName("Test updating date with 1ms future time")
            @Test
            void testUpdateDateWithFutureTime() {
                LocalDateTime future = LocalDateTime.now().plusNanos(1000000);
                appointmentService.updateDate("0", future); // update date with current date for id = 0
                // time for appointment with id"0" should match updated date field
                assertEquals(future, appointmentService.getById("0").getDate());
            }

            @DisplayName("Test updating description with 50 characters")
            @Test
            void testUpdateDescriptionWithValidName() {
                String id = "0";
                appointmentService.updateDescription(id, "this is an example of a 50 character description..");
                // appointment with id "0" should have updated last name field
                assertEquals("this is an example of a 50 character description..",
                        appointmentService.getById(id).getDescription());
            }
        }

        @Nested
        @DisplayName("Test Update Methods with Invalid Input")
        class InvalidUpdateTests {

            @DisplayName("Test updating date with date in the past")
            @Test
            void testUpdateDateWithDateInPast() {
                // get a date 1 ms behind current system time
                date = LocalDateTime.now().minusNanos(1000000);

                // update date with invalid date, check for exception
                assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDate("0", date));
            }

            @Nested
            @DisplayName("Test Update Methods with Invalid Id")
            class InvalidUpdateTestsWithInvalidId {
                @DisplayName("Test updating date with invalid id")
                @Test
                void testUpdateDateWithInvalidId() {
                    // appointment with id "1" does not exist and should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDate("1", date));
                }

                @DisplayName("Test updating description with invalid id")
                @Test
                void testUpdateDescriptionWithInvalidId() {
                    // appointment with id "1" does not exist and should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("1", "some description"));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Invalid Character Lengths")
            class InvalidUpdateTestsWithInvalidLengths {
                @DisplayName("Test updating description with 0 characters")
                @Test
                void testUpdateDescriptionWithEmptyDescription() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("0", ""));
                }

                @DisplayName("Test updating description with 51 characters")
                @Test
                void testUpdateDescriptionWithInvalidDescription() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("0", "this is an example of a 51 character description..."));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Null Input")
            class InvalidUpdateTestsWithNullInput {
                @DisplayName("Test updating date with null")
                @Test
                void testUpdateDateWithNullDate() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDate("0", null));
                }

                @DisplayName("Test updating description with null")
                @Test
                void testUpdateDescriptionWithNullDescription() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("0", null));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with whitespace")
            class InvalidUpdateTestsWithWhitespace
            {
                @DisplayName("Test updating description with full whitespace, should throw exception")
                @Test
                void testUpdateDescriptionWithWhitespace() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("0", "                    "));
                }

                @DisplayName("Test updating description with trailing whitespace, should throw exception")
                @Test
                void testUpdateDescriptionWithTrailingWhitespace() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("0", "this has trailing whitespace "));
                }

                @DisplayName("Test updating description with trailing whitespace, should throw exception")
                @Test
                void testUpdateDescriptionWithLeadingWhitespace() {
                    // appointment with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> appointmentService.updateDescription("0", " this has leading whitespace"));
                }
            }

            @DisplayName("Test updating unknown field")
            @Test
            void testUpdateUnknownField() {
                assertThrows(IllegalArgumentException.class, ()-> appointmentService.updateField("0", Appointment.Field.valueOf("unknown"), "value"));
            }
        }
    }
}