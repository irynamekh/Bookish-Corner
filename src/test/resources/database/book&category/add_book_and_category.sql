INSERT INTO book (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Colony', 'Max Kidruk', '9786176798323', 699.00, false);

INSERT INTO category (id, name, description, is_deleted)
VALUES (1, 'Science Fiction', 'Imaginative and futuristic concepts such as space exploration and time travel.', false);

INSERT INTO book_category (book_id, category_id)
VALUES (1, 1);
