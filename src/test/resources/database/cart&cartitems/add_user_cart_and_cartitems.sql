INSERT INTO book (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Colony', 'Max Kidruk', '9786176798323', 699.00, false);

INSERT INTO category (id, name, description, is_deleted)
VALUES (1, 'Science Fiction', 'Imaginative and futuristic concepts such as space exploration and time travel.', false);

INSERT INTO book_category (book_id, category_id)
VALUES (1, 1);

INSERT INTO users (id, email, password, first_name, last_name)
VALUES (2, 'user@example.com', 'password', 'Name', 'LastName');

INSERT INTO users_role (users_id, role_id)
VALUES (2, 2);

INSERT INTO shopping_cart (user_id, is_deleted)
VALUES (2, false);

INSERT INTO cart_item (id, cart_id, book_id, quantity, is_deleted)
VALUES (1, 2, 1, 5, false);
