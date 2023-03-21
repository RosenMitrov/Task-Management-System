# Information about endpoints:

## User without roles - GUEST
#### @GetMapping
1. http://localhost:8080/
2. http://localhost:8080/users/login
3. http://localhost:8080/users/register
#### @PostMapping
1. http://localhost:8080/users/login-error
2. http://localhost:8080/users/register

## User with USER role
#### @GetMapping

## User with MODERATOR role
#### @GetMapping

## User with ADMIN role
#### @GetMapping
1. http://localhost:8080/admin/all-users
2. http://localhost:8080/admin/user-details/2 **(2 is user ID)**
3. http://localhost:8080/admin/user-edit/2 **(2 is user ID)**
4. http://localhost:8080/admin/all-departments
5. http://localhost:8080/admin/department-details/2 **(2 is department ID)**

#### @DeleteMapping
1. http://localhost:8080/admin/user-delete/2 **(2 is user ID)**


