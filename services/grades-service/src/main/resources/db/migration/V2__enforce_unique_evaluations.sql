CREATE UNIQUE INDEX uk_evaluations_identity_lower
    ON evaluations (LOWER(course), LOWER(subject), LOWER(title), evaluation_date)
    WHERE evaluation_date IS NOT NULL;
