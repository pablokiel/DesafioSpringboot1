package com.kielcurso.desafio.Principal;

import com.kielcurso.desafio.model.Datos;
import com.kielcurso.desafio.model.DatosLibros;
import com.kielcurso.desafio.service.ConsumoAPI;
import com.kielcurso.desafio.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);


    public void muestraElMenu(){

        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        // top 10 libors mas descargados

        System.out.println("top 10 libors mas descargados");

        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10).map(l->l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Buscar libros por nombre

        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();

        json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()){

            System.out.println("libro Encontrado");
            System.out.println(libroBuscado.get());
        }else{
            System.out.println("Libro no encontrado");
        }

        //trabajando con estadisticas

        DoubleSummaryStatistics est = datosBusqueda.resultados().stream()
                .filter(d -> d.numeroDeDescargas()>0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));

        System.out.println("Cantidad de descargas: "+ est.getAverage());
        System.out.println("Cantidad maxima de descargas: "+ est.getMax());
        System.out.println("Cantidad minima de descargas: "+ est.getMin());
        System.out.println(("Cantidad de registros evaluados para las estadisticas: "+ est.getCount()));



    }


}
