---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, `ContactDashboardPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

The following sequence diagram shows how the follow-up date filter is parsed and applied for the command
`find fd/2026-10-20`.

<puml src="diagrams/FollowUpDateSequenceDiagram.puml" alt="Interactions for finding contacts by follow-up date" />


How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

#### Edit command and follow-up dates

`EditCommandParser` validates follow-up dates in the exact `YYYY-MM-DD` format before constructing a `FollowUpDate`, which also rejects invalid calendar dates. An empty `fd/` is treated as an explicit request to clear the existing follow-up date; omitting `fd/` preserves the current date. `EditPersonDescriptor` tracks this explicit clear operation separately from an omitted field.

Contact identity is name-based through `Person#isSamePerson(Person)`, so the edit duplicate check and its error message refer to duplicate names. The edit success message formats the complete updated contact record, including tags, category, remark, follow-up date, and interaction history.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


<box type="info" seamless>

**Note:** The alternative, arguably more object-oriented, design below keeps a unique list of tags in `AddressBook`, and each `Person` references tags from that list. This lets `AddressBook` maintain one `Tag` object per unique tag instead of each `Person` holding its own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />
</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo/redo feature

#### Implementation

The undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:
* `VersionedAddressBook#commit()` -- Saves the current address book state in its history.
* `VersionedAddressBook#undo()` -- Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` -- Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.
</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.
</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.
</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.


### Category/Categories feature

#### Implementation

`category INDEX c/CATEGORY` is parsed by `CategoryCommandParser`. `CategoryCommand` selects the person at the displayed index, creates a copy with the new category, and replaces the original through `Model#setPerson`. Other person details are preserved.

<puml src="diagrams/CategorySequenceDiagram-Logic.puml" width="500" />

<puml src="diagrams/CategorySequenceDiagram-Model.puml" width="250" />

`categories` creates a `CategoriesCommand` directly. It reads the unfiltered person list, counts contacts in every `Category` (including those with zero contacts), and returns the formatted counts without changing the model.

<puml src="diagrams/CategoriesSequenceDiagram-Logic.puml" width="500" />

<puml src="diagrams/CategoriesSequenceDiagram-Model.puml" width="250" />

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is an investment banker working in a fast-paced industry
* has a large number of industry contacts (e.g. private equity buyers, accountants, lawyers)
* needs a centralised place to record details about each contact, such as their company, role, specialisation and where they met
* needs to keep track of past interactions and upcoming follow-ups with contacts
* handles sensitive contact information and prefers it to be stored locally rather than in the cloud
* often needs to export contact and interaction logs for status reports or client presentations
* prefers desktop apps over other types of applications
* can type fast and prefers typing to mouse interactions
* is reasonably comfortable using CLI apps


**Value proposition**: Manage contacts and log interactions locally faster than with a typical mouse-driven GUI application. Log interactions

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …           | I want to …                                                 | So that I can…                                       |
|----------|------------------|-------------------------------------------------------------|------------------------------------------------------|
| `* * *`  | new user         | view a user guide                                           | understand how to use ContactFind                    |
| `* *`    | new user         | learn about the main functions                              | navigate through the features                        |
| `* *`    | new user         | start using the app without much setup                      | begin managing my contacts quickly                   |
| `* * *`  | user             | add a new contact                                           | keep track of people I meet                          |
| `* * *`  | user             | record essential information such as email and phone number | identify and reach people in my contacts easily      |
| `* * *`  | user             | update a contact's information                              | keep my contacts manageable and up to date           |
| `* * *`  | user             | set a follow-up date for a contact                          | avoid missing follow-ups with clients                |
| `* * *`  | user             | find a contact by name, company or role                     | quickly identify relevant contacts                   |
| `* *`    | user             | categorise contacts                                         | keep my contacts organised                           |
| `* *`    | user             | assign one contact to multiple tags                         | represent contacts with different areas of expertise |
| `* * *`  | user             | delete contact with name, email, phone, or index            | keep my contacts updated                             |
| `* *`    | experienced user | bulk delete contacts with same company or role              | quickly keep my contacts relevant                    |
| `* *`    | detail-oriented user | add, update, or remove a remark for a contact            | retain useful context about the relationship         |
| `* * *`  | relationship-focused user | log an interaction with a contact                  | keep a record of my communications                   |
| `* *`    | busy user        | view interactions across my contacts                        | review my recent relationship activity               |
| `* *`    | organised user   | view the number of contacts in each category                | understand how my contacts are distributed           |
| `* *`    | time-conscious user | sort contacts by their follow-up date                    | prioritise the people I need to contact next         |
| `* *`    | user restarting their contact list | clear all contacts                            | start again with an empty contact list               |
| `* *`    | cautious user    | undo or redo a recent change                                | recover from mistakes without re-entering data       |
| `* *`    | cautious user    | save a checkpoint before making changes                     | return to a known contact-list state                 |
| `* *`    | well-connected user | record a contact's manager                               | keep track of reporting relationships                |
| `* *`    | privacy-conscious user | have my contact data saved locally and automatically | keep it private and avoid losing updates             |

