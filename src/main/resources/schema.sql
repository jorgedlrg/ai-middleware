/**
 * Author:  jorge
 * Created: 22 may 2025
 */

-- TODO: Add audit data 
-- Use Liquibase

CREATE TABLE IF NOT EXISTS scenario (id UUID NOT NULL PRIMARY KEY, name TEXT NOT NULL, description LONGTEXT NOT NULL);

CREATE TABLE IF NOT EXISTS role (id UUID NOT NULL PRIMARY KEY, scenario UUID NOT NULL, name TEXT NOT NULL, details LONGTEXT NOT NULL);

CREATE TABLE IF NOT EXISTS session (id UUID NOT NULL PRIMARY KEY, scenario UUID NOT NULL, current_context UUID, locale VARCHAR(5) NOT NULL, last_interaction UUID);

CREATE TABLE IF NOT EXISTS performance (session UUID NOT NULL, role UUID NOT NULL, actor UUID NOT NULL, PRIMARY KEY(session, role));

CREATE TABLE IF NOT EXISTS interaction (id UUID NOT NULL PRIMARY KEY, role UUID NOT NULL, actor UUID NOT NULL, session UUID NOT NULL, timestamp BIGINT NOT NULL, text LONGTEXT NOT NULL, context UUID NOT NULL, parent UUID, mood TEXT, thoughts LONGTEXT, action LONGTEXT);

CREATE TABLE IF NOT EXISTS context (id UUID NOT NULL PRIMARY KEY, scenario UUID NOT NULL, name TEXT NOT NULL, physical_desc LONGTEXT NOT NULL);

CREATE TABLE IF NOT EXISTS actor (id UUID NOT NULL PRIMARY KEY, name TEXT NOT NULL, physical_desc LONGTEXT NOT NULL, current_outfit UUID);

CREATE TABLE IF NOT EXISTS mind (actor UUID NOT NULL PRIMARY KEY, personality LONGTEXT NOT NULL);

CREATE TABLE IF NOT EXISTS outfit (id UUID NOT NULL PRIMARY KEY, name TEXT NOT NULL, description LONGTEXT NOT NULL);

CREATE TABLE IF NOT EXISTS usertable (id UUID NOT NULL PRIMARY KEY, string TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS settings (userid UUID NOT NULL PRIMARY KEY,textgen_provider TEXT NOT NULL, openrouter_apikey TEXT, openrouter_model TEXT, ollama_host TEXT, ollama_model TEXT, actions_enabled BIT NOT NULL, mood_enabled BIT NOT NULL, thoughts_enabled BIT NOT NULL);
