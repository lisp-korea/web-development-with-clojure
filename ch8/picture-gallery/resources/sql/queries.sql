-- :name create-user! :! :n
-- creates a new user record
INSERT INTO users
(id, pass)
VALUES (:id, :pass)

-- :name get-user :? :1
-- retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- delete a user given the id
DELETE FROM users
WHERE id = :id

-- :name save-file! :! :n
-- saves a file to the database
INSERT INTO files
(owner, type, name, data)
VALUES (:owner, :type, :name, :data)

-- :name list-thumbnails
-- selects thumbnail names for the given gallery owner
SELECT owner, name FROM files
 WHERE owner = :owner
  AND name LIKE 'thumb\_%'

-- :name get-image :? :1
-- retrieve image data by name
SELECT type, data FROM files
WHERE name = :name
AND owner = :owner
