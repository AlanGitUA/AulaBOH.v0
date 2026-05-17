CREATE UNIQUE INDEX uk_students_student_username_lower
    ON students (LOWER(student_username))
    WHERE student_username IS NOT NULL;
