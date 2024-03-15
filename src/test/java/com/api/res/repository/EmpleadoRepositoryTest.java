package com.api.res.repository;

import com.api.res.model.Empleado;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup() {
        empleado = Empleado.builder()
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();
    }


    @DisplayName("Test para guargar empleado")
    @Test
    void testGuardarEmpleado() {
        //given se especifica el escenario de precondiciones
        Empleado empleado1 = Empleado.builder()
                .nombre("Facundo")
                .apellido("Lopex")
                .email("facundox@correo")
                .build();

        //when accion o el comportamineto que vamos a probar
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);

        //then verificar la salida
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar empelados")
    @Test
    void testListarEmpleados() {
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Manuel")
                .apellido("Parra")
                .email("manuel@correo")
                .build();
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        //when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para listar empelados por ID")
    @Test
    void testObtenerEmpleadoPorId() {
        //given
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        //then
        assertThat(empleadoBD).isNotNull();

    }

    @DisplayName("Test para actualizar empleado")
    @Test
    void testActualizarEmpleado(){
        //given
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setNombre("Manuel");
        empleadoGuardado.setApellido("Parra");
        empleadoGuardado.setEmail("santiago@correo");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("santiago@correo");
        assertThat(empleadoActualizado.getApellido()).isEqualTo("Parra");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Manuel");
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    void testEliminarEmpleado() {
        //given
        empleadoRepository.save(empleado);

        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        //then
        assertThat(empleadoOptional).isEmpty();


    }

}
