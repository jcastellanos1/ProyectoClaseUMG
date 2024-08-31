/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.umg.proyectoclaseumg;


//si jala
import com.edu.umg.DTO.PersonaDTO;
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
  private List<PersonaDTO>list;
  private boolean mostrarDatos = true;

 
    /**
     * @return the list
     */
    public List<PersonaDTO> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<PersonaDTO> list) {
        this.list = list;
    }

  public BknIndexUI(){
     
    } 
    

    
    private DMLBdd dml;
    private PersonaDTO persona;
    private PersonaDTO personaModify;
    
  @PostConstruct
    public void init() {
        persona = new PersonaDTO();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isMostrarDatos() {
        return mostrarDatos;
    }
    
    public void cerrarDialogo() {
        mostrarDatos = false; // Oculta el diálogo
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
        List<PersonaDTO> personasEncontradas = dml.buscarPersona(nombre, apellido);
        if (personasEncontradas.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "No se encontraron personas", "No se encontraron personas con el nombre " + nombre + " y apellido " + apellido));
        } else {
            setList(personasEncontradas);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Número de personas encontradas", "Número de personas encontradas: " + personasEncontradas.size()));
        }
    } catch (Exception ex) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
            "Error al buscar personas", "Error al buscar personas: " + ex.getMessage()));
    }
}



 
 
 
 /*
 public void agregarPersona() {
    System.out.println("Método agregarPersona() llamado");
    
    if (validarPersona()) {
        try {
            DMLBdd dmlBdd = new DMLBdd();
            persona.setEstado(1);
            dmlBdd.insertarPersona(persona);

            FacesContext.getCurrentInstance().addMessage(null, 
                
                  new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Persona agregada exitosamente"));
            persona = new PersonaDTO();
            cargarDatos();
            // Resetear el objeto persona para futuras inserciones
            persona = new PersonaDTO();
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al agregar la persona: " + ex.getMessage()));
        }
    } else {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor, complete todos los campos."));
    }
}

private boolean validarPersona() {
    return persona.getNombre() != null && !persona.getNombre().isEmpty()
            && persona.getApellido() != null && !persona.getApellido().isEmpty()
            && persona.getTelefono() != 0
            && persona.getCorreo() != null && !persona.getCorreo().isEmpty();
            
}
  */
   
    // Getters y Setters para persona
    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }
 
 
 /*
 
    public void limpiarCampos() {
        nombre = "";
        apellido = "";
        telefono = 0;
        correo = "";
        estado = 0;
    }
   public void modificarPersona(PersonaDTO personaModificada) {
        try {
            dml.modificarPersona(personaModificada);
            cargarDatos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persona modificada con éxito"));
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar la persona", null));
        }
    }

  public void eliminarPersona(PersonaDTO persona) {
        try {
            dml.eliminarPersona(persona);
            cargarDatos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persona eliminada con éxito"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar la persona", null));
        }
    }
  
      // Método para mostrar el diálogo de modificación
    public void mostrarDialogo(PersonaDTO persona) {
        this.personaModify = persona; // Copia la persona seleccionada para modificarla
    }

    public PersonaDTO getPersonaModify() {
        return personaModify;
    }

    public void setPersonaModify(PersonaDTO personaModify) {
        this.personaModify = personaModify;
    }

  
  */
  
  
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
