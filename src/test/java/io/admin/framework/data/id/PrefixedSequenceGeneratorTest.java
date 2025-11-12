package io.admin.framework.data.id;


import io.admin.common.utils.JsonUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PrefixedSequenceGeneratorTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    public void test() {
        TestEntity1 e = new TestEntity1();
        e.setName("OK");
        em.persist(e);
        System.out.println("生成的id为 " + e.getId());
        System.out.println(JsonUtils.toJsonQuietly(e));


        Assertions.assertTrue(e.getId().startsWith("BOOK"));

    }


}
