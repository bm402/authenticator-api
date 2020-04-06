package com.bncrypted.authenticator.repository;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;

public interface RoleDao {

    @SqlUpdate(
            "INSERT INTO user_roles " +
            "SELECT id AS user_id, :role AS role " +
            "FROM users " +
            "WHERE username = :username"
    )
    void addUserRole(String username, String role);

    @SqlQuery(
            "SELECT ur.role " +
            "FROM users u, user_roles ur " +
            "WHERE u.username = :username " +
            "AND u.id = ur.user_id"
    )
    Set<String> getUserRoles(String username);

}
