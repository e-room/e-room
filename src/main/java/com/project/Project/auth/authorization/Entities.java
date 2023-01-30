package com.project.Project.auth.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Entities {
    private final EntityManager em = null;

    private Set<EntityType<?>> entities;

    @PostConstruct
    public void init() {
        this.entities = this.em.getMetamodel().getEntities();
    }

    public Set<EntityType<?>> getEntityTypeSets() {
        return this.entities;
    }

    public List<EntityType<?>> getEntityTypeLists() {
        return new ArrayList<>(this.entities);
    }

    public Optional<Object> findEntityClass(String entityName) {
        return this.entities.stream().filter((entityType) -> entityType.getName().equalsIgnoreCase(entityName)).findFirst()
                .map(Type::getJavaType);
    }

    public <T extends Class> Optional<? extends Class<?>> findEntityClass(T entityClass) {
        return this.entities.stream().map(Type::getJavaType).filter((entityType) -> entityType.equals(entityClass)).findFirst();
    }
}
