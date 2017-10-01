delete from apiuser;
ALTER TABLE apiuser
ADD CONSTRAINT unique_email UNIQUE (email);