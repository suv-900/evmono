package com.project.evm.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.evm.models.dto.EventDTO;
import com.project.evm.models.dto.HostDTO;
import com.project.evm.models.entities.Event;
import com.project.evm.models.entities.Host;

public interface HostRepository extends JpaRepository<Host,Long>{
    @Query(value="SELECT username,email FROM Host WHERE id = :id",nativeQuery=true)
    public Host getHostById(@Param("id")Long id);

    @Query(value="SELECT title,description,date,location FROM Event WHERE hostedBy = :hostID")
    public Event getEventsForHost(@Param("hostID")Long hostID);

    @Query(value="SELECT id,password FROM User WHERE username = :username",nativeQuery=true)
    public Host getPasswordByUsernameWithID(@Param("username")String username);

    @Query(value="SELECT EXISTS(SELECT 1 FROM Host WHERE username = :username OR email = :email) AS host_exists",nativeQuery=true)
    public boolean hostExists(@Param("username")String username,@Param("email")String email);
}
