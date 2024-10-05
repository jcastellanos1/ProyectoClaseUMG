/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.umg.proyectoclaseumg;


//si jala
import com.edu.umg.DTO.persona;
import com.edu.umg.bdd.DMLBdd;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author caste
 */

//@RequestScoped

@ManagedBean(name = "bkn_indexUI")
//@ViewScoped
public class BknIndexUI implements Serializable{
  private String nombre;
  private String apellido;
  private Number telefono;
  private String correo;
  private Number estado;
  private List<persona>list;
  private boolean mostrarDatos = true;

 
    /**
     * @return the list
     */
    public List<persona> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<persona> list) {
        this.list = list;
    }

  public BknIndexUI(){
     
    } 
    

    
    private DMLBdd dml;
    private persona persona;
    private persona personaModify;
    
  @PostConstruct
    public void init() {
        persona = new persona();
        dml = new DMLBdd();
       // cargarDatos(); // Llamada al método para cargar los datos al iniciar
    }
  
public String irAInsertarPersona() {
     
    return "AgregarPersona?faces-redirect=true";
}

    
    
    
public void cargarDatos() {
    try {
        list = dml.listaPersona();
        mostrarDatos = true; // Muestra la tabla después de cargar los datos
    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al cargar datos: " + e.getMessage()));
    }
}
    public boolean isMostrarDatos() {
        return mostrarDatos;
    }
    
    public void cerrarDialogo() {
        mostrarDatos = false; // Oculta el diálogo
    }

   
    // Getters y Setters para persona
    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }
 
 public void buscarPersonaP() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Validar campos vacíos
    boolean nombreVacío = nombre == null || nombre.trim().isEmpty();
    boolean apellidoVacío = apellido == null || apellido.trim().isEmpty();

    if (nombreVacío && apellidoVacío) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
            "Advertencia", "Por favor, complete los campos Nombre y Apellido."));
        return; // Salir del método si ambos campos están vacíos
    }

    if (nombreVacío) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
            "Advertencia", "El campo Nombre no puede estar vacío."));
        return; // Salir del método si solo el nombre está vacío
    }

    if (apellidoVacío) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
            "Advertencia", "El campo Apellido no puede estar vacío."));
        return; // Salir del método si solo el apellido está vacío
    }

    try {
        List<persona> personasEncontradas = dml.buscarPersona(nombre, apellido);
        
        // Verificar si el resultado es null
        if (personasEncontradas == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Error", "Ocurrió un error al buscar personas."));
            return;
        }

        if (personasEncontradas.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "No se encontraron personas", "No se encontraron personas con el nombre " + nombre + " y apellido " + apellido));
        } else {
            setList(personasEncontradas);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Personas encontradas", "Número de personas encontradas: " + personasEncontradas.size()));
        }
    } catch (Exception ex) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
            "Error", "Error al buscar personas: " + ex.getMessage()));
    }
}

  
  
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Number getTelefono() {
        return telefono;
    }

    public void setTelefono(Number telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Number getEstado() {
        return estado;
    }

    public void setEstado(Number estado) {
        this.estado = estado;
    }

    public DMLBdd getDml() {
        return dml;
    }

    public void setDml(DMLBdd dml) {
        this.dml = dml;
    }



 
   
}
