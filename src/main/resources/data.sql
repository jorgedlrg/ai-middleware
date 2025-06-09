/**
 * Author:  jorge
 * Created: 22 may 2025
 */

/** TODO: Add audit data **/

CREATE TABLE scenario (id UUID);
INSERT INTO scenario VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE session (id UUID, scenario UUID);
INSERT INTO session VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE interaction (id UUID, role UUID, session UUID, timestamp BIGINT, text LONGTEXT, isuser bit, context UUID);

CREATE TABLE context (id UUID, scenario UUID, name TEXT, physical_desc LONGTEXT);
INSERT INTO context VALUES ('af521f08-65f4-4171-9152-8e8e5c229ebf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','the white room','this is an empty white room with one door and one window, in the middle of a forest');