### Use cases
(For all use cases below, the **System** is `ContactFind` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a person**

**Guarantees**: The new contact and their details are saved in the contact list.

**MSS**

1. User requests to add a person and provides the person's details.
2. ContactFind validates the provided details.
3. ContactFind adds the person to the contact list.
4. ContactFind informs the user that the person was successfully added.
5. ContactFind displays newly added contact.
Use case ends.

**Extensions**:

* 2a. There is a missing field in the command.

  * 2a1. ContactFind informs the user of the missing information and the required format.
  * 2a2. ContactFind requests for contact details
  * 2a3. User enters new details.
  Steps 2 - 2a3 repeats until successful.

    Use case resumes at step 3.

* 2b. The provided command does not follow the required format.

  * 2b1. System displays correct format to use.
  * 2b2. System requests for contact details.
  * 2b3. User enters command again.
  Steps 2 - 2b3 repeat until successful.

  Use case resumes at step 3.

* 2c. One or more details are invalid.

    * 2c1. ContactFind informs the user of the invalid field and its required format.
    * 2c2. ContactFind requests for contact details.
    * 2c3. User enters correct details.
    Steps 2 to 2c3 repeats until successful.

    Use case resumes at step 3.

* 3a. A contact with the same name already exists in the contact list.

    * 3a1. ContactFind informs the user that the contact already exists.

    Use case ends.

**Use case: Delete a contact**

**MSS**

1. User requests to delete a listed contact.
2. ContactFind deletes the contact and shows a confirmation.

    Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. ContactFind shows an error message.

      Use case ends.

* 1b. User requests to delete a contact by exact name, phone number, or email address.

    * 1b1. ContactFind finds and deletes the matching contact, then shows a confirmation.

      Use case ends.

* 1c. No contact matches specified keywords.

    * 1c1. ContactFind shows an error message.

      Use case ends.

* 1d. User requests to delete contacts by exact role or company.

    * 1d1. ContactFind displays matching contacts as a bulk-delete preview without deleting them.
    * 1d2. User requests `delete all`.
    * 1d3. ContactFind deletes every contact in the preview and shows a confirmation.

      Use case ends.

* 1e. User requests to delete contacts by exact role or company, then deletes one contact in the preview by index, name, phone number, or email address.

    * 1e1. ContactFind deletes that contact and keeps the remaining preview displayed.

      Use case ends.

* 1f. User requests to delete contacts by follow-up date.

    * 1f1. ContactFind deletes contacts in the current displayed list whose follow-up date exactly matches the given date and shows a confirmation.

      Use case ends.

* 1g. The command syntax is invalid, including an unsupported prefix or multiple delete criteria.

    * 1g1. ContactFind shows an error message with the required format.

      Use case ends.

**Use case: Categorize a contact**

MSS

1. User requests to assign a category (e.g. INVESTOR, CLIENT) to a contact.
2. ContactFind updates the contact with the specified category.

    Use case ends.

Extensions

* 1a. The given index is invalid.

   * 1a1. ContactFind shows an error message.

      Use case resumes at step 2.

* 1b. The specified category format or syntax is invalid.

   * 1b1. ContactFind shows an error message.

      Use case resumes at step 2.

**Use case: List categories**

MSS

1. User requests to view category counts.
2. ContactFind shows all categories and the number of contacts in each category.

    Use case ends.

Extensions

* 2a. The contact list is empty.

   * 2a1. ContactFind shows all categories with zero counts.

      Use case ends.

**Use case: Log an interaction**

MSS

1. User requests to log an interaction with a contact.
2. ContactFind adds the interaction to the contact's history.
3. ContactFind saves the updated contact-list state so the interaction can be undone or redone.

    Use case ends.

Extensions

* 1a. The given index is invalid.

   * 1a1. ContactFind shows an error message.

      Use case resumes at step 2.

* 1b. The specified interaction details or command syntax are invalid.

   * 1b1. ContactFind shows an error message.

      Use case resumes at step 2.

**Use case: List interactions**

MSS

1. User requests to view recorded interactions. 
2. ContactFind shows recorded interactions across all contacts.

    Use case ends.

Extensions

* 2a. No interactions have been recorded.

   * 2a1. ContactFind shows a message indicating that no interactions are recorded.

      Use case ends.

**Use case: View usage instructions**

**MSS**

1.  User requests to view usage instructions
2.  ContactFind shows a help window with a link to the user guide
3.  User requests to copy the link
4.  ContactFind copies the link to the clipboard
5.  User opens the user guide in a browser to read the instructions

    Use case ends.

**Extensions**

* 1a. The user enters an unknown command.

    * 1a1. ContactFind shows an error message and suggests using the help command.

      Use case resumes at step 1.

* 2a. The help window is already open.

    * 2a1. ContactFind brings the existing help window to the front.

      Use case resumes at step 3.

**Use case: Set up ContactFind for first use**

**MSS**

1.  User downloads the ContactFind application file
2.  User launches ContactFind
3.  ContactFind starts and shows a list of sample contacts
4.  User requests to clear all sample contacts
5.  ContactFind deletes all contacts and shows an empty contact list

    User can now start adding their own contacts. Use case ends.

**Extensions**

* 2a. The user's computer does not have the required Java version installed.

    * 2a1. ContactFind fails to launch.

      Use case ends.

* 3a. A data file from a previous use already exists.

    * 3a1. ContactFind loads the existing contacts instead of sample contacts.

      Use case ends.

* 3b. The existing data file is corrupted or in an invalid format.

    * 3b1. ContactFind starts with an empty contact list.

      Use case ends.

**Use case: Edit a contact**

**MSS**

1. User requests to list persons
2. ContactFind shows a list of persons
3. User requests to edit a specific person's information
4. ContactFind updates the information for that person

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a The given information to update is invalid

    * 3a1. ContactFind shows an error message

      Use case resumes at step 2.

* 3b. The given index is invalid.

    * 3b1. ContactFind shows an error message.

      Use case resumes at step 2.

**Use case: List contacts**

**MSS**

1. User requests to list contacts.
2. ContactFind displays all contacts.

    Use case ends.

**Use case: Find contacts**

**MSS**

1. User requests to find contacts using a supported field and one or more keywords.
2. ContactFind displays contacts whose selected field matches at least one keyword.

    Use case ends.

**Extensions**

* 1a. The search command has invalid syntax, an unsupported field, or an invalid follow-up date.

    * 1a1. ContactFind shows an error message with the required format.

      Use case ends.

* 2a. No contacts match the search.

    * 2a1. ContactFind displays an empty contact list.

      Use case ends.

**Use case: Add or remove a remark**

**MSS**

1. User requests to set a remark for a listed contact.
2. ContactFind replaces the contact's remark and confirms the update.

    Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. ContactFind shows an error message.

      Use case ends.

* 1b. User omits the remark, or provides an empty remark.

    * 1b1. ContactFind removes the contact's existing remark and confirms the update.

      Use case ends.

**Use case: Sort contacts by follow-up date**

**MSS**

1. User requests to sort contacts by follow-up date.
2. ContactFind orders the displayed contacts by follow-up date in the requested direction, placing contacts without a follow-up date last.

    Use case ends.

**Extensions**

* 1a. The sort field or direction is invalid.

    * 1a1. ContactFind shows an error message with the required format.

      Use case ends.

**Use case: Clear all contacts**

**MSS**

1. User requests to clear all contacts.
2. ContactFind removes all contacts and displays an empty contact list.

    Use case ends.

**Use case: Save an undo checkpoint**

**MSS**

1. User requests to save a checkpoint using.
2. ContactFind saves the current contact-list state.

    Use case ends.

**Use case: Undo a contact-list change**

**MSS**

1. User requests to undo the most recent saved change.
2. ContactFind restores the preceding contact-list state, including any logged interaction, and displays it.

    Use case ends.

**Extensions**

* 1a. There is no saved change to undo.

    * 1a1. ContactFind shows an error message.

      Use case ends.

**Use case: Redo an undone contact-list change**

**MSS**

1. User requests to redo the most recently undone change.
2. ContactFind restores the redone contact-list state, including any logged interaction, and displays it.

    Use case ends.

**Extensions**

* 1a. There is no undone change to redo.

    * 1a1. ContactFind shows an error message.

      Use case ends.



### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `25` or above installed.
2.  Should be able to hold up to 1000 persons without noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Any command being executed through the CLI should complete within 100 miliseconds with up to 1000 persons in the address book.
5. The application should be ready to use within 5 seconds of launch on a typical modern computer with a mainstream OS.
6. The system should be usable by a new user without any training or prior experience with the system.
7. The application must store all data in a human-readable format.
8. The application store all data on local disk, and not on any remote server or cloud service.
9. The application should not require an internet connection to function.
10. The data stored locally should not exceed 10MB for 1000 persons in the address book.
11. RAM usage should not exceed 150MB for 1000 persons in the address book.
12. Write operations to the data file should be atomic, if the application crashes during a write operation, the data file should not be corrupted.
13. The application should have zero external dependencies, and should not require any installation of additional software or libraries.

*{More to be added}*

* **API (Application Programming Interface)**: A set of specifications, rules, and methods that components use to communicate and interact with each other without exposing internal implementation details.
* **Architecture**: The high-level structure of the application, defining its major components (`UI`, `Logic`, `Model`, `Storage`) and the rules governing interactions between them.
* **CLI (Command Line Interface)**: A text-based user interface where users execute operations by typing textual commands instead of interacting via mouse clicks.
* **Command**: An executable action initiated by the user (such as `add`, `delete`, `undo`, `redo`) that modifies the state of the model or queries stored information.
* **FXML**: An XML-based markup language provided by JavaFX to define the structure and layout of user interface views separately from application logic.
* **GUI (Graphical User Interface)**: A visual display interface through which users interact with the app via windows, buttons, text fields, and panels.
* **JavaFX**: The open-source client application platform and GUI framework used to build the AB-3 desktop interface.
* **JSON (JavaScript Object Notation)**: A lightweight, human-readable text format used by the `Storage` component to persist address book data and user preferences to disk.
* **Mainstream OS**: Windows, Linux, Unix, or macOS operating systems capable of running Java standard runtime environments.
* **MSS (Main Success Scenario)**: The straightforward path in a use case where the user achieves their goal without encountering errors or edge cases.
* **ObservableList**: A list implementation in JavaFX that notifies attached UI listeners when elements change, enabling real-time UI data binding.
* **Parser**: A logic component responsible for decomposing and validating raw text commands entered by the user into structured `Command` objects.
* **PlantUML**: A tool and domain-specific language used to generate software diagrams (such as class, sequence, and activity diagrams) from plain-text descriptions.
* **Private contact detail**: A contact attribute (e.g., specific tags, unlisted phone number) intended for restricted access rather than general sharing.
* **Sequence Diagram**: A UML interaction diagram depicting how components and objects interact across time via method invocations.
* **State**: A snapshot of all active `Person` entries and settings held in memory at a specific point in time.
* **UiPart**: The base abstract class in the UI layer that encapsulates common behavior for visible GUI elements and links them to corresponding `.fxml` files.
* **VersionedAddressBook**: An extension of `AddressBook` that maintains a history of past application states (`addressBookStateList`) and a pointer (`currentStatePointer`) to support undo and redo operations.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.
</box>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

### Adding contacts

1. Prerequisites: Ensure that no existing contact is named Farid Noor.

2. Test case: `add n/Farid Noor p/90000006 e/farid.noor@example.com a/6 Fern Lane rl/Advisor com/Aurora Partners b/Farid Noor`<br>
   Expected: The command is rejected because a contact cannot be their own boss; no contact is added.

### Editing contacts

1. Prerequisites: Run the preparation commands in [Deleting contacts](#deleting-contacts), then enter `list`.

2. Test case: `edit 1 b/Blair Lim`<br>
   Expected: Alex's boss is set to Blair Lim.

3. Test case: `edit 2 b/Alex Tan`<br>
   Expected: The command is rejected because the update would create a circular management chain; Blair's boss remains unchanged.

### Deleting contacts

1. Prepare predictable contacts. Enter each command separately:

   ```text
   clear
   add n/Alex Tan p/90000001 e/alex.tan@example.com a/1 Alpha Road rl/Analyst com/Orion Bank fd/2026-10-20
   add n/Blair Lim p/90000002 e/blair.lim@example.com a/2 Beta Road rl/Analyst com/Orion Bank fd/2026-10-20
   add n/Casey Ng p/90000003 e/casey.ng@example.com a/3 Cedar Road rl/Associate com/Zenith Group fd/2026-10-21
   add n/Drew Koh p/90000004 e/drew.koh@example.com a/4 Delta Road rl/Analyst com/Zenith Group fd/2026-10-22
   add n/Evan Goh p/90000005 e/evan.goh@example.com a/5 Elm Road rl/Director com/Nova Capital fd/2026-10-25
   ```

   Expected: Five contacts are displayed. The data deliberately provides two
   matching-company contacts, an additional `Analyst`, and distinct follow-up dates.

2. Test case: `delete com/Orion Bank`, then `delete p/90000003`.<br>
   Expected: The second command is rejected because Casey is outside the active preview. Alex and Blair remain displayed and unchanged.

3. Test case: after restoring the prepared contacts, enter `find rl/Analyst`, then `delete fd/2026-10-20`.<br>
   Expected: Alex and Blair are deleted. Drew remains, proving that date-based deletion applies only to contacts in the current filtered list.

4. Test case: restore the prepared contacts, enter `delete com/Orion Bank`, then `delete all`.<br>
   Expected: Exactly two contacts are deleted, the preview is cleared, and the status message reports the deleted count.

### Recording interactions

1. Prerequisites: Restore the prepared contacts above, then enter `find n/Blair`.

2. Test case: `interaction 1 d/2026-12-01 ty/EMAIL`<br>
   Expected: A success message confirms an EMAIL interaction with Blair on `2026-12-01`.

3. Test case: enter `find n/Casey`, then `interactions`.<br>
   Expected: The interaction list still includes Blair's email interaction even though Blair is hidden by the current filter.

### Categorizing contacts

1. Prerequisites: Restore the prepared contacts above and enter `list`.

2. Test case: `category 3 c/CONSULTANT`<br>
   Expected: Casey's category changes to `CONSULTANT`, and the status message confirms the change.

3. Test case: repeat `category 3 c/CONSULTANT`.<br>
   Expected: The status message reports that Casey is already in that category; no duplicate category is created.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases … }_
