package com.Ecomarket.Logistica.client;

public class DetalleVentaClient {



    
        // DTO interno para detalles
        public static class DetalleVentaDTO {
            private String nombreProducto;
            private Long idProducto;
            private Integer cantidad;

            // Getters y setters


            public Long getIdProducto(){
                return idProducto;
            } 
            public void setIdProducto(Long idProducto){
                this.idProducto = idProducto;
            }

            public String getNombreProducto() {
                return nombreProducto;
            }
            public void setNombreProducto(String nombreProducto) {
                this.nombreProducto = nombreProducto;
            }

            public Integer getCantidad() {
                return cantidad;
            }
            public void setCantidad(Integer cantidad) {
                this.cantidad = cantidad;
            }
        }
    }
    

