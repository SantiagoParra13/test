package com.api.res.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.api.res.exception.ResourceNotFoundException;
import com.api.res.model.Empleado;
import com.api.res.repository.EmpleadoRepository;
import com.api.res.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//indica que se va a trabajar con mockito y carga extensiones de juint
@ExtendWith(MockitoExtension.class)

public class EmpleadoServiceTest {


    private Empleado empleado;

    @BeforeEach
    void setup() {
        empleado = Empleado.builder()
                .id(1)
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();
    }

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado() {
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado))
                .willReturn(empleado);

        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);

        //then
        assertThat(empleadoGuardado).isNotNull();

    }

    @DisplayName("Test para guardar un empleado ConThrowException")
    @Test
    void testGuardarEmpleadoConThrowException() {
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));


        //when
        assertThrows(ResourceNotFoundException.class, () -> {
            empleadoService.saveEmpleado(empleado);
        });

        //then
        verify(empleadoRepository, never()).save(any(Empleado.class));

    }

    @DisplayName("Test para listar a los empleados")
    @Test
    void testListarEmpleados() {
        //given
        Empleado empleado1 = Empleado.builder()
                .id(2)
                .nombre("oliva")
                .apellido("gina")
                .email("rico@correo")
                .build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado, empleado1));
        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();

        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }


    @DisplayName("Test para devolver una lista vacia")
    @Test
    void testListarColeccionEmpleadosVacia() {
        //given
        Empleado empleado1 = Empleado.builder()
                .id(1)
                .nombre("oliva")
                .apellido("gina")
                .email("rico@correo")
                .build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();

        //then
        assertThat(listaEmpleados).isEmpty();
        assertThat(listaEmpleados.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener un empleado por Id")
    @Test
    void testObtenerEmpleadoPorId() {
        //given
        given(empleadoRepository.findById(1)).willReturn(Optional.of(empleado));

        //when
        Empleado empleadoGuardado = empleadoService.getEmppleadoById(empleado.getId()).get();

        //than
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para para actualizar un empleado")
    @Test
    void testActualizarEmpleado() {
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("coore@asdas");
        empleado.setNombre("vas ir");

        //when
        Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);

        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("coore@asdas");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("vas ir");
    }

    @DisplayName("Test para para eliminar un empleado")
    @Test
    void tesEliminarEmpleado() {
        //given
        Integer empleadoId = 1;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId); //hace algo pero no retorna nada

        //when
        empleadoService.eliminarEmpleado(empleadoId);

        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);

    }


}
