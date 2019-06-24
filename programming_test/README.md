# Software Developer test

## Part 1 (String Frame):
Run the solution by running for example
```
node string_frame.js
```
at the root of the directory.

## Part 2: (Spark):
The project can be run in Java IDE and while running, POST requests can be made to http://localhost:4567/hash with the suitable tool of one's choice, such as Postman.

Below are provided some request bodies to try the request with: 
```
{"text": "XXX"}
(Status 200 OK)
{"text": "123456789"}
(Status 200 OK)
{"tect": "XXX"}
(Status 400 Bad request)
{"text":}
(Status 400 Bad request)