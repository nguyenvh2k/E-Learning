package com.elearning.repository;

import com.elearning.dto.RoleName;
import com.elearning.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

    @Query(value = "select r.name from role r" +
            " join user_role ur on ur.role_id = r.id " +
            " join users u on u.id = ur.user_id " +
            " where u.id =:idUser",nativeQuery = true)
    List<RoleName> findRoleById(@Param("idUser")Long idUser);
}
