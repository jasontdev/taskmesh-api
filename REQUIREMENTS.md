# TASKMESH.XYZ: REQUIREMENTS

## 1. Introduction
Taskmesh is a task management tool for teams. It expands upon the familiar concept of the task or todo list 
by allowing multiple users to interact with each task list. Taskmesh benefits teams by facilitating the 
coordinated and efficient completion of projects.

Organisations will find value in Taskmesh by using it as a tool to streamline communication between team
members working together on projects. Taskmesh moves project coordination from multiple existing communication
channels to a single, streamlined channel purpose-built for coordination. Teams can also find Taskmesh a valuable
tool for reviewing projects once completed as it provides a record of task completion.

## 2. General description
### 2.1 Core features
Users can:
- associate with each other through their contacts list,
- request that users be added to their contacts list,
- remove users from their contact list,
- create tasklists by themselves or with one or more contacts,
- navigate between their tasklists,
- add and complete tasks to a tasklist,
- rename or delete tasks that they have created,
- rename or delete tasklists that they have created

### 2.2. User experience
- The user interface will be a single-page web application.
- Access to the application will be protected by single sign-on.
- The page layout will be comprised of three primary components:
    1. Navigation bar displayed at the top of the page.
    2. Drawer displayed to the left with a button to toggle.
    3. A main content component displayed at the centre of the screen.
- The main content being displayed in the should be reflected by the dynamic route.

## 3. User stories
### 3.1 Authentication
- User logs in using a single sign-on provider.
- User logs out of application.

### 3.1 Contacts
- User requests that another user be added to their list of contacts.
- User receives a request to be added to another user's contact list. The user can 
decline or accept the request.
- User removes a contact from their contacts list.
- User requests another user be added to their contacts list and is informed
that there is already a pending contact list request for that user.

### 3.2. Tasklists
- User selects a tasklist to view. The user can see the tasks and their completion. 
- User views the list of users assigned to a tasklist.
- User creates a tasklist that:
    - has a name,
    - is assigned to zero or more contacts, and
    - has zero or tasks.
- User edits the name of a task that they created.
- User deletes a task that they created.
- User removes themself from a tasklist to which they have been assigned.

### 3.3. Tasks
- User adds a task to a tasklist. The task:
    - has a name,
    - has a completion status.
- User edits the name of a task they creat.
- User deletes a task that they created.
- User changes the completion status of a task they may or may not have created.