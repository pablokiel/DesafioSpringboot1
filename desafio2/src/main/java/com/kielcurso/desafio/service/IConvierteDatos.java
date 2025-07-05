package com.kielcurso.desafio.service;

public interface IConvierteDatos {

    <T> T obtenerDatos ( String json, Class<T> clase);
}
