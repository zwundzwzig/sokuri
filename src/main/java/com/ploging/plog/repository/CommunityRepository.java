package com.ploging.plog.repository;

import com.ploging.plog.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommunityRepository extends JpaRepository<Community, UUID>, CrudRepository<Community, UUID> {
}
