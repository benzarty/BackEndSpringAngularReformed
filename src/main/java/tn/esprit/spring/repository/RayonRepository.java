package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entity.Rayon;

@Repository
public interface RayonRepository extends JpaRepository<Rayon, Long> {

}
