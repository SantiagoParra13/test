package com.api.res.controller;


import com.api.res.model.Empleado;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerWebTestClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void testGuardarEmpleado() {
        //given
        Empleado empleado = Empleado.builder()
                .id(1)
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();

        //when
        webTestClient.post().uri("http://localhost:8080/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleado)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(empleado.getId())
                .jsonPath("$.nombre").isEqualTo(empleado.getNombre())
                .jsonPath("$.apellido").isEqualTo(empleado.getApellido())
                .jsonPath("$.email").isEqualTo(empleado.getEmail());
    }

    @Test
    @Order(2)
    void testObtenerEmpleadoPorID() {
        webTestClient.get().uri("http://localhost:8080/api/empleados/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Santiago")
                .jsonPath("$.apellido").isEqualTo("Vidal")
                .jsonPath("$.email").isEqualTo("santiago@correo");
    }

    @Test
    @Order(3)
    void testListarEmpleados() {
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].nombre").isEqualTo("Santiago")
                .jsonPath("$[0].apellido").isEqualTo("Vidal")
                .jsonPath("$[0].email").isEqualTo("santiago@correo")
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(1));
    }

    @Test
    @Order(4)
    void testObtenerListadodeEmpleados() {
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .consumeWith(response -> {
                    List<Empleado> empleados = response.getResponseBody();
                    Assertions.assertNotNull(empleados);
                    Assertions.assertEquals(1, empleados.size());
                });
    }

    @Test
    @Order(5)
    void testActualizarEmpleado() {
        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Meanuel")
                .apellido("Parra")
                .email("Santiagoas@gamil.cim")
                .build();

        webTestClient.put().uri("http://localhost:8080/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleadoActualizado)
                .exchange()

                //then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(6)
    void testEliminarEmpleado() {
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .hasSize(1);

        webTestClient.delete().uri("http://localhost:8080/api/empleados/1").exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .hasSize(0);

        webTestClient.get().uri("http://localhost:8080/api/empleados/1").exchange()
                .expectStatus().is4xxClientError();

    }

}
