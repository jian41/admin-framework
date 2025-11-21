package io.admin.common.utils;

import io.admin.framework.data.domain.BaseEntity;
import jakarta.annotation.Resource;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;

/**
 * Unit tests for DbUtils.findAll method
 */
@Slf4j
@SpringBootTest
class DbUtilsTest {



    @Resource
    private DbUtils dbUtils;

    @BeforeEach
    void setUp() {
        dbUtils.execute("DROP TABLE IF EXISTS test_user");
        dbUtils.execute("CREATE TABLE test_user (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))");
        for (int i = 0; i < 10; i++) {
            dbUtils.execute("INSERT INTO test_user (name) VALUES ('User " + i + "')");
        }
    }


    @Test
    void testFindAllWithPagination() throws SQLException {
        Pageable pageable = PageRequest.of(0, 1);
        String sql = "SELECT * FROM test_user";
        Page<TestUser> page = dbUtils.findAll(TestUser.class, pageable, sql);
        Assertions.assertEquals("User 0", page.getContent().get(0).getName());

        pageable = PageRequest.of(1, 1);
        page = dbUtils.findAll(TestUser.class, pageable, sql);
        Assertions.assertEquals("User 1", page.getContent().get(0).getName());
    }

    /**
     * Simple entity class for testing purposes
     */

    @Data
    public static class TestUser  {
        private Integer id;
        private String name;
    }
}
