package com.api.res.service.impl;

import com.api.res.exception.ResourceNotFoundException;
import com.api.res.model.Empleado;
import com.api.res.repository.EmpleadoRepository;
import com.api.res.service.EmpeadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpeadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Override
    public Empleado saveEmpleado(Empleado empleado) {
        Optional<Empleado> empleadoGuardado = empleadoRepository.findByEmail(empleado.getEmail());
        if(empleadoGuardado.isPresent()){
            throw new ResourceNotFoundException("El empleado con ese email ya existe: " + empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> getEmppleadoById(Integer Id) {
        return empleadoRepository.findById(Id);
    }

    @Override
    public Empleado updateEmpleado(Empleado empleadoActualizado) {
        return empleadoRepository.save(empleadoActualizado);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
    empleadoRepository.deleteById(id);
    }
}
