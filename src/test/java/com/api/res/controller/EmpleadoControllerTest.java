package com.api.res.controller;

import com.api.res.model.Empleado;
import com.api.res.service.EmpeadoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpeadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("test guardar empleado")
    @Test
    void testGuardarEmpleado() throws Exception {
        //given
        Empleado empleado = Empleado.builder()
                .id(1)
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();
        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados() throws Exception {
        //given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Ramirez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ramirez").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Ramirez").email("a@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));

    }

    @Test
    void testObtenerEmpleadoPorId() throws Exception {
        //given
        Integer empleadoId = 1;

        Empleado empleado = Empleado.builder()
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();
        given(empleadoService.getEmppleadoById(empleadoId)).willReturn(Optional.of(empleado));

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));

    }

    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception {

        Integer empleadoId = 1;
        Empleado empleado = Empleado.builder()
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();
        given(empleadoService.getEmppleadoById(empleadoId)).willReturn(Optional.empty());
        // Configura el comportamiento simulado de 'empleadoService.getEmpleadoById'
        // para que devuelva un 'Optional' vacío cuando se le llame con 'empleadoId'.

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testActulizarEmpleado() throws Exception {
        //given
        Integer empleadoId = 1;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Manuel")
                .apellido("Parra")
                .email("santiasago@correo")
                .build();

        given(empleadoService.getEmppleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleadoActualizado.getEmail())));


    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception {
        //given
        Integer empleadoId = 1;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Santiago")
                .apellido("Vidal")
                .email("santiago@correo")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Manuel")
                .apellido("Parra")
                .email("santiasago@correo")
                .build();

        given(empleadoService.getEmppleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void testEliminarEmpleado() throws Exception {
        //given
        Integer empleadoId = 1;
        willDoNothing().given(empleadoService).eliminarEmpleado(empleadoId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}", empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
