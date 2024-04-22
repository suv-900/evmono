package com.project.evm.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.evm.models.dto.EventDTO;
import com.project.evm.models.dto.HostDTO;
import com.project.evm.models.entities.Host;

public interface HostRepository extends JpaRepository<Host,Long>{
    @Query(value="SELECT username,email FROM hosts WHERE id = :id",nativeQuery=true)
    public HostDTO getHostById(@Param("id")Long id);

    @Query(value="SELECT title,description,date,location FROM events WHERE hostedBy = :hostID")
    public EventDTO getEventsForHost(@Param("hostID")Long hostID);

    @Query(value="SELECT id,password FROM users WHERE username = :username",nativeQuery=true)
    public Host getPasswordByUsernameWithID(@Param("username")String username);

    @Query(value="SELECT EXISTS(SELECT 1 FROM hosts WHERE username = :username OR email = :email) AS host_exists",nativeQuery=true)
    public boolean hostExists(@Param("username")String username,@Param("email")String email);
}
