/**
 * Author:  jorge
 * Created: 22 may 2025
 */

/** TODO: Add audit data **/

CREATE TABLE scenario (id UUID);
INSERT INTO scenario VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE session (id UUID, scenario UUID);
INSERT INTO session VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE interaction (id UUID, role UUID, session UUID, timestamp INT, text LONGTEXT);