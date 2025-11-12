package io.admin.framework.data.id;

import io.admin.framework.data.DBConstants;
import io.admin.framework.data.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TestEntity1  {


    @Id
    @GeneratePrefixedSequenceId(prefix = "BOOK")
    private String id;

    private String name;
}
