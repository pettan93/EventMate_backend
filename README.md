# EventMate_backend

### Progress

- [x] Project Architecture
- [x] Models
- [x] Security (login)
- [x] Event CRUD
- [x] Task CRUD
- [x] Permission CRUD
- [x] User CRUD
- [ ] Permission policy
- [ ] Chat system
- [ ] Register via Google / Facebook
- [ ] Report generating
- [ ] File management



### Event endpoint
GET /event | **List all events**

GET /event/{id} | **Get specific event** 

POST /event | **Create new event**

POST /event/{id}/task | **Add task to event**

PUT /event/{id} | **Modify specific event**

GET /me/events | **List of events where is current logged user involved**

GET /me/events/forRole/{userRoleNum} | **List of all events where is current logged user involved, filtered by his role**

### Task endpoint
GET /task | **List all tasks** 

GET /task/{id} | **Get specific task** 

PUT /task/{id} | **Modify specific task**

GET /me/tasks | **List of tasks where is current logged user involved**

GET /me/tasks/forRole/{userRoleNum} | **List of all tasks where is current logged user involved, filtered by his role**

GET /me/event/{id}/tasks | **List of tasks of specific event where is current logged user involved**

GET /me/event/{id}/tasks/forRole/{userRoleNum} | **List of tasks of specific event where is current logged user involved, filtered by his role**

### Permission endpoint
POST /permission/event/{id} | **Add permission to event**

POST /permission/task/{id} | **Add permission to task**

POST /permission/event/{id}/list | **Add a list of permissions to event**

POST /permission/task/{id}/list | **Add a list of permissions permission to task**

### User endpoint

GET /users | **List all users**

GET /users/details/{id} | **Get specific user**

POST /users/register | **Create new user**

 
