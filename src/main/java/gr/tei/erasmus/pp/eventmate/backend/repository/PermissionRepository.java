package gr.tei.erasmus.pp.eventmate.backend.repository;

import gr.tei.erasmus.pp.eventmate.backend.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findPermissionsByUserId(Long userId);

}
