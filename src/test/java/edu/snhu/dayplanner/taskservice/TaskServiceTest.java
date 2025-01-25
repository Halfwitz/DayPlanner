/******************************************************************************
 * Module Three Milestone
 * [TaskServiceTest.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class contains unit tests for TaskService class. Verifies functionality
 * of updating Task fields as well as validation and exception handling.
 *
 * Date: Due 9/29/2024
 *****************************************************************************/
package edu.snhu.dayplanner.taskservice;

import org.junit.jupiter.api.*;
import edu.snhu.dayplanner.service.taskservice.Task;
import edu.snhu.dayplanner.service.taskservice.TaskService;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest
{
    TaskService taskService;

    // Initialize a new TaskService for each test
    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }
    // Reset the unique id incrementer to 0 after each test
    @AfterEach
    void tearDown() {
        Task.resetCounter();
    }

    //Requirement: Test adding tasks with unique IDs
    @Test
    @DisplayName("Test adding one task works")
    void testAddOneTask() {
        taskService.add("eat dinner", "cook some lasagna");

        // task with id "0" should exist
        assertNotNull(taskService.getEntityById("0"));
        // verify each field matches given input
        assertEquals(taskService.getEntityById("0").getName(), "eat dinner");
        assertEquals(taskService.getEntityById("0").getDescription(), "cook some lasagna");
    }

    @Test
    @DisplayName("Test adding tasks uses unique IDs")
    void testAddUsesUniqueId() {
        taskService.add("Shovel some snow", "get snow off the porch");
        taskService.add("Read a book", "read 100 pages");

        // Ensure both tasks exist and have unique ids
        Task task1 = taskService.getEntityById("0");
        Task task2 = taskService.getEntityById("1");
        assertNotNull(task1);
        assertNotNull(task2);
        // check both tasks' id are not equal
        assertNotEquals(task1.getId(), task2.getId());
    }

    // Requirement 2: delete tasks per taskId
    @DisplayName("Test deleting task by valid ID")
    @Test
    void testDeleteTaskWithId() {
        taskService.add("eat dinner", "cook some lasagna");
        String id = "0";
        // verify task with id '0' exists
        assertNotNull(taskService.getEntityById(id));
        // delete task by id 0 and verify no longer exists if exception is thrown
        taskService.delete(id);
        assertThrows(IllegalArgumentException.class, () -> taskService.getEntityById(id));
    }

    // Requirement 3: Update task fields per taskId
    // VALID ID
    @Nested
    @DisplayName("Tests for Updating Task Fields")
    class UpdateTaskTests
    {
        // Add task to modify before each test
        @BeforeEach
        void add() {
            taskService.add("eat dinner", "cook some lasagna");
        }

        @Nested
        @DisplayName("Test Update Methods with Valid Input")
        class ValidUpdateTests {
            @DisplayName("Test updating name with 20 characters")
            @Test
            void testUpdateNameWithValidName() {
                String id = "0";
                taskService.updateName(id, "this task is 20 char");
                // task with id "0" should match updated name field
                assertEquals("this task is 20 char", taskService.getEntityById("0").getName());
            }

            @DisplayName("Test updating description with 50 characters")
            @Test
            void testUpdateDescriptionWithValidName() {
                String id = "0";
                taskService.updateDescription(id, "this is an example of a 50 character description..");
                // task with id "0" should have updated description field
                assertEquals("this is an example of a 50 character description..",
                        taskService.getEntityById(id).getDescription());
            }
        }

        @Nested
        @DisplayName("Test Update Methods with Invalid Input")
        class InvalidUpdateTests {

            @Nested
            @DisplayName("Test Update Methods with Invalid Id")
            class InvalidUpdateTestsWithInvalidId
            {

                @DisplayName("Test updating name with invalid id")
                @Test
                void testUpdateNameWithInvalidId() {
                    // task with id "1" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateName("1", "some task"));
                }

                @DisplayName("Test updating description with invalid id")
                @Test
                void testUpdateDescriptionWithInvalidId() {
                    // task with id "1" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateDescription("1", "some description"));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Invalid Character Lengths")
            class InvalidUpdateTestsWithInvalidLengths
            {
                @DisplayName("Test updating name with 0 characters")
                @Test
                void testUpdateNameWithEmptyName() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateName("0", ""));
                }

                @DisplayName("Test updating name with 21 characters")
                @Test
                void testUpdateNameWithInvalidName() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateName("0", "this task is 21 chars"));
                }

                @DisplayName("Test updating description with 0 characters")
                @Test
                void testUpdateDescriptionWithEmptyDescription() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateDescription("0", ""));
                }

                @DisplayName("Test updating description with 51 characters")
                @Test
                void testUpdateDescriptionWithInvalidDescription() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateDescription("0", "this is an example of a 51 character description..."));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with Null Input")
            class InvalidUpdateTestsWithNullInput {
                @DisplayName("Test updating name with null")
                @Test
                void testUpdateNameWithNullName() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateName("0", null));
                }

                @DisplayName("Test updating description with null")
                @Test
                void testUpdateDescriptionWithNullDescription() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateDescription("0", null));
                }
            }

            @Nested
            @DisplayName("Test Update Methods with whitespace")
            class InvalidUpdateTestsWithWhitespace
            {
                @DisplayName("Test updating name with whitespace")
                @Test
                void testUpdateNameWithWhitespace() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateName("0", "                    "));
                }

                @DisplayName("Test updating description with whitespace")
                @Test
                void testUpdateDescriptionWithWhitespace() {
                    // task with id "0" should throw exception
                    assertThrows(IllegalArgumentException.class, () -> taskService.updateDescription("0", "                                                  "));
                }
            }

            @DisplayName("Test updating unknown field")
            @Test
            void testUpdateUnknownField() {
                assertThrows(IllegalArgumentException.class, ()-> taskService.updateEntityField("0", "unknown", "value"));
            }
        }
    }
}