package com.api.res.repository;

import com.api.res.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado,Integer> {

    Optional<Empleado> findByEmail(String email);
}
