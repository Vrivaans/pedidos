package com.pedidos.codigo.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    @NotNull(message = "El codigo del cliente no puede ser nulo")
    @NotEmpty(message = "El codigo del cliente no puede ser vacío")
    private String codigoCliente;
    @NotNull(message = "El codigo de la empresa no puede ser nulo")
    @NotEmpty(message = "El codigo de la empresa no puede ser vacío")
    private String codigoEmpresa;
    @NotNull(message = "La lista de items no puede ser nulo")
    @NotEmpty(message = "La lista de items no puede estar vacía")
    private Map<String, Integer> listaItems; //Codigo del producto y la cantidad
    @NotNull(message = "La fecha no puede ser nula")
    @NotEmpty(message = "La fecha no puede estar vacía")
    private LocalDate fecha;

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public Map<String, Integer> getListaItems() {
        return listaItems;
    }

    public void setListaItems(Map<String, Integer> listaItems) {
        this.listaItems = listaItems;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}
