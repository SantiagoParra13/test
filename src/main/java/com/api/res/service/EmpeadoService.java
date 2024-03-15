package com.api.res.service;

import com.api.res.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpeadoService {

    Empleado saveEmpleado (Empleado empleado);

    List<Empleado> getAllEmpleados();

    Optional<Empleado> getEmppleadoById(Integer Id);

    Empleado updateEmpleado(Empleado empleadoActualizado);

    void eliminarEmpleado(Integer id);
}
