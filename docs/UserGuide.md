---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# ContactFind User Guide

ContactFind is a **desktop contact manager for investment bankers**. It helps you keep track of clients, investors, lawyers, and other professional contacts, including each contact's role, company, and boss. ContactFind is **optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, ContactFind can help you manage contacts faster than traditional GUI applications.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2627S1-CS2103T-F12-2/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for ContactFind.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 rl/Analyst com/Example Bank` : Adds a contact named `John Doe`.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items followed by `...` can appear zero or more times.<br>
  For example, `[t/TAG]... ` may be omitted, or written as `t/friend` or `t/friend t/family`.

* Parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* `INDEX` refers to the index number shown in the displayed contact list. It **must be a positive integer** 1, 2, 3, ...

* Extraneous parameters for commands that take no parameters, such as `help`, `list`, `categories`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Prefix reference

Use a prefix immediately before its value. Not every prefix is accepted by every command; refer to each command's format.

Prefix | Field | Used by
-------|-------|--------
`n/` | Name | `add`, `edit`, `find`
`p/` | Phone number | `add`, `edit`
`e/` | Email address | `add`, `edit`
`a/` | Address | `add`, `edit`
`rl/` | Role | `add`, `edit`, `find`
`com/` | Company | `add`, `edit`, `find`
`b/` | Boss | `add`, `edit`
`t/` | Tag | `add`, `edit`
`r/` | Remark | `remark`
`c/` | Category | `category`

### Field constraints

ContactFind checks every value you enter. If a value does not meet its constraint, the command is rejected and an error message is shown.

Field | Constraint | Valid example
------|------------|--------------
Name | Letters only, with at least a first and last name separated by a space. When using `edit`, the name must be exactly two words. | `John Doe`
Phone number | Digits only, at least 8 digits long. | `98765432`
Email address | Must be in the form `local-part@domain`. | `johnd@example.com`
Address | Any value, but must not be blank. | `John street, block 123, #01-01`
Role | Letters and digits, with words separated by single spaces. | `Vice President`
Company | Letters and digits, with words separated by single spaces, and at least 3 characters long. | `Example Bank`
Boss | Same as Name, or empty. | `Alex Smith`
Tag | Letters and digits only, with no spaces. | `priority`
Remark | Any value. | `Prefers email over calls`
Category | One of `UNCATEGORIZED`, `CLIENT`, `INVESTOR`, `LAWYER`, `ACCOUNTANT`, `CONSULTANT`, `OTHER` (case-insensitive). | `INVESTOR`

<box type="info" seamless>

**Note:** Two contacts with exactly the same name are treated as duplicates. ContactFind will not let you add a contact, or edit a contact's name, if another contact already has that name.
</box>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a contact: `add`

Adds a contact to ContactFind.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS rl/ROLE com/COMPANY [b/BOSS] [t/TAG]...`

* Name, phone number, email address, address, role, and company are required.
* Boss and tags are optional.
* New contacts start in the `UNCATEGORIZED` category. Use [`category`](#categorizing-a-contact-category) to change it.

<box type="tip" seamless>

**Tip:** You can provide any number of tags.
To assign multiple tags, repeat the `t/` prefix.
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 rl/CEO com/ABC Bank`
* `add n/Mary Smith p/12345678 e/betsycrowe@example.com a/ABC Street rl/CFO com/Hello Company b/Alex Smith t/client t/priority`

### Listing all contacts: `list`

Shows a list of all contacts in ContactFind.

Format: `list`

### Editing a contact: `edit`

Edits an existing contact in ContactFind.

Format: `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [rl/ROLE] [com/COMPANY] [b/BOSS] [t/TAG]...`

* Edits the contact at the specified `INDEX`.
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* To remove a contact's boss, enter `b/` without a name after it.
* When editing tags, all of the contact's existing tags are removed and replaced with the tags you enter; adding tags this way is not cumulative.
* To remove all of a contact's tags, enter `t/` without a tag after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st contact to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd contact to be `Betsy Crower` and clears all existing tags.
*  `edit 3 rl/Managing Director com/Example Bank b/` Updates the role and company of the 3rd contact and removes their boss.

### Adding a remark to a contact: `remark`

Adds, replaces, or removes the remark of an existing contact.

Format: `remark INDEX [r/REMARK]`

