package com.schedulehelper.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.entity.ShiftRole;

@Repository
public interface ShiftRoleRepository extends JpaRepository<ShiftRole, Integer> {
    /**
     * Finds all shift roles for a given shift.
     * Used to retrieve the full team assigned to a shift.
     *
     * @param shift the shift to search by
     * @return all shift roles associated with the given shift
     */
    List<ShiftRole> findByShift(Shift shift);

    /**
     * Finds all shift roles for a given employee.
     * Used to retrieve an employee's full shift history for performance analysis.
     *
     * @param employee the employee to search by
     * @return all shift roles associated with the given employee
     */
    List<ShiftRole> findByEmployee(Employee employee);

    /**
     * Finds all shift roles for a given employee in a specific role.
     * Used to retrieve role-specific history for skill calculation.
     *
     * @param employee the employee to search by
     * @param roleType the role type to filter by
     * @return all shift roles matching the given employee and role type
     */
    List<ShiftRole> findByEmployeeAndRoleType(Employee employee, RoleType roleType);
}
