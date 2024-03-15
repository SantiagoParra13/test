package com.api.res.controller;

import com.api.res.model.Empleado;
import com.api.res.service.EmpeadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpeadoService empeadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado guardarEmpleado(@RequestBody  Empleado empleado){
        return empeadoService.saveEmpleado(empleado);
    }

    @GetMapping()
     public List<Empleado> listarEmpleados(){
        return empeadoService.getAllEmpleados();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable("Id")Integer empleadoId){
        return empeadoService.getEmppleadoById(empleadoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{Id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@RequestBody Empleado empleado,@PathVariable("Id")Integer empleadoId){
        return empeadoService.getEmppleadoById(empleadoId)
                .map(empleadoGuardado ->{
                    empleadoGuardado.setNombre(empleado.getNombre());
                    empleadoGuardado.setApellido(empleado.getApellido());
                    empleadoGuardado.setEmail(empleado.getEmail());

                    Empleado empleadoActualizado = empeadoService.updateEmpleado(empleadoGuardado);

                    return  new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
                })

                .orElseGet(() ->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<String> eliminarEmplado(@PathVariable("Id")Integer empleadoId){
        empeadoService.eliminarEmpleado(empleadoId);

        return new  ResponseEntity<String>("Empleado Eliminado" ,HttpStatus.OK);
    }


}
