RESTfulService
==============
Example of RESTful service based on Spring MVC, security, aop.

1. use /users POST to create new user
2.  a) use login?j_username=admin&j_password=admin POST to login as admin   
    b) use login?j_username={user email}&j_password={password} POST to login as use
3. use /users GET to get all users
4. user /users/{user_id} PUT to change use if you are logged in
5. use /users/{user_id} DELETE to delete use if you are logged in
6. use /logout to logout
7. use /forgotPassword?userId={user_id} to get password on email

You can register new users under admin account. 
1. Login using admin username/password from config.properties
2. Create account for new user
3. Now you can login under new user or use forgot password (need to configure gmail account before)
 
Also, in this project aspects are used to restrict permissions to some services.
