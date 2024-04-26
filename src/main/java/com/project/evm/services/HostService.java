package com.project.evm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.dao.HostDao;
import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.HostNotFoundException;
import com.project.evm.models.entities.Host;

import jakarta.transaction.Transactional;

@Service
public class HostService {
   
    @Autowired
    private PasswordHasher hasher;

    @Autowired
    private HostDao hostDao;

    @Transactional
    public Long addHost(Host host)throws Exception{
        String hashedPassword = hasher.hashPassword(host.getPassword());
        host.setPassword(hashedPassword);
        return hostDao.addHost(host);
    }
   
    public Host retrieveHostById(Long hostID)throws HostNotFoundException,Exception{
        Host host = hostDao.retrieveHostById(hostID);
        if(host == null){
            throw new HostNotFoundException();
        }
        return host;
    } 
    
    public Host updateHost(Host host)throws Exception{
        return hostDao.updateHost(host);
    }
    
    public void removeHost(Host host)throws Exception{
        hostDao.removeHost(host); 
    }
    
    public Long loginHost(String name,String password)throws HostNotFoundException,
        CredentialsDontMatchException,Exception
    {
        Optional<Object[]> resultOp = hostDao.loginHost(name);
        
        if(resultOp.isEmpty()){
            throw new HostNotFoundException();
        }

        Object[] result = resultOp.get();
        Long hostID = (Long)result[0];
        String dbPassword = (String)result[1];

        hasher.comparePassword(dbPassword,password);
        
        return hostID;
    }

    public Host getHostFullDetails(Long hostID)throws Exception{
        Host host = hostDao.getHostFullDetails(hostID);
        if(host == null){
            throw new HostNotFoundException();
        }
        return host;
    }

    public List<Host> getHostFullDetailsList(List<Long> hostIDs)throws Exception{
       return hostDao.getHostFullDetailsList(hostIDs); 
    }
    
    public boolean exists(String name,String email)throws Exception{
       return hostDao.exists(name,email); 
    }
    
}
