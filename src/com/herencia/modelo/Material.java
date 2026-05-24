package com.herencia.modelo;

public class Material {

    private int    materialId;
    private String nombreMaterial;

    public Material(int materialId, String nombreMaterial) {
        this.materialId     = materialId;
        this.nombreMaterial = nombreMaterial;
    }

    public int    getMaterialId()                   { return materialId; }
    public void   setMaterialId(int id)             { this.materialId = id; }

    public String getNombreMaterial()               { return nombreMaterial; }
    public void   setNombreMaterial(String nombre)  { this.nombreMaterial = nombre; }

    @Override
    public String toString() {
        return "Material [" + materialId + "]: " + nombreMaterial;
    }
}
