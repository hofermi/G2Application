package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.LoaderInputFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaderInputFileRepository extends JpaRepository<LoaderInputFile, Long> {

}
