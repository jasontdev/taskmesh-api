# taskmesh-api architecture

## 1. Introduction
[taskmesh-api](http://github.com/jasontdev/taskmesh-api) is a RESTful backend for the [taskmesh](https://taskmesh.xyz) 
application. The backend is accessed by the [taskmesh-web](https://github.com/jasontdev/taskmesh-web) front-end.

## 2. Entities
- A User has:
    - an id (corresponds with "sub" claim on JWT)
    - a name
    - contacts: a list of Users
    - tasklists: a list of Tasklist
- A Tasklist has:
    - an id
    - tasks: a list of Task
    - users: a list of User
- A Task has:
    - an id
    - isComplete: a boolean completion status
    - a name
    - a tasklist

### 2.1. Relationships
- A User has a many to many relationship with other Users via the contacts list.
- A User has a many to many relationship with Tasklists via the tasklists list.
- A Tasklist has many to one relationship with Tasks via the tasks list.
- A Task has a one to many relationship with Tasklists via the tasklist field.
