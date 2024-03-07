# TODO

- Integration test can read just the application.conf, fix it to ready the application.yaml instead
- 

# How to run

`docker-compose up -d`

`./gradlew run`


# Intellij scratch pad for manual test

`POST http://127.0.0.1:8080/todos
Content-Type: application/json

{
"title": "manual test on the app",
"done": false
}
`
