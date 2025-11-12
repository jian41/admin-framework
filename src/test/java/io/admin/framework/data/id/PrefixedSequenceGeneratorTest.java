package io.admin.framework.data.id;


import io.admin.common.utils.JsonUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


        Assertions.assertTrue(e.getId().startsWith("BOOK"));

        List list = em.createQuery("select t from TestEntity1 t").getResultList();
        System.out.println(JsonUtils.toJsonQuietly(list));

    }


}
