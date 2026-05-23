package com.schedulehelper.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.entity.ShiftRole;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ShiftRoleRepository}.
 */
@Tag("repository")
@DataJpaTest
public class ShiftRoleRepositoryIT {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShiftRoleRepository shiftRoleRepository;

    // - Shift setup -

    @Autowired
    private ShiftRepository shiftRepository;

    private final static Map<String, Shift> shifts = new HashMap<>();

    private void setupShifts() {
        shifts.put("Sun", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 17, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 17, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
        shifts.put("Mon", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 18, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 18, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
        shifts.put("Tue", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 19, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 19, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
        shifts.put("Wed", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 20, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 20, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
        shifts.put("Thu", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 21, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 21, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
        shifts.put("Fri", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 22, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 22, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
        shifts.put("Sat", shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 23, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 23, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        )));
    }

    // - Employee setup -

    @Autowired
    private EmployeeRepository employeeRepository;

    private final static Map<String, Employee> employees = new HashMap<>();

    private void setupEmployees() {
        employees.put("John Smith", employeeRepository.save(new Employee("John", "Smith")));
        employees.put("Jane Doe", employeeRepository.save(new Employee("Jane", "Doe")));
        employees.put("Alex", employeeRepository.save(new Employee("Alex")));
        employees.put("Steve", employeeRepository.save(new Employee("Steve")));
    }

    // - RoleType setup -

    @Autowired
    private RoleTypeRepository roleTypeRepository;

    private final static Map<String, RoleType> roleTypes = new HashMap<>();

    private void setupRoleTypes() {
        roleTypes.put("PA", roleTypeRepository.save(new RoleType("PA")));
        roleTypes.put("Director", roleTypeRepository.save(new RoleType("Director")));
        roleTypes.put("Camera #1", roleTypeRepository.save(new RoleType("Camera #1")));
    }

    // - automated setup -

    @BeforeEach
    void setUp() {
        this.shiftRepository.deleteAll();
        this.employeeRepository.deleteAll();
        this.roleTypeRepository.deleteAll();
        this.shiftRoleRepository.deleteAll();

        jdbcTemplate.execute("ALTER TABLE shift      ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE employee   ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE role_type  ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE shift_role ALTER COLUMN id RESTART WITH 1");

        setupShifts();
        setupEmployees();
        setupRoleTypes();

        // 1-3
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Mon"), employees.get("John Smith"), roleTypes.get("Director")));
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Mon"), employees.get("Jane Doe"), roleTypes.get("PA")));
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Mon"), employees.get("Alex"), roleTypes.get("Camera #1")));

        // 4-5
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Tue"), employees.get("John Smith"), roleTypes.get("Director")));
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Tue"), employees.get("Alex"), roleTypes.get("Camera #1")));

        // 6-7
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Wed"), employees.get("John Smith"),roleTypes.get("Director")));
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Wed"), employees.get("Alex"), roleTypes.get("Camera #1")));

        // 8-10
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Fri"), employees.get("John Smith"), roleTypes.get("Director")));
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Fri"), employees.get("Jane Doe"), roleTypes.get("Camera #1")));
        this.shiftRoleRepository.save(new ShiftRole(shifts.get("Fri"), employees.get("Alex"), roleTypes.get("PA")));
    }

    // --- findByShift ---

    static Stream<Arguments> matchFindByShift() {
        return Stream.of(
            Arguments.of("three people all roles Mon", "Mon", Arrays.asList(1, 2, 3)),
            Arguments.of("two people two roles Tue", "Tue", Arrays.asList(4, 5))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchFindByShift")
    void findByShift_returnsShiftRole(
        final String description,
        final String shiftKey,
        final List<Integer> contains
    ) {
        assertThat(this.shiftRoleRepository.findByShift(shifts.get(shiftKey)))
            .extracting(ShiftRole::getId)
            .containsAll(contains);
    }

    static Stream<Arguments> noMatchFindByShift() {
        return Stream.of(
            Arguments.of("no people no roles Sun", "Sun")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchFindByShift")
    void findByShift_noMatch_returnsEmpty(
        final String description,
        final String shiftKey
    ) {
        assertThat(this.shiftRoleRepository.findByShift(shifts.get(shiftKey))).isEmpty();
    }

    // --- findByEmployee ---

    static Stream<Arguments> matchFindByEmployee() {
        return Stream.of(
            Arguments.of("John 4 shifts", "John Smith", Arrays.asList(1, 4, 6, 8)),
            Arguments.of("Jane 2 shifts", "Jane Doe", Arrays.asList(2, 9))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchFindByEmployee")
    void findByEmployee_returnsShiftRole(
        final String description,
        final String employeeKey,
        final List<Integer> contains
    ) {
        assertThat(this.shiftRoleRepository.findByEmployee(employees.get(employeeKey)))
            .extracting(ShiftRole::getId)
            .containsAll(contains);
    }

    static Stream<Arguments> noMatchFindByEmployee() {
        return Stream.of(
            Arguments.of("Never scheduled", "Steve")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchFindByEmployee")
    void findByEmployee_noMatch_returnsEmpty(
        final String description,
        final String employeeKey
    ) {
        assertThat(this.shiftRoleRepository.findByEmployee(employees.get(employeeKey))).isEmpty();
    }

    // --- findByEmployeeAndRoleType ---
    
    static Stream<Arguments> matchFindByEmployeeAndRoleType() {
        return Stream.of(
            Arguments.of("John 4 shifts director", "John Smith", "Director", Arrays.asList(1, 4, 6, 8)),
            Arguments.of("Jane 1 shift PA", "Jane Doe", "PA", Arrays.asList(2)),
            Arguments.of("Alex 3 shifts Cam 1", "Alex", "Camera #1", Arrays.asList(3, 5, 7))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchFindByEmployeeAndRoleType")
    void findByEmployeeAndRoleType_returnsShiftRole(
        final String description,
        final String employeeKey, final String roleTypeKey,
        final List<Integer> contains
    ) {
        assertThat(this.shiftRoleRepository.findByEmployeeAndRoleType(employees.get(employeeKey), roleTypes.get(roleTypeKey)))
            .extracting(ShiftRole::getId)
            .containsAll(contains);
    }

    static Stream<Arguments> noMatchFindByEmployeeAndRoleType() {
        return Stream.of(
            Arguments.of("Never Direct", "Alex", "Director"),
            Arguments.of("Never scheduled", "Steve", "PA")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchFindByEmployeeAndRoleType")
    void findByEmployeeAndRoleType_noMatch_returnsEmpty(
        final String description,
        final String employeeKey, final String roleTypeKey
    ) {
        assertThat(this.shiftRoleRepository.findByEmployeeAndRoleType(employees.get(employeeKey), roleTypes.get(roleTypeKey))).isEmpty();
    }
}
