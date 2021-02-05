# BugTracker
Spring Framework Bug Tracker Web Application.

This is a web application that allows users to track issues.

## Role Based Permissions

---

Users logged in have access to different features depending on their account authorities.

### **Submitters**
Able to submit/edit (their own) tickets, leave comments, upload files. (All other roles have these capabilities as well)

### **Developers**
Allowed to take tickets and view all tickets of the project they're currently assigned to.

### **Project Managers**
Allowed to view project members and relevant information pertaining to them. They can delete tickets as well.

### **Admins**
Able to manage all accounts and tickets. Some authorities include the ability to disable user accounts, create new project teams, and assigning users to different teams.

---

## Other Features

The application is secured through Spring Security, using form based authentication and authorization.

Data is stored in a persistent (mySQL) database, which accessed through JPA/Hibernate.