* Sets the remark of the contact at the specified `INDEX`.
* The contact's existing remark is replaced by the new one.
* To remove a contact's remark, enter `remark INDEX` or `remark INDEX r/` without any text.

Examples:
* `remark 1 r/Prefers email over calls` sets the remark of the 1st contact.
* `remark 1` removes the remark of the 1st contact.

### Categorizing a contact: `category`

Places an existing contact into a category.

Format: `category INDEX c/CATEGORY`

* Categorizes the contact at the specified `INDEX`.
* `CATEGORY` must be one of: `UNCATEGORIZED`, `CLIENT`, `INVESTOR`, `LAWYER`, `ACCOUNTANT`, `CONSULTANT`, `OTHER`.
* Category names are case-insensitive.
* Each contact can have only one category. Categorizing a contact replaces that contact's current category.

Examples:
* `category 1 c/INVESTOR` categorizes the 1st contact in the displayed list as an investor.
* `category 2 c/UNCATEGORIZED` removes the 2nd contact's specific category.

### Listing all categories: `categories`

Shows all available categories and the number of contacts assigned to each category.

Format: `categories`

### Finding contacts: `find`

Finds contacts whose name, role, or company contains any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]...` or `find n/KEYWORD [MORE_KEYWORDS]...` or `find rl/KEYWORD [MORE_KEYWORDS]...` or `find com/KEYWORD [MORE_KEYWORDS]...`

* Without a prefix, or with `n/`, the search looks at names. With `rl/`, it looks at roles. With `com/`, it looks at companies.
* The search is case-insensitive; for example, `hans` matches `Hans`.
* Partial words match; for example, `Han` matches `Hans`.
* Keyword order does not matter.
* Contacts matching at least one keyword are returned (an `OR` search); for example, `find Hans Bo` returns `Hans Gruber` and `Bo Yang`.
* Only one field can be searched at a time. If you enter more than one prefix, only the first of `n/`, `rl/`, `com/` (in that order) is used.

Examples:
* `find John` returns `John Doe` and `Johnny Tan`.
* `find alex david` returns `Alex Yeoh` and `David Li`.<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)
* `find rl/analyst` returns every contact whose role contains `analyst`.
* `find com/example` returns every contact whose company contains `example`.

### Deleting a contact: `delete`

Deletes the specified contact from ContactFind.

Format: `delete INDEX`

* Deletes the contact at the specified `INDEX`.

Examples:
* `list` followed by `delete 2` deletes the 2nd contact in ContactFind.
* `find Betsy` followed by `delete 1` deletes the 1st contact in the results of the `find` command.

### Clearing all entries: `clear`

Clears all contacts from ContactFind.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

ContactFind automatically saves data after every command. You do not need to save manually.

### Editing the data file

ContactFind data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes make the data file invalid, ContactFind starts with an empty contact list at the next run. The invalid file remains on disk until you run a command (ContactFind saves after every command). Still, we recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause ContactFind to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the data file it creates with the data file from your previous ContactFind home folder.

**Q**: How do I add a tag without losing a contact's existing tags?<br>
**A**: The `edit` command replaces all of a contact's existing tags, so include every tag you want to keep. For example, if a contact is tagged `client`, use `edit INDEX t/client t/priority` to add `priority`.

**Q**: Why was my phone number rejected?<br>
**A**: Phone numbers must contain digits only and be at least 8 digits long. Remove spaces, dashes, and the `+` country code prefix. See [Field constraints](#field-constraints).

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS rl/ROLE com/COMPANY [b/BOSS] [t/TAG]...` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 rl/Analyst com/Example Bank t/client`
**Categories** | `categories`
**Category** | `category INDEX c/CATEGORY`<br> e.g., `category 1 c/INVESTOR`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [rl/ROLE] [com/COMPANY] [b/BOSS] [t/TAG]...`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`
**Exit**   | `exit`
**Find**   | `find KEYWORD [MORE_KEYWORDS]...`, `find n/KEYWORD...`, `find rl/KEYWORD...`, `find com/KEYWORD...`<br> e.g., `find James Jake`, `find com/example`
**Help**   | `help`
**List**   | `list`
**Remark** | `remark INDEX [r/REMARK]`<br> e.g., `remark 1 r/Prefers email over calls`
