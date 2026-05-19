package com.schedulehelper.api.schedulehelperapi.repository;

import com.schedulehelper.api.schedulehelperapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Employee} entities.
 *
 * @see <a href="db/schedulehelper.sql">schedulehelper.sql</a>
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    /**
     * Finds an employee by partial, case-insensitive first and last name match.
     *
     * @param first partial first name
     * @param last  partial last name
     * @return the matching employee, if found
     */
    @Query(
        value = "SELECT * FROM employee "
              + "WHERE LOWER(first_name) LIKE '%' || LOWER(:first) || '%' "
              + "AND LOWER(last_name) LIKE '%' || LOWER(:last) || '%'",
        nativeQuery = true
    )
    Optional<Employee> findByName(@Param("first") String first, @Param("last") String last);
}
