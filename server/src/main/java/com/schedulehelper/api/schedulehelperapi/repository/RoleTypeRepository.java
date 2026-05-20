package com.schedulehelper.api.schedulehelperapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.schedulehelper.api.schedulehelperapi.entity.Employee;
import com.schedulehelper.api.schedulehelperapi.entity.RoleType;

@Repository
public interface RoleTypeRepository extends JpaRepository<RoleType, Integer> {
    /**
     * Finds a role type by its exact title.
     *
     * @param title the title to search by
     * @return the matching role type, if found
     */
    Optional<RoleType> findByTitle(String title);

    /**
     * Finds a role type by partial, case-insensitive title match.
     *
     * @param title partial title
     * @return the matching titles, if found
     */
    @Query(
        value = "SELECT * FROM role_type WHERE LOWER(title) LIKE '%' || LOWER(:title) || '%' ",
        nativeQuery = true
    )
    Optional<Employee> findByPartialTitle(@Param("title") String title);
}
