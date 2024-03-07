CREATE SCHEMA todo_list AUTHORIZATION postgres;

ALTER ROLE postgres SET search_path TO todo_list;
