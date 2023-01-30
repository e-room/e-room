package com.project.Project.auth.authorization;


import com.project.Project.auth.enums.Access;
import com.project.Project.auth.enums.Action;
import com.project.Project.auth.enums.DomainType;
import com.project.Project.auth.enums.ResourceType;

/**
 * Policy는 Resource, Effect 구성됩니다.
 * 해당 엔티티 또는 객체에 대해 어떻게 할 수 있는지를 기술합니다.
 * Ex) (DomainType, id, Access, Action)
 * Ex) (Review,null,Allow,Create), (Review,null,Allow,Delete)
 * Ex) (Review, 1000, Deny, Update)
 * Resource: Entity 전체나 특정 엔티티 instance 하나
 * Effect: Resource에 대해 어떤 행위를 할 수 있는지를 기술합니다.
 */
public class Policy {

    private ResourceType resourceType;

    /**
     * Resource
     */
    private DomainType entityType;
    private Long entityId;


    /**
     * Effect
     */
    private Access access;
    private Action action;
}
