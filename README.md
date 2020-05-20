# Restaurant voting

**Web application to voting for a restaurant where to have lunch**

The application developed in accordance with the following technical requirements:

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we asume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed.

Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code, README.md with API documentation and couple curl commands to test it.

P.S.: Make sure everything works with latest version that is on GitHub.

Asume that your API will be used by a frontend developer to build frontend on top of that.

---

## Build and Run
Enter in the command line:
```
git clone https://github.com/vkiz/rest-voting
cd rest-voting
mvn package
mvn cargo:run
```

Application is available at the following URL:
```
http://localhost:8080/voting
```

---

## REST API documentation

**Available Users**

| User name          | Password | Role                    |
| ------------------ | -------- | ----------------------- |
| `admin@gmail.com`  | `admin`  | `ROLE_ADMIN, ROLE_USER` |
| `user@gmail.com`   | `user`   | `ROLE_USER`             |

**Available methods**

| Method   | Request URI               | Authorization | Description       |
| -------- | ------------------------- |  ------------ | ----------------- |
| `POST`   | `/admin/restaurants`      | `ROLE_ADMIN`  | Create restaurant |
| `PUT`    | `/admin/restaurants/{id}` | `ROLE_ADMIN`  | Update restaurant |
| `DELETE` | `/admin/restaurants/{id}` | `ROLE_ADMIN`  | Delete restaurant |

---

## CURL examples

Request and Response

**Create restaurant**
```
curl -X POST 'http://localhost:8080/voting/admin/restaurants' --user admin@gmail.com:admin -d '{"name": "New Restaurant"}' -H 'Content-Type:application/json;charset=UTF-8'
```
```json
{
  "id":100031,
  "name":"New Restaurant"
}
```
---

**Update restaurant**
```
curl -X PUT 'http://localhost:8080/voting/admin/restaurants/100031' --user admin@gmail.com:admin -d '{"name": "UpdatedRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8'
```
---

**Delete restaurant**
```
curl -X DELETE 'http://localhost:8080/voting/admin/restaurants/100031' --user admin@gmail.com:admin
```
---

**Documentation is under development...**