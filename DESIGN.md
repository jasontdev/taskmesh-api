# Taskmesh Design Document

## Introduction
### What is Taskmesh?
Taskmesh is a distributed task manager for teams. 


## User stories
### Tasks
- User creates a tasklist. The tasklist has a name.
- User creates a tasks in a tasklist.
- User edits a task.
- User completes/uncompletes a task.
- User deletes a task.

### Teams
- User creates a team. That user is a team leader.
- User invites another user to a team. That user could be a leader or a member.
- Team leader removes a member from team.

## Entities 
- User: humans interacting with the system.
- Team: a collection of users.
- Team member: a user in a team. Has potentially limited ability to interact
with tasks.
- Team leader: a user in a team with control over team members ability to
interact with tasks.
- Task: an entity created, edited and completed by users. A task features: 
    - name,
    - description,
    - creation date,
    - completion status and, 
    - completion status.
- Tasklist: a collection of tasks. Tasklists have many users.
