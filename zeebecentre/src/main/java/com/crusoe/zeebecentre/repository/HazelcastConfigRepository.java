package com.crusoe.zeebecentre.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.crusoe.zeebecentre.entity.HazelcastConfig;

@Repository
public interface HazelcastConfigRepository extends CrudRepository<HazelcastConfig, String> {}